package yichen.yao.core.rpc.protocol.request;

import lombok.Data;
import yichen.yao.core.rpc.protocol.RpcRequest;

/**
 * @Author: siran.yao
 * @time: 2020/2/11:下午6:25
 */
@Data
public class InstallSnapshotRequest extends RpcRequest {
    /**
     * leader的 Id 以便于跟随者重定向请求
     */
    private int leaderId;

    /**
     * 快照中包含的最后日志条目的索引值
     */
    private long lastIncludedIndex;

    /**
     * 快照中包含的最后日志条目的任期号
     */
    private int lastIncludedTerm;

    /**
     * 分块在快照中的字节偏移量
     */
    private long offset;

    /**
     * 从偏移量开始的快照分块的原始字节
     */
    private byte[] data;

    /**
     * 如果这是最后一个分块则为 true
     */
    private boolean done;
}
