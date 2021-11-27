package com.OtherTest;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

public class OTest {

    public static void main(String[] args) {
        ByteBuf buf = Unpooled.buffer(10);
        String s = "你好, world01!";
        buf.writeBytes(s.getBytes(StandardCharsets.UTF_8));
        int index = 0;
        while (buf.isReadable()) {
            System.out.println("buf.readerIndex = " + buf.readerIndex());
            char ch1 = buf.readChar();
            char ch2 = s.charAt(index++);
            System.out.println("ch1 = " + ch1 + "; ch2 = " + ch2);
            System.out.println("readChar() 后, buf.readableBytes() = " + buf.readableBytes());
        }
    }
}
