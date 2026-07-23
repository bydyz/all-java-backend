package org.rc.apiCollect.basicApi.lang.String.collectAPI;

import org.junit.jupiter.api.Test;

public class getBytes {
    // public byte[] getBytes()
    // getBytes() 是 Java 中 String 类的一个方法，这个方法不接受任何参数，用于将字符串转换成字节数组。这个方法按照平台默认的字符编码来编码字符串。

    // getBytes() 方法使用的是平台默认的字符编码来编码字符串。这意味着在不同的平台上，相同的字符串可能会生成不同的字节序列。
    // 如果需要指定特定的字符编码，应该使用重载的 getBytes(String charsetName) 方法，例如 str.getBytes("UTF-8")。
    // 返回的字节数组是原字符串的副本，对字节数组的修改不会影响到原始字符串。
    // 如果字符串包含无法映射到字节的字符（在使用非标准编码时），则可能会抛出 UnsupportedEncodingException。


    @Test
    public void test01() {
        String str = "Hello World";
        byte[] bytes = str.getBytes();

        // 打印字节数组的内容
        for (byte b : bytes) {
            System.out.print((b & 0xFF) + " ");
        }
        // 输出可能类似于: 72 101 108 108 111 32 87 111 114 108 100



        // byte[] bytesUTF8 = str.getBytes("UTF-8");
        // byte[] bytesISO8859_1 = str.getBytes("ISO-8859-1");

        // 打印不同编码的字节数组内容
        // System.out.println("UTF-8: " + Arrays.toString(bytesUTF8));
        // System.out.println("ISO-8859-1: " + Arrays.toString(bytesISO8859_1));
    }


    @Test
    public void test02() {
        String str = "中国";
        // System.out.println(str.getBytes("ISO8859-1").length);// 2
        // // ISO8859-1 把所有的字符都当做一个 byte 处理，处理不了多个字节
        // System.out.println(str.getBytes("GBK").length);// 4 每一个中文都是对应 2 个字节
        // System.out.println(str.getBytes("UTF-8").length);// 6 常规的中文都是 3 个字

        // System.out.println(new String(str.getBytes("ISO8859-1"), "ISO8859-1"));// 乱码
        // System.out.println(new String(str.getBytes("GBK"), "GBK"));// 中国
        // System.out.println(new String(str.getBytes("UTF-8"), "UTF-8"));//中国
    }
}

