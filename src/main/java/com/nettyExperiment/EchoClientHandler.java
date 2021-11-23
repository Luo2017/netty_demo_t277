package com.nettyExperiment;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private String handlerName;


    public EchoClientHandler() {

    }

    public EchoClientHandler(String name) {
        this.handlerName = name;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 当被通知 channel 活跃时，发送一条消息，即与服务器的链接建立之后被调用
        System.out.println("客户端 " + handlerName +" channel active!");
        ctx.writeAndFlush(Unpooled.copiedBuffer("客户端发来的消息-客户端已经 active 来自 " + handlerName + "\n", CharsetUtil.UTF_8));
//        ctx.fireChannelActive();
        super.channelActive(ctx);
    }

    // 实现 SimpleInboundHandler 不要实现 channelRead() 而要实现 channelRead0()
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("handler " + handlerName + "接收到消息");
        System.out.println("客户端接收到服务端发送的消息: " + byteBuf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close(); // 关闭 channel，返回 ChannelFuture
    }
}
