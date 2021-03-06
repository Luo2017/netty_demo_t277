package com.nettyChatRoom;

import com.nettyDemo.EchoServerHandler;
import com.nettyDemo.NettyServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.ImmediateEventExecutor;

import javax.net.ssl.SSLEngine;
import java.net.InetSocketAddress;

public class ChatServer {
    private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        try {
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(8088))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            SelfSignedCertificate cert = new SelfSignedCertificate();
                            SslContext sslContext = SslContext.newServerContext(cert.certificate(), cert.privateKey());
                            SSLEngine engine = sslContext.newEngine(socketChannel.alloc());
                            engine.setUseClientMode(false);
                            socketChannel.pipeline()
//                                    .addFirst(new SslHandler(engine))
                                    .addLast(new HttpServerCodec())
                                    .addLast(new ChunkedWriteHandler())
                                    .addLast(new HttpObjectAggregator(64 * 1024))
                                    .addLast(new HttpRequestHandler("/ws")) // uri ??????????????????????????????????????? /ws ?????????????????? webSocket ??????
                                    .addLast(new WebSocketServerProtocolHandler("/ws")) // ?????? /ws ??????????????????WebSocket??????
                                    .addLast(new TextWebSocketFrameHandler(channelGroup));
                        }
                    });
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();
        } finally {
            channelGroup.close();
            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        new ChatServer().start();
    }

}
