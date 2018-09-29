package com.gsq.jvm.memory.stat;

/**
 * 死锁
 */
public class DeadLock {

    private static class SynAddRunning implements Runnable {

        private int a;
        private int b;

        public SynAddRunning(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public void run() {
            synchronized (Integer.valueOf(a)) {
                synchronized (Integer.valueOf(b)) {
                    System.out.println("(a + b) = " + (a + b));
                }
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 200; ++ i) {
            new Thread(new SynAddRunning(1, 2)).start();
            new Thread(new SynAddRunning(2, 1)).start();
        }
    }
}
