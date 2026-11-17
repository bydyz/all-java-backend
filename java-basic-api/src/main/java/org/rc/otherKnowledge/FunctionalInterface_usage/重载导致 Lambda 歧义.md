核心原因：**Lambda 表达式本身没有独立类型**，它是“多表达式（poly expression）”，必须靠上下文的目标类型来确定。重载解析时，如果多个重载方法的参数都是函数式接口，并且这个 Lambda 同时兼容多个候选，而编译器又无法用“最具体方法”规则选出唯一一个，就会报类似：

```text
reference to xxx is ambiguous
both method xxx(...) and method xxx(...) match
```

下面列举常见会导致 Lambda 歧义的重载场景。

---

## 1. 两个不同函数式接口，抽象方法签名完全相同

```java
@FunctionalInterface
interface A {
    void run();
}

@FunctionalInterface
interface B {
    void run();
}

static void call(A a) {}
static void call(B b) {}

public static void main(String[] args) {
    // call(() -> {}); // 编译错误：reference to call is ambiguous
}
```

原因：`() -> {}` 同时可以匹配 `A.run()` 和 `B.run()`，两个接口之间又没有子类型关系，编译器无法选择。

消除方式：显式指定目标类型。

```java
call((A) () -> {});
```

---

## 2. 返回类型不同，但 Lambda 同时兼容

例如 `Predicate<String>` 和 `Function<String, Boolean>`：

```java
import java.util.function.*;

static void exec(Predicate<String> p) {}
static void exec(Function<String, Boolean> f) {}

public static void main(String[] args) {
    // exec(s -> true); // 编译错误：ambiguous
}
```

原因：

- `Predicate<String>.test(String)` 返回 `boolean`
- `Function<String, Boolean>.apply(String)` 返回 `Boolean`
- `s -> true` 对两者都兼容，`boolean` 可以装箱为 `Boolean`

消除方式：

```java
exec((Predicate<String>) s -> true);
// 或
exec((Function<String, Boolean>) s -> true);
```

---

## 3. 基本类型函数式接口 vs 包装类型函数式接口

```java
import java.util.function.*;

static void check(IntPredicate p) {}
static void check(Predicate<Integer> p) {}

public static void main(String[] args) {
    // check(x -> x > 0); // 编译错误：ambiguous
}
```

原因：

- `IntPredicate.test(int)` 返回 `boolean`
- `Predicate<Integer>.test(Integer)` 返回 `boolean`
- `x -> x > 0` 对两者都兼容，涉及自动装箱/拆箱

消除方式：

```java
check((IntPredicate) x -> x > 0);
// 或
check((Predicate<Integer>) x -> x > 0);
```

---

## 4. 不同接口但“形状”相同：`Supplier` vs `Callable`

```java
import java.util.function.Supplier;
import java.util.concurrent.Callable;

static void submit(Supplier<String> s) {}
static void submit(Callable<String> c) {}

public static void main(String[] args) {
    // submit(() -> "ok"); // 编译错误：ambiguous
}
```

原因：

- `Supplier<String>.get()`：无参，返回 `String`
- `Callable<String>.call()`：无参，返回 `String`，允许抛 `Exception`
- `() -> "ok"` 同时兼容两者，且二者没有子类型关系

消除方式：

```java
submit((Supplier<String>) () -> "ok");
// 或
submit((Callable<String>) () -> "ok");
```

---

## 5. 事件监听器 vs `Consumer`

```java
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

static void on(ActionListener l) {}
static void on(Consumer<ActionEvent> c) {}

public static void main(String[] args) {
    // on(e -> {}); // 编译错误：ambiguous
}
```

原因：

- `ActionListener.actionPerformed(ActionEvent)`：一个参数，返回 `void`
- `Consumer<ActionEvent>.accept(ActionEvent)`：一个参数，返回 `void`
- `e -> {}` 同时匹配两者

消除方式：

```java
on((Consumer<ActionEvent>) e -> {});
```

---

## 6. 方法引用也可能出现类似歧义

Method Reference 和 Lambda 一样需要目标类型，因此也可能歧义：

```java
import java.util.function.Supplier;
import java.util.concurrent.Callable;

static String make() {
    return "ok";
}

static void handle(Supplier<String> s) {}
static void handle(Callable<String> c) {}

public static void main(String[] args) {
    // handle(Test::make); // 可能编译错误：ambiguous
}
```

消除方式同样是显式强转目标函数式接口：

```java
handle((Supplier<String>) Test::make);
```

---

## 7. 非歧义对照：有“更具体”的候选时会选出来

如果两个函数式接口存在子类型关系，通常不会歧义。例如：

```java
import java.util.function.Function;
import java.util.function.UnaryOperator;

static void run(Function<String, String> f) {}
static void run(UnaryOperator<String> u) {}

public static void main(String[] args) {
    run(s -> s); // 选择 UnaryOperator<String>
}
```

因为 `UnaryOperator<String>` 是 `Function<String, String>` 的子接口，编译器会选择更具体的重载。

---

## 8. 注意：同一函数式接口不同泛型参数的重载通常会擦除冲突

下面这种不是 Lambda 歧义，而是方法擦除冲突：

```java
import java.util.function.Function;

static void f(Function<String, Integer> fn) {}
// static void f(Function<CharSequence, Integer> fn) {} // 编译错误：same erasure
```

两个方法擦除后都是 `f(Function)`，不能作为重载存在。

---

## 总结

重载导致 Lambda 歧义的典型条件是：

1. 多个重载参数都是函数式接口；
2. Lambda 同时兼容多个函数式接口；
3. 这些函数式接口之间没有子类型/更具体关系；
4. 编译器无法通过最具体方法规则选出唯一候选。

常见消除方式：

```java
call((A) () -> {});
exec((Predicate<String>) s -> true);
check((IntPredicate) x -> x > 0);
submit((Supplier<String>) () -> "ok");
on((Consumer<ActionEvent>) e -> {});
```

其中**强转 Lambda 到目标函数式接口**最直接；显式写 Lambda 参数类型只有在能排除其他候选时才有效。
