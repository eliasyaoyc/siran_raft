package yichen.yao.core.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yichen.yao.core.rpc.RpcClient;
import yichen.yao.core.rpc.remoting.netty.client.NettyClient;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: siran.yao
 * @time: 2020/2/20:下午7:43
 */
public class RaftClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(RaftClient.class);


    private final static RpcClient client = NettyClient.getInstance();

    static String addr = "localhost:8778";
    static List<String> list = Arrays.asList("localhost:8777", "localhost:8778", "localhost:8779");

    public static void main(String[] args) throws InterruptedException {

        AtomicLong count = new AtomicLong(3);

    }
}
