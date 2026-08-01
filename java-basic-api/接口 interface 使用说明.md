# 接口 interface 使用说明

最终会被编 译成.class 文件，但一定要明确它并不是类，而是另外一种引用数据类型。

## 1. 接口的定义格式

### 1.1 声明格式

```text
[修饰符] interface 接口名 [extends 父接口1, 父接口2, ...] {
    // 接口的成员列表：
    // 公共的静态常量
    // 公共的抽象方法
    
    // 公共的默认方法（JDK1.8 以上）
    // 公共的静态方法（JDK1.8 以上）
    // 私有方法（JDK1.9 以上）
}
```

**示例：**
```java
// 定义一个接口
public interface MyInterface {
    // 接口内容
}
```

**修饰符说明：**
- `public`：公共接口，可以在任何包中访问
- 默认（不写修饰符）：包级私有，只能在同一个包中访问

### 1.2 接口中的成员

#### 1.2.1 常量（静态常量）

```java
public interface MyInterface {
    // 接口中的变量默认是 public static final 的
    int MAX_SIZE = 100;           // 等同于 public static final int MAX_SIZE = 100;
    String NAME = "interface";    // 等同于 public static final String NAME = "interface";
}
```

**特点：**
- 默认修饰符：`public static final`
- 必须在声明时初始化
- 值不可修改（常量）

#### 1.2.2 抽象方法

```java
public interface MyInterface {
    // 接口中的方法默认是 public abstract 的
    void method1();                    // 等同于 public abstract void method1();
    String method2(int param);         // 等同于 public abstract String method2(int param);
}
```

**特点：**
- 默认修饰符：`public abstract`
- 只有方法签名，没有方法体
- 实现类必须重写所有抽象方法

#### 1.2.3 默认方法（Java 8+）

```java
public interface MyInterface {
    // 默认方法 - 有方法体
    default void defaultMethod() {
        System.out.println("这是默认方法");
    }
    
    // 默认方法可以有参数
    default void printMessage(String message) {
        System.out.println(message);
    }
}
```

**特点：**
- 使用 `default` 关键字修饰
- 有方法体
- 实现类可以重写，也可以不重写
- 提供了接口的默认实现

#### 1.2.4 静态方法（Java 8+）

```java
public interface MyInterface {
    // 静态方法
    static void staticMethod() {
        System.out.println("这是静态方法");
    }
    
    // 工具方法
    static int add(int a, int b) {
        return a + b;
    }
}
```

**特点：**
- 使用 `static` 关键字修饰
- 有方法体
- 通过接口名直接调用：`MyInterface.staticMethod()`
- 实现类不能重写静态方法

#### 1.2.5 私有方法（Java 9+）

```java
public interface MyInterface {
    default void method1() {
        System.out.println("method1");
        helperMethod();  // 调用私有方法
    }
    
    default void method2() {
        System.out.println("method2");
        helperMethod();  // 调用私有方法
    }
    
    // 私有方法 - 只能在接口内部使用
    private void helperMethod() {
        System.out.println("这是私有方法，用于复用代码");
    }
}
```

**特点：**
- 使用 `private` 关键字修饰
- 只能在接口内部使用
- 用于复用默认方法中的代码
- 提高代码复用性

#### 1.2.6 接口中没有构造器，没有初始化块，因为接口中没有成员变量需要动态初始化。

### 1.3 接口成员总结

| 成员类型 | 修饰符 | 是否有方法体 | 说明 |
|---------|--------|------------|------|
| 常量 | public static final | - | 必须初始化，值不可变 |
| 抽象方法 | public abstract | 否 | 实现类必须重写 |
| 默认方法 | public | 是 | 实现类可重写可不重写 |
| 静态方法 | public | 是 | 通过接口名调用 |
| 私有方法 | private | 是 | 只能在接口内部使用 |

没有构造器，没有初始化块

---

## 2. 接口的使用规则

1. 接口不能创建对象，但是可以被类实现（implements ，类似于被继承）。
2. 类与接口的关系为实现关系，即类实现接口，该类可以称为接口的实现类。实现的动作类似继承，格式相仿，只是关键字不同，实现使用 implements 关键字。

