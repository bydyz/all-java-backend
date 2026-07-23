package org.rc.apiCollect.basicApi.util.Map.BApiCollect;

import java.util.HashMap;
import java.util.Map;

public class remove {
    // remove(Object key)
    // 根据指定的键移除对应的键值对。

    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("Apple", 1);
        map.put("Banana", 2);

        map.remove("Apple");
        System.out.println(map.containsKey("Apple")); // 输出: false
        System.out.println(map); // 输出: {Banana=2}
    }
}
