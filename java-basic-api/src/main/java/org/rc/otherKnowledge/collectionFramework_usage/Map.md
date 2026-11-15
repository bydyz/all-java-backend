`Map` 是 Java 集合框架中用于存储**键值对**的接口，核心契约是**键唯一**，每个键最多映射一个值。下面从特点、API 分类、实现类对比和关键注意事项几个层面展开，并尽量附上可运行的代码片段。

---

## 先看 Map 的核心特点

**键唯一，值可重复**。一个 Map 中不能存在两个相等的键（依据 `equals` 判断），但不同键可以映射到相同的值。键和值都可以为 `null`，不过具体是否允许取决于实现类（`HashMap` 允许，`TreeMap` 的键不允许 `null`，不可变 Map 不允许 `null`）。

**三种视图**：Map 本身不是 `Collection` 的子接口，但提供了 `keySet()`（键的 Set 视图）、`values()`（值的 Collection 视图）和 `entrySet()`（键值对的 Set 视图）来以集合的形式观察 Map。

**顺序取决于实现**。`Map` 接口本身不规定迭代顺序；`HashMap` 不保证顺序，`LinkedHashMap` 保证插入顺序（或访问顺序），`TreeMap` 按键排序。

**可变键的风险**。如果用作键的对象在作为键期间被修改，且修改影响了 `equals` 比较，Map 的行为将不再保证。这是使用 Map 时最容易忽视的契约问题。


## 核心 API 分类与用法

### 基础增删改查

| 方法 | 作用 |
|---|---|
| `put(K, V)` | 添加或覆盖键值对，返回旧值（无旧值返回 `null`） |
| `get(Object)` | 按键取值，不存在返回 `null` |
| `remove(Object)` | 按键删除，返回被删除的值 |
| `containsKey(Object)` / `containsValue(Object)` | 判断键或值是否存在 |
| `size()` / `isEmpty()` | 键值对数量 / 是否为空 |
| `clear()` | 清空所有映射 |

```java
Map<String, Integer> scores = new HashMap<>();
scores.put("Alice", 90);
scores.put("Bob", 85);
System.out.println(scores.get("Alice"));        // 90
System.out.println(scores.getOrDefault("Cara", 0)); // 0
System.out.println(scores.containsKey("Bob")); // true
scores.remove("Bob");
System.out.println(scores.size());             // 1
```
`getOrDefault` 是 Java 8 加入的默认方法，省去了先 `get` 再判空的样板代码。

---

### 遍历：三种方式，效率差异显著

**推荐 `entrySet()`**，一次取得键和值，无二次查表：

```java
for (Map.Entry<String, Integer> e : scores.entrySet()) {
    System.out.println(e.getKey() + " = " + e.getValue());
}
```

`keySet()` 只取键，若同时需要值，每一轮都要额外调用一次 `map.get(key)`，在大 Map 上会累积可观的哈希查找开销。Java 8 的 `forEach` 更简洁，内部仍基于 `entrySet`，适合项目已锁定 Java 8+ 的场景：

```java
scores.forEach((k, v) -> System.out.println(k + " = " + v));
```

结论：**键值都要，用 `entrySet` 或 `forEach`；只需键，才用 `keySet`**。

---

### Java 8 默认方法：消除样板逻辑

Java 8 为 `Map` 引入了一组默认方法，让常见的“判空—计算—写回”模式可以用一行表达。

**`putIfAbsent(K, V)`** ：键不存在或对应 `null` 时才放入：

```java
Map<String, Integer> counts = new HashMap<>();
counts.putIfAbsent("apple", 1);
counts.putIfAbsent("apple", 100); // 不会覆盖，仍为 1
```

**`computeIfAbsent(K, Function)`** ：键不存在时，用函数计算初始值并放入。这是构建“多值 Map”的标准写法：

```java
Map<String, List<Integer>> index = new HashMap<>();
index.computeIfAbsent("alpha", k -> new ArrayList<>()).add(42);
index.computeIfAbsent("alpha", k -> new ArrayList<>()).add(43);
// alpha -> [42, 43]
```
关键点：`computeIfAbsent` 返回的是 Map 中实际存在的值（新建或已有），可以直接链式操作。

**`merge(K, V, BiFunction)`** ：最典型的用途是计数或合并：

