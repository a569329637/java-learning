package com.gsq.jvm.file.exec;

import java.io.*;
import java.nio.channels.Channel;
import java.util.Properties;

/**
 * @author guishangquan
 * @date 2018/10/10
 */
public class HackSystem {

    public final static InputStream in=System.in;

    private static ByteArrayOutputStream buffer=new ByteArrayOutputStream();

    public final static PrintStream out=new PrintStream(buffer);

    public final static PrintStream err=out;

    public static String getBufferString(){

        return buffer.toString();
    }

    public static void clearBuffer(){

        buffer.reset();
    }

    public static void setSecurityManager(final SecurityManager s){

        System.setSecurityManager(s);
    }

    public static SecurityManager getSecurityManager(){

        return System.getSecurityManager();
    }

    public static long currentTimeMills(){

        return System.currentTimeMillis();
    }

    public static void arrayCopy(Object src,int srcPos,Object dest,int destPos,int length){

        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    public static int identityHashCode(Object x){

        return System.identityHashCode(x);
    }


    public static void setIn(InputStream in) {

        System.setIn(in);
    }

    public static void setOut(PrintStream out) {
        System.setOut(out);
    }
    public static Console console() {

        return System.console();
    }

    public static Channel inheritedChannel() throws IOException {

        return System.inheritedChannel();
    }

    public static  long nanoTime(){
        return System.nanoTime();
    };

    public static Properties getProperties() {

        return System.getProperties();
    }

    public static void setProperties(Properties props) {

        System.setProperties(props);
    }

    public static String getProperty(String key) {

        return System.getProperty(key);
    }

    public static String getProperty(String key, String def) {

        return System.getProperty(key, def);
    }


    public static String setProperty(String key, String value) {

        return System.setProperty(key, value);
    }


    public static String clearProperty(String key) {

        return System.clearProperty(key);
    }

    public static String getenv(String name) {

        return System.getenv(name);
    }


    public static java.util.Map<String,String> getenv() {

        return System.getenv();
    }


    public static void exit(int status) {
        System.exit(status);
    }


    public static void gc() {
        System.gc();
    }

    public static void runFinalization() {
        System.runFinalization();
    }

    public static void load(String filename) {

        System.load(filename);
    }

    public static void loadLibrary(String libname) {

        System.loadLibrary(libname);
    }

    public static  String mapLibraryName(String libname){

        return System.mapLibraryName(libname);
    };

}