### 2.1 类实现接口

```java
// 定义接口
public interface Printable {
    void print();
}

public interface Loggable {
    void log();
}

// 类实现接口
public class MyClass implements Printable, Loggable {
    @Override
    public void print() {
        System.out.println("打印中...");
    }
    
    @Override
    public void log() {
        System.out.println("记录日志...");
    }
}
```

**规则：**
- 使用 `implements` 关键字
- 一个类可以实现多个接口（多继承）
- 必须重写所有抽象方法
- 否则该类必须声明为抽象类

### 2.2 接口继承接口

```java
// 定义父接口
public interface Animal {
    void eat();
}

public interface Flyable {
    void fly();
}

// 接口继承多个接口
public interface Bird extends Animal, Flyable {
    void sing();
}
```

**规则：**
- 使用 `extends` 关键字
- 一个接口可以继承多个接口
- 子接口继承父接口的所有方法（包括抽象方法、默认方法、静态方法）
- 子接口可以添加新的抽象方法

### 2.3 抽象类实现接口

```java
public interface Shape {
    double getArea();
    double getPerimeter();
}

// 抽象类可以只实现部分方法
public abstract class AbstractShape implements Shape {
    private String name;
    
    public AbstractShape(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    // 只实现 getArea，getPerimeter 留给子类
    @Override
    public double getArea() {
        return 0;
    }
}
```

### 2.4 接口的多态性

```java
public interface Animal {
    void makeSound();
}

public class Dog implements Animal {
    @Override
    public void makeSound() {
        System.out.println("汪汪汪");
    }
}

public class Cat implements Animal {
    @Override
    public void makeSound() {
        System.out.println("喵喵喵");
    }
}

// 多态使用
public class Test {
    public static void main(String[] args) {
        Animal animal1 = new Dog();
        Animal animal2 = new Cat();
        
        animal1.makeSound();  // 汪汪汪
        animal2.makeSound();  // 喵喵喵
    }
}
```

### 2.5 接口作为参数和返回值

```java
public interface Comparator<T> {
    int compare(T o1, T o2);
}

public class StringLengthComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return o1.length() - o2.length();
    }
}

public class Test {
    // 接口作为参数
    public static void sort(String[] arr, Comparator<String> comp) {
        // 排序逻辑
    }
    
    // 接口作为返回值
    public static Comparator<String> getComparator() {
        return new StringLengthComparator();
    }
    
    public static void main(String[] args) {
        Comparator<String> comp = getComparator();
        sort(new String[]{"a", "bb", "ccc"}, comp);
    }
}
```

### 2.6 匿名内部类实现接口

```java
public interface ClickListener {
    void onClick();
}

public class Button {
    private ClickListener listener;
    
    public void setOnClickListener(ClickListener listener) {
        this.listener = listener;
    }
    
    public void click() {
        if (listener != null) {
            listener.onClick();
        }
    }
}

// 使用匿名内部类
public class Test {
    public static void main(String[] args) {
        Button button = new Button();
        
        button.setOnClickListener(new ClickListener() {
            @Override
            public void onClick() {
                System.out.println("按钮被点击了");
            }
        });
        
        button.click();
    }
}
```

### 2.7 Lambda 表达式实现函数式接口（Java 8+）

```java
// 函数式接口 - 只有一个抽象方法的接口
@FunctionalInterface
public interface ClickListener {
    void onClick();
}

public class Test {
    public static void main(String[] args) {
        Button button = new Button();
        
        // 使用 Lambda 表达式
        button.setOnClickListener(() -> System.out.println("按钮被点击了"));
        
        button.click();
    }
}
```

### 2.8 默认方法的使用

```java
public interface Vehicle {
    default void start() {
        System.out.println("车辆启动");
    }
    
    default void stop() {
        System.out.println("车辆停止");
    }
}

// 实现类可以选择不重写默认方法
public class Car implements Vehicle {
    // 不重写 start() 和 stop()，使用默认实现
}

// 实现类也可以选择重写
public class Truck implements Vehicle {
    @Override
    public void start() {
        System.out.println("卡车启动，发出轰鸣声");
    }
}
```

### 2.9 静态方法的使用

