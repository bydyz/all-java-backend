`java.io.File` 是 Java 中操作文件系统的核心类。但理解它的关键第一步，是明确一个核心边界：**`File` 类只负责文件和目录的“元操作”（如路径、属性、创建、删除），它本身不能读写文件内容**。真正的读写工作，需要配合 `InputStream` / `OutputStream` 等 IO 流来完成。

---

### 📌 核心特点

- **抽象路径表示**：`File` 对象是文件或目录**路径名的抽象表示**。它并不保证对应的路径在磁盘上真实存在，即使文件不存在，`File` 对象依然可以被成功创建。
- **不可变性**：一旦创建了 `File` 对象，它所代表的抽象路径名就是**不可变的**，无法被修改。
- **平台无关**：`File` 类提供了与平台无关的路径处理方法，其内部使用系统相关的分隔符，并建议通过 `File.separator` 来获取，以保证跨平台兼容性。
- **实现了特定接口**：`File` 类实现了 `Serializable` 和 `Comparable` 接口，因此 `File` 对象可以被序列化，并支持比较和排序。

---

### 🏗️ 构造器（共4个）

`File` 类提供了 4 个公共构造器，其核心区别在于**如何指定路径**，构造器本身**不会检查路径是否真实存在**。

| 构造器签名 | 说明 |
| :--- | :--- |
| `File(String pathname)` | 通过一个完整的路径字符串直接创建。 |
| `File(String parent, String child)` | 通过父路径字符串和子路径字符串拼接创建，避免手动处理分隔符。 |
| `File(File parent, String child)` | 父路径用 `File` 对象表示，子路径用字符串，适合链式操作。 |
| `File(URI uri)` | 通过 URI 创建，适用于更复杂的资源定位场景。 |

**代码示例**：

```java
import java.io.File;

public class FileConstructorDemo {
    public static void main(String[] args) {
        // 1. 通过完整路径字符串
        File file1 = new File("D:\\test\\demo.txt");

        // 2. 通过父路径字符串 + 子路径字符串
        File file2 = new File("D:\\test", "demo.txt");

        // 3. 通过父 File 对象 + 子路径字符串
        File parentDir = new File("D:\\test");
        File file3 = new File(parentDir, "demo.txt");

        System.out.println("file1 路径: " + file1.getPath());
        System.out.println("file2 路径: " + file2.getPath());
        System.out.println("file3 路径: " + file3.getPath());
    }
}
```


---

### ⚙️ 常用 API 使用方法

`File` 的 API 主要可分为 **判断、获取信息、创建/删除、目录操作** 四类。绝大多数操作类方法返回 `boolean`，表示操作是否成功。

#### 🧐 判断功能
用于在操作前进行预检，避免异常。

| 方法 | 说明 |
| :--- | :--- |
| `exists()` | 文件或目录是否存在 |
| `isFile()` | 是否是文件 |
| `isDirectory()` | 是否是目录 |
| `canRead()` / `canWrite()` / `canExecute()` | 是否可读 / 可写 / 可执行 |
| `isHidden()` | 是否是隐藏文件 |

```java
File file = new File("demo.txt");
System.out.println("是否存在: " + file.exists());
System.out.println("是否是文件: " + file.isFile());
System.out.println("是否可读: " + file.canRead());
System.out.println("是否可写: " + file.canWrite());
```


#### 📋 获取信息功能
用于获取路径、名称、大小等元数据。

| 方法 | 说明 |
| :--- | :--- |
| `getName()` | 获取文件名或目录名（不含路径） |
| `getPath()` | 返回构造时传入的路径（可能是相对路径） |
| `getAbsolutePath()` | 返回绝对路径 |
| `getParent()` / `getParentFile()` | 获取父目录路径（String / File） |
| `length()` | 文件大小（字节），目录返回不确定值 |
| `lastModified()` | 最后修改时间戳（毫秒） |

```java
File file = new File("dir", "demo.txt");
System.out.println("文件名: " + file.getName());          // demo.txt
System.out.println("构造路径: " + file.getPath());        // dir/demo.txt
System.out.println("绝对路径: " + file.getAbsolutePath()); // /.../dir/demo.txt
System.out.println("父目录: " + file.getParent());        // dir
System.out.println("大小(字节): " + file.length());
```


#### ✨ 创建与删除功能

| 方法 | 说明 |
| :--- | :--- |
| `createNewFile()` | **原子性**地创建一个新的空文件（文件已存在则返回 `false`）。需处理 `IOException`。 |
| `delete()` | 删除文件或**空目录**。 |
| `deleteOnExit()` | 请求在 JVM 退出时删除。 |

```java
File file = new File("newfile.txt");
try {
    if (file.createNewFile()) {
        System.out.println("文件创建成功");
    }
} catch (IOException e) {
    e.printStackTrace();
}
// ... 后续操作 ...
System.out.println("删除结果: " + file.delete());
```


#### 📂 目录操作功能

| 方法 | 说明 |
| :--- | :--- |
| `mkdir()` | 创建单级目录，父目录不存在则失败。 |
| `mkdirs()` | 创建多级目录，父目录不存在会一并创建（**推荐**）。 |
| `list()` | 返回目录下所有文件/子目录的**名称**数组（`String[]`）。 |
| `listFiles()` | 返回目录下所有内容的 **`File` 对象**数组（`File[]`），更常用。 |

```java
File dir = new File("a/b/c");
// 使用 mkdirs 递归创建多级目录
System.out.println("目录创建结果: " + dir.mkdirs());

// 遍历目录
File[] files = dir.listFiles();
if (files != null) {
    for (File f : files) {
        System.out.println(f.getName());
    }
}
```


---

### 💎 总结与关键提醒

- **职责单一**：牢记 `File` **只管“文件本身”**，内容读写必须交给 IO 流。
- **构造器不校验**：`new File(...)` 永远成功，路径是否存在需要用 `exists()` 判断。
- **删除限制**：`delete()` **只能删除空目录**。若要删除非空目录，需要递归删除其内部所有内容后再删除自身。
- **路径拼接**：拼接路径时，推荐使用 `File(parent, child)` 构造器或 `File.separator`，而不是硬编码 `\` 或 `/`，以保证跨平台兼容性。
