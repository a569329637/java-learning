package com.gsq.jvm.memory.error;

/**
 * 虚拟机栈和本地方法栈测试
 *
 * VM Args: -Xss160k
 * -Xss 栈内存容量
 */
public class JavaVMStackSOF {

    private int stackLength = 1;

    public void stackLeak() {
        stackLength ++;
        stackLeak();
    }

    public static void main(String[] args) {
        JavaVMStackSOF oom = new JavaVMStackSOF();

        try {
            oom.stackLeak();
        } catch (Throwable e) {
            System.out.println("oom.stackLength = " + oom.stackLength);
            throw e;
        }
    }
}

/*
oom.stackLength = 744
Exception in thread "main" java.lang.StackOverflowError
	at com.gsq.jvm.memory.error.JavaVMStackSOF.stackLeak(JavaVMStackSOF.java:14)
	at com.gsq.jvm.memory.error.JavaVMStackSOF.stackLeak(JavaVMStackSOF.java:15)
	at com.gsq.jvm.memory.error.JavaVMStackSOF.stackLeak(JavaVMStackSOF.java:15)
 */
