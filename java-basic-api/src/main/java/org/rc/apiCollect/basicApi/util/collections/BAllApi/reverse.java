package org.rc.apiCollect.basicApi.util.collections.BAllApi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class reverse {
    // reverse(List<?> list)
    // 反转列表中元素的顺序。

    public static void main(String[] args) {
        List<String> list = Arrays.asList("Apple", "Banana", "Orange");
        System.out.println("Original list: " + list);

        Collections.reverse(list);
        System.out.println("Reversed list: " + list); // 输出: [Orange, Banana, Apple]
    }
}
