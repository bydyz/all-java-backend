package org.rc.apiCollect.basicApi.util.Collection.Set;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;

public class LinkedHashSetOne {
    @Test
    public void test01(){
        LinkedHashSet set = new LinkedHashSet();
        set.add("张三");
        set.add("张三");
        set.add("李四");
        set.add("王五");
        set.add("王五");
        set.add("赵六");

        // 利用 HashSet 添加时是无序的
        System.out.println("set = " + set);     //不允许重复，体现添加顺序
    }
}
