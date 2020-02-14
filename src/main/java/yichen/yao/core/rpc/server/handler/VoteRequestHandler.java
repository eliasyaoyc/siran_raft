package yichen.yao.core.rpc.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import yichen.yao.core.rpc.protocol.request.VoteRequest;

/**
 * @Author: siran.yao
 * @time: 2020/2/14:上午11:47
 */
public class VoteRequestHandler extends SimpleChannelInboundHandler<VoteRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, VoteRequest voteRequest) throws Exception {

    }
}
