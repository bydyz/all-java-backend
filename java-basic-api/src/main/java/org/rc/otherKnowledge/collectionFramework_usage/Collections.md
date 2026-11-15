`java.util.Collections` 是一个专为操作 `Collection` 接口（如 `List`、`Set`）而设计的**静态工具类**。它与 `Collection` 接口虽然名字相似，但角色完全不同：`Collection` 是集合框架的根接口，而 `Collections` 则是操作这些集合的“辅助工具”。

`Collections` 类的核心特点在于，它提供的方法大多是**静态方法**，且许多方法返回的是基于原集合的**视图**或**包装器**，而不是创建全新的集合。这意味着对包装后集合的某些操作会直接反映在原始集合上，并且这些包装器通常以较小的开销提供了额外的功能（如只读、线程安全等）。

下面将 `Collections` 的常用 API 按功能分类进行详解。

---

### 📊 排序与顺序操作

这类方法直接修改传入的 `List` 本身，因此要求 `List` 必须是**可变**的（例如 `ArrayList`），`Arrays.asList()` 返回的固定大小列表通常无法使用这些方法。

#### `sort(List<T> list)` / `sort(List<T> list, Comparator<? super T> c)`

对 `List` 进行自然排序或按自定义 `Comparator` 排序。现代 Java 中，`list.sort(c)` 是更推荐的方式，但 `Collections.sort` 在需要兼容旧代码时仍会见到。

```java
List<String> list = new ArrayList<>(List.of("banana", "apple", "cherry"));
Collections.sort(list); // 自然升序
// list: [apple, banana, cherry]

Collections.sort(list, Comparator.reverseOrder()); // 自定义降序
// list: [cherry, banana, apple]
```

#### `reverse(List<?> list)`

反转列表中元素的顺序。

```java
List<Integer> nums = new ArrayList<>(List.of(1, 2, 3, 4, 5));
Collections.reverse(nums);
// nums: [5, 4, 3, 2, 1]
```

#### `shuffle(List<?> list)` / `shuffle(List<?> list, Random rnd)`

使用默认或指定的随机源，随机打乱列表中元素的顺序，常用于模拟洗牌或生成随机排列。

```java
List<Integer> deck = new ArrayList<>(List.of(1, 2, 3, 4, 5));
Collections.shuffle(deck);
// deck 的顺序随机，例如: [3, 1, 5, 2, 4]
```

#### `swap(List<?> list, int i, int j)`

交换列表中指定两个索引位置的元素。

```java
List<String> letters = new ArrayList<>(List.of("A", "B", "C", "D"));
Collections.swap(letters, 1, 3);
// letters: [A, D, C, B]
```

#### `rotate(List<?> list, int distance)`

将列表中的所有元素旋转指定的距离。正数表示向右旋转（元素向后移动），负数表示向左旋转。

```java
List<String> cycle = new ArrayList<>(List.of("a", "b", "c", "d", "e"));
Collections.rotate(cycle, 2);
// cycle: [d, e, a, b, c] (每个元素向右移动2位)
```

---

### 🔍 查找与统计

#### `binarySearch(List<? extends Comparable<? super T>> list, T key)` / `binarySearch(List<? extends T> list, T key, Comparator<? super T> c)`

使用二分查找法在列表中搜索指定元素，返回其索引。**前提是列表必须已经按相同顺序排序**，否则结果不确定。

```java
List<Integer> sortedNums = new ArrayList<>(List.of(10, 20, 30, 40, 50));
int index = Collections.binarySearch(sortedNums, 30); // 返回 2
// 若未找到，返回负数（通常是 -(插入点) - 1）
```

#### `max(Collection<? extends T> coll)` / `min(Collection<? extends T> coll)`

根据元素的自然顺序或自定义 `Comparator`，返回集合中的最大或最小元素。

```java
List<Integer> values = List.of(3, 1, 4, 1, 5, 9);
int maxVal = Collections.max(values); // 9
int minVal = Collections.min(values); // 1
```

#### `frequency(Collection<?> c, Object o)`

统计指定对象在集合中出现的次数。

```java
List<String> words = List.of("apple", "banana", "apple", "cherry");
int count = Collections.frequency(words, "apple"); // 2
```

#### `disjoint(Collection<?> c1, Collection<?> c2)`

判断两个集合是否**没有交集**。如果两个集合不包含任何共同元素，则返回 `true`。

```java
List<Integer> listA = List.of(1, 2, 3);
List<Integer> listB = List.of(4, 5, 6);
boolean noOverlap = Collections.disjoint(listA, listB); // true
```

