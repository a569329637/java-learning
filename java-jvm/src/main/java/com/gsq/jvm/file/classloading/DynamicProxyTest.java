package com.gsq.jvm.file.classloading;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 运行时生成并加载了字节码
 */
public class DynamicProxyTest {
    interface  IHello {
        void sayHello() ;
    }

    static  class Hello implements  IHello{
        @Override
        public void sayHello() {
            System.out.println("hello world");
        }
    }

    static class DynamicProxy implements InvocationHandler {

        Object oriObj ;

        Object bind(Object oriObj){
            this.oriObj = oriObj ;
            return Proxy.newProxyInstance(oriObj.getClass().getClassLoader(), oriObj.getClass().getInterfaces(), this);
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("welcome ");
            return  method.invoke(oriObj, args) ;
            //return null;
        }
    }
    public static void main(String [] args) {
        /**
         * 返回了一个实现了IHello接口并代理了new Hello()实例行为的对象，进行了验证，优化等
         * 生成了字节码，并进行了类的显式加载
         */
        IHello hello = (IHello)new DynamicProxy().bind(new Hello());
        hello.sayHello();
    }
}
