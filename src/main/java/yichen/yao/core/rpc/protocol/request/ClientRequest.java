package yichen.yao.core.rpc.protocol.request;

import lombok.Data;
import yichen.yao.core.common.constants.RequestType;
import yichen.yao.core.rpc.protocol.RpcRequest;

/**
 * @Author: siran.yao
 * @time: 2020/2/15:下午12:48
 */
@Data
public class ClientRequest extends RpcRequest {
    private long logIndex;
    private String command;

    public ClientRequest() {
    }

    public ClientRequest(long logIndex, String command) {
        this.logIndex = logIndex;
        this.command = command;
        setRequestType(RequestType.CLIENT_REQUEST);
    }
}
