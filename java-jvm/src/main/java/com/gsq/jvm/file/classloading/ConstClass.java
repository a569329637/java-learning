package com.gsq.jvm.file.classloading;

/**
 * @author guishangquan
 * @date 2018/10/9
 */
public class ConstClass {

    static {
        System.out.println("Const Class Init!");
    }

    public static final String m = "Hello World";
}
