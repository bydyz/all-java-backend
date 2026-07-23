package org.rc.apiCollect.basicApi.util.Collection.List.BAllApi;

import java.util.ArrayList;
import java.util.List;

public class lastIndexOf {
    public static void main(String[] args) {
        // int lastIndexOf(Object obj):返回 obj 在当前集合中末次出现的位置



        // 创建 List 集合对象
        List<String> list = new ArrayList<String>();

        // 往 尾部添加 指定元素
        list.add("图图");
        list.add("小美");
        list.add("不高兴");
        list.add("小美");
        System.out.println(list);

        System.out.println(list.lastIndexOf("小美"));

    }
}
