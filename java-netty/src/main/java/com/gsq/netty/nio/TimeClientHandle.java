package com.gsq.netty.nio;

import com.gsq.netty.utils.CharsetUtils;
import com.gsq.netty.utils.PrintUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author guishangquan
 * @date 2019-08-22
 */
public class TimeClientHandle implements Runnable {

    private String host;
    private int port;
    private Selector selector;
    private SocketChannel sc;
    private volatile boolean running;

    public TimeClientHandle(String host, int port) {
        this.host = host;
        this.port = port;
        this.running = true;

        try {
            selector = Selector.open();
            sc = SocketChannel.open();
            sc.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (running) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();

                    try {
                        handleInput(selectionKey);
                    } catch (Exception e) {
                        if (selectionKey != null) {
                            selectionKey.cancel();
                            if (selectionKey.channel() != null) {
                                selectionKey.channel().close();
                            }
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

        }

        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doConnect() throws IOException {
        boolean isConnected = sc.connect(new InetSocketAddress(host, port));
//        System.out.println("host = " + host);
//        System.out.println("port = " + port);
//        System.out.println("isConnected = " + isConnected);
        if (isConnected) {
            // 如果直接连上，注册读到多路复用器上，发送请求消息
            sc.register(selector, SelectionKey.OP_READ);
            doWrite(sc);
        } else {
            // 如果没直接连上，注册连接到多路复用器上
            sc.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    private void handleInput(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isValid()) {
            SocketChannel channel = (SocketChannel) selectionKey.channel();

            if (selectionKey.isConnectable()) {
                // 是连接的话，注册读到多路复用器上，发送请求消息
                if (channel.isConnectionPending()) {
                    channel.finishConnect();
                    channel.configureBlocking(false);
                    channel.register(selector, SelectionKey.OP_READ);

                    doWrite(channel);
                } else {
                    System.out.println("System exit");
                    System.exit(1);
                }
            }

            if (selectionKey.isReadable()) {
                // 读响应数据
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                int readBytes = channel.read(byteBuffer);
                if (readBytes > 0) {
                    byteBuffer.flip();
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);
                    PrintUtils.printBytes(bytes);
                    String body = new String(bytes, CharsetUtils.GBK);
                    System.out.println("Now is " + body);

                    stop();
                }
            }
        }
    }

    private void doWrite(SocketChannel socketChannel) throws IOException {
        byte[] bytes = "QUERY TIME ORDER".getBytes(CharsetUtils.GBK);
        PrintUtils.printBytes(bytes);
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
        if (!byteBuffer.hasRemaining()) {
            System.out.println("Send order to server success.");
        }
    }

    public void stop() {
        running = false;
    }
}
