package com.nettyCoderDemo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

// 注意 String 指的是输入handler消息的类型，输出的是 Object 类型，我们可以任意指定
public class EchoClientMTMEncoder extends MessageToMessageEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String s, List<Object> list) throws Exception {
        System.out.println("EchoClientMTMEncoder 正在处理 String 类型的出站消息");
        // 将出站消息分成每段 4 个字符的字符串
        // List 中的消息被传递给下一个出站 handler 不会从整个 pipeline 开始传递
        // 正是因为 EchoClientEncoder 处理的也是 String 类型的消息，所以才可以传递给它
        for (int i = 0; i < s.length();) {
            if (i + 4 < s.length()) {
                list.add(s.substring(i, i + 4)); // 0 - 3
                i += 4;
            } else {
                list.add(s.substring(i));
                break;
            }
        }
    }
}
