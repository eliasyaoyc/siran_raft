package yichen.yao.core.rpc;

import yichen.yao.core.rpc.protocol.RpcRequest;
import yichen.yao.core.rpc.protocol.RpcResponse;

/**
 * @Author: siran.yao
 * @time: 2020/2/15:下午12:21
 */
public interface RpcClient {

    RpcResponse sendRequest(String ip,RpcRequest rpcRequest) throws InterruptedException;

}
