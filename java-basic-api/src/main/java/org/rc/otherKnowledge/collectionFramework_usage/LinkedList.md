`LinkedList` 是 Java 集合框架中一个特点鲜明的类，它的核心是**双向链表**。理解它“擅长什么”与“不擅长什么”，是正确使用它的关键。

### 🔗 核心特点：一个类，三重身份

`LinkedList` 同时实现了 `List` 和 `Deque` 接口，这赋予了它三种主要的使用方式。

1. **作为 `List`（列表）**：支持按索引访问，但这是它**最不擅长**的操作。`get(index)` 需要从链表头或尾开始遍历，时间复杂度为 **O(n)**。因此，**随机访问性能很差**，应避免在循环中使用 `get(i)` 遍历。
2. **作为 `Deque`（双端队列）**：这是它最自然的用法。得益于双向链表结构，在**头部或尾部**进行插入、删除、获取操作都是 **O(1)** 的。
3. **作为 `Queue`（队列）**：`Deque` 继承自 `Queue`，因此它可以自然地作为先进先出（FIFO）的队列使用。
4. **作为 `Stack`（栈）**：通过 `push()` / `pop()` 等方法，可以将其作为后进先出（LIFO）的栈使用。

**⚠️ 关键权衡**：
`LinkedList` 的“增删快”有严格前提——**操作位置必须在头或尾（或已持有节点引用）**。在列表**中间**按索引插入/删除，依然需要 O(n) 的遍历时间。此外，每个元素都需要额外的节点对象开销，内存占用比 `ArrayList` 更大，且内存不连续导致**缓存不友好**。在工程实践中，对于纯粹的列表场景，**`ArrayList` 通常是默认且更优的选择**；若需要双端操作，`ArrayDeque` 通常比 `LinkedList` 性能更好。

### 📋 API 分类详解与代码示例

`LinkedList` 的 API 可以按其承担的角色来分类理解。

#### 作为 `List` 使用（基础列表操作）

这类方法继承自 `List` 接口，特点是**按索引操作**，但性能上需要留意。

| 方法 | 说明 | 性能注意 |
|:---|:---|:---|
| `add(E e)` | 在尾部添加元素 | O(1) |
| `add(int index, E element)` | 在指定索引处插入 | **O(n)**，需先遍历定位 |
| `get(int index)` | 获取指定索引元素 | **O(n)**，需遍历 |
| `set(int index, E element)` | 替换指定索引元素 | **O(n)**，需遍历 |
| `remove(int index)` | 删除指定索引元素 | **O(n)**，需遍历 |

```java
LinkedList<String> list = new LinkedList<>();
list.add("A"); // ["A"]
list.add("B"); // ["A", "B"]
list.add(1, "X"); // ["A", "X", "B"]，在索引1插入

String elem = list.get(2); // "B"，需要遍历
list.set(0, "Z"); // ["Z", "X", "B"]
list.remove(1); // ["Z", "B"]，删除索引1的元素"X"
```
> **注意**：`remove(int index)` 和 `remove(Object o)` 是两个不同的方法。前者按索引删除，后者按对象删除。

#### 作为 `Deque` 使用（双端操作，核心优势）

这是 `LinkedList` 最应被重用的部分。`Deque` 的方法通常提供**两套策略**，应对操作失败（如队列为空或容量满）的情况：**抛出异常**或**返回特殊值**（`null` 或 `false`）。

| 操作类型 | 队首（Head）操作 | 队尾（Tail）操作 | 失败策略 |
|:---|:---|:---|:---|
| **插入** | `addFirst(e)` / `offerFirst(e)` | `addLast(e)` / `offerLast(e)` | `add`系抛异常；`offer`系返回`boolean` |
| **删除** | `removeFirst()` / `pollFirst()` | `removeLast()` / `pollLast()` | `remove`系抛异常；`poll`系返回`null` |
| **获取** | `getFirst()` / `peekFirst()` | `getLast()` / `peekLast()` | `get`系抛异常；`peek`系返回`null` |

