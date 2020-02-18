package yichen.yao.core.rpc.protocol.response;

import lombok.Data;
import yichen.yao.core.common.constants.RequestType;
import yichen.yao.core.rpc.protocol.RpcResponse;

/**
 * @Author: siran.yao
 * @time: 2020/2/11:下午6:20
 */
@Data
public class VoteResponse extends RpcResponse {
    /**
     * 候选人赢得了此张选票时为真
     */
    private boolean voteGranted;

    public VoteResponse() {
        setRequestType(RequestType.VOTE_RESPONSE);
    }
}
