package com.gsq.nio.channel.pipe;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * Pipe receive
 */
public class PipeReceive {

    private Pipe pipe;

    public PipeReceive(Pipe pipe) {
        this.pipe = pipe;
    }

    public void receive() {
        ByteBuffer buffer = ByteBuffer.allocate(48);
        try (Pipe.SourceChannel channel = pipe.source()) {
            buffer.clear();
            channel.read(buffer);
            buffer.flip();

            while (buffer.hasRemaining()) {
                System.out.print((char) buffer.get());
            }
            System.out.println("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