```java
Deque<String> deque = new LinkedList<>();
// 头部插入
deque.addFirst("B");
deque.offerFirst("A"); // 队列: [A, B]
// 尾部插入
deque.addLast("C");
deque.offerLast("D"); // 队列: [A, B, C, D]

// 获取但不删除
String head = deque.getFirst(); // "A"
String tail = deque.peekLast(); // "D"

// 删除头部
String removedHead = deque.removeFirst(); // "A"，队列变为 [B, C, D]
// 删除尾部
String removedTail = deque.pollLast(); // "D"，队列变为 [B, C]
```
> **选择建议**：在不确定队列是否为空时，优先使用 `offer` / `poll` / `peek` 系列，避免异常处理的开销。

#### 作为 `Queue` 使用（FIFO 队列）

`Queue` 的方法在 `LinkedList` 中操作的是**队首（头部）**，恰好对应 `Deque` 的 `First` 系列方法。

| 操作 | 抛出异常 | 返回特殊值 |
|:---|:---|:---|
| **入队** | `add(e)` | `offer(e)` |
| **出队** | `remove()` | `poll()` |
| **查看队首** | `element()` | `peek()` |

```java
Queue<String> queue = new LinkedList<>();
queue.offer("A");
queue.offer("B");

System.out.println(queue.peek()); // "A"，队列不变
System.out.println(queue.poll()); // "A"，队列变为 ["B"]
```
`queue.add()` 与 `queue.offer()` 在 `LinkedList` 中行为基本一致（总是成功），但遵循 `Queue` 接口约定时，`offer` 是更通用的选择。

#### 作为 `Stack` 使用（LIFO 栈）

`Deque` 提供了 `push` / `pop` / `peek`，它们被定义为**操作栈顶（即队首）**。

```java
Deque<String> stack = new LinkedList<>();
stack.push("A"); // 等价于 addFirst("A")
stack.push("B"); // 等价于 addFirst("B")

System.out.println(stack.peek()); // "B"
System.out.println(stack.pop()); // "B"，等价于 removeFirst()
```
需要注意的是，`Stack` 的 `push` 在 `Deque` 中对应的是**头部插入**，因此栈顶始终是链表的第一个元素。

#### 迭代器与遍历

**必须使用迭代器**来遍历 `LinkedList`。`ListIterator` 还支持双向遍历和修改。

```java
LinkedList<String> list = new LinkedList<>(List.of("A", "B", "C"));

// 推荐：for-each 底层使用迭代器，是 O(n) 的
for (String s : list) {
    System.out.println(s);
}

// ListIterator 双向遍历
ListIterator<String> it = list.listIterator(list.size()); // 从末尾开始
while (it.hasPrevious()) {
    System.out.println(it.previous()); // 倒序输出 C, B, A
}
```
`ListIterator` 的 `add`、`set`、`remove` 操作是修改链表的安全方式，不会触发 `ConcurrentModificationException`。

### 💎 总结与使用建议

**何时选择 `LinkedList`？**

* **需要频繁在头部插入/删除**：这是 `LinkedList` 在 `List` 场景下**唯一**明显优于 `ArrayList` 的操作。
* **需要同时当作列表、队列和栈使用**：如果确实需要这种“多面手”能力，`LinkedList` 是现成的选择。
* **你的算法本身就基于链表节点操作**：例如实现 LRU 缓存（配合 `HashMap` 使用），或需要 `ListIterator` 在遍历中安全地修改结构。

**何时应避免使用 `LinkedList`？**

* **需要随机访问（按索引读取）**：`ArrayList` 的 O(1) 访问是决定性的。
* **纯粹的队列或栈场景**：`ArrayDeque` 通常比 `LinkedList` 性能更好，内存效率更高。
* **只是追加元素然后读取**：`ArrayList` 的尾部追加是均摊 O(1)，且内存布局对 CPU 缓存更友好。

Java 集合框架的设计哲学是**根据使用模式选择实现**。`LinkedList` 的价值在于其双端操作能力，而非作为一个通用的 `List` 实现。将它的 `Deque` 能力用在正确的场景，才能发挥其设计初衷。
