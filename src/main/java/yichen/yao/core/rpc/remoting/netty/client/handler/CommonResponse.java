package yichen.yao.core.rpc.remoting.netty.client.handler;

import yichen.yao.core.rpc.protocol.RpcResponse;

/**
 * @Author: siran.yao
 * @time: 2020/2/17:下午10:00
 */
public interface CommonResponse {
    RpcResponse getResponse() throws InterruptedException;
}
