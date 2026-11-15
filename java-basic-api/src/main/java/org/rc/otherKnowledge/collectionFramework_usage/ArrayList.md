# Java ArrayList 的特点和用法详解

`ArrayList` 是 Java 集合框架中最核心也最常用的类之一。它的本质是一个**动态数组**，在普通数组的基础上，实现了自动扩容，并提供了丰富的增删改查方法。下面从它的核心特点开始，逐一梳理各个 API 的用法与代码示例。

---

## 🧬 核心特点：基于数组的动态列表

`ArrayList` 的底层是一个 `Object[]` 数组（名为 `elementData`），同时用一个 `size` 变量记录实际存储的元素个数。这决定了它的几个关键特性：

- **随机访问极快**：因为内存连续，通过 `get(index)` 访问元素的时间复杂度是 **O(1)**，这是它最大的优势。
- **容量自动增长**：与普通数组不同，你不需要预先指定大小，当元素数量超过当前容量时，`ArrayList` 会自动扩容。
- **中间插入/删除较慢**：在列表头部或中间插入/删除元素，需要移动后续所有元素，时间复杂度为 **O(n)**；在末尾追加（`add(E e)`）通常是 **O(1)** 的。
- **允许重复和 `null`**：可以存储重复元素，也允许存储 `null`。
- **非线程安全**：`ArrayList` 没有做同步处理。如果需要在多线程环境中使用，应考虑 `CopyOnWriteArrayList` 或使用 `Collections.synchronizedList()` 包装。
- **只能存储引用类型**：泛型 `E` 必须是对象类型。存储基本类型（如 `int`）需要使用其包装类（如 `Integer`）。

---

## 🏗️ 构造方法：选择合适的起点

`ArrayList` 有三种构造方式，选择哪种直接影响后续的性能表现。

### 1. 无参构造（默认）

```java
ArrayList<String> list = new ArrayList<>();
```

创建一个空列表。内部的数组在**第一次添加元素时**才会被初始化为默认容量 **10**。

### 2. 指定初始容量（推荐用于已知数据量的场景）

```java
ArrayList<String> list = new ArrayList<>(1000);
```

直接分配一个容量为 `1000` 的数组。**这是最重要的性能优化手段之一**。如果你能预估大概要存多少元素，指定一个略大的初始容量，可以完全避免后续因扩容带来的数组复制开销。

### 3. 从其他集合构造

```java
List<String> source = List.of("A", "B", "C");
ArrayList<String> list = new ArrayList<>(source);
```

将另一个 `Collection` 中的元素复制到新的 `ArrayList` 中。

---

## ⚙️ 常用 API 详解与代码示例

### ➕ 添加元素

**`add(E e)`**：将元素追加到列表**末尾**，返回 `true`。

```java
ArrayList<String> fruits = new ArrayList<>();
fruits.add("Apple");
fruits.add("Banana");
// fruits: [Apple, Banana]
```

**`add(int index, E element)`**：在指定索引处**插入**元素，原索引及之后的元素**整体后移一位**。

```java
fruits.add(1, "Mango"); // 在索引 1 处插入
// fruits: [Apple, Mango, Banana]
```

> **性能提示**：`add(E e)` 追加到末尾通常是高效的；而 `add(int index, E e)` 在中间插入需要移动元素，代价与插入位置到末尾的距离成正比。

**`addAll(Collection<? extends E> c)`**：将另一个集合的所有元素追加到末尾。

```java
ArrayList<String> moreFruits = new ArrayList<>(List.of("Orange", "Grape"));
fruits.addAll(moreFruits);
// fruits: [Apple, Mango, Banana, Orange, Grape]
```

### 📖 访问与查询

**`get(int index)`**：返回指定索引处的元素。索引必须在 `[0, size()-1]` 范围内，否则抛出 `IndexOutOfBoundsException`。

```java
String first = fruits.get(0); // "Apple"
```

**`size()`**：返回列表中元素的个数。

```java
int count = fruits.size(); // 5
```

**`contains(Object o)`**：判断列表中是否包含指定元素（依据 `equals` 方法）。

```java
boolean hasApple = fruits.contains("Apple"); // true
```

**`indexOf(Object o)` / `lastIndexOf(Object o)`**：返回元素第一次/最后一次出现的索引，不存在则返回 `-1`。

```java
int firstIndex = fruits.indexOf("Banana"); // 2
```

### ✏️ 修改元素

**`set(int index, E element)`**：将指定索引处的元素**替换**为新元素，并返回**被替换的旧元素**。

```java
String old = fruits.set(0, "Green Apple");
// old: "Apple"
// fruits: [Green Apple, Mango, Banana, Orange, Grape]
```

### ❌ 删除元素

**`remove(int index)`**：删除指定索引处的元素，并返回**被删除的元素**。后续元素整体**前移一位**。

```java
String removed = fruits.remove(1); // 删除 "Mango"
// removed: "Mango"
// fruits: [Green Apple, Banana, Orange, Grape]
```

**`remove(Object o)`**：删除**第一个**匹配（`equals`）的指定元素，返回 `boolean` 表示是否删除成功。

