# Java 中 `this(...)` 调用本类构造器详解

## 一、基本语法与作用

* this()：调用本类的无参构造器
* this(实参列表)：调用本类的有参构造器

`this(参数列表)` 用于**在一个构造器中调用本类的另一个重载构造器**，目的是复用构造逻辑，避免重复代码。

```java
public class Person {
    private String name;
    private int age;
    private String address;

    // 构造器 1：无参，委托给 3 参构造器
    public Person() {
        this("未知", 0, "未填写");
    }

    // 构造器 2：1 参，委托给 2 参构造器
    public Person(String name) {
        this(name, 0);
    }

    // 构造器 3：2 参，委托给 3 参构造器
    public Person(String name, int age) {
        this(name, age, "未填写");
    }

    // 构造器 4：最终执行实际赋值，构造器链的“终点”
    public Person(String name, int age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }
}
```

调用 `new Person("张三")` 时，实际执行链为：

```text
Person(String) → this(name, 0) → Person(String,int) → this(name, age, "未填写")
→ Person(String,int,String) → super() → 赋值 → 逐层返回
```

**只有最顶层的构造器会调用 `super()`**，链上其他构造器不会再插入 `super()`。

---

## 二、典型使用场景

### 1. 统一默认值（模拟“默认参数”）

```java
public class Config {
    private final String host;
    private final int port;
    private final boolean ssl;

    public Config() {
        this("localhost", 8080, false);   // 默认配置
    }

    public Config(String host) {
        this(host, 8080, false);
    }

    public Config(String host, int port) {
        this(host, port, false);
    }

    public Config(String host, int port, boolean ssl) {
        this.host = host;
        this.port = port;
        this.ssl  = ssl;
    }
}
```

### 2. 参数校验集中到一处

```java
public class Range {
    private final int min;
    private final int max;

    public Range() {
        this(0, 100);
    }

    public Range(int max) {
        this(0, max);
    }

    public Range(int min, int max) {
        if (min > max) {                       // 校验只写一次
            throw new IllegalArgumentException("min > max");
        }
        this.min = min;
        this.max = max;
    }
}
```

### 3. 配合 `final` 字段

`final` 字段必须在构造器结束前赋值。通过 `this(...)` 委托到“终点构造器”赋值，既满足 `final` 语义，又避免重复赋值。

```java
public class ImmutablePoint {
    private final int x, y;

    public ImmutablePoint() {
        this(0, 0);            // 委托给终点构造器赋值
    }
    public ImmutablePoint(int v) {
        this(v, v);
    }
    public ImmutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
```

### 4. 泛型构造器委托（冷门）

```java
public class Box<T> {
    private final T value;

    public <U extends T> Box(U value) {   // 泛型构造器
        this.value = value;
    }

    public Box() {
        this((T) null);      // 委托到泛型构造器，需强制转型以匹配
    }
}
```

---

## 三、注意事项（重点）

### 1. `this(...)` 必须是构造器体中的**第一条语句**

推论：在类的一个构造器中，最多只能声明一个 "this(参数列表)" 或 "this()"

```java
public Person(String name, int age) {
    System.out.println("init");   // ❌ 编译错误：constructor call must be the first statement
    this(name, 0);
}
```

也正因为如此，**不能把 `this(...)` 放进 `try`、`if` 等语句中**：

```java
public Person() {
    try {
        this("x");               // ❌ 编译错误：构造器调用不是第一条语句
    } catch (Exception e) { }
}
```

> 推论：想对 `this(...)` 的调用做异常处理是不可能的，参数校验应在被委托的构造器内部完成。

### 2. 与 `super(...)` 互斥

`this(...)` 和 `super(...)` 都要求位于第一条语句，所以**同一个构造器中二者只能出现一个**：

```java
public Person(String name) {
    super();
    this(name, 0);      // ❌ 编译错误
}
```

### 3. 不能直接递归调用自己

推论：如果一个类中声明了 n 个构造器，则最多有 n - 1 个构造器中使用了"this(形参列表)"

```java
public Person() {
    this();             // ❌ 编译错误：recursive constructor invocation
}
```

### 4. 不能形成间接循环

```java
public Person()        { this("a"); }
public Person(String s){ this();    }   // ❌ 编译错误：recursive constructor invocation
```

编译器会检测构造器调用环并直接报错。

### 5. 只能出现在构造器中，不能出现在普通方法、静态块中

```java
public void foo() {
    this("x");          // ❌ 编译错误：call to this must be first statement in constructor
}

static {
    this();             // ❌ 编译错误：不能在静态上下文中使用
}
```

注意区分 **`this(...)`（构造器委托）** 与 **`this.`（引用当前对象成员）**。

### 6. 必须存在参数匹配的构造器

