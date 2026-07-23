package org.rc.apiCollect.basicApi.util.Collection.List.BAllApi;

import java.util.ArrayList;
import java.util.List;

public class indexOf {
    public static void main(String[] args) {
        // int indexOf(Object obj):返回 obj 在集合中首次出现的位置



        // 创建 List 集合对象
        List<String> list = new ArrayList<String>();

        // 往 尾部添加 指定元素
        list.add("图图");
        list.add("小美");
        list.add("不高兴");
        System.out.println(list);

        System.out.println(list.indexOf("不高兴"));

    }
}
