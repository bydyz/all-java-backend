package org.rc.apiCollect.basicApi.util.Collection.BAllApi;

import java.util.ArrayList;
import java.util.Collection;

public class remove {
    public static void main(String[] args) {
        //ArrayList 是 Collection 的子接口 List 的实现类之一。
        Collection coll = new ArrayList();
        coll.add("小李广");
        coll.add("扫地僧");
        coll.add("石破天");
        System.out.println();

        System.out.println("coll集合：" + coll);

        boolean a = coll.remove("小李广");
        System.out.println("coll集合：" + coll);
        System.out.println(a);  // true

        boolean b = coll.remove("333");
        System.out.println("coll集合：" + coll);
        System.out.println(b);  // false
    }
}
