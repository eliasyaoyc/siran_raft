package yichen.yao.core.consistency.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yichen.yao.core.common.enums.NodeEnum;
import yichen.yao.core.consistency.Consensus;
import yichen.yao.core.entity.LogEntry;
import yichen.yao.core.rpc.protocol.request.AppendEntriesRequest;
import yichen.yao.core.rpc.protocol.request.InstallSnapshotRequest;
import yichen.yao.core.rpc.protocol.request.VoteRequest;
import yichen.yao.core.rpc.protocol.response.AppendEntriesResponse;
import yichen.yao.core.rpc.protocol.response.InstallSnapshotResponse;
import yichen.yao.core.rpc.protocol.response.VoteResponse;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: siran.yao
 * @time: 2020/2/15:下午12:28
 * 默认的一致性实现
 */
public class DefaultConsensusImpl implements Consensus {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConsensusImpl.class);

    private DefaultNodeImpl node;

    public DefaultConsensusImpl(DefaultNodeImpl defaultNode) {
        this.node = defaultNode;
    }

    private ReentrantLock lock = new ReentrantLock();

    /**
     * 接收投票请求并且处理
     *
     * @param request
     * @return 遵守：
     * 1.如果commitIndex > lastApplied，那么就 lastApplied 加一，并把log[lastApplied]应用到状态机中（5.3 节）
     * 2.如果接收到的 RPC 请求或响应中，任期号T > currentTerm，那么就令 currentTerm 等于 T，并切换状态为跟随者
     * 接收者处理流程：
     * 1.如果voteRequest中的term < curTerm 直接返回false 不投票
     * 2.如果voteFor 字段为空或者就是voteRequest中的candidateId，并且判断这个candidate的日志是否和自己一样新 是的话就投票给他返回true 否则返回false
     * note：选举限制：
     * 1.voteRequest中包含了candidate的日志信息。查看是否包含自己的日志信息，不包含则会拒绝
     * 2.通过比较两份日志中的最后一条日志条目的索引值和任期号定义谁的日志比较新
     * 3.任期号相同，日志索引较长的更新
     * 4.任期号不同，任期号大的更新
     */
    @Override
    public VoteResponse voteRequest(VoteRequest request) {
        LOGGER.info("handle vote request {}", request);
        if (node.nodeState != NodeEnum.Candidate.getCode()) {
            if (node.nodeState == NodeEnum.Leader.getCode())
                if (request.getTerm() > node.currentTerm) {
                    node.currentTerm = request.getTerm();
                    node.nodeState = NodeEnum.Follower.getCode();
                }

            if (request.getTerm() >= node.currentTerm) {
                if (node.votedFor.equals("") || node.votedFor == null ||
                        node.votedFor.equals(request.getCandidateId())) {

                    LogEntry log = node.logManager.getLast();
                    if (log != null) {
                        if (request.getLastLogTerm() > log.getLogIndex() ||
                                request.getLastLogTerm() == log.getLogIndex() && request.getLastLogIndex() > log.getLogIndex()) {
                            node.votedFor = request.getCandidateId();
                            return composeVoteResponse(node.currentTerm, true);
                        }
                    } else {
                        node.votedFor = request.getCandidateId();
                        return composeVoteResponse(node.currentTerm, true);
                    }
                }
            }
        }
        return composeVoteResponse(node.currentTerm, false);
    }

    /**
     * 接收附加日志、心跳请求并且处理
     *
     * @param request
     * @return 1.如果 term < currentTerm 就返回 false
     * 2.如果日志在 prevLogIndex 位置处的日志条目的任期号和 prevLogTerm 不匹配，则返回 false
     * 3.如果已经存在的日志条目和新的产生冲突（索引值相同但是任期号不同），删除这一条和之后所有的
     * 4.附加日志中尚未存在的任何新条目
     * 5.如果 leaderCommit > commitIndex，令 commitIndex 等于 leaderCommit 和 新日志条目索引值中较小的一个
     */
    @Override
    public AppendEntriesResponse appendEntriesRequest(AppendEntriesRequest request) {
        LOGGER.info("handle append entries request {}", request);
        try {
            if (lock.tryLock()) {
                if (request.getTerm() >= node.currentTerm) {
                    if (node.nodeState != NodeEnum.Follower.getCode()) {
                        node.nodeState = NodeEnum.Follower.getCode();
                        node.currentTerm = request.getTerm();
                    }

                    if (request.getEntries() == null && request.getEntries().size() < 0) {
                        node.prevHeartBeatTime = System.currentTimeMillis();
                        node.preElectionTIme = System.currentTimeMillis();
                        node.nodeConfig.setLeaderIp(request.getLeaderId());
                        //心跳
                        return composeAppendEntriesResponse(node.currentTerm, true);
                    }

                    //附加日志
                    if (request.getPrevLogIndex() != 0) {
                        if (node.logManager.getLastIndex() != 0) {
                            LogEntry entry = node.logManager.read(request.getPrevLogIndex());
                            if (entry != null) {
                                if (entry.getLogTerm() == request.getTerm()) {
                                    //请求的任期号不对
                                    return composeAppendEntriesResponse(node.currentTerm, false);
                                }
                            } else {
                                //请求的index不对 需要leader 把index - 1 返回false
                                return composeAppendEntriesResponse(node.currentTerm, false);
                            }
                        }
                    }
                    // 如果已经存在的日志条目和新的产生冲突（索引值相同但是任期号不同），删除这一条和之后所有的
                    LogEntry existLog = node.logManager.read(request.getPrevLogIndex() + 1);
                    if (existLog != null && existLog.getLogTerm() != request.getEntries().get(0).getLogTerm()) {
                        // 删除这一条和之后所有的
                        node.logManager.removeIndex(request.getPrevLogIndex() + 1);
                    } else if (existLog != null) {
                        // 已存在 不用重复写入
                        return composeAppendEntriesResponse(node.currentTerm, true);
                    }

                    // 写进日志
                    for (LogEntry entry : request.getEntries()) {
                        node.logManager.write(entry);
                    }

                    //比较leader 已提交的日志 和本地已提交的日志
                    if (request.getLeaderCommit() >= node.commitIndex) {
                        for (long i = node.commitIndex + 1; i <= request.getLeaderCommit(); i++) {
                            LogEntry entry = node.logManager.read(i);
                            node.stateMachine.apply(entry);
                            node.commitIndex = i;
                            node.lastApplied = i;
                        }
                    }
                }
            }
            return composeAppendEntriesResponse(node.currentTerm, true);
        } catch (Exception e) {
            LOGGER.error("encounter unknown exception {}", e);
            return composeAppendEntriesResponse(node.currentTerm, false);
        } finally {
            lock.unlock();
        }

    }

    /**
     * 接收快照请求并且处理
     *
     * @param request
     * @return
     */
    @Override
    public InstallSnapshotResponse installSnapshotRequest(InstallSnapshotRequest request) {
        LOGGER.info("handle install snapshot request {}", request);
        return new InstallSnapshotResponse();
    }


    private VoteResponse composeVoteResponse(int term, boolean voteGranted) {
        return VoteResponse.builder()
                .term(term)
                .voteGranted(voteGranted)
                .build();
    }

    private AppendEntriesResponse composeAppendEntriesResponse(int term, boolean success) {
        return AppendEntriesResponse.builder()
                .success(success)
                .term(term)
                .build();
    }
}
