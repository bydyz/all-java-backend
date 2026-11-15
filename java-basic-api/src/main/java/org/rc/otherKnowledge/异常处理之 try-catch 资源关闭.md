# Java 异常处理中的资源关闭

Java 中“在异常处理中关闭资源”主要有两种方式：

1. **传统方式：`try-catch-finally`，在 `finally` 中手动关闭资源**
2. **推荐方式：`try-with-resources`（Java 7+），自动关闭资源**

核心原则：**资源必须在 `finally` 中关闭，或交给 `try-with-resources` 自动关闭；不要只在 `try` 块末尾关闭，因为异常发生时可能执行不到。**

---

## 1. 传统方式：`try-catch-finally` 手动关闭

资源需要判空，`close()` 本身也可能抛异常，需要单独处理，避免关闭异常覆盖原始业务异常。

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TraditionalCloseDemo {
    public static void main(String[] args) {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader("a.txt"));
            String line = br.readLine();
            System.out.println(line);
        } catch (IOException e) {
            System.out.println("读取文件异常: " + e.getMessage());
        } finally {
            // 资源必须判空
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // close 异常单独处理，避免覆盖 try 中的原始异常
                    System.out.println("关闭资源异常: " + e.getMessage());
                }
            }
        }
    }
}
```

说明：

- `finally` 通常一定会执行，适合释放资源。
- `close()` 可能抛异常，所以 `finally` 中还要 `try-catch`。
- 如果 `finally` 中直接写 `br.close()` 且不捕获异常，`close()` 异常可能覆盖原始异常。
- 多个资源手动关闭时，应**逆序关闭**，并且每个资源单独 `try-catch`，避免一个关闭失败导致后面的资源无法关闭。

```java
Resource r1 = null;
Resource r2 = null;

try {
    r1 = new Resource();
    r2 = new Resource();
    // 使用资源
} catch (Exception e) {
    // 处理异常
} finally {
    // 后创建的先关闭
    if (r2 != null) {
        try {
            r2.close();
        } catch (Exception e) {
            // 记录关闭异常
        }
    }

    if (r1 != null) {
        try {
            r1.close();
        } catch (Exception e) {
            // 记录关闭异常
        }
    }
}
```

---

## 2. 推荐方式：`try-with-resources` 自动关闭

只要资源实现了 `AutoCloseable` 或 `Closeable`，就可以写在 `try(...)` 中，Java 会自动调用 `close()`。

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TryWithResourcesDemo {
    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader("a.txt"))) {
            String line = br.readLine();
            System.out.println(line);
        } catch (IOException e) {
            System.out.println("读取或关闭资源异常: " + e.getMessage());
        }
    }
}
```

说明：

- `BufferedReader` 实现了 `Closeable`，因此可以自动关闭。
- 不需要显式写 `finally` 关闭。
- 即使 `try` 块中抛异常，也会自动关闭资源。
- `try-with-resources` 也可以配合 `catch`、`finally` 使用。

```java
try (BufferedReader br = new BufferedReader(new FileReader("a.txt"))) {
    System.out.println(br.readLine());
} catch (IOException e) {
    System.out.println("处理异常: " + e.getMessage());
} finally {
    System.out.println("业务清理逻辑");
}
```

---

## 3. 多个资源：按声明逆序自动关闭

多个资源用分号分隔，关闭顺序与声明顺序相反：**后声明的先关闭**。

```java
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MultiResourceDemo {
    public static void main(String[] args) {
        try (
            FileInputStream in = new FileInputStream("in.txt");
            FileOutputStream out = new FileOutputStream("out.txt")
        ) {
            int data;
            while ((data = in.read()) != -1) {
                out.write(data);
            }
        } catch (IOException e) {
            System.out.println("IO 异常: " + e.getMessage());
        }
    }
}
```

关闭顺序：

```text
先关闭 out
再关闭 in
```

这符合资源依赖关系，通常后创建的资源依赖先创建的资源。

---

## 4. 自定义资源实现 `AutoCloseable`

```java
class MyResource implements AutoCloseable {

    public void work() throws Exception {
        throw new Exception("work error");
    }

    @Override
    public void close() throws Exception {
        throw new Exception("close error");
    }
}

public class CustomResourceDemo {
    public static void main(String[] args) {
        try (MyResource r = new MyResource()) {
            r.work();
        } catch (Exception e) {
            System.out.println("主异常: " + e.getMessage());

            for (Throwable suppressed : e.getSuppressed()) {
                System.out.println("被抑制异常: " + suppressed.getMessage());
            }
        }
    }
}
```

可能输出：

```text
主异常: work error
被抑制异常: close error
```

说明：

- `try` 块中业务异常是主异常。
- `close()` 抛出的异常不会覆盖主异常，而是通过 `addSuppressed` 作为**被抑制异常**保存。
- 可以通过 `e.getSuppressed()` 获取关闭资源时产生的异常。
- 如果业务代码没有异常，而 `close()` 抛异常，则 `close()` 异常会成为主异常。

---

## 5. 两种方式对比

| 方式 | 是否自动关闭 | 是否推荐 | 说明 |
|---|---|---|---|
| `try-catch-finally` 手动关闭 | 否 | 老代码常见 | 需要判空、捕获 `close()` 异常、注意关闭顺序 |
| `try-with-resources` | 是 | 推荐 | 代码简洁，自动处理关闭和异常抑制 |

---

## 6. 注意事项

1. **资源必须实现 `AutoCloseable` 才能用于 `try-with-resources`。**
2. `Closeable` 继承自 `AutoCloseable`，其 `close()` 只抛 `IOException`；`AutoCloseable.close()` 可抛 `Exception`。
3. 多个资源关闭顺序是**逆序**。
4. 传统 `finally` 中关闭多个资源时，每个 `close()` 最好单独 `try-catch`。
5. `finally` 中不要随意 `return`，否则可能吞掉异常或覆盖返回值。
6. Java 9+ 支持对 effectively final 变量使用 `try-with-resources`：

```java
BufferedReader br = new BufferedReader(new FileReader("a.txt"));
try (br) {
    System.out.println(br.readLine());
}
```

---

总结：**新代码优先使用 `try-with-resources`；老代码或特殊场景使用 `try-catch-finally`，并确保在 `finally` 中安全关闭资源。**
