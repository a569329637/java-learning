package com.gsq.netty.chapter4;

import com.gsq.netty.utils.CharsetUtils;
import com.gsq.netty.utils.MsgUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.io.UnsupportedEncodingException;

/**
 * @author guishangquan
 * @date 2019-08-23
 */
public class TimeClient {

    public static void main(String[] args) {
        int port = 8080;
        String host = "127.0.0.1";
        if (args != null && args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        new TimeClient().connect(host, port);
    }

    public void connect(String host, int port) {

        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(nioEventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            //会出现粘包拆包的问题
                            //socketChannel.pipeline().addLast(new TimeClientHandler());

                            socketChannel.pipeline()
                                    .addLast(new LineBasedFrameDecoder(1024))
                                    .addLast(new StringDecoder())
                                    .addLast(new TimeClientHandler1());
                        }
                    });

            ChannelFuture f = bootstrap.connect(host, port).sync();

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            nioEventLoopGroup.shutdownGracefully();
        }
    }
}

class TimeClientHandler extends ChannelInboundHandlerAdapter {

    private ByteBuf msg;
    private int counter;

    public TimeClientHandler() throws UnsupportedEncodingException {
        String tmp = MsgUtils.QUERY_TIME_ORDER + MsgUtils.LINE_SEPARATOR;
        byte[] reqBytes = tmp.getBytes(CharsetUtils.UTF8);
        msg = Unpooled.copiedBuffer(reqBytes);

        counter = 0;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ByteBuf byteBuf;
        for (int i = 0; i < 100; ++ i) {
            byteBuf = Unpooled.copiedBuffer(msg);
            ctx.writeAndFlush(byteBuf);
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf respByteBuf = (ByteBuf) msg;
        byte[] respBytes = new byte[respByteBuf.readableBytes()];
        respByteBuf.readBytes(respBytes);
        String resp = new String(respBytes, CharsetUtils.UTF8);
        String substring = resp.substring(0, respBytes.length - MsgUtils.LINE_SEPARATOR.length());

        System.out.println("时间是(" + (++counter) + ")：" + substring);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("客户端异常~");
        ctx.close();
    }
}

class TimeClientHandler1 extends ChannelInboundHandlerAdapter {

    private ByteBuf msg;
    private int counter;

    public TimeClientHandler1() throws UnsupportedEncodingException {
        String tmp = MsgUtils.QUERY_TIME_ORDER + MsgUtils.LINE_SEPARATOR;
        byte[] reqBytes = tmp.getBytes(CharsetUtils.UTF8);
        msg = Unpooled.copiedBuffer(reqBytes);

        counter = 0;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ByteBuf byteBuf;
        for (int i = 0; i < 100; ++ i) {
            byteBuf = Unpooled.copiedBuffer(msg);
            ctx.writeAndFlush(byteBuf);
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String resp = (String) msg;
        System.out.println("时间是(" + (++counter) + ")：" + resp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("客户端异常~");
        ctx.close();
    }
}
