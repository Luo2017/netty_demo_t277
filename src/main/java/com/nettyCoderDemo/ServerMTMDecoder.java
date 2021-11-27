package com.nettyCoderDemo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class ServerMTMDecoder extends MessageToMessageDecoder<String> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, String s, List<Object> list) throws Exception {
        s += " decoded by MTMDecoder";
        list.add(s);
    }
}
