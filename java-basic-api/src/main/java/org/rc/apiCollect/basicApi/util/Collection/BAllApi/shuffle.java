package org.rc.apiCollect.basicApi.util.Collection.BAllApi;

// Collections.shuffle 是 Java 标准库中 java.util.Collections 类的一个静态方法，用于将一个列表（List）中的元素随机打乱。这个方法对列表进行了原地（in-place）的随机置换，意味着它直接修改传入的列表。

// public static void shuffle(List<?> list, Random rnd)
//     list: 要被打乱的列表。
//     rnd: 用于打乱列表的 Random 对象，它定义了随机数生成的种子和算法。

// Collections.shuffle 方法会直接修改传入的列表，不会返回一个新的列表。
// 如果你需要保留原始列表的顺序，你应该在调用 shuffle 之前先复制原始列表。
// 使用 Random 类的实例可以确保随机性的可重复性，特别是在需要可重现的测试场景中。
// 由于 Collections.shuffle 是同步的，频繁地对大型列表使用它可能会影响性能。如果性能是一个问题，可以考虑使用并行算法或其他库。
//
// Collections.shuffle 方法是快速打乱列表元素的有用工具，适用于需要随机顺序的场景，如随机抽样、随机化测试输入等。

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class shuffle {
    public static void main(String[] args) {
        // 创建一个包含一些元素的列表
        List<String> items = new ArrayList<>();
        items.add("Apple");
        items.add("Banana");
        items.add("Cherry");
        items.add("Date");
        items.add("Elderberry");

        // 打印原始列表
        System.out.println("Original list: " + items);

        // 创建一个 Random 对象，用于随机打乱列表
        Random rnd = new Random();

        // 使用 Collections.shuffle 方法打乱列表
        Collections.shuffle(items, rnd);

        // 打印打乱后的列表
        System.out.println("Shuffled list: " + items);
    }
}
