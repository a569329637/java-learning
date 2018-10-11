package com.gsq.jvm.concurrency;

import java.util.Vector;

/**
 * Vector 的add(),remove(),get()都是线程安全
 * //1中任然会报错，//2就不会
 */
public class VectorTest {

    private static Vector<Integer> vector = new Vector<>();

    public static void main(String[] args) {
        // 1
        while (true) {
            for (int i = 0; i < 100; ++ i) {
                vector.add(i);
            }

            new Thread(() -> {
                for (int i = 0; i < vector.size(); ++ i) {
                    vector.remove(i);
                }
            }).start();

            new Thread(() -> {
                for (int i = 0; i < vector.size(); ++ i) {
                    vector.get(i);
                }
            }).start();

            while (Thread.activeCount() > 20) ;
        }

        // 2
//        while (true) {
//            for (int i = 0; i < 100; ++ i) {
//                vector.add(i);
//            }
//
//            new Thread(() -> {
//                synchronized (vector) {
//                    for (int i = 0; i < vector.size(); ++i) {
//                        vector.remove(i);
//                    }
//                }
//            }).start();
//
//            new Thread(() -> {
//                synchronized (vector) {
//                    for (int i = 0; i < vector.size(); ++i) {
//                        vector.get(i);
//                    }
//                }
//            }).start();
//
//            while (Thread.activeCount() > 20) ;
//        }
    }
}
