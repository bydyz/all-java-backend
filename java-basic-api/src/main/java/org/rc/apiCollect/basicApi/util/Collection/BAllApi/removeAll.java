package org.rc.apiCollect.basicApi.util.Collection.BAllApi;

import java.util.ArrayList;
import java.util.Collection;

public class removeAll {
    public static void main(String[] args) {
        //ArrayList 是 Collection 的子接口 List 的实现类之一。
        Collection coll = new ArrayList();
        coll.add("小李广");
        coll.add("小李广2");

        Collection coll3 = new ArrayList();
        coll3.add("小李广");

        System.out.println(coll);

        coll.removeAll(coll3);

        System.out.println(coll);
    }
}
