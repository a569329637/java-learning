package com.gsq.nio.channel.pipe;

import java.io.IOException;
import java.nio.channels.Pipe;

/**
 * Pipe 是两个线程之前的单向数据连接
 * Pipe 有一个 source 通道，有一个 sink 通道，数据从 sink 通道写入，从 source 通道读取
 */
public class PipeTest {
    public static void main(String[] args) throws IOException {
        Pipe open = Pipe.open();
        new Thread(() -> new PipeReceive(open).receive()).start();
        new Thread(() -> new PipeSend(open).send()).start();
    }
}
