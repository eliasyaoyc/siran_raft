package yichen.yao.core.rpc.protocol.request;

import lombok.Data;
import yichen.yao.core.common.constants.RequestType;
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
    private String candidateId;

    /**
     * 候选人的最后日志条目的索引值
     */
    private long lastLogIndex;

    /**
     * 候选人最后日志条目的任期号
     */
    private long lastLogTerm;

    private int term;

    public VoteRequest() {
    }

    private VoteRequest(String candidateId, long lastLogIndex, long lastLogTerm, int term) {
        this.candidateId = candidateId;
        this.lastLogIndex = lastLogIndex;
        this.lastLogTerm = lastLogTerm;
        this.term = term;
        super.setRequestType(RequestType.VOTE_REQUEST);
    }

    public static  VoteRequestBuilder builder(){
        return new VoteRequestBuilder();
    }

    public static class VoteRequestBuilder{
        private String candidateId;
        private long lastLogIndex;
        private long lastLogTerm;
        private int term;

        private VoteRequestBuilder() {
        }
        public VoteRequestBuilder candidateId(String candidateId){
            this.candidateId = candidateId;
            return this;
        }
        public VoteRequestBuilder lastLogIndex(long lastLogIndex){
            this.lastLogIndex = lastLogIndex;
            return this;
        }
        public VoteRequestBuilder lastLogTerm(long lastLogTerm){
            this.lastLogTerm = lastLogTerm;
            return this;
        }
        public VoteRequestBuilder term(int term){
            this.term = term;
            return this;
        }
        public VoteRequest build(){
            return new VoteRequest(candidateId,lastLogIndex,lastLogTerm,term);
        }
    }
}
