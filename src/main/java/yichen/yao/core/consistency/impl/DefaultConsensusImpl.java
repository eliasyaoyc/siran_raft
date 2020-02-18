package yichen.yao.core.consistency.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yichen.yao.core.consistency.Consensus;
import yichen.yao.core.rpc.protocol.request.AppendEntriesRequest;
import yichen.yao.core.rpc.protocol.request.InstallSnapshotRequest;
import yichen.yao.core.rpc.protocol.request.VoteRequest;
import yichen.yao.core.rpc.protocol.response.AppendEntriesResponse;
import yichen.yao.core.rpc.protocol.response.InstallSnapshotResponse;
import yichen.yao.core.rpc.protocol.response.VoteResponse;

/**
 * @Author: siran.yao
 * @time: 2020/2/15:下午12:28
 * 默认的一致性实现
 */
public class DefaultConsensusImpl implements Consensus {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConsensusImpl.class);

    private DefaultNodeImpl defaultNode;

    public DefaultConsensusImpl(DefaultNodeImpl defaultNode) {
        this.defaultNode = defaultNode;
    }

    /**
     * 发送投票请求
     * @param request
     * @return
     */
    @Override
    public VoteResponse voteRequest(VoteRequest request) {
        LOGGER.info("handle vote request {}",request);
        return new VoteResponse();
    }

    /**
     * 发送附加日志、心跳请求
     * @param request
     * @return
     */
    @Override
    public AppendEntriesResponse appendEntriesRequest(AppendEntriesRequest request) {
        LOGGER.info("handle append entries request {}",request);
        return new AppendEntriesResponse();
    }

    @Override
    public InstallSnapshotResponse installSnapshotRequest(InstallSnapshotRequest request) {
        LOGGER.info("handle install snapshot request {}",request);
        return new InstallSnapshotResponse();
    }

}
