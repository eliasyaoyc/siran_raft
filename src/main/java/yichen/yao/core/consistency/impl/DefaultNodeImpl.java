package yichen.yao.core.consistency.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yichen.yao.core.common.enums.NodeEnum;
import yichen.yao.core.common.threadpool.RaftThreadPool;
import yichen.yao.core.config.NodeConfig;
import yichen.yao.core.consistency.Consensus;
import yichen.yao.core.consistency.Node;
import yichen.yao.core.entity.LogEntry;
import yichen.yao.core.rpc.RpcClient;
import yichen.yao.core.rpc.RpcServer;
import yichen.yao.core.rpc.protocol.request.AppendEntriesRequest;
import yichen.yao.core.rpc.protocol.request.ClientRequest;
import yichen.yao.core.rpc.protocol.request.InstallSnapshotRequest;
import yichen.yao.core.rpc.protocol.request.VoteRequest;
import yichen.yao.core.rpc.protocol.response.AppendEntriesResponse;
import yichen.yao.core.rpc.protocol.response.ClientResponse;
import yichen.yao.core.rpc.protocol.response.InstallSnapshotResponse;
import yichen.yao.core.rpc.protocol.response.VoteResponse;
import yichen.yao.core.rpc.remoting.netty.client.NettyClient;
import yichen.yao.core.rpc.remoting.netty.server.NettyServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: siran.yao
 * @time: 2020/2/15:下午1:35
 * 节点的实现
 * 遵循原则：广播时间(broadcastTime) << 选举超时时间(electionTimeout) << 平均故障间隔时间(MTBF)
 */
public class DefaultNodeImpl implements Node {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultNodeImpl.class);

    private NodeConfig nodeConfig;
    private Consensus consensus;
    private RpcClient rpcClient;

    private RaftThreadPool raftThreadPool = RaftThreadPool.INSTANCE;

    private HeartBeatTask heartBeatTask = new HeartBeatTask();
    private ElectionTask electionTask = new ElectionTask();

    private boolean started;

    private volatile long electionTime = 15 * 1000;
    private volatile long preElectionTIme = 0;
    private long heartBeatInterval = 5 * 1000;
    private volatile long prevHeartBeatTime = 0;

    //node state  init(follower)
    private volatile int nodeState = NodeEnum.Follower.getCode();

    //----------------   持久存在 --------------------
    /**
     * 服务器最后一次知道的任期号（初始化为 0，持续递增）
     */
    private int currentTerm;
    /**
     * 在当前获得选票的候选人的 Id
     */
    private String votedFor;

    /**
     * 日志条目集；每一个条目包含一个用户状态机执行的指令，和收到时的任期号
     */
    private List<LogEntry> log;

//----------------   所有服务器上经常变的 --------------------
    /**
     * 已知的最大的已经被提交的日志条目的索引值
     */
    private long commitIndex;
    /**
     * 最后被应用到状态机的日志条目索引值（初始化为 0，持续递增）
     */
    private long lastApplied;

