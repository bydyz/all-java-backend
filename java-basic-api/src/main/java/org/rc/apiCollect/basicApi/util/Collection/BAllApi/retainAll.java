package org.rc.apiCollect.basicApi.util.Collection.BAllApi;

import java.util.ArrayList;
import java.util.Collection;

public class retainAll {
    public static void main(String[] args) {
        // 从当前集合
        // 中删除两个集合中不同的元素，使得当前集合仅保留与 coll 集合中的元素相同
        // 的元素，即当前集合中仅保留两个集合的交集，即 this = this ∩ coll；

        Collection coll = new ArrayList();
        coll.add("小李广");
        coll.add("扫地僧");
        coll.add("石破天");
        coll.add("佛地魔");
        System.out.println("coll = " + coll);

        Collection other = new ArrayList();
        other.add("小李广");
        other.add("扫地僧");
        other.add("尚硅谷");
        System.out.println("other = " + other);

        coll.retainAll(other);
        System.out.println("coll.retainAll(other)之后，coll = " + coll);
        System.out.println("coll.retainAll(other)之后，other = " + other);

    }
}
