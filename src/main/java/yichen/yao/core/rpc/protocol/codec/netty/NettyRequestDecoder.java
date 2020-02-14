package yichen.yao.core.rpc.protocol.codec.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import yichen.yao.core.rpc.protocol.codec.RpcCodec;

import java.util.List;

/**
 * @Author: siran.yao
 * @time: 2020/2/14:上午10:04
 */

public class NettyRequestDecoder extends ByteToMessageDecoder {

    private RpcCodec rpcCodec;

    public NettyRequestDecoder(RpcCodec rpcCodec) {
        this.rpcCodec = rpcCodec;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List out) throws Exception {
        out.add(rpcCodec.Decode(byteBuf));
    }
}
