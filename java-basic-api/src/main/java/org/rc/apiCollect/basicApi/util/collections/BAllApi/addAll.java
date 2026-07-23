package org.rc.apiCollect.basicApi.util.collections.BAllApi;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class addAll {
    // addAll(Collection<? super T> c, T... elements)
    // 将所有指定的元素添加到集合中。

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, "Apple", "Banana", "Orange");
        System.out.println("List after addAll: " + list); // 输出: [Apple, Banana, Orange]
    }
}
