package org.rc.apiCollect.basicApi.util.Map;

public class Hashtable1 {
    // • Hashtable 是 Map 接口的古老实现类，JDK1.0 就提供了。不同于 HashMap，Hashtable 是线程安全的。
    // • Hashtable 实现原理和 HashMap 相同，功能相同。底层都使用哈希表结构（数组 + 单向链表），查询速度快。
    // • 与 HashMap 一样，Hashtable 也不能保证其中 Key-Value 对的顺序
    // • Hashtable 判断两个 key 相等、两个 value 相等的标准，与 HashMap 一致。
    // • 与 HashMap 不同，Hashtable 不允许使用 null 作为 key 或 value。
    //
    // 面试题：Hashtable 和 HashMap 的区别
    // HashMap:底层是一个哈希表（jdk7:数组+链表;jdk8:数组+链表+红黑树）,是一个线程不安全的集合,执行效率高
    // Hashtable:底层也是一个哈希表（数组+链表）,是一个线程安全的集合,执行效率低
    // HashMap 集合:可以存储 null 的键、null 的值
    // Hashtable 集合,不能存储 null 的键、null 的值
    // Hashtable 和 Vector 集合一样,在 jdk1.2 版本之后被更先进的集合(HashMap,ArrayList)取代了。
    //     所以 HashMap 是 Map 的主要实现类，Hashtable 是 Map 的古老实现类。
    // Hashtable 的子类 Properties（配置文件）依然活跃在历史舞台
    // Properties 集合是一个唯一和 IO 流相结合的集合
}