```java
public interface MathUtils {
    static int add(int a, int b) {
        return a + b;
    }
    
    static int multiply(int a, int b) {
        return a * b;
    }
}

// 通过接口名调用静态方法
public class Test {
    public static void main(String[] args) {
        int sum = MathUtils.add(10, 20);      // 30
        int product = MathUtils.multiply(5, 6); // 30
    }
}
```

---

## 3. 接口之间的冲突问题

### 3.1 多个接口中默认方法签名相同

```java
public interface InterfaceA {
    default void show() {
        System.out.println("InterfaceA 的默认方法");
    }
}

public interface InterfaceB {
    default void show() {
        System.out.println("InterfaceB 的默认方法");
    }
}
```

**冲突情况：** 当一个类同时实现这两个接口时，编译器不知道该使用哪个默认方法的实现。

### 3.2 解决方案：必须重写该方法

```java
public class MyClass implements InterfaceA, InterfaceB {
    @Override
    public void show() {
        // 方案1：选择一个接口的实现
        InterfaceA.super.show();
        
        // 方案2：完全自定义实现
        System.out.println("MyClass 的自定义实现");
    }
}
```

**规则：**
- 必须显式重写有冲突的默认方法
- 可以使用 `接口名.super.方法名()` 调用特定接口的实现
- 不能同时调用两个接口的默认方法

### 3.3 多层继承中的默认方法冲突

```java
public interface InterfaceA {
    default void show() {
        System.out.println("InterfaceA");
    }
}

public interface InterfaceB extends InterfaceA {
    default void show() {
        System.out.println("InterfaceB");
    }
}

public class MyClass implements InterfaceA, InterfaceB {
    @Override
    public void show() {
        // InterfaceB 的默认方法会覆盖 InterfaceA 的
        InterfaceB.super.show();  // 输出：InterfaceB
    }
}
```

**规则：** 当接口之间存在继承关系时，子接口的默认方法优先级更高。

### 3.4 多个接口中静态方法冲突

```java
public interface InterfaceA {
    static void show() {
        System.out.println("InterfaceA 的静态方法");
    }
}

public interface InterfaceB {
    static void show() {
        System.out.println("InterfaceB 的静态方法");
    }
}

// 静态方法不会冲突，通过接口名区分
public class Test {
    public static void main(String[] args) {
        InterfaceA.show();  // InterfaceA 的静态方法
        InterfaceB.show();  // InterfaceB 的静态方法
    }
}
```

**规则：** 静态方法属于接口本身，通过接口名调用，不会产生冲突。

### 3.5 接口常量冲突

```java
public interface InterfaceA {
    int MAX = 100;
}

public interface InterfaceB {
    int MAX = 200;
}

// 常量冲突
public class MyClass implements InterfaceA, InterfaceB {
    // 编译错误：MAX 有歧义
    // 必须显式指定使用哪个常量
    public void printMax() {
        System.out.println(InterfaceA.MAX);  // 100
        System.out.println(InterfaceB.MAX);  // 200
    }
}
```

---

## 4. 接口与类之间的冲突问题

### 4.1 类的成员与接口的默认方法冲突

```java
public interface Greeting {
    default void sayHello() {
        System.out.println("Hello from interface");
    }
}

public class MyClass implements Greeting {
    // 类的成员方法优先于接口的默认方法
    @Override
    public void sayHello() {
        System.out.println("Hello from class");
    }
}
```

**规则：** 类的成员方法优先级高于接口的默认方法。

### 4.2 多个接口的默认方法与类方法冲突

```java
public interface InterfaceA {
    default void show() {
        System.out.println("InterfaceA");
    }
}

public interface InterfaceB {
    default void show() {
        System.out.println("InterfaceB");
    }
}

public class MyClass implements InterfaceA, InterfaceB {
    // 必须重写，否则编译错误
    @Override
    public void show() {
        System.out.println("MyClass");
    }
}
```

### 4.3 类的静态方法与接口的静态方法

