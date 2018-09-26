package com.gsq.jvm.memory.gc;

/**
 * 长期存活的对象进入老年代
 *
 * VM Args: -Xmx20m -Xms20m -Xmn10m -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=1
 * -XX:MaxTenuringThreshold gc回收次数，大于它用新生代进入老年代
 *
 */
public class TenuringThresholdTest {

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        byte[] alloc1, alloc2, alloc3;
        alloc1 = new byte[1 * _1MB];
        alloc2 = new byte[4 * _1MB];
        alloc3 = new byte[4 * _1MB];
        alloc3 = null;
        alloc3 = new byte[4 * _1MB];
    }
}
