# Object 类的 `equals()` 方法详解

## 一、源码实现

`java.lang.Object` 中的 `equals()` 方法定义如下：

```java
public boolean equals(Object obj) {
    return (this == obj);
}
```

**这就是全部源码。** 它做的事情非常简单——使用 `==` 运算符比较两个引用是否指向**同一个对象**（即比较内存地址）。

---

## 二、默认行为演示

```java
public class EqualsDemo {
    public static void main(String[] args) {
        Object o1 = new Object();
        Object o2 = new Object();
        Object o3 = o1;

        System.out.println(o1.equals(o2)); // false —— 不同对象
        System.out.println(o1.equals(o3)); // true  —— 同一引用
        System.out.println(o1.equals(o1)); // true  —— 自反性
        System.out.println(o1.equals(null)); // false —— 与 null 比较
    }
}
```

对于自定义类，如果**不重写** `equals()`，同样走引用比较：

```java
class Person {
    String name;
    int age;
    Person(String name, int age) { this.name = name; this.age = age; }
}

public class Test {
    public static void main(String[] args) {
        Person p1 = new Person("张三", 20);
        Person p2 = new Person("张三", 20);

        System.out.println(p1.equals(p2)); // false！内容一样，但不是同一对象
        System.out.println(p1 == p2);      // false
    }
}
```

**结论**：`Object.equals()` 的默认语义就是"是不是同一个对象"。

---

## 三、`equals()` 的 5 条契约（Java 规范约定）

重写 `equals()` 时必须遵守，否则集合类（`HashMap`、`HashSet`）会出问题：

| 性质                   | 说明                                                            | 代码体现          |
|----------------------|---------------------------------------------------------------|---------------|
| **自反性** (Reflexive)  | `x.equals(x)` 必须为 `true`                                      | `x.equals(x)` |
| **对称性** (Symmetric)  | `x.equals(y)` 为 `true` 则 `y.equals(x)` 也必为 `true`             | 双向一致          |
| **传递性** (Transitive) | `x.equals(y)`、`y.equals(z)` 为 `true`，则 `x.equals(z)` 为 `true` | 链式一致          |
| **一致性** (Consistent) | 只要对象状态不变，多次调用结果不变                                             | 不要依赖随机数/时间    |
| **非空性** (Non-null)   | `x.equals(null)` 必须返回 `false`                                 | 判空处理          |

---

## 四、如何正确重写 `equals()`

### 示例：手写经典实现

```java
class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object obj) {
        // 1. 引用相等，直接 true（性能优化 + 满足自反性）
        if (this == obj) return true;

        // 2. null 或类型不同，返回 false（满足非空性）
        if (obj == null || getClass() != obj.getClass()) return false;

        // 3. 强制类型转换后逐字段比较
        Person other = (Person) obj;
        return this.age == other.age
            && Objects.equals(this.name, other.name); // 防止 name 为 null
    }

    // ⚠️ 重写 equals 必须同时重写 hashCode
    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
```

> **为什么用 `getClass()` 而不是 `instanceof`？**
> - `getClass()`：严格同类型才相等，符合**对称性**（推荐）
> - `instanceof`：允许子类与父类相等，但在继承场景下容易破坏对称性（除非父类 `equals` 是 final 的）

### 测试验证

```java
public class Test {
    public static void main(String[] args) {
        Person p1 = new Person("张三", 20);
        Person p2 = new Person("张三", 20);
        Person p3 = new Person("李四", 25);

        System.out.println(p1.equals(p2));   // true  —— 内容相同
        System.out.println(p1.equals(p3));   // false
        System.out.println(p1.equals(null)); // false —— 非空性
        System.out.println(p1.equals(p1));   // true  —— 自反性

        // 放入 HashSet 验证 hashCode 一致性
        java.util.Set<Person> set = new java.util.HashSet<>();
        set.add(p1);
        System.out.println(set.contains(p2)); // true，因为 equals + hashCode 一致
    }
}
```

