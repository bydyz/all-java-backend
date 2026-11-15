# Java 中 final 修饰的内容、作用与效果详解

下面按“final 能修饰什么”逐一说明，并给出代码。先记住一句话：**final 的核心是“只能赋值一次 / 不能被继承 /
不能被重写”，但不等于对象内容完全不可变，也不自动等于线程安全。**

## 1. final 总览

| 修饰目标            | 作用                     | 效果与注意点                               |
|-----------------|------------------------|--------------------------------------|
| final 类         | 类不能被继承                 | 不能与 abstract 同时使用；类中方法自然不能被覆盖        |
| final 方法        | 方法不能被子类重写              | 可以重载；不能与 abstract 同时使用               |
| final 成员变量/字段   | 字段只能初始化一次              | 可在声明处、实例初始化块、构造器中初始化；引用不可重新赋值        |
| static final 字段 | 静态常量                   | 若为基本类型/String 常量表达式，可能是“编译期常量”，有内联风险 |
| final 局部变量      | 局部变量只能赋值一次             | 可先声明后赋值；常用于 lambda/匿名内部类捕获           |
| final 方法参数      | 参数在方法内不能重新赋值           | 可被 lambda/匿名内部类捕获                    |
| final 引用变量      | 引用不能指向别的对象             | 对象内部状态仍可改变，如 final List 仍可 add       |
| 接口字段            | 隐式 public static final | 接口中的字段天然是常量                          |
| 枚举常量            | 隐式 public static final | 枚举类型不能被继承                            |
| record 类        | 隐式 final               | record 不能被继承                         |
| sealed 子类       | 可声明为 final             | 表示该子类不再允许被继承                         |

---

## 2. final 修饰类：类不能被继承；例如 String 类、System 类、StringBuffer 类

```java
final class FinalClass {
    void hello() {
        System.out.println("hello");
    }
}

// 编译错误：Cannot inherit from final 'FinalClass'
// class SubClass extends FinalClass {
// }
```

效果：

- `FinalClass` 不能被任何类继承。
- 因为不能被继承，所以它的方法也不可能被子类覆盖。
- `final` 类不能同时声明为 `abstract`，两者矛盾。

---

## 3. final 修饰方法：方法不能被重写

```java
class Parent {
    final void f() {
        System.out.println("Parent.f");
    }

    void g() {
        System.out.println("Parent.g");
    }
}

class Child extends Parent {
    // 编译错误：'f()' cannot override 'f()' in 'Parent'; overridden method is final
    // @Override
    // void f() {
    // }

    // 合法：这是重载，不是重写
    void f(int x) {
        System.out.println("Child.f(int)");
    }

    // 合法：g 不是 final，可以重写
    @Override
    void g() {
        System.out.println("Child.g");
    }
}
```

效果：

- `final` 方法可以被子类继承，但不能被子类重写。
- `final` 方法可以被重载。
- `final` 方法不能是 `abstract`。
- `private` 方法不能被重写，子类写同名方法只是“新方法”，不是重写：

```java
class P {
    private void m() {
        System.out.println("P.m");
    }
}

class Q extends P {
    // 这不是重写，只是 Q 自己新定义的方法
    void m() {
        System.out.println("Q.m");
    }
}
```

---

## 4. final 修饰成员变量：字段只能初始化一次

```java
import java.util.ArrayList;
import java.util.List;

class Person {
    // 空白 final：声明时不赋值，必须在构造器或实例初始化块中赋值
    final String name;

    // 声明时初始化
    final int age = 18;

    // 静态常量
    static final String SPECIES = "Human";

    // final 引用：引用不能变，但对象内容可改
    final List<String> tags = new ArrayList<>();

    Person(String name) {
        this.name = name;
    }

    void test() {
        // 编译错误：Cannot assign a value to final variable 'name'
        // name = "李四";

        // 合法：修改的是 List 对象内部内容，不是修改 tags 引用
        tags.add("java");

        // 编译错误：Cannot assign a value to final variable 'tags'
        // tags = new ArrayList<>();
    }
}
```

关键点：

- `final` 字段必须初始化一次。
- 实例 `final` 字段可以在：
  1. 声明时初始化；
  2. 实例初始化块中初始化；
  3. 构造器中初始化。
- 静态 `final` 字段可以在：
  1. 声明时初始化；
  2. 静态初始化块中初始化。
