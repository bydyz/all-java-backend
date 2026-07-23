package org.rc.apiCollect.basicApi.util.Map.CExample;

import java.util.HashMap;

public class Example4 {
    // 统计字符串中每个字符出现的次数
    public static void main(String[] args) {
        String str = "aaaabbbcccccccccc";
        char[] arr = str.toCharArray();             // 将字符串转换成字符数组
        HashMap map = new HashMap();                // 创建双列集合存储键和值
        for (char c : arr) {                        // 遍历字符数组
            if (!map.containsKey(c)) {              // 如果不包含这个键
                map.put(c, 1);                      // 就将键和值为 1 添加
            } else {                                // 如果包含这个键
                map.put(c, (int)map.get(c) + 1);    // 就将键和值再加 1 添加进来
            }
        }
        for (Object key : map.keySet()) {           // 遍历双列集合
            System.out.println(key + "=" + map.get(key));
        }
    }
}
