# Java Optional 类详解

Java 的 `Optional` 位于 `java.util` 包，是 Java 8 引入的 `final` 值类，用来表示“可能有值、也可能为空”的容器，常用于**方法返回值**，减少显式 `null` 判断和空指针异常。

## 1. Optional 常用公共方法列举

| 分类 | 方法 | 说明 |
|---|---|---|
| 创建 | `Optional.empty()` | 返回空 Optional |
| 创建 | `Optional.of(T value)` | 包装非 null 值；传入 null 会抛 `NullPointerException` |
| 创建 | `Optional.ofNullable(T value)` | value 为 null 返回 empty，否则返回包装值 |
| 判断 | `isPresent()` | 有值返回 true |
| 判断 | `isEmpty()` | 无值返回 true，Java 11+ |
| 获取 | `get()` | 有值则返回；空则抛 `NoSuchElementException`，不推荐直接使用 |
| 默认值 | `orElse(T other)` | 有值返回值，否则返回 other；other 会立即求值 |
| 默认值 | `orElseGet(Supplier<? extends T>)` | 有值返回值，否则调用 Supplier；惰性求值 |
| 异常 | `orElseThrow()` | 空则抛 `NoSuchElementException`，Java 10+ |
| 异常 | `orElseThrow(Supplier<? extends X>)` | 空则抛 Supplier 提供的异常 |
| 消费 | `ifPresent(Consumer<? super T>)` | 有值才执行 Consumer |
| 消费 | `ifPresentOrElse(Consumer, Runnable)` | 有值执行前者，空执行后者，Java 9+ |
| 转换 | `map(Function)` | 有值时转换；转换结果为 null 则返回 empty |
| 转换 | `flatMap(Function)` | 有值时转换，要求返回 Optional，用于避免 `Optional<Optional<T>>` |
| 过滤 | `filter(Predicate)` | 有值且满足条件才保留，否则返回 empty |
| 组合 | `or(Supplier<Optional>)` | 当前为空时返回 Supplier 提供的 Optional，Java 9+ |
| 流 | `stream()` | 有值返回单元素 Stream，空返回空 Stream，Java 9+ |
| 其他 | `equals/hashCode/toString` | 基于值语义；空为 `Optional.empty` |

## 2. 创建与判断

```java
import java.util.Optional;

Optional<String> empty = Optional.empty();
Optional<String> hello = Optional.of("hello");
Optional<String> nullable = Optional.ofNullable(null); // empty

// Optional.of(null); // 抛 NullPointerException

System.out.println(hello.isPresent()); // true
System.out.println(nullable.isEmpty()); // true，Java 11+
```

## 3. 获取值与默认值

```java
Optional<String> opt = Optional.ofNullable(getName());

// 不推荐：先 isPresent 再 get
if (opt.isPresent()) {
    System.out.println(opt.get());
}

// 推荐：提供默认值
String name1 = opt.orElse("默认名称");

// orElseGet 是惰性求值，只有为空时才调用
String name2 = opt.orElseGet(() -> createDefaultName());

// 为空时抛异常
String name3 = opt.orElseThrow(() -> new IllegalStateException("名称不存在"));

// Java 10+：为空抛 NoSuchElementException
// String name4 = opt.orElseThrow();
```

`orElse` 和 `orElseGet` 的区别：

```java
static String expensiveDefault() {
    System.out.println("计算默认值");
    return "default";
}

Optional.of("x").orElse(expensiveDefault());   // 会打印“计算默认值”
Optional.of("x").orElseGet(() -> expensiveDefault()); // 不会打印
```

## 4. 消费值

```java
Optional<String> opt = Optional.of("Tom");

opt.ifPresent(v -> System.out.println("值：" + v));

opt.ifPresentOrElse(
    v -> System.out.println("有值：" + v),
    () -> System.out.println("没有值")
); // Java 9+
```

## 5. 转换与过滤

```java
Optional<String> name = Optional.of("  Alice  ");

Optional<Integer> length = name
    .map(String::trim)
    .filter(s -> !s.isEmpty())
    .map(String::length);

System.out.println(length.orElse(0)); // 5
```

`map` 与 `flatMap`：

```java
Optional<String> s = Optional.of("42");

// map：如果 lambda 返回 Optional，会得到 Optional<Optional<Integer>>
Optional<Optional<Integer>> nested =
    s.map(v -> Optional.of(Integer.parseInt(v)));

// flatMap：自动展平，得到 Optional<Integer>
Optional<Integer> flat =
    s.flatMap(v -> Optional.of(Integer.parseInt(v)));
```

自定义 flatMap 示例：

```java
static Optional<Integer> parsePositiveInt(String s) {
    try {
        int n = Integer.parseInt(s);
        return n > 0 ? Optional.of(n) : Optional.empty();
    } catch (NumberFormatException e) {
        return Optional.empty();
    }
}

Optional<Integer> result =
    Optional.of("42").flatMap(OptionalDemo::parsePositiveInt);
```

## 6. 组合与 Stream

```java
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

Optional<String> a = Optional.empty();
Optional<String> b = a.or(() -> Optional.of("fallback")); // Java 9+

System.out.println(b.get()); // fallback

var list = Stream.of(
        Optional.of("a"),
        Optional.empty(),
        Optional.of("b")
    )
    .flatMap(Optional::stream) // Java 9+
    .collect(Collectors.toList());

System.out.println(list); // [a, b]
```

## 7. 链式取值示例

```java
class User {
    private Address address;
    public Address getAddress() { return address; }
}

class Address {
    private String city;
    public String getCity() { return city; }
}

static String getCity(User user) {
    return Optional.ofNullable(user)
        .map(User::getAddress)
        .map(Address::getCity)
        .orElse("未知城市");
}
```

## 8. 原始类型 Optional

Java 还提供了避免装箱的原始类型版本：

```java
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.OptionalDouble;

OptionalInt oi = OptionalInt.of(10);
OptionalLong ol = OptionalLong.empty();
OptionalDouble od = OptionalDouble.of(3.14);

int x = oi.orElse(0);
System.out.println(x); // 10
```

## 9. 使用注意

- `Optional` 主要用作**方法返回值**，表示“可能没有结果”。
- 通常不建议把 `Optional` 用作字段、方法参数、集合元素。
- 不要写 `optional.isPresent()` 后再 `get()` 的冗长代码，优先用 `map`、`flatMap`、`orElse`、`ifPresent` 等。
- `Optional` 没有实现 `Serializable`，不适合直接作为序列化字段。
- `Optional` 是值类，避免使用 `==` 比较，应使用 `equals`。
- `Optional.of(null)` 会抛异常；不确定是否为 null 时用 `Optional.ofNullable`。
