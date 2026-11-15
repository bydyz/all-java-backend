# Java 中 `static` 关键字详解

`static` 是 Java 中的修饰符，核心含义是 **"属于类，而不属于某个实例"**。凡是 `static` 修饰的内容，都在**类加载时**
初始化，被该类所有实例共享，可以直接通过 `类名.成员` 访问，无需创建对象。

---

## 一、`static` 可以修饰哪些内容？

| 修饰对象 | 说明                   | 引入版本     |
|------|----------------------|----------|
| 成员变量 | 类变量（静态变量），所有实例共享     | Java 1.0 |
| 成员方法 | 类方法（静态方法），无 `this`   | Java 1.0 |
| 代码块  | 静态代码块，类加载时执行一次       | Java 1.0 |
| 内部类  | 静态内部类，不依赖外部类实例       | Java 1.0 |
| 导入   | `import static` 静态导入 | Java 5   |
| 接口方法 | 接口中的静态方法             | Java 8   |

---

## 二、`static` 成员变量（类变量）

### 2.1 基本用法

```java
public class Counter {
    // 静态变量：所有实例共享一份
    public static int total = 0;
    // 实例变量：每个对象独有一份
    public int instanceId;

    public Counter() {
        total++;                 // 修改共享的静态变量
        this.instanceId = total; // 每个对象有自己独立的 id
    }

    public static void main(String[] args) {
        Counter c1 = new Counter();
        Counter c2 = new Counter();
        Counter c3 = new Counter();

        System.out.println("total = " + Counter.total); // 3
        System.out.println("c1.id = " + c1.instanceId); // 1
        System.out.println("c2.id = " + c2.instanceId); // 2
        System.out.println("c3.id = " + c3.instanceId); // 3

        // 静态变量也可以通过实例访问（不推荐，易产生误解）
        System.out.println(c1.total); // 3
    }
}
```

**效果：**

- `total` 在**类加载**时被初始化一次，存放在 **方法区（元空间）**，与实例无关。
- 任意实例修改 `total`，其他实例看到的都是同一个值。
- 通过 `c1.total` 访问实际等价于 `Counter.total`，编译器会给出警告，语义上不推荐。

### 2.2 静态变量与实例变量的存储区别

```java
public class Person {
    static String species = "Homo sapiens"; // 方法区，一份
    String name;                            // 堆中每个对象一份
    int age;                                // 堆中每个对象一份
}
```

```
方法区(Metaspace)             堆(Heap)
+-----------------+         +-----------------------+
| species = "..." |<--------| Person1{name,age}     |
|                 |<--------| Person2{name,age}     |
+-----------------+         +-----------------------+
```

---

## 三、`static` 成员方法（类方法）

### 3.1 基本用法与限制

```java
public class MathUtil {

    private static final double PI = 3.1415926;

    // 静态方法：工具方法常用，不需要对象状态
    public static double circleArea(double r) {
        return PI * r * r;
    }

    // 静态方法不能直接访问非静态成员
    private int count = 0;

    // public static void bad() {
    //     count++;          // ❌ 编译错误：无法从静态上下文引用非静态变量
    //     this.count++;     // ❌ 编译错误：静态方法没有 this
    // }
}

// 直接通过类名调用，无需 new
double area = MathUtil.circleArea(2.0);
```

### 3.2 静态方法不能被重写，只能被隐藏

```java
class Parent {
    public static void hello() { System.out.println("Parent.hello"); }
    public        void hi()    { System.out.println("Parent.hi");    }
}

class Child extends Parent {
    // @Override 会报错，因为静态方法不能被重写
    public static void hello() { System.out.println("Child.hello"); } // ✅ 隐藏
    public        void hi()    { System.out.println("Child.hi");    } // ✅ 重写
}

public class Demo {
    public static void main(String[] args) {
        Parent p = new Child();

        p.hello();   // Parent.hello  —— 静态方法看“声明类型”
        p.hi();      // Child.hi      —— 实例方法看“运行时类型”

        Child.hello(); // Child.hello
    }
}
```

