package com.gsq.jvm.memory.error;

/**
 * 虚拟机栈和本地方法栈测试
 *
 * VM Args: -Xss2m
 * -Xss 栈内存容量
 */
public class JavaVMStackOOM {

    public void dontStop() {
        while (true);
    }

    public void stackLeakByThread() {
        while (true) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    dontStop();
                }
            }).start();
        }
    }

    public static void main(String[] args) {
        JavaVMStackOOM oom = new JavaVMStackOOM();
        oom.stackLeakByThread();
    }
}
