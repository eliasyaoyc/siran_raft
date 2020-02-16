package yichen.yao.core.rpc.remoting.netty.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import yichen.yao.core.rpc.protocol.request.InstallSnapshotRequest;

/**
 * @Author: siran.yao
 * @time: 2020/2/14:上午11:49
 */
public class InstallSnapshotRequestHandler extends SimpleChannelInboundHandler<InstallSnapshotRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, InstallSnapshotRequest installSnapshotRequest) throws Exception {

    }
}
