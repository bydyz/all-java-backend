## Java `Vector` 详解：特点、API 与代码实践

`Vector` 是 Java 最早提供的集合类之一（JDK 1.0），本质上是一个**方法级同步的动态数组**。在现代 Java 开发中，它已被视为**遗留类**（legacy class），但在维护旧系统或理解集合框架演进时仍值得掌握。下面从核心特点、API 分类用法、代码示例和选型建议四个层面展开。

---

### 一、核心特点

#### 1. 线程安全但锁粒度粗

`Vector` 的绝大多数公开方法（`add`、`get`、`set`、`remove`、`size` 等）都直接使用 `synchronized` 修饰，锁对象是当前 `Vector` 实例（`this`）。这意味着**每一次读写操作都要获取同一把互斥锁**，包括本不需要互斥的 `get` 操作。在并发场景下，这会造成严重的锁竞争和上下文切换，吞吐量远低于现代并发容器。

作为对比，`Collections.synchronizedList` 虽然在本质上也是互斥锁，但它使用代码块级别的同步（`synchronized (mutex)`），且不包含 `Vector` 遗留的老旧 API，因此性能略好一点点；但两者在“读读互斥”这一点上并无实质改善。

#### 2. 扩容机制：默认翻倍，可自定义增量

`Vector` 维护 `capacity`（当前数组缓冲区长度）和 `capacityIncrement`（每次扩容的增量）两个参数。扩容规则如下：

- 如果 `capacityIncrement > 0`：新容量 = 当前容量 + `capacityIncrement`
- 如果 `capacityIncrement <= 0`（默认值就是 0）：新容量 = 当前容量 × 2

这与 `ArrayList` 默认扩容 1.5 倍（`oldCapacity + (oldCapacity >> 1)`）不同。`Vector` 的翻倍策略在元素量很大时可能产生较大的内存浪费，但可以通过构造器精确控制 `capacityIncrement` 来缓解。

#### 3. Fail-fast 迭代器，但 `Enumeration` 不是

`Vector` 的 `iterator()` 和 `listIterator()` 返回的迭代器是 **fail-fast** 的：如果在迭代过程中通过迭代器自身以外的方式结构性修改了 `Vector`，迭代器会抛出 `ConcurrentModificationException`。

但需要注意一个历史遗留差异：`Vector.elements()` 返回的 `Enumeration` **不是 fail-fast 的**。这意味着在多线程环境下使用 `Enumeration` 遍历时，并发修改可能静默产生未定义的行为，而不一定抛出异常。这是 `Enumeration` 在现代代码中应被 `Iterator` 取代的重要原因之一。

#### 4. 遗留地位

从 Java 2 v1.2 开始，`Vector` 被改造为实现 `List` 接口，从而融入 Collections Framework。但官方文档明确说明：“**如果不需要线程安全的实现，推荐使用 `ArrayList` 代替 `Vector`**”。在需要线程安全的现代代码中，`CopyOnWriteArrayList`（读多写少）或 `Collections.synchronizedList`（低并发简单场景）通常是更合适的选择。


### 二、API 分类详解与代码示例

`Vector` 的 API 可以分为两套：**实现 `List` 接口的现代方法**（与 `ArrayList` 基本一致）和**遗留的 `Vector` 专有方法**。现代代码应优先使用前者。

#### 1. 构造

`Vector` 提供四个构造器：

| 构造器 | 说明 |
|---|---|
| `Vector()` | 初始容量 **10**，`capacityIncrement = 0`（默认翻倍） |
| `Vector(int initialCapacity)` | 指定初始容量，`capacityIncrement = 0` |
| `Vector(int initialCapacity, int capacityIncrement)` | 完全自定义容量与增量 |
| `Vector(Collection<? extends E> c)` | 从已有集合构造 |

```java
Vector<String> v1 = new Vector<>();                    // 容量10，增量为0 → 翻倍
Vector<String> v2 = new Vector<>(20);                  // 容量20
Vector<String> v3 = new Vector<>(20, 5);               // 满时每次+5
Vector<String> v4 = new Vector<>(List.of("A", "B"));   // 从集合构造
```

#### 2. 添加元素

核心方法与 `List` 一致：`add(E)` 末尾追加，`add(int, E)` 指定位置插入。`Vector` 还保留了历史方法 `addElement(E)`，行为等同于 `add(E)`，但返回 `void` 而非 `boolean`。

