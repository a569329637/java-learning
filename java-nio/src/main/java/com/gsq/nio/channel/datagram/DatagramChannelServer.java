package com.gsq.nio.channel.datagram;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * UDP
 */
public class DatagramChannelServer {
    public static void main(String[] args) throws IOException {
        receive1();
    }

    static void receive1() {

        try (DatagramChannel channel = DatagramChannel.open()) {
            channel.socket().bind(new InetSocketAddress(9999));

            while (true) {
                System.out.println("listen port 9999 ...");
                ByteBuffer buffer = ByteBuffer.allocate(48);
                buffer.clear();
                // 配合 send2()，如果Buffer容不下收到的数据，多出的数据将被丢弃
                channel.receive(buffer);
                buffer.flip();

                while (buffer.hasRemaining()) {
                    System.out.print((char) buffer.get());
                }
                System.out.println("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
