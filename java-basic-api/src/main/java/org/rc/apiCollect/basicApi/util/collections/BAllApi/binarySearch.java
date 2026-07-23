package org.rc.apiCollect.basicApi.util.collections.BAllApi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class binarySearch {
    // binarySearch(List<? extends Comparable<? super T>> list, T key)
    // 在已排序的列表中进行二分查找。如果找到匹配项，则返回其索引；否则返回负数插入点。

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 3, 4, 5, 9);
        Collections.sort(list); // 确保列表已排序

        int index = Collections.binarySearch(list, 4);
        System.out.println("Index of 4: " + index); // 输出: 2

        index = Collections.binarySearch(list, 7);
        System.out.println("Index of 7: " + index); // 输出: -4 (表示7应该插入到索引3的位置)
    }
}
