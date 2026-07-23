package org.rc.apiCollect.basicApi.util.Collection.Set;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class HashSetExampleTwo {
    public static void main(String[] args) {
        List list = new ArrayList();
        list.add(Integer.valueOf(1));
        list.add(Integer.valueOf(2));
        list.add(Integer.valueOf(2));
        list.add(Integer.valueOf(4));
        list.add(Integer.valueOf(4));
        System.out.println(list);

        List list2 = duplicateList(list);

        System.out.println(list2);
        for (Object integer : list2) {
            System.out.println(integer);
        }
    }

    public static List duplicateList(List list) {
        HashSet set = new HashSet();
        // HashSet 对 Array List 用 addAll        但是后面还是用 new ArrayList()转换了一下
        set.addAll(list);
        System.out.println(set);

        // 将 HashSet 转换为 ArrayList          public ArrayList(Collection<? extends E> c)
        return new ArrayList(set);
    }
}
