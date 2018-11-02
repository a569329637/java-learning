package com.gsq.nio.channel.datagram;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * UDP
 */
public class DatagramChannelClient {
    public static void main(String[] args) throws IOException {
        send1();
        send2();
        send3();
    }

    static void send1() {
        String msg = "New String to write to file..." + System.currentTimeMillis();
        ByteBuffer buffer = ByteBuffer.allocate(48);
        buffer.clear();
        buffer.put(msg.getBytes());
        buffer.flip();

        try (DatagramChannel channel = DatagramChannel.open()) {
            channel.send(buffer, new InetSocketAddress("localhost", 9999));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void send2() {
        String msg = "New String to write to file... New String to write to file... New String to write to file...";
        ByteBuffer buffer = ByteBuffer.allocate(100);
        buffer.clear();
        buffer.put(msg.getBytes());
        buffer.flip();

        try (DatagramChannel channel = DatagramChannel.open()) {
            channel.send(buffer, new InetSocketAddress("localhost", 9999));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void send3() {
        String msg = "New String to write to file..." + System.currentTimeMillis();
        ByteBuffer buffer = ByteBuffer.allocate(48);
        buffer.clear();
        buffer.put(msg.getBytes());
        buffer.flip();

        try (DatagramChannel channel = DatagramChannel.open()) {
            // UDP 是无连接的，连接特定的地址并不会想 TCP 那样创建真正的连接
            // 这里是锁定 DatagramChannel，只能向特定的地址发送数据，也可以使用 write 方法了
            channel.connect(new InetSocketAddress("localhost", 9999));
            channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