```java
Vector<String> vector = new Vector<>();
vector.add("Java");
vector.add("Vector");
vector.add(1, "is");          // 在索引1插入，后续元素右移
vector.addElement("!");       // 遗留方法，等价于 add("!")

System.out.println(vector);   // [Java, is, Vector, !]
```

#### 3. 访问与修改

`get(int)` 按索引读取；`set(int, E)` 替换指定位置元素并返回旧值。`Vector` 专有的 `elementAt(int)` 与 `get` 等价，`firstElement()` / `lastElement()` 分别返回首尾元素。

```java
String first = vector.get(0);              // "Java"
String alsoFirst = vector.firstElement();  // 等价，遗留方法
String old = vector.set(0, "Kotlin");      // 返回旧值 "Java"
System.out.println(vector);                // [Kotlin, is, Vector, !]
```

#### 4. 删除元素

`remove(int)` 按索引删除，`remove(Object)` 删除首次出现的指定元素。`Vector` 遗留方法 `removeElement(Object)` 与 `remove(Object)` 等价，`removeElementAt(int)` 与 `remove(int)` 等价，`removeAllElements()` 清空全部元素。

```java
vector.remove("is");          // 删除元素 "is"
vector.remove(0);             // 删除索引 0 处的元素
// vector.removeAllElements(); // 清空（遗留方法，等价于 clear()）
```

#### 5. 容量与大小

这是 `Vector` 区别于 `ArrayList` 的重要 API 组：

- `size()`：元素个数
- `capacity()`：当前数组缓冲区容量（**≥ size**）
- `ensureCapacity(int minCapacity)`：预先扩容，减少多次扩容的开销
- `trimToSize()`：将容量收缩到与 `size` 一致

```java
Vector<Integer> nums = new Vector<>(4, 2);  // 容量4，增量2
nums.add(1); nums.add(2); nums.add(3); nums.add(4);
System.out.println(nums.capacity());         // 4
nums.add(5);
System.out.println(nums.capacity());         // 6（4 + 2）
nums.trimToSize();
System.out.println(nums.capacity());         // 5（收缩到size）
```

#### 6. 遍历：`Iterator` 与 `Enumeration`

**推荐使用 `Iterator`**，它与现代集合框架一致，且具备 fail-fast 保护。`Enumeration` 是 `Vector` 专有的遗留遍历方式，不具备 fail-fast 特性，仅应在维护旧代码时识别，新代码不应使用。

```java
Vector<String> vector = new Vector<>(List.of("A", "B", "C"));

// ✅ 推荐：Iterator（fail-fast）
Iterator<String> it = vector.iterator();
while (it.hasNext()) {
    System.out.println(it.next());
}

// ⚠️ 遗留：Enumeration（非 fail-fast，仅限旧代码）
Enumeration<String> en = vector.elements();
while (en.hasMoreElements()) {
    System.out.println(en.nextElement());
}
```

#### 7. Fail-fast 行为演示

```java
Vector<String> vector = new Vector<>(List.of("A", "B", "C"));
Iterator<String> it = vector.iterator();
while (it.hasNext()) {
    String s = it.next();
    if (s.equals("B")) {
        vector.add("D");  // 通过 Vector 而非迭代器修改 → 触发 fail-fast
    }
}
// 抛出 java.util.ConcurrentModificationException
```

如果需要在使用迭代器的同时删除元素，必须使用 `it.remove()`；`Vector` 的 `Iterator` 支持通过迭代器自身安全删除。


### 三、选型建议：`Vector` 在现代代码中的位置

| 场景 | 推荐方案 | 理由 |
|---|---|---|
| **单线程** | `ArrayList` | 无同步开销，性能显著优于 `Vector` |
| **低并发、简单同步需求** | `Collections.synchronizedList(new ArrayList<>())` | 与 `Vector` 锁机制类似，但不包含遗留 API，锁粒度稍细 |
| **读多写少、高并发** | `CopyOnWriteArrayList` | 读操作完全无锁，写时复制；读性能远高于 `Vector`，但写开销大、内存占用高 |
| **维护遗留系统** | 保持 `Vector` | 除非有重构计划，否则不必为了一致性而强行替换，但新代码不应再引入 `Vector` |

**核心结论**：`Vector` 在功能上是 `ArrayList` 的同步版本，但其“方法级 `synchronized`”的并发模型已经被更精细的方案取代。在 **JDK 21+** 的代码库中，除非受制于遗留 API 兼容性，否则**没有理由在新代码中使用 `Vector`**。理解它的价值主要在于读懂旧代码，以及在面试中说明 Java 集合框架的演进脉络。
