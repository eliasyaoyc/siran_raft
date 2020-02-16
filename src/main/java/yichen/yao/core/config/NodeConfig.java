package yichen.yao.core.config;

import lombok.Data;

import java.util.List;

/**
 * @Author: siran.yao
 * @time: 2020/2/15:下午12:38
 * 集群中 每个节点自身的ip+port ，和集群中其他节点的ip+port
 */
@Data
public class NodeConfig {
    /**
     * 当前节点自身的ip
     */
    private String host;
    /**
     * 当前节点自身的port
     */
    private int port;

    /**
     * 其他节点的ip+port
     */
    private List<String> otherNodeList;
}
