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

    private int term;

    public VoteResponse() {
    }

    public VoteResponse(boolean voteGranted,int term) {
        this.voteGranted = voteGranted;
        this.term = term;
        setRequestType(RequestType.VOTE_RESPONSE);
    }
    public static VoteResponseBuilder builder(){
        return new VoteResponseBuilder();
    }
    public static class VoteResponseBuilder{
        private boolean voteGranted;
        private int term;

        private VoteResponseBuilder() {
        }

        public VoteResponseBuilder voteGranted(boolean voteGranted) {
            this.voteGranted = voteGranted;
            return this;
        }

        public VoteResponseBuilder term(int term) {
            this.term = term;
            return this;
        }

        public VoteResponse build(){
            return new VoteResponse(voteGranted,term);
        }
    }
}
