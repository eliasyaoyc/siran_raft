package yichen.yao.core.rpc.remoting.netty.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import yichen.yao.core.rpc.protocol.RpcResponse;
import yichen.yao.core.rpc.protocol.response.InstallSnapshotResponse;

/**
 * @Author: siran.yao
 * @time: 2020/2/14:上午11:51
 */
public class InstallSnapshotResponseHandler extends SimpleChannelInboundHandler<InstallSnapshotResponse> implements CommonResponse {

    private static RpcResponse rpcResponse;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, InstallSnapshotResponse installSnapshotResponse) throws Exception {
        rpcResponse = installSnapshotResponse;
        notify();
    }

    @Override
    public RpcResponse getResponse() throws InterruptedException {
        wait();
        return rpcResponse;
    }
}
