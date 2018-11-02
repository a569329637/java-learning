package com.gsq.nio.demo.sever;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author guishangquan
 * @date 2018/10/31
 */
public class Writer extends Thread {

    private Selector writeSelector;

    private Boolean running;

    public Writer(Boolean running) throws IOException {
        super.setName("nio-writer");
        this.running = running;
        writeSelector = Selector.open();
    }

    @Override
    public void run() {
        System.out.println(getName() + ", run");

        while (running) {
            try {
                registerWriters();
                int num = writeSelector.select(1000);
                if (num == 0)
                    continue;

                Set<SelectionKey> selectionKeys = writeSelector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    if (key.isValid() && key.isWritable()) {
                        doAsyncWrite(key);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerWriters() {
        Iterator<Call> iterator = Server.responseCalls.iterator();
        while (iterator.hasNext()) {
            Call call = iterator.next();
            iterator.remove();

            Connection connection = call.getConnection();
            SocketChannel socketChannel = connection.getSocketChannel();

            SelectionKey selectionKey = socketChannel.keyFor(writeSelector);
            if (selectionKey == null) {
                try {
                    socketChannel.register(writeSelector, SelectionKey.OP_WRITE, call);
                } catch (ClosedChannelException e) {
                    System.out.println(getName() + ", socket channel register exception");
                }
            }
            else {
                selectionKey.interestOps(SelectionKey.OP_WRITE);
                System.out.println(getName() + ", registered");
            }
        }
    }

    private void doAsyncWrite(SelectionKey key) {
        Call call = (Call) key.attachment();
        Connection connection = call.getConnection();
        ByteBuffer response = call.getResponse();

        try {
            connection.getSocketChannel().write(response);
        } catch (IOException e) {
            System.out.println(getName() + ", async write socket error");
        }

        // 不关注写了
        key.interestOps(0);
    }

    public void doSyncWrite(Call call) {
        Connection connection = call.getConnection();
        ByteBuffer response = call.getResponse();

        int count = connection.write(response);
        if (count < 0) {
            // 写出错
            System.out.println(Thread.currentThread().getName() + ", sync write socket error");
            connection.close();
        }
        else if (response.hasRemaining()) {
            // 没写完
            Server.responseCalls.add(call);
            writeSelector.wakeup();
        }
        else {
            // 写完了
        }
    }
}
