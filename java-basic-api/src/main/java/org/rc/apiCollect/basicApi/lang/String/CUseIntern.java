package org.rc.apiCollect.basicApi.lang.String;

import org.junit.jupiter.api.Test;

public class CUseIntern {
    @Test
    public void test01() {
        // 在字符串常量池中创建了一个字面量为"hello"的字符串。
        String s1 = "hello";
        String s2 = "world";
        // s3 指向字符串常量池中已经创建的"hello world"的字符串。  常量 + 常量 结果在常量池中
        String s3 = "hello" + "world";
        // 原来的“hello”字符串对象已经丢弃了，现在在堆空间中产生了一个字符串 s1 + " world"（也就是"hello world")。
        // 如果多次执行这些改变串内容的操作，会导致大量副本字符串对象存留在内存中，降低效率。如果这样的操作放到循环中，会极
        // 大影响程序的性能。
        String s4 = s1 + "world";
        // 堆空间的 s4 对象在调用 intern()之后，会将常量池中已经存在的"hello world"字符串赋值给 s9。
        String s9 = s4.intern();
        // 变量 + 变量的结果在堆中
        String s5 = s1 + s2;
        String s6 = (s1 + s2).intern();
        String s7 = (s1 + "world").intern();
        String s8 = "hello".concat("world");


        System.out.println(s3 == s4);           // false
        System.out.println(s3 == s5);           // false
        System.out.println(s4 == s5);           // false
        System.out.println(s3 == s6);           // true
        System.out.println(s3 == s7);           // true
        System.out.println(s3 == s8);           // false
        System.out.println(s3 == s9);           // true


        // 常量+常量：结果是常量池。且常量池中不会存在相同内容的常量。
        // 常量与变量 或 变量与变量：结果在堆中
        // 拼接后调用 intern 方法：返回值在常量池中


        // concat 方法拼接，哪怕是两个常量对象拼接，结果也是在堆。



        // 神奇，加了 final 就不一样了    因为加了 final 所以编译期间就可以确定结果，故在常量池
        final String s10 = "aa";
        final String s11 = "bb";
        final String s12 = "aabb";
        String s13 = s10 + "bb";
        String s14 = s10 + s11;
        System.out.println(s13 == s12);         // true
        System.out.println(s13 == s14);         // true
    }
}
