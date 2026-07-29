package org.rc.apiCollect.basicApi.lang.String.collectAPI;

import org.junit.jupiter.api.Test;

public class toCharArray {
    @Test
    public void test01() {
        String a = "amg";

        // 转换字符串为字符数组
        // toCharArray() 方法返回的是原字符串的一个副本，对返回的字符数组所做的任何修改都不会影响到原始字符串
        // 返回的字符数组是新创建的，因此它是一个独立的数组，可以被修改和使用。
        // 如果字符串是空（""），那么 toCharArray() 方法将返回一个长度为 0 的空数组，而不是 null。
        char[] charArray = a.toCharArray();     // ['a', 'm', 'g']

        // amg
        System.out.println(charArray);

        // a m g
        for (char c : charArray) {
            System.out.print(c + " ");
        }
        System.out.println();

        charArray[0] = 'o';
        System.out.println(a);
        System.out.println(charArray);
    }
}
