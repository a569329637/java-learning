package com.gsq.jvm.memory.stat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 线程等待
 */
public class ThreadWait {

    public static void createBusyThread() {
        new Thread(() -> {
            while (true);
        }, "testBusyThread").start();
    }

    public static void createLockThread(Object o) {
        new Thread(() -> {
            synchronized (o) {
                try {
                    o.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "testLockThread").start();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.readLine();
        createBusyThread();
        reader.readLine();
        createLockThread(new Object());
    }

}
