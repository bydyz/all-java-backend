# Java 中的多态性详解

## 一、什么内容有多态性

Java 中具有多态性的“内容”主要分三类：

| 类型                  | 表现形式        | 绑定时机    |
|---------------------|-------------|---------|
| **方法重载（Overload）**  | 同类中同名不同参    | 编译时（静态） |
| **方法重写（Override）**  | 子类重写父类/接口方法 | 运行时（动态） |
| **向上转型（Upcasting）** | 父类引用指向子类对象  | 运行时（动态） |

> ⚠️ 没有多态性的内容：**成员变量、static 方法、private 方法、final 方法、构造器**。它们都在编译期确定，或者不能被子类改变。

---

## 二、多态如何产生

**运行时多态的三要素（缺一不可）：**

1. **继承**（extends）或 **实现**（implements）
2. **重写**（Override）父类/接口的方法
3. **向上转型**（父类引用 = 子类对象）

> 原理：JVM 通过 **虚方法表（vtable）** 在运行时根据对象的实际类型决定调用哪个方法（动态绑定）。

**编译时多态**只要方法名相同、参数列表不同即可。

---

## 三、多态的作用

1. **解耦**：调用方只依赖抽象（父类/接口），不依赖具体实现
2. **可扩展**：新增实现类无需修改已有代码（**开闭原则**）
3. **可维护**：统一调用入口，代码简洁
4. **代码复用**：父类通用逻辑被子类继承

---

## 四、代码详解

### 1. 编译时多态：方法重载

```java
public class Calculator {
    public int add(int a, int b)               { return a + b; }
    public double add(double a, double b)      { return a + b; }
    public int add(int a, int b, int c)        { return a + b + c; }

    public static void main(String[] args) {
        Calculator c = new Calculator();
        System.out.println(c.add(1, 2));            // 3        → add(int,int)
        System.out.println(c.add(1.0, 2.0));        // 3.0      → add(double,double)
        System.out.println(c.add(1, 2, 3));         // 6        → add(int,int,int)
    }
}
```

**编译器**根据实参的**静态类型**（编译期可见的类型）来选定方法，所以叫**静态多态**。

---

### 2. 运行时多态：继承 + 重写 + 向上转型

```java
// 父类
abstract class Animal {
    public abstract void speak();
}

class Dog extends Animal {
    @Override public void speak() { System.out.println("汪汪汪"); }
}

class Cat extends Animal {
    @Override public void speak() { System.out.println("喵喵喵"); }
}

public class Test {
    public static void main(String[] args) {
        Animal a1 = new Dog();   // 向上转型
        Animal a2 = new Cat();
        a1.speak();              // 汪汪汪  ← 运行时才决定调 Dog.speak
        a2.speak();              // 喵喵喵  ← 运行时才决定调 Cat.speak
    }
}
```

编译期 `a1` 是 `Animal` 类型，看不到 `Dog` 的细节；运行时 JVM 通过对象实际类型 `Dog` 找到 `speak()` 的实现。

---

### 3. 接口多态（最常用场景）

```java
interface Payment {
    void pay(double amount);
}

class Alipay implements Payment {
    @Override public void pay(double amount) {
        System.out.println("支付宝支付：" + amount);
    }
}

class WechatPay implements Payment {
    @Override public void pay(double amount) {
        System.out.println("微信支付：" + amount);
    }
}

public class Shop {
    // 关键：形参是接口类型，不关心具体实现
    public void checkout(Payment p, double amount) {
        p.pay(amount);
    }

    public static void main(String[] args) {
        Shop shop = new Shop();
        shop.checkout(new Alipay(), 100);     // 支付宝支付：100.0
        shop.checkout(new WechatPay(), 200);  // 微信支付：200.0

        // 新增银行卡支付？不用改 Shop.checkout，只加一个新类即可 → 开闭原则
    }
}
```

**这是多态最核心的作用**：`checkout` 方法一处编写，对未来任何新支付方式都自动适配。

---

### 4. 集合中的多态（面向接口编程）

```java
import java.util.*;

public class CollectionDemo {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();   // 接口引用指向实现类
        list.add("Java"); list.add("Python");
        for (String s : list) System.out.println(s);

        // 换成 LinkedList 也无需改其他代码
        list = new LinkedList<>(list);
        System.out.println(list);
    }
}
```

`List` 引用变，调用方代码不变——这就是**依赖抽象**带来的灵活性。

---

### 5. 没有多态性的反例（必须掌握）

```java
class Parent {
    String name = "parent";
    static void staticMethod() { System.out.println("Parent static"); }
    private void privateMethod() { System.out.println("Parent private"); }
    final   void finalMethod()  { System.out.println("Parent final"); }
}

class Child extends Parent {
    String name = "child";
    static void staticMethod() { System.out.println("Child static"); }   // 隐藏，不是重写
    // privateMethod() 不能被重写（父类不可见）
    // finalMethod() 不能被重写（final 禁止）
}

public class NoPolymorphism {
    public static void main(String[] args) {
        Parent p = new Child();
        System.out.println(p.name);       // parent —— 字段看引用类型
        p.staticMethod();                  // Parent static —— 静态方法看引用类型
    }
}
```

结论：**字段、static、private、final 都不具备多态性**。

---

### 6. 完整的实际应用示例（策略模式）

```java
interface DiscountStrategy {
    double discount(double price);
}

class NoDiscount implements DiscountStrategy {
    @Override public double discount(double price) { return price; }
}

class VipDiscount implements DiscountStrategy {
    @Override public double discount(double price) { return price * 0.8; }
}

class SvipDiscount implements DiscountStrategy {
    @Override public double discount(double price) { return price * 0.6; }
}

class Order {
    private DiscountStrategy strategy;

    // 通过构造注入具体策略 → 多态的核心用法
    public Order(DiscountStrategy strategy) { this.strategy = strategy; }

    public double calcTotal(double price) {
        return strategy.discount(price);   // 运行时根据实际对象执行
    }
}

public class StrategyDemo {
    public static void main(String[] args) {
        double price = 1000;
        System.out.println(new Order(new NoDiscount()).calcTotal(price));   // 1000.0
        System.out.println(new Order(new VipDiscount()).calcTotal(price));  // 800.0
        System.out.println(new Order(new SvipDiscount()).calcTotal(price)); // 600.0
    }
}
```

---

## 五、一句话总结

> **多态 = 同一引用，多种形态。**
> **重载**在编译期决定方法；
> **重写 + 向上转型**在运行期决定方法。
> 它的价值在于**面向抽象编程**，让代码对扩展开放、对修改关闭。
