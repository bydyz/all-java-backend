package org.rc.apiCollect.basicApi.util.Collection.List.BAllApi;

import java.util.ArrayList;
import java.util.List;

public class get {
    public static void main(String[] args) {
        // Object get(int index):获取指定 index 位置的元素



        // 创建 List 集合对象
        List<String> list = new ArrayList<>();

        // 往 尾部添加 指定元素
        list.add("图图");
        list.add("小美");
        list.add("不高兴");
        System.out.println(list);

        System.out.println(list.get(1));

    }
}