//----------------   在领导人里经常改变的 （选举后重新初始化）--------------------
    /**
     * 对于每一个服务器，需要发送给他的下一个日志条目的索引值（初始化为领导人最后索引值加一）
     */
    private int[] nextIndex;
    /**
     * 对于每一个服务器，已经复制给他的日志的最高索引值
     */
    private int[] matchIndex;

    /**
     * 大致流程：
     * 原则：相同term，先来先服务
     * 1 服务启动至为follower状态，等待接受rpc (leader发送的心跳，candidate发送的投票)
     * 1.1 如果在这段时间内没有接受到任何rpc，那么认为整个集群中没有leader，那么term+1转换为candidate状态并且发送投票rpc竞选成为leader
     * 1.2 如果candidate 获得集群中大多数节点的认可那么成为leader
     * 1.3 如果在其他节点成为了leader那么他会接收到leader节点发送过来的心跳rpc，那么candidate转换成follower
     * 1.4 如果在同一个term下有多个candidate竞争并且获得的票数相同那么没有candidate可以成为leader，然后超时，继续发送vote请求
     */
    @Override
    public void init() {
        if (started) {
            return;
        }
        synchronized (this) {
            if (started) {
                return;
            }
            consensus = new DefaultConsensusImpl(this);
            //获取当前节点配置 开启通信
            RpcServer rpcServer = new NettyServer(nodeConfig.getHost(), nodeConfig.getPort());
            rpcClient = new NettyClient("", 1);
            try {
                rpcServer.startServer();
            } catch (InterruptedException e) {
                LOGGER.info("服务器启动失败" + e.toString());
            }
            raftThreadPool.scheduleWithFixedDelay(heartBeatTask, 500);
            raftThreadPool.scheduleAtFixedRate(electionTask, 6000, 500);

            started = true;
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public void setConfig(NodeConfig config) {
        this.nodeConfig = config;
    }

    @Override
    public VoteResponse handlerVoteRequest(VoteRequest voteRequest) {
        return consensus.voteRequest(voteRequest);
    }

    @Override
    public AppendEntriesResponse handleAppendEntriesRequest(AppendEntriesRequest appendEntriesRequest) {
        return consensus.appendEntriesRequest(appendEntriesRequest);
    }

    @Override
    public InstallSnapshotResponse handleInstallSnapshotRequest(InstallSnapshotRequest installSnapshotRequest) {
        return consensus.installSnapshotRequest(installSnapshotRequest);
    }

    @Override
    public ClientResponse handlerClientRequest(ClientRequest clientRequest) {
        return null;
    }


    class HeartBeatTask implements Runnable {

        @Override
        public void run() {
            //只有leader才能发送心跳rpc
            if (nodeState != NodeEnum.Leader.getCode())
                return;

            //是否满足心跳时间间隔
            long curTime = System.currentTimeMillis();
            if (curTime - prevHeartBeatTime >= heartBeatInterval) {
                for (String peer : nodeConfig.getOtherNodeList()) {
                    //note: 此处使用线程池是提升性能给其他节点并发发送心跳
                    raftThreadPool.execute(() -> {
                        AppendEntriesResponse response = (AppendEntriesResponse) rpcClient.sendRequest(composeHeartBeatRequest(peer));
                        //follower 和 leader 不在同一任期中： leader发生宕机重新上线或者其他情况导致  重新竞选了leader  那么转换为follower
                        if (currentTerm < response.getTerm())
                            nodeState = NodeEnum.Follower.getCode();
                    });
                }
                //设置上次发送心跳时间
                prevHeartBeatTime = curTime;
            }
        }
    }

    class ElectionTask implements Runnable {
        @Override
        public void run() {
            if (nodeState == NodeEnum.Leader.getCode())
                return;
            long curTime = System.currentTimeMillis();
            electionTime = electionTime + ThreadLocalRandom.current().nextInt(50);
            if (curTime - preElectionTIme < electionTime)
                return;

            nodeState = NodeEnum.Candidate.getCode();

            preElectionTIme = System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(200);

            currentTerm++;

            votedFor = nodeConfig.getHost() + nodeConfig.getPort();

            //只有candidate才可以发起vote请求
            if (nodeState != NodeEnum.Candidate.getCode())
                return;

            List<String> otherNodeList = nodeConfig.getOtherNodeList();
            if (otherNodeList == null && otherNodeList.size() <= 0)
                return;
            List<Future> futureList = new ArrayList<>();
            for (String peer : otherNodeList) {
                futureList.add(  raftThreadPool.submit(new Callable() {
                    @Override
                    public Object call() throws Exception {
                        return rpcClient.sendRequest(composeVoteRequest(peer));
                    }
                }));
            }
            CountDownLatch countDownLatch = new CountDownLatch(futureList.size());
            AtomicInteger voteCount = new AtomicInteger(1);
            for (Future f : futureList){
                raftThreadPool.submit(new Callable() {
                    @Override
                    public Object call() throws Exception {
                        try {
                            VoteResponse res = (VoteResponse) f.get(3000, TimeUnit.MILLISECONDS);
                            if (res == null)
                                return -1;
                            if(res.isVoteGranted())
                                voteCount.incrementAndGet();
                            else {
                                if(currentTerm <= res.getTerm())
                                    currentTerm = res.getTerm();
                            }
                            return 0;
                        } catch (Exception e) {
                            LOGGER.error(" get future error, cause : {}",e);
                            return -1;
                        }finally {
                            countDownLatch.countDown();
                        }
                    }
                });
            }

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                LOGGER.error("Interrupted by master thread , cause : {}",e);
            }

            int count = voteCount.get();
            LOGGER.info("node {} maybe become leader , success count = {} , status : {}", nodeConfig.getHost()+":"+nodeConfig.getPort(), count,nodeState);
            if(nodeState == NodeEnum.Follower.getCode())
                return;
            if (count >= otherNodeList.size() / 2){
                LOGGER.info("node {} become leader",nodeConfig.getHost()+":"+nodeConfig.getPort());
                nodeState = NodeEnum.Leader.getCode();
                votedFor = "";
            }else {
                votedFor = "";
            }
        }
    }

    private AppendEntriesRequest composeHeartBeatRequest(String peer) {
        return AppendEntriesRequest.builder()
                .leaderId(peer)
                .preLogIndex(0)
                .prevLogTerm(0)
                .entries(null) //heartbeat 条目为空
                .leaderCommit(0)
                .term(currentTerm)
                .build();
    }

    private VoteRequest composeVoteRequest(String peer) {
        LogEntry logEntry = log.get(log.size());
        return VoteRequest.builder()
                .candidateId(peer)
                .lastLogIndex(logEntry == null ? 0 : logEntry.getLogIndex())
                .lastLogTerm(logEntry == null ? 0 : logEntry.getLogTerm())
                .term(currentTerm)
                .build();
    }
}
