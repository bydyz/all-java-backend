package org.rc.apiCollect.basicApi.lang.String;

// 不可变字符序列：String
// java.lang.String 类代表字符串。Java 程序中所有的字符串文字（例如 "hello" ）都可以看作是实现此类的实例。
// 字符串是常量，用双引号引起来表示。它们的值在创建之后不能更改。
// 字符串 String 类型本身是 final 声明的，意味着我们不能继承 String。


//jdk8 中的 String 源码：
// public final class String
//     implements java.io.Serializable, Comparable<String>, CharSequence {
//     /** The value is used for character storage. */
//     private final char value[]; //String 对象的字符内容是存储在此数组中
//     /** Cache the hash code for the string */
//     private int hash; // Default to 0
//
// – private 意味着外面无法直接获取字符数组，而且 String 没有提供 value 的get 和 set 方法。
// – final 意味着字符数组的引用不可改变，而且 String 也没有提供方法来修改 value 数组某个元素值
// – 因此字符串的字符数组内容也不可变的，即 String 代表着不可变的字符序列。即，一旦对字符串进行修改，就会产生新对象。


// – JDK9 只有，底层使用 byte[]数组。
//     public final class String implements java.io.Serializable, Comparable<String>, CharSequence {
//         @Stable
//         private final byte[] value;
//     }
// //官方说明：... that most String objects contain only Lati
// n-1 characters. Such characters require only one byte of
// storage, hence half of the space in the internal char arr
// ays of such String objects is going unused.
// //细节：... The new String class will store characters enc
// oded either as ISO-8859-1/Latin-1 (one byte per characte
// r), or as UTF-16 (two bytes per character), based upon th
// e contents of the string. The encoding flag will indicate
// which encoding is used.
//
// • Java 语言提供对字符串串联符号（"+"）以及将其他对象转换为字符串的特殊支持（toString()方法）。


import org.junit.jupiter.api.Test;

public class ATextAndCodeExplain {

    @Test
    public void test1() {

    }
}