> **要点**：静态方法的调用分派在**编译期**按引用变量的声明类型确定；实例方法在**运行期**按实际对象类型确定（多态）。

### 3.3 `main` 方法必须是 `public static void`

JVM 调用 `main` 时**还没有创建任何对象**，所以必须通过类名调用 → 必须 `static`。

```java
public class MainDemo {
    public static void main(String[] args) {
        // JVM 直接调用 MainDemo.main(args)，无需 new MainDemo()
    }
}
```

---

## 四、`static` 代码块

### 4.1 类加载时执行一次

```java
public class Config {
    public static String url;
    public static final int TIMEOUT;

    // 静态代码块：类加载时执行，且只执行一次，按书写顺序执行
    static {
        System.out.println("静态代码块执行");
        url = System.getProperty("app.url", "http://localhost");
        TIMEOUT = 5000;
    }

    static {
        System.out.println("第二个静态代码块");
    }

    public Config() {
        System.out.println("构造方法执行");
    }

    public static void main(String[] args) {
        System.out.println("main 开始");
        new Config();
        new Config();
    }
}
```

**输出顺序：**

```
静态代码块执行
第二个静态代码块
main 开始
构造方法执行
构造方法执行
```

### 4.2 用途

- 初始化静态变量（尤其是需要复杂逻辑的）
- 加载配置文件、加载驱动（如 `Class.forName("com.mysql.cj.jdbc.Driver")`）
- 初始化某些 `final` 常量

### 4.3 顺序陷阱

```java
public class OrderDemo {
    static int a = 10;

    static {
        a = 20;
        b = 30;      // ✅ 可以赋值，因为 b 已声明
    }

    static int b = 40; // 之后又把 b 赋成 40，覆盖了 30

    public static void main(String[] args) {
        System.out.println(a); // 20
        System.out.println(b); // 40
    }
}
```

---

## 五、`static` 内部类（静态嵌套类）

### 5.1 与普通内部类的区别

```java
public class Outer {
    private static int staticField = 100;
    private        int instanceField = 200;

    // 静态内部类：不持有外部类实例的引用
    public static class StaticNested {
        void show() {
            System.out.println(staticField);        // ✅ 可以访问外部静态成员
            // System.out.println(instanceField);   // ❌ 无法访问实例成员
        }
    }

    // 非静态内部类：持有外部类实例的引用
    public class Inner {
        void show() {
            System.out.println(staticField);        // ✅
            System.out.println(instanceField);      // ✅
        }
    }

    public static void main(String[] args) {
        // 静态内部类：直接 new，不需要外部类对象
        Outer.StaticNested n = new Outer.StaticNested();
        n.show();

        // 非静态内部类：必须先有外部类对象
        Outer outer = new Outer();
        Outer.Inner i = outer.new Inner();
        i.show();
    }
}
```

**效果对比：**

| 特性       | 静态内部类 `static class`     | 普通内部类 `class`       |
|----------|--------------------------|---------------------|
| 是否持有外部引用 | 否                        | 是                   |
| 创建方式     | `new Outer.Nested()`     | `outer.new Inner()` |
| 访问外部实例成员 | ❌ 不可以                    | ✅ 可以                |
| 是否可能内存泄漏 | 否                        | 可能                  |
| 典型应用     | `Map.Entry`、`Builder` 模式 | 需要访问外部状态的场景         |

---

## 六、`static import` 静态导入（Java 5）

```java
// 普通方式
import java.lang.Math;
public class Demo1 {
    public static void main(String[] args) {
        System.out.println(Math.PI);
        System.out.println(Math.max(3, 5));
    }
}
```

```java
// 静态导入后：直接用方法名/字段名
import static java.lang.Math.PI;
import static java.lang.Math.*;

public class Demo2 {
    public static void main(String[] args) {
        System.out.println(PI);       // 省略 Math.
        System.out.println(max(3, 5)); // 省略 Math.
        System.out.println(abs(-7));
    }
}
```

**建议**：适度使用，否则代码可读性会下降（读者分不清 `max` 是哪个类的）。常见于 JUnit
测试：`import static org.junit.Assert.*;`

---

