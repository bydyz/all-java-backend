Java 的 `foreach` 循环（官方称“增强 `for` 语句”）是 JDK 5 引入的语法糖。它的本质是**编译器根据被遍历对象的类型（数组或 `Iterable`），将其自动翻译为等价的普通 `for` 循环**，从而简化了遍历代码，但也在底层引入了相应的约束。

---

### 特点：简洁但有明确的边界

`foreach` 的首要特点是**语法简洁**，隐藏了索引或迭代器的管理细节。但这份简洁的代价是**控制权的让渡**，它带来了一系列明确的能力边界：

- **无法获取索引**：循环变量直接是元素本身，不暴露当前索引。若需索引，必须自行维护计数器。
- **无法修改集合结构**：循环中通过集合自身的方法（如 `list.remove()`）增删元素，几乎必然触发 `ConcurrentModificationException`（fail-fast）。
- **无法修改数组/基本类型集合的元素**：对循环变量赋值（如 `for (int x : arr) x = 10;`）只改变局部变量，不影响原数组或集合中的元素。
- **无法在同一个循环中遍历多个集合**：`foreach` 的头部只允许声明一个循环变量。

### 用法：统一的遍历入口

`foreach` 可以遍历**数组**以及所有实现了 `Iterable` 接口的集合（`List`、`Set`、`Map` 的 `entrySet()` / `values()` 等）。

**遍历数组：**
```java
int[] numbers = {1, 2, 3, 4, 5};
int sum = 0;
for (int num : numbers) {   // 循环变量 num 依次持有每个元素的值
    sum += num;
}
```

**遍历 `List`：**
```java
List<String> list = Arrays.asList("A", "B", "C");
for (String s : list) {
    System.out.println(s);
}
```

**遍历 `Map` 的条目：**
```java
Map<String, Integer> map = new HashMap<>();
map.put("apple", 1);
map.put("banana", 2);
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    System.out.println(entry.getKey() + " -> " + entry.getValue());
}
```

### 执行原理：编译器的两种翻译策略

`foreach` 的真正行为，由 Java 语言规范（JLS）以“等价翻译”的形式精确界定，依据表达式的类型分为两条路径。

#### 对数组：翻译为索引循环

如果表达式是数组类型 `T[]`，`foreach` 等价于以下普通 `for` 循环：
```java
T[] #a = Expression;          // #a 是编译器生成的临时变量，持有数组引用
for (int #i = 0; #i < #a.length; #i++) {
    TargetType Identifier = #a[#i];   // 每次迭代取出一个元素赋给循环变量
    Statement
}
```
反编译后的源码也能印证这一点：`for (String s : array)` 会被展开为基于 `array.length` 和索引的循环。因此，**遍历数组时 `foreach` 的开销与手写索引循环几乎相同**，不存在迭代器对象分配。

#### 对 `Iterable`：翻译为迭代器循环

如果表达式是 `Iterable` 的子类型，`foreach` 等价于：
```java
for (I #i = Expression.iterator(); #i.hasNext(); ) {   // #i 是编译器生成的迭代器引用
    TargetType Identifier = (TargetType) #i.next();     // 取出下一个元素
    Statement
}
```
对集合使用 `foreach` 时，**每次循环都依赖于迭代器的 `hasNext()` 与 `next()`**。反编译结果与此完全一致：`for (Integer i : list)` 被展开为 `Iterator` 驱动的 `while` 结构。这也解释了为什么集合 `foreach` 中不能直接调用集合的增删方法——迭代器内部维护着“预期修改计数”，集合被外部修改后会与迭代器状态脱节，从而抛出 `ConcurrentModificationException`。

> 一个容易被忽略的细节：`foreach` 语法本身**并不强制要求 `Iterable`**。编译器只认上述两种等价形式；如果某个类型既不是数组也不实现 `Iterable`，即使定义了 `iterator()` 方法，也无法直接用于 `foreach`，除非它显式实现了该接口。

### 关键限制与应对

#### 限制一：遍历中删除元素

以下代码在绝大多数集合上都会抛出 `ConcurrentModificationException`：
```java
List<String> list = new ArrayList<>(List.of("a", "b", "c"));
for (String s : list) {
    if (s.equals("b")) {
        list.remove(s);   // 危险：集合被修改，迭代器状态失效
    }
}
```
**替代方案**：若确需在遍历中删除，应显式使用 `Iterator` 并调用 `iterator.remove()`；`Iterator` 的 `remove()` 会同步更新迭代器的预期计数，从而避免异常。或者改用索引 `for` 循环（适用于 `List`），或使用 `removeIf` 等批量方法。

#### 限制二：无法通过循环变量修改元素

对于引用类型，修改循环变量指向的对象**内部状态**是有效的（因为传递的是引用副本）；但**重新赋值循环变量本身**不会影响集合或数组中的引用。对于基本类型数组，赋值循环变量完全无法修改原数组元素。

#### 限制三：无索引

若逻辑依赖索引，`foreach` 本身不提供。可在循环外声明计数器手动递增，或换回普通索引 `for` 循环。

### 适用场景

`foreach` 最适合**只读遍历**——即只需依次访问每个元素，而不修改集合结构、不依赖索引、不重新赋值循环变量的场景。它在可读性上明显优于手写迭代器循环，且对数组的性能与索引循环基本持平。当需求超出“只读访问”的范围时，应毫不犹豫地换用索引 `for` 循环或显式 `Iterator`，而不是试图在 `foreach` 内部绕过其设计约束。
