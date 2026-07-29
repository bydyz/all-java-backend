# Java 常用数据结构及 API

## 目录

- [1. List（列表）](#1-list列表)
  - [1.1 ArrayList](#11-arraylist)
  - [1.2 LinkedList](#12-linkedlist)
- [2. Set（集合）](#2-set集合)
  - [2.1 HashSet](#21-hashset)
  - [2.2 TreeSet](#22-treeset)
- [3. Map（映射）](#3-map映射)
  - [3.1 HashMap](#31-hashmap)
- [4. Queue（队列）](#4-queue队列)
  - [4.1 PriorityQueue](#41-priorityqueue)
- [5. Java 8+ Stream API 操作](#5-java-8-stream-api-操作)
- [6. 场景选择指南](#6-场景选择指南)

---

## 1. List（列表）

有序集合，允许重复元素，可通过索引访问。

### 1.1 ArrayList

**底层结构**：动态数组（Object[]），支持随机访问。

**适用场景**：频繁随机读取，尾部增删。

```java
import java.util.ArrayList;
import java.util.List;

public class ArrayListDemo {
    public static void main(String[] args) {
        // 创建 ArrayList，初始容量为 10（默认）
        List<String> list = new ArrayList<>();

        // 添加元素
        list.add("Java");        // 尾部添加
        list.add("Python");
        list.add(0, "Go");      // 指定索引添加，后续元素右移

        // 获取元素（O(1) 时间复杂度）
        String first = list.get(0);  // "Go"

        // 修改元素
        list.set(1, "Rust");     // 将索引1的元素改为 "Rust"

        // 删除元素
        list.remove("Python");   // 按值删除（首次匹配）
        list.remove(0);          // 按索引删除

        // 查询
        boolean hasJava = list.contains("Java");  // true
        int index = list.indexOf("Java");          // 查找索引，不存在返回 -1
        int size = list.size();                    // 元素个数

        // 遍历
        for (String item : list) {
            System.out.println(item);
        }

        // 转为数组
        String[] array = list.toArray(new String[0]);
    }
}
```

### 1.2 LinkedList

**底层结构**：双向链表，每个节点有 prev 和 next 指针。

**适用场景**：频繁在头部或中间插入删除，可作为队列/双端队列使用。

```java
import java.util.LinkedList;
import java.util.Queue;
import java.util.Deque;

public class LinkedListDemo {
    public static void main(String[] args) {
        LinkedList<String> list = new LinkedList<>();

        // 作为普通 List 使用
        list.add("A");
        list.addFirst("B");      // 头部添加 → [B, A]
        list.addLast("C");       // 尾部添加 → [B, A, C]

        String first = list.getFirst();  // "B"
        String last = list.getLast();    // "C"

        list.removeFirst();  // 移除头部 → [A, C]
        list.removeLast();   // 移除尾部 → [A]

        // 作为队列（FIFO）使用
        Queue<String> queue = new LinkedList<>();
        queue.offer("任务1");   // 入队（尾部）
        queue.offer("任务2");
        String head = queue.poll();  // 出队（头部）→ "任务1"

        // 作为双端队列（Deque）使用
        Deque<String> deque = new LinkedList<>();
        deque.push("栈底");    // 入栈（头部）
        deque.push("栈顶");
        String top = deque.pop();  // 出栈（头部）→ "栈顶"
    }
}
```

---

## 2. Set（集合）

不允许重复元素的集合。

### 2.1 HashSet

**底层结构**：基于 HashMap（key 存储元素，value 为固定值）。

**适用场景**：快速去重，不关心顺序。

```java
import java.util.HashSet;
import java.util.Set;

public class HashSetDemo {
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();

        // 添加元素
        set.add("Java");
        set.add("Python");
        set.add("Java");       // 重复，不会被添加

        System.out.println(set.size());  // 2

        // 查询（O(1) 时间复杂度）
        boolean exists = set.contains("Java");  // true

        // 删除
        set.remove("Python");

        // 判空
        boolean empty = set.isEmpty();

        // 遍历
        for (String lang : set) {
            System.out.println(lang);
        }

        // 集合运算
        Set<String> setA = new HashSet<>();
        Set<String> setB = new HashSet<>();
        // ... 添加元素后

        // 交集
        setA.retainAll(setB);

        // 并集
        setA.addAll(setB);

        // 差集
        setA.removeAll(setB);
    }
}
```

### 2.2 TreeSet

**底层结构**：红黑树（TreeMap），元素自动排序。

**适用场景**：需要有序集合，或按自然顺序/自定义比较器排序。

```java
import java.util.TreeSet;
import java.util.Comparator;

public class TreeSetDemo {
    public static void main(String[] args) {
        // 自然排序（升序）
        TreeSet<Integer> nums = new TreeSet<>();
        nums.add(30);
        nums.add(10);
        nums.add(20);

        System.out.println(nums);  // [10, 20, 30] 自动排序

        // 获取首尾元素
        int first = nums.first();  // 10
        int last = nums.last();    // 30

        // 范围查询
        nums.subSet(15, true, 25, true);  // [20] (>=15 且 <=25)
        nums.headSet(20);                 // [10] (<20)
        nums.tailSet(20);                 // [20, 30] (>=20)

        // 自定义排序（降序）
        TreeSet<String> names = new TreeSet<>(Comparator.reverseOrder());
        names.add("Charlie");
        names.add("Alice");
        names.add("Bob");

        System.out.println(names);  // [Charlie, Bob, Alice] 降序

        // 删除
        nums.pollFirst();  // 移除最小元素
        nums.pollLast();   // 移除最大元素
    }
}
```

---

## 3. Map（映射）

键值对存储，键不允许重复。

### 3.1 HashMap

**底层结构**：数组 + 链表 + 红黑树（JDK 1.8+），链表长度超过 8 时转为红黑树。

**适用场景**：快速键值查找，不关心顺序。

```java
import java.util.HashMap;
import java.util.Map;

public class HashMapDemo {
    public static void main(String[] args) {
        Map<String, Integer> scores = new HashMap<>();

        // 添加/修改
        scores.put("Alice", 90);
        scores.put("Bob", 85);
        scores.putIfAbsent("Charlie", 80);  // 仅当键不存在时添加
        scores.put("Alice", 95);            // 覆盖已有值

        // 获取
        int score = scores.get("Alice");         // 95
        int value = scores.getOrDefault("Dave", 0);  // 键不存在返回默认值

        // 删除
        scores.remove("Bob");              // 按键删除
        scores.remove("Charlie", 80);      // 仅当值匹配时删除

        // 查询
        boolean hasKey = scores.containsKey("Alice");      // true
        boolean hasValue = scores.containsValue(95);       // true
        int size = scores.size();

        // 遍历
        // 方式1：遍历键值对（推荐）
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // 方式2：遍历键
        for (String key : scores.keySet()) {
            System.out.println(key + ": " + scores.get(key));
        }

        // 方式3：Java 8 forEach
        scores.forEach((key, value) -> System.out.println(key + ": " + value));

        // 获取所有键/值
        var keys = scores.keySet();       // Set<String>
        var values = scores.values();     // Collection<Integer>
    }
}
```

---

## 4. Queue（队列）

先进先出（FIFO）的数据结构。

### 4.1 PriorityQueue

**底层结构**：小顶堆（数组实现的完全二叉树），每次出队最小元素。

**适用场景**：任务调度、Top-K 问题、Dijkstra 算法。

```java
import java.util.PriorityQueue;
import java.util.Comparator;

public class PriorityQueueDemo {
    public static void main(String[] args) {
        // 小顶堆（默认，自然排序）
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        minHeap.offer(30);
        minHeap.offer(10);
        minHeap.offer(20);

        System.out.println(minHeap.peek());  // 10（队首，最小元素）
        System.out.println(minHeap.poll());  // 10 出队
        System.out.println(minHeap.poll());  // 20 出队

        // 大顶堆（自定义比较器）
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        maxHeap.offer(30);
        maxHeap.offer(10);
        maxHeap.offer(20);

        System.out.println(maxHeap.poll());  // 30 出队（最大元素）

        // 实际场景：按优先级处理任务
        class Task {
            String name;
            int priority;

            Task(String name, int priority) {
                this.name = name;
                this.priority = priority;
            }
        }

        PriorityQueue<Task> taskQueue = new PriorityQueue<>(
            Comparator.comparingInt(t -> t.priority)
        );
        taskQueue.offer(new Task("低优先级", 1));
        taskQueue.offer(new Task("高优先级", 10));
        taskQueue.offer(new Task("中优先级", 5));

        // 按优先级出队：高优先级 → 中优先级 → 低优先级
        while (!taskQueue.isEmpty()) {
            Task task = taskQueue.poll();
            System.out.println(task.name);
        }
    }
}
```

---

## 5. Java 8+ Stream API 操作

Stream API 提供函数式风格的集合操作，代码更简洁。

```java
import java.util.*;
import java.util.stream.*;
import java.util.function.*;

public class StreamDemo {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // ========== 过滤 ==========
        // filter: 保留满足条件的元素
        List<String> filtered = names.stream()
            .filter(name -> name.length() > 3)  // 长度 > 3
            .collect(Collectors.toList());       // Alice, Charlie, David

        // ========== 转换 ==========
        // map: 将每个元素转换为另一个值
        List<String> upper = names.stream()
            .map(String::toUpperCase)           // 方法引用
            .collect(Collectors.toList());       // ALICE, BOB, CHARLIE...

        // ========== 排序 ==========
        List<String> sorted = names.stream()
            .sorted()                           // 自然排序
            .collect(Collectors.toList());

        List<String> descSorted = names.stream()
            .sorted(Comparator.reverseOrder())  // 降序
            .collect(Collectors.toList());

        // ========== 聚合 ==========
        int sum = numbers.stream()
            .reduce(0, Integer::sum);           // 求和 → 55

        Optional<Integer> max = numbers.stream()
            .max(Integer::compareTo);           // 最大值

        Optional<Integer> min = numbers.stream()
            .min(Integer::compareTo);           // 最小值

        long count = numbers.stream()
            .filter(n -> n % 2 == 0)            // 偶数
            .count();                           // 5

        // ========== 收集 ==========
        // toList
        List<Integer> evenList = numbers.stream()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());

        // toSet
        Set<Integer> evenSet = numbers.stream()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toSet());

        // joining: 字符串拼接
        String joined = names.stream()
            .collect(Collectors.joining(", "));  // "Alice, Bob, Charlie, David, Eve"

        // groupingBy: 分组
        Map<Integer, List<String>> byLength = names.stream()
            .collect(Collectors.groupingBy(String::length));
        // {3=[Bob, Eve], 5=[Alice, David], 7=[Charlie]}

        // partitioningBy: 分区（true/false 两组）
        Map<Boolean, List<Integer>> partition = numbers.stream()
            .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        // {false=[1,3,5,7,9], true=[2,4,6,8,10]}

        // toMap: 转为 Map
        Map<String, Integer> nameLengthMap = names.stream()
            .collect(Collectors.toMap(
                name -> name,               // key: 原名
                String::length             // value: 长度
            ));

        // ========== 去重 ==========
        List<Integer> withDups = Arrays.asList(1, 2, 2, 3, 3, 3);
        List<Integer> unique = withDups.stream()
            .distinct()
            .collect(Collectors.toList());  // [1, 2, 3]

        // ========== limit / skip（分页） ==========
        List<Integer> page = numbers.stream()
            .skip(5)       // 跳过前5个
            .limit(3)      // 取3个
            .collect(Collectors.toList());  // [6, 7, 8]
    }
}
```

---

## 6. 场景选择指南

```
┌─────────────────────────────────────────────────────────────────────┐
│                     数据结构选择决策树                               │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  需要键值对？ ──Yes──▶ HashMap                                       │
│       │                                                             │
│       No                                                            │
│       │                                                             │
│  需要去重？ ──Yes──▶ HashSet                                         │
│       │                                                             │
│       No                                                            │
│       │                                                             │
│  需要排序？ ──Yes──▶ TreeSet / TreeMap                               │
│       │                                                             │
│       No                                                            │
│       │                                                             │
│  频繁随机访问？ ──Yes──▶ ArrayList                                    │
│       │                                                             │
│       No                                                            │
│       │                                                             │
│  频繁头部/中间增删？ ──Yes──▶ LinkedList                               │
│       │                                                             │
│       No                                                            │
│       │                                                             │
│  需要优先级/Top-K？ ──Yes──▶ PriorityQueue                            │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 快速对照表

| 场景 | 推荐数据结构 | 时间复杂度（查找） |
|------|-------------|-------------------|
| 随机访问元素 | `ArrayList` | O(1) |
| 频繁在头部增删 | `LinkedList` | O(1) |
| 快速去重 | `HashSet` | O(1) |
| 需要有序集合 | `TreeSet` | O(log n) |
| 键值对存储 | `HashMap` | O(1) |
| 按优先级处理 | `PriorityQueue` | O(1) 取顶 |
| 线程安全 Map | `ConcurrentHashMap` | O(1) |

### 性能提示

```java
// ✅ 预估容量，避免频繁扩容
List<String> list = new ArrayList<>(10000);
Map<String, Integer> map = new HashMap<>(10000);

// ✅ 遍历时使用 iterator，避免 ConcurrentModificationException
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String item = it.next();
    if (item.length() < 3) {
        it.remove();  // 安全删除
    }
}

// ✅ 大量数据用 Stream，小数据直接循环（Stream 有额外开销）
```

---

## 参考资料

- [Java 官方文档 - Collections Framework](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/package-summary.html)
- 《Effective Java》第 3 版 - Joshua Bloch
