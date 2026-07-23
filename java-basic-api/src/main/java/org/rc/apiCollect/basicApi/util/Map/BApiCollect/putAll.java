package org.rc.apiCollect.basicApi.util.Map.BApiCollect;

import java.util.HashMap;
import java.util.Map;

public class putAll {
    // putAll(Map<? extends K, ? extends V> m)
    // 将指定 Map 中的所有映射关系复制到此 Map 中。

    public static void main(String[] args) {
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("Apple", 1);
        map1.put("Banana", 2);

        Map<String, Integer> map2 = new HashMap<>();
        map2.put("Apple", 2);
        map2.put("Orange", 3);
        map2.put("Grape", 4);

        map1.putAll(map2);
        System.out.println(map1); // 输出: {Apple=2, Banana=2, Orange=3, Grape=4}
    }
}
