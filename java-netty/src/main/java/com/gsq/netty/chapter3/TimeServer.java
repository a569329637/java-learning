package com.gsq.netty.chapter3;

import com.gsq.netty.utils.CharsetUtils;
import com.gsq.netty.utils.DateTimeUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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
        //NioEventLoopGroup是个线程组，包含一组nio线程
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();//用于服务端接收客户端的链接
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();//用于进行SocketChannel的网络读写

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)//NioServerSocketChannel的功能对应于jdk里的ServerSocketChannel
                    .option(ChannelOption.SO_BACKLOG, 128)//bossGroup设置tcp参数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//workerGroup设置tcp参数
                    .handler(new LoggingHandler())
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            System.out.println("\n---------------------");
                            System.out.println("有新客户端连接到服务器");
                            System.out.println("客户端地址：" + socketChannel.localAddress().getHostString());

                            socketChannel.pipeline().addLast(new TimeServerHandler());
                        }
                    });//绑定io事件的处理类，作用类似于Reactor模式中的Handle类，处理io事件，对消息进行编解码

            //绑定端口，同步等待成功
            ChannelFuture f = serverBootstrap.bind(port).sync();

            //相当于一直阻塞在这里，直到ServerSocketChannel关闭
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

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf byteBuf = (ByteBuf) msg;//转换对象ByteBuf，类似于jdk的ByteBuffer
        byte[] reqBytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(reqBytes);
        String body = new String(reqBytes, CharsetUtils.UTF8);

        System.out.println("接收到客户端的请求信息：" + body);

        String resp;
        if ("QUERY TIME ORDER".equalsIgnoreCase(body)) {
            LocalDateTime now = LocalDateTime.now();
            resp = DateTimeUtils.formatLocalDateTime(now);
        } else {
            resp = "BAD ORDER";
        }
        ByteBuf resByteBuf = Unpooled.copiedBuffer(resp.getBytes(CharsetUtils.UTF8));
        ctx.write(resByteBuf);//通过write方法异步发送应答消息给客户端
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //为了防止频繁地唤醒Selector进行消息发送，ChannelHandlerContext.write方法不直接将消息写入SocketChannel中
        //write方法只是把待发送的消息写入到缓冲区，通过调用flush方法将缓冲区的消息写入到SocketChannel中
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //关闭
        System.out.println("服务端异常~");
        ctx.close();
    }
}
