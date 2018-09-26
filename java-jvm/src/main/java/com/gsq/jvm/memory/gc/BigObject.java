package com.gsq.jvm.memory.gc;

/**
 * 大对象直接进入老年代
 *
 * VM Args: -Xmx20m -Xms20m -Xmn10m -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=3145728 -XX:+UseParNewGC (1.8)
 * -XX:PretenureSizeThreshold 对象大于设置值将直接在老年代分配
 */
public class BigObject {

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        byte[] alloc = new byte[7 * _1MB];
    }
}

/*
Heap
 PSYoungGen      total 9216K, used 1352K [0x00000007bf600000, 0x00000007c0000000, 0x00000007c0000000)
  eden space 8192K, 16% used [0x00000007bf600000,0x00000007bf7520b8,0x00000007bfe00000)
  from space 1024K, 0% used [0x00000007bff00000,0x00000007bff00000,0x00000007c0000000)
  to   space 1024K, 0% used [0x00000007bfe00000,0x00000007bfe00000,0x00000007bff00000)
 ParOldGen       total 10240K, used 7168K [0x00000007bec00000, 0x00000007bf600000, 0x00000007bf600000)
  object space 10240K, 70% used [0x00000007bec00000,0x00000007bf300010,0x00000007bf600000)
 Metaspace       used 3210K, capacity 4494K, committed 4864K, reserved 1056768K
  class space    used 355K, capacity 386K, committed 512K, reserved 1048576K
 */
