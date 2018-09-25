package com.gsq.jvm.memory.error;

import java.util.ArrayList;
import java.util.List;

/**
 * 运行时常量池测试
 *
 * VM Args: -XX:PermSize=2M -XX:MaxPermSize=2M (1.8 以前)
 * VM Args: -XX:MetaspaceSize=2M -XX:MaxMetaspaceSize=2M (1.8 之后)
 *
 */
public class RuntimeConstantPoolOOM {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        int i = 0;
        for (;;) {
            list.add(String.valueOf(i++).intern());
        }
    }
}

/*
Error occurred during initialization of VM
java.lang.OutOfMemoryError: Metaspace
	<<no stack trace available>>
 */