- `final` 字段不能在普通方法中再次赋值。

示例：

```java
class InitDemo {
    final int a = 1;       // 声明时初始化
    final int b;           // 空白 final
    final int c;

    static final int S1 = 10;
    static final int S2;

    {
        b = 2;             // 实例初始化块
    }

    static {
        S2 = 20;           // 静态初始化块
    }

    InitDemo() {
        c = 3;
    }

    InitDemo(int x) {
        c = x;
    }

    // 编译错误：不能在普通方法中给 final 字段赋值
    // void change() {
    //     c = 4;
    // }
}
```

---

## 5. final 引用：引用不可变，但对象内容可变

这是最常见的误区。

```java
import java.util.ArrayList;
import java.util.List;

public class FinalReferenceDemo {
    public static void main(String[] args) {
        final List<String> list = new ArrayList<>();

        // 合法：修改对象内容
        list.add("A");
        list.add("B");

        // 编译错误：不能重新赋值引用
        // list = new ArrayList<>();

        final int[] arr = {1, 2, 3};

        // 合法：修改数组元素
        arr[0] = 100;

        // 编译错误：不能重新赋值数组引用
        // arr = new int[]{4, 5, 6};
    }
}
```

效果：

- `final` 只保证变量不能再指向另一个对象。
- 不保证对象内部状态不变。
- 如果要实现不可变对象，需要：`final` 字段 + 不可变类型 + 防御性拷贝 + 不暴露可变内部引用。

```java
import java.util.List;

final class ImmutablePerson {
    private final String name;
    private final List<String> tags;

    ImmutablePerson(String name, List<String> tags) {
        this.name = name;
        // 防御性拷贝，避免外部修改影响内部状态
        this.tags = List.copyOf(tags);
    }

    public String name() {
        return name;
    }

    public List<String> tags() {
        return tags;
    }
}
```

---

## 6. final 修饰局部变量：只能赋值一次

```java
public class FinalLocalDemo {
    public static void main(String[] args) {
        final int x = 10;
        // 编译错误：Cannot assign a value to final variable 'x'
        // x = 20;

        final int y;
        y = 30;
        // 编译错误：Variable 'y' might already have been assigned
        // y = 40;

        System.out.println(x + y);
    }
}
```

特点：

- `final` 局部变量可以声明时赋值，也可以先声明后赋值一次。
- 赋值后不能再改。
- 常用于 lambda 或匿名内部类捕获。

---

## 7. final 修饰方法参数：方法内不能重新赋值

```java
public class FinalParamDemo {
    void print(final String s) {
        // 编译错误：Cannot assign a value to final variable 's'
        // s = "other";

        Runnable r = () -> System.out.println(s);
        r.run();
    }

    void normal(int x) {
        x = 100; // 非 final 参数可以重新赋值
        System.out.println(x);
    }
}
```

效果：

- `final` 参数在方法内部不能重新赋值。
- 参数本身由调用者传入，方法内只能使用。
- 可被 lambda/匿名内部类捕获。

---

## 8. final 与 lambda / 匿名内部类捕获

Java 8 之后，lambda 和匿名内部类捕获的局部变量必须是 `final` 或 “effectively final”，即事实上的 final。

```java
public class LambdaFinalDemo {
    public static void main(String[] args) {
        int count = 1; // 没有修改，属于 effectively final
        Runnable r1 = () -> System.out.println(count);
        r1.run();

        final int age = 18; // 显式 final
        Runnable r2 = () -> System.out.println(age);
        r2.run();

        int number = 10;
        // 如果取消下面注释，number 不再是 effectively final，
        // 上面的 lambda 如果捕获 number 就会编译失败
        // number = 20;
    }

    void test(final String msg) {
        Runnable r = () -> System.out.println(msg);
        r.run();
    }
}
```

注意：

- 显式写 `final` 不是必须的。
- 只要变量在初始化后没有再被赋值，就是 effectively final。
- 一旦被重新赋值，就不能被 lambda 捕获。

---

## 9. static final 与编译期常量：注意内联风险

```java
class Constants {
    public static final int MAX = 100;
    public static final String NAME = "Java";

    // 不是编译期常量，因为类型是 Integer，不是基本类型或 String
    public static final Integer BOXED = 100;

    // 不是常量表达式
    public static final int RANDOM = Integer.parseInt("123");
}

class UseConstants {
    int a = Constants.MAX;      // 编译时可能直接内联为 100
    String b = Constants.NAME;  // 编译时可能直接内联为 "Java"
}
```

