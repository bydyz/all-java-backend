# Java 中 `super` 的使用方式与注意事项详解

`super` 是 Java 中指向**直接父类**的特殊引用，只有三种合法用法：

1. `super.成员变量` —— 访问被隐藏的父类字段
2. `super.方法名(...)` —— 调用父类方法（非虚调用）
3. `super(...)` / `super(参数...)` —— 调用父类构造器

下面逐一配代码说明，再列出易踩的坑。

---

## 一、`super.字段`：访问被隐藏的父类成员变量

```java
class Father {
    String name = "Father";
    int age = 50;
}

class Son extends Father {
    String name = "Son";   // 隐藏(hide)了父类的 name

    void print() {
        System.out.println(name);        // Son
        System.out.println(this.name);   // Son
        System.out.println(super.name);  // Father  ← 必须用 super 才能拿到
        System.out.println(super.age);   // 50      ← 没被隐藏，super. 可省略
    }
}
```

⚠️ 注意 `super.name` 是**编译期静态绑定**：它按「当前类的直接父类」去查找字段，不受多态影响（下面 N6 会展开）。

---

## 二、`super.方法()`：调用父类方法实现

最常见的场景是在**重写方法中复用父类逻辑**：

```java
class Animal {
    public void speak() {
        System.out.println("Animal 发出声音");
    }
    protected void breathe() {
        System.out.println("呼吸");
    }
}

class Dog extends Animal {
    @Override
    public void speak() {
        super.speak();                    // 先执行父类逻辑
        System.out.println("汪汪汪");
    }

    void demo() {
        super.breathe();                  // 调用父类 protected 方法（同包/子类可见）
        // super.toString();              // 也能调 Object 的方法
    }
}
```

关键点：`super.speak()` 是**非虚调用（静态绑定）**，直接跳到 `Animal.speak()` 的字节码，绝不会再回头调用 `Dog.speak()`，因此*
*不会死递归**。

```java
new Dog().speak();
// 输出：
// Animal 发出声音
// 汪汪汪
```

---

## 三、`super(...)`：调用父类构造器

```java
class Person {
    private final String name;
    private final int age;

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    Person() {
        this("无名", 0);
    }
}

class Student extends Person {
    private final String school;

    Student(String name, int age, String school) {
        super(name, age);        // ① 必须是构造器第一句
        this.school = school;    // ② 父类初始化完成后才能初始化自己
    }

    Student() {
        super();                 // 显式调用父类无参构造器
        this.school = "未知";
    }
}
```

### 隐式 `super()`

如果子类构造器里**没有**写 `super(...)` 也没有写 `this(...)`，编译器会自动插入 `super();`：

```java
class A {
    A() { System.out.println("A()"); }
}

class B extends A {
    B() {
        // 编译器自动插入 super();
        System.out.println("B()");
    }
}
```

⚠️ 如果父类**没有无参构造器**，而你又不显式写 `super(参数)`，就会编译失败：

```java
class P {
    P(int x) { }
}

class C extends P {
    // C() { }   // ❌ 编译错误：P 中没有无参构造器
    C() {
        super(10); // ✅ 必须显式传参
    }
}
```

### 构造器链的执行顺序

```java
class Grand { Grand() { System.out.println("Grand"); } }
class Parent extends Grand { Parent() { super(); System.out.println("Parent"); } }
class Child extends Parent { Child() { super(); System.out.println("Child"); } }

new Child();
// Grand
// Parent
// Child
```

---

## 四、注意事项（踩坑清单）

### N1. `super(...)` 必须是构造器的第一条语句

```java
class C extends P {
    C() {
        int x = 1;      // ❌ 编译错误
        super(10);
    }
}
```

> 补充：JDK 25 起（JEP 513 Flexible Constructor Bodies）允许在 `super(...)` 之前写**不引用 this 的语句**，如参数校验；但在
> JDK 25 之前的标准语法里必须严格第一句。

```java
// JDK 25+ 合法写法
class C extends P {
    C(int x) {
        if (x < 0) throw new IllegalArgumentException();  // 允许
        super(x);
    }
}
```

### N2. `this(...)` 与 `super(...)` 不能同时出现

```java
class C extends P {
    C() {
        super(1);
        this(2);      // ❌ 编译错误：构造器委托只能有一个
    }
    C(int x) { super(x); }
}
```

### N3. 静态上下文中不能用 `super`

`super` 依赖实例 `this`，所以：

```java
class C extends P {
    static void f() {
        super.toString();   // ❌ 编译错误：non-static variable super cannot be referenced
    }
}
```

### N4. 父类的 `private` 成员无法通过 `super` 访问

```java
class P {
    private void secret() { }
    private int hidden = 1;
}

class C extends P {
    void f() {
        // super.secret();   // ❌ private 不可见
        // System.out.println(super.hidden);  // ❌ 同上
    }
}
```

同理，**父类构造器也不能被 `super.构造器名()` 直接调用**，只能用 `super(...)` 语法。

### N5. `super` 只能指向**直接父类**，不存在 `super.super`

```java
class C extends B {
    void f() {
        // super.super.toString();  // ❌ 语法错误
    }
}
```

想跨越直接父类，只能通过父类暴露的方法间接实现。

super 本质是"从直接父类开始做成员解析（member resolution）"的语法入口。解析规则是：

* 先在直接父类里找
* 遇到第一个匹配就停，就不会跳过
* 找不到就继续找父类的父类
* 一直找到 Object 为止

### N6. 字段是静态绑定，方法是动态绑定

这是最容易出错的点：

