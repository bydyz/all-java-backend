package org.rc.apiCollect.basicApi.util.Collection.List.CSon.ArrayList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class ExampleThree {
    public static void main(String[] args) {
        // 创建集合，集合存放随机生成的 30 个小写字母
        Collection list = new ArrayList();
        Random rand = new Random();
        for (int i = 0; i < 30; i++) {
            list.add((char)(rand.nextInt(26) + 97)+"");
        }

        System.out.println(list);
        System.out.println("a:" + listTest(list, "a"));
        System.out.println("b:" + listTest(list, "b"));
        System.out.println("c:" + listTest(list, "c"));
        System.out.println("x:" + listTest(list, "x"));
    }

    // 用 listTest 统计，a、b、c、x 元素的出现次数
    public static int listTest(Collection list, String string) {
        int count = 0;
        for (Object object : list) {
            if(string.equals(object)){
                count++;
            }
        }
        return count;
    }
}
