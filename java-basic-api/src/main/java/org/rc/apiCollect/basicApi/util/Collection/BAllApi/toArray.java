package org.rc.apiCollect.basicApi.util.Collection.BAllApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class toArray {
    public static void main(String[] args) {
        // 返回包含当前集合中所有元素的数组

        Collection coll = new ArrayList();
        coll.add("小李广");
        coll.add("扫地僧");
        coll.add("石破天");
        coll.add("佛地魔");

        //集合转换为数组：集合的 toArray()方法
        Object[] objects = coll.toArray();

        System.out.println("用数组返回 coll 中所有元素：" + Arrays.toString(objects));

        //对应的，数组转换为集合：调用 Arrays 的 asList(Object ...objs)
        Object[] arr1 = new Object[]{123,"AA","CC"};
        Collection list = Arrays.asList(arr1);
        System.out.println(list);
    }
}
