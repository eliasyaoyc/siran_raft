package yichen.yao.core.rpc.remoting.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import yichen.yao.core.rpc.RpcClient;
import yichen.yao.core.rpc.protocol.RpcRequest;
import yichen.yao.core.rpc.protocol.RpcResponse;
import yichen.yao.core.rpc.protocol.codec.RpcCodec;
import yichen.yao.core.rpc.protocol.codec.netty.NettyRequestCodec;
import yichen.yao.core.rpc.protocol.codec.netty.NettyRequestDecoder;
import yichen.yao.core.rpc.protocol.codec.netty.NettyRequestEncoder;
import yichen.yao.core.rpc.protocol.codec.netty.Spliter;
import yichen.yao.core.rpc.protocol.request.AppendEntriesRequest;
import yichen.yao.core.rpc.protocol.request.InstallSnapshotRequest;
import yichen.yao.core.rpc.protocol.request.VoteRequest;
import yichen.yao.core.rpc.remoting.netty.client.handler.AppendEntriesResponseHandler;
import yichen.yao.core.rpc.remoting.netty.client.handler.InstallSnapshotResponseHandler;
import yichen.yao.core.rpc.remoting.netty.client.handler.VoteResponseHandler;
import yichen.yao.core.rpc.serialization.SerializerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: siran.yao
 * @time: 2020/2/13:下午6:03
 */
public class NettyClient implements RpcClient {
    private RpcCodec rpcCodec;
    private Map<String, Channel> channelMap;
    private VoteResponseHandler voteResponseHandler;
    private AppendEntriesResponseHandler appendEntriesResponse;
    private InstallSnapshotResponseHandler installSnapshotResponse;

    public NettyClient() {
        rpcCodec = new NettyRequestCodec(new SerializerFactory().getSerializer());
        channelMap = new ConcurrentHashMap<>();
    }

    private void connection(String peer) {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        voteResponseHandler = new VoteResponseHandler();
        appendEntriesResponse = new AppendEntriesResponseHandler();
        installSnapshotResponse = new InstallSnapshotResponseHandler();
        try {
            bootstrap
                    .group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new Spliter())
                                    .addLast(new NettyRequestDecoder(rpcCodec))
                                    .addLast(voteResponseHandler)
                                    .addLast(appendEntriesResponse)
                                    .addLast(installSnapshotResponse)
                                    .addLast(new NettyRequestEncoder(rpcCodec))
//                            .addLast(new TimeClientHandler())
                            ;
                        }
                    });
            ChannelFuture cf = bootstrap.connect(new InetSocketAddress("localhost", Integer.parseInt(peer.split(":")[1]))).sync();
            Channel channel = cf.channel();
            if (!channelMap.containsKey(peer))
                channelMap.put(peer, channel);
            channel.closeFuture();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public RpcResponse sendRequest(String peer, RpcRequest rpcRequest) throws InterruptedException {
        Channel channel = null;
        if (channelMap.containsKey(peer))
            channel = channelMap.get(peer);
        else {
            connection(peer);
            if (channelMap.containsKey(peer))
                channel = channelMap.get(peer);
        }
        RpcResponse rpcResponse = null;
        if (channel != null) {
            channel.writeAndFlush(rpcRequest);
            if (rpcRequest instanceof VoteRequest)
                rpcResponse = voteResponseHandler.getResponse();
            if (rpcRequest instanceof AppendEntriesRequest)
                rpcResponse = appendEntriesResponse.getResponse();
            if (rpcRequest instanceof InstallSnapshotRequest)
                rpcResponse = installSnapshotResponse.getResponse();
        }
        return rpcResponse;
    }
}
