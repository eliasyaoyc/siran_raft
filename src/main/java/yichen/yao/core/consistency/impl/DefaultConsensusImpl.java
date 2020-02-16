package yichen.yao.core.consistency.impl;

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
    public VoteResponse sendVoteRequest(VoteRequest request) {
        return null;
    }

    /**
     * 发送附加日志、心跳请求
     * @param request
     * @return
     */
    @Override
    public AppendEntriesResponse sendAppendEntriesRequest(AppendEntriesRequest request) {
        return null;
    }

    @Override
    public InstallSnapshotResponse sendInstallSnapshotRequest(InstallSnapshotRequest request) {
        return null;
    }

}
