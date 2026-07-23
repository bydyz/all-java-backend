package org.rc.apiCollect.basicApi.util.Map.BApiCollect;

import java.util.HashMap;
import java.util.Map;

public class put {
    // put(K key, V value)
    // 向 Map 中添加一个键值对。如果该键已经存在，则替换旧的值。

    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("Apple", 1);
        map.put("Banana", 2);
        System.out.println(map); // 输出: {Apple=1, Banana=2}

        // 替换已存在的键的值
        map.put("Apple", 3);
        System.out.println(map); // 输出: {Apple=3, Banana=2}
    }
}
