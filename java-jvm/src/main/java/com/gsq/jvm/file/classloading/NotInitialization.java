package com.gsq.jvm.file.classloading;

/**
 * 子类通过引用父类的静态字段，子类不会进行初始化
 *
 * 可以通过 -XX:+TraceClassLoading 参数观察此操作会导致子类加载
 */
public class NotInitialization {
    public static void main(String[] args) {
        System.out.println(SubClass.value);

        /*
        子类被加载了
        输出结果：
        ...
        [Loaded com.gsq.jvm.file.classloading.SuperClass from file:/Users/guishangquan/repo/github/java-learning/java-jvm/target/classes/]
        [Loaded com.gsq.jvm.file.classloading.SubClass from file:/Users/guishangquan/repo/github/java-learning/java-jvm/target/classes/]
        Super Class init!
        123
        ...
         */
    }
}
