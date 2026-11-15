# Java 中 Iterator 接口详解

`Iterator` 是 Java 集合框架中一个看似简单、但设计精巧的接口。它的核心价值在于：**将“遍历”这一行为从集合的具体数据结构中抽象出来**，使得客户端可以用统一的方式访问 `ArrayList`、`LinkedList`、`HashSet` 等内部结构完全不同的容器。

---

## 特点：抽象、单向、一次性、可安全删除

`Iterator` 接口定义了三个核心方法（Java 8 后增加了默认方法 `forEachRemaining`）：

```java
public interface Iterator<E> {
    boolean hasNext();   // 是否还有下一个元素
    E next();            // 返回下一个元素，并移动游标
    void remove();       // 删除上一次 next() 返回的元素（可选操作）
}
```

它的关键特点可以概括为以下几点：

**① 抽象了遍历过程，与数据结构解耦**
无论底层是数组、链表还是哈希桶，客户端只通过 `hasNext()` / `next()` 交互。集合负责“如何存”，迭代器负责“如何取”。

**② 单向遍历**
基础的 `Iterator` 只能从前往后单向移动，不能回退。如果需要双向遍历，`List` 提供了增强的 `ListIterator` 子接口。

**③ 一次性使用**
一个迭代器实例对应一次遍历过程。遍历结束后（`hasNext()` 为 `false`），不能“重置”后再次使用，必须重新调用 `collection.iterator()` 获取新的迭代器。

**④ `remove()` 是唯一安全的“边遍历边删”方式**
在迭代过程中直接调用集合自身的 `remove()` 会破坏迭代器的状态，触发 `ConcurrentModificationException`（fail-fast）。而迭代器自身的 `remove()` 在删除元素的同时会**同步更新迭代器的预期修改计数**，从而保持一致性。

**⑤ 定义了“失败”的时机**
标准的 `java.util` 集合迭代器是 **fail-fast** 的：一旦检测到迭代期间集合发生了结构性修改（非迭代器自身的 `remove()`），会尽快抛出 `ConcurrentModificationException`，而不是在不确定的未来产生不可预期的行为。

---

## 基本用法

### 显式使用 `Iterator`

```java
List<String> list = new ArrayList<>(List.of("A", "B", "C"));
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String s = it.next();
    System.out.println(s);
}
```

`iterator()` 返回的并不是 `Iterator` 接口本身（接口不能实例化），而是集合内部一个**实现了 `Iterator` 的私有内部类实例**。对于 `ArrayList`，这个内部类叫 `Itr`；对于 `HashMap`，则是 `HashIterator` 及其子类。

### 增强 for 循环的本质

日常更常用的是增强 `for` 循环：

```java
for (String s : list) {
    System.out.println(s);
}
```

这本质上是编译器提供的语法糖。反编译后，它等价于：

```java
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String s = it.next();
    System.out.println(s);
}
```

因此，**增强 for 循环中不能直接调用集合的 `remove()`**，因为底层迭代器无法感知这种外部修改，会触发 fail-fast。

### `remove()` 的安全用法与限制

迭代器的 `remove()` 有严格的调用条件：

- 必须在**最近一次 `next()` 之后**调用；
- 在最近一次 `next()` 之后，`remove()` **只能调用一次**。

违反任一条件都会抛出 `IllegalStateException`。

```java
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String s = it.next();
    if (s.equals("B")) {
        it.remove();   // 安全：删除的是刚 next() 返回的 "B"
    }
}
```

`remove()` 之后，迭代器的内部状态被重置（`lastRet = -1`），必须再次调用 `next()` 才能进行下一次删除。

### `forEachRemaining`（Java 8+）

如果不需要在遍历中做删除操作，`forEachRemaining` 可以更简洁地处理剩余元素：

```java
Iterator<String> it = list.iterator();
it.next(); // 跳过第一个
it.forEachRemaining(System.out::println);
```

---

## 执行原理：内部类 + 游标 + 修改计数

### 以 `ArrayList.Itr` 为例

`ArrayList` 的迭代器是一个**非静态内部类 `Itr`**，它持有三个关键状态：

```java
private class Itr implements Iterator<E> {
    int cursor;       // 下一个要返回的元素索引
    int lastRet = -1; // 上一次返回的元素索引，-1 表示“没有”
    int expectedModCount = modCount; // 创建迭代器时“快照”的修改次数
}
```

`hasNext()` 只需要判断游标是否已经到达 `size`：

```java
public boolean hasNext() {
    return cursor != size;
}
```

