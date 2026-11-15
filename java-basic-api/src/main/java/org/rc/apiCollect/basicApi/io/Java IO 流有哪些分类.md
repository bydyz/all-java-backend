Java IO 流的分类体系虽然类目繁多，但遵循一套清晰、规整的规则。理解这套分类规则，比单纯记忆类名更重要。下面从**三个最核心的维度**来拆解，并在每一类中附上可运行的代码示例。

---

### 按流的方向：输入流 vs. 输出流

这是最基础的理解：**输入（Input）** 是将数据从外部（文件、网络等）读入到程序内存中；**输出（Output）** 是将程序内存中的数据写出到外部。

这个分类是相对于**程序**而言的。

#### 输入流代码示例（`FileInputStream`）

```java
import java.io.FileInputStream;
import java.io.IOException;

public class InputDemo {
    public static void main(String[] args) {
        // try-with-resources 自动关闭流
        try (FileInputStream fis = new FileInputStream("source.txt")) {
            int data;
            // read() 返回读取的字节（0-255），末尾返回 -1
            while ((data = fis.read()) != -1) {
                System.out.print((char) data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```


#### 输出流代码示例（`FileOutputStream`）

```java
import java.io.FileOutputStream;
import java.io.IOException;

public class OutputDemo {
    public static void main(String[] args) {
        String content = "Hello, Java IO!";
        try (FileOutputStream fos = new FileOutputStream("target.txt")) {
            // 字符串转为字节数组后写出
            fos.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```


---

### 按数据单位：字节流 vs. 字符流

这是 Java IO 中**最重要的区分**，直接关系到“会不会乱码”和“能处理什么文件”。

| 维度 | **字节流** | **字符流** |
| :--- | :--- | :--- |
| **基本单位** | 字节（8 bit） | 字符（16 bit，Java 内部 Unicode） |
| **顶层抽象类** | `InputStream` / `OutputStream` | `Reader` / `Writer` |
| **适用场景** | **所有类型**文件（图片、音频、视频、二进制） | **纯文本**文件（`.txt`、`.java`、`.properties`） |
| **编码处理** | 不处理编码，原样传输字节 | 内部依赖编码表（如 UTF-8），自动完成字节↔字符转换 |

**核心区别**：字节流“看到什么就传什么”，是原始数据的搬运工；字符流在字节流的基础上加了一层“编码解码”逻辑，适合处理人类可读的文本。

#### 字节流处理非文本文件（以复制图片为例）

```java
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ByteCopyDemo {
    public static void main(String[] args) {
        try (FileInputStream fis = new FileInputStream("photo.jpg");
             FileOutputStream fos = new FileOutputStream("photo_copy.jpg")) {
            byte[] buffer = new byte[4096];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```


#### 字符流处理文本文件（以读取文本为例）

```java
import java.io.FileReader;
import java.io.IOException;

public class CharReadDemo {
    public static void main(String[] args) {
        try (FileReader reader = new FileReader("note.txt")) {
            char[] buffer = new char[1024];
            int len;
            while ((len = reader.read(buffer)) != -1) {
                System.out.print(new String(buffer, 0, len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

> **一个容易忽略的细节**：字符流内部使用了缓冲区。如果只写字符流却不关闭或 `flush()`，数据可能滞留缓冲区而未真正写出到文件。


---

### 按功能/角色：节点流 vs. 处理流

这是理解 Java IO “装饰器模式”设计的关键。

- **节点流**：直接连接数据源或目的地（如文件、数组、管道），是“**源头**”或“**终点**”。例如 `FileInputStream`、`FileOutputStream`、`FileReader`、`FileWriter`。
- **处理流**：不直接连接数据源，而是**包装**另一个已有的流（节点流或处理流），在它的基础上增加功能（如缓冲、编码转换、打印格式化）。**构造方法必须接收另一个流对象作为参数**。

```java
// 节点流：直接连文件
FileInputStream fis = new FileInputStream("data.bin");

