package org.rc.apiCollect.basicApi.util.Iterator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

// 在程序开发中，经常需要遍历集合中的所有元素。针对这种需求，JDK 专门提供了一个接口 java.util.Iterator。
//     Iterator 接口也是 Java 集合中的一员，但它与Collection、Map 接口有所不同。
//     – Collection 接口与 Map 接口主要用于存储元素
//     – Iterator，被称为迭代器接口，本身并不提供存储对象的能力，主要用于遍历 Collection 中的元素
//
// Collection 接口继承了 java.lang.Iterable 接口，该接口有一个 iterator()方法，那么所有实现了
//     Collection 接口的集合类都有一个 iterator()方法，用以返回一个实现了 Iterator接口的对象。
//     – public Iterator iterator(): 获取集合对应的迭代器，用来遍历集合中的元素的。
//     – 集合对象每次调用 iterator()方法都得到一个全新的迭代器对象，默认游标都在集合的第一个元素之前。
//
// Iterator 接口的常用方法如下：
//     – public E next():返回迭代的下一个元素。
//     – public boolean hasNext():如果仍有元素可以迭代，则返回 true。
//
// 注意：在调用 it.next()方法之前必须要调用 it.hasNext()进行检测。若不调用，且下一
//      条记录无效，直接调用 it.next()会抛出 NoSuchElementException 异常。



// Iterator 迭代器对象在遍历集合时，内部采用指针的方式来跟踪集合中的元素

public class Example1 {
    @Test
    public void test01(){
        Collection coll = new ArrayList();
        coll.add("小李广");
        coll.add("扫地僧");
        coll.add("石破天");

        // 所有实现了Collection 接口的集合类都有一个 iterator()方法，用以返回一个实现了 Iterator接口的对象。
        // public Iterator iterator(): 获取集合对应的迭代器，用来遍历集合中的元素的。
        // 集合对象每次调用 iterator()方法都得到一个全新的迭代器对象，默认游标都在集合的第一个元素之前。
        Iterator iterator = coll.iterator();

        System.out.println(iterator.next());
        System.out.println(iterator.next());
        System.out.println(iterator.next());
        System.out.println(iterator.next()); //报 NoSuchElementException 异常
    }


    @Test
    public void test02(){
        Collection coll = new ArrayList();
        coll.add("小李广");
        coll.add("扫地僧");
        coll.add("石破天");

        // 所有实现了Collection 接口的集合类都有一个 iterator()方法，用以返回一个实现了 Iterator接口的对象。
        // public Iterator iterator(): 获取集合对应的迭代器，用来遍历集合中的元素的。
        // 集合对象每次调用 iterator()方法都得到一个全新的迭代器对象，默认游标都在集合的第一个元素之前。
        Iterator iterator = coll.iterator();//获取迭代器对象
        System.out.println("iterator：" + iterator);

        while(iterator.hasNext()) {//判断是否还有元素可迭代
            System.out.println(iterator.next());//取出下一个元素
            System.out.println(coll);
        }
    }

}
