package yichen.yao.core.consistency.impl.log;

import yichen.yao.core.consistency.impl.DefaultConsensusImpl;
import yichen.yao.core.rpc.protocol.request.AppendEntriesRequest;
import yichen.yao.core.rpc.protocol.response.AppendEntriesResponse;

/**
 * @Author: siran.yao
 * @time: 2020/2/15:下午12:32
 * 日志复制实现
 */
public class LogReplicationImpl extends DefaultConsensusImpl {

    @Override
    public AppendEntriesResponse sendAppendEntriesRequest(AppendEntriesRequest request) {
        return super.sendAppendEntriesRequest(request);
    }
}
