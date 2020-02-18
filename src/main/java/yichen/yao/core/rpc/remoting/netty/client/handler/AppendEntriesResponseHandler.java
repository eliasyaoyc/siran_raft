package yichen.yao.core.rpc.remoting.netty.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import yichen.yao.core.rpc.protocol.RpcResponse;
import yichen.yao.core.rpc.protocol.response.AppendEntriesResponse;

/**
 * @Author: siran.yao
 * @time: 2020/2/14:上午11:51
 */
@ChannelHandler.Sharable
public class AppendEntriesResponseHandler extends SimpleChannelInboundHandler<AppendEntriesResponse> implements CommonResponse {
    private RpcResponse rpcResponse;
    private boolean permit = false;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AppendEntriesResponse appendEntriesResponse) throws Exception {
        rpcResponse = appendEntriesResponse;
        permit = true;
    }

    @Override
    public RpcResponse getResponse() throws InterruptedException {
        while (!permit){

        }
        return rpcResponse;
    }
}
