package org.rc.apiCollect.basicApi.util.Collection.Set;

import java.util.HashSet;
import java.util.Random;

public class HashSetExampleThree {
    public static void main(String[] args) {
        HashSet hs = new HashSet(); // 创建集合对象
        Random r = new Random();
        while (hs.size() < 10) {
            int num = r.nextInt(20) + 1;    // 生成 1 到 20 的随机数
            hs.add(num);
        }
        for (Object integer : hs) { // 遍历集合
            System.out.println(integer); // 打印每一个元素
        }
    }
}
