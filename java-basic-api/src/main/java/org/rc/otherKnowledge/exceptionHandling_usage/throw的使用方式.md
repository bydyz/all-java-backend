# Java 中 `throw` 的使用方式详解

`throw` 是 Java 中**主动抛出异常**的关键字。它和 `throws`（声明异常）容易混淆，但职责完全不同：`throw` 是"动作"，`throws` 是"声明"。

---

## 一、基本语法

```java
throw <Throwable 类型的表达式>;
```

规则：
1. `throw` 后面必须是 `Throwable` 或其子类的**实例**（不能是类名、不能是字符串、不能是 null 字面量意义上的"空"）。
2. `throw` 语句本身是一个**语句**（Java 14 起也可用于 `switch` 箭头右侧），执行后控制流立即跳出当前方法（除非被上层 `catch` 捕获）。
3. 抛出 **checked 异常**时，方法必须声明 `throws` 或用 `try-catch` 处理；抛出 **unchecked 异常**（`RuntimeException` / `Error`）无此要求。

```java
public void demo() {
    throw new IllegalArgumentException("参数不合法");   // unchecked，无需声明
}
```

对比下面这段**编译错误**的代码，说明类型限制：

```java
throw "错误信息";          // ❌ 编译错误：不兼容的类型 String
throw new String("x");     // ❌ 编译错误
throw null;                // ⚠️ 编译通过，但运行时抛 NullPointerException
```

`throw null` 是个经典陷阱：编译器不报错，但 JVM 在执行 `throw` 时会检查表达式是否为 null，为 null 则抛出 `NullPointerException`。

---

## 二、抛出内置异常

最常见的用法是在参数校验、状态检查处直接抛出 `RuntimeException` 的子类：

```java
public class UserService {

    public User findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id 必须为正数: " + id);
        }
        User user = repository.get(id);
        if (user == null) {
            throw new NoSuchElementException("用户不存在: " + id);
        }
        return user;
    }

    public void withdraw(BigDecimal amount) {
        if (amount == null) {
            throw new NullPointerException("amount 不能为 null");
        }
        if (balance.compareTo(amount) < 0) {
            throw new IllegalStateException("余额不足");   // 状态不满足
        }
        this.balance = balance.subtract(amount);
    }
}
```

### 抛出 checked 异常

```java
public void readFile(String path) throws IOException {   // 必须声明
    File file = new File(path);
    if (!file.exists()) {
        throw new FileNotFoundException("文件不存在: " + path);
    }
    try (FileInputStream in = new FileInputStream(file)) {
        in.readAllBytes();
    }
}
```

如果方法内 `throw` 了 checked 异常却既不声明也不捕获，**编译不通过**：

```java
public void bad() {
    throw new IOException("x");   // ❌ 编译错误：未处理的异常类型 IOException
}
```

---

## 三、抛出自定义异常

### 1. 自定义 unchecked 异常（推荐用于业务校验）

```java
public class BizException extends RuntimeException {
    private final int code;

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() { return code; }
}
```

使用：

```java
public void transfer(Account from, Account to, BigDecimal money) {
    if (money.compareTo(BigDecimal.ZERO) <= 0) {
        throw new BizException(40001, "转账金额必须大于 0");
    }
    if (from.getBalance().compareTo(money) < 0) {
        throw new BizException(40002, "余额不足");
    }
    // ...
}
```

### 2. 自定义 checked 异常（调用方必须显式处理）

```java
public class InsufficientStockException extends Exception {
    public InsufficientStockException(String msg) {
        super(msg);
    }
}

public class OrderService {
    public void placeOrder(Item item, int qty) throws InsufficientStockException {
        if (item.getStock() < qty) {
            throw new InsufficientStockException(
                String.format("库存不足，需要 %d，剩余 %d", qty, item.getStock()));
        }
        item.setStock(item.getStock() - qty);
    }
}
```

调用方就必须处理：

