package com.gsq.nio.channel.pipe;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * Pipe send
 */
public class PipeSend {

    private Pipe pipe;

    public PipeSend(Pipe pipe) {
        this.pipe = pipe;
    }

    public void send() {
        String msg = "New String to write to file..." + System.currentTimeMillis();
        ByteBuffer buffer = ByteBuffer.allocate(48);
        buffer.clear();
        buffer.put(msg.getBytes());
        buffer.flip();

        try (Pipe.SinkChannel channel = pipe.sink()) {
            channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
