package org.rc.apiCollect.basicApi.util.Collection.Set;
// 在一个 List 集合中存储了多个无大小顺序并且有重复的字符串，定义一个方法，让其有序(从小到大排序)，并且不能去除重复元素。

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class TreeSetThree {
    public static void main(String[] args) {
        ArrayList list = new ArrayList();
        list.add("ccc");
        list.add("ccc");
        list.add("aaa");
        list.add("aaa");
        list.add("bbb");
        list.add("ddd");
        list.add("ddd");

        sort(list);

        System.out.println(list);
    }

    /*
     * 对集合中的元素排序,并保留重复
     */
    public static void sort(List list) {
        TreeSet ts = new TreeSet(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) { // 重写 compare方法
                String s1 = (String)o1;
                String s2 = (String)o2;
                int num = s1.compareTo(s2); // 比较内容
                // return num;      // 升序且不重复
                // return -num;        // 降序且不重复
                // return num == 0 ? 1 : num;   // 升序且允许重复
                return num == 0 ? 1 : -num;  // 降序且允许重复
            }
        });
        ts.addAll(list);    // 将 list 集合中的所有元素添加到 ts 中
        list.clear();       // 清空 list
        list.addAll(ts);    // 将 ts 中排序并保留重复的结果在添加到 list 中
    }

}
