package com.gsq.netty.chapter5;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author guishangquan
 * @date 2019-08-26
 */
public class EchoServer1 {

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        new EchoServer1().bind(port);
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
                            System.out.println("有新客户端连接到服务器");
                            System.out.println("客户端地址：" + socketChannel.localAddress().getHostString());

                            socketChannel.pipeline()
                                    .addLast(new FixedLengthFrameDecoder(11))//最大长度11，需要在实际内容前加一个长度，所以内容最长为10
                                    .addLast(new StringDecoder())
                                    .addLast(new EchoServerHandler1());
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

class EchoServerHandler1 extends ChannelInboundHandlerAdapter {

    private int counter;

    public EchoServerHandler1() {
        this.counter = 0;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.out.println("接收到客户端的请求信息(" + (++counter) + ")：" + body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
