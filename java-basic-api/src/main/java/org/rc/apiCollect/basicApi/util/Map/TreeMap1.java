package org.rc.apiCollect.basicApi.util.Map;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeMap;

public class TreeMap1 {
    // • TreeMap 存储 key-value 对时，需要根据 key-value 对进行排序。TreeMap 可以保证所有的 key-value 对处于有序状态。
    // • TreeSet 底层使用红黑树结构存储数据
    // • TreeMap 的 Key 的排序：
    //     – 自然排序：TreeMap 的所有的 Key 必须实现 Comparable 接口，而且所
    //         有的 Key 应该是同一个类的对象，否则将会抛出 ClasssCastException
    //     – 定制排序：创建 TreeMap 时，构造器传入一个 Comparator 对象，该对
    //         象负责对 TreeMap 中的所有 key 进行排序。此时不需要 Map 的 Key 实现 Comparable 接口
    // • TreeMap 判断两个 key 相等的标准：两个 key 通过 compareTo()方法或者 compare()方法返回 0。



    /*
     * 自然排序举例
     * */
    @Test
    public void test1(){
        TreeMap map = new TreeMap();
        map.put("CC",45);
        map.put("MM",78);
        map.put("DD",56);
        map.put("GG",89);
        map.put("JJ",99);
        // 默认按照 key 进行排序
        System.out.println("000   " + map);

        Set entrySet = map.entrySet();
        for(Object entry : entrySet){
            System.out.println(entry);
        }
    }


    /*
     * 定制排序
     *
     * */
    @Test
    public void test2(){
        //按照 User 的姓名的从小到大的顺序排列
        TreeMap map = new TreeMap(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if(o1 instanceof User u1 && o2 instanceof User u2){
                    // 此处应该是调用 String 的 compareTo 方法吧
                    return u1.name.compareTo(u2.name);
                }
                throw new RuntimeException("输入的类型不匹配");
            }
        });
        map.put(new User("Tom",12), 67);
        map.put(new User("Rose",23), "87");
        map.put(new User("Jerry",2), 88);
        map.put(new User("Eric",18), 45);
        map.put(new User("Tommy",44), 77);
        map.put(new User("Jim",23), 88);
        map.put(new User("Maria",18), 34);
        System.out.println("000   " + map);

        Set entrySet = map.entrySet();
        for(Object entry : entrySet){
            System.out.println(entry);
        }
    }

}


