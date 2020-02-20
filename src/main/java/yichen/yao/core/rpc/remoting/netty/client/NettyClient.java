package yichen.yao.core.rpc.remoting.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import yichen.yao.core.consistency.impl.DefaultNodeImpl;
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
import yichen.yao.core.rpc.remoting.netty.server.NettyServer;
import yichen.yao.core.rpc.serialization.SerializerFactory;

import java.net.InetSocketAddress;

/**
 * @Author: siran.yao
 * @time: 2020/2/13:下午6:03
 */
public class NettyClient implements RpcClient {
    private String host;
    private int port;
    private RpcCodec rpcCodec;
    private Channel channel;
    private VoteResponseHandler voteResponseHandler;
    private AppendEntriesResponseHandler appendEntriesResponse;
    private InstallSnapshotResponseHandler installSnapshotResponse;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
        rpcCodec = new NettyRequestCodec(new SerializerFactory().getSerializer());
    }

    @Override
    public void connection() {
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
            ChannelFuture cf = bootstrap.connect(new InetSocketAddress(host,port)).sync();
            channel = cf.channel();
            channel.closeFuture();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public RpcResponse sendRequest(String ip,RpcRequest rpcRequest) throws InterruptedException {
        if(channel != null)
            channel.writeAndFlush(rpcRequest);
        else {
            connection();
            if(channel != null)
                channel.writeAndFlush(rpcRequest);
        }
        RpcResponse rpcResponse = null;
        if(rpcRequest instanceof VoteRequest)
            rpcResponse = voteResponseHandler.getResponse();
        if(rpcRequest instanceof AppendEntriesRequest)
            rpcResponse = appendEntriesResponse.getResponse();
        if (rpcRequest instanceof InstallSnapshotRequest)
            rpcResponse = installSnapshotResponse.getResponse();
        return rpcResponse;
    }

    public static void main(String[] arg){
        new NettyServer("localhost",8776,new DefaultNodeImpl()).startServer();
        new NettyClient("localhost", 8775).connection();
    }
}
