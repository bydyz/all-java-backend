package org.rc.apiCollect.basicApi.util.Map.BApiCollect;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class keySet {
    // keySet()
    // 返回 Map 中所有键的集合视图。

    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("Apple", 1);
        map.put("Banana", 2);

        Set<String> keys = map.keySet();
        System.out.println(keys); // 输出: [Apple, Banana]
    }
}
