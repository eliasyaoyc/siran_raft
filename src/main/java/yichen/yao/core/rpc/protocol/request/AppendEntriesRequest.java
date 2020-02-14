package yichen.yao.core.rpc.protocol.request;

import lombok.Data;
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
    private int leaderId;
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
    private List<String> entries;
    /**
     * 领导人已经提交的日志的索引值
     */
    private long leaderCommit;
}
