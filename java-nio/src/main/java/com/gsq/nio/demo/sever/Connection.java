package com.gsq.nio.demo.sever;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @author guishangquan
 * @date 2018/10/31
 */
public class Connection {

    private SocketChannel socketChannel;
    private ByteBuffer typeBuffer;
    private ByteBuffer lengthBuffer;
    private ByteBuffer dataBuffer;

    public Connection(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        typeBuffer = ByteBuffer.allocate(4);
        lengthBuffer = ByteBuffer.allocate(4);
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public ByteBuffer getTypeBuffer() {
        return typeBuffer;
    }

    public ByteBuffer getLengthBuffer() {
        return lengthBuffer;
    }

    public ByteBuffer getDataBuffer() {
        return dataBuffer;
    }

    public int read() throws IOException {
        socketChannel.read(typeBuffer);
        socketChannel.read(lengthBuffer);
        lengthBuffer.flip();
        int length = lengthBuffer.getInt();
        dataBuffer = ByteBuffer.allocate(length);
        socketChannel.read(dataBuffer);
        return length;
    }

    public void print() {
        typeBuffer.flip();
        lengthBuffer.flip();
        dataBuffer.flip();

        int length = lengthBuffer.getInt();
        byte[] bytes = new byte[length];
        while (dataBuffer.hasRemaining()) {
            dataBuffer.get(bytes);
        }

        StringBuilder sb = new StringBuilder("server read data, type = ");
        sb.append(typeBuffer.getInt());
        sb.append(", length = ").append(length);
        sb.append(", data = ").append(new String(bytes, Charset.forName("gbk")));

        System.out.println(sb.toString());
    }

    public void process() throws InterruptedException {
        Server.queue.put(new Call(this));
    }

    public int write(ByteBuffer buffer) {
        int count = -1;
        try {
            buffer.flip();
            count = socketChannel.write(buffer);
        } catch (IOException e) {
            System.out.println(Thread.currentThread().getName() + ", write socket exception");
        }
        return count;
    }

    public void close() {
        if (socketChannel != null) {
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
