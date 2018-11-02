package com.gsq.nio.demo;

import com.gsq.nio.demo.sever.Server;

import java.io.IOException;

/**
 * @author guishangquan
 * @date 2018/10/31
 */
public class ServerDemo {

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.start();
        } catch (IOException e) {
            System.out.println("server start exception");
        }
    }
}
