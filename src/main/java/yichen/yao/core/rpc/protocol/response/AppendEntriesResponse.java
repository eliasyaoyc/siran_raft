package yichen.yao.core.rpc.protocol.response;

import lombok.Data;
import yichen.yao.core.common.constants.RequestType;
import yichen.yao.core.rpc.protocol.RpcResponse;

/**
 * @Author: siran.yao
 * @time: 2020/2/11:下午6:18
 */
@Data
public class AppendEntriesResponse extends RpcResponse {

    /**
     * follower包含了匹配上 prevLogIndex 和 prevLogTerm 的日志时为真
     */
    private boolean success;

    public AppendEntriesResponse() {
        setRequestType(RequestType.APPEND_ENTRIES_RESPONSE);
    }
}
