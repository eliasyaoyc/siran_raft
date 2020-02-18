package yichen.yao.core.consistency.impl.log;

import yichen.yao.core.consistency.impl.DefaultConsensusImpl;
import yichen.yao.core.consistency.impl.DefaultNodeImpl;
import yichen.yao.core.rpc.protocol.request.AppendEntriesRequest;
import yichen.yao.core.rpc.protocol.response.AppendEntriesResponse;

/**
 * @Author: siran.yao
 * @time: 2020/2/15:下午12:32
 * 日志复制实现
 */
public class LogReplicationImpl extends DefaultConsensusImpl {


    public LogReplicationImpl(DefaultNodeImpl defaultNode) {
        super(defaultNode);
    }

    @Override
    public AppendEntriesResponse appendEntriesRequest(AppendEntriesRequest request) {
        return super.appendEntriesRequest(request);
    }
}