```java
public interface MyInterface {
    static void show() {
        System.out.println("Interface static method");
    }
}

public class MyClass {
    // 类的静态方法与接口的静态方法可以同名，互不影响
    static void show() {
        System.out.println("Class static method");
    }
}

// 使用时通过类名或接口名区分
public class Test {
    public static void main(String[] args) {
        MyInterface.show();  // Interface static method
        MyClass.show();      // Class static method
    }
}
```

### 4.4 抽象类与接口的成员冲突

```java
public interface MyInterface {
    void show();
    
    default void display() {
        System.out.println("Interface default method");
    }
}

public abstract class MyClass implements MyInterface {
    // 抽象类可以只实现部分接口方法
    @Override
    public void show() {
        System.out.println("Class implemented method");
    }
    
    // display() 可以不实现，留给子类
}

public class SubClass extends MyClass {
    @Override
    public void display() {
        System.out.println("SubClass override");
    }
}
```

### 4.5 继承与实现的优先级

```java
public interface MyInterface {
    default void show() {
        System.out.println("Interface default method");
    }
}

public class Parent {
    public void show() {
        System.out.println("Parent class method");
    }
}

// 当类同时继承父类和实现接口时，类方法优先
public class Child extends Parent implements MyInterface {
    // 不需要重写 show()，Parent 的方法优先
}

public class Test {
    public static void main(String[] args) {
        Child child = new Child();
        child.show();  // 输出：Parent class method
    }
}
```

**规则：** 当类同时继承父类和实现接口时，如果方法签名相同，父类的方法优先于接口的默认方法。

---

## 5. 接口的总结和面试题

### 5.1 接口核心要点总结

#### 5.1.1 接口的本质
- 接口是一种**引用数据类型**（类似于类）
- 接口是一种**规范**和**契约**
- 接口体现了**面向接口编程**的思想

#### 5.1.2 接口的特点
1. **抽象性**：包含抽象方法，定义行为规范
2. **多继承性**：一个类可以实现多个接口
3. **多态性**：接口引用可以指向不同的实现类对象
4. **解耦性**：定义和实现分离，降低耦合度
5. **可扩展性**：易于扩展和维护

#### 5.1.3 接口的成员总结
| 成员类型 | 修饰符 | 数量限制 |
|---------|--------|---------|
| 常量 | public static final | 无限制 |
| 抽象方法 | public abstract | 无限制 |
| 默认方法 | public | 无限制 |
| 静态方法 | public | 无限制 |
| 私有方法 | private | 无限制 |

#### 5.1.4 接口的使用场景
1. **定义规范**：规定类必须实现的方法
2. **实现多态**：通过接口引用指向不同实现类
3. **解耦设计**：分离定义和实现
4. **回调机制**：实现事件监听、回调函数
5. **策略模式**：定义算法族，使算法可互换

### 5.2 常见面试题

#### 面试题1：接口和抽象类有什么区别？

**答案：**

| 特性 | 接口 | 抽象类 |
|-----|------|-------|
| 关键字 | interface | abstract class |
| 多继承 | 支持多实现 | 只能单继承 |
| 构造方法 | 没有 | 有 |
| 成员变量 | 只有常量（public static final） | 可以有普通变量 |
| 方法 | 抽象方法、默认方法、静态方法、私有方法 | 抽象方法、普通方法 |
| 设计目的 | 定义行为规范 | 定义通用功能 |

#### 面试题2：接口中可以有构造方法吗？

**答案：** 不可以。接口是抽象类型，不能被实例化，所以不需要构造方法。

#### 面试题3：接口中可以有 main 方法吗？

**答案：** 可以。Java 8 开始，接口可以有静态方法，所以可以有 `main` 方法。

```java
public interface MyInterface {
    static void main(String[] args) {
        System.out.println("接口中的 main 方法");
    }
}
```

#### 面试题4：一个类可以实现多个接口吗？

**答案：** 可以。Java 支持多实现，一个类可以同时实现多个接口。

```java
public class MyClass implements InterfaceA, InterfaceB, InterfaceC {
    // 必须重写所有接口的抽象方法
}
```

#### 面试题5：接口可以继承多个接口吗？

**答案：** 可以。Java 支持接口的多继承。

```java
public interface InterfaceA extends InterfaceB, InterfaceC {
    // 继承了 InterfaceB 和 InterfaceC 的所有方法
}
```

