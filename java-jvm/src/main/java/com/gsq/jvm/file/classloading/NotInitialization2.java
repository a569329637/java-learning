package com.gsq.jvm.file.classloading;

/**
 * 数组
 *
 * 没有触发「com.gsq.jvm.file.classloading.SuperClass」的初始化阶段
 * 触发了一个名为「[Lcom.gsq.jvm.file.classloading.SuperClass」的类型初始化
 * 对于用户来说，这个类不是一个合法的类，他是由虚拟机自动生成的
 *
 */
public class NotInitialization2 {

    public static void main(String[] args) {
        SuperClass[] sca = new SuperClass[10];

        System.out.println("SubClass.class = " + SubClass.class);
        System.out.println("sca.getClass() = " + sca.getClass());
    }

}

