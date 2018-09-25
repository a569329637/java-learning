package com.gsq.jvm.memory.error;

import java.util.ArrayList;
import java.util.List;

/**
 * Java 堆内存溢出异常测试
 *
 * VM Args: -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 * -Xms 堆内存最小值
 * -Xmx 堆内存最大值
 * -XX:+HeapDumpOnOutOfMemoryError 可以让虚拟机在出现内存溢出异常时 Dump 出当时的内存转储为文件
 */
public class HeapOOM {

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<>();
        for (;;) {
            list.add(new OOMObject());
        }
    }

    private static class OOMObject {
        String a;
        Integer b;
        Float c;

        public OOMObject() {
            this.a = new String("asdfghjkl");
            this.b = new Integer(12345);
            this.c = new Float(123456.0);
        }
    }
}

/*
java.lang.OutOfMemoryError: Java heap space
Dumping heap to java_pid58641.hprof ...
Heap dump file created [36430319 bytes in 0.355 secs]
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at com.gsq.jvm.memory.error.HeapOOM$OOMObject.<init>(HeapOOM.java:29)
 */
