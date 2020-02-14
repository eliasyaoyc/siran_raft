package yichen.yao.core.rpc.protocol.response;

import lombok.Data;
import yichen.yao.core.rpc.protocol.RpcRequest;

/**
 * @Author: siran.yao
 * @time: 2020/2/11:下午6:20
 */
@Data
public class VoteResponse extends RpcRequest {
    /**
     * 候选人赢得了此张选票时为真
     */
    private boolean voteGranted;
}
