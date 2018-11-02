package com.gsq.nio.demo.sever;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author guishangquan
 * @date 2018/10/31
 */
public class Listener extends Thread {

    private Selector acceptSelector;
    private Reader[] readers;
    private Boolean running;
    private int robin;

    public Listener(int port, boolean running, Reader[] readers) throws IOException {
        super.setName("nio-listener");

        this.readers = readers;
        this.running = running;
        robin = 0;
        acceptSelector = Selector.open();

        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(port));
        serverChannel.register(acceptSelector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void run() {
        System.out.println(getName() + ", run");

        while (running) {
            try {
                int num = acceptSelector.select();
                if (num == 0)
                    continue;

                Set<SelectionKey> selectionKeys = acceptSelector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    if (key.isValid() && key.isAcceptable()) {
                        doAccept(key);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doAccept(SelectionKey key) throws IOException {
        System.out.println(getName() + ", do accept");

        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();

        SocketChannel socketChannel;
        while ((socketChannel = serverChannel.accept()) != null) {
            Reader reader = getReader();
            Connection connection = new Connection(socketChannel);
            reader.register(socketChannel, connection);
            if (!reader.isAlive()) {
                synchronized (reader) {
                    reader.start();
                }
            }
        }
    }

    private Reader getReader() {
        if (robin == readers.length) {
            robin = 0;
        }
        return readers[robin++];
    }
}
