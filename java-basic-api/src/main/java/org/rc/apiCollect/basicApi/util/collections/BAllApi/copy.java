package org.rc.apiCollect.basicApi.util.collections.BAllApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class copy {
    // copy(List<? super T> dest, List<? extends T> src)
    // 将一个列表的内容复制到另一个列表中，目标列表必须至少和源列表一样大。

    public static void main(String[] args) {
        List<Integer> source = Arrays.asList(1, 2, 3);
        List<Integer> destination = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0));

        Collections.copy(destination, source);
        System.out.println("Destination after copy: " + destination); // 输出: [1, 2, 3, 0, 0]
    }
}
