package com.gsq.jvm.file.compile;

import java.util.HashMap;
import java.util.Map;

/**
 * 编译之后会将类型擦除
 */
public class GenericTest {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("hello", "你好");
        System.out.println(map.get("hello"));
    }
}

/*
public class GenericTest {
    public GenericTest() {
    }

    public static void main(String[] args) {
        HashMap map = new HashMap();
        map.put("hello", "你好");
        System.out.println((String)map.get("hello"));
    }
}
 */
