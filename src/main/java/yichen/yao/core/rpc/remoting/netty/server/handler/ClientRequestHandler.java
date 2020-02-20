package yichen.yao.core.rpc.remoting.netty.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import yichen.yao.core.consistency.Node;
import yichen.yao.core.rpc.protocol.request.ClientRequest;
import yichen.yao.core.rpc.protocol.response.ClientResponse;

/**
 * @Author: siran.yao
 * @time: 2020/2/20:上午10:14
 */
public class ClientRequestHandler extends SimpleChannelInboundHandler<ClientRequest> {
    private Node node;

    public ClientRequestHandler(Node node) {
        this.node = node;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ClientRequest clientRequest) throws Exception {
        Channel channel = ctx.channel();
        ClientResponse clientResponse = node.handlerClientRequest(clientRequest);
        channel.writeAndFlush(clientResponse);
    }
}
