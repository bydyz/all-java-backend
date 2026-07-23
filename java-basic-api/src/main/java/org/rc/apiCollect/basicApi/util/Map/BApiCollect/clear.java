package org.rc.apiCollect.basicApi.util.Map.BApiCollect;

import java.util.HashMap;
import java.util.Map;

public class clear {
    // clear()
    // 清空 Map 中的所有键值对。

    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("Apple", 1);
        map.put("Banana", 2);

        map.clear();
        System.out.println(map.isEmpty()); // 输出: true
    }
}
