package com.gsq.jvm.file.exec;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 直接通过设置 PrintStream 来获取打印
 */
public class JavaClassExecutor1 {

    private static String fileName = "/Users/guishangquan/shell/Hello.class";
    private static String methodName = "main";

    public static void main(String[] args) throws ClassNotFoundException, IOException {

        // 输出到文件
//        File file = new File("/Users/guishangquan/shell/Hello.log");
//        FileOutputStream fos = new FileOutputStream(file);
//        PrintStream printStream = new PrintStream(fos);
//        HackSystem.setOut(printStream);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);
        baos.reset();

        HotSwapClassLoader classLoader = new HotSwapClassLoader();
        byte[] bytes = loadClass();
        Class clazz = classLoader.loadByte(bytes);

        try {
            Method method = clazz.getMethod(methodName, String[].class);
            method.invoke(null, new String[] { null });
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        String s = baos.toString();
        baos.close();
    }

    public static byte[] loadClass() throws IOException {
        FileInputStream fis = new FileInputStream(fileName);
        byte[] bytes = new byte[fis.available()];
        try {
            fis.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fis.close();
        }
        return bytes;
    }
}
