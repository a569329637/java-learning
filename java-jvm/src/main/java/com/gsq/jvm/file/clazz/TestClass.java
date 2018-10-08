package com.gsq.jvm.file.clazz;

/**
 * .class 文件解析
 */
public class TestClass {

    private int m;

    public int inc() {
        return m + 1;
    }
}

/*
// 魔数与class版本
CAFEBABE    // magic 魔数
0000    // minor_version 副版本号 0
0034    // major_version 主版本号 52

// 常量池
0016    // constant_pool_count 常量池数量 22「实际21」
0A 0004 0012 // CONSTANT_Methodref_info #4 #18
09 0003 0013 // CONSTANT_Fieldref_info #3 #19
07 0014 // CONSTANT_Class_info #20
07 0015 // CONSTANT_Class_info #21
01 0001 6D // CONSTANT_Utf8_info 1 m
01 0001 49 // CONSTANT_Utf8_info 1 I
01 0006 3C696E69743E // CONSTANT_Utf8_info 6 <init>
01 0003 282956  // CONSTANT_Utf8_info 3 ()V
01 0004 436F6465    // CONSTANT_Utf8_info 4 Code
01 000F 4C696E654E756D6265725461626C65  // CONSTANT_Utf8_info 15 LineNumberTable
01 0012 4C6F63616C5661726961626C655461626C65   // CONSTANT_Utf8_info 18 LocalVariableTable
01 0004 74686973    // CONSTANT_Utf8_info 4 this
01 0022 4C636F6D2F6773712F6A766D2F66696C652F636C617A7A2F54657374436C6173733B    // CONSTANT_Utf8_info 34 Lcom/gsq/jvm/file/clazz/TestClass;
01 0003 696E63  // CONSTANT_Utf8_info 3 inc
01 0003 282949  // CONSTANT_Utf8_info 3 ()I
01 000A 536F7572636546696C65    // CONSTANT_Utf8_info 10 SourceFile
01 000E 54657374436C6173732E6A617661    // CONSTANT_Utf8_info 14 TestClass.java
0C 0007 0008    // CONSTANT_NameAndType_info 7 8
0C 0005 0006    // CONSTANT_NameAndType_info 5 6
01 0020 636F6D2F6773712F6A766D2F66696C652F636C617A7A2F54657374436C617373    // CONSTANT_Utf8_info 32 com/gsq/jvm/file/clazz/TestClass
01 0010 6A6176612F6C616E672F4F626A656374    // CONSTANT_Utf8_info 16 java/lang/Object

// 访问标志
0021            // access_flags 访问标志

// 类索引、父类索引、接口索引集合
0003            // this_class 3
0004            // super_class 4
0000            // interfaces_count 0
                // interfaces 数组长度是interfaces_count，这里为0所以没有

// 字段集合
0001            // fields_count 字段容量计数器
0002            // access_flags 访问标志 2「ACC_PRIVATE」
0005            // name_index 对常量池的一个有效索引 #5
0006            // descriptor_index 对常量池的一个有效索引 #6
0000            // attributes_count
                // attributes 数组长度是attributes_count，这里为0所以没有

// 方法集合
0002            // 方法容量计数器
0001            // access_flags 访问标志 1「ACC_PUBLIC」
0007            // name_index 对常量池的一个有效索引 #7
0008            // descriptor_index 对常量池的一个有效索引 #8
0001            // attributes_count
0009            // attribute_name_index 属性 #9「Code」 代表Java代码编译成的字节码指令
0000002F        // attribute_length
0001            // max_stack
0001            // max_locals
00000005        // code_length
2AB70001B1      // code
0000            // exception_table_length 0
                // exception_table
0002            // attributes_count 2
    000A        // attribute_name_index #10「LineNumberTable」
    00000006    // atttibute_length 6
    0001        // line_number_table_length 1
        0000    // start_pc 字节码行号
        0007    // line_number Java源代码行号
    000B        // attribute_name_index #11「LocalVariableTable」
    0000000C    // atttibute_length 12
    0001        // local_variable_table_length 1
        0000    // start_pc 0
        0005    // length 5
        000C    // name_index #12「this」
        000D    // descriptor_index #13「Lcom/gsq/jvm/file/clazz/TestClass;」
        0000    // index

0001            // access_flags 访问标志 1「ACC_PUBLIC」
000E            // name_index 对常量池的一个有效索引 #14「inc」
000F            // descriptor_index 对常量池的一个有效索引 #8「()V」
0001            // attributes_count
0009            // attribute_name_index 属性 #9「Code」 代表Java代码编译成的字节码指令
00000031        // attribute_length
0002            // max_stack
0001            // max_locals
00000007        // code_length
2AB400020460AC  // code
0000            // exception_table_length 0
                // exception_table
0002            // attributes_count
    000A        // attribute_name_index #10「LineNumberTable」
    00000006    // atttibute_length 6
    0001        // line_number_table_length 1
        0000    // start_pc 字节码行号
        000C    // line_number Java源代码行号
    000B        // attribute_name_index #11「LocalVariableTable」
    0000000C    // atttibute_length 12
    0001        // local_variable_table_length 1
        0000    // start_pc 0
        0007    // length 7
        000C    // name_index #12「this」
        000D    // descriptor_index #13「Lcom/gsq/jvm/file/clazz/TestClass;」
        0000    // index

000100 10000000 020011
 */

/*
警告: 二进制文件TestClass包含com.gsq.jvm.file.clazz.TestClass
Classfile /Users/guishangquan/repo/github/java-learning/java-jvm/target/classes/com/gsq/jvm/file/clazz/TestClass.class
  Last modified 2018-10-8; size 399 bytes
  MD5 checksum 33c7ceae75eefe4a32fc7a2f1262d2f5
  Compiled from "TestClass.java"
public class com.gsq.jvm.file.clazz.TestClass
  minor version: 0
  major version: 52
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Methodref          #4.#18         // java/lang/Object."<init>":()V
   #2 = Fieldref           #3.#19         // com/gsq/jvm/file/clazz/TestClass.m:I
   #3 = Class              #20            // com/gsq/jvm/file/clazz/TestClass
   #4 = Class              #21            // java/lang/Object
   #5 = Utf8               m
   #6 = Utf8               I
   #7 = Utf8               <init>
   #8 = Utf8               ()V
   #9 = Utf8               Code
  #10 = Utf8               LineNumberTable
  #11 = Utf8               LocalVariableTable
  #12 = Utf8               this
  #13 = Utf8               Lcom/gsq/jvm/file/clazz/TestClass;
  #14 = Utf8               inc
  #15 = Utf8               ()I
  #16 = Utf8               SourceFile
  #17 = Utf8               TestClass.java
  #18 = NameAndType        #7:#8          // "<init>":()V
  #19 = NameAndType        #5:#6          // m:I
  #20 = Utf8               com/gsq/jvm/file/clazz/TestClass
  #21 = Utf8               java/lang/Object
{
  public com.gsq.jvm.file.clazz.TestClass();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 7: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   Lcom/gsq/jvm/file/clazz/TestClass;

  public int inc();
    descriptor: ()I
    flags: ACC_PUBLIC
    Code:
      stack=2, locals=1, args_size=1
         0: aload_0
         1: getfield      #2                  // Field m:I
         4: iconst_1
         5: iadd
         6: ireturn
      LineNumberTable:
        line 12: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       7     0  this   Lcom/gsq/jvm/file/clazz/TestClass;
}
SourceFile: "TestClass.java"
 */
