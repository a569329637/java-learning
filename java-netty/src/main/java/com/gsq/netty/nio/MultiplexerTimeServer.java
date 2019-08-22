package com.gsq.netty.nio;

import com.gsq.netty.utils.CharsetUtils;
import com.gsq.netty.utils.DateTimeUtils;
import com.gsq.netty.utils.PrintUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;

/**
 * @author guishangquan
 * @date 2019-08-22
 */
public class MultiplexerTimeServer implements Runnable {

    private int port;
    private volatile boolean running;
    private ServerSocketChannel ssc;
    private Selector selector;

    public MultiplexerTimeServer(int port) {
        this.port = port;
        this.running = true;

        try {
            ssc = ServerSocketChannel.open();
            ssc.socket().bind(new InetSocketAddress(port));
            ssc.configureBlocking(false);

            selector = Selector.open();

            ssc.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("The time server is start in port: " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey selectionKey;
                while (iterator.hasNext()) {
                    selectionKey = iterator.next();
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
            }
        }

        // 多路复用器关闭后，所有注册在上面的资源也会关闭，比如 channel
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isValid()) {
            // 处理新接入的请求
            if (selectionKey.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                // 注册新的连接到selector
                sc.register(selector, SelectionKey.OP_READ);
            }

            // 处理可以读的channel
            if (selectionKey.isReadable()) {
                SocketChannel sc = (SocketChannel) selectionKey.channel();

                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(byteBuffer);
                if (readBytes > 0) {
                    byteBuffer.flip();
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);
                    PrintUtils.printBytes(bytes);
                    String body = new String(bytes, CharsetUtils.GBK);

                    System.out.println("The time server receive order: " + body);
                    String resp;
                    if ("QUERY TIME ORDER".equalsIgnoreCase(body)) {
                        LocalDateTime now = LocalDateTime.now();
                        resp = DateTimeUtils.formatLocalDateTime(now);
                    } else {
                        resp = "BAD ORDER";
                    }

                    doWrite(sc, resp);
                }
            }
        }
    }

    private void doWrite(SocketChannel sc, String resp) throws IOException {
        if (resp != null && resp.trim().length() > 0) {
            byte[] bytes = resp.getBytes(CharsetUtils.GBK);
            PrintUtils.printBytes(bytes);
            ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
            byteBuffer.put(bytes);
            byteBuffer.flip();
            sc.write(byteBuffer);
            if (!byteBuffer.hasRemaining()) {
                System.out.println("response order to client success.");
            }
        }
    }

    public void stop() {
        this.running = false;
    }
}