```java
boolean isRemoved = fruits.remove("Orange");
// isRemoved: true
// fruits: [Green Apple, Banana, Grape]
```

> ⚠️ **易错点**：对于 `List<Integer>`，`remove(1)` 调用的是 `remove(int index)`，删除的是索引 1 处的元素；如果要删除**值**为 1 的元素，必须写成 `remove(Integer.valueOf(1))`。

**`clear()`**：清空列表中的所有元素，`size` 变为 0。

```java
fruits.clear();
// fruits 变为空列表
```

### 🔄 批量操作与视图

**`removeAll(Collection<?> c)`**：从当前列表中删除所有也存在于 `c` 中的元素（差集操作）。

```java
ArrayList<Integer> nums = new ArrayList<>(List.of(1, 2, 3, 4, 5));
nums.removeAll(List.of(2, 4)); // 删除 2 和 4
// nums: [1, 3, 5]
```

**`retainAll(Collection<?> c)`**：仅保留也存在于 `c` 中的元素（交集操作）。

```java
ArrayList<Integer> a = new ArrayList<>(List.of(1, 2, 3, 4));
a.retainAll(List.of(3, 4, 5)); // 仅保留 3 和 4
// a: [3, 4]
```

**`subList(int fromIndex, int toIndex)`**：返回 `[fromIndex, toIndex)` 范围的**视图**（左闭右开）。**这不是拷贝**，对子列表的修改会反映到原列表上，反之亦然。

```java
ArrayList<String> original = new ArrayList<>(List.of("A", "B", "C", "D"));
List<String> sub = original.subList(1, 3); // [B, C]
sub.set(0, "B2");
// original 也变成了 [A, B2, C, D]
```

### 🔁 遍历方式

`ArrayList` 支持多种遍历方式，可根据场景选择。

#### 1. 普通 `for` 循环（索引遍历）

适用于需要**索引**，或可能在遍历中**修改**（通过索引 `set`）的场景。

```java
for (int i = 0; i < list.size(); i++) {
    System.out.println(list.get(i));
}
```

#### 2. 增强 `for` 循环（for-each）

最简洁，适用于只读遍历。

```java
for (String fruit : list) {
    System.out.println(fruit);
}
```

#### 3. 迭代器 `Iterator`

适用于需要在遍历过程中**安全删除**元素的场景。**不能在 for-each 中直接 `remove`**，否则会抛出 `ConcurrentModificationException`；而 `Iterator.remove()` 是安全的。

```java
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String fruit = it.next();
    if (fruit.equals("Banana")) {
        it.remove(); // 安全删除
    }
}
```

### 📊 排序

`ArrayList` 本身没有 `sort` 方法，排序通过工具类 `Collections.sort()` 或 Java 8+ 的 `List.sort()` 完成。元素需要实现 `Comparable` 接口，或提供 `Comparator`。

```java
ArrayList<String> names = new ArrayList<>(List.of("Charlie", "Alice", "Bob"));

// 自然排序（字母顺序）
Collections.sort(names);
// [Alice, Bob, Charlie]

// 自定义排序（按长度）
names.sort(Comparator.comparingInt(String::length));
// [Bob, Alice, Charlie] （长度相同时保持相对顺序）
```

---

## 🚀 性能关键：扩容机制与 `ensureCapacity`

`ArrayList` 的默认初始容量是 **10**。当添加元素导致 `size` 即将超过当前容量时，会触发扩容：**新容量 = 旧容量 + (旧容量 >> 1)**，即大约 **1.5 倍**，然后将原数组的所有元素**复制**到新数组中。

这意味着，如果不预设容量，从 10 增长到 10000，会经历**多次数组复制**，每次复制的成本都与当前元素数量成正比。对于可预估大小的批量数据，**在构造时指定初始容量是性价比最高的优化**：

```java
// 不推荐：可能触发多次扩容与复制
ArrayList<String> list = new ArrayList<>();
for (int i = 0; i < 10000; i++) list.add("data" + i);

// 推荐：预设容量，零扩容开销
ArrayList<String> list = new ArrayList<>(10000);
for (int i = 0; i < 10000; i++) list.add("data" + i);
```

此外，如果一个大列表在经历临时膨胀后长期保持较小规模，可以调用 **`trimToSize()`** 将底层数组容量收缩到恰好等于 `size()`，以释放多余的内存。

---

## 💎 总结

`ArrayList` 是一个**读多写少、以索引访问为主**场景下的首选列表实现。它的 API 设计直观，但有几个关键点值得始终留意：

- **预估容量**：已知数据量时，优先使用 `new ArrayList<>(initialCapacity)`。
- **中间插入/删除慎用**：频繁在列表中间操作时，考虑 `LinkedList` 是否更合适。
- **遍历中删除用迭代器**：避免 `ConcurrentModificationException`。
- **`List<Integer>` 的 `remove` 歧义**：区分 `remove(int index)` 与 `remove(Object o)`。
- **`subList` 是视图而非拷贝**：注意其对原列表的联动影响。
- **非线程安全**：并发环境需额外包装或换用并发集合。
