package com.nettyExperiment;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class EchoClientOutboundHandler extends ChannelOutboundHandlerAdapter {
    private String handlerName;

    public EchoClientOutboundHandler() {

    }

    public EchoClientOutboundHandler(String name) {
        this.handlerName = name;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println(handlerName + " 正在处理出站消息！");
        super.write(ctx, msg, promise);
    }
}
