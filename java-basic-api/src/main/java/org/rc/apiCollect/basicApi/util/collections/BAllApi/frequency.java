package org.rc.apiCollect.basicApi.util.collections.BAllApi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class frequency {
    // frequency(Collection<?> c, Object o)
    // 计算集合中某个元素出现的次数。

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 2, 3, 3, 3);

        int frequencyOf2 = Collections.frequency(list, 2);
        System.out.println("Frequency of 2: " + frequencyOf2); // 输出: 2

        int frequencyOf3 = Collections.frequency(list, 3);
        System.out.println("Frequency of 3: " + frequencyOf3); // 输出: 3
    }
}
