package com.nettyChatRoom;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String wsUri;
    private static final File INDEX;

    static {
        URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            String path = location.toURI() + "index.html";
            path = !path.contains("file:") ? path : path.substring(5); // 去掉 file: 这个开头
            INDEX = new File(path);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("不能找到 index.html", e);
        }
    }

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response); // 因为 response 没有用到传递的消息，所以不用 retain
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        if (wsUri.equalsIgnoreCase(fullHttpRequest.getUri())) {
            // 经过测试这个删除了之后无法建立 websocket 握手，所有 ws 握手是 下一个 ws handler 完成的
            channelHandlerContext.fireChannelRead(fullHttpRequest.retain());
        } else {
            if (HttpHeaders.is100ContinueExpected(fullHttpRequest)) {
                send100Continue(channelHandlerContext);
            }
            RandomAccessFile file = new RandomAccessFile(INDEX, "r");
            // 注意不是 DefaultFullHttpResponse，因为三部分才是一个完整的
            HttpResponse response = new DefaultHttpResponse(fullHttpRequest.getProtocolVersion(), HttpResponseStatus.OK);
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
            boolean keepAlive = HttpHeaders.isKeepAlive(fullHttpRequest);
            if (keepAlive) {
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, file.length());
                response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            }
            channelHandlerContext.write(response); // 最后 flush 一次即可
            if (channelHandlerContext.pipeline().get(SslHandler.class) == null) {
                channelHandlerContext.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
            } else {
                channelHandlerContext.write(new ChunkedNioFile(file.getChannel())); // ssl 传递要用这个
            }
            ChannelFuture future = channelHandlerContext.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            // 分成三部分发送，浏览器端将会识别到一个完整的 http response，以 LastHttpContent 作为结尾，从而解析出来
            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE); // 最后的 flush 操作完成会自动关闭，写是按照顺序的
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
