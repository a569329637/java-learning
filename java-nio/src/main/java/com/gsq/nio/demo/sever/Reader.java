package com.gsq.nio.demo.sever;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author guishangquan
 * @date 2018/10/31
 */
public class Reader extends Thread {

    private Selector readSelector;
    private Boolean running;

    public Reader(int i, Boolean running) throws IOException {
        super.setName("nio-reader-" + i);
        this.running = running;
        readSelector = Selector.open();
    }

    public void register(SocketChannel socketChannel, Object attachment) throws IOException {
        System.out.println(getName() + ", register");

        socketChannel.configureBlocking(false);
        socketChannel.socket().setTcpNoDelay(true);
        socketChannel.socket().setKeepAlive(true);
        socketChannel.register(readSelector, SelectionKey.OP_READ, attachment);
    }

    public void wakeup() {
        System.out.println(getName() + ", wake up");

        readSelector.wakeup();
    }

    @Override
    public void run() {
        System.out.println(getName() + ", run");

        while (running) {
            try {
                int num = readSelector.select();
                System.out.println(getName() + ", num = " + num);
                if (num == 0)
                    continue;

                Set<SelectionKey> selectionKeys = readSelector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    if (key.isValid() && key.isReadable()) {
                        doRead(key);
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void doRead(SelectionKey key) throws IOException, InterruptedException {
        System.out.println(getName() + ", do read");

        Connection connection = (Connection) key.attachment();
        if (connection == null)
            return;

        connection.read();
        connection.print();
        connection.process();
    }
}
