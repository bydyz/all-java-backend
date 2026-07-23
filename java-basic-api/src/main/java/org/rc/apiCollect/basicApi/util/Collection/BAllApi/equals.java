package org.rc.apiCollect.basicApi.util.Collection.BAllApi;

import java.util.ArrayList;
import java.util.Collection;

public class equals {
    public static void main(String[] args) {
        //ArrayList 是 Collection 的子接口 List 的实现类之一。
        Collection coll = new ArrayList();
        coll.add("小李广");

        Collection coll3 = new ArrayList();
        coll3.add("小李广");

        System.out.println();

        System.out.println("是否相等：" + coll.equals(coll3));       // true
    }
}