```java
Map<String, Integer> counts = new HashMap<>();
for (String word : List.of("apple", "banana", "apple")) {
    counts.merge(word, 1, Integer::sum);
}
// apple=2, banana=1
```
`merge` 的语义是：键不存在或值为 `null` 时直接放入第二个参数；否则用第三个函数将旧值与新值合并。

**`compute`** 对已有键值做完整重算，若函数返回 `null`，会删除该键：

```java
map.compute(key, (k, old) -> old == null ? 1 : old + 1);
```

**`getOrDefault`** 已在上文示例中体现，用于取值时提供兜底值。

---

### 条件操作：`remove(K,V)` 与 `replace`

这两个方法都要求**键和值同时匹配**才执行，适合需要原子性判断的场景：

```java
map.put("x", 1);
map.remove("x", 2);       // 不删除，值不匹配
map.remove("x", 1);       // 删除
map.replace("x", 1, 100); // 只有键和旧值都匹配才替换
```
`replace(K, V)`（两参数版本）则不论旧值直接替换。

---

### 不可变 Map 工厂方法（Java 9+）

`Map.of(...)` 创建真正不可变的 Map，任何修改操作都会抛出 `UnsupportedOperationException`，且不允许 `null` 键或值：

```java
Map<String, Integer> config = Map.of("timeout", 30, "retries", 3);
config.put("x", 1); // UnsupportedOperationException
```
`Map.of` 最多支持 10 对键值；超过 10 对使用 `Map.ofEntries(Map.entry(...), ...)`。Java 8 及以前可以用 `Collections.unmodifiableMap(origin)` 获得只读视图，但原 Map 仍然可变，并非真正不可变。


## 三个通用实现类怎么选

| 实现类 | 顺序 | 典型时间复杂度 | 选型场景 |
|---|---|---|---|
| **`HashMap`** | 不保证 | 平均 O(1) | 绝大多数场景，追求最大速度，不关心迭代顺序 |
| **`LinkedHashMap`** | 插入顺序或访问顺序 | 接近 HashMap | 需要按插入顺序迭代，或实现 LRU 缓存（访问顺序 + `removeEldestEntry`） |
| **`TreeMap`** | 按键排序（自然顺序或 Comparator） | O(log n) | 需要按键有序迭代，或使用 `SortedMap`/`NavigableMap` 的范围操作 |

官方建议的选型逻辑很直接：要排序用 `TreeMap`；要最大速度且不关心顺序用 `HashMap`；要接近 HashMap 的性能且保持插入顺序用 `LinkedHashMap`。`LinkedHashMap` 的 `removeEldestEntry` 重写是构建简单 LRU 缓存的标准手法。

线程安全场景下，`ConcurrentHashMap` 是并发 Map 的首选，读操作通常不加锁，写操作使用桶级锁或 CAS（JDK 8+），不应使用 `HashMap` 或 `Collections.synchronizedMap` 替代。


## 需要留意的关键问题

**1. 可变对象作为键。** 这是 Map 契约中最需要警惕的一条。如果一个键在被放入 Map 后，其 `equals`/`hashCode` 所依赖的字段被修改，Map 将无法再正常定位该键，`get`、`remove` 都会失败。优先使用不可变类型（`String`、`Integer`、枚举等）作为键；如果必须使用可变对象，只将不可变字段纳入 `equals`/`hashCode` 的计算。

**2. 遍历中修改 Map。** 使用增强 for 或迭代器遍历时直接 `put`/`remove` 原 Map，会触发 `ConcurrentModificationException`。正确做法是先收集修改，遍历结束后统一执行；或改用 `ConcurrentHashMap` 等并发容器。

**3. `keySet` + `get` 的隐性开销。** 在 `TreeMap` 上，`keySet` 循环中每次 `get` 额外消耗一次 O(log n) 查找；在 `HashMap` 上则是额外的哈希计算与桶探测。键值都需要时，`entrySet` 是默认推荐。

**4. 不可变 Map 的 `null` 限制。** `Map.of` / `Map.ofEntries` 均不接受 `null` 键或值，尝试放入会抛出 `NullPointerException`；这与 `HashMap` 的宽松行为不同。


总体而言，日常开发中 `HashMap` 覆盖大部分需求，Java 8 的 `computeIfAbsent` / `merge` / `getOrDefault` 能显著简化 Map 相关逻辑，而 `LinkedHashMap` 与 `TreeMap` 分别解决顺序和排序两个特定问题。真正需要刻意防范的，主要是**可变键**和**遍历中修改**这两类契约违规。
