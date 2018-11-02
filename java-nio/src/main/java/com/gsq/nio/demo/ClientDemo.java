package com.gsq.nio.demo;

import com.gsq.nio.demo.message.MessageType;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @author guishangquan
 * @date 2018/10/31
 */
public class ClientDemo extends Thread {

    private Selector selector;
    private SocketChannel socketChannel;
    private Boolean running;

    public ClientDemo() throws IOException {
        super.setName("nio-client");

        selector = Selector.open();
        socketChannel = SocketChannel.open();
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 9999);
        socketChannel.configureBlocking(false);
        socketChannel.socket().setTcpNoDelay(true);
        socketChannel.socket().setKeepAlive(true);
        socketChannel.connect(address);
        running = true;

        socketChannel.register(selector, SelectionKey.OP_CONNECT);
    }

    public void close() {
        running = false;
    }

    private void doSend(SocketChannel socketChannel, String msg) throws IOException {
        System.out.println(getName() + ", do send, msg = " + msg);

        byte[] bytes = msg.getBytes(Charset.forName("gbk"));

        ByteBuffer byteBuffer = ByteBuffer.allocate(8 + bytes.length);
        byteBuffer.putInt(MessageType.TEXT.getType());
        byteBuffer.putInt(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
    }

    public void send(String msg) throws IOException {
        doSend(socketChannel, msg);
    }

    private String doRead(SocketChannel socketChannel) throws IOException {
        System.out.println(getName() + ", do read");

        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        socketChannel.read(lengthBuffer);

        lengthBuffer.flip();
        int length = lengthBuffer.getInt();
        ByteBuffer byteBuffer = ByteBuffer.allocate(length);
        socketChannel.read(byteBuffer);

        byteBuffer.flip();
        StringBuilder sb = new StringBuilder();
        while (byteBuffer.hasRemaining()) {
            sb.append((char) byteBuffer.get());
        }
        return sb.toString();
    }

    private void doConnect(SelectionKey key) throws IOException {
        System.out.println(getName() + ", do connect");

        SocketChannel channel = (SocketChannel) key.channel();
        if (channel.isConnectionPending()) {
            channel.finishConnect();
            doSend(channel, "connect success!");
        }

        key.interestOps(key.interestOps() | SelectionKey.OP_READ);
    }

    @Override
    public void run() {
        while (running) {
            try {
                int num = selector.select();
                if (num <= 0)
                    continue;

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    SocketChannel channel = (SocketChannel) key.channel();
                    if (key.isValid() && key.isConnectable()) {
                        doConnect(key);
                    }
                    else if (key.isValid() && key.isReadable()) {
                        String response = doRead(channel);
                        System.out.println(getName() + ", " + response);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ClientDemo client = new ClientDemo();
        client.start();

        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\nplz input:");
            String str = scanner.nextLine();
            if ("exit".equals(str)) {
                client.close();
                break;
            }
            client.send(str);
        }
    }
}
