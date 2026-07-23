package org.rc.apiCollect.basicApi.util.collections.BAllApi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class replaceAll {
    // replaceAll(List<T> list, T oldVal, T newVal)
    // 将列表中所有等于 oldVal 的元素替换为 newVal。

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 2, 3, 2);
        System.out.println("Original list: " + list);

        Collections.replaceAll(list, 2, 4);
        System.out.println("List after replaceAll: " + list); // 输出: [1, 4, 4, 3, 4]
    }
}
