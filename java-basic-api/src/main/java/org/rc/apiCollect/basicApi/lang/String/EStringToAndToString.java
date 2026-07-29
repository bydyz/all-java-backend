package org.rc.apiCollect.basicApi.lang.String;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class EStringToAndToString {
    // 字符串 --> 基本数据类型、包装类：
    //     • Integer 包装类的 public static int parseInt(String s)：可以将由“数字”字符组成的字符串转换为整型。
    //     • 类似地，使用 java.lang 包中的 Byte、Short、Long、Float、Double 类调相应的类方
    //         法可以将由“数字”字符组成的字符串，转化为相应的基本数据类型。
    @Test
    public void test0() {
        String intStr = "123";
        String floatStr = "123.45";
        String boolStr = "true";

        // 字符串转基本数据类型
        int intValue = Integer.parseInt(intStr);
        float floatValue = Float.parseFloat(floatStr);
        boolean boolValue = Boolean.parseBoolean(boolStr);

        System.out.println("String to int: " + intValue); // 输出：123
        System.out.println("String to float: " + floatValue); // 输出：123.45
        System.out.println("String to boolean: " + boolValue); // 输出：true

        // 字符串转包装类
        Integer intObj = Integer.valueOf(intStr);
        Float floatObj = Float.valueOf(floatStr);
        Boolean boolObj = Boolean.valueOf(boolStr);

        System.out.println("String to Integer: " + intObj); // 输出：123
        System.out.println("String to Float: " + floatObj); // 输出：123.45
        System.out.println("String to Boolean: " + boolObj); // 输出：true
    }


    // 基本数据类型、包装类 --> 字符串：
    //     • 调用 String 类的 public String valueOf(int n)可将 int 型转换为字符串
    //     • 相应的 valueOf(byte b)、valueOf(long l)、valueOf(float f)、valueOf(double d)、
    //         valueOf(boolean b)可由参数的相应类型到字符串的转换。
    @Test
    public void test1() {
        int intValue = 123;
        float floatValue = 123.45f;
        boolean boolValue = true;

        // 基本数据类型转字符串
        String intStr = String.valueOf(intValue);
        String floatStr = String.valueOf(floatValue);
        String boolStr = String.valueOf(boolValue);

        System.out.println("int to String: " + intStr); // 输出：123
        System.out.println("float to String: " + floatStr); // 输出：123.45
        System.out.println("boolean to String: " + boolStr); // 输出：true

        // 包装类转字符串
        Integer intObj = Integer.valueOf(123);
        Float floatObj = Float.valueOf(123.45f);
        Boolean boolObj = Boolean.valueOf(true);

        String intObjStr = intObj.toString();
        String floatObjStr = floatObj.toString();
        String boolObjStr = boolObj.toString();

        System.out.println("Integer to String: " + intObjStr); // 输出：123
        System.out.println("Float to String: " + floatObjStr); // 输出：123.45
        System.out.println("Boolean to String: " + boolObjStr); // 输出：true
    }
    
    
    // 字符数组 --> 字符串：
    //     • String 类的构造器：String(char[]) 和 String(char[]，int offset，int length) 分别用字
    //         符数组中的全部字符和部分字符创建字符串对象。
    @Test
    public void test2() {
        char[] charArray = {'H', 'e', 'l', 'l', 'o'};

        // 字符数组转字符串
        String str = new String(charArray);

        System.out.println("Char array to String: " + str); // 输出：Hello
    }


    // 字符串 --> 字符数组：
    //     • public char[] toCharArray()：将字符串中的全部字符存放在一个字符数组中的方法。
    //     • public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin)：提供了将指定索
    //         引范围内的字符串存放到数组中的方法。
    @Test
    public void test3() {
        String str = "Hello";

        // 字符串转字符数组
        char[] charArray = str.toCharArray();

        for (char c : charArray) {
            System.out.print(c + " "); // 输出：H e l l o
        }
    }


    // 字符串 --> 字节数组：（编码）
    //     • public byte[] getBytes() ：使用平台的默认字符集将此 String 编码为 byte 序列，并
    //         将结果存储到一个新的 byte 数组中。
    //     • public byte[] getBytes(String charsetName) ：使用指定的字符集将此 String 编码到
    //         byte 序列，并将结果存储到新的 byte 数组。
    @Test
    public void test4() {
        String str = "Hello, 世界"; // 包含中文字符以展示不同编码效果

        // 字符串转字节数组（UTF-8 编码）
        byte[] utf8Bytes = str.getBytes(StandardCharsets.UTF_8);
        System.out.print("String to byte array (UTF-8): ");
        for (byte b : utf8Bytes) {
            System.out.printf("%02X ", b); // 输出：48 65 6C 6C 6F 2C 20 E4 B8 96 E7 95 8C
        }
        System.out.println();

        // 字符串转字节数组（ISO-8859-1 编码）
        byte[] isoBytes = str.getBytes(StandardCharsets.ISO_8859_1);
        System.out.print("String to byte array (ISO-8859-1): ");
        for (byte b : isoBytes) {
            System.out.printf("%02X ", b); // 输出：48 65 6C 6C 6F 2C 20 EF BF BD EF BF BD
        }
        System.out.println();
    }

    // 字节数组 --> 字符串：（解码）
    //     • String(byte[])：通过使用平台的默认字符集解码指定的 byte 数组，构造一个新的String。
    //     • String(byte[]，int offset，int length) ：用指定的字节数组的一部分，即从数组起始位
    //         置 offset 开始取 length 个字节构造一个字符串对象。
    //     • String(byte[], String charsetName ) 或 new String(byte[], int, int,String
    //         charsetName )：解码，按照指定的编码方式进行解码。
    @Test
    public void test5() {
        byte[] utf8Bytes = {72, 101, 108, 108, 111, 44, 32, -28, -67, -95, -27, -91, -67}; // Hello, 世界 in UTF-8
        // ISO-8859-1 是一个单字节编码方案，不支持中文字符，因此这个数组实际上并不正确地表示 "Hello, 世界"。
        byte[] isoBytes = {72, 101, 108, 108, 111, 44, 32, -17, -65, -65, -17, -67, -65}; // Hello, 世界 in ISO-8859-1

        for (byte b : utf8Bytes) {
            System.out.printf("%02X ", b & 0xFF); // 确保输出无符号字节值
        }
        // 字节数组转字符串（UTF-8 解码）
        String utf8Str = new String(utf8Bytes, StandardCharsets.UTF_8);
        System.out.println("Byte array to String (UTF-8): " + utf8Str); // 输出：Hello, 佡好

        // 字节数组转字符串（ISO-8859-1 解码）
        String isoStr = new String(isoBytes, StandardCharsets.ISO_8859_1);
        System.out.println("Byte array to String (ISO-8859-1): " + isoStr); // 输出：Hello, ï¿¿ï½¿
    }
}
