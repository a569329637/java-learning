package com.gsq.nio.channel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * channel 之前直接传数据
 */
public class ChannelTransferTest {

    public static void main(String[] args) throws FileNotFoundException {
        RandomAccessFile fromFile = new RandomAccessFile("nio-data.txt", "rw");
        FileChannel fromChannel = fromFile.getChannel();

        RandomAccessFile toFile = new RandomAccessFile("to-nio-data.txt", "rw");
        FileChannel toChannel = toFile.getChannel();

        try {
            // 表示开始
            long position = 0;
            // 表示大小
            long size = fromChannel.size();
            // 通过channel直接传数据
            //toChannel.transferFrom(fromChannel, position, size);
            fromChannel.transferTo(position, size, toChannel);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fromFile != null) {
                try {
                    fromFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (toFile != null) {
                try {
                    toFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
