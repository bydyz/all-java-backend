package org.rc.apiCollect.basicApi.util.Collection.BAllApi;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public class removeIf {
    @Test
    public void test01(){
        Collection coll = new ArrayList();
        coll.add("小李广");
        coll.add("扫地僧");
        coll.add("石破天");
        coll.add("佛地魔");
        System.out.println("coll = " + coll);
        coll.removeIf(new Predicate() {
            @Override
            public boolean test(Object o) {
                String str = (String) o;
                return str.contains("地");
            }
        });
        System.out.println("删除包含\"地\"字的元素之后 coll = " + coll);



        // Predicate接口有默认方法，也有静态方法，但是只有一个抽象方法，故也是函数式接口
        Predicate<String> a = new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s.contains("天");
            }
        };
        coll.removeIf(a);
        System.out.println(coll);

    }
}
