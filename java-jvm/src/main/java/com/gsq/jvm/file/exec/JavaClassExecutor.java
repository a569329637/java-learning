package com.gsq.jvm.file.exec;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 通过修改源文件字节码来获取打印
 * 通过把
 */
public class JavaClassExecutor {
    private static String fileName = "/Users/guishangquan/shell/Hello.class";
    private static String methodName = "main";

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        HackSystem.clearBuffer();

        HotSwapClassLoader classLoader = new HotSwapClassLoader();
        byte[] bytes = loadClass();
        ClassModifier classModifier = new ClassModifier(bytes);
        bytes = classModifier.modifyUTF8Constant("java/lang/System", "com/gsq/jvm/file/exec/HackSystem");
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

        String s = HackSystem.getBufferString();
        System.out.println("s = " + s);
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
