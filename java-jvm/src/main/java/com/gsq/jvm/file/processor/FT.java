package com.gsq.jvm.file.processor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guishangquan
 * @date 2018/10/10
 */
public class FT {
    public static class A_B_C {

    }
    public static String ab_sd="";

    public static int method(List<Integer> list) {
        System.out.println("method(List<Integer>list)");
        return 1;
    }

    public static void main(String[] args) {
        method(new ArrayList<Integer>());
        Long aLong = 2l;
        Integer aInteger = 1;
        Integer bInteger = 1;
        System.out.println(aLong.equals(aInteger + bInteger));
        System.out.println(aInteger.equals(1L));
        System.out.println("true");
    }
}
