package com.nettyCoderDemo;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class EchoClientEncoder extends MessageToByteEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String s, ByteBuf byteBuf) throws Exception {
        System.out.println("客户端 EchoClientEncoder 开始编码，编码前消息为：" + s);
        String apdMsg = "[encode from client]";
        byte[] apdMsgBytes = apdMsg.getBytes(Charsets.UTF_8);
        for (int i = 0; i < 10000; i++) {
            byteBuf.writeBytes(s.getBytes(Charsets.UTF_8));
            byteBuf.writeBytes(apdMsgBytes);
        }
    }
}

