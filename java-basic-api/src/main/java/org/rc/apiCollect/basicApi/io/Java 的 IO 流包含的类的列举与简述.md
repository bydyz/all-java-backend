Java 的 I/O 流体系建立在四个抽象基类之上：`InputStream`、`OutputStream`、`Reader`、`Writer`。所有具体流类都由此派生，按**数据单位**可分为**字节流**与**字符流**，按**角色**可分为直接连接数据源的**节点流**和包装在其他流之上的**处理流**。

下面按类别逐一说明主要流类及其功能，并附上可运行的代码示例。

---

## 一、字节流（以 8 位字节为单位，适用于一切二进制数据）

### 1. `FileInputStream` / `FileOutputStream`

**功能**：节点流，直接以文件为数据源或目的地，读写原始字节。适用于图片、音频、视频等二进制文件，也可用于文本文件（但需自行处理编码）。

```java
// 字节流复制文件（一次读一个字节，效率较低，仅作演示）
try (FileInputStream fis = new FileInputStream("source.jpg");
     FileOutputStream fos = new FileOutputStream("dest.jpg")) {
    int b;
    while ((b = fis.read()) != -1) {
        fos.write(b);
    }
}
```

### 2. `BufferedInputStream` / `BufferedOutputStream`

**功能**：处理流，为底层字节流提供内存缓冲区，减少直接 I/O 次数，显著提升读写效率。构造时传入一个已存在的 `InputStream` / `OutputStream`。

```java
// 缓冲字节流复制文件——推荐方式
try (InputStream in = new BufferedInputStream(new FileInputStream("source.jpg"));
     OutputStream out = new BufferedOutputStream(new FileOutputStream("dest.jpg"))) {
    byte[] buf = new byte[8192];
    int len;
    while ((len = in.read(buf)) != -1) {
        out.write(buf, 0, len);
    }
}
```

### 3. `DataInputStream` / `DataOutputStream`

**功能**：处理流，允许按 Java 基本数据类型（`int`、`double`、`boolean`、`UTF` 字符串等）读写字节流，写出与读入的顺序必须一致。

```java
try (DataOutputStream dos = new DataOutputStream(
        new BufferedOutputStream(new FileOutputStream("data.bin")))) {
    dos.writeInt(42);
    dos.writeDouble(3.14);
    dos.writeUTF("hello");
}
try (DataInputStream dis = new DataInputStream(
        new BufferedInputStream(new FileInputStream("data.bin")))) {
    int i = dis.readInt();
    double d = dis.readDouble();
    String s = dis.readUTF();
}
```

### 4. `ObjectInputStream` / `ObjectOutputStream`

**功能**：处理流，实现对象的**序列化**与**反序列化**。被序列化的类必须实现 `Serializable` 接口；`transient` 修饰的字段不会被序列化。

```java
class Student implements Serializable {
    private String name;
    private int age;
    // 构造器、getter/setter 略
}

// 序列化
try (ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream("student.ser"))) {
    oos.writeObject(new Student("小魏", 20));
}
// 反序列化
try (ObjectInputStream ois = new ObjectInputStream(
        new FileInputStream("student.ser"))) {
    Student s = (Student) ois.readObject();
}
```

### 5. `ByteArrayInputStream` / `ByteArrayOutputStream`

**功能**：节点流，数据源或目的地不是文件而是**内存中的字节数组**。常用于在内存中临时缓存字节数据，或对字节数组进行流式处理，无需关闭。

```java
ByteArrayOutputStream baos = new ByteArrayOutputStream();
baos.write("hello".getBytes());
byte[] data = baos.toByteArray();   // 取出内存中积累的全部字节

ByteArrayInputStream bais = new ByteArrayInputStream(data);
int b;
while ((b = bais.read()) != -1) {
    System.out.print((char) b);
}
```

### 6. `PipedInputStream` / `PipedOutputStream`

**功能**：用于**线程间**的管道通信，一个线程向 `PipedOutputStream` 写入，另一个线程从 `PipedInputStream` 读取。两个流必须建立连接（构造时传入对方，或调用 `connect()`）。

```java
PipedOutputStream pos = new PipedOutputStream();
PipedInputStream pis = new PipedInputStream(pos);

new Thread(() -> {                       // 写线程
    try { pos.write("pipe message".getBytes()); pos.close(); }
    catch (IOException e) { e.printStackTrace(); }
}).start();

new Thread(() -> {                       // 读线程
    try {
        int b;
        while ((b = pis.read()) != -1) System.out.print((char) b);
    } catch (IOException e) { e.printStackTrace(); }
}).start();
```

### 7. `PrintStream`

**功能**：字节打印流，提供 `print`、`println`、`printf` 等便捷格式化输出方法。`System.out` 本身就是 `PrintStream` 实例。

```java
try (PrintStream ps = new PrintStream(new FileOutputStream("out.txt"))) {
    ps.println("整数: " + 42);
    ps.printf("浮点数: %.2f%n", 3.14159);
}
```

---

