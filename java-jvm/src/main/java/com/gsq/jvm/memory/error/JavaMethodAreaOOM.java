package com.gsq.jvm.memory.error;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 方法区测试
 *
 * VM Args: -XX:PermSize=5M -XX:MaxPermSize=7M (1.8 以前)
 * VM Args: -XX:MetaspaceSize=5M -XX:MaxMetaspaceSize=7M (1.8 之后)
 *
 */
public class JavaMethodAreaOOM {

    public static void main(String[] args) {
        while (true) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(OOMObject.class);
            enhancer.setUseCache(false);
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                    return methodProxy.invoke(o, objects);
                }
            });
            enhancer.create();
        }
    }

    static class OOMObject {

    }
}

/*
Exception in thread "main" java.lang.OutOfMemoryError: Metaspace
 */