说明：

- `static final` 修饰的基本类型或 `String`，如果使用常量表达式初始化，属于编译期常量。
- 使用处可能在编译时被内联。
- 如果只修改 `Constants.MAX` 并只重新编译 `Constants`，而没有重新编译 `UseConstants`，`UseConstants` 中可能仍然是旧值。
- 所以发布时通常需要整体重新编译依赖代码。

---

## 10. final 与多线程：final 字段有特殊初始化安全语义

Java 内存模型对 `final`
字段有特殊保证：正确构造并安全发布对象后，其他线程看到该对象引用时，至少能保证看到构造器中为 `final` 字段写入的值。

```java
class FinalSemantics {
    final int x;
    int y;

    FinalSemantics() {
        x = 1;
        y = 2;
    }
}

class Publisher {
    volatile FinalSemantics ref;

    void publish() {
        ref = new FinalSemantics();
    }

    void consume() {
        FinalSemantics o = ref;
        if (o != null) {
            int a = o.x; // final 字段：正确发布后保证看到 1
            int b = o.y; // 这里由 volatile 发布保证可见；若数据竞争则不保证
        }
    }
}
```

注意：

- `final` 字段的初始化安全语义，不保证 `final` 引用对象内部状态不可变。
- 如果构造器中 `this` 逸出，可能破坏这种保证。
- `final` 不能替代 `volatile`、锁、线程安全集合等并发控制。
- `final` 与 `volatile` 不能同时修饰同一个字段。

---

## 11. final 与接口、枚举、record、sealed

```java
interface Config {
    // 接口字段隐式 public static final
    int MAX = 100;
}

enum Color {
    RED, GREEN;
    // 枚举常量隐式 public static final
}

record Point(int x, int y) {
    // record 类隐式 final
}

sealed interface Shape permits Circle, Square {
}

final class Circle implements Shape {
}

non-sealed class Square implements Shape {
}
```

说明：

- 接口字段天然是 `public static final`。
- 枚举常量天然是 `public static final`。
- `record` 类隐式 `final`，不能被继承。
- Java 17 的 `sealed` 中，允许的子类可以声明为 `final`、`sealed` 或 `non-sealed`。

---

## 12. final 与反射：不推荐依赖反射修改 final

```java
import java.lang.reflect.Field;

class ReflectFinal {
    final int x = 1;
}

public class ReflectFinalDemo {
    public static void main(String[] args) throws Exception {
        ReflectFinal obj = new ReflectFinal();

        Field f = ReflectFinal.class.getDeclaredField("x");
        f.setAccessible(true);

        // 某些 JDK/模块配置下可以修改实例 final 字段，但非常不推荐
        // f.setInt(obj, 2);

        System.out.println(obj.x);
    }
}
```

注意：

- 反射修改 `final` 字段属于破坏封装，不推荐。
- 对于 `static final` 编译期常量，直接引用处可能已经被内联，反射修改后直接访问仍可能是旧值。
- 不同 JDK 版本、模块系统下行为限制不同，不应依赖。

---

## 13. 常见误区总结

1. **final 引用不等于对象不可变**
   `final List<String> list` 不能重新赋值，但可以 `list.add(...)`。

2. **final 字段不等于编译期常量**
   只有 `static final` 基本类型/String 且用常量表达式初始化，才可能是编译期常量。

3. **final 类中的方法不能被覆盖**
   因为类根本不能被继承。

4. **final 方法可以重载，但不能重写**。

5. **final 不能修饰构造器**
   构造器不能被继承，也谈不上重写。

6. **final 与 abstract 互斥**
   `final abstract class`、`final abstract method` 都不合法。

7. **final 与 volatile 不能同时修饰字段**。

8. **final 不保证线程安全**
   `final` 字段有初始化安全语义，但不能替代同步和安全发布。

9. **实现不可变对象需要组合手段**
   `final class` + `private final` 字段 + 不可变类型 + 防御性拷贝 + 不泄露 `this`。

一句话总结：**final 限制的是“赋值一次、继承、重写”这些行为；它提高了安全性、可读性和某些场景下的优化与内存可见性保证，但不要把它误解为“对象完全不可变”或“天然线程安全”。
**
