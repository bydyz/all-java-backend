`Set` 是 Java 集合框架中用于存储**不重复元素**的集合。它最大的价值在于**自动去重**，其所有 API 的设计都围绕“唯一性”这一核心约束展开。

### 📌 Set 的三大核心特点

1. **元素唯一性**：这是 `Set` 最根本的契约。任何实现类都保证集合中不会出现两个“相等”的元素（依据 `equals` 和 `hashCode` 判断）。`add()` 方法在遇到重复元素时会拒绝添加并返回 `false`，而非抛出异常。
2. **无索引**：`Set` 没有像 `List` 那样的 `get(int index)` 方法，不能通过下标访问元素。这意味着遍历 `Set` 必须使用迭代器、增强 for 循环或 `forEach` 方法。
3. **顺序性因实现而异**：`Set` 接口本身不规定顺序，但不同的实现类提供了不同的顺序保证：`HashSet` 完全不保证顺序；`LinkedHashSet` 保证**插入顺序**；`TreeSet` 保证**排序顺序**。

---

### 🗂️ 三大通用实现类：如何选择？

| 实现类 | 底层结构 | 顺序保证 | 性能（add/contains/remove） | 允许 `null` | 核心适用场景 |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **`HashSet`** | 哈希表 | **无** | **O(1)**，最快 | 允许一个 | 仅需去重，不关心顺序。**最常用** |
| **`LinkedHashSet`** | 哈希表 + 双向链表 | **插入顺序** | 接近 O(1)，略慢于 HashSet | 允许一个 | 去重且需要保留元素的添加顺序 |
| **`TreeSet`** | 红黑树 | **排序顺序** | **O(log n)**，较慢 | **不允许**（抛 NPE） | 去重且需要元素自动排序（自然序或定制序） |

> **选择口诀**：绝大多数情况用 `HashSet`；想保留顺序用 `LinkedHashSet`；需要排序用 `TreeSet`。

---

### 🛠️ Set 核心 API 详解与代码示例

`Set` 接口继承自 `Collection`，其方法语义在“唯一性”约束下有了特殊的表现。

#### ✍️ 添加元素：`boolean add(E e)`

**特点**：如果元素尚不存在，则添加并返回 `true`；如果元素已存在，则**不添加**并返回 `false`。**返回值是判断是否发生去重的关键**。

```java
Set<String> set = new HashSet<>();
System.out.println(set.add("Java")); // true (首次添加)
System.out.println(set.add("Python")); // true
System.out.println(set.add("Java")); // false (已存在，拒绝添加)
System.out.println(set); // 输出可能为 [Java, Python] (顺序不定)
```

#### 🗑️ 删除元素：`boolean remove(Object o)`

**特点**：如果元素存在，则删除并返回 `true`；如果不存在，则返回 `false`。

```java
Set<String> set = new HashSet<>(Set.of("A", "B", "C"));
System.out.println(set.remove("B")); // true
System.out.println(set.remove("X")); // false (不存在)
System.out.println(set); // [A, C]
```

#### 🔍 查询元素：`boolean contains(Object o)`

**特点**：判断集合是否包含指定元素。`HashSet` 的查找是 O(1) 的，这是它相比 `List` 的巨大优势。

```java
Set<String> set = new HashSet<>(Set.of("Apple", "Banana"));
System.out.println(set.contains("Apple")); // true
System.out.println(set.contains("Orange")); // false
```

#### 📊 基础属性：`int size()` / `boolean isEmpty()` / `void clear()`

```java
Set<String> set = new HashSet<>(Set.of("X", "Y"));
System.out.println(set.size()); // 2
System.out.println(set.isEmpty()); // false
set.clear();
System.out.println(set.isEmpty()); // true
```

---

### 🧩 批量操作与集合运算 API

这些方法使 `Set` 能够方便地执行数学上的集合运算（交集、差集、并集）。

#### ➕ 批量添加（并集）：`boolean addAll(Collection<? extends E> c)`

将参数集合中的所有**不重复**元素添加到当前集合。效果等同于求**并集**。

```java
Set<Integer> setA = new HashSet<>(Set.of(1, 2, 3));
Set<Integer> setB = new HashSet<>(Set.of(3, 4, 5));
setA.addAll(setB); // setA 变为 [1, 2, 3, 4, 5]
```

