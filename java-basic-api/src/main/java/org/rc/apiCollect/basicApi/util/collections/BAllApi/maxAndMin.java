package org.rc.apiCollect.basicApi.util.collections.BAllApi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class maxAndMin {
    // max(Collection<? extends T> coll) 和 min(Collection<? extends T> coll)
    // 返回集合中的最大值或最小值，默认是根据自然顺序；也可以使用自定义比较器。

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(3, 1, 4, 1, 5, 9);

        Integer max = Collections.max(list);
        Integer min = Collections.min(list);

        System.out.println("Max value: " + max); // 输出: 9
        System.out.println("Min value: " + min); // 输出: 1
    }
}
