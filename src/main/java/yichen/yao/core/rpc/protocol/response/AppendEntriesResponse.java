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

    private int term;

    public AppendEntriesResponse() {
    }

    public AppendEntriesResponse(boolean success, int term) {
        this.success = success;
        this.term = term;
        setRequestType(RequestType.APPEND_ENTRIES_RESPONSE);
    }

    public static AppendEntriesResponseBuilder builder(){
        return new AppendEntriesResponseBuilder();
    }

    public static class AppendEntriesResponseBuilder{
        private boolean success;
        private int term;

        private AppendEntriesResponseBuilder() {
        }

        public AppendEntriesResponseBuilder success(boolean success) {
            this.success = success;
            return this;
        }

        public AppendEntriesResponseBuilder term(int term) {
            this.term = term;
            return this;
        }

        public AppendEntriesResponse build(){
            return new AppendEntriesResponse(success,term);
        }
    }
}
