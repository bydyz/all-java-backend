在 JDK 5.0 之前，Java 语言层面并没有 `enum` 关键字。如果需要在代码中表达“一组固定的常量”，开发者必须*
*手动使用普通类（Class）的特性来模拟枚举**。这种模式在《Effective Java》中被称为 **“Int Enum 模式”** 的改进版，但本质上仍然是一个普通类。JDK
5.0 引入 `enum` 关键字后，枚举才成为语言级别的特性，之前那些需要手动维持的约束被固化到了编译器和 `java.lang.Enum` 基类中。

枚举类型本质上也是一种类，只不过是这个类的对象是有限的、固定的几个，不能让用户随意创建。
若枚举只有一个对象, 则可以作为一种单例模式的实现方式。
枚举类的实现：
– 在 JDK5.0 之前，需要程序员自定义枚举类型。
– 在 JDK5.0 之后，Java 支持 enum 关键字来快速定义枚举类型。
---

### JDK 5.0 之前：使用普通类“模拟”枚举

在 JDK 5.0 之前实现一个枚举类，需要严格遵循几条**自我约束**的规则，否则就无法保证“实例数量有限且不可变”的语义。

**核心要求：**

1. **属性私有且 final**：枚举所代表的属性必须用 `private final` 修饰，防止外界修改。
2. **构造器私有**：构造器必须声明为 `private`，这是最关键的一步——外界无法通过 `new` 创建新的实例，从而保证枚举对象的数量在类的内部就已经固定。
3. **提供 public static final 实例**：在类内部显式地创建并暴露固定的几个实例，通常命名为全大写。
4. **（可选）重写 `toString()`**：便于打印时有意义的输出。

**代码示例（JDK 5.0 之前）** ：

```java
// 一个“手工打造的枚举类”
class Season {
    private final String seasonName;
    private final String seasonDesc;

    // 私有构造器：外界无法 new，保证实例数量可控
    private Season(String seasonName, String seasonDesc) {
        this.seasonName = seasonName;
        this.seasonDesc = seasonDesc;
    }

    // 在类内部创建唯一的几个实例，并用 public static final 暴露
    public static final Season SPRING = new Season("春天", "春暖花开");
    public static final Season SUMMER = new Season("夏天", "夏日炎炎");
    public static final Season AUTUMN = new Season("秋天", "秋高气爽");
    public static final Season WINTER = new Season("冬天", "冰天雪地");

    public String getSeasonName() { return seasonName; }
    public String getSeasonDesc() { return seasonDesc; }

    @Override
    public String toString() {
        return "Season{" + seasonName + ", " + seasonDesc + "}";
    }
}
```

使用方式：

```java
public class Test {
    public static void main(String[] args) {
        Season spring = Season.SPRING; // 直接获取唯一实例
        System.out.println(spring);    // Season{春天, 春暖花开}
    }
}
```

这种方式**本质上就是一个普通类**。编译器不会对它做任何特殊处理，所有“枚举的语义”都依赖于开发者**自觉地**不去破坏 `private`
构造器和 `public static final` 实例的约定。它也没有 `values()`、`valueOf()` 等遍历或查找的便捷方法，需要自己额外编写。

---

### JDK 5.0 之后：`enum` 关键字

JDK 5.0 引入了 `enum` 关键字，它和 `class`、`interface` 地位相同，专门用于定义枚举类型。

#### 语法与要求

使用 `enum` 时，原先“模拟”阶段需要手动遵守的规则，大部分变成了**编译器强制的语法要求**：

- **实例必须在第一行列出**，用逗号分隔，末尾用分号结束。如果不写实例，这个枚举类就永远无法产生实例。
- 列出的实例会被编译器**自动添加 `public static final` 修饰**，无需手动书写。
- **构造器只能是 `private`**（或包私有，但效果等价于 `private`）。如果尝试声明为 `public` 或 `protected`
  ，会直接导致编译错误。原因在于枚举的实例集合必须是封闭的，公共构造器会允许外界创建新的实例，从而破坏枚举的根本语义。
- 使用 `enum` 定义的枚举类**默认继承 `java.lang.Enum`**，而不是 `Object`。由于 Java 不支持多重继承，枚举类**不能再显式继承其他任何类
  **，但**可以实现一个或多个接口**。
- 实例的 `ordinal`（序号）由声明顺序决定，从 **0** 开始，**不允许**像 C/C++ 那样用 `=` 显式赋值。

编译器给枚举类默认提供的是 private 的无参构造，如果枚举类需要的是无参构造，就不需要声明，写常量对象列表时也不用加参数
如果枚举类需要的是有参构造，需要手动定义，有参构造的 private 可以省略，调用有参构造的方法就是在常量对象名后面加(实参列表)
就可以。
JDK5.0 之后 switch，提供支持枚举类型，case 后面可以写枚举常量名，无需添加枚举类作为限定。

