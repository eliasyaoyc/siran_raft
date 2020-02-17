package yichen.yao.core.rpc.remoting.netty.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import yichen.yao.core.consistency.Node;
import yichen.yao.core.rpc.protocol.request.VoteRequest;
import yichen.yao.core.rpc.protocol.response.VoteResponse;

/**
 * @Author: siran.yao
 * @time: 2020/2/14:上午11:47
 */
public class VoteRequestHandler extends SimpleChannelInboundHandler<VoteRequest> {

    private Node node;

    public VoteRequestHandler(Node node) {
        this.node = node;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, VoteRequest voteRequest) throws Exception {
        Channel channel = ctx.channel();
        VoteResponse voteResponse = node.handlerVoteRequest(voteRequest);
        channel.writeAndFlush(voteResponse);
    }
}
