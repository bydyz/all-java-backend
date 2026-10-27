# Java 中使用 `super` 调用父类构造器

## 一、基本语法

`super(参数列表)` 用于在**子类构造器**中显式调用**直接父类**的某个构造器。

```java
class Animal {
    String name;
    Animal(String name) {
        this.name = name;
    }
}

class Dog extends Animal {
    int age;
    Dog(String name, int age) {
        super(name);      // 调用父类 Animal(String) 构造器
        this.age = age;   // 之后才能给子类字段赋值
    }
}
```

注意：`super(...)` 只能调到**直接父类**的构造器，不存在 `super.super(...)`。

---

## 二、两种调用方式

### 1. 隐式调用（默认）

如果子类构造器**没有**显式写 `super(...)` 或 `this(...)`，编译器会自动插入 `super();`，即调用父类**无参构造器**。

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

### 2. 显式调用

当父类**没有无参构造器**，或需要传入特定参数时，必须显式调用：

```java
class A {
    A(int x) { }
    // 没有无参构造器
}

class B extends A {
    B() {
        super(10);   // 必须显式调用，否则编译错误
    }
}
```

---

## 三、核心注意事项

### 1. 必须是构造器的第一条语句

`super(...)` 之前不能有任何语句（注释和空白除外）。

```java
Dog(String name) {
    this.name = name;   // ❌ 编译错误
    super(name);
}
```

原因：父类构造器必须先执行完，父类状态初始化完成后，子类才能安全地使用继承来的成员。

### 2. 与 `this(...)` 互斥

`super(...)` 和 `this(...)` 都要求是第一条语句，所以**一个构造器中只能二选一**。

```java
class B extends A {
    B() {
        this(1);
        super();    // ❌ 编译错误
    }
    B(int x) {
        super(x);
    }
}
```

### 3. 参数表达式不能引用实例成员

在 `super(...)` 的参数中，不能引用当前类或父类的实例变量、实例方法，也不能使用 `this` / `super` 表达式——因为此时父类构造器尚未执行。

```java
class B extends A {
    int y = 5;

    B() {
        super(y);        // ❌ 编译错误: cannot reference y before supertype constructor has been called
        super(foo());    // ❌ 同理，foo() 隐式引用了 this
    }
    int foo() { return 1; }
}
```

可以引用**静态成员、局部变量、外部参数、常量**等。

### 4. 父类没有无参构造器时必须显式调用

这是最常见的编译错误来源：

```java
class A {
    A(String s) { }
}

class B extends A {
    B() { }   // ❌ 编译错误：A 中没有无参构造器
}
```

### 5. 受检异常的传播

如果父类构造器声明了受检异常，子类构造器必须声明抛出它——因为 `super(...)` 不能放进 `try-catch`（它必须是第一条语句）。

```java
class A {
    A() throws IOException { }
}

class B extends A {
    B() throws IOException {   // 必须声明
        super();
    }

    // ❌ 不能这样写：
    // B() {
    //     try { super(); } catch (IOException e) { }
    // }
}
```

### 6. 不能调用父类的 `private` 构造器

`private` 构造器对子类不可见（除非子类是父类的嵌套类，编译器会生成合成访问器）。

### 7. 不能在静态上下文中使用

`super(...)` 只能出现在构造器中，不能在静态方法、静态初始化块中。

### 8. 不能在普通方法中调用父类构造器

普通方法里只能使用 `super.method()` 调用父类方法，`super(...)` 构造器调用只属于构造器。

```java
void doSomething() {
    super();          // ❌ 编译错误
    super.method();   // ✅ 合法
}
```

### 9. 一次构造器链中父类构造器只执行一次

`super(...)` 在一个构造器里只能出现一次，并且每个对象的父类构造器只会沿着继承链执行一次（通过 `this(...)`
链转交给其他构造器时，最终仍只会调用一次 `super(...)`）。

### 10. 子类继承父类时，不会继承父类的构造器。只能通过“super(形参列表)”的方式调用父类指定的构造器。

### 11. 如果在子类构造器的首行既没有显示调用"this(形参列表)"，也没有显式调用"super(形参列表)"， 则子类此构造器默认调用"super()"，即调用父类中空参的构造器。

引申结论：

* 子类的任何一个构造器中，要么会调用本类中重载的构造器，要么会调用父类的构造器。 只能是这两种情况之一。
* 一个类中声明有 n 个构造器，最多有 n-1 个构造器中使用了"this(形参列表)"，则剩下的那个一定使用"super(形参列表)"。

---

## 四、几个特殊场景

| 场景              | 说明                                                                   |
|-----------------|----------------------------------------------------------------------|
| **枚举**          | 枚举构造器**不能**显式调用 `super(...)` 或 `this(...)`（JLS 8.9.2）                |
| **record**      | 紧凑构造器不能显式调用 `super(...)`，record 隐式继承 `java.lang.Record`              |
| **匿名内部类**       | 不能写构造器，父类构造器参数通过 `new Foo(args) { ... }` 传入                          |
| **构造器中调用可重写方法** | 父类构造器里调用被子类重写的方法会触发动态分派，此时子类字段尚未初始化，容易产生 bug —— 这是 `super` 相关的一个经典陷阱 |

```java
class A {
    A() { print(); }          // 调用的是 B.print()
    void print() { System.out.println("A"); }
}

class B extends A {
    int x = 10;
    B() { super(); }
    @Override
    void print() {
        System.out.println("B, x=" + x);  // 此时 x 还是 0
    }
}
// new B() 输出: "B, x=0"
```

---

## 五、小结

| 要点   | 规则                    |
|------|-----------------------|
| 位置   | 必须是构造器第一条语句           |
| 互斥   | 不能与 `this(...)` 同时出现  |
| 隐式   | 未显式调用时自动插入 `super()`  |
| 前提   | 父类必须有无参构造器，否则必须显式调用   |
| 参数限制 | 不能引用 `this`、实例成员、实例方法 |
| 异常   | 父类构造器的受检异常必须由子类构造器声明  |
| 作用域  | 只在构造器中可用，只调直接父类       |

记住一句话：**"先父后子，第一行，不能碰 this"**——把握住这三点，绝大多数 `super` 使用问题都能避开。
