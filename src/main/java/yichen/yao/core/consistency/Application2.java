package yichen.yao.core.consistency;

import yichen.yao.core.config.NodeConfig;
import yichen.yao.core.consistency.impl.DefaultNodeImpl;

import java.util.Arrays;

/**
 * @Author: siran.yao
 * @time: 2020/2/18:上午9:55
 */
public class Application2 {
    public static void main(String[] args) {
        NodeConfig nodeConfig = new NodeConfig();
        nodeConfig.setHost("localhost");
        nodeConfig.setPort(8776);
        nodeConfig.setOtherNodeList(Arrays.asList("localhost:8775","localhost:8777"));
        Node node = new DefaultNodeImpl();
        node.setConfig(nodeConfig);
        node.init();
    }
}