#### ➖ 批量删除（差集）：`boolean removeAll(Collection<?> c)`

从当前集合中移除所有也存在于参数集合中的元素。效果等同于求**差集** (`A - B`)。

```java
Set<Integer> setA = new HashSet<>(Set.of(1, 2, 3, 4));
Set<Integer> setB = new HashSet<>(Set.of(3, 4, 5));
setA.removeAll(setB); // setA 变为 [1, 2]
```

#### 🎯 保留交集：`boolean retainAll(Collection<?> c)`

仅保留当前集合中也存在于参数集合中的元素。效果等同于求**交集**。

```java
Set<Integer> setA = new HashSet<>(Set.of(1, 2, 3, 4));
Set<Integer> setB = new HashSet<>(Set.of(3, 4, 5));
setA.retainAll(setB); // setA 变为 [3, 4]
```

#### ✅ 包含判断：`boolean containsAll(Collection<?> c)`

判断当前集合是否包含参数集合中的**所有**元素。

```java
Set<String> set = new HashSet<>(Set.of("A", "B", "C"));
System.out.println(set.containsAll(Set.of("A", "B"))); // true
System.out.println(set.containsAll(Set.of("A", "D"))); // false
```

---

### 🔄 遍历 Set 的三种方式

由于没有索引，遍历方式有别于 `List`。

```java
Set<String> fruits = new LinkedHashSet<>(Set.of("Apple", "Banana", "Cherry")); // 使用 LinkedHashSet 保证演示顺序

// 方式一：增强 for 循环 (最简洁)
for (String fruit : fruits) {
    System.out.println(fruit);
}

// 方式二：Iterator 迭代器 (可在遍历中安全删除元素)
Iterator<String> it = fruits.iterator();
while (it.hasNext()) {
    String fruit = it.next();
    if (fruit.equals("Banana")) {
        it.remove(); // 安全删除
    }
}

// 方式三：forEach + Lambda (JDK 8+)
fruits.forEach(System.out::println);
```

---

### ⚠️ 关键注意事项与特有概念

#### 🔑 依赖 `hashCode` 与 `equals`（对 `HashSet` / `LinkedHashSet`）

`HashSet` 判断元素是否重复，先比较 `hashCode`，再比较 `equals`。如果自定义对象没有正确重写这两个方法，去重机制会失效。

```java
class Person {
    String name;
    Person(String name) { this.name = name; }
    // 故意不重写 equals 和 hashCode
}
Set<Person> set = new HashSet<>();
set.add(new Person("Alice"));
set.add(new Person("Alice")); // 会被当作两个不同的对象，因为地址不同
System.out.println(set.size()); // 输出 2 (去重失败)
```

正确做法是同时重写 `equals()` 和 `hashCode()`，确保“相等”的对象具有相同的哈希码。

#### 🌳 `TreeSet` 的排序依赖 `Comparable` 或 `Comparator`

`TreeSet` 要求元素可比较。如果元素是自定义类型，必须实现 `Comparable` 接口，或在创建 `TreeSet` 时传入 `Comparator`，否则会抛出 `ClassCastException`。

```java
// 方式一：元素实现 Comparable
class Student implements Comparable<Student> {
    int age;
    public int compareTo(Student other) { return this.age - other.age; }
}

// 方式二：传入 Comparator (更灵活)
Set<String> byLength = new TreeSet<>(Comparator.comparingInt(String::length));
byLength.add("Java");
byLength.add("C");
byLength.add("Python");
System.out.println(byLength); // [C, Java, Python] (按长度排序)
```

#### 🚫 `null` 元素的限制

`HashSet` 和 `LinkedHashSet` 允许存入一个 `null` 元素。**`TreeSet` 不允许存入 `null`**，否则在添加时会抛出 `NullPointerException`，因为它需要进行比较排序。

#### 🔒 不可变 Set（JDK 9+）

`Set.of(...)` 可以快速创建**结构不可变**的 Set。任何试图修改的操作（`add`、`remove`）都会抛出 `UnsupportedOperationException`。它拒绝 `null` 元素，并且创建时就会对重复元素抛出 `IllegalArgumentException`。

```java
Set<String> immutable = Set.of("A", "B", "C");
// immutable.add("D"); // 抛出 UnsupportedOperationException
```