```java
try {
    orderService.placeOrder(item, 10);
} catch (InsufficientStockException e) {
    log.warn("下单失败: {}", e.getMessage());
}
```

---

## 四、`throw` vs `throws`

| 维度 | `throw` | `throws` |
|---|---|---|
| 位置 | 方法体 / 构造器 / 静态块 / Lambda 体内部 | 方法签名末尾、构造器签名末尾 |
| 作用 | **实际抛出一个异常对象** | **声明**该方法可能抛出哪些异常 |
| 数量 | 一条语句只能抛一个 | 可同时声明多个，用逗号分隔 |
| 后面跟什么 | 异常**实例** | 异常**类名** |
| 执行时机 | 运行时 | 编译期约定 |

```java
public void foo() throws IOException, SQLException {   // 声明
    if (x) {
        throw new IOException("io 错误");                 // 抛出
    }
    if (y) {
        throw new SQLException("db 错误");
    }
}
```

---

## 五、结合 `try-catch-finally`

### 1. 被上层捕获

```java
public void outer() {
    try {
        inner();
    } catch (BizException e) {
        System.out.println("捕获业务异常: " + e.getMessage());
    }
}

private void inner() {
    throw new BizException(50000, "内部错误");
}
```

### 2. 包装后重抛（异常链）

保留原始原因 `cause`，避免丢失堆栈：

```java
public void loadConfig() {
    try {
        Files.readAllLines(Path.of("config.yml"));
    } catch (IOException e) {
        throw new IllegalStateException("配置加载失败", e);   // 保留 cause
    }
}
```

打印 `e.printStackTrace()` 时会输出 `Caused by: java.io.IOException ...`。

### 3. finally 中 throw 会吞掉原异常（陷阱）

```java
public void danger() {
    try {
        throw new RuntimeException("原始异常");
    } finally {
        throw new RuntimeException("finally 里的异常");   // 原始异常被丢弃
    }
}
```

**结论**：`finally` 里不要 `throw`，也不要 `return`，否则会覆盖 `try` 块中的异常/返回值。

---

## 六、`throw` 后的"不可达代码"

`throw` 之后的代码在同一控制流上不可达，编译器会报错：

```java
public int f() {
    throw new RuntimeException("x");
    // return 1;   ❌ 编译错误：无法访问的语句
}
```

但如果在条件分支里，则后续代码依然可达：

```java
public int f(int a) {
    if (a < 0) {
        throw new IllegalArgumentException();
    }
    return a * 2;   // ✅ 合法
}
```

另外，**方法返回类型与 throw 无关**：`int` 方法里只 `throw` 不 `return` 是合法的，因为 `throw` 意味着永远不会正常返回。

---

## 七、精确重抛（Precise Rethrow，Java 7+）

当 `catch` 捕获父类型后原样 `throw e`，编译器会做类型推断，只按 `try` 中**可能抛出的具体类型**来检查 `throws` 声明，而不是按 `catch` 参数的静态类型。

```java
public void process() throws IOException, SQLException {
    try {
        if (flag) {
            throw new IOException("io");
        } else {
            throw new SQLException("sql");
        }
    } catch (Exception e) {
        throw e;   // ✅ Java 7+ 允许：编译器推断只可能是 IOException 或 SQLException
    }
}
```

在 Java 6 及以前，这段代码会报错（因为 `catch (Exception e)` 的 `e` 静态类型是 `Exception`，需要 `throws Exception`）。注意：精确重抛要求 `catch` 参数是 **final 或 effectively final**（即 `catch` 块内没有对它重新赋值）。

---

## 八、`throw` 在 Lambda / 方法引用 / `switch` 表达式中的使用

### 1. Lambda 体中抛出

```java
Supplier<String> supplier = () -> {
    throw new UnsupportedOperationException("不支持");
};

list.forEach(x -> {
    if (x == null) throw new IllegalArgumentException("元素不能为空");
    process(x);
});
```

