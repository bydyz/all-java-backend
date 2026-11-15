## 一、List 的核心特点

`List` 是 `java.util` 包中 `Collection` 接口最重要的子接口之一，本质上是一个**有序的线性表**。它有三个不可动摇的核心约定：

**有序（Ordered）** ：元素严格按照添加顺序保存，索引从 0 开始，`get(0)` 永远返回第一个放入的元素。

**可重复（Duplicates Allowed）** ：同一个元素可以出现多次，`List` 不做任何去重限制。

**索引访问（Index-Based Access）** ：每个元素都有明确的整数索引，支持按位置直接定位、插入、替换和删除。

```java
List<String> list = new ArrayList<>();
list.add("A");
list.add("B");
list.add("A");          // 允许重复
System.out.println(list);       // [A, B, A]
System.out.println(list.get(0)); // A  → 索引访问
```

`List` 是**接口**，不能直接 `new`，必须实例化其实现类。日常开发中最常用的是 `ArrayList`，其次是 `LinkedList`，`Vector` 已较少使用。


## 二、实现类的选择：ArrayList vs LinkedList

理解 `List` 的 API 之前，先明确用哪个实现类，因为**同一个 API 在不同实现上的性能可能截然不同**。

| 维度 | ArrayList | LinkedList |
|---|---|---|
| 底层结构 | 动态数组 | 双向链表 |
| 随机访问 `get(i)` / `set(i)` | **O(1)** | O(n) |
| 头部插入/删除 | O(n)（需要移动元素） | **O(1)** |
| 尾部插入/删除 | 均摊 O(1) | O(1) |
| 中间插入/删除 | O(n)（需要移动后续元素） | O(n)（需要先遍历定位，再改指针） |
| 内存占用 | 较小（仅数组） | 较大（每个节点额外存前后引用） |
| 默认选择 | **绝大多数场景的首选** | 频繁在头部操作、或用作队列/栈时 |

`ArrayList` 的“中间插入/删除 O(n)”来自数组元素的整体搬迁，而 `LinkedList` 虽然改指针是 O(1)，但**定位到那个位置仍然需要 O(n) 遍历**，因此实际收益通常没有想象中大。**如果没有明确的理由，默认选 `ArrayList`**。

```java
// ArrayList：读多写少、按索引访问
List<Integer> arrayList = new ArrayList<>();
arrayList.add(100);
int val = arrayList.get(0);     // O(1)

// LinkedList：频繁在头部插入/删除
List<Integer> linkedList = new LinkedList<>();
linkedList.add(0, -1);          // O(1)
linkedList.remove(0);           // O(1)
```


## 三、List 接口 API 分类详解与代码

以下 API 均以 `List` 接口声明、`ArrayList` 为实现进行演示，便于直接运行理解。

### 3.1 添加元素

**`add(E e)`** ：追加到末尾，返回 `true`。**`add(int index, E element)`** ：在指定索引插入，原位置及之后的元素全部后移一位。

```java
List<String> fruits = new ArrayList<>();
fruits.add("苹果");
fruits.add("香蕉");
fruits.add(1, "葡萄");     // 在下标 1 处插入
System.out.println(fruits); // [苹果, 葡萄, 香蕉]
```

**`addAll(Collection<? extends E> c)`** 和 **`addAll(int index, Collection<? extends E> c)`** 用于批量添加。

```java
List<String> more = Arrays.asList("橙子", "西瓜");
fruits.addAll(more);
System.out.println(fruits); // [苹果, 葡萄, 香蕉, 橙子, 西瓜]
```

### 3.2 获取与修改

**`get(int index)`** 返回指定位置的元素；**`set(int index, E element)`** 替换指定位置的元素，**返回被替换掉的旧值**。

```java
List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));
String first = list.get(0);           // A
String old = list.set(1, "X");        // 返回 "B"
System.out.println(list);             // [A, X, C]
System.out.println(old);              // B（旧值）
```

**`size()`** 返回元素个数，**`isEmpty()`** 判断是否为空。

```java
System.out.println(list.size());     // 3
System.out.println(list.isEmpty());  // false
```

### 3.3 删除元素（最容易出错的区域）

`List` 有两个 `remove` 重载，**行为完全不同**：

- **`remove(int index)`** ：按**索引**删除，返回被删除的元素。
- **`remove(Object o)`** ：按**值**删除**第一个匹配项**，返回 `boolean`。

**关键陷阱**：当 `List` 的泛型是 `Integer` 时，`list.remove(1)` 会被编译器匹配到 `remove(int index)`，**删除的是下标为 1 的元素，而不是值为 1 的元素**。要按值删除整数，必须显式装箱：`list.remove(Integer.valueOf(1))`。

```java
List<Integer> nums = new ArrayList<>(Arrays.asList(1, 2, 3));
nums.remove(1);                    // 删除下标 1，即元素 2
System.out.println(nums);          // [1, 3]

nums = new ArrayList<>(Arrays.asList(1, 2, 3));
nums.remove(Integer.valueOf(1));   // 按值删除元素 1
System.out.println(nums);          // [2, 3]
```

**`removeIf(Predicate)`** （Java 8+）是批量按条件删除的安全方式，适合在“删除所有满足某条件的元素”场景替代手写循环。

```java
List<String> words = new ArrayList<>(Arrays.asList("Java", "Go", "Python", "C"));
words.removeIf(s -> s.length() <= 2);
System.out.println(words); // [Java, Python]
```

**`clear()`** 清空所有元素。

### 3.4 查找与判断

