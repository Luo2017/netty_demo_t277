package com.nettyCoderDemo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class NettyClient {

    private final String host;

    private final int port;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class) // 注意和服务端 NioServerSocketChannel 区别
                    .remoteAddress(new InetSocketAddress(host, port)) // java io 包中 host 就是 String 类型的
                    .handler(new ChannelInitializer<SocketChannel>() { // 注意服务器端的是 childHandler
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoClientHandler("clientInboundHandler1"))
                                    .addLast(new EchoClientOutboundHandler("outboundHandler01"))
                                    .addLast(new EchoClientEncoder()); // 出站消息第一个经过 Encoder
                        }
                    });
            ChannelFuture f = b.connect().sync(); // 注意不是 bind() 监听端口
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new NettyClient("127.0.0.1", 8088).start();
    }

}
