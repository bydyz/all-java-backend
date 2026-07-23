package org.rc.apiCollect.basicApi.util.Map.ALearn;

public class ALearn {
    // Map 与 Collection 并列存在。用于保存具有映射关系的数据：key-value
    //     – Collection 集合称为单列集合，元素是孤立存在的（理解为单身）。
    //     – Map 集合称为双列集合，元素是成对存在的(理解为夫妻)。

    // Map 中的 key 和 value 都可以是 任何引用类型 的数据。但常用 String 类作为 Map的“键”。
    // Map 接口的常用实现类：HashMap、LinkedHashMap、TreeMap 和 `Properties。其中，HashMap 是 Map 接口使用频率最高的实现类。


    // Map 中的 key 用 Set 来存放，不允许重复，即同一个 Map 对象所对应的类，须重写 hashCode()和 equals()方法
    // Map 中的 value 用 Collection 来存放
    // Map 中的 Entry 用 Set 来存放

    // key 和 value 之间存在单向一对一关系，即通过指定的 key 总能找到唯一的、确定的 value，不同 key 对应的 value 可以重复。
    //     value 所在的类要重写 equals()方法。

    // key 和 value 构成一个 entry。所有的 entry 彼此之间是无序的、不可重复的。






    // put：添加或更新键值对。
    // putAll：批量添加键值对。
    // containsKey 和 containsValue：检查键或值是否存在。
    // isEmpty：判断是否为空。
    // equals：比较两个 Map 是否相等。
    // size：获取键值对的数量。
    // clear：清空 Map。
    // get：根据键获取值。
    // remove：根据键移除键值对。
    // keySet：获取所有键的集合。
    // values：获取所有值的集合。
    // entrySet：获取所有键值对的集合。
}
