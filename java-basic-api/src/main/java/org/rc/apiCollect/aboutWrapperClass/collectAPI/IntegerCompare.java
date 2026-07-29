package org.rc.apiCollect.aboutWrapperClass.collectAPI;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class IntegerCompare {
    // Integer.compare 是 Java 中的一个静态方法，用于比较两个 Integer 类型的值。这个方法遵循 Comparator<Integer> 接口，返回一个整数，表示两个整数的比较结果。

    // public static int compare(Integer x, Integer y)
        // x: 要比较的第一个整数。
        // y: 要比较的第二个整数。

        // 如果 x 小于 y，则返回 -1。
        // 如果 x 大于 y，则返回 1。
        // 如果 x 和 y 相等，则返回 0。

    @Test
    public void test01() {
        int a = 10;
        int b = 20;
        int result = Integer.compare(a, b);
        System.out.println(result);         // 输出: -1，因为 10 < 20

        result = Integer.compare(b, a);
        System.out.println(result);         // 输出: 1，因为 20 > 10

        result = Integer.compare(a, a);
        System.out.println(result);         // 输出: 0，因为 10 == 10
    }


    @Test
    public void test02() {
        Integer[] numbers = {5, 3, 9, 1};
        Arrays.sort(numbers, (x, y) -> Integer.compare(x, y));
        // 或者更简洁地使用：
        // Arrays.sort(numbers, Integer::compare);

        System.out.println(Arrays.toString(numbers));   // 输出: [1, 3, 5, 9]
    }


    // Integer.compare 方法在比较时会考虑 null 值。如果方法的任一参数为 null，则会抛出 NullPointerException。
    // 对于原始 int 类型的值，应使用 Integer.valueOf 方法将它们转换为 Integer 对象，然后再使用 compare 方法。
    // compare 方法是 java.lang.Comparable 接口的一部分，许多 Java 基本类型包装类（如 Integer、Long、Double 等）都重写了这个方法，以提供相应的比较功能。
}
