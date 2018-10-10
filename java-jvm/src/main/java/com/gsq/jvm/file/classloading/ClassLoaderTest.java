package com.gsq.jvm.file.classloading;

import java.io.IOException;
import java.io.InputStream;

/**
 * 不用的「ClassLoader」加载的类在虚拟机看来是不同的类
 */
public class ClassLoaderTest {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ClassLoader myLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                String filename = name.substring(name.lastIndexOf(".")+1) + ".class";
                InputStream is = getClass().getResourceAsStream(filename);
                if (is == null) {
                    return super.loadClass(name);
                }

                try {
                    byte[] b = new byte[is.available()];
                    is.read(b);
                    return defineClass(name, b, 0, b.length);
                } catch (IOException e) {
                    throw new ClassNotFoundException(filename);
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Object o = myLoader.loadClass("com.gsq.jvm.file.classloading.ClassLoaderTest").newInstance();
        System.out.println(o.getClass());
        System.out.println(o instanceof com.gsq.jvm.file.classloading.ClassLoaderTest);

        // ClassLoader 不一样
        System.out.println(o.getClass().getClassLoader());
        System.out.println(Thread.currentThread().getContextClassLoader());
    }

}
