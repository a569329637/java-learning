package com.gsq.jvm.file.classloading;

/**
 * @author guishangquan
 * @date 2018/10/9
 */
public class SuperClass {

    static {
        System.out.println("Super Class init!");
    }

    public static int value = 123;
}
