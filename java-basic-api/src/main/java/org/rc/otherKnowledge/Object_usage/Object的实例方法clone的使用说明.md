# Object 类 clone() 详解

## 一、方法签名

```java
protected native Object clone() throws CloneNotSupportedException;
```

关键点：

| 特性                                  | 说明                                   |
|-------------------------------------|--------------------------------------|
| `native`                            | 底层由 JVM 用 C/C++ 实现，直接操作内存            |
| `protected`                         | 只能被同类、同包、或子类访问；所以外部调用需子类重写为 `public` |
| `throws CloneNotSupportedException` | 若类未实现 `Cloneable`，调用即抛此异常            |
| 返回值 `Object`                        | 需要强转，通常重写时改为具体类型（协变返回）               |

## 二、clone() 到底做了什么

`Object.clone()` 的**原生实现**流程：

1. 检查当前对象所属类是否实现 `Cloneable` 接口，没实现 → 抛 `CloneNotSupportedException`。
2. 按**当前对象的实际类型**在堆上分配一块新内存（**不调用构造器**）。
3. 将原对象的所有字段**逐字段按位复制**到新对象（包括基本类型值和引用地址）——即**浅拷贝**。
4. 返回新对象引用。

> ⚠️ 注意：clone 不执行任何构造器，也不执行初始化块。若构造器里做了注册、赋值等副作用，clone 出来的对象会"跳过"它们。

## 三、Cloneable 接口：一个"标记接口"

```java
public interface Cloneable { }  // 空接口
```

它本身没有任何方法。它的唯一作用是在 `Object.clone()` 中做 `instanceof` 检查，起**开关/许可**作用。不实现它就是不让 clone。

## 四、基础示例

### 示例 1：正确实现 clone

```java
public class Person implements Cloneable {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public Person clone() {                 // 返回具体类型，协变
        try {
            return (Person) super.clone();  // 调用 Object.clone() 分配+复制
        } catch (CloneNotSupportedException e) {
            // 因为已实现 Cloneable，理论上不会走到这里
            throw new AssertionError("不可能发生", e);
        }
    }

    public static void main(String[] args) {
        Person p1 = new Person("Tom", 20);
        Person p2 = p1.clone();

        System.out.println(p1 == p2);          // false  → 两个不同对象
        System.out.println(p1.name == p2.name); // true  → String 引用相同(浅拷贝)
        p2.age = 30;
        System.out.println(p1.age);            // 20    → 基本类型互不影响
    }
}
```

要点：

- `super.clone()` 是必须的，它才是真正分配新内存的地方。若自己 `new Person(...)` 就不是 clone 语义。
- 重写后改为 `public`，否则外部无法调用。

### 示例 2：不实现 Cloneable 会怎样

```java
class NoClone {
    // 没有 implements Cloneable
}

public class Test {
    public static void main(String[] args) {
        NoClone o = new NoClone();
        try {
            Object c = o.clone();   // 编译错误！clone() 是 protected，外部不可见
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
```

即便在子类中重写并调用 `super.clone()`：

```java
class NoClone2 {
    @Override
    public NoClone2 clone() {
        try {
            return (NoClone2) super.clone(); // 运行期抛异常
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("未实现 Cloneable", e);
        }
    }
}

// 运行 new NoClone2().clone() → CloneNotSupportedException
```

> 类没有实现 `Cloneable`，`Object.clone()` 里的检查会失败。

## 五、浅拷贝的坑

```java
public class Address {
    String city;
    public Address(String city) { this.city = city; }
}

public class Person implements Cloneable {
    String name;
    Address address;

    public Person(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    @Override
    public Person clone() {
        try { return (Person) super.clone(); }
        catch (CloneNotSupportedException e) { throw new AssertionError(e); }
    }

    public static void main(String[] args) {
        Person p1 = new Person("Tom", new Address("Beijing"));
        Person p2 = p1.clone();

        p2.address.city = "Shanghai";
        System.out.println(p1.address.city);  // Shanghai ← 原对象被"污染"了！
        System.out.println(p1.address == p2.address); // true → 引用共享
    }
}
```

原因：`Object.clone()` 只是把 `address` 这个**引用地址**复制过去，两个 Person 的 address 指向同一个 Address 对象。这叫**浅拷贝
**。

## 六、深拷贝实现

### 方式 A：手动递归 clone

