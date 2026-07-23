package org.rc.apiCollect.basicApi.util.Collection.List.BAllApi;

import java.util.ArrayList;
import java.util.List;

public class addAll {
    public static void main(String[] args) {
        // boolean addAll(int index, Collection eles):从 index 位置开始将 eles 中的所有元素添加进来


        // 创建 List 集合对象
        List<String> list = new ArrayList<String>();

        // 往 尾部添加 指定元素
        list.add("图图");
        list.add("小美");
        list.add("不高兴");
        System.out.println(list);


        List<String> list1 = new ArrayList<>();
        list1.add("图图1");
        list1.add("小美1");

        boolean a = list.addAll(1, list1);
        System.out.println(list);       // [图图, 图图1, 小美1, 小美, 不高兴]
        System.out.println(a);          // true

    }
}
