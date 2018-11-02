package com.gsq.nio.channel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * http://ifeve.com/java-nio-all/
 * 读写文件
 */
public class FileChannelTest {

    public static void main(String[] args) throws FileNotFoundException {
        RandomAccessFile file = new RandomAccessFile("nio-data.txt", "rw");
        FileChannel channel = file.getChannel();

        // 申请20个字节ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(20);

        try {
            // 将数据写到ByteBuffer中，返回读了多少个字节
            int bytesRead = channel.read(byteBuffer);
            while (bytesRead != -1) {
                System.out.println("bytesRead = " + bytesRead);

                // 将指针跳到数组的开始，之后就可以开始读了
                byteBuffer.flip();

                // 表示是否还可以读取数据
                while (byteBuffer.hasRemaining()) {
                    // 从ByteBuffer中读取数据
                    System.out.print((char) byteBuffer.get());
                    System.out.println("byteBuffer = " + byteBuffer.get());
                }
                System.out.println();

                // 清空缓冲区，让它可以再次写入数据
                // clear()方法会清空整个缓冲区，compact()方法只会清除已经读过的数据
                byteBuffer.clear();
                bytesRead = channel.read(byteBuffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (file != null) {
                    file.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
