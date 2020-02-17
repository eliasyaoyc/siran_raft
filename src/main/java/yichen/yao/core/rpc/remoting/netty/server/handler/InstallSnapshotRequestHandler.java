package yichen.yao.core.rpc.remoting.netty.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import yichen.yao.core.consistency.Node;
import yichen.yao.core.rpc.protocol.request.InstallSnapshotRequest;
import yichen.yao.core.rpc.protocol.response.InstallSnapshotResponse;

/**
 * @Author: siran.yao
 * @time: 2020/2/14:上午11:49
 */
public class InstallSnapshotRequestHandler extends SimpleChannelInboundHandler<InstallSnapshotRequest> {

    private Node node;

    public InstallSnapshotRequestHandler(Node node) {
        this.node = node;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, InstallSnapshotRequest installSnapshotRequest) throws Exception {
        Channel channel = ctx.channel();
        InstallSnapshotResponse installSnapshotResponse = node.handleInstallSnapshotRequest(installSnapshotRequest);
        channel.writeAndFlush(installSnapshotResponse);
    }
}
