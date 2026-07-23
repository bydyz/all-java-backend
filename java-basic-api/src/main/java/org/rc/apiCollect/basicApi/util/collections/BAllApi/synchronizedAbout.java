package org.rc.apiCollect.basicApi.util.collections.BAllApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class synchronizedAbout {
    // synchronizedXXX(Collection<T> c)
    // 返回一个线程安全的集合包装器。对于每个基本操作，它都提供了内置的同步机制。

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        List<String> synchronizedList = Collections.synchronizedList(list);

        // 线程安全的操作
        synchronized (synchronizedList) {
            synchronizedList.add("Apple");
            synchronizedList.add("Banana");
        }

        System.out.println("Synchronized list: " + synchronizedList); // 输出: [Apple, Banana]
    }
}
