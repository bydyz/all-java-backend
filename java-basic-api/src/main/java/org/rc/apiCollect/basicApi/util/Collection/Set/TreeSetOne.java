package org.rc.apiCollect.basicApi.util.Collection.Set;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class TreeSetOne {
    @Test
    public void test0(){
        // ArrayList 会按照add时的顺序填入
        List set = new ArrayList();
        set.add("MM");
        set.add("CC");
        set.add("AA");
        set.add("DD");
        set.add("ZZ");

        // TreeSet集合 调用 iterator方法
        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());

            // 输出
            // MM
            // CC
            // AA
            // DD
            // ZZ

        }
    }



    /*
     * 自然排序：针对 String 类的对象
     * */
    @Test
    public void test1(){
        TreeSet set = new TreeSet();
        set.add("MM");
        set.add("CC");
        set.add("AA");
        set.add("DD");
        set.add("ZZ");

        // 向 TreeSet 中添加的应该是同一个类的对象。
        // set.add(123); //报 ClassCastException 的异常

        // TreeSet集合 调用 iterator方法
        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());

            // 输出
            // AA
            // CC
            // DD
            // MM
            // ZZ

        }
    }



    /*
     * 自然排序：针对 User 类的对象
     * */
    @Test
    public void test2(){
        TreeSet set = new TreeSet();
        set.add(new User("Tom",12));
        set.add(new User("Rose",23));
        set.add(new User("Jerry",2));
        set.add(new User("Eric",18));
        set.add(new User("Tommy",44));
        set.add(new User("Jim",23));
        set.add(new User("Maria",18));
        set.add(new User("Maria",28));

        // set.add("Tom");   // 报 ClassCastException 的异常

        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }

        // TreeSet 使用 contains 方法
        System.out.println(set.contains(new User("Jack", 23))); //true
    }

}

class User implements Comparable{
    String name;
    int age;

    public User() {
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    /*
    举例：按照 age 从小到大的顺序排列，如果 age 相同，则按照 name 从大到小的顺序排列
    this - user  表示升序                   - (this - user)  表示降序
    * */
    @Override
    public int compareTo(Object o) {
        if(this == o){
            return 0;
        }
        if(o instanceof User user){
            int value = this.age - user.age;
            if(value != 0){
                return -value;          // 升序
                // return value;        // 降序
            }
            // name是String 故下面是调用 String 的compareTo 方法
            return -this.name.compareTo(user.name);
        }

        throw new RuntimeException("输入的类型不匹配");
    }
}
