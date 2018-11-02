package com.gsq.nio.demo.sever;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author guishangquan
 * @date 2018/10/31
 */
public class Server {

    public static BlockingQueue<Call> queue = new LinkedBlockingQueue<>();

    public static Queue<Call> responseCalls = new ConcurrentLinkedQueue<>();

    private Boolean running = true;

    private int readerNumber = 10;

    private Reader[] readers;

    private Listener listener;

    private int handlerNumber = 5;

    private Handler[] handlers;

    public Writer writer;

    public Server() throws IOException {
        readers = new Reader[readerNumber];
        for (int i = 0; i < readerNumber; ++ i) {
            readers[i] = new Reader(i, running);
        }
        listener = new Listener(9999, running, readers);
        writer = new Writer(running);
        handlers = new Handler[handlerNumber];
        for (int i = 0; i < handlerNumber; ++ i) {
            handlers[i] = new Handler(i, running, writer);
        }
    }

    public void start() {
        listener.start();
//        for (int i = 0; i < readerNumber; ++ i) {
//            readers[i].start();
//        }
        writer.start();
        for (int i = 0; i< handlerNumber; ++ i) {
            handlers[i].start();
        }
    }

    public void close() {
        running = false;
    }
}
