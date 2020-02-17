package yichen.yao.core.rpc.remoting.netty.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import yichen.yao.core.consistency.Node;
import yichen.yao.core.rpc.protocol.request.AppendEntriesRequest;
import yichen.yao.core.rpc.protocol.response.AppendEntriesResponse;

/**
 * @Author: siran.yao
 * @time: 2020/2/14:上午11:48
 */
public class AppendEntriesRequestHandler extends SimpleChannelInboundHandler<AppendEntriesRequest> {
    private Node node;

    public AppendEntriesRequestHandler(Node node) {
        this.node = node;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AppendEntriesRequest appendEntriesRequest) throws Exception {
        Channel channel = ctx.channel();
        AppendEntriesResponse appendEntriesResponse = node.handleAppendEntriesRequest(appendEntriesRequest);
        channel.writeAndFlush(appendEntriesResponse);
    }
}
