package com.nettyCoderDemo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class EchoClientEncoder extends MessageToByteEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String s, ByteBuf byteBuf) throws Exception {
        System.out.println("客户端开始编码，编码消息为：" + s);
        String apdMsg = "encode from client\n";
        byte[] apdMsgBytes = apdMsg.getBytes();
        byteBuf.writeBytes(s.getBytes());
        byteBuf.writeBytes(apdMsgBytes);
    }
}
