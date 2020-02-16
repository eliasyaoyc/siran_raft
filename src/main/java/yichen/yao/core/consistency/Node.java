package yichen.yao.core.consistency;

import yichen.yao.core.config.NodeConfig;
import yichen.yao.core.rpc.protocol.request.AppendEntriesRequest;
import yichen.yao.core.rpc.protocol.request.ClientRequest;
import yichen.yao.core.rpc.protocol.request.InstallSnapshotRequest;
import yichen.yao.core.rpc.protocol.request.VoteRequest;
import yichen.yao.core.rpc.protocol.response.AppendEntriesResponse;
import yichen.yao.core.rpc.protocol.response.ClientResponse;
import yichen.yao.core.rpc.protocol.response.InstallSnapshotResponse;
import yichen.yao.core.rpc.protocol.response.VoteResponse;

/**
 * @Author: siran.yao
 * @time: 2020/2/15:下午12:37
 */
public interface Node {
    /**
     * 初始化当前节点
     */
    void init();

    /**
     * 关闭当前节点
     */
    void destroy();

    /**
     * 设置集群中节点信息
     * @param config
     */
    void setConfig(NodeConfig config);

    /**
     * 节点处理投票请求
     * @param voteRequest
     * @return
     */
    VoteResponse handlerVoteRequest(VoteRequest voteRequest);

    /**
     * 节点处理附加日志请求
     * @return
     */
    AppendEntriesResponse handleAppendEntriesRequest(AppendEntriesRequest appendEntriesRequest);

    /**
     * 节点处理日志快照请求
     * @param installSnapshotRequest
     * @return
     */
    InstallSnapshotResponse handleInstallSnapshotRequest(InstallSnapshotRequest installSnapshotRequest);

    /**
     * 节点处理客户端发来的数据请求,如果不是leader节点 那么要转发给leader节点
     * @param clientRequest
     * @return
     */
    ClientResponse handlerClientRequest(ClientRequest clientRequest);


}
