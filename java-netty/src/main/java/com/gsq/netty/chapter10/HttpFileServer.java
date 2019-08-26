package com.gsq.netty.chapter10;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;

import javax.activation.MimetypesFileTypeMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * @author guishangquan
 * @date 2019-08-26
 */
public class HttpFileServer {

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        new HttpFileServer().bind(port);
    }

    public void bind(int port) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            System.out.println("\n---------------------");
                            System.out.println("有新客户端连接到服务器");
                            System.out.println("客户端地址：" + socketChannel.localAddress().getHostString());

                            socketChannel.pipeline()
                                    // http请求解码器，将消息转换为httpRequest、httpResponse、httpContent、LastHttpContent等
                                    .addLast("http-decoder", new HttpRequestDecoder())
                                    // http对象聚合，把httpRequest、httpResponse、httpContent、LastHttpContent等封装成httpFullRequest、httpFullResponse
                                    .addLast("http-aggregator", new HttpObjectAggregator(65535))
                                    // http响应编码器，对http响应信息进行编码
                                    .addLast("http-encoder", new HttpResponseEncoder())
                                    // 主要是支持异步发送大码流，比如大文件传输，但不占用过多的内存，防止发生java内存溢出
                                    .addLast("http-chunked", new ChunkedWriteHandler())
                                    // 文件服务器业务逻辑的处理
                                    .addLast("http-file-server-handler", new HttpFileServerHandler());
                        }
                    });

            ChannelFuture f = serverBootstrap.bind(port).sync();

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //退出
            System.out.println("time server exit");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}

class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final String MODULE = "java-netty";
    private static final String DEFAULT_URL = "/src/main/java/com/gsq";
    private static final String BR = "</br>";
    private static final String SPACE = "&nbsp&nbsp&nbsp&nbsp&nbsp";

    private static final String BAD_REQUEST = "Bad Request";
    private static final String METHOD_NOT_ALLOWED = "Method Not Allowed";
    private static final String NOT_FOUND = "Not Found";


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        if (!msg.getDecoderResult().isSuccess()) {
            sendError(ctx, BAD_REQUEST);
            return;
        }

        if (msg.getMethod() != HttpMethod.GET) {
            sendError(ctx, METHOD_NOT_ALLOWED);
            return;
        }

        String uri = msg.getUri();
        String path = System.getProperty("user.dir") + "/" + MODULE + uri;
        File file = new File(path);
        if (file.isHidden() || !file.exists()) {
            sendError(ctx, NOT_FOUND);
            return;
        }

        if (file.isDirectory()) {
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            response.headers().set("Content-Type", "text/html;charset=UTF-8");
            response.headers().set("Connection", HttpHeaderValues.CLOSE);

            StringBuilder body = new StringBuilder();
            body.append("当前目录：").append(path).append(BR);
            body.append(BR);
            body.append(SPACE).append("<a href=\"").append(uri).append("\">.</a>").append(BR);
            body.append(SPACE).append("<a href=\"").append(uri, 0, uri.lastIndexOf("/")).append("\">..</a>").append(BR);
            for (File f : Objects.requireNonNull(file.listFiles())) {
                body.append(SPACE).append("<a href=\"").append(uri).append("/").append(f.getName()).append("\">").append(f.getName()).append("</a>").append(BR);
            }

            ByteBuf byteBuf = Unpooled.copiedBuffer(body.toString().getBytes());
            response.content().writeBytes(byteBuf);
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }

        if (file.isFile()) {
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            response.headers().set("Content-Type", "text/html;charset=UTF-8");
            response.headers().set("Connection", HttpHeaderValues.CLOSE);

            InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "GBK");
            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuilder body = new StringBuilder();
            body.append("当前文件：").append(path).append(BR);
            body.append(BR);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                body.append(line).append(BR);
            }

            ByteBuf byteBuf = Unpooled.copiedBuffer(body.toString().getBytes());
            response.content().writeBytes(byteBuf);
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void sendError(ChannelHandlerContext ctx, String responseMsg) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set("Content-Type", "text/plain;charset=UTF-8");
        response.headers().set("Connection", HttpHeaderValues.CLOSE);

        ByteBuf byteBuf = Unpooled.copiedBuffer(responseMsg.getBytes());
        response.content().writeBytes(byteBuf);

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private void setContentTypeHeader(HttpResponse response, File file) {
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, mimeTypesMap.getContentType(file.getPath()));
    }
}
