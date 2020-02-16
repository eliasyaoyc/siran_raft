package yichen.yao.core.rpc.protocol.codec;

import yichen.yao.core.rpc.protocol.codec.netty.NettyRequestCodec;
import yichen.yao.core.rpc.serialization.Serializer;
import yichen.yao.core.rpc.serialization.SerializerFactory;

/**
 * @Author: siran.yao
 * @time: 2020/2/14:上午10:11
 */
public class RpcCodecFactory {
    private RpcCodec rpcCodec;

    //默认使用nettyCodec
    public RpcCodecFactory(){
        Serializer serializer = new SerializerFactory().getSerializer();
        rpcCodec = new NettyRequestCodec(serializer);
    }

    public RpcCodecFactory(RpcCodec rpcCodec){
        this.rpcCodec = rpcCodec;
    }
    public RpcCodec getRpcCodec(){
        return rpcCodec;
    }
}
