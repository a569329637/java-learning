package com.gsq.netty.chapter4;

import com.gsq.netty.utils.CharsetUtils;
import com.gsq.netty.utils.DateTimeUtils;
import com.gsq.netty.utils.MsgUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LoggingHandler;

import java.time.LocalDateTime;

/**
 * @author guishangquan
 * @date 2019-08-23
 */
public class TimeServer {

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        new TimeServer().bind(port);
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
                    .handler(new LoggingHandler())
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            System.out.println("\n---------------------");
                            System.out.println("有新客户端连接到服务器4");
                            System.out.println("客户端地址：" + socketChannel.localAddress().getHostString());

                            //会出现粘包拆包的问题
                            //socketChannel.pipeline().addLast(new TimeServerHandler());

                            socketChannel.pipeline()
                                    .addLast(new LineBasedFrameDecoder(1024))
                                    .addLast(new StringDecoder())
                                    .addLast(new TimeServerHandler1());
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

class TimeServerHandler extends ChannelInboundHandlerAdapter {

    private int counter;

    public TimeServerHandler() {
        this.counter = 0;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] reqBytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(reqBytes);
        String body = new String(reqBytes, CharsetUtils.UTF8);
        String substring = body.substring(0, reqBytes.length - MsgUtils.LINE_SEPARATOR.length());

        System.out.println("接收到客户端的请求信息(" + (++counter) + ")：" + substring);

        String resp;
        if ("QUERY TIME ORDER".equalsIgnoreCase(body)) {
            LocalDateTime now = LocalDateTime.now();
            resp = DateTimeUtils.formatLocalDateTime(now);
        } else {
            resp = "BAD ORDER";
        }
        resp += MsgUtils.LINE_SEPARATOR;
        ByteBuf resByteBuf = Unpooled.copiedBuffer(resp.getBytes(CharsetUtils.UTF8));
        ctx.writeAndFlush(resByteBuf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}

class TimeServerHandler1 extends ChannelInboundHandlerAdapter {

    private int counter;

    public TimeServerHandler1() {
        this.counter = 0;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;

        System.out.println("接收到客户端的请求信息(" + (++counter) + ")：" + body);

        String resp;
        if ("QUERY TIME ORDER".equalsIgnoreCase(body)) {
            LocalDateTime now = LocalDateTime.now();
            resp = DateTimeUtils.formatLocalDateTime(now);
        } else {
            resp = "BAD ORDER";
        }
        resp += MsgUtils.LINE_SEPARATOR;
        ByteBuf resByteBuf = Unpooled.copiedBuffer(resp.getBytes(CharsetUtils.UTF8));
        ctx.writeAndFlush(resByteBuf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}