#### 面试题6：接口中的变量默认是什么修饰符？

**答案：** 默认是 `public static final`。

```java
public interface MyInterface {
    int MAX = 100;  // 等同于 public static final int MAX = 100;
}
```

#### 面试题7：接口中的方法默认是什么修饰符？

**答案：**
- 抽象方法：默认是 `public abstract`
- 默认方法：必须使用 `default` 关键字
- 静态方法：必须使用 `static` 关键字

#### 面试题8：Java 8 对接口做了哪些增强？

**答案：**
1. **默认方法**：使用 `default` 关键字，提供默认实现
2. **静态方法**：接口中可以定义静态方法
3. **函数式接口**：只有一个抽象方法的接口，可以用 Lambda 表达式实现

#### 面试题9：Java 9 对接口做了哪些增强？

**答案：**
1. **私有方法**：使用 `private` 关键字，可以在接口内部复用代码
2. **改进的接口继承**：子接口可以继承父接口的默认方法

#### 面试题10：多个接口的默认方法冲突时怎么办？

**答案：**
1. 必须在实现类中显式重写该方法
2. 可以使用 `接口名.super.方法名()` 调用特定接口的实现

```java
public class MyClass implements InterfaceA, InterfaceB {
    @Override
    public void show() {
        InterfaceA.super.show();  // 调用 InterfaceA 的实现
    }
}
```

#### 面试题11：接口可以有成员变量吗？

**答案：** 只能有常量（public static final），不能有普通成员变量。

#### 面试题12：接口可以有私有方法吗？

**答案：** Java 9 开始支持。私有方法用于在接口内部复用默认方法中的代码。

#### 面试题13：接口与类的关系有哪些？

**答案：**
1. **实现关系（implements）**：类实现接口
2. **继承关系（extends）**：接口继承接口
3. **依赖关系**：类的方法参数或返回值使用接口类型

#### 面试题14：面向接口编程的好处是什么？

**答案：**
1. **解耦**：接口定义和实现分离，降低耦合度
2. **可扩展性**：易于扩展新的实现
3. **可测试性**：可以使用 Mock 对象进行测试
4. **灵活性**：运行时可以选择不同的实现

#### 面试题15：接口可以有对象引用吗？

**答案：** 可以。接口可以声明引用变量，类型为接口类型，用于指向实现该接口的对象。

```java
MyInterface obj = new MyClass();  // 接口引用指向实现类对象
```

---

## 6. 接口与抽象类之间的对比

### 6.1 概念对比

| 特性 | 接口 | 抽象类 |
|-----|------|-------|
| 定义 | 一种规范和契约 | 一种抽象的概念 |
| 关键字 | `interface` | `abstract class` |
| 实例化 | 不能实例化 | 不能实例化 |
| 设计目的 | 定义行为规范 | 定义通用功能 |

### 6.2 语法对比

| 特性 | 接口 | 抽象类 |
|-----|------|-------|
| 继承 | 多继承（extends 多个接口） | 单继承（extends 一个类） |
| 实现 | 多实现（implements 多个接口） | 只能被一个类继承 |
| 构造方法 | 没有 | 有 |
| 析构方法 | 没有 | 没有 |
| main 方法 | 可以有（Java 8+） | 可以有 |

### 6.3 成员对比

| 成员 | 接口 | 抽象类 |
|-----|------|-------|
| 常量 | public static final | 可以有普通常量 |
| 变量 | 不允许 | 允许普通成员变量 |
| 抽象方法 | public abstract | 可以有，修饰符任意 |
| 普通方法 | 只有默认方法和静态方法 | 可以有普通方法 |
| 静态方法 | Java 8+ 支持 | 支持 |
| 私有方法 | Java 9+ 支持 | 支持 |

### 6.4 设计理念对比

#### 接口：**"能做什么"**
- 定义行为规范
- 描述能力
- 强调契约

```java
// 接口定义"能飞"的能力
public interface Flyable {
    void fly();
}

// 接口定义"能游泳"的能力
public interface Swimmable {
    void swim();
}

// 一个类可以有多种能力
public class Duck implements Flyable, Swimmable {
    @Override
    public void fly() {
        System.out.println("鸭子在飞");
    }
    
    @Override
    public void swim() {
        System.out.println("鸭子在游泳");
    }
}
```

