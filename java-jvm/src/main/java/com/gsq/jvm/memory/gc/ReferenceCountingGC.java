package com.gsq.jvm.memory.gc;

/**
 * 相互引用，无法垃圾回收
 *
 * -XX:+PrintGCDetails 打印GC日志
 * -Xloggc:/log/gc.log GC写文件
 *
 */
public class ReferenceCountingGC {

    public Object instance;

    private static final int _1MB = 1024 * 1024;

    private byte[] bigSize = new byte[_1MB];

    public static void main(String[] args) {
        ReferenceCountingGC o1 = new ReferenceCountingGC();
        ReferenceCountingGC o2 = new ReferenceCountingGC();
        o1.instance = o2;
        o2.instance = o1;

        o1 = null;
        o2 = null;

        System.gc();
    }
}
