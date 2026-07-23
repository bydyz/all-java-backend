package org.rc.apiCollect.basicApi.lang.String.collectAPI;

public class compareTo {
    public static void main(String[] args) {
        // public int compareTo(String other)
        // 在 Java 中，compareTo(String other) 方法是 String 类的一个实例方法，用于比较两个字符串的字典顺序。
        // 这个方法实现了 Comparable 接口中定义的 compareTo 方法，允许字符串可以用于排序集合和进行自然排序。

        // 这个方法返回一个整数，表示调用对象（即 this 字符串）与参数字符串 other 的字典顺序关系：
            // 如果返回值是负数，那么 this 在字典顺序上小于 other。
            // 如果返回值是 0，那么 this 和 other 相等（在内容上完全相同）。
            // 如果返回值是正数，那么 this 在字典顺序上大于 other。


        // 字典顺序（也称为词序或词典序）是一种字符串比较的方法，通常按照字符的 Unicode 值进行比较。比较从两个字符串的第一个字符开始，按照以下规则进行：
            // 如果两个字符串的当前字符相同，比较下一个字符。
            // 如果一个字符串在某个位置结束，而另一个字符串在这个位置或之后还有字符，则更长的字符串更大。
            // 如果两个字符串在所有对应的字符位置上都相同，并且长度也相同，则认为它们相等。


        String str1 = "Hello";
        String str2 = "World";
        String str3 = "hello";

        // 似乎大小写的比较有些问题！！！！！！！！！！！

        // ASCII值，小写字符 - 大写字母 = 32              H 72    W  87     h  104

        int result1 = str1.compareTo(str2); // 返回负数
        System.out.println(result1);

        int result2 = str1.compareTo(str3); // 返回负数
        System.out.println(result2);

        int result3 = str3.compareTo(str1); // 返回正数
        System.out.println(result3);

        int result4 = str1.compareTo(str1); // 返回 0
        System.out.println(result4);
    }
}
