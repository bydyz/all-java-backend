# Object.toString() 详解

## 说明

* 默认情况下，toString()返回的是“对象的运行时类型 @ 对象的 hashCode 值 的十六进制形式"
* 在进行 String 与其它类型数据的连接操作时，自动调用 toString()方法
* 如果我们直接 System.out.println(对象)，默认会自动调用这个对象的toString()
  * 因为 Java 的引用数据类型的变量中存储的实际上时对象的内存地址，但是 Java 对程序员隐藏内存地址信息，所以不能直接将内存地址显示出来，所以当你打印对象时，JVM
    帮你调用了对象的 toString()。
* 可以根据需要在用户自定义类型中重写 toString()方法

## 一、源码

JDK 中 `java.lang.Object` 的 `toString()` 定义如下：

```java
public class Object {

    /**
     * 返回该对象的字符串表示。
     * 默认实现返回：类名 + "@" + hashCode 的十六进制形式
     */
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
}
```

拆开看三个部分：

| 部分                                | 说明                     |
|-----------------------------------|------------------------|
| `getClass().getName()`            | 全限定类名（包名 + 类名）         |
| `"@"`                             | 固定分隔符                  |
| `Integer.toHexString(hashCode())` | 对象 hashCode 的无符号十六进制表示 |

> 注意：`Object.hashCode()` 是 native 方法，HotSpot 默认值与对象地址相关，**每次运行 JVM 打印出来的值可能不同**，不要依赖它。

---

## 二、默认行为的代码演示

```java
public class ToStringDemo {
    public static void main(String[] args) {
        Person p = new Person("张三", 18);

        // 1. 直接调用
        String s1 = p.toString();
        System.out.println(s1);          // 例：com.example.Person@1b6d3586

        // 2. println 内部会自动调用 toString()
        System.out.println(p);           // 同上

        // 3. 字符串拼接也会自动调用 toString()
        String s2 = "person = " + p;     // 同上
        System.out.println(s2);

        // 4. 查看 hashCode 的十六进制，验证与默认 toString 一致
        System.out.println(Integer.toHexString(p.hashCode())); // 1b6d3586
    }
}

class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    // 未重写 toString()
}
```

输出类似：

```
com.example.Person@1b6d3586
com.example.Person@1b6d3586
person = com.example.Person@1b6d3586
1b6d3586
```

**问题来了**：这个输出对我们调试毫无价值，看不到 `name` 和 `age`。所以官方文档明确建议：

> It is recommended that all subclasses override this method.

---

## 三、为什么必须重写

1. **调试/日志**：`log.info("{}", user)` 输出的是有用的字段信息，而不是 `User@6d06d69c`。
2. **字符串拼接**：`"订单：" + order` 语义清晰。
3. **异常信息**：`throw new IllegalStateException("非法状态: " + this)` 能直接定位对象状态。
4. **可读性**：默认值无法反映对象内容。

---

## 四、重写的 5 种常见写法

### 写法 1：字符串拼接（最常用，JDK 8 下编译期优化为 StringBuilder）

```java
public class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}
```

输出：

```
Person{name='张三', age=18}
```

> 规范建议：重写时加 `@Override` 注解，避免方法名写错（写成 `tostring` 之类）导致"看似重写其实没重写"。

### 写法 2：StringBuilder（类字段很多时略快，可读性稍差）

```java
@Override
public String toString() {
    final StringBuilder sb = new StringBuilder("Person{");
    sb.append("name='").append(name).append('\'');
    sb.append(", age=").append(age);
    sb.append('}');
    return sb.toString();
}
```

### 写法 3：String.format（可读性好，但性能较差，**不要放在高频日志里**）

```java
@Override
public String toString() {
    return String.format("Person{name='%s', age=%d}", name, age);
}
```

### 写法 4：工具类 Objects / 第三方（Apache Commons Lang）

```java
import java.util.Objects;

@Override
public String toString() {
    // 适合字段少、想统一处理 null 的场景
    return "Person{name=" + Objects.toString(name, "null")
         + ", age=" + age + "}";
}
```

```java
// Apache Commons Lang3
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Override
public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("name", name)
            .append("age", age)
            .toString();
}
```

### 写法 5：Lombok / record（现代做法）

```java
import lombok.ToString;

@ToString
public class Person {
    private String name;
    private int age;
}
```

```java
// Java 16+ record 自动生成 equals/hashCode/toString
public record Person(String name, int age) { }

System.out.println(new Person("张三", 18));
// 输出：Person[name=张三, age=18]
```

---

## 五、与字符串拼接、String.valueOf 的关系

```java
Person p = new Person("张三", 18);

// 这三行是等价的，都调用 p.toString()
System.out.println(p);
System.out.println(String.valueOf(p));
System.out.println(p + "");
```

