package yichen.yao.core.rpc.remoting.netty.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import yichen.yao.core.rpc.protocol.RpcResponse;
import yichen.yao.core.rpc.protocol.response.VoteResponse;

/**
 * @Author: siran.yao
 * @time: 2020/2/14:上午11:50
 */
public class VoteResponseHandler extends SimpleChannelInboundHandler<VoteResponse> implements CommonResponse {
    private RpcResponse rpcResponse;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, VoteResponse voteResponse) throws Exception {
        rpcResponse = voteResponse;
        notify();
    }

    @Override
    public RpcResponse getResponse() throws InterruptedException {
        wait();
        return rpcResponse;
    }
}
