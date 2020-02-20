package yichen.yao.core.rpc.remoting.netty.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import yichen.yao.core.rpc.protocol.RpcResponse;
import yichen.yao.core.rpc.protocol.response.ClientResponse;

/**
 * @Author: siran.yao
 * @time: 2020/2/20:上午10:12
 */
@ChannelHandler.Sharable
public class ClientResponseHandler extends SimpleChannelInboundHandler<ClientResponse> implements CommonResponse{
    private RpcResponse rpcResponse;
    private boolean permit = false;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ClientResponse clientResponse) throws Exception {
        rpcResponse = clientResponse;
        permit = true;
    }

    @Override
    public RpcResponse getResponse() throws InterruptedException {
        while (!permit){

        }
        return rpcResponse;
    }
}