**代码示例（使用 `enum`）** ：

```java
enum Season {
    // 实例必须在第一行，逗号分隔，分号结束
    SPRING("春天", "春暖花开"),
    SUMMER("夏天", "夏日炎炎"),
    AUTUMN("秋天", "秋高气爽"),
    WINTER("冬天", "冰天雪地"); // 分号不能省

    private final String seasonName;
    private final String seasonDesc;

    // 构造器默认就是 private，写不写 private 修饰符效果相同
    Season(String seasonName, String seasonDesc) {
        this.seasonName = seasonName;
        this.seasonDesc = seasonDesc;
    }

    public String getSeasonName() { return seasonName; }
    public String getSeasonDesc() { return seasonDesc; }
}
```

#### 底层本质：编译器生成的“普通类”

`enum` 看起来像一种新的数据类型，但**编译之后，它仍然是一个普通的 Java 类**。用 `javap` 查看上面 `Season`
编译后的字节码，会发现编译器做了以下工作：

1. 将 `Season` 声明为 **`final class`**，并让它继承 `java.lang.Enum<Season>`。`final` 意味着枚举不能再被继承。
2. 将 `SPRING`、`SUMMER` 等实例编译为 **`public static final` 的静态字段**。
3. 生成一个 **`static {}` 静态代码块**，在其中依次 `new` 出所有实例，并按照声明顺序传入 `"SPRING", 0`、`"SUMMER", 1`
   等 `name` 和 `ordinal` 参数，完成初始化。
4. 自动生成 **`values()`** 方法（返回所有实例的数组）和 **`valueOf(String)`**
   方法（根据名称查找实例）。这两个方法并非继承自 `Enum`，而是编译器**插入到具体枚举类中**的。

因此，`enum` 本质上是一个**“受限制的类”**：你获得了更简洁的语法和编译器保证的约束，但失去了继承其他类的能力，实例的集合也被彻底封闭。

---

### 从“模拟”到 `enum`：关键特性的变化

将 JDK 5.0 前后的方式对比，差异不只在语法层面，更在于**语义保证由谁负责**。

| 维度              | JDK 5.0 之前（普通类模拟）                                  | JDK 5.0 之后（`enum`）                                       |
|-----------------|----------------------------------------------------|----------------------------------------------------------|
| **实例封闭性**       | 依赖开发者自觉遵守 `private` 构造器；一旦疏忽（或通过反射），就可能产生额外实例      | 编译器强制 `private` 构造器；`Enum` 内部机制进一步阻止反射创建实例               |
| **类型安全**        | 方法参数若使用模拟类型，虽比 `int`/`String` 常量好，但本质上仍是普通对象，约束力有限 | 参数类型就是具体的枚举类型，编译器在编译期即可限定取值范围，避免传入非法值                    |
| **遍历与查找**       | 需要手动编写类似 `values()` 和 `valueOf()` 的辅助方法            | 编译器自动生成 `values()` 和 `valueOf(String)`，无需任何额外代码          |
| **`switch` 支持** | 普通类对象**不能**直接用在 `switch` 表达式中                      | 枚举对象可以直接用于 `switch`，`case` 分支直接写实例名，无需类名前缀               |
| **序列化安全性**      | 若实现 `Serializable`，反序列化可能产生**新的实例**，破坏“唯一性”        | 枚举的序列化由 `Enum` 专门处理；反序列化时根据名称查找已有实例，**不会创建新对象**，天然保持实例唯一 |
| **单例实现**        | 需要额外处理反射、序列化等问题（如 `readResolve`）                   | 单元素枚举是**最简洁、最安全的单例实现方式**，线程安全由 JVM 类加载机制保证，且能抵御反射和反序列化攻击 |

`switch` 的底层实现尤其值得留意：JVM 的 `switch` 指令本身只支持整数类型。编译器在处理枚举的 `switch` 时，会生成一个**合成类
**，其中包含一个将每个枚举实例的 `ordinal` 映射到 `case` 标号的整数数组。这样即使枚举类后续被重新编译、`ordinal`
顺序发生变化，已经编译的 `switch` 代码也不会被悄悄改变语义。

---

### 总结

JDK 5.0 之前的“枚举”是一种**开发者约定**：你用 `private` 构造器和 `public static final`
实例告诉外界“这是一组固定常量”，但编译器并不强制，反射和序列化都可能绕过这些约定。JDK 5.0 的 `enum` 把同样的思想*
*下沉到了语言和编译器层面**：构造器私有由语法强制，实例列表由编译器生成，`values`/`valueOf` 自动注入，序列化行为由 `Enum`
基类专门保障。你牺牲了继承其他类的能力，换来的是类型安全、实例封闭性和一系列内建操作的可靠性。
