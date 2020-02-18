package yichen.yao.core.consistency;

import yichen.yao.core.config.NodeConfig;
import yichen.yao.core.consistency.impl.DefaultNodeImpl;

import java.util.Arrays;

/**
 * @Author: siran.yao
 * @time: 2020/2/18:上午9:55
 */
public class Application1 {
    public static void main(String[] args) {
        NodeConfig nodeConfig = new NodeConfig();
        nodeConfig.setHost("localhost");
        nodeConfig.setPort(8775);
        nodeConfig.setOtherNodeList(Arrays.asList("localhost:8776","localhost:8777"));
        Node node = new DefaultNodeImpl();
        node.setConfig(nodeConfig);
        node.init();

        NodeConfig nodeConfig2 = new NodeConfig();
        nodeConfig2.setHost("localhost");
        nodeConfig2.setPort(8776);
        nodeConfig2.setOtherNodeList(Arrays.asList("localhost:8775","localhost:8777"));
        Node node2 = new DefaultNodeImpl();
        node2.setConfig(nodeConfig2);
        node2.init();

        NodeConfig nodeConfig3 = new NodeConfig();
        nodeConfig3.setHost("localhost");
        nodeConfig3.setPort(8777);
        nodeConfig3.setOtherNodeList(Arrays.asList("localhost:8775","localhost:8776"));
        Node node3 = new DefaultNodeImpl();
        node.setConfig(nodeConfig3);
        node.init();
    }
}