## 二、字符流（以 16 位字符为单位，专用于文本数据）

### 8. `FileReader` / `FileWriter`

**功能**：节点字符流，直接读写文本文件。字符流在内部会处理编码转换（使用平台默认字符集），适合 `.txt`、`.java` 等文本文件。`FileWriter` 构造时可传入 `append` 参数控制追加或覆盖。

```java
// 读文本
try (FileReader fr = new FileReader("hello.txt")) {
    char[] cbuf = new char[1024];
    int len;
    while ((len = fr.read(cbuf)) != -1) {
        System.out.print(new String(cbuf, 0, len));
    }
}
// 写文本（追加模式）
try (FileWriter fw = new FileWriter("hello.txt", true)) {
    fw.write("追加一行\n");
}
```

### 9. `BufferedReader` / `BufferedWriter`

**功能**：处理流，为字符流提供缓冲。`BufferedReader` 独有的 `readLine()` 方法可以按行读取文本，是逐行处理文本文件的首选方式；`BufferedWriter` 提供 `newLine()` 写入平台相关的换行符。

```java
try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
    String line;
    while ((line = br.readLine()) != null) {
        System.out.println(line);
    }
}
try (BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))) {
    bw.write("第一行");
    bw.newLine();
    bw.write("第二行");
}
```

### 10. `PrintWriter`

**功能**：字符打印流，与 `PrintStream` 类似，但面向字符流。支持 `println`、`printf`，且可以设置自动刷新（构造时 `autoFlush = true`）。

```java
try (PrintWriter pw = new PrintWriter(new FileWriter("out.txt"), true)) {
    pw.println("Hello");
    pw.printf("Pi = %.3f%n", Math.PI);
}
```

---

## 三、转换流（字节流 ↔ 字符流的桥梁）

### 11. `InputStreamReader` / `OutputStreamWriter`

**功能**：处理流，将**字节流**转换为**字符流**。这是解决编码问题的关键工具：可以在构造时显式指定字符集（如 `UTF-8`、`GBK`），从而控制解码/编码行为。不指定时使用平台默认字符集。

```java
// 以 UTF-8 读取字节文件为字符（显式指定编码，避免平台默认编码不一致导致乱码）
try (BufferedReader br = new BufferedReader(
        new InputStreamReader(new FileInputStream("input.txt"), "UTF-8"))) {
    String line;
    while ((line = br.readLine()) != null) {
        System.out.println(line);
    }
}
// 以 GBK 写出字符到字节文件
try (Writer w = new OutputStreamWriter(
        new FileOutputStream("output_gbk.txt"), "GBK")) {
    w.write("中文内容");
}
```

> **注意**：读和写如果使用不同编码，会导致乱码。转换流的核心价值就在于让你显式控制编码，而不是依赖平台默认值。

---

## 四、NIO（New I/O，Java 7+ 的现代文件 API）

`java.nio.file` 包中的 `Path` 和 `Files` 提供了比传统 `java.io.File` 更简洁、更丰富的文件操作，`Files` 类封装了大量静态工具方法。

### 12. `Path` / `Files`

**功能**：`Path` 表示文件路径，`Files` 提供读写、复制、移动、删除、遍历等一站式操作。常用的 `Files.readString()`、`Files.readAllLines()`、`Files.write()` 等方法在处理中小型文本文件时非常简洁。

```java
Path path = Paths.get("input.txt");

// 读取全部内容为字符串（Java 11+）
String content = Files.readString(path, StandardCharsets.UTF_8);

// 按行读取
List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

// 写入
Files.write(Paths.get("out.txt"),
        List.of("line1", "line2"),
        StandardCharsets.UTF_8);

// 复制
Files.copy(Paths.get("source.txt"), Paths.get("dest.txt"),
        StandardCopyOption.REPLACE_EXISTING);

// 删除（文件不存在也不抛异常）
Files.deleteIfExists(Paths.get("temp.txt"));
```

**与传统 IO 的定位差异**：传统 `java.io` 流式 API 仍然是逐字节/逐字符处理、网络编程、以及需要精细控制读写过程的场景的基础；NIO 的 `Path`/`Files` 更适合常规文件系统操作（读取、写入、复制、遍历），代码更短、不易泄漏资源。

---

### 附：常用流类的选择速查

| 场景 | 推荐类 |
|---|---|
| 复制二进制文件（图片、音视频） | `BufferedInputStream` + `BufferedOutputStream` |
| 逐行读取文本 | `BufferedReader` + `FileReader`（或 `InputStreamReader` 指定编码） |
| 写入格式化文本 | `PrintWriter` 或 `BufferedWriter` |
| 需要控制编码 | `InputStreamReader` / `OutputStreamWriter` |
| 对象持久化 | `ObjectOutputStream` / `ObjectInputStream` |
| 内存中临时缓存字节 | `ByteArrayInputStream` / `ByteArrayOutputStream` |
| 线程间传递字节数据 | `PipedInputStream` / `PipedOutputStream` |
| 现代简洁文件操作 | `Files` + `Path`（NIO.2） |
