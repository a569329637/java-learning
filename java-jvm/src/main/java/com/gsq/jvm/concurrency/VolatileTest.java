package com.gsq.jvm.concurrency;

import java.util.concurrent.CountDownLatch;

/**
 *
 * volatile 第一个语义：保证对线程的可见性，但是不能保证原子性
 * 由于race++不是原子操作，所以多线程执行操作该操作时错误
 * 应用：控制并发，调用shutdown()，所有doWork()都要停下来
 * volatile boolean shutdownRequested = false;
 * void shutdown { shutdownRequested = true }
 * void doWork() {
 *     while(!shutdownRequested) {}
 * }
 *
 * volatile 还能防止指令重排
 * 应用：双重检验-单例模式
 */
public class VolatileTest {

    private static volatile int race = 0;
    private final static int thread_count = 20;
    private final static int inc_count = 100000;

    static void increase() {
        race ++;
    }

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(thread_count);

        for (int i = 0; i < thread_count; i ++) {
            new Thread(() -> {
                for (int j = 0; j < inc_count; j ++) {
                    increase();
                }
                latch.countDown();
                System.out.println(Thread.currentThread().getName());
            }).start();
        }

        try {
            // 等待所有线程解锁
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(race);
    }

}