## 七、接口中的 `static` 方法（Java 8+）

```java
public interface Vehicle {
    // 抽象方法
    void run();

    // 静态方法：属于接口自身，只能通过接口名调用，不被实现类继承
    static Vehicle create() {
        return () -> System.out.println("默认车辆启动");
    }

    static int wheels() {
        return 4;
    }
}

class Car implements Vehicle {
    @Override
    public void run() { System.out.println("Car runs"); }
}

public class InterfaceDemo {
    public static void main(String[] args) {
        Vehicle v = Vehicle.create();
        v.run();

        System.out.println(Vehicle.wheels()); // ✅
        // System.out.println(Car.wheels()); // ❌ 实现类无法继承接口静态方法
    }
}
```

---

## 八、`static` 的核心作用与效果总结

### 8.1 作用

1. **属于类，不属于对象**：类加载即可用，无需 `new`。
2. **共享**：所有实例共享同一份数据（用于计数器、缓存、常量）。
3. **生命周期长**：从类加载开始到 JVM 退出（或类卸载），一直存在。
4. **内存占用优化**：工具类方法、公共常量不重复创建对象。

### 8.2 关键约束与陷阱

| 约束                       | 说明                                    |
|--------------------------|---------------------------------------|
| 静态方法不能有 `this` / `super` | 静态方法没有对象上下文                           |
| 静态方法不能直接访问实例成员           | 只能访问静态成员，或通过对象引用访问                    |
| 静态方法不能重写                 | 只能隐藏（`static` 不能变非 `static`，反之亦然）     |
| 静态方法中可以 `new` 实例         | 允许，只是不能直接引用当前实例成员                     |
| 静态变量线程不安全                | 多线程共享需注意，考虑 `volatile`/`synchronized` |
| 静态代码块按顺序执行一次             | 类初始化阶段执行                              |
| 静态成员在继承中可被隐藏             | 通过子类引用访问需注意分派规则                       |

### 8.3 什么时候用 `static`？

✅ **适合用 `static`：**

- 工具方法（`Math.max`、`Collections.sort`）
- 常量（`static final` 的 `PI`、`MAX_VALUE`）
- 全局共享状态（配置、计数器、单例的实例引用）
- 工厂方法（`Vehicle.create()`）
- 不依赖实例状态且通用的逻辑

❌ **不适合用 `static`：**

- 需要访问实例状态的方法
- 需要考虑线程安全且被多线程修改的共享数据（除非妥善加锁）
- 面向对象中表示"每个对象独立状态"的属性（如人的姓名、年龄）

### 8.4 一个完整的综合示例

```java
public class Employee {
    private static int nextId = 1;             // 共享，自增 id 生成器
    private static final String COMPANY = "Acme"; // 常量

    private final int id;
    private String name;

    static {                                    // 类加载时执行
        System.out.println("[Employee] 类初始化，公司=" + COMPANY);
    }

    public Employee(String name) {
        this.id = nextId++;
        this.name = name;
    }

    // 实例方法：可以访问静态成员
    public void print() {
        System.out.println(id + ": " + name + " @" + COMPANY);
    }

    // 静态方法：工厂 + 统计
    public static Employee create(String name) { return new Employee(name); }
    public static int totalCreated() { return nextId - 1; }

    // 静态内部类：Builder 模式
    public static class Builder {
        private String name;
        public Builder name(String n) { this.name = n; return this; }
        public Employee build() { return new Employee(name); }
    }

    public static void main(String[] args) {
        Employee e1 = Employee.create("Alice");
        Employee e2 = new Employee.Builder().name("Bob").build();
        e1.print();
        e2.print();
        System.out.println("总创建数: " + Employee.totalCreated()); // 2
    }
}
```

**输出：**

```
[Employee] 类初始化，公司=Acme
1: Alice @Acme
2: Bob @Acme
总创建数: 2
```

---

## 九、一句话总结

> `static` 让成员从"对象级别"上升到"类级别"——**随类加载而生，被所有实例共享，可直接用类名调用**
> ；代价是失去对象状态、不能访问实例成员、并且要注意共享带来的线程安全问题。
