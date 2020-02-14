package yichen.yao.core.rpc.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import yichen.yao.core.rpc.protocol.response.AppendEntriesResponse;

/**
 * @Author: siran.yao
 * @time: 2020/2/14:上午11:51
 */
public class AppendEntriesResponseHandler extends SimpleChannelInboundHandler<AppendEntriesResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AppendEntriesResponse appendEntriesResponse) throws Exception {

    }
}
