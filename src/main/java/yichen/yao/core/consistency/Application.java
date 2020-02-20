package yichen.yao.core.consistency;

import yichen.yao.core.config.NodeConfig;
import yichen.yao.core.consistency.impl.DefaultNodeImpl;

import java.util.Arrays;

/**
 * @Author: siran.yao
 * @time: 2020/2/18:上午9:55
 * -DserverPort=8775
 * -DserverPort=8776
 * -DserverPort=8777
 * -DserverPort=8778
 * -DserverPort=8779
 */
public class Application {
    public static void main(String[] args) {
        NodeConfig nodeConfig = new NodeConfig();
        nodeConfig.setHost("localhost");
        nodeConfig.setPort(Integer.valueOf(System.getProperty("serverPort")));
        nodeConfig.setOtherNodeList(Arrays.asList("localhost:8776","localhost:8777"));
        Node node = new DefaultNodeImpl();
        node.setConfig(nodeConfig);
        node.init();
    }
}
