package com.gsq.netty.chapter3;

import com.gsq.netty.utils.CharsetUtils;
import com.gsq.netty.utils.MsgUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.UnsupportedEncodingException;

/**
 * @author guishangquan
 * @date 2019-08-23
 */
public class TimeClient {

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
                            socketChannel.pipeline().addLast(new TimeClientHandler());
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

    public static void main(String[] args) {
        int port = 8080;
        String host = "127.0.0.1";
        if (args != null && args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        new TimeClient().connect(host, port);
    }
}

class TimeClientHandler extends ChannelInboundHandlerAdapter {

    private ByteBuf msg;

    public TimeClientHandler() throws UnsupportedEncodingException {
        byte[] reqBytes = MsgUtils.QUERY_TIME_ORDER.getBytes(CharsetUtils.UTF8);
        msg = Unpooled.copiedBuffer(reqBytes);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf respByteBuf = (ByteBuf) msg;
        byte[] respBytes = new byte[respByteBuf.readableBytes()];
        respByteBuf.readBytes(respBytes);
        String resp = new String(respBytes, CharsetUtils.UTF8);
        System.out.println("时间是：" + resp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("客户端异常~");
        ctx.close();
    }
}
