Java 中 **switch 表达式**（switch expression）从 Java 14 正式标准化（之前是 preview）。它和传统 `switch` 语句的区别是：**switch 表达式本身会产生一个值**，可用于赋值、`return`、方法参数等；分支常用 `->`，代码块中用 `yield` 返回值。

下面按常见形式列举并给出代码。

---

## 1. 基本形式：箭头标签直接返回值

```java
enum Day { MON, TUE, WED, THU, FRI, SAT, SUN }

Day day = Day.WED;

int letters = switch (day) {
    case MON, FRI, SUN -> 6;
    case TUE -> 7;
    case THU, SAT -> 8;
    case WED -> 9;
};
// letters = 9
```

说明：

- `case MON, FRI, SUN ->` 支持多个常量标签。
- 箭头标签**不会贯穿**，不需要 `break`。
- 枚举所有常量都覆盖时，可以没有 `default`；否则 switch 表达式必须穷尽，通常要写 `default`。

---

## 2. 用在赋值、return、方法参数中

```java
int code = switch (status) {
    case "OK" -> 200;
    case "NOT_FOUND" -> 404;
    default -> 500;
};

return switch (type) {
    case "A" -> 1;
    case "B" -> 2;
    default -> 0;
};

System.out.println(switch (n) {
    case 1 -> "one";
    default -> "other";
});
```

说明：switch 表达式是表达式，可以出现在任何需要值的位置。

---

## 3. 代码块 + `yield` 返回值

当分支需要多条语句时，用 `{ ... }`，并通过 `yield` 返回该分支的值。

```java
String s = "hello";

int len = switch (s) {
    case "one" -> 3;
    case "two" -> {
        int n = s.length();
        yield n;
    }
    default -> {
        if (s.isEmpty()) {
            yield 0;
        }
        yield s.length();
    }
};
```

说明：

- `yield` 只用于 switch 表达式的代码块中，表示“该分支产生这个值”。
- 不能用 `break value` 从 switch 表达式返回值。
- 代码块中所有可能执行到的路径都必须 `yield` 或 `throw`。

---

## 4. 分支中直接 `throw`

```java
int code = switch (status) {
    case "OK" -> 200;
    case "ERROR" -> {
        logError();
        yield 500;
    }
    default -> throw new IllegalArgumentException("unknown status: " + status);
};
```

说明：`throw` 可以作为 switch 表达式的一个分支，因为它不产生正常返回值。

---

## 5. 传统冒号标签 + `yield`（合法，但一般推荐箭头）

switch 表达式也可以使用冒号标签，但代码块中要用 `yield` 返回值。

```java
int x = switch (day) {
    case MON:
    case TUE:
        yield 1;
    case WED:
        yield 2;
    default:
        yield 0;
};
```

说明：

- 冒号标签仍有传统 switch 的贯穿语义。
- 实际项目中更推荐 `->`，更简洁且不易出错。

---

## 6. 选择器类型

传统常量标签的 switch 支持：

- `byte`、`short`、`char`、`int`
- 对应包装类：`Byte`、`Short`、`Character`、`Integer`
- `String`
- `enum`
- Java 21 起支持模式匹配的 switch 表达式，可对引用类型使用类型模式、`when` 守卫、`case null` 等。

不支持直接用 `long`、`float`、`double`、`boolean` 作为传统常量 switch 的选择器。

---

## 7. Java 21 模式匹配 switch 表达式

Java 21 中 switch 支持类型模式、守卫条件和 `case null`：

```java
static String describe(Object obj) {
    return switch (obj) {
        case null -> "null";
        case Integer i when i > 0 -> "positive int: " + i;
        case Integer i -> "int: " + i;
        case String s -> "string: " + s;
        default -> "other: " + obj;
    };
}
```

说明：

- `case Integer i when i > 0` 是带守卫条件的模式。
- `case null` 用于显式处理 `null`；传统 switch 遇到 `null` 通常会抛 `NullPointerException`。
- 对 sealed 类层次等，编译器可检查穷尽性，满足穷尽时可省略 `default`。

---

## 8. 嵌套 switch 表达式

```java
int r = switch (a) {
    case 1 -> switch (b) {
        case 2 -> 3;
        default -> 4;
    };
    default -> 0;
};
```

说明：switch 表达式可以嵌套，因为内层本身也是一个表达式。

---

## 关键点总结

- switch 表达式必须产生值，常用于赋值、`return`、方法实参。
- 箭头标签 `->` 不贯穿；多语句块用 `yield` 返回值。
- switch 表达式必须穷尽：枚举覆盖全部常量可不写 `default`；`int`、`String` 等通常需要 `default`。
- `yield` 用于 switch 表达式代码块返回值，不是 `return`。
- Java 21 支持模式匹配、`when` 守卫、`case null`。
