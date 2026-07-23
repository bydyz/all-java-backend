package org.rc.apiCollect.basicApi.util.Collection.BAllApi;

import java.util.ArrayList;
import java.util.Collection;

public class addAll {
    public static void main(String[] args) {
        Collection c1 = new ArrayList();
        c1.add(1);
        c1.add(2);
        System.out.println("c1 集合元素的个数：" + c1.size());//2
        System.out.println("c1 = " + c1);
        System.out.println();

        Collection other = new ArrayList();
        other.add(1);
        other.add(2);
        other.add(3);

        // addAll  拆散加入
        c1.addAll(other);
        System.out.println("c1 集合元素的个数：" + c1.size());//5
        System.out.println("c1.addAll(other) = " + c1);
        // add  当成一个加入
        c1.add(other);
        System.out.println("c2 集合元素的个数：" + c1.size());//3
        System.out.println("c2.add(other) = " + c1);

    }
}