```java
class A {
    int x = 1;
    void show() { System.out.println("A.show, x=" + x); }
}

class B extends A {
    int x = 2;                    // 隐藏父类字段
    @Override
    void show() { System.out.println("B.show, x=" + x); }
}

public class Test {
    public static void main(String[] args) {
        A a = new B();
        System.out.println(a.x);   // 1  ← 字段看「引用类型」A
        a.show();                  // B.show, x=2  ← 方法看「实际对象」B

        B b = new B();
        System.out.println(((A) b).x);  // 1，字段依然按引用类型
    }
}
```

结论：**重写(override)只针对方法；字段只会被隐藏(hide)**，`super.x` 拿的是直接父类那份字段。

### N7. 构造器中调用可重写方法 → 子类字段还未初始化

```java
class Parent {
    Parent() {
        init();     // ⚠️ 隐式 super() 时会被调用，此时子类字段还是默认值！
    }
    void init() { }
}

class Child extends Parent {
    int value = 42;

    @Override
    void init() {
        System.out.println("value = " + value);  // 输出 0，不是 42
    }
}

new Child();   // value = 0
```

原因：`super()` 执行时机早于子类字段初始化与构造体。**父类构造器里不要调用可被重写的方法**（同理，用 `super.xxx()`
调用父类构造逻辑时也要注意）。

### N8. 内部类 / 匿名类 / Lambda 中的 `super` 指向谁

```java
class Base {
    void hello() { System.out.println("Base.hello"); }
}

class Outer extends Base {
    void test() {
        // ① 匿名内部类：super 指匿名类的父类(Object)
        new Object() {
            void f() {
                super.toString();      // Object.toString()
                Outer.super.hello();   // 要用「外部类.super」才能到 Base
            }
        };

        // ② Lambda：super 是词法作用域，仍指 Outer 的父类
        Runnable r = () -> super.hello();   // 输出 Base.hello
        r.run();
    }
}
```

### N9. 多接口默认方法冲突 → 用 `接口名.super.方法()`

```java
interface A { default void hello() { System.out.println("A"); } }
interface B { default void hello() { System.out.println("B"); } }

class C implements A, B {
    @Override
    public void hello() {
        A.super.hello();   // 显式选择，不能写 super.hello()
        B.super.hello();
    }
}
```

注意这里 `A.super` 只对**接口默认方法**有效，不能用于类。

### N10. `super` 不是对象引用，不能传递/赋值

```java
Object o = super;      // ❌ 编译错误
return super;          // ❌ 编译错误
super == null;         // ❌ 编译错误
```

`super` 只是一个语法关键字，无法取到"父类对象"本身。

### N11. 通过 `super` 访问静态成员：能编译，但不推荐

```java
class P {
    static int COUNT = 10;
    static void util() { }
}

class C extends P {
    void f() {
        super.util();          // ✅ 编译通过（IDE 会警告）
        System.out.println(super.COUNT);  // ✅ 能编译
        // 推荐写法：
        P.util();
        System.out.println(P.COUNT);
    }
}
```

静态成员属于类而非实例，`super.` 没有语义价值，直接写 `父类名.` 更清晰。

### N12. 枚举中不能显式调用 `super(...)`

```java
enum Color {
    RED;   // ❌ 枚举构造器不能写 super(...)，隐式继承 java.lang.Enum
    Color() {
        // super();   // 编译错误
    }
}
```

---

## 五、完整可运行示例（方法重写 + 构造器链 + 字段隐藏）

```java
class Shape {
    protected String name = "Shape";
    protected int sides = 0;

    Shape() {
        System.out.println("Shape() 构造器");
    }

    Shape(String name, int sides) {
        this.name = name;
        this.sides = sides;
        System.out.println("Shape(" + name + ", " + sides + ") 构造器");
    }

    public String describe() {
        return name + " 有 " + sides + " 条边";
    }
}

class Triangle extends Shape {
    protected String name = "Triangle";   // 隐藏父类字段

    Triangle() {
        super("Triangle", 3);             // 调用父类带参构造器
        System.out.println("Triangle() 构造器");
    }

    @Override
    public String describe() {
        // 复用父类实现 + 扩展
        return "[子类] " + super.describe()
                + " | 父类name=" + super.name
                + ", 子类name=" + this.name;
    }
}

public class Demo {
    public static void main(String[] args) {
        Shape s = new Triangle();
        System.out.println(s.describe());
    }
}
```

输出：

```
Shape(Triangle, 3) 构造器
Triangle() 构造器
[子类] Triangle 有 3 条边 | 父类name=Triangle, 子类name=Triangle
```

---

## 六、速查表

| 场景           | 正确写法                | 说明                             |
|--------------|---------------------|--------------------------------|
| 构造器内首行       | `super(args);`      | 必须第一句（JDK 25+ 前）；与 `this()` 互斥 |
| 复用父类方法       | `super.method();`   | 非虚调用，不会死递归                     |
| 父类被隐藏字段      | `super.field`       | 只取直接父类那份，静态绑定                  |
| 静态方法中        | ❌ 不可用               | `super` 依赖实例                   |
| private 成员   | ❌ 不可访问              | 用 protected 或提供 getter         |
| 多接口默认方法      | `A.super.m();`      | 显式指定接口                         |
| 匿名内部类访问外部父类  | `Outer.super.m();`  | 直接 `super` 指内部类父类              |
| 跨越多层父类       | ❌ `super.super` 不存在 | 通过中间父类暴露方法                     |
| 父类构造器里调可重写方法 | ⚠️ 危险               | 子类字段尚未初始化                      |

掌握这些，`super` 的边界基本就覆盖全了。核心记忆点：**方法重写用 super 复用，字段隐藏用 super 穿透，构造器靠 super
串联，其余边界由「直接父类 + 实例上下文」两条铁律决定**。
