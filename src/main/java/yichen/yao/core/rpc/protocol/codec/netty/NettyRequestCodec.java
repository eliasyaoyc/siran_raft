package yichen.yao.core.rpc.protocol.codec.netty;

import io.netty.buffer.ByteBuf;
import yichen.yao.core.rpc.protocol.RpcRequest;
import yichen.yao.core.rpc.protocol.codec.RpcCodec;
import yichen.yao.core.rpc.serialize.Serializer;

/**
 * @Author: siran.yao
 * @time: 2020/2/14:上午9:23
 */
public class NettyRequestCodec extends RpcCodec {

    private Serializer serializer;

    public NettyRequestCodec(Serializer serializer) {
        super();
        this.serializer = serializer;
    }

    @Override
    public ByteBuf encode(ByteBuf byteBuf, RpcRequest rpcRequest) {
        byte[] serialize = serializer.serialize(rpcRequest);
        //序列化方式
        byteBuf.writeByte(serializer.getSerializerAlgorithm());
        //rpc类型
        byteBuf.writeByte(rpcRequest.getRequestType());
        //数据长度
        byteBuf.writeInt(serialize.length);
        //数据
        byteBuf.writeBytes(serialize);
        return byteBuf;
    }

    @Override
    public RpcRequest Decode(ByteBuf byteBuf) {
        byte serializerAlgorithm = byteBuf.readByte();
        byte requestType = byteBuf.readByte();
        int dataLength = byteBuf.readInt();
        byte[] bytes = new byte[dataLength];
        byteBuf.readBytes(bytes);
        Class<? extends RpcRequest> rpcRequest = getRequestMap(requestType);
        if (rpcRequest != null && serializer != null)
            return serializer.deserialize(rpcRequest, bytes);
        return null;
    }
}
