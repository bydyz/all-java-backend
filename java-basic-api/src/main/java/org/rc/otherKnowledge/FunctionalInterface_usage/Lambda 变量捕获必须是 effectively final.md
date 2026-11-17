## 规则表述

Java 中 Lambda 表达式（以及匿名内部类）捕获外部**局部变量、方法参数、异常参数**时，该变量必须是 `final` 或 **effectively final**。

常见表述：

- 编译器错误：`local variables referenced from a lambda expression must be final or effectively final`
- JLS 大意：Lambda 体中使用但未在 Lambda 内声明的局部变量、形式参数、异常参数，必须声明为 `final` 或实际上是 final，否则在使用处发生编译时错误。
- 通俗表述：Lambda 只能“读取”一个值不会再变的局部变量；不能捕获一个之后还会被重新赋值的局部变量。

**effectively final**：变量声明后只初始化一次，之后再也没有被赋值、复合赋值、自增/自减等修改。注意：即使修改发生在 Lambda 表达式之后，也会使该变量不再是 effectively final。

---

## 代码示例

### 1. 合法：变量只初始化一次，未再修改

```java
int count = 0; // 之后没有修改，因此是 effectively final
Runnable r = () -> System.out.println(count);
r.run(); // 输出 0
```

虽然没有写 `final`，但 `count` 是 effectively final，可以捕获。

---

### 2. 非法：Lambda 后修改变量

```java
int count = 0;
Runnable r = () -> System.out.println(count); // 编译错误
count++; // 因为这里修改了 count，导致 count 不是 effectively final
```

编译器看整个作用域，不是看运行时顺序。只要 `count` 在 Lambda 外被再次赋值，Lambda 捕获它就非法。

---

### 3. 非法：先修改再捕获

```java
int count = 0;
count++;
Runnable r = () -> System.out.println(count); // 编译错误
```

`count++` 使 `count` 不再是 effectively final。

---

### 4. 合法：显式声明为 `final`

```java
final int base = 10;
java.util.function.IntUnaryOperator op = x -> x + base;
System.out.println(op.applyAsInt(5)); // 15
```

显式 `final` 一定可以捕获。

---

### 5. 方法参数：未重新赋值才合法

```java
void ok(int x) {
    Runnable r = () -> System.out.println(x); // 合法：x 未被重新赋值
    r.run();
}

void bad(int x) {
    x++;
    Runnable r = () -> System.out.println(x); // 编译错误：x 不是 effectively final
}
```

---

### 6. 普通 `for` 循环变量通常不能捕获

```java
for (int i = 0; i < 3; i++) {
    Runnable r = () -> System.out.println(i); // 编译错误：i 被 i++ 修改
}
```

增强 `for` 每轮迭代的循环变量通常可以捕获，因为每轮相当于一个新的变量：

```java
for (String s : java.util.List.of("a", "b")) {
    Runnable r = () -> System.out.println(s); // 通常合法
    r.run();
}
```

但如果在循环体内重新给 `s` 赋值，也会破坏 effectively final。

---

### 7. 捕获的是字段时不受此限制

Lambda 捕获的是 `this`，访问的是实例字段，不是捕获局部变量：

```java
class Counter {
    private int count = 0;

    void inc() {
        Runnable r = () -> count++; // 合法：count 是实例字段
        r.run();
    }
}
```

静态字段同理。

---

### 8. 捕获数组引用：引用不能变，但元素可变

```java
int[] box = {0};
Runnable r = () -> box[0]++; // 合法：box 引用是 effectively final
r.run();
System.out.println(box[0]); // 1
```

但下面非法：

```java
int[] box = {0};
Runnable r = () -> System.out.println(box[0]); // 编译错误
box = new int[]{1}; // box 被重新赋值，不再是 effectively final
```

---

## 原因简述

Lambda 捕获局部变量时，本质上是把变量的值复制到 Lambda 内部使用。若允许外部变量继续变化，Lambda 内部看到的值和外部变量就会不一致，因此 Java 要求被捕获的局部变量必须是 `final` 或 effectively final。

如果确实需要在 Lambda 中修改外部状态，可以使用：

- 实例字段/静态字段；
- 数组或可变对象的一个元素/状态；
- `AtomicInteger` 等并发安全容器。

例如：

```java
java.util.concurrent.atomic.AtomicInteger count =
        new java.util.concurrent.atomic.AtomicInteger();

Runnable r = () -> count.incrementAndGet(); // 合法：count 引用未变
r.run();
```
