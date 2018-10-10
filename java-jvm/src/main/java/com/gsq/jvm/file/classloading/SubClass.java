package com.gsq.jvm.file.classloading;

/**
 * @author guishangquan
 * @date 2018/10/9
 */
public class SubClass extends SuperClass {

    static {
        System.out.println("Sub Class init!");
    }

}
