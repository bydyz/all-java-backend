# Java `instanceof` 用法及注意事项详解

`instanceof` 是 Java 的二元运算符，用于在**运行时**判断一个引用是否指向某个类/接口/数组类型的实例。它返回 `boolean`。Java
16 起支持**模式匹配**，Java 21 起支持 **switch 模式匹配**与**记录模式**。

---

## 一、基本语法与语义

```java
boolean result = obj instanceof Type;
```

规则：

| 场景                          | 结果                 |
|-----------------------------|--------------------|
| `obj` 为 `null`              | 永远 `false`（不会 NPE） |
| `obj` 实际类型是 `Type` 或其子类/实现类 | `true`             |
| `obj` 实际类型与 `Type` 无继承/实现关系 | `false`            |
| 左操作数为基本类型（`int` 等）          | **编译错误**           |
| 右操作数为基本类型（`int` 等）          | **编译错误**           |

```java
Object o = "hello";
System.out.println(o instanceof String);     // true
System.out.println(o instanceof Object);     // true
System.out.println(o instanceof CharSequence);// true（String 实现该接口）
System.out.println(o instanceof Integer);    // false（编译通过，运行 false）

Object n = null;
System.out.println(n instanceof String);     // false，不会 NPE
System.out.println(null instanceof String);  // false，null 字面量合法

// int x = 1;
// x instanceof Integer;      // 编译错误：左操作数不能是基本类型
// o instanceof int;          // 编译错误：右操作数不能是基本类型
```

**典型用途**：类型判断后强转。

```java
if (obj instanceof String) {
    String s = (String) obj;   // Java 16 之前必须手动强转
    System.out.println(s.length());
}
```

---

## 二、编译期检查规则（易踩坑）

编译器会做“**是否可能存在子类型关系**”的静态检查，不可能成立时直接报错。规则可简化如下：

- 左操作数 `S` 是 **final 类**：`T` 必须是 `S` 自身或其父类/父接口，否则编译错误。
- 左操作数 `S` 是**非 final 类**：只要 `T` 是类或接口，基本都放行（编译器认为可能存在共同子类）。
- 左操作数 `S` 是**接口**：`T` 是 final 类且未实现 `S` 时编译错误，否则放行。

```java
String s = "x";
// if (s instanceof Integer) {}          // 编译错误：String 是 final，不可能是 Integer

Integer i = 1;
if (i instanceof Number) {}               // OK，Integer 是 Number 子类
// if (i instanceof String) {}            // 编译错误：Integer 是 final，不可能是 String

Number n = 1;
// if (n instanceof String) {}            // 编译错误：String 是 final 且非 Number 子类

Object o = new Object();
if (o instanceof String) {}               // OK，Object 非 final

ArrayList<?> list = new ArrayList<>();
if (list instanceof LinkedList) {}        // 编译通过，但运行永远 false
                                          // 两个都是非 final 类，编译器不深入判断

Runnable r = () -> {};
// if (r instanceof String) {}            // 编译错误：String 是 final 且未实现 Runnable
if (r instanceof Thread) {}               // OK，Thread 实现 Runnable
```

> 结论：**编译通过 ≠ 运行可能为 true**。`ArrayList instanceof LinkedList` 就是典型“永远 false 但能编译”的例子，属于逻辑错误而非语法错误。

---

## 三、`instanceof` 与泛型（类型擦除）

泛型信息在运行时被擦除，因此 `instanceof` 右操作数**不能是不可具体化的参数化类型**，只能用原始类型或无限制通配符。

```java
List<String> list = new ArrayList<>();

if (list instanceof List) {}       // OK，原始类型
if (list instanceof List<?>) {}    // OK，无限制通配符
// if (list instanceof List<String>) {}  // 编译错误：illegal generic type for instanceof
// if (list instanceof ArrayList<String>) {} // 编译错误

// 数组类型可以带泛型组件类型吗？也不行：
// if (list instanceof List<?>[]) {}  // 编译错误
```

---

## 四、数组相关的 `instanceof`

数组也是对象，规则与类一致，注意**数组协变**：

```java
int[] ints = new int[0];
System.out.println(ints instanceof Object);    // true，数组是对象
System.out.println(ints instanceof int[]);     // true
System.out.println(ints instanceof Object[]);  // false，int[] 不是 Object[] 子类型

String[] strs = new String[0];
System.out.println(strs instanceof Object[]);  // true，数组协变
System.out.println(strs instanceof String[]);  // true
```

---

## 五、模式匹配 `instanceof`（Java 16 正式）

### 5.1 基本写法

```java
Object obj = "hello";

if (obj instanceof String s) {          // 判断通过则自动绑定 s，无需强转
    System.out.println(s.length());     // s 类型为 String
}
```

### 5.2 作用域是“流敏感”（flow scoping），不是普通块作用域

模式变量只在**确定判断为 true 的路径**上可见：

```java
// ① 提前返回后可用
if (!(obj instanceof String s)) {
    return;
}
System.out.println(s.length());         // OK，此处 s 一定已绑定

// ② else 分支不可用
if (obj instanceof String s) {
    System.out.println(s);
} else {
    // System.out.println(s);           // 编译错误：s 未绑定
}

// ③ && 右侧可用
if (obj instanceof String s && s.length() > 3) { ... }   // OK

// ④ || 右侧不可用（短路时可能未绑定）
// if (obj instanceof String s || s.length() > 3) { }    // 编译错误

// ⑤ 位运算符 & / | 也不支持
// if (obj instanceof String s & s.length() > 3) { }     // 编译错误

// ⑥ 三元表达式可用
String r = obj instanceof String s ? s.toUpperCase() : "";
```