> 注意：Lambda 中不能直接抛 checked 异常，除非目标函数式接口的方法声明了 `throws`（如 `Callable`）。

```java
Callable<String> c = () -> { throw new IOException("io"); };   // ✅ Callable.call() 声明了 throws Exception
Runnable r = () -> { throw new IOException("io"); };           // ❌ Runnable.run() 未声明
```

### 2. 作为 `switch` 表达式的分支（Java 14+）

```java
public String level(int score) {
    return switch (score / 10) {
        case 10, 9 -> "A";
        case 8 -> "B";
        case 7 -> "C";
        default -> throw new IllegalArgumentException("非法分数: " + score);
    };
}
```

### 3. 结合 `Optional`

```java
User user = optionalUser.orElseThrow(
    () -> new BizException(404, "用户不存在"));
```

`orElseThrow` 内部就是用 `throw` 抛出了供应商提供的异常。

---

## 九、`throw` 在构造器、静态初始化块中

```java
public class Connection {
    private final Socket socket;

    public Connection(String host, int port) {
        if (host == null) {
            throw new IllegalArgumentException("host 不能为 null");
        }
        try {
            this.socket = new Socket(host, port);
        } catch (IOException e) {
            throw new UncheckedIOException("连接失败", e);
        }
    }
}
```

```java
public class Config {
    private static final Properties PROPS;

    static {
        PROPS = new Properties();
        try {
            PROPS.load(new FileInputStream("app.properties"));
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);   // 静态块中抛异常，类初始化失败
        }
    }
}
```

静态初始化块中抛出异常，会导致该类首次被使用时抛 `ExceptionInInitializerError`（再之后访问会抛 `NoClassDefFoundError`）。

---

## 十、常见陷阱汇总

| 陷阱 | 说明 |
|---|---|
| `throw null;` | 编译通过，运行时抛 `NPE` |
| `throw "msg";` | 编译错误，必须是 `Throwable` 实例 |
| `finally` 中 `throw` | 会覆盖 `try` 中原异常，导致排查困难 |
| 抛异常却不带 `cause` | `throw new RuntimeException("x")` 丢失原始堆栈，应写成 `throw new RuntimeException("x", e)` |
| 用异常控制正常流程 | 异常开销大且语义不清，非异常情况应返回值或 `Optional` |
| 抛 `Error` 或继承 `Throwable` | 一般不应主动 `throw new Error()`；业务异常应继承 `RuntimeException`（非受检）或 `Exception`（受检） |
| 在 `catch` 中吞掉再抛新异常不带 `cause` | 会丢失根因 |
| `throw e` 但 `catch` 参数被重新赋值 | 破坏精确重抛，需声明 `throws Exception` |

---

## 十一、完整示例串起来

```java
public class OrderService {

    public Order createOrder(Long userId, List<Item> items) {
        // 1. 参数校验 —— throw unchecked
        if (userId == null) {
            throw new IllegalArgumentException("userId 不能为 null");
        }
        if (items == null || items.isEmpty()) {
            throw new BizException(40001, "商品列表不能为空");
        }

        // 2. 业务校验 —— throw 自定义异常
        for (Item it : items) {
            if (it.getStock() < it.getQty()) {
                throw new BizException(40002,
                    "库存不足: " + it.getName());
            }
        }

        // 3. 依赖调用 —— 包装底层异常重抛
        try {
            Order order = repository.save(new Order(userId, items));
            return order;
        } catch (DataAccessException e) {
            throw new BizException(50001, "订单保存失败", e);   // 保留 cause
        }
    }
}
```

---

### 一句话总结

`throw` 用于**在代码中主动产生异常**：语法是 `throw new XxxException(...)`；必须抛出 `Throwable` 子类实例；受检异常需配合 `throws` 或 `try-catch`；业务中优先用自定义异常并**保留 `cause`**；`finally` 中避免 `throw`；`throw` 之后的代码在该分支上不可达。
