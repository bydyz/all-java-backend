package org.rc.apiCollect.basicApi.util.Collection.BAllApi;

import java.util.ArrayList;
import java.util.Collection;

public class add {
    public static void main(String[] args) {
        //ArrayList 是 Collection 的子接口 List 的实现类之一。
        Collection coll = new ArrayList();
        coll.add("小李广");
        coll.add("扫地僧");
        coll.add("石破天");
        System.out.println();

        System.out.println("coll集合中的元素个数：" + coll.size());
        System.out.println(coll);



        Collection coll2 = new ArrayList();
        coll2.add(1);
        coll2.add(2);



        // add  当成一个加入
        coll.add(coll2);

        System.out.println();
        System.out.println(coll);


        // addAll  拆散加入
        coll.addAll(coll2);

        System.out.println();
        System.out.println(coll);
    }
}
