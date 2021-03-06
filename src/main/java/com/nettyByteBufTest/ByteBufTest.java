package com.nettyByteBufTest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

public class ByteBufTest {

    public static void main(String[] args) {
        // 下面创建了 initialCapacity 为 6 和 maxCapacity 为 10 的 ByteBuf
        // 6 指的就是可以写的 byte 数
        // ByteBufAllocator 可以看 netty 基础部分第 5 节对 ByteBuf 的介绍
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(6, 10);
        getByteBufInfo("buf 的 initial state", buf);
        buf.writeBytes(new byte[]{1, 2});
        getByteBufInfo("buf 加入 1，2 后",buf);
        buf.writeInt(1000);
        getByteBufInfo("buf 加入 int 1000 后", buf);
        buf.writeByte(1);
        getByteBufInfo("buf 继续加入一个 byte 1 后", buf);
        System.out.println("buf.getInt(2)" + buf.getInt(2));
        buf.setInt(2, 2000);
        System.out.println("buf.setInt(2, 2000) 后");
        System.out.println("buf.getInt(2)" + buf.getInt(2));
        getByteBufInfo("buf.setInt(2, 2000) 后", buf);
//        buf.writeBytes(new byte[]{1,2,3,4}); 超出最大容量会出错

        // CompositeByteBuf 包含了堆缓冲区和直接缓冲区两种模式
        // Unpooled 是 netty 提供的对 ByteBuf 进行操作的工作类，可以用来创建 ByteBuf
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
        int length = compositeByteBuf.readableBytes();
        System.out.println("\n\n***\n\ncomBuf.length() = " + length);
    }

    private static void getByteBufInfo(String step, ByteBuf buf) {
        System.out.println("+++++ " + step + " +++++");
        System.out.println("readerIndex(): " + buf.readerIndex());
        System.out.println("writerIndex(): " + buf.writerIndex());
        System.out.println("isReadable(): " + buf.isReadable());
        System.out.println("isWritable(): " + buf.isWritable());
        System.out.println("readableBytes(): " + buf.readableBytes());
        System.out.println("writableBytes(): " + buf.writableBytes());
        System.out.println("maxWritableBytes(): " + buf.maxWritableBytes());
        System.out.println("capacity(): " + buf.capacity());
        System.out.println("maxCapacity(): " + buf.maxCapacity());
    }
}
