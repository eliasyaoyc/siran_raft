package yichen.yao.core.rpc.protocol.response;

import lombok.Data;
import yichen.yao.core.common.constants.RequestType;
import yichen.yao.core.rpc.protocol.RpcResponse;

/**
 * @Author: siran.yao
 * @time: 2020/2/15:下午12:48
 */
@Data
public class ClientResponse extends RpcResponse {
    private int sequenceNumber;
    private boolean success;

    public ClientResponse() {
    }

    public ClientResponse(int sequenceNumber, boolean success) {
        this.sequenceNumber = sequenceNumber;
        this.success = success;
        setRequestType(RequestType.CLIENT_RESPONSE);
    }
}
