package org.rc.apiCollect.basicApi.util.Map;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LinkedHashMap1 {
    // • LinkedHashMap 是 HashMap 的子类
    // • 存储数据采用的哈希表结构+链表结构，在 HashMap 存储结构的基础上，使用了一对
    //     双向链表来记录添加元素的先后顺序，可以保证遍历元素时，与添加的顺序一致。
    // • 通过哈希表结构可以保证键的唯一、不重复，需要键所在类重写 hashCode()方法、equals()方法。

    public static void main(String[] args) {
        LinkedHashMap map = new LinkedHashMap();
        map.put("王五", 13000.0);
        map.put("张三", 10000.0);

        //key 相同，新的 value 会覆盖原来的 value
        //因为 String 重写了 hashCode 和 equals 方法
        map.put("张三", 12000.0);

        map.put("李四", 14000.0);

        //HashMap 支持 key 和 value 为 null 值
        String name = null;
        Double salary = null;
        map.put(name, salary);

        System.out.println(map);

        Set entrySet = map.entrySet();
        for (Object obj : entrySet) {
            Map.Entry entry = (Map.Entry)obj;
            System.out.println(entry);
        }
    }

}
