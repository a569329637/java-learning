package com.gsq.netty.utils;

import java.io.UnsupportedEncodingException;

/**
 * @author guishangquan
 * @date 2019-08-22
 */
public class PrintUtils {

    private static boolean debug = false;

    public static void printBytes(byte[] bytes) {
        if (!debug) {
            return;
        }
        String string = null;
        try {
            string = new String(bytes, CharsetUtils.GBK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("string = " + string + ", bytes = " + bytes.length);
        for (int i = 0; i < bytes.length; ++ i) {
            System.out.print(bytes[i]);
        }
        System.out.println();
    }
}
