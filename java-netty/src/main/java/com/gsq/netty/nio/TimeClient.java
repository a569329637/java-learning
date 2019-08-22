package com.gsq.netty.nio;

/**
 * @author guishangquan
 * @date 2019-08-22
 */
public class TimeClient {

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        new Thread(new TimeClientHandle("127.0.0.1", port), "NIO-TimeClient").start();
    }
}
