package org.rc.apiCollect.basicApi.util.Map.BApiCollect;

import java.util.HashMap;
import java.util.Map;

public class equals {
    // equals(Object o)
    // 判断两个 Map 是否相等。两个 Map 相等当且仅当它们有相同的键值对。

    public static void main(String[] args) {
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("Apple", 1);
        map1.put("Banana", 2);

        Map<String, Integer> map2 = new HashMap<>();
        map2.put("Apple", 1);
        map2.put("Banana", 2);

        System.out.println(map1.equals(map2)); // 输出: true

        map2.put("Orange", 3);
        System.out.println(map1.equals(map2)); // 输出: false
    }
}
