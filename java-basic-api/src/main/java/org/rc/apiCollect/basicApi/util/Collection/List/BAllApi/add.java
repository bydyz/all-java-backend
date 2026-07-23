package org.rc.apiCollect.basicApi.util.Collection.List.BAllApi;

import java.util.ArrayList;
import java.util.List;

public class add {
    public static void main(String[] args) {
        // void add(int index, Object ele):在 index 位置插入 ele 元素



        // 创建 List 集合对象
        List<String> list = new ArrayList<String>();

        // 往 尾部添加 指定元素
        list.add("图图");
        list.add("小美");
        list.add("不高兴");
        System.out.println(list);

        // add(int index,String s) 往指定位置添加
        list.add(1,"没头脑");
        System.out.println(list);

    }
}
