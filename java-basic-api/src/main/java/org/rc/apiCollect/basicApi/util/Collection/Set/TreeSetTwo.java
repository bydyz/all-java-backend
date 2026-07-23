package org.rc.apiCollect.basicApi.util.Collection.Set;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

public class TreeSetTwo {
    /*
     * 定制排序
     * */
    @Test
    public void test3(){
        //按照 User 的姓名的从小到大的顺序排列
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                // u1 - u2 升序           u2 - u1 降序
                if(o1 instanceof User u1 && o2 instanceof User u2){
                    int value = u1.age - u2.age;
                    if(value != 0){
                        return value;
                    }
                    return u1.name.compareTo(u2.name);
                }
                throw new RuntimeException("输入的类型不匹配");
            }
        };
        TreeSet set = new TreeSet(comparator);

        set.add(new User("Tom",12));
        set.add(new User("Rose",23));
        set.add(new User("Jerry",2));
        set.add(new User("Eric",18));
        set.add(new User("Tommy",44));
        set.add(new User("Jim",23));
        set.add(new User("Maria",18));
        System.out.println("111   " + set);

        // 似乎即使有这个 add 也没效果，原因呢？？？   因为少了20-23行用age的比较，因此只会用nane来确定添加的是否是相同的元素
        set.add(new User("Maria",28));
        System.out.println("222   " + set);

        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }
}
