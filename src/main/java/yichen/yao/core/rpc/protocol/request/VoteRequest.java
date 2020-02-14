package yichen.yao.core.rpc.protocol.request;

import lombok.Data;
import yichen.yao.core.rpc.protocol.RpcRequest;

/**
 * @Author: siran.yao
 * @time: 2020/2/11:下午6:20
 */
@Data
public class VoteRequest extends RpcRequest {
    /**
     * 请求选票的候选人Id
     */
    private int candidateId;

    /**
     * 候选人的最后日志条目的索引值
     */
    private long lastLogIndex;

    /**
     * 候选人最后日志条目的任期号
     */
    private int lastLogTerm;
}
