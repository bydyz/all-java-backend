package org.rc.apiCollect.basicApi.util.Iterator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class remove {
    public static void main(String[] args) {
        Collection coll = new ArrayList();
        coll.add("小李广");
        coll.add("扫地僧");
        coll.add("石破天");
        coll.add("Tom");

        Iterator iter = coll.iterator();//回到起点
        while(iter.hasNext()){
            Object obj = iter.next();
            if(obj.equals("石破天")){
                // remove 会影响 coll          此 remove() 并不是集合对象的 remove方法
                iter.remove();
            }
        }
        System.out.println(coll);
    }


    // 删除以下集合元素中的偶数
    @Test
    public void test01(){
        Collection coll = new ArrayList();
        coll.add(1);
        coll.add(2);
        coll.add(3);
        coll.add(4);
        coll.add(5);
        coll.add(6);
        Iterator iterator = coll.iterator();

        while(iterator.hasNext()){
            Integer element = (Integer) iterator.next();
            if(element % 2 == 0){
                iterator.remove();
            }
        }
        System.out.println(coll);
    }
}

// 注意：
// • Iterator 可以删除集合的元素，但是遍历过程中通过迭代器对象的 remove 方法，不是集合对象的 remove 方法。
// • 如果还未调用 next()或在上一次调用 next() 方法之后已经调用了 remove() 方法，再调用 remove()都会报 IllegalStateException。
// • Collection 已经有 remove(xx)方法了，为什么 Iterator 迭代器还要提供删除方法呢？因为迭代器的 remove()可以按指定的条件进行删除。
