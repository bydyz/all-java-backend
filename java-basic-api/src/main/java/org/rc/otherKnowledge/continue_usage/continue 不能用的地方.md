# Java 中 `continue` 不能用的地方

`continue` 必须位于**循环**（`for` / `while` / `do-while`）内部。以下位置使用 `continue` 会**编译报错**或**不符合预期**
，需要特别注意。

---

## 一、单独的 `if` 里（无循环包裹）

`if` 本身不是 `continue` 的合法容器，除非 `if` 被循环包住。

```java
public void test() {
    if (true) {
        // continue;   // ❌ 编译错误：continue outside of loop
    }
}
```

**判断标准**：看 `continue` 的**外层**有没有循环，跟 `if` 无关。

```java
// ✅ 合法：if 在循环里
for (int i = 0; i < 5; i++) {
    if (i == 3) {
        continue;
    }
    System.out.println(i);
}
```

---

## 二、方法体直接写

```java
public void test() {
    // continue;   // ❌ 编译错误：continue outside of loop
}
```

`continue` 不能脱离循环独立存在。

---

## 三、`switch` 外无循环时

`continue` **不能直接作用于 `switch`**。如果 `switch` 不在循环里，写 `continue` 会编译报错：

```java
switch (x) {
    case 1:
        // continue;   // ❌ 编译错误：continue outside of loop
}
```

只有当 `switch` **位于循环内**时，`continue` 才合法，且作用是**跳过外层循环的本次迭代**：

```java
// ✅ 合法：switch 在 for 里，continue 作用于 for
for (int i = 1; i <= 5; i++) {
    switch (i) {
        case 2:
            System.out.println("i=2，跳过本次循环");
            continue;            // ← 跳过 for 本轮剩余代码，进入 i=3
        default:
            System.out.println("i=" + i);
    }
    System.out.println("i=" + i + " 这轮结束");   // i=2 时不会打印
}
```

输出：

```
i=1
i=1 这轮结束
i=2，跳过本次循环
i=3
i=3 这轮结束
i=4
i=4 这轮结束
i=5
i=5 这轮结束
```

> **对比**：switch 里的 `break` 只跳出 switch；`continue` 则越过 switch，直接作用于外层循环。

---

## 四、lambda 内部不能跳到外部循环

lambda 有独立的执行边界，`continue` / `break` 都**不能跨过 lambda 边界**作用到外部循环。
continue / break 不作用于 lambda，也不能跳出 lambda 而作用与外部的循环

```java
public void test() {
    for (int i = 0; i < 5; i++) {
        list.forEach(x -> {
            // if (x == 3) continue;   // ❌ lambda 不能 continue 到外层 for
        });
    }
}
```

### 替代方案

**方案 1：用普通 for 代替 lambda**

```java
for (int i = 0; i < 5; i++) {
    for (int x : list) {
        if (x == 3) continue;   // ✅ 用普通 for 代替 lambda
    }
}
```

**方案 2：用 `return` 从 lambda 返回**

`return` 在 lambda 中只结束**本次 lambda 调用**，效果类似"跳过当前元素"，但不是真正的 `continue`：

```java
for (int i = 0; i < 5; i++) {
    list.forEach(x -> {
        if (x == 3) {
            return;            // ✅ 结束本次 lambda 调用，效果类似跳过 x=3
        }
        System.out.println(x);
    });
}
```

**方案 3：用 `filter` 预先过滤**

```java
list.stream()
    .filter(x -> x != 3)
    .forEach(x -> System.out.println(x));
```

---

## 五、汇总表

| 位置                             | 是否合法 | 说明                         |
|--------------------------------|------|----------------------------|
| 循环内部（`for`/`while`/`do-while`） | ✅    | 跳过本次迭代                     |
| 循环内的 `if` 里                    | ✅    | `if` 被循环包住即合法              |
| 循环内的 `switch` 里                | ✅    | 作用于外层循环，跳过本轮               |
| 带标签的循环                         | ✅    | 跳到标签所指循环的下一轮               |
| 单独的 `if` 里（无循环）                | ❌    | `continue outside of loop` |
| 方法体直接写                         | ❌    | `continue outside of loop` |
| `switch` 外无循环                  | ❌    | `continue outside of loop` |
| lambda 内部跳到外部循环                | ❌    | 不能跨 lambda 边界              |

---

## 六、一句话记忆

> **`continue` 只活在循环里。`if` 本身不是它的合法容器，`switch` 也不是——除非它们被循环包住。lambda 是硬边界，`continue`
出不去。**
