package org.rc.apiCollect.basicApi.util.Map.BApiCollect;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class values {
    // values()
    // 返回 Map 中所有值的集合视图。

    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("Apple", 1);
        map.put("Banana", 2);

        Collection<Integer> values = map.values();
        System.out.println(values); // 输出: [1, 2]
    }
}
