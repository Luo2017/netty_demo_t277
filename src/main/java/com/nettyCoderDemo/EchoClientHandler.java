package com.nettyCoderDemo;

import com.google.common.base.Charsets;
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
        super.channelActive(ctx);
//        ctx.channel().writeAndFlush("haha");
        // 从 pipelien 的 tail 开始向前写，但是 tailHandler 没有实现 OutboundHandler，它只是调用 tailCtx.write() 操作
        ctx.pipeline().writeAndFlush("客户端发来的 String 类型的消息-客户端已经 active 来自" + handlerName);
        ctx.pipeline().writeAndFlush(Unpooled.copiedBuffer("客户端发来的普通类型的消息-客户端已经 active 来自 " + handlerName + "\n", CharsetUtil.UTF_8));
//        ctx.fireChannelActive();
//        super.channelActive(ctx);
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
