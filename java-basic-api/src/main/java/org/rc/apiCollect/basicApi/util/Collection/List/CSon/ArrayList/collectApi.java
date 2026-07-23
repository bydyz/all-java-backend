package org.rc.apiCollect.basicApi.util.Collection.List.CSon.ArrayList;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class collectApi {
    @Test
    public void test0() {
        // ArrayList 是 Java 提供的实现 List 接口的类之一，它使用数组来存储列表中的元素。

        // 创建 List 集合对象
        List<String> list = new ArrayList<>();

        // 往 尾部添加 指定元素    返回 true 表示元素成功添加。
        list.add("图图");
        list.add("小美");
        list.add("不高兴");
        System.out.println(list);
        System.out.println(list.size());

        // 虽说时储存在了数组中，但是不能直接当作数组处理
        // for (int i = 0; i < list.size(); i++) {
        //     System.out.println(list[i]);
        // }

        // String get(int index) 获取指定位置元素
        // 跟 size() 方法一起用 来 遍历的
        for(int i = 0; i<list.size(); i++){
            System.out.println(list.get(i));
        }

        //还可以使用增强 for
        for (String string : list) {
            System.out.println(string);
        }
    }

    @Test
    public void test1() {
        // ArrayList 是 Java 提供的实现 List 接口的类之一，它使用数组来存储列表中的元素。

        // 创建 List 集合对象
        List<String> list = new ArrayList<String>();

        // 往 尾部添加 指定元素    返回 true 表示元素成功添加。
        list.add("图图");
        list.add("小美");
        list.add("不高兴");

        // add(int index,String s) 往指定位置添加
        list.add(1,"没头脑");
        System.out.println(list);
    }

    @Test
    public void test2() {
        // 创建 List 集合对象
        List<String> list = new ArrayList<String>();

        // 往 尾部添加 指定元素
        list.add("图图");
        list.add("小美");
        list.add("不高兴");
        list.add("没头脑");

        // String remove(int index) 删除指定位置元素 返回被删除元素
        // 删除索引位置为 2 的元素
        System.out.println("删除索引位置为 2 的元素");
        System.out.println(list.remove(2));
        System.out.println(list);
    }

    @Test
    public void test3() {
        // 创建 List 集合对象
        List<String> list = new ArrayList<String>();

        // 往 尾部添加 指定元素
        list.add("图图");
        list.add("小美");
        list.add("不高兴");
        list.add("没头脑");

        // String set(int index,String s)
        // 在指定位置 进行 元素替代（改）
        // 修改指定位置元素    返回被替代的元素
        System.out.println(list.set(0, "三毛"));
        System.out.println(list);
    }

    @Test
    public void test4() {
        // 创建 List 集合对象
        List<String> list = new ArrayList<String>();

        // 往 尾部添加 指定元素
        list.add("图图");
        list.add("小美");
        list.add("不高兴");


        List<String> list2 = new ArrayList<>();
        list2.add("111");
        list2.add("222");

        list.addAll(list2);
        System.out.println(list);
    }

    @Test
    public void test5() {
        // 创建 List 集合对象
        List<Object> list = new ArrayList<>();

        // 往 尾部添加 指定元素
        list.add("图图");
        list.add("小美");
        list.add("不高兴");


        List<String> list2 = new ArrayList<>();
        list2.add("111");
        list2.add("222");

        // 要用 add 上面就不能把 list 的成员限定为 String
        list.add(list2);
        System.out.println(list);
    }

}
