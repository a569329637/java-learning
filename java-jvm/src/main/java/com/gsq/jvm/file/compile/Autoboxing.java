package com.gsq.jvm.file.compile;

import java.util.Arrays;
import java.util.List;

/**
 * 语法糖
 */
public class Autoboxing {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        int sum = 0;
        for (Integer a : list) {
            sum += a;
            System.out.println("a = " + a);
        }
        System.out.println("sum = " + sum);

        test();
        test1();
    }

    public static void test() {
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 3;
        Integer e = 321;
        Integer f = 321;
        Long g = 3L;
        System.out.println(c == d); // true
        System.out.println(e == f); // false
        System.out.println(c == (a + b)); // true
        System.out.println(c.equals(a + b)); // true
        System.out.println(g == (a + b)); // true
        System.out.println(g.equals(a + b)); // false
    }

    public static void test1() {
        if (true) {
            System.out.println("block 1");
        }
        else {
            System.out.println("block 2");
        }
    }
}

/*
public class Autoboxing {
    public Autoboxing() {
    }

    public static void main(String[] args) {
        List list = Arrays.asList(new Integer[]{Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5)});
        int sum = 0;
        Iterator var3 = list.iterator();

        while(var3.hasNext()) {
            Integer a = (Integer)var3.next();
            sum += a.intValue();
            System.out.println("a = " + a);
        }

        System.out.println("sum = " + sum);
        test();
        test1();
    }

    public static void test() {
        Integer a = Integer.valueOf(1);
        Integer b = Integer.valueOf(2);
        Integer c = Integer.valueOf(3);
        Integer d = Integer.valueOf(3);
        Integer e = Integer.valueOf(321);
        Integer f = Integer.valueOf(321);
        Long g = Long.valueOf(3L);
        System.out.println(c == d);
        System.out.println(e == f);
        System.out.println(c.intValue() == a.intValue() + b.intValue());
        System.out.println(c.equals(Integer.valueOf(a.intValue() + b.intValue())));
        System.out.println(g.longValue() == (long)(a.intValue() + b.intValue()));
        System.out.println(g.equals(Integer.valueOf(a.intValue() + b.intValue())));
    }

    public static void test1() {
        System.out.println("block 1");
    }
}
 */