### 5.3 模式变量是隐式 final

```java
if (obj instanceof String s) {
    // s = "x";    // 编译错误：模式变量不能重新赋值
    s.length();    // 只能读
}
```

### 5.4 不能与已有变量重名

```java
String s = "old";
// if (obj instanceof String s) { }   // 编译错误：s 已在作用域内定义
```

### 5.5 泛型 + 模式匹配

```java
Object o = new ArrayList<String>();
if (o instanceof List<?> l) {          // OK
    System.out.println(l.size());
}
// if (o instanceof List<String> l) {} // 编译错误
```

---

## 六、switch 模式匹配与记录模式（Java 21 正式）

### 6.1 switch 模式匹配

```java
static String describe(Object obj) {
    return switch (obj) {
        case Integer i -> "int: " + i;
        case String s  -> "string: " + s;
        case null      -> "null";       // 必须显式处理 null
        default        -> "unknown";
    };
}
```

**重要注意点**：

1. **`default` 不匹配 `null`**。若 selector 可能为 `null` 且没有 `case null`，会抛 `NullPointerException`。
2. 可以用 `case null, default -> ...` 合并处理。
3. 分支必须**穷尽**（对 sealed 类型由编译器检查，对普通类型需要 `default`）。
4. 可以用 `when` 守卫：

```java
static String len(Object obj) {
    return switch (obj) {
        case String s when s.isEmpty() -> "empty";
        case String s                  -> "len=" + s.length();
        case null                      -> "null";
        default                        -> "other";
    };
}
```

### 6.2 记录模式

```java
record Point(int x, int y) {}
record Line(Point start, Point end) {}

static String show(Object obj) {
    return switch (obj) {
        case Point(int x, int y) when x == y -> "对角线上的点";
        case Point(int x, int y)             -> "点(" + x + "," + y + ")";
        case Line(Point(var x1, var y1), Point(var x2, var y2))
                -> "线段 (" + x1 + "," + y1 + ")-(" + x2 + "," + y2 + ")";
        case null -> "null";
        default   -> "other";
    };
}
```

---

## 七、常见陷阱与注意事项汇总

### 7.1 `null` 安全，但仍要区分 `getClass()` 与 `instanceof`

```java
Object o = null;
System.out.println(o instanceof Object);   // false，不抛异常

// 精确类型判断（不含子类）应该用 getClass()
String s = "x";
System.out.println(s.getClass() == String.class);   // true
System.out.println(s instanceof CharSequence);      // true（含接口/父类）
```

若需要“严格等于某类型”，用 `getClass()`；若需要“是某类型或其子类型”，用 `instanceof`。

### 7.2 避免“判断后强转”的冗余

```java
// 旧写法
if (obj instanceof String) {
    String s = (String) obj;
    ...
}

// 推荐（Java 16+）
if (obj instanceof String s) {
    ...
}
```

### 7.3 包装类型与自动装箱

```java
Integer i = 128;
Object o = i;
System.out.println(o instanceof Integer);  // true
System.out.println(o instanceof Number);   // true

Integer n = null;
System.out.println(n instanceof Number);   // false
```

注意 `Integer` 与 `int` 的自动装箱：`obj instanceof Integer` 检查的是包装类型；`int` 不能直接用于 `instanceof`。

### 7.4 接口的宽松检查

```java
interface A {}
interface B {}

class C implements A {}
C c = new C();
if (c instanceof B) { }   // 编译通过（C 非 final，可能有子类实现 B），运行 false

final class D implements A {}
D d = new D();
// if (d instanceof B) {} // 编译错误：D 是 final 且未实现 B
```

### 7.5 性能与设计

- `instanceof` 本身是廉价操作，JIT 可优化；但**长 if-else 类型链**通常是设计味道。
- 优先用多态、策略模式、访问者模式替代大量 `instanceof`。
- 对于 sealed 类型，用 `switch` 模式匹配可让编译器检查穷尽性，比手写 `instanceof` 链更安全。

### 7.6 运算符优先级

`instanceof` 优先级低于算术/关系运算符，高于 `==`/`&&`：

```java
Object o = "x";
boolean b = o instanceof String == true;   // 等价于 (o instanceof String) == true
// 建议加括号，避免歧义
```

### 7.7 `instanceof` 与 `Class.isInstance` / `isAssignableFrom`

```java
Object o = "x";
o instanceof String;                      // 语法级判断
String.class.isInstance(o);               // 等价，运行时反射
CharSequence.class.isAssignableFrom(String.class); // 类之间可赋值性
```

---

## 八、最佳实践小结

1. **优先用模式匹配**：`if (obj instanceof String s)` 替代判断+强转。
2. **判空交给 `instanceof`**：`null instanceof X` 恒为 false，无需先判 null。
3. **警惕“永远 false”**：非 final 类之间的 `instanceof` 编译通过但可能永远 false，注意逻辑正确性。
4. **泛型只能用 `<?>` 或原始类型**。
5. **switch 模式匹配记得处理 `case null`**，`default` 不覆盖 null。
6. **精确类型用 `getClass()`，类型族用 `instanceof`**。
7. **少用长类型链**，多用多态或 sealed + switch。

掌握以上规则，就能既写出正确的类型判断，又避免编译期和运行期的隐蔽坑。
