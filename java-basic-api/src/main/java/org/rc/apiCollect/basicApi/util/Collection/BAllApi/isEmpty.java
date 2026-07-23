package org.rc.apiCollect.basicApi.util.Collection.BAllApi;

import java.util.ArrayList;
import java.util.Collection;

public class isEmpty {
    public static void main(String[] args) {
        //ArrayList 是 Collection 的子接口 List 的实现类之一。
        Collection coll = new ArrayList();
        coll.add("小李广");
        coll.add("扫地僧");
        coll.add("石破天");
        System.out.println();

        System.out.println(coll);
        System.out.println("coll的isEmpty的值：" + coll.isEmpty());     // false




        Collection coll2 = new ArrayList();
        System.out.println();

        System.out.println(coll2);
        System.out.println("coll的isEmpty的值：" + coll2.isEmpty());    // true
    }
}
