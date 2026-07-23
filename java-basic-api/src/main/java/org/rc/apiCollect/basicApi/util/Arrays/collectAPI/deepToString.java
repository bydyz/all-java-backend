package org.rc.apiCollect.basicApi.util.Arrays.collectAPI;

import java.util.Arrays;
// Module java.base
// Package java.util

public class deepToString {
    public static void main(String[] args) {
        int[][] my2DArray = {{1, 2, 3}, {4, 5, 6}};
        // Arrays.deepToString 是 Java 中的一个便捷方法，用于将多维数组（例如二维数组或更高维度的数组）转换成一个易于阅读的字符串表示形式。
        // 如果数组中的元素是基本数据类型（如 int、double 等），Arrays.deepToString 将自动将它们包装在相应的包装类中（如 Integer、Double 等）。
        // 如果数组的某个维度是 null，Arrays.deepToString 将输出 null 而不是抛出异常。
        // 对于对象数组，Arrays.deepToString 将调用对象的 toString 方法来获取其字符串表示。

        // Arrays.deepToString(param)       param 需要对象数组    int[] a = {1, 2, 4};不是对象数组
        System.out.println(Arrays.deepToString(my2DArray));


    }
}
