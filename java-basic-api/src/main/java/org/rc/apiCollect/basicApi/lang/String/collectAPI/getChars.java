package org.rc.apiCollect.basicApi.lang.String.collectAPI;

import org.junit.jupiter.api.Test;

public class getChars {
    // public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin)
        // srcBegin: 字符串中的起始索引（包含），从这个位置开始复制字符。
        // srcEnd: 字符串中的结束索引（不包含），在这个位置停止复制字符。
        // dst: 目标字符数组，用于存放从字符串复制的字符。
        // dstBegin: 目标数组中的起始索引（包含），从这个位置开始存放复制的字符。

    // Java 中 String 类的一个方法，用于将字符串的一部分（从 srcBegin 到 srcEnd-1）复制到一个指定的字符数组 dst 中，从 dstBegin 索引开始放置。

    // 如果 srcEnd 比 srcBegin 小，此方法将抛出 StringIndexOutOfBoundsException。
    // 如果 dst 数组没有足够的空间来存放从 srcBegin 到 srcEnd-1 的字符，此方法将抛出 ArrayIndexOutOfBoundsException。
    // 此方法不会修改原始字符串。
    // 如果 srcBegin 等于 srcEnd，则不会复制任何字符到 dst 数组中。
    // getChars 方法可以用于精确控制字符串的哪一部分被复制，以及复制到目标数组的哪个位置。


    @Test
    public void test01() {
        String str = "Hello World";
        char[] charArray = new char[10];

        // 从索引 0 开始复制 6 个字符到 charArray，从索引 2 开始存放
        str.getChars(0, 6, charArray, 2);
        // charArray 现在变成了 [0, 0, 'H', 'e', 'l', 'l', 'o', 0, 0, 0]

        // 打印字符数组
        for (char c : charArray) {
            System.out.print(c + " ");
        }
        // 输出: 0 0 H e l l o   0 0          倒数第三个是 空格
    }
}
