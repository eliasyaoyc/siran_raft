package yichen.yao.core.rpc.protocol.request;

import lombok.Data;
import yichen.yao.core.common.constants.RequestType;
import yichen.yao.core.entity.LogEntry;
import yichen.yao.core.rpc.protocol.RpcRequest;

import java.util.List;

/**
 * @Author: siran.yao
 * @time: 2020/2/11:下午6:07
 */
@Data
public class AppendEntriesRequest extends RpcRequest {
    /**
     * leader的 Id 以便于跟随者重定向请求
     */
    private String leaderId;
    /**
     * 新的日志条目紧随之前的索引值
     */
    private long prevLogIndex;
    /**
     * prevLogIndex 条目的任期号
     */
    private int prevLogTerm;
    /**
     * 准备存储的日志条目（表示心跳时为空；一次性发送多个是为了提高效率）
     */
    private List<LogEntry> entries;
    /**
     * 领导人已经提交的日志的索引值
     */
    private long leaderCommit;

    private int term;

    public AppendEntriesRequest() {
    }

    private AppendEntriesRequest(String leaderId, long prevLogIndex, int prevLogTerm
            , List<LogEntry> entries, long leaderCommit, int term) {
        this.leaderId = leaderId;
        this.prevLogIndex = prevLogIndex;
        this.prevLogTerm = prevLogTerm;
        this.entries = entries;
        this.leaderCommit = leaderCommit;
        this.term = term;
        super.setRequestType(RequestType.APPEND_ENTRIES_REQUEST);
    }

    public static AppendEntriesRequestBuilder builder(){
        return new AppendEntriesRequestBuilder();
    }
    public static class AppendEntriesRequestBuilder {
        private String leaderId;
        private long prevLogIndex;
        private int prevLogTerm;
        private List<LogEntry> entries;
        private long leaderCommit;
        private int term;

        private AppendEntriesRequestBuilder() {
        }

        public AppendEntriesRequestBuilder leaderId(String leaderId){
            this.leaderId = leaderId;
            return this;
        }
        public AppendEntriesRequestBuilder preLogIndex(long prevLogIndex){
            this.prevLogIndex = prevLogIndex;
            return this;
        }
        public AppendEntriesRequestBuilder prevLogTerm(int prevLogTerm){
            this.prevLogTerm = prevLogTerm;
            return this;
        }
        public AppendEntriesRequestBuilder entries(List<LogEntry> entries){
            this.entries = entries;
            return this;
        }
        public AppendEntriesRequestBuilder leaderCommit(long leaderCommit){
            this.leaderCommit = leaderCommit;
            return this;
        }

        public AppendEntriesRequestBuilder term(int term){
            this.term = term;
            return this;
        }
        public AppendEntriesRequest build(){
            return new AppendEntriesRequest(leaderId,prevLogIndex,prevLogTerm,entries,leaderCommit,term);
        }
    }
}