// 处理流：包装节点流，增加缓冲功能
BufferedInputStream bis = new BufferedInputStream(fis);
```

处理流是 Java IO 灵活性的来源，通过层层包装，可以用少量基础流组合出丰富的行为。

#### 处理流示例一：缓冲流（`BufferedInputStream` / `BufferedOutputStream`）

缓冲流在内存中维护一个缓冲区，减少直接与磁盘（或网络）交互的次数，从而大幅提升读写性能。

```java
import java.io.*;

public class BufferedCopyDemo {
    public static void main(String[] args) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream("input.dat"));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("output.dat"))) {
            byte[] buf = new byte[8192];
            int len;
            while ((len = bis.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```


#### 处理流示例二：转换流（`InputStreamReader` / `OutputStreamWriter`）

转换流是**字节流与字符流之间的“适配器”**。当需要指定编码来读写文本时，必须使用转换流，而不能依赖平台默认编码。

```java
import java.io.*;
import java.nio.charset.StandardCharsets;

public class ConvertStreamDemo {
    public static void main(String[] args) {
        // 以 UTF-8 编码读取文件（将字节流转换为字符流）
        try (InputStreamReader isr = new InputStreamReader(
                new FileInputStream("utf8.txt"), StandardCharsets.UTF_8)) {
            int ch;
            while ((ch = isr.read()) != -1) {
                System.out.print((char) ch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

> `FileReader` / `FileWriter` 在底层其实也是通过转换流实现的，但它们使用平台默认编码，**在跨平台场景中容易出现乱码**。需要明确指定编码时，请直接使用 `InputStreamReader` / `OutputStreamWriter`。


#### 处理流示例三：打印流（`PrintStream` / `PrintWriter`）

打印流提供了便捷的 `print` / `println` / `printf` 方法，将各种类型的数据格式化后写出，`System.out` 本质上就是一个 `PrintStream`。

```java
import java.io.*;

public class PrintStreamDemo {
    public static void main(String[] args) {
        try (PrintStream ps = new PrintStream(
                new FileOutputStream("log.txt"), true, "UTF-8")) {
            ps.println("用户登录成功");
            ps.printf("耗时：%d ms%n", 120);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

> 第二个参数 `true` 表示**自动 flush**，每次写入后立即刷新缓冲区，避免数据滞留。


#### 处理流示例四：对象流（`ObjectOutputStream` / `ObjectInputStream`）

对象流用于**对象的序列化与反序列化**：将内存中的 Java 对象转换为字节序列保存到文件或网络，也可以从字节序列恢复对象。被序列化的类必须实现 `java.io.Serializable` 接口（这是一个标记接口，不含任何方法）。

```java
import java.io.*;

class User implements Serializable {
    private static final long serialVersionUID = 1L;
    String name;
    int age;
    transient String password; // transient 修饰的字段不会被序列化

    User(String name, int age, String password) {
        this.name = name;
        this.age = age;
        this.password = password;
    }
}

public class SerializationDemo {
    public static void main(String[] args) {
        User user = new User("Alice", 28, "secret");

        // 序列化：对象 → 文件
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("user.ser"))) {
            oos.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 反序列化：文件 → 对象
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("user.ser"))) {
            User restored = (User) ois.readObject();
            System.out.println(restored.name + ", " + restored.age
                    + ", password=" + restored.password); // password 为 null
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
```


---

### 总结：四大抽象基类与装饰器模式

Java IO 的 40 多个类，本质上都是从**四个抽象基类**派生而来，整个体系非常规整：

| 抽象基类 | 方向 | 数据单位 |
| :--- | :--- | :--- |
| `InputStream` | 输入 | 字节 |
| `OutputStream` | 输出 | 字节 |
| `Reader` | 输入 | 字符 |
| `Writer` | 输出 | 字符 |

处理流的层层包装，背后是**装饰器模式（Decorator Pattern）** 的应用：`FilterInputStream` / `FilterOutputStream` 是字节流装饰器的核心基类，`BufferedInputStream`、`DataInputStream` 等都是具体的装饰器，在不改变原始流结构的前提下，动态地为其添加缓冲、数据类型读写等能力。

掌握“**方向 → 单位 → 角色**”这三个维度，再结合装饰器模式的理解，面对任意一个 Java IO 类，都能快速判断它的定位和使用场景。
