package yichen.yao.core.consistency;

import yichen.yao.core.rpc.protocol.request.AppendEntriesRequest;
import yichen.yao.core.rpc.protocol.request.InstallSnapshotRequest;
import yichen.yao.core.rpc.protocol.request.VoteRequest;
import yichen.yao.core.rpc.protocol.response.AppendEntriesResponse;
import yichen.yao.core.rpc.protocol.response.InstallSnapshotResponse;
import yichen.yao.core.rpc.protocol.response.VoteResponse;

/**
 * @Author: siran.yao
 * @time: 2020/2/14:下午8:03
 */
public interface Consensus {

    /**
     * 请求投票 rpc
     * @param request
     * @return
     */
    VoteResponse voteRequest(VoteRequest request);

    /**
     * 附加日志 ，心跳  rpc
     * @param request
     * @return
     */
    AppendEntriesResponse appendEntriesRequest(AppendEntriesRequest request);

    /**
     * 快照 rpc
     * @param request
     * @return
     */
    InstallSnapshotResponse installSnapshotRequest(InstallSnapshotRequest request);
}
