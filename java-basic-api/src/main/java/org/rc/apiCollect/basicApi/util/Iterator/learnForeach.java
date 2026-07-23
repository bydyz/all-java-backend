package org.rc.apiCollect.basicApi.util.Iterator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

// for(元素的数据类型 局部变量 : Collection 集合或数组){
//     //操作局部变量的输出操作
// }
//这里局部变量就是一个临时变量，自己命名就可以

// 它用于遍历 Collection 和数组。通常只进行遍历元素，不要在遍历的过程中对集合元素进行增删操作。
public class learnForeach {
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
    public void test3() {
        String[] str = new String[5];
        for (String myStr : str) {
            myStr = "atguigu";
            System.out.println(myStr);
        }
        for (int i = 0; i < str.length; i++) {
            System.out.println(str[i]);     // null
        }

    }
}
