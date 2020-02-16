package yichen.yao.core.rpc.remoting.netty.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import yichen.yao.core.rpc.protocol.request.AppendEntriesRequest;

/**
 * @Author: siran.yao
 * @time: 2020/2/14:上午11:48
 */
public class AppendEntriesRequestHandler extends SimpleChannelInboundHandler<AppendEntriesRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AppendEntriesRequest appendEntriesRequest) throws Exception {

    }
}
