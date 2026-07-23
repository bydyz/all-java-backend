package org.rc.apiCollect.basicApi.util.collections.BAllApi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class shuffle {
    // shuffle(List<?> list)
    // 随机打乱列表中元素的顺序。

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        System.out.println("Original list: " + list);

        Collections.shuffle(list);
        System.out.println("Shuffled list: " + list); // 输出: 随机排列后的列表
    }
}
