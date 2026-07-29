package org.rc.apiCollect.basicApi.util.Map.BApiCollect;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class entrySet {
    // entrySet()
    // 返回 Map 中所有键值对的集合视图，每个元素都是一个 Map.Entry<K, V> 对象。

    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("Apple", 1);
        map.put("Banana", 2);

        // Mpa.Entry
        // Map.Entry 是一个内部接口，用于表示映射（Map）中的一个键值对（key-value pair）
        // entrySet() 方法返回的是一个 Set 集合，其中的元素是 Map.Entry 对象。
        // Map.Entry 接口提供了 getKey() 和 getValue() 两个方法，分别用于获取映射条目的键和值。
        // 使用 Map.Entry 可以方便地访问和操作映射中的键和值。
        // Map.Entry 对象是不可变的，它们的 getKey() 和 getValue() 方法返回的值不能被修改。
        Set<Map.Entry<String, Integer>> entries = map.entrySet();
        for (Map.Entry<String, Integer> entry : entries) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }

        // 输出:
        // Apple = 1
        // Banana = 2
    }

    @Test
    public void test0() {
        Map<String, Integer> map = new HashMap<>();
        map.put("Apple", 1);
        map.put("Banana", 2);

        Set entrySet = map.entrySet();
        for (Object mapping : entrySet) {
            //System.out.println(entry);
            Map.Entry entry = (Map.Entry) mapping;
            System.out.println(entry.getKey() + "->" + entry.getValue());
        }
    }
}
