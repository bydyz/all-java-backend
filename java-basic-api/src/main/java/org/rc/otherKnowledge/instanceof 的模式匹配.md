# Java `instanceof` 模式匹配整理

Java 的 `instanceof` 模式匹配（Pattern Matching for `instanceof`）在 **Java 16** 中正式定型（JEP 394）。它的核心价值在于：将**类型检查**与**变量声明及类型转换**合并为一步，消除 `instanceof` 之后紧跟的显式强制转换，同时通过流式作用域（flow scoping）保证变量只在类型匹配成功的代码路径中可用，从语言层面杜绝误转型的风险。

---

## 基础用法：从“检查后强转”到“检查即绑定”

传统写法需要先判断类型，再手动强转：

```java
if (obj instanceof String) {
    String s = (String) obj; // 冗余的强转
    System.out.println(s.length());
}
```

使用模式匹配后，`instanceof` 的右操作数是一个**类型模式**（如 `String s`），匹配成功时变量 `s` 自动被初始化，无需强转：

```java
if (obj instanceof String s) {
    System.out.println(s.length()); // s 已自动转换为 String
}
```

官方文档中的经典示例是对不同形状计算周长，改写后去除了所有显式强转：

```java
public static double getPerimeter(Shape shape) throws IllegalArgumentException {
    if (shape instanceof Rectangle r) {
        return 2 * r.length() + 2 * r.width();
    } else if (shape instanceof Circle c) {
        return 2 * c.radius() * Math.PI;
    } else {
        throw new IllegalArgumentException("Unrecognized shape");
    }
}
```

---

## 作用域：流式作用域（Flow Scoping）

模式变量 `s` 的作用域不是“整个 `if` 块”那么简单，而是**仅在 `instanceof` 确定为 `true` 才能到达的位置**才在作用域内。

**在 `else if` 中不可访问前一个模式的变量**：

```java
if (shape instanceof Rectangle s) {
    // s 在此可用（Rectangle 类型）
} else if (shape instanceof Circle s) {
    // 此处的 s 是 Circle 类型，前一个 Rectangle s 不可见
} else {
    // Rectangle s 和 Circle s 都不可用
}
```

**结合 `&&` 时**，因为 `&&` 是短路求值，右侧只有在左侧 `instanceof` 为 `true` 时才会执行，所以变量可以在右侧使用：

```java
if (shape instanceof Rectangle r && r.length() > 5) {
    // r.length() 是合法的，因为只有 instanceof 为 true 才会走到这里
}
```

**结合 `||` 时则非法**，因为 `instanceof` 为 `false` 时右侧仍可能执行，模式变量没有确定的赋值：

```java
if (shape instanceof Rectangle r || r.length() > 0) { // 编译错误
    // 错误：右侧执行时 r 未赋值
}
```

**通过 `!(...)` 提前返回**后，模式变量的作用域可以“延续”到后续代码：

```java
if (!(shape instanceof Rectangle r)) {
    return false; // 此处不能用 r（instanceof 为 false）
}
return r.length() > 5; // 此处 r 可用，因为执行到这里时 instanceof 必然为 true
```

---

## Record 模式：在 `instanceof` 中直接解构

从 **Java 21** 起（Record Patterns 正式定型，JEP 440），`instanceof` 的右侧可以使用 **Record 模式**，在匹配类型的同时**递归地解构** Record 的组件。

假设有 `record Point(double x, double y)`，传统方式需要先绑定 `p`，再调用访问器：

```java
if (obj instanceof Point p) {
    System.out.println(Math.toDegrees(Math.atan2(p.y(), p.x())));
}
```

使用 Record 模式可以直接在模式中“拆出” `x` 和 `y`：

```java
if (obj instanceof Point(double x, double y)) {
    System.out.println(Math.toDegrees(Math.atan2(y, x)));
}
```

也可以在组件列表中使用 `var`，让编译器推断类型：

```java
if (obj instanceof Point(var x, var y)) {
    System.out.println(Math.toDegrees(Math.atan2(y, x)));
}
```

Record 模式还支持**嵌套解构**。例如一个 `Box<T>` 中装的是 `String`，可以一步匹配到内层值：

```java
record Box<T>(T t) {}

if (bo instanceof Box<String>(String s)) {
    System.out.println("Box contains: " + s);
}
// 或使用 var 让编译器推断：
if (bo instanceof Box(var s)) {
    System.out.println("Box contains: " + s);
}
```

---

## 延伸：`switch` 模式匹配与 `instanceof` 的衔接

`instanceof` 模式匹配常用于 `if-else if` 链，而 **Java 21** 的 `switch` 模式匹配（JEP 441）进一步将这种多分支类型判断改写为更紧凑的 `switch`。官方文档展示了将前述形状周长逻辑改写为 `switch` 表达式：

```java
public static double getPerimeter(Shape shape) {
    return switch (shape) {
        case Rectangle r -> 2 * r.length() + 2 * r.width();
        case Circle c    -> 2 * c.radius() * Math.PI;
        default -> throw new IllegalArgumentException("Unrecognized shape");
    };
}
```

`switch` 模式同样支持 Record 解构，并且当选择器类型是 **sealed 类**时，编译器可以检查分支是否穷尽（exhaustive），在不需要 `default` 的情况下保证安全性。但需要注意，`instanceof` 模式匹配本身不要求穷尽性，而 `switch` **表达式**要求穷尽；`instanceof` 链更适合逐步筛选、允许漏报的场景，`switch` 模式则更适合需要明确覆盖所有可能类型的分发逻辑。

---

## 小结

| 用法 | 最低版本 | 解决的问题 |
|---|---|---|
| `if (obj instanceof Type t)` | Java 16 | 消除检查后的显式强转，防止误转型 |
| 流式作用域 | Java 16 | 变量仅在确定匹配的路径中可见，兼顾安全与可读 |
| `instanceof RecordType(组件...)` | Java 21 | 在类型检查的同时解构 Record，减少访问器调用样板 |
| `switch` 模式匹配 | Java 21 | 将多分支 `instanceof` 链转化为更紧凑、可穷尽检查的形式 |

日常使用中，最频繁的是基础的 `instanceof Type variable` 形式；当涉及 Record 或大量类型分支时，再逐步引入 Record 模式与 `switch` 模式匹配即可。
