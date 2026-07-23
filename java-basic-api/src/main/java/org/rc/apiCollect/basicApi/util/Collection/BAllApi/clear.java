package org.rc.apiCollect.basicApi.util.Collection.BAllApi;

import java.util.ArrayList;
import java.util.Collection;

public class clear {
    public static void main(String[] args) {
        //ArrayList 是 Collection 的子接口 List 的实现类之一。
        Collection coll = new ArrayList();
        coll.add("小李广");
        coll.add("扫地僧");
        coll.add("石破天");
        System.out.println();

        System.out.println("coll集合：" + coll);       // [小李广, 扫地僧, 石破天]
        coll.clear();
        System.out.println("coll集合经过clear后的结果：" + coll);    // []
    }
}
