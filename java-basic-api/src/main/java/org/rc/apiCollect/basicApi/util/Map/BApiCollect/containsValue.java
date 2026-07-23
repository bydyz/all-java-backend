package org.rc.apiCollect.basicApi.util.Map.BApiCollect;

import java.util.HashMap;
import java.util.Map;

public class containsValue {
    // containsValue(Object value)
    // 检查 Map 是否包含指定的值。

    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("Apple", 1);
        map.put("Banana", 2);

        System.out.println(map.containsValue(1)); // 输出: true
        System.out.println(map.containsValue(3)); // 输出: false
    }
}
