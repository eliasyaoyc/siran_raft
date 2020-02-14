package yichen.yao.core.rpc.protocol.codec.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import yichen.yao.core.rpc.protocol.RpcRequest;
import yichen.yao.core.rpc.protocol.codec.RpcCodec;
import yichen.yao.core.rpc.protocol.codec.RpcCodecFactory;

/**
 * @Author: siran.yao
 * @time: 2020/2/14:上午10:04
 */
public class NettyRequestEncoder extends MessageToByteEncoder<RpcRequest> {

    private RpcCodec rpcCodec;

    public NettyRequestEncoder(RpcCodec rpcCodec) {
        this.rpcCodec = rpcCodec;
    }

    //编码
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest, ByteBuf byteBuf) throws Exception {
        rpcCodec.encode(byteBuf,rpcRequest);
    }
}
