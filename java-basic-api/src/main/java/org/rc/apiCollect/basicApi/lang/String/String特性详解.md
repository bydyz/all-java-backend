Java 中的 `String` 是一个设计精巧且使用极其频繁的类。它的核心特性——**不可变性**，深刻地影响了它的内存模型、性能表现以及与其它对象的交互方式。

### 🔒 核心基石：不可变性

`String` 的不可变性是指，一个 `String` 对象一旦被创建，其内部的字符序列就**永远无法被改变**。任何看似修改字符串的操作（如 `concat`、`substring`、`replace`），实际上都会返回一个**全新的 `String` 对象**，而原始对象保持不变。

从 JDK 源码层面看，不可变性由以下设计保证：
```java
public final class String // 1. 类被 final 修饰，禁止被继承和覆盖方法
    implements java.io.Serializable, Comparable<String>, CharSequence {

    // 2. 存储数据的数组被 private 和 final 修饰
    // JDK 9 之前是 char[]，之后是 byte[] 以节省内存
    private final byte[] value;

    // ...
}
```
`final` 类防止了子类通过重写方法来破坏不可变性；`private final` 的 `value` 数组则保证了其引用地址和内容在初始化后无法被外部直接修改。

**代码说明**：
```java
String s = "Hello";
s.concat(" World"); // 看似修改 s，但返回值被丢弃
System.out.println(s); // 输出仍然是 "Hello"

// 必须用新变量接收“修改”后的结果
String s2 = s.concat(" World");
System.out.println(s2); // 输出 "Hello World"
System.out.println(s == s2); // false，因为 s2 是一个全新的对象
```

不可变性带来了三大核心优势：
1. **线程安全**：对象状态永不改变，可被多线程自由共享，无需同步。
2. **支持缓存**：哈希值（`hashCode`）在首次计算后可以被缓存，因为内容不会变，这让 `String` 作为 `HashMap` 的键时非常高效。
3. **安全性**：防止敏感信息（如密码、网络地址）在传递过程中被意外篡改。

---

### 🏊 字符串常量池与对象创建

为了减少大量重复字符串对象的开销，JVM 维护了一个**字符串常量池**。它主要影响 `String` 对象的创建方式和内存位置。

`String` 主要有两种创建方式：
*   **字面量方式 (`String s = "abc"`)**：JVM 会首先检查常量池中是否存在 `"abc"`。若存在，直接返回池中对象的引用；若不存在，则在池中创建该字符串，再返回其引用。
*   **`new` 关键字 (`String s = new String("abc")`)**：这**必定**会在堆内存中创建一个全新的 `String` 对象。同时，如果 `"abc"` 尚未在常量池中，也会在池中创建它（或确保其存在）。

**代码说明**：
```java
String s1 = "Java";
String s2 = "Java";
String s3 = new String("Java");

System.out.println(s1 == s2); // true，都指向常量池中的同一个对象
System.out.println(s1 == s3); // false，s3 指向堆中一个全新的对象
```

**`intern()` 方法**：一个 `new` 创建的字符串可以通过 `intern()` 方法尝试“入池”。如果常量池中已存在内容相等的字符串，`intern()` 返回池中对象的引用；否则，它会将当前对象的引用**记录到池中**（JDK 7+ 行为）并返回。
```java
String s3 = new String("Java");
String s4 = s3.intern(); // s4 尝试从常量池获取 "Java"
System.out.println(s1 == s4); // true，s4 现在指向池中的对象
System.out.println(s3 == s4); // false，s3 仍在堆中
```

---

### ⚖️ `equals` 与 `==` 的本质区别

在比较 `String` 时，这两个操作符的含义截然不同：
*   **`==`**：比较的是**引用（内存地址）**，即两个变量是否指向**同一个对象**。
*   **`equals()`**：`String` 类重写了 `Object` 的 `equals` 方法，将其改为**内容（值）比较**，即两个字符串的字符序列是否逐一相等。

**代码说明**：
```java
String a = "hello";
String b = new String("hello");

System.out.println(a == b);      // false，引用不同
System.out.println(a.equals(b)); // true，内容相同
```
**实践原则**：判断字符串内容是否相等，**永远使用 `equals()`**。

---

### ➕ 字符串拼接的演进与性能

`String` 的不可变性决定了 `+` 拼接操作会产生新对象。但其底层实现随 JDK 版本演进，性能特性也因此不同。

**JDK 8 及之前**：`+` 拼接在编译后会被转换为 `StringBuilder` 的 `append` 链。在**非循环**的简单拼接中，这与直接使用 `StringBuilder` 效率相当。
```java
// String result = "value " + i; 在 JDK 8 下等价于：
String result = new StringBuilder().append("value ").append(i).toString();
```

**JDK 9 及之后**：得益于 `invokedynamic` 和 `StringConcatFactory`，`+` 拼接不再依赖 `StringBuilder`，而是由 JVM 在运行时动态生成更高效的拼接策略，通常比 `StringBuilder` 更快，因为它避免了中间对象的创建和额外的数组拷贝。

**循环拼接的陷阱**：无论哪个版本，在**循环体内**使用 `+` 拼接字符串都是性能大忌。因为每次迭代都会创建新的 `String`（以及可能的 `StringBuilder`）对象，导致大量临时垃圾。此时应**显式使用 `StringBuilder`**（非线程安全，性能更高）或 `StringBuffer`（线程安全）。

```java
// 低效：循环中使用 +
String result = "";
for (int i = 0; i < 1000; i++) {
    result += i; // 每次迭代都可能创建新对象
}

// 高效：显式使用 StringBuilder
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i);
}
String result2 = sb.toString();
```

---

### 💡 其他值得关注的特性

*   **不可变性与 `switch` 支持**：`String` 的不可变性及其稳定的 `hashCode`（可被 `switch` 的底层实现利用进行高效分发），是它能够作为 `switch` 语句条件表达式的基础。
*   **字符编码**：`String` 内部以 UTF-16 编码存储字符（JDK 9 后对 Latin-1 字符使用更紧凑的 `byte[]` 以节省空间），这决定了其处理 Unicode 字符的方式。

总结来说，理解 `String` 的不可变性是掌握其常量池、`equals` 比较、拼接性能等所有衍生特性的关键。这一设计在安全性、线程安全和性能之间做出了精妙的权衡。
