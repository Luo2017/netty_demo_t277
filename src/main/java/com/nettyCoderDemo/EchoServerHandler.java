package com.nettyCoderDemo;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.HashMap;
import java.util.Map;

@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端 channel active!");
        System.out.println("服务端的子 channel 的 socketAddress 为 : " + ctx.channel().localAddress()); // 8088 端口号一致
        // 没有必要一定向后面传播这个 active 的消息
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        if (msg instanceof String) {
//            System.out.println("hello");
//        }
        ByteBuf in = (ByteBuf) msg;
        System.out.println("服务器收到消息：" + in.toString(CharsetUtil.UTF_8));
//        String in = (String) msg;
//        System.out.println("服务器收到消息： " + in);
//        ctx.writeAndFlush(Unpooled.copiedBuffer("服务端发来的消息-服务端收到消息！\n", CharsetUtil.UTF_8));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace(); // 不在此处拦截，异常信息会传输到 pipeline 的尾端
        ctx.close(); // 关闭该 ctx
    }
}
