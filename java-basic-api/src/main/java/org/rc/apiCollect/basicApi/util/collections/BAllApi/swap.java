package org.rc.apiCollect.basicApi.util.collections.BAllApi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class swap {
    // swap(List<?> list, int i, int j)
    // 交换列表中指定位置的两个元素。

    public static void main(String[] args) {
        List<String> list = Arrays.asList("Apple", "Banana", "Orange");
        System.out.println("Original list: " + list);

        Collections.swap(list, 0, 2);
        System.out.println("Swapped list: " + list); // 输出: [Orange, Banana, Apple]
    }
}
