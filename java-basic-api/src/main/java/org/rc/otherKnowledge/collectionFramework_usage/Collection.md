`Collection` 接口是 Java 集合框架中**单元素集合的根规范**，它本身不提供任何直接实现，而是将“一组对象”的通用操作——增、删、查、遍历——抽象为一套契约，由 `List`、`Set`、`Queue` 等子接口根据各自的数据结构特性去落实。理解 `Collection`，关键不在于背熟所有方法签名，而在于看清它如何用统一接口屏蔽底层实现的差异，以及不同子接口如何重新定义“集合”的语义。

---

## 接口定位：规则与实现的分离

`Collection<E>` 继承自 `Iterable<E>`，这意味着**凡是 Collection 的实现类，天然支持 for-each 循环**。它只规定“能做什么”，不规定“怎么做”。例如 `add(E e)` 在不同实现中行为可能不同：对 `ArrayList` 几乎总是返回 `true`，对 `HashSet` 则在元素已存在时返回 `false`。这种设计让调用方可以面向 `Collection` 编程，更换实现时只需改动实例化那一行代码：

```java
Collection<String> coll = new ArrayList<>();
// 后续 add、remove、iterator 等操作完全不变
coll = new LinkedList<>(); // 切换实现无需改动操作代码
```

`Collection` 的部分方法被标记为 **optional operation**（可选操作）。如果某个实现不支持某操作（例如不可变集合不支持 `add`），对应方法应抛出 `UnsupportedOperationException`。这是理解集合 API 行为边界的重要前提。

---

## 核心 API 分类详解

`Collection` 的方法可按用途分为**增删、查询、批量操作、转换与流**几类。下面结合代码逐一说明其特点。

### 增删元素

| 方法 | 特点与返回值含义 |
|---|---|
| `boolean add(E e)` | 添加单个元素。返回 `true` 表示集合因本次调用发生了改变；`HashSet` 中重复添加返回 `false` |
| `boolean remove(Object o)` | 删除**第一个**匹配元素（若存在）。返回是否成功删除 |
| `void clear()` | 清空集合，非可选操作，几乎所有实现都支持 |

```java
Collection<String> set = new HashSet<>();
System.out.println(set.add("A")); // true
System.out.println(set.add("A")); // false —— Set 中重复元素未被添加
System.out.println(set.remove("A")); // true
```

`add` 的返回值语义在 `List` 和 `Set` 之间差异明显：**List 允许重复，add 通常恒为 true；Set 依赖返回值判断“是否真正发生了插入”**。

### 查询与判断

| 方法 | 说明 |
|---|---|
| `int size()` | 元素数量 |
| `boolean isEmpty()` | 是否为空，等价于 `size() == 0`，但某些实现可能提供 O(1) 的优化 |
| `boolean contains(Object o)` | 是否包含指定元素；`HashSet` 基于哈希平均 O(1)，`ArrayList` 为 O(n) |

```java
Collection<String> list = new ArrayList<>(List.of("Java", "Python"));
System.out.println(list.contains("Java")); // true
System.out.println(list.isEmpty());        // false
```

### 批量操作

批量方法是 `Collection` 接口高效性的集中体现，它们允许**一次调用完成集合间的交、并、差运算**：

- `addAll(Collection<? extends E> c)`：并集式批量添加
- `removeAll(Collection<?> c)`：从当前集合中删除与 `c` 的交集
- `retainAll(Collection<?> c)`：**仅保留**与 `c` 的交集（取交集）
- `containsAll(Collection<?> c)`：当前集合是否包含 `c` 的全部元素

```java
Collection<String> a = new ArrayList<>(List.of("A", "B", "C"));
Collection<String> b = List.of("B", "C", "D");

a.retainAll(b); // 仅保留交集
System.out.println(a); // [B, C]
```

### 转换与数组

`toArray()` 将集合转为数组。**推荐使用带参数的 `toArray(T[] a)`**，它在运行时知道目标类型，避免得到无类型的 `Object[]`：

```java
Collection<String> coll = new ArrayList<>(List.of("x", "y"));
String[] arr = coll.toArray(new String[0]);
```

### 遍历的底层基石：`iterator()`

`iterator()` 返回一个 `Iterator<E>`，是**所有遍历方式的底层基础**。for-each 在编译后本质上也转换为迭代器遍历。迭代器最重要的附加能力是**在遍历中安全删除元素**：

```java
Iterator<String> it = coll.iterator();
while (it.hasNext()) {
    String s = it.next();
    if (s.startsWith("x")) {
        it.remove(); // 安全删除；若用 coll.remove(s) 会抛 ConcurrentModificationException
    }
}
```

如果在迭代器遍历过程中直接调用集合自身的 `add`/`remove` 修改结构，迭代器的 fail-fast 机制会抛出 `ConcurrentModificationException`。**需要边遍历边删除时，必须使用迭代器自身的 `remove()`**。

