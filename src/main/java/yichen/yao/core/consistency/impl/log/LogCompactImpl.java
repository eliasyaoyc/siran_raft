package yichen.yao.core.consistency.impl.log;

import yichen.yao.core.consistency.impl.DefaultConsensusImpl;
import yichen.yao.core.consistency.impl.DefaultNodeImpl;
import yichen.yao.core.rpc.protocol.request.InstallSnapshotRequest;
import yichen.yao.core.rpc.protocol.response.InstallSnapshotResponse;

/**
 * @Author: siran.yao
 * @time: 2020/2/15:下午12:29
 * 日志压缩实现
 */
public class LogCompactImpl extends DefaultConsensusImpl {

    public LogCompactImpl(DefaultNodeImpl defaultNode) {
        super(defaultNode);
    }

    @Override
    public InstallSnapshotResponse installSnapshotRequest(InstallSnapshotRequest request) {
        return super.installSnapshotRequest(request);
    }

}
