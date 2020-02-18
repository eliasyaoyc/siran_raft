package yichen.yao.core.rpc.remoting.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import yichen.yao.core.consistency.Node;
import yichen.yao.core.consistency.impl.DefaultNodeImpl;
import yichen.yao.core.rpc.RpcServer;
import yichen.yao.core.rpc.protocol.codec.RpcCodec;
import yichen.yao.core.rpc.protocol.codec.netty.NettyRequestCodec;
import yichen.yao.core.rpc.protocol.codec.netty.NettyRequestDecoder;
import yichen.yao.core.rpc.protocol.codec.netty.NettyRequestEncoder;
import yichen.yao.core.rpc.protocol.codec.netty.Spliter;
import yichen.yao.core.rpc.remoting.netty.client.NettyClient;
import yichen.yao.core.rpc.remoting.netty.server.handler.AppendEntriesRequestHandler;
import yichen.yao.core.rpc.remoting.netty.server.handler.InstallSnapshotRequestHandler;
import yichen.yao.core.rpc.remoting.netty.server.handler.VoteRequestHandler;
import yichen.yao.core.rpc.serialization.SerializerFactory;

import java.net.InetSocketAddress;

/**
 * @Author: siran.yao
 * @time: 2020/2/13:下午6:06
 */
public class NettyServer implements RpcServer {

    private String host;
    private int port;
    private RpcCodec rpcCodec;
    private Node node;

    public NettyServer(String host, int port,Node node) {
        this.host = host;
        this.port = port;
        rpcCodec = new NettyRequestCodec(new SerializerFactory().getSerializer());
        this.node = node;
    }

    public void startServer() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            serverBootstrap
                    .group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel
                                    .pipeline()
                                    .addLast(new Spliter())
                                    .addLast(new NettyRequestDecoder(rpcCodec))
                                    .addLast(new VoteRequestHandler(node))
                                    .addLast(new AppendEntriesRequestHandler(node))
                                    .addLast(new InstallSnapshotRequestHandler(node))
                                    .addLast(new NettyRequestEncoder(rpcCodec))
//                            .addLast(new TimeServerHandler())
                            ;
                        }
                    });
            ChannelFuture cf = serverBootstrap.bind(new InetSocketAddress(host,port));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }
    }


    public static void main(String[] args) {
        new NettyServer("localhost",8775,new DefaultNodeImpl()).startServer();
        new NettyClient("localhost",8776).connection();
    }
}
