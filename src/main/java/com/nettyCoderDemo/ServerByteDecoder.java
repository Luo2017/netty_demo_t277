package com.nettyCoderDemo;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

public class ServerByteDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("服务端发来的消息-服务端收到消息！\n", CharsetUtil.UTF_8));
        StringBuffer sb = new StringBuffer();
        // 这个过程可能是这样的，toString() 是 netty 实现的
        // 用 UTF_8 格式接收后，再将每个字符转换为 2 个字节的 char 存储到 String 当中
        String s = byteBuf.toString(Charsets.UTF_8); // 不能用 toString() 不带参数的方式
        System.out.println("原消息为: " + s);
//        while (byteBuf.isReadable()) {
//            char ch = byteBuf.readChar();
//            sb.append(ch);
//            System.out.print(ch);
//            if (ch == ']') {
//                System.out.println("kkk");
//                sb = new StringBuffer();
//                list.add(sb.toString());
//            }
//        }
        int index = 0;
        while (index < s.length()) {
            char ch = s.charAt(index++);
            sb.append(ch);
            if (ch == ']') {
                list.add(sb.toString());
                sb = new StringBuffer();
            }
        }
        byteBuf.skipBytes(byteBuf.readableBytes());
    }
}