---

### 🛡️ 不可变视图与轻量集合

这类方法返回的集合是**不可修改的**，任何尝试修改的操作（如 `add`、`remove`、`clear`）都会抛出 `UnsupportedOperationException`。

#### `unmodifiableXxx()` (如 `unmodifiableList`)

创建一个现有集合的**只读视图**。对原始集合的修改会透过这个视图可见，但通过视图本身无法修改。

```java
List<String> mutable = new ArrayList<>(List.of("a", "b"));
List<String> readOnlyView = Collections.unmodifiableList(mutable);
// readOnlyView.add("c"); // 抛出 UnsupportedOperationException
mutable.add("c"); // 原始集合可以修改
System.out.println(readOnlyView); // [a, b, c] — 视图反映了变化
```

#### `emptyList()` / `emptySet()` / `emptyMap()`

返回共享的、不可变的空集合实例。这比 `new ArrayList<>()` 更高效，因为无需分配新对象。

```java
List<String> noItems = Collections.emptyList();
Set<Integer> noNumbers = Collections.emptySet();
```

#### `singletonList(T o)` / `singleton(T o)` / `singletonMap(K key, V value)`

返回仅包含一个指定元素的不可变集合。这在需要传递单个元素的集合时非常有用，同样比手动创建集合更节省内存。

```java
List<String> single = Collections.singletonList("only");
Set<Integer> singleSet = Collections.singleton(42);
```

#### `nCopies(int n, T o)`

返回一个包含 `n` 个对同一对象 `o` 的引用的**不可变列表**。由于元素相同，它在内部实现上非常高效，并非真的存储了 `n` 个独立元素。

```java
List<String> repeats = Collections.nCopies(5, "echo");
// repeats: [echo, echo, echo, echo, echo]
```

---

### 🔒 线程安全与类型安全包装

#### `synchronizedXxx()` (如 `synchronizedList`)

返回一个线程安全的集合包装器。它的每个方法都使用 `synchronized` 进行同步，类似于 `Vector` 的机制。

**关键注意点**：`synchronizedList` 只保证**单个方法调用**的原子性。对于**复合操作**（如“检查再添加”）和**迭代**，仍然需要手动对返回的列表对象进行同步。

```java
List<String> syncList = Collections.synchronizedList(new ArrayList<>());
// 单个操作是线程安全的
syncList.add("element");

// 迭代时必须手动同步
synchronized (syncList) {
    for (String s : syncList) {
        // ...
    }
}
```

#### `checkedXxx()` (如 `checkedList`)

返回一个**动态类型安全**的视图。它确保任何添加到列表中的元素都是指定的类型，否则立即抛出 `ClassCastException`。这主要用于在泛型被擦除的情况下，于运行时捕获类型错误。

```java
List rawList = new ArrayList();
List<String> checkedList = Collections.checkedList(rawList, String.class);
checkedList.add("hello"); // 正常
// checkedList.add(123); // 立即抛出 ClassCastException
```

---

### 📦 批量添加

#### `addAll(Collection<? super T> c, T... elements)`

将指定的一系列元素（可变参数或数组）一次性添加到集合中，是快速初始化集合的便捷方式。

```java
List<String> fruits = new ArrayList<>();
Collections.addAll(fruits, "Apple", "Banana", "Cherry");
// fruits: [Apple, Banana, Cherry]
```

### 💎 总结与选用建议

`Collections` 工具类的设计主要围绕**包装**和**视图**展开，这带来了灵活性，但也需要理解其语义：

- **修改语义**：`sort`、`shuffle`、`reverse` 等直接修改传入的可变 `List`。
- **视图语义**：`unmodifiableXxx`、`synchronizedXxx`、`checkedXxx` 返回的是包装器，对原始集合的修改通常会影响包装器（只读和类型安全包装器除外，它们主要增加约束）。
- **不可变轻量集合**：`emptyXxx`、`singletonXxx`、`nCopies` 返回的是不可变的、内存高效的集合实例。

在实际开发中，对于**创建不可变集合**，Java 9+ 的 `List.of()`、`Set.of()` 等通常是更简洁的选择。对于**线程安全**，`java.util.concurrent` 包中的 `CopyOnWriteArrayList`、`ConcurrentHashMap` 等并发集合在性能和可扩展性上通常优于 `synchronizedList`。`Collections` 在需要**适配旧代码**、**创建轻量视图**或进行**算法性操作**（如排序、查找、打乱）时，依然是不可或缺的工具。
