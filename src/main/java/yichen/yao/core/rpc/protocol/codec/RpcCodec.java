package yichen.yao.core.rpc.protocol.codec;

import io.netty.buffer.ByteBuf;
import yichen.yao.core.common.constants.RequestType;
import yichen.yao.core.common.constants.SerializerType;
import yichen.yao.core.rpc.protocol.RpcRequest;
import yichen.yao.core.rpc.protocol.request.AppendEntriesRequest;
import yichen.yao.core.rpc.protocol.request.InstallSnapshotRequest;
import yichen.yao.core.rpc.protocol.request.VoteRequest;
import yichen.yao.core.rpc.protocol.response.AppendEntriesResponse;
import yichen.yao.core.rpc.protocol.response.InstallSnapshotResponse;
import yichen.yao.core.rpc.protocol.response.VoteResponse;
import yichen.yao.core.rpc.serialize.Serializer;
import yichen.yao.core.rpc.serialize.fastjson.FastJsonSerializer;
import yichen.yao.core.rpc.serialize.probuf.ProbufSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: siran.yao
 * @time: 2020/2/14:上午9:20
 */
public abstract class RpcCodec {
    private final Map<Byte, Class<? extends RpcRequest>> requestMap;
    private final Map<Byte, Serializer> serializerMap;

    public RpcCodec() {
        requestMap = new HashMap<Byte, Class<? extends RpcRequest>>();
        requestMap.put(RequestType.VOTE_REQUEST, VoteRequest.class);
        requestMap.put(RequestType.APPEND_ENTRIES_REQUEST, AppendEntriesRequest.class);
        requestMap.put(RequestType.INSTALL_SNAPSHOT_REQUEST, InstallSnapshotRequest.class);
        requestMap.put(RequestType.VOTE_RESPONSE, VoteResponse.class);
        requestMap.put(RequestType.APPEND_ENTRIES_RESPONSE, AppendEntriesResponse.class);
        requestMap.put(RequestType.INSTALL_SNAPSHOT_RESPONSE, InstallSnapshotResponse.class);

        serializerMap = new HashMap<Byte, Serializer>();
        serializerMap.put(SerializerType.FAST_JSON, new FastJsonSerializer());
        serializerMap.put(SerializerType.PROTO_BUF,new ProbufSerializer());
    }

    //编码
    public abstract ByteBuf encode(ByteBuf byteBuf,RpcRequest rpcRequest);

    //解码
    public abstract RpcRequest Decode(ByteBuf byteBuf);

    public Class<? extends RpcRequest> getRequestMap(Byte requestType){
        if (requestMap.containsKey(requestType))
            return requestMap.get(requestType);
        throw new IllegalStateException("非法的RPC！");
    }
}