### 示例：使用 IDE / Lombok 自动生成

**Lombok（推荐）**：

```java
@EqualsAndHashCode
class Person {
    private String name;
    private int age;
}
```

**IDEA**：`Alt+Insert` → `equals() and hashCode()` → 自动生成。

**JDK 16+ `record`（自动实现）**：

```java
public record Person(String name, int age) {}
// 编译器自动生成 equals()、hashCode()、toString()
```

---

## 五、常见错误与陷阱

### 陷阱 1：重写 `equals()` 却忘了 `hashCode()`

```java
class Bad {
    String id;
    Bad(String id) { this.id = id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bad)) return false;
        return id.equals(((Bad) o).id);
    }
    // ❌ 没有重写 hashCode
}

public class Test {
    public static void main(String[] args) {
        Set<Bad> set = new HashSet<>();
        set.add(new Bad("001"));
        System.out.println(set.contains(new Bad("001"))); // false！违反逻辑
    }
}
```

**原因**：`HashSet` 先按 `hashCode` 找桶，再在桶里用 `equals` 比较。两个"相等"对象如果 `hashCode` 不同，会落到不同桶，永远找不到彼此。

### 陷阱 2：参数类型写成自定义类而非 `Object`

```java
class Wrong {
    public boolean equals(Wrong other) { // ❌ 这不是重写，是重载！
        return true;
    }
}
```

这**不会**覆盖 `Object.equals(Object)`，实际调用的仍是父类方法。正确签名必须写 `equals(Object obj)`，并加 `@Override`
让编译器帮你检查。

### 陷阱 3：`equals` 参数为 `null` 时引发 NPE

```java
@Override
public boolean equals(Object obj) {
    Person p = (Person) obj; // ❌ 若 obj 为 null，抛 NPE
    return this.age == p.age;
}
```

要先判空：

```java
if (obj == null) return false;
```

### 陷阱 4：可变字段参与 `equals`/`hashCode`

```java
class Mutable {
    int id; // 若放入 HashSet 后修改 id，hashCode 变化，就再也找不到了
}
```

**规则**：作为 `HashMap` / `HashSet` 的 key 的字段，应**尽量不可变**。

### 陷阱 5：浮点数字段直接用 `==` 比较

```java
double x, y;
// ❌ return this.x == other.x;
// ✅ return Double.compare(this.x, other.x) == 0;
```

NaN 的情况尤其要注意（`NaN == NaN` 为 `false`，但业务上通常希望相等）。

---

## 六、`equals` 与 `==` 的对照总结

| 比较对象   | `==` | `equals()`（重写后）                |
|--------|------|--------------------------------|
| 基本类型   | 比较值  | 无此方法                           |
| 引用类型   | 比较地址 | 比较内容（重写后）                      |
| 未重写时   | 比较地址 | 比较地址（等同 `==`）                  |
| `null` | 可用   | 调用方会 NPE，参数传 `null` 返回 `false` |

---

## 七、推荐写法（模板）

```java
@Override
public boolean equals(Object obj) {
    if (this == obj) return true;                          // 1. 引用相等
    if (obj == null) return false;                         // 2. 判 null
    if (getClass() != obj.getClass()) return false;        // 3. 类型检查
    MyClass other = (MyClass) obj;                         // 4. 转型
    return Objects.equals(field1, other.field1)            // 5. 逐字段
        && this.field2 == other.field2;
}

@Override
public int hashCode() {
    return Objects.hash(field1, field2);
}
```

记住一句话：**`equals` 管相等性，`hashCode` 管散列桶，两者必须结伴重写，且逻辑一致。**

# == 的理解

* 基本类型比较值:只要两个变量的值相等，即为 true。
* 引用类型比较引用(是否指向同一个对象)：只有指向同一个对象时，==才返回 true
* 用“==”进行比较时，符号两边的数据类型必须兼容(可自动转换的基本数据类型除外)，否则编译出错