**`contains(Object o)`** 判断是否存在（基于 `equals`）；**`indexOf(Object o)`** 返回第一次出现的索引，不存在返回 -1；**`lastIndexOf(Object o)`** 返回最后一次出现的索引。

```java
List<String> list = new ArrayList<>(Arrays.asList("A", "B", "A", "C"));
System.out.println(list.contains("B"));    // true
System.out.println(list.indexOf("A"));     // 0
System.out.println(list.lastIndexOf("A")); // 2
System.out.println(list.indexOf("Z"));     // -1
```

### 3.5 子列表：`subList`（视图，非副本）

**`subList(int fromIndex, int toIndex)`** 返回 `[fromIndex, toIndex)` 范围的**视图**。视图与原列表**共享底层数据**：对视图的非结构性修改（`set`）会反映到原列表，反之亦然。但**一旦原列表发生了结构性修改（`add`/`remove`/`sort` 等改变大小的操作），之前获取的 `subList` 视图就会失效**，继续操作可能抛出 `ConcurrentModificationException`。

```java
List<String> source = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
List<String> sub = source.subList(1, 3);   // [B, C]
sub.set(0, "X");                           // 非结构性修改：影响原列表
System.out.println(source);                // [A, X, C, D]

// source.add("E"); // ← 如果取消注释，后续 sub.get(0) 可能抛异常
```

**安全用法**：如果需要独立的子列表，立即复制一份：

```java
List<String> independent = new ArrayList<>(source.subList(1, 3));
```

### 3.6 排序与替换（Java 8+ 默认方法）

**`sort(Comparator)`** 对列表原地排序；**`replaceAll(UnaryOperator)`** 对每个元素应用函数并替换。

```java
List<String> names = new ArrayList<>(Arrays.asList("Bob", "alice", "Charlie"));
names.sort(String.CASE_INSENSITIVE_ORDER);
System.out.println(names); // [alice, Bob, Charlie]

names.replaceAll(String::toUpperCase);
System.out.println(names); // [ALICE, BOB, CHARLIE]
```

### 3.7 转为数组：`toArray`

**`toArray()`** 返回 `Object[]`；**`toArray(T[] a)`** 返回指定类型的数组。

```java
List<String> list = new ArrayList<>(Arrays.asList("A", "B"));
Object[] objects = list.toArray();
String[] strings = list.toArray(new String[0]); // 推荐写法
```

### 3.8 不可变/固定大小列表的工厂方法

**`Arrays.asList(...)`** 返回**固定大小**的列表：**不能 `add` 或 `remove`**，但可以 `set`，且修改会写回原数组。

**`List.of(...)`**（Java 9+）返回**完全不可变**的列表：`add`、`remove`、`set` 都会抛出 `UnsupportedOperationException`，且**不允许 `null` 元素**。

```java
List<String> fixed = Arrays.asList("A", "B", "C");
fixed.set(0, "X");       // ✅ 允许
// fixed.add("D");       // ❌ UnsupportedOperationException

List<String> immutable = List.of("A", "B", "C");
// immutable.set(0, "X"); // ❌ UnsupportedOperationException
// immutable.add("D");    // ❌ UnsupportedOperationException
```


## 四、遍历 List 的五种方式与选型

| 方式 | 适用场景 | 关键限制 |
|---|---|---|
| 普通 `for` + `get(i)` | 需要索引；`ArrayList` 上的随机访问 | `LinkedList` 上性能差（O(n) 每次 `get`） |
| 增强 `for`（for-each） | 只读遍历，代码最简洁 | **不能**在循环中 `add`/`remove`，否则抛 `ConcurrentModificationException` |
| `Iterator` | 遍历中需要**安全删除**元素 | 单向，代码较繁琐 |
| `ListIterator` | 需要**双向遍历**，或遍历中添加/替换元素 | 仅适用于 `List` |
| `forEach()` / Stream | 函数式简洁处理 | `forEach` 无法 `break`/`continue` |

**代码对比**：

```java
List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));

// 1. 普通 for：需要索引时
for (int i = 0; i < list.size(); i++) {
    System.out.println(i + ": " + list.get(i));
}

// 2. 增强 for：只读，最简洁
for (String s : list) {
    System.out.println(s);
}

// 3. Iterator：遍历中安全删除
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if ("B".equals(it.next())) {
        it.remove();          // ✅ 唯一安全的遍历中删除方式
    }
}

// 4. ListIterator：双向
ListIterator<String> lit = list.listIterator(list.size());
while (lit.hasPrevious()) {
    System.out.println(lit.previous());
}

// 5. forEach
list.forEach(System.out::println);
```

**核心易错点**：在增强 `for` 中直接调用 `list.remove(...)` 或 `list.add(...)`，会破坏迭代器的 `modCount` 一致性，抛出 `ConcurrentModificationException`。唯一在遍历中删除元素的安全方式是使用 `Iterator.remove()`；另一种可选的模式是**倒序普通 `for` 循环**，但可读性不如 `Iterator`。


## 五、总结

`List` 的用法可以归结为几条决策原则：**默认用 `ArrayList`**，除非有明确的头部高频增删需求才考虑 `LinkedList`；**按索引删除和按值删除必须分清**，`Integer` 列表尤其要显式 `Integer.valueOf()`；**`subList` 是视图不是副本**，需要独立数据时立即 `new ArrayList<>(...)`；**遍历中删除只用 `Iterator.remove()` 或 `removeIf`**；**`Arrays.asList` 是固定大小，`List.of` 是完全不可变**，二者都不可假设为可增删的普通列表。掌握这些边界，List 的 API 就能在绝大多数场景下安全、高效地使用。