```java
public Person() {
    this(1, 2, 3, 4);   // ❌ 编译错误：找不到匹配的构造器
}
```

重载解析遵循 Java 普通方法规则（自动类型提升、装箱、可变参数等）：

```java
public Person(long v) { }
public Person() {
    this(1);            // ✅ int 自动提升为 long
}

public Person(String... args) { }
public Person() {
    this();             // ✅ 可变参数可传 0 个
    // this((String[]) null); 也可以
}
```

### 7. 构造器链最终必须能到达 `super()`

只要链的终点构造器没有显式写 `super(...)`，编译器就会插入隐式 `super()`。因此要保证**父类存在可访问的无参构造器**，否则编译失败：

```java
class Parent {
    Parent(int x) { }        // 没有无参构造器
}
class Child extends Parent {
    Child() {
        this(10);            // 最终会走到下面这个构造器
    }
    Child(int x) { }         // ❌ 编译错误：隐式 super() 找不到 Parent()
    // 正确写法：Child(int x) { super(x); }
}
```

### 8. `this(...)` 的参数中引用实例字段，拿到的是**默认值**

构造器链中，字段初始化器（`private int x = 5;`）和实例初始化块要在 `super()` 返回后才执行。若在 `this(...)`
的参数里使用字段，此时字段还是默认值：

```java
public class Bad {
    private int x = 5;

    public Bad() {
        this(x);              // ⚠️ 此时 x 是 0，不是 5
    }
    public Bad(int x) {
        this.x = x;
    }

    public static void main(String[] args) {
        System.out.println(new Bad().x);   // 输出 0
    }
}
```

这是常见的隐蔽 bug，建议构造器委托的参数使用**字面量或参数**，不要用尚未初始化的实例字段。

### 9. 初始化块与字段初始化器在 `this()` 链中只执行一次

```java
public class InitOrder {
    { System.out.println("实例初始化块"); }
    private int x = initX();

    public InitOrder() {
        this(10);
        System.out.println("无参构造器体");
    }
    public InitOrder(int x) {
        System.out.println("有参构造器体，x=" + x);
        this.x = x;
    }
    private int initX() {
        System.out.println("字段初始化器");
        return 1;
    }

    public static void main(String[] args) {
        new InitOrder();
    }
}
```

输出：

```text
实例初始化块
字段初始化器
有参构造器体，x=10
无参构造器体
```

说明：委托链中只有**调用 `super()` 的那个构造器**会插入实例初始化代码，返回后逐层执行各构造器体剩余部分。

### 10. `private` 构造器同样可以被 `this(...)` 调用

```java
public class Singleton {
    private Singleton() { }
    public Singleton(String tag) {
        this();               // ✅ 同类中可调用 private 构造器
    }
}
```

### 11. 构造器抛异常时，构造失败

`this(...)` 调用目标构造器若抛异常，整个对象构造失败，异常向上传播，**不会继续执行当前构造器体**。

```java
public Person() {
    this(-1);             // 若目标构造器抛 IllegalArgumentException
    System.out.println("永远不会执行");
}
```

### 12. `record` 的限制（Java 16+）

`record` 的**规范构造器（canonical constructor）不能包含 `this(...)` 或 `super(...)`**；非规范构造器必须委托给规范构造器：

```java
public record Point(int x, int y) {
    public Point() {
        this(0, 0);       // ✅ 非规范构造器委托
    }
    // 紧凑构造器（规范构造器）内不能写 this(...)
}
```

### 13. 匿名内部类没有显式构造器

匿名内部类无法声明构造器，因此**不能使用 `this(...)`**。它的初始化逻辑应放在实例初始化块中。

---

## 四、使用建议

| 建议                            | 说明                        |
|-------------------------------|---------------------------|
| 采用**“参数少的构造器委托给参数多的构造器”**     | 终点构造器集中赋值，职责单一            |
| 把参数校验、`final` 字段赋值放在**终点构造器** | 避免重复，且保证 `final` 语义       |
| 委托参数避免引用**尚未初始化的实例字段**        | 用字面量或入参                   |
| 不要在构造器里做耗时/可失败的重逻辑            | 因为无法对 `this(...)` 做异常处理   |
| 链的终点要保证父类可构造                  | 显式 `super(...)` 或父类有无参构造器 |

---

## 总结

- `this(...)` 是构造器**复用**工具，必须位于构造器**第一条语句**。
- 它与 `super(...)` 互斥，禁止递归/循环调用，也不能出现在普通方法或静态上下文中。
- 委托链中**只有终点构造器调用 `super()`**，实例初始化块与字段初始化器也只在终点构造器执行一次。
- 最大的坑是**在 `this(...)` 参数中使用尚未初始化的实例字段**，以及**无法对被委托构造器的异常做捕获处理**。
