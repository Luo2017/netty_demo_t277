package com.nettyCoderDemo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

// 注意 String 指的是输入handler消息的类型，输出的是 Object 类型，我们可以任意指定
public class EchoClientMTMEncoder extends MessageToMessageEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String s, List<Object> list) throws Exception {
        // 将出站消息分成每段 4 个字符的字符串
        for (int i = 0; i < s.length();) {
            if (i + 4 < s.length()) {
                list.add(s.substring(i, i + 4) + " "); // 0 - 3
                i += 4;
            } else {
                list.add(s.substring(i) + " ");
                break;
            }
        }
    }
}
