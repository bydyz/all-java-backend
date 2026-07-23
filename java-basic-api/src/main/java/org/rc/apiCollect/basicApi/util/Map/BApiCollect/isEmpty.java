package org.rc.apiCollect.basicApi.util.Map.BApiCollect;

import java.util.HashMap;
import java.util.Map;

public class isEmpty {
    // isEmpty()
    // 判断 Map 是否为空。

    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();

        System.out.println(map.isEmpty()); // 输出: true

        map.put("Apple", 1);
        System.out.println(map.isEmpty()); // 输出: false
    }
}