#### 抽象类：**"是什么"**
- 定义本质特征
- 描述共性
- 强调继承

```java
// 抽象类定义"动物"的本质
public abstract class Animal {
    protected String name;
    
    public Animal(String name) {
        this.name = name;
    }
    
    public abstract void makeSound();
    
    public void sleep() {
        System.out.println(name + "在睡觉");
    }
}

// 子类继承动物的本质
public class Dog extends Animal {
    public Dog(String name) {
        super(name);
    }
    
    @Override
    public void makeSound() {
        System.out.println("汪汪汪");
    }
}
```

### 6.5 使用场景对比

#### 适合使用接口的场景：
1. **定义规范**：规定类必须实现的方法
2. **实现多态**：通过接口引用指向不同实现类
3. **解耦设计**：分离定义和实现
4. **回调机制**：实现事件监听、回调函数
5. **策略模式**：定义算法族，使算法可互换
6. **混合类型**：一个类需要多种不相关的能力

```java
// 示例：回调机制
public interface Callback {
    void onComplete(String result);
    void onError(String error);
}

public class AsyncTask {
    public void execute(Callback callback) {
        try {
            // 执行任务
            String result = "任务完成";
            callback.onComplete(result);
        } catch (Exception e) {
            callback.onError(e.getMessage());
        }
    }
}
```

#### 适合使用抽象类的场景：
1. **代码复用**：多个子类有共同的代码
2. **模板方法**：定义算法骨架，子类实现具体步骤
3. **部分实现**：提供部分默认实现
4. **状态管理**：管理共享状态

```java
// 示例：模板方法
public abstract class DataProcessor {
    // 模板方法
    public final void process() {
        readData();
        processData();
        writeData();
    }
    
    protected abstract void readData();
    protected abstract void processData();
    protected abstract void writeData();
}

public class CSVProcessor extends DataProcessor {
    @Override
    protected void readData() {
        System.out.println("读取CSV文件");
    }
    
    @Override
    protected void processData() {
        System.out.println("处理CSV数据");
    }
    
    @Override
    protected void writeData() {
        System.out.println("写入CSV文件");
    }
}
```

### 6.6 选择原则

#### 优先使用接口的情况：
1. 需要多继承
2. 需要定义行为规范
3. 需要实现多态
4. 需要解耦设计
5. 需要回调机制

#### 优先使用抽象类的情况：
1. 需要共享代码
2. 需要模板方法
3. 需要部分实现
4. 需要管理状态
5. 需要构造方法

### 6.7 组合使用

在实际开发中，接口和抽象类经常组合使用：

```java
// 接口定义行为
public interface Comparable<T> {
    int compareTo(T o);
}

// 抽象类提供部分实现
public abstract class AbstractList<E> {
    protected E[] elements;
    protected int size;
    
    public E get(int index) {
        return elements[index];
    }
    
    public int size() {
        return size;
    }
    
    public abstract void add(E element);
}

// 具体实现
public class SortedList<E extends Comparable<E>> extends AbstractList<E> {
    @Override
    public void add(E element) {
        // 实现排序添加逻辑
    }
}
```

### 6.8 总结对比表

| 对比维度 | 接口 | 抽象类 | 选择建议 |
|---------|------|-------|---------|
| 设计目的 | 定义行为规范 | 定义通用功能 | 看需求 |
| 继承方式 | 多继承 | 单继承 | 需要多继承用接口 |
| 成员变量 | 只有常量 | 可以有变量 | 需要变量用抽象类 |
| 构造方法 | 没有 | 有 | 需要构造用抽象类 |
| 方法实现 | 只有默认和静态方法 | 可以有普通方法 | 需要复用代码用抽象类 |
| 使用频率 | 高 | 中 | 接口更灵活 |
| 设计模式 | 策略、观察者等 | 模板方法等 | 根据模式选择 |

---

## 7. 实战示例

### 7.1 接口实现多态