```java
public class Address implements Cloneable {
    String city;
    public Address(String city) { this.city = city; }

    @Override
    public Address clone() {
        try { return (Address) super.clone(); } // Address 内部只有 String/int 等不可变或基本类型
        catch (CloneNotSupportedException e) { throw new AssertionError(e); }
    }
}

public class Person implements Cloneable {
    String name;
    Address address;

    public Person(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    @Override
    public Person clone() {
        try {
            Person p = (Person) super.clone(); // 先浅拷贝
            p.address = this.address.clone();  // 再把引用类型字段也深拷
            return p;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public static void main(String[] args) {
        Person p1 = new Person("Tom", new Address("Beijing"));
        Person p2 = p1.clone();
        p2.address.city = "Shanghai";
        System.out.println(p1.address.city); // Beijing ✔ 互不影响
        System.out.println(p1.address == p2.address); // false ✔
    }
}
```

### 方式 B：序列化实现深拷贝（推荐用于复杂对象图）

```java
import java.io.*;

public class DeepCopyUtil {
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T deepCopy(T obj) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(obj);
            }
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            try (ObjectInputStream ois = new ObjectInputStream(bis)) {
                return (T) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
```

优点：对象图嵌套再深也自动深拷贝；缺点：所有对象必须 `Serializable`，性能稍差。

### 方式 C：拷贝构造器 / 工厂方法（《Effective Java》推荐）

```java
public class Person {
    String name;
    Address address;

    public Person(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    // 拷贝构造器
    public Person(Person other) {
        this.name = other.name;
        this.address = new Address(other.address.city);
    }
}

// 使用
Person p2 = new Person(p1);
```

这是 Josh Bloch 在《Effective Java》Item 13 中推荐的方案——**避免实现 Cloneable**，因为 clone 机制设计得比较脆弱。

## 七、数组的 clone()

数组天然实现 `Cloneable`，其 `clone()` 是**浅拷贝**：

```java
int[] a = {1, 2, 3};
int[] b = a.clone();
b[0] = 99;
System.out.println(a[0]); // 1 → 基本类型元素互不影响

String[][] s1 = {{"x"}};
String[][] s2 = s1.clone();     // 外层数组新分配
s2[0][0] = "y";
System.out.println(s1[0][0]);   // y → 内部数组仍是共享引用(浅)

// 深拷数组用 Arrays.copyOf / System.arraycopy 并逐层拷
```

## 八、常见坑总结

| 坑                            | 说明                                                        | 规避                 |
|------------------------------|-----------------------------------------------------------|--------------------|
| 忘记 `implements Cloneable`    | 运行时抛 `CloneNotSupportedException`                         | 实现接口，重写 `public`   |
| `super.clone()` 写成 `new X()` | 丢失了 native 内存分配语义（其实等价于 copy ctor，但别混用）                   | 保留 `super.clone()` |
| 只调 `super.clone()` 就当深拷贝     | 只有引用字段被共享                                                 | 引用字段手动克隆           |
| 可变引用字段包括集合/数组                | `List` 需 `new ArrayList<>(list)` 才能深                      | 逐层处理               |
| 绕过构造器                        | 构造器副作用、`final` 字段、注册逻辑会失效                                 | 用拷贝构造器替代           |
| `String` / 包装类不用深拷           | 它们不可变，引用共享是安全的                                            | —                  |
| 泛型/多态                        | `clone()` 返回实际类型（因为按 `this.getClass()` 分配内存），子类调用不需要再重写检查 | —                  |

## 九、最佳实践建议

1. **优先用拷贝构造器或静态工厂**（`new Person(p)`），语义清晰、无异常、不依赖标记接口。
2. **只有在性能极其敏感**（避免构造器开销）或**需要 `Cloneable` 多态语义**（如 `clone()` 多态分派）时才实现 `clone()`。
3.
若必须实现：`implements Cloneable` + `public X clone()` + `try { return (X) super.clone(); } catch (CloneNotSupportedException e) { throw new AssertionError(e); }`。
4. **深/浅拷贝语义必须在文档中写清楚**，否则调用方会踩坑。
5. 对象图复杂（多层级引用、图结构）时首选**序列化或 JSON 序列化**深拷贝。

---

一句话总结：`Object.clone()` 是一个 **native 的浅拷贝**，需要 `Cloneable` 作为许可开关，**用 `super.clone()` 触发真正的内存分配与字段复制
**；引用字段共享是它最大的坑，需要手动或序列化做深拷贝。