### Java 8 新增的默认方法

Java 8 为 `Collection` 注入了多个 `default` 方法，使接口在不破坏已有实现的前提下获得函数式能力：

| 方法 | 作用 |
|---|---|
| `removeIf(Predicate<? super E> filter)` | 删除满足谓词的所有元素，**内部通过迭代器安全删除** |
| `forEach(Consumer<? super E> action)` | 对每个元素执行动作 |
| `stream()` / `parallelStream()` | 获取顺序流 / 并行流 |
| `spliterator()` | 获取可分割迭代器，支撑并行流 |

```java
Collection<Integer> nums = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6));
nums.removeIf(n -> n % 2 == 0); // 删除所有偶数
System.out.println(nums); // [1, 3, 5]
```

`removeIf` 的价值在于**把“条件删除”从手写迭代器循环中解放出来**，同时保持 fail-fast 安全性。

---

## 子接口的语义分化

`Collection` 的三个核心子接口——`List`、`Set`、`Queue`——在通用契约之上各自增加了**方向性约束**：

| 子接口 | 核心语义 | 关键新增能力 |
|---|---|---|
| **List** | 有序、可重复，**有索引** | `get(int index)`、`set`、`add(int, E)`、`indexOf` |
| **Set** | **不包含重复元素**，不承诺索引 | 依赖 `equals`/`hashCode`（HashSet）或 `compareTo`（TreeSet）实现去重 |
| **Queue** | 按**特定顺序**处理元素（FIFO 或优先级） | `offer`/`poll`/`peek`，队头访问与移除 |

**`List` 的索引能力**是它区别于 `Collection` 的最显著特征。但不同实现的索引代价截然不同：`ArrayList` 的 `get(i)` 是 O(1)，`LinkedList` 的 `get(i)` 是 O(n)，后者在循环中按索引访问会退化为 O(n²)。`LinkedList` 的真正优势在于**两端增删 O(1)**，且同时实现了 `Deque`，可当作双端队列使用。

**`Set` 的“去重”是一个行为契约，而非结构保证**。`HashSet` 用哈希表实现平均 O(1) 的 `add`/`contains`，但**不保证迭代顺序**；`LinkedHashSet` 在哈希基础上维护插入顺序；`TreeSet` 基于红黑树，保证**排序顺序**，代价是 O(log n) 的增删查，且**不允许 null**（因为需要比较）。

```java
Set<String> hashSet = new HashSet<>(List.of("B", "A", "C"));       // 顺序不确定
Set<String> linkedSet = new LinkedHashSet<>(List.of("B", "A", "C")); // [B, A, C]
Set<String> treeSet = new TreeSet<>(List.of("B", "A", "C"));       // [A, B, C]
```

---

## Java 21 的演进：Sequenced Collections

在 Java 21 之前，**“访问第一个/最后一个元素”和“反向视图”在不同集合类型中缺乏统一方式**：`List` 用 `get(0)` / `get(size()-1)`，`Deque` 用 `getFirst` / `getLast`，`SortedSet` 用 `first` / `last`，而 `LinkedHashSet` 甚至没有直接获取最后一个元素的通用方法。

Java 21 引入的 **`SequencedCollection`** 接口统一了这套操作：

```java
interface SequencedCollection<E> extends Collection<E> {
    SequencedCollection<E> reversed();
    void addFirst(E);  void addLast(E);
    E getFirst();      E getLast();
    E removeFirst();   E removeLast();
}
```

这意味着**任何具有确定 encounter order 的集合**（`List`、`Deque`、`LinkedHashSet`、`SortedSet` 等）都可以通过同一套 `getFirst()` / `getLast()` / `reversed()` 来操作两端。`reversed()` 返回的是一个**视图**，对视图的修改会反映到原集合，反之亦然。对于新代码，处理有序集合的双端访问时，应优先考虑这套统一的默认方法，而不是为不同实现编写分支逻辑。

---

## 选型原则

选型应基于**读写模式与语义需求**，而非类名记忆：

- **需要索引、读多写少** → `ArrayList`；**频繁在两端增删、很少按索引访问** → `LinkedList`（但不要把它当万能列表用）
- **仅去重、无序** → `HashSet`；**去重且保持插入顺序** → `LinkedHashSet`；**去重且排序** → `TreeSet`
- **队列/栈语义** → `ArrayDeque`（通常优于 `LinkedList` 作为 Deque 使用）
- **多线程环境**：上述实现均非线程安全，应使用 `ConcurrentHashMap`、`CopyOnWriteArrayList` 或外部同步，而不是在多线程下直接共享普通集合

`Collection` 接口的价值，在于它把“一组元素”的操作抽象为可替换的契约。掌握它的 API，重点不是记住每个方法签名，而是理解**每个方法在不同实现下的行为差异与代价**——返回值语义、复杂度、顺序保证、删除安全性——这些才是正确使用集合框架的关键。
