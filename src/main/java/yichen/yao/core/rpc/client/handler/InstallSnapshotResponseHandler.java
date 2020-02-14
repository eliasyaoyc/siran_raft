package yichen.yao.core.rpc.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import yichen.yao.core.rpc.protocol.RpcRequest;

/**
 * @Author: siran.yao
 * @time: 2020/2/14:上午11:51
 */
public class InstallSnapshotResponseHandler extends SimpleChannelInboundHandler<RpcRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {

    }
}
