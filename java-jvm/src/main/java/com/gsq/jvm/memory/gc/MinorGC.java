package com.gsq.jvm.memory.gc;

/**
 * 新生代GC
 *
 * VM Args: -Xmx20m -Xms20m -Xmn10m -XX:+PrintGCDetails -XX:SurvivorRatio=8
 * -Xmx 最大堆内存
 * -Xms 最小堆内存
 * -Xmn 新生代内存
 * -XX:+PrintGCDetails 打印GC日志
 * -XX:SurvivorRatio eden和survivor比例
 *
 */
public class MinorGC {

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        byte[] alloc1, alloc2, alloc3, alloc4;
        alloc1 = new byte[2 * _1MB];
        alloc2 = new byte[2 * _1MB];
        alloc3 = new byte[2 * _1MB];
        alloc1 = null;
        alloc2 = null;
        alloc3 = null;
        alloc4 = new byte[3 * _1MB];
    }
}

/*
[GC (Allocation Failure) [PSYoungGen: 7495K->528K(9216K)] 7495K->536K(19456K), 0.0015136 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
Heap
 PSYoungGen      total 9216K, used 3764K [0x00000007bf600000, 0x00000007c0000000, 0x00000007c0000000)
  eden space 8192K, 39% used [0x00000007bf600000,0x00000007bf929140,0x00000007bfe00000)
  from space 1024K, 51% used [0x00000007bfe00000,0x00000007bfe84010,0x00000007bff00000)
  to   space 1024K, 0% used [0x00000007bff00000,0x00000007bff00000,0x00000007c0000000)
 ParOldGen       total 10240K, used 8K [0x00000007bec00000, 0x00000007bf600000, 0x00000007bf600000)
  object space 10240K, 0% used [0x00000007bec00000,0x00000007bec02000,0x00000007bf600000)
 Metaspace       used 3145K, capacity 4494K, committed 4864K, reserved 1056768K
  class space    used 349K, capacity 386K, committed 512K, reserved 1048576K
 */
