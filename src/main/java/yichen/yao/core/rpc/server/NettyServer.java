package yichen.yao.core.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import yichen.yao.core.rpc.protocol.codec.RpcCodec;
import yichen.yao.core.rpc.protocol.codec.netty.NettyRequestCodec;
import yichen.yao.core.rpc.protocol.codec.netty.NettyRequestDecoder;
import yichen.yao.core.rpc.protocol.codec.netty.NettyRequestEncoder;
import yichen.yao.core.rpc.protocol.codec.netty.Spliter;
import yichen.yao.core.rpc.serialize.SerializerFactory;
import yichen.yao.core.rpc.server.handler.AppendEntriesRequestHandler;
import yichen.yao.core.rpc.server.handler.InstallSnapshotRequestHandler;
import yichen.yao.core.rpc.server.handler.VoteRequestHandler;

import java.net.InetSocketAddress;

/**
 * @Author: siran.yao
 * @time: 2020/2/13:下午6:06
 */
public class NettyServer extends RpcServer{

    private String host;
    private int port;
    private RpcCodec rpcCodec;

    public NettyServer(String host, int port) {
        this.host = host;
        this.port = port;
        rpcCodec = new NettyRequestCodec(new SerializerFactory().getSerializer());
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
                                    .addLast(new VoteRequestHandler())
                                    .addLast(new AppendEntriesRequestHandler())
                                    .addLast(new InstallSnapshotRequestHandler())
                                    .addLast(new NettyRequestEncoder(rpcCodec))
                            ;
                        }
                    });
            ChannelFuture cf = serverBootstrap.bind(new InetSocketAddress(host,port)).sync();
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    public static void main(String[] args) {
        new NettyServer("localhost",8080).startServer();
    }
}