`String.valueOf(Object)` 的源码：

```java
public static String valueOf(Object obj) {
    return (obj == null) ? "null" : obj.toString();
}
```

> 重要区别：**`null.toString()` 会 NPE，`String.valueOf(null)` 返回 `"null"`**。日志框架里常见 `log.info("{}", obj)` 也不会
> NPE，因为它内部也用 `String.valueOf`。

如果想给 null 一个默认值：

```java
String s = Objects.toString(p, "未知对象");
```

---

## 六、经典坑

### 坑 1：打印数组得到 `[I@1b6d3586`

```java
int[] arr = {1, 2, 3};
System.out.println(arr);            // [I@1b6d3586
System.out.println(arr.toString()); // [I@1b6d3586  ← 数组没有重写 toString

// 正确写法
System.out.println(Arrays.toString(arr));       // [1, 2, 3]
System.out.println(Arrays.deepToString(new int[][]{{1},{2}})); // [[1], [2]]
```

### 坑 2：方法名写错导致没重写

```java
public String tostring() { ... }   // 编译能过，但没重写，输出还是默认格式
```

一定要用 `@Override`。

### 坑 3：toString() 中抛出 NPE

```java
@Override
public String toString() {
    return address.getCity();   // address 为 null 时直接 NPE
}
```

`toString` 应该"永不抛异常"，否则日志打印本身会把业务搞崩。字段访问前做好 null 保护，或使用 `String.valueOf`。

### 坑 4：循环引用导致 StackOverflowError

```java
class A { B b; @Override public String toString() { return "A{b=" + b + "}"; } }
class B { A a; @Override public String toString() { return "B{a=" + a + "}"; } }

new A().toString();   // A -> B -> A -> B ... 栈溢出
```

实体关系双向引用时，**只让其中一方 toString**，或使用带 ID 引用的写法。

### 坑 5：在 toString() 里做重活

```java
@Override
public String toString() {
    return "User{orders=" + loadOrdersFromDB() + "}";  // 千万不要
}
```

`toString` 可能被日志框架在任何地方调用（包括 DEBUG 日志、异常链路），应该轻量、无副作用。

### 坑 6：toString() 返回 null

```java
@Override
public String toString() {
    return null;   // 违反契约！
}
```

会导致 `println` 输出 `"null"`、拼接出现 `"null"`，甚至某些框架内部 NPE。规范要求返回非 null 字符串。

### 坑 7：敏感信息泄露

```java
@Override
public String toString() {
    return "Account{password='" + password + "'}";  // 密码写进日志！
}
```

日志脱敏是必须的，密码、token、身份证号要打码。

---

## 七、规范建议清单

| 建议                     | 说明                                                   |
|------------------------|------------------------------------------------------|
| 每个 POJO 都重写            | 尤其是有字段的类                                             |
| 加 `@Override`          | 防止写错签名                                               |
| 格式统一                   | 推荐 `类名{字段=值, 字段=值}` 风格                               |
| 不返回 null               | 契约要求                                                 |
| 不抛异常                   | 保护日志系统                                               |
| 不做耗时操作、无副作用            | 可能被调用多次                                              |
| 注意循环引用                 | 避免 StackOverflowError                                |
| 敏感字段脱敏                 | 密码/token 不打印                                         |
| 与 equals/hashCode 一起重写 | 保持字段一致                                               |
| 日志中惰性求值                | 如 log4j2 的 `Supplier`，或用 `if (log.isDebugEnabled())` |

---

## 八、一个完整可运行的完整示例

```java
import java.util.Arrays;
import java.util.Objects;

public class ToStringFullDemo {

    static class Person {
        private final String name;
        private final int age;
        private final String[] tags;

        Person(String name, int age, String[] tags) {
            this.name = name;
            this.age = age;
            this.tags = tags;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", tags=" + Arrays.toString(tags) +   // 数组要用 Arrays.toString
                    '}';
        }
    }

    public static void main(String[] args) {
        Person p = new Person("张三", 18, new String[]{"VIP", "新人"});
        System.out.println(p);                          // Person{name='张三', age=18, tags=[VIP, 新人]}
        System.out.println(String.valueOf(p));          // 同上
        System.out.println("输出：" + p);                // 输出：Person{...}
        System.out.println(Objects.toString(p, "默认")); // 同上
        System.out.println(Objects.toString(null, "默认")); // 默认
    }
}
```

**核心记住一句话**：`Object.toString()` 的默认实现是 `getClass().getName() + "@" + Integer.toHexString(hashCode())`
，它只告诉你"是什么类"，不告诉你"内容是什么"——所以几乎每个自定义类都应该重写它。
