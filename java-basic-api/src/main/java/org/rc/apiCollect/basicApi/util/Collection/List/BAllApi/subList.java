package org.rc.apiCollect.basicApi.util.Collection.List.BAllApi;

import java.util.ArrayList;
import java.util.List;

public class subList {
    public static void main(String[] args) {
        // List subList(int fromIndex, int toIndex):返回从 fromIndex 到 toIndex 位置的子集合


        // 创建 List 集合对象
        List<String> list = new ArrayList<>();

        // 往 尾部添加 指定元素
        list.add("图图");
        list.add("小美");
        list.add("不高兴");
        list.add("不高兴1");
        list.add("不高兴2");
        System.out.println(list);

        System.out.println(list.subList(1, 3));         // [小美, 不高兴]
        System.out.println(list);                       // [图图, 小美, 不高兴, 不高兴1, 不高兴2]

    }
}
