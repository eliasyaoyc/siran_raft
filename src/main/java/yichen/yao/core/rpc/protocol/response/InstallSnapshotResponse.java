package yichen.yao.core.rpc.protocol.response;

import lombok.Data;
import yichen.yao.core.common.constants.RequestType;
import yichen.yao.core.rpc.protocol.RpcResponse;

/**
 * @Author: siran.yao
 * @time: 2020/2/11:下午6:25
 */
@Data
public class InstallSnapshotResponse extends RpcResponse {

    public InstallSnapshotResponse() {
        setRequestType(RequestType.INSTALL_SNAPSHOT_RESPONSE);
    }
}
