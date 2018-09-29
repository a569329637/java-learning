package com.gsq.jvm.memory.stat;

import java.util.ArrayList;
import java.util.List;

/**
 * VM Args: -Xms100m -Xmx100m -XX:+UseSerialGC
 *
 */
public class JConsole {

    private static final int _64K = 64 * 1024;

    private static class OOMObject {
        private byte[] bytes = new byte[_64K];
    }

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(10000);

        System.out.println("开始");
        List<OOMObject> lists = new ArrayList<>();
        for (int i = 0; i < 1000; ++ i) {
            System.out.println("i = " + i);
            Thread.sleep(50);
            lists.add(new OOMObject());
        }

        System.gc();

        Thread.sleep(10000);
    }
}