```java
// 定义接口
public interface Payment {
    void pay(double amount);
}

// 实现类
public class Alipay implements Payment {
    @Override
    public void pay(double amount) {
        System.out.println("支付宝支付：" + amount + "元");
    }
}

public class WechatPay implements Payment {
    @Override
    public void pay(double amount) {
        System.out.println("微信支付：" + amount + "元");
    }
}

// 使用
public class Shop {
    public void checkout(Payment payment, double amount) {
        payment.pay(amount);
    }
    
    public static void main(String[] args) {
        Shop shop = new Shop();
        shop.checkout(new Alipay(), 100);
        shop.checkout(new WechatPay(), 200);
    }
}
```

### 7.2 接口回调机制

```java
// 定义回调接口
public interface OnDataLoadedListener {
    void onDataLoaded(String data);
    void onFailed(String error);
}

// 异步任务类
public class DataLoader {
    public void loadData(String url, OnDataLoadedListener listener) {
        try {
            // 模拟加载数据
            String data = "加载的数据";
            listener.onDataLoaded(data);
        } catch (Exception e) {
            listener.onFailed(e.getMessage());
        }
    }
}

// 使用回调
public class MainActivity {
    public static void main(String[] args) {
        DataLoader loader = new DataLoader();
        loader.loadData("http://example.com/api", new OnDataLoadedListener() {
            @Override
            public void onDataLoaded(String data) {
                System.out.println("数据加载成功：" + data);
            }
            
            @Override
            public void onFailed(String error) {
                System.out.println("数据加载失败：" + error);
            }
        });
    }
}
```

### 7.3 抽象类与接口组合

```java
// 接口定义行为
public interface Drawable {
    void draw();
}

// 抽象类提供部分实现
public abstract class Shape implements Drawable {
    protected String color;
    
    public Shape(String color) {
        this.color = color;
    }
    
    public String getColor() {
        return color;
    }
    
    public abstract double getArea();
    
    @Override
    public void draw() {
        System.out.println("绘制" + color + "形状");
    }
}

// 具体实现
public class Circle extends Shape {
    private double radius;
    
    public Circle(String color, double radius) {
        super(color);
        this.radius = radius;
    }
    
    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
    
    @Override
    public void draw() {
        System.out.println("绘制" + color + "圆形");
    }
}

public class Rectangle extends Shape {
    private double width;
    private double height;
    
    public Rectangle(String color, double width, double height) {
        super(color);
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double getArea() {
        return width * height;
    }
    
    @Override
    public void draw() {
        System.out.println("绘制" + color + "矩形");
    }
}
```

---

## 8. 注意事项

### 8.1 接口使用注意事项
1. 接口不能实例化，只能通过实现类使用
2. 接口中的常量是全局的，任何类都可以访问
3. 接口的静态方法不能被重写，只能通过接口名调用
4. 接口的私有方法只能在接口内部使用

### 8.2 实现接口注意事项
1. 必须重写所有抽象方法，否则该类必须声明为抽象类
2. 多个接口的默认方法冲突时，必须显式重写
3. 类的成员方法优先于接口的默认方法
4. 接口的常量可以通过实现类访问，但不能修改

### 8.3 设计注意事项
1. 接口应该小而专，遵循单一职责原则
2. 避免在接口中定义过多方法
3. 优先使用组合而非继承
4. 接口应该稳定，避免频繁修改

---

## 9. 总结

### 9.1 核心要点
1. **接口是规范**：定义行为契约
2. **多继承支持**：一个类可以实现多个接口
3. **多态性**：接口引用指向不同实现类
4. **解耦设计**：分离定义和实现
5. **可扩展性**：易于扩展和维护

### 9.2 选择指南
- 需要定义行为规范 → 使用接口
- 需要共享代码 → 使用抽象类
- 需要多继承 → 使用接口
- 需要构造方法 → 使用抽象类
- 需要解耦设计 → 使用接口

### 9.3 设计原则
1. **面向接口编程**：依赖抽象而非具体实现
2. **单一职责原则**：接口应该小而专
3. **开闭原则**：对扩展开放，对修改关闭
4. **依赖倒置原则**：依赖抽象，不依赖具体

掌握接口的使用是Java编程的重要基础，能够帮助你写出更灵活、可维护的代码！