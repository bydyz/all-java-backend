package org.rc.apiCollect.basicApi.util.Collection.BAllApi;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public class foreach {
    public static void main(String[] args) {
        String[] str = new String[5];
        for (String myStr : str) {
            myStr = "atguigu";
            System.out.println(myStr);
        }
        for (int i = 0; i < str.length; i++) {
            // 打印的全是 null
            System.out.println(str[i]);
        }
    }

    @Test
    public void test01(){
        Collection coll = new ArrayList();
        coll.add("小李广");
        coll.add("扫地僧");
        coll.add("石破天");
        //foreach 循环其实就是使用 Iterator 迭代器来完成元素的遍历的。
        for (Object o : coll) {
            System.out.println(o);
        }
    }

    @Test
    public void test02(){
        int[] nums = {1,2,3,4,5};
        for (int num : nums) {
            System.out.println(num);
        }
        System.out.println("-----------------");
        String[] names = {"张三","李四","王五"};
        for (String name : names) {
            System.out.println(name);
        }
    }

    @Test
    public void test03(){
        Collection coll = new ArrayList();
        coll.add("小李广");
        coll.add("扫地僧");
        coll.add("石破天");


        // 定义一个 Consumer，用于打印元素
        Consumer<String> printConsumer = new Consumer<String>() {
            @Override
            public void accept(String item) {
                System.out.println(item);
            }
        };
        coll.forEach(printConsumer);



        System.out.println();



        Consumer<String> print = System.out::println;
        coll.forEach(print);



        System.out.println();



        // 使用Lambda 表达式
        coll.forEach(e -> System.out.println(e));
    }

}
