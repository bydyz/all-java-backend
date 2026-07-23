package org.rc.apiCollect.basicApi.lang.StringBuffer;

public class ALearn {
    // 字符串相关类之可变字符序列：StringBuffer、StringBuilder

    // 因为 String 对象是不可变对象，虽然可以共享常量对象，但是对于频繁字符串的修改和拼接操作，效率极低，空间消耗也比较高。因此，JDK 又在 java.lang
    // 包提供了可变字符序列 StringBuffer 和 StringBuilder 类型。





    // java.lang.StringBuffer 代表可变的字符序列，JDK1.0 中声明，可以对字符串内容进行增删，此时不会产生新的对象。
    // StringBuilder 和 StringBuffer 非常类似，均代表可变的字符序列，而且提供相关功能的方法也一样。
    // 区分 String、StringBuffer、StringBuilder
    //     – String:不可变的字符序列； 底层使用 char[]数组存储(JDK8.0 中)
    //     – StringBuffer:可变的字符序列；线程安全（方法有 synchronized 修饰），效率低；底层使用 char[]数组存储 (JDK8.0 中)
    //     – StringBuilder:可变的字符序列； jdk1.5 引入，线程不安全的，效率高；底层使用 char[]数组存储(JDK8.0 中)

    // StringBuilder、StringBuffer 的 API 是完全一致的，并且很多方法与 String 相同。
}
