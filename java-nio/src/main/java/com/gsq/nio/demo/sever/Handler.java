package com.gsq.nio.demo.sever;

import java.nio.ByteBuffer;
import java.util.Random;

/**
 * @author guishangquan
 * @date 2018/11/1
 */
public class Handler extends Thread {

    private Writer writer;

    private Boolean running;

    public Handler(int i, Boolean running, Writer writer) {
        super.setName("nio-handler-" + i);
        this.writer = writer;
        this.running = running;
    }

    @Override
    public void run() {
        System.out.println(getName() + ", run");

        while (running) {
            try {
                Call call = Server.queue.take();
                doHandle(call);
            } catch (InterruptedException e) {
                System.out.println(getName() + ", take exception");
            }
        }
    }

    public void doHandle(Call call) {
        System.out.println(getName() + ", do handle");

        StringBuilder sb = new StringBuilder("request: ");
        ByteBuffer request = call.getRequest();
        request.flip();
        while (request.hasRemaining()) {
            sb.append((char) request.get());
        }

        sb.append(", ").append(getName()).append(", response: ");
        Random random = new Random();
        int length = random.nextInt(30) + 20;
        for (int i = 0; i < length; ++ i) {
            int c = random.nextInt(51);
            char ch = (char) (c < 26 ? c + 65 : c - 26 + 97);
            sb.append(ch);
        }

        String responseStr = sb.toString();
        byte[] bytes = responseStr.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length + 4);
        byteBuffer.putInt(bytes.length);
        byteBuffer.put(bytes);

        call.setResponse(byteBuffer);
        writer.doSyncWrite(call);
    }
}
