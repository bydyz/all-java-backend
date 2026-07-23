package org.rc.apiCollect.basicApi.util.collections.BAllApi;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class sort {
    // sort(List<T> list) 和 sort(List<T> list, Comparator<? super T> c)
    // 对列表进行排序，默认是升序；也可以使用自定义比较器。

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(3, 1, 4, 1, 5, 9);
        System.out.println("Original list: " + list);

        Collections.sort(list);
        System.out.println("Sorted list (ascending): " + list); // 输出: [1, 1, 3, 4, 5, 9]

        Collections.sort(list, Comparator.reverseOrder());
        System.out.println("Sorted list (descending): " + list); // 输出: [9, 5, 4, 3, 1, 1]
    }
}