`next()` 的核心逻辑是：**先检查并发修改，再返回 `cursor` 位置元素，然后移动游标并记录 `lastRet`**：

```java
public E next() {
    checkForComodification();
    int i = cursor;
    if (i >= size) throw new NoSuchElementException();
    cursor = i + 1;
    return (E) elementData[lastRet = i];
}
```

`remove()` 则在删除底层数组元素后，**修正游标并重新同步 `expectedModCount`**：

```java
public void remove() {
    if (lastRet < 0) throw new IllegalStateException();
    checkForComodification();
    ArrayList.this.remove(lastRet);
    cursor = lastRet;
    lastRet = -1;
    expectedModCount = modCount; // 关键：与集合的 modCount 重新对齐
}
```

如果游标之后还有元素，删除 `lastRet` 位置的元素会使后续元素整体左移，因此 `cursor` 需要回退到 `lastRet` 的位置，才能保证下一次 `next()` 返回的是原本位于 `lastRet + 1` 的元素。

---

## fail-fast 的核心机制：`modCount` 与 `expectedModCount`

`fail-fast` 的实现依赖于集合内部维护的一个**修改计数器 `modCount`**，以及迭代器内部的**快照 `expectedModCount`**。

- **`modCount`**：集合的结构性修改（`add`、`remove`、`clear` 等改变元素数量的操作）会使它递增。
- **`expectedModCount`**：迭代器在**创建时**将当前 `modCount` 复制过来，作为“我认为集合应该被修改了多少次”的预期值。
- **检查点**：每次 `next()`、`remove()` 之前，迭代器都会调用 `checkForComodification()`，比较两者是否相等。如果不等，立即抛出 `ConcurrentModificationException`。

```java
final void checkForComodification() {
    if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
}
```

这解释了为什么**直接调用集合的 `remove()` 会崩溃，而迭代器的 `remove()` 是安全的**：

- 集合的 `remove()` 只递增 `modCount`，`expectedModCount` 保持旧值 → 下一次 `next()` 检查时两者不等 → 异常；
- 迭代器的 `remove()` 在调用底层删除之后，**额外执行 `expectedModCount = modCount`**，将预期值同步到最新状态 → 检查通过。

### `HashMap` 的迭代器也遵循同一模式

`HashMap` 的 `HashIterator` 同样在构造时快照 `modCount`，在 `nextNode()` 中检查：

```java
abstract class HashIterator {
    Node<K,V> next;
    Node<K,V> current;
    int expectedModCount;
    int index;

    HashIterator() {
        expectedModCount = modCount; // 快照
        // 定位到第一个非空桶...
    }

    final Node<K,V> nextNode() {
        if (modCount != expectedModCount)
            throw new ConcurrentModificationException(); // 检查
        // ...
    }
}
```

`KeyIterator`、`ValueIterator`、`EntryIterator` 都继承自 `HashIterator`，复用同一套 fail-fast 逻辑。

> **注意**：fail-fast **不是一种保证**。它的检查没有同步，在极端并发时序下可能“漏检”；它的设计目的是**尽早暴露 bug**，而不是作为程序正确性的依赖机制。多线程场景应使用 `ConcurrentHashMap` 等并发容器。

---

## 补充：`Iterable` 与 `Iterator` 的分工

二者容易混淆，但职责不同：

| | `Iterable` | `Iterator` |
|---|---|---|
| **含义** | “可迭代的**能力**” | “迭代的**方式/过程**” |
| **核心方法** | `Iterator<T> iterator()` | `hasNext()`、`next()`、`remove()` |
| **角色** | 集合声明“我可以被遍历” | 真正执行遍历的游标对象 |
| **与增强 for 的关系** | 实现了 `Iterable` 才能用于增强 for | 增强 for 底层调用的是它 |

几乎所有 `Collection` 都实现了 `Iterable`（`Map` 本身不实现，但 `Map` 的 `keySet()`、`entrySet()`、`values()` 返回的集合实现了 `Iterable`）。

---

## 实践要点

1. **边遍历边删，只用迭代器的 `remove()`**，不要调用集合自身的 `remove()`。
2. **不要在增强 for 循环中修改集合结构**（除非只调用迭代器 `remove()`，但增强 for 隐藏了迭代器引用，实际操作中不便于安全删除）。
3. **`remove()` 必须紧跟 `next()`，且一次 `next()` 后只能删一次**，否则 `IllegalStateException`。
4. **`Iterator` 是一次性的**，不要复用同一个迭代器实例。
5. **fail-fast 是 bug 检测机制，不是并发控制手段**；多线程请使用并发集合。
