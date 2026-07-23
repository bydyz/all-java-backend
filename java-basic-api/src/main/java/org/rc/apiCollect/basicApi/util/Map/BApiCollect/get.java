package org.rc.apiCollect.basicApi.util.Map.BApiCollect;

import java.util.HashMap;
import java.util.Map;

public class get {
    // get(Object key)
    // 根据指定的键获取对应的值。如果键不存在，则返回 null 或者是默认值（取决于具体的 Map 实现）。

    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("Apple", 1);
        map.put("Banana", 2);

        System.out.println(map.get("Apple")); // 输出: 1
        System.out.println(map.get("Orange")); // 输出: null
    }
}
