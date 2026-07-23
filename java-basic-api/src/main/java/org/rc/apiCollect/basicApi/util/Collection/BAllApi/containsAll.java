package org.rc.apiCollect.basicApi.util.Collection.BAllApi;

import java.util.ArrayList;
import java.util.Collection;

public class containsAll {
    public static void main(String[] args) {
        //ArrayList 是 Collection 的子接口 List 的实现类之一。
        Collection coll = new ArrayList();
        coll.add("小李广");
        coll.add("扫地僧");
        coll.add("石破天");

        Collection coll2 = new ArrayList();

        Collection coll3 = new ArrayList();
        coll3.add("小李广");

        Collection coll4 = new ArrayList();
        coll4.add("小李广");
        coll4.add("狄仁杰");

        System.out.println();

        System.out.println("coll集合：" + coll);
        System.out.println("coll集合中包含 [] 吗：" + coll.containsAll(coll2));        // true
        System.out.println("coll集合中包含 [\"小李广\"] 吗：" + coll.containsAll(coll3));        // true
        System.out.println("coll集合中包含 [\"小李广\", \"狄仁杰\"] 吗：" + coll.containsAll(coll4));        // false
    }
}
