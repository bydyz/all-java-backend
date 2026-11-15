在 Java 中，通过 `ClassLoader` 获取资源流的核心方法是 `getResourceAsStream(String name)`。它返回一个 `InputStream`，如果资源不存在，则返回 `null`。理解其路径规则，并厘清它与 `Class.getResourceAsStream` 的差异，是正确使用的关键。

### 核心方法：`ClassLoader.getResourceAsStream`

`ClassLoader.getResourceAsStream` 始终**以 Classpath 根目录为基准**来定位资源。

**路径规则**：传入的 `name` 是**相对于 Classpath 根的路径**，并且**不能以斜杠 `/` 开头**。如果以 `/` 开头，通常会返回 `null`，因为类加载器不会自动去除这个前缀。

**代码示例**：

假设项目结构如下（Maven/Gradle 标准布局）：
```text
src/main/resources/
├── app.properties          (位于 Classpath 根)
└── config/
    └── db.cfg              (位于 config 包下)
```

通过 `ClassLoader` 获取这两个文件的流，写法如下：
```java
ClassLoader classLoader = MyClass.class.getClassLoader();

// 获取 Classpath 根下的 app.properties
InputStream rootStream = classLoader.getResourceAsStream("app.properties");

// 获取 config 包下的 db.cfg (路径使用 / 分隔，不带前导斜杠)
InputStream configStream = classLoader.getResourceAsStream("config/db.cfg");

// 错误写法：以 / 开头，很可能返回 null
InputStream wrongStream = classLoader.getResourceAsStream("/app.properties");
```
需要注意，在 JAR 包或 WAR 包部署环境下，**不能**先用 `getResource` 拿到 `URL`，再通过 `new File(url.getFile())` 去读取文件，因为资源在归档文件中时，路径转换会失败。直接使用 `getResourceAsStream` 是更安全、稳定的方式。

### 关键辨析：`ClassLoader` 与 `Class` 的 `getResourceAsStream` 有何不同？

`Class` 类也提供了一个同名方法 `Class.getResourceAsStream(String name)`。它的行为**取决于路径是否以 `/` 开头**，并且最终会委托给 `ClassLoader` 执行。

根据 `Class.java` 的源码逻辑，`Class.getResourceAsStream` 在内部会对路径进行“预处理”（`resolveName`）：
- 如果路径**不以 `/` 开头**：会在路径前自动拼接当前类所在的包名（将点 `.` 替换为 `/`），使其成为一个相对于**当前类所在包**的路径。
- 如果路径**以 `/` 开头**：会去除这个前导 `/`，使其成为一个相对于 **Classpath 根**的路径，从而等价于 `ClassLoader.getResourceAsStream`。

三者的等价关系可以总结为：
```java
// 假设当前类是 com.example.Demo
// 以下三种写法等效，最终都会定位到 Classpath 根下的 com/example/config.xml
Demo.class.getResourceAsStream("config.xml");          // 相对于包 com.example
Demo.class.getResourceAsStream("/com/example/config.xml"); // 绝对路径，最终去掉 /
Demo.class.getClassLoader().getResourceAsStream("com/example/config.xml"); // 相对于 Classpath 根
```

**选择建议**：如果你希望资源路径相对于**当前类所在包**，使用 `Class.getResourceAsStream("filename")` 更便捷；如果你希望路径始终以 **Classpath 根**为基准，使用 `ClassLoader.getResourceAsStream("path/from/root")` 语义更明确。在不确定类加载器行为的环境下，`Class.getResourceAsStream` 通常是一个稳妥的选择，因为它会使用加载该类本身的类加载器。

### 实战中必须注意的要点

**1. 返回值可能为 `null`，必须判空**
当资源不存在时，`getResourceAsStream` 会返回 `null`。如果不加判断直接调用 `read()` 或 `load()`，会抛出 `NullPointerException`。这是最常见的错误之一。

**2. 使用 try-with-resources 确保流关闭**
获取到的 `InputStream` 需要被关闭。推荐使用 try-with-resources 语法，它能保证在代码块结束时自动关闭流，避免资源泄漏。

**3. 注意字符编码**
如果你读取的是文本资源（尤其是包含非 ASCII 字符的 `.properties` 文件），默认的 `Properties.load(InputStream)` 使用 ISO-8859-1 编码，可能导致中文乱码。应当使用指定字符集的 `Reader` 进行包装：
```java
try (InputStream is = classLoader.getResourceAsStream("config/app.properties")) {
    if (is == null) {
        throw new IllegalArgumentException("配置文件未找到");
    }
    Properties props = new Properties();
    // 显式指定 UTF-8 编码
    props.load(new InputStreamReader(is, StandardCharsets.UTF_8));
    // ... 使用 props
}
```

**4. 谨慎使用 `ClassLoader.getSystemResourceAsStream`**
这是一个静态方法，它强制使用**系统类加载器**来查找资源。在 Web 容器（如 Tomcat）或模块化环境中，系统类加载器可能“看不到”应用自身 Classpath 下的资源，导致返回 `null`。通常应优先使用当前类的类加载器（即 `SomeClass.class.getClassLoader()`），而不是系统类加载器。

**5. 在特定场景下获取上下文类加载器**
在某些框架或线程环境中（例如，代码运行在由不同类加载器加载的线程中），可以使用 `Thread.currentThread().getContextClassLoader()` 来获取资源流。这种方式在需要与线程上下文保持一致的类加载视野时是有用的补充手段。

### 总结
使用 `ClassLoader` 获取资源流的核心可以归纳为三点：**路径以 Classpath 根为基准且不带前导 `/`**；**务必判空并用 try-with-resources 关闭流**；**区分它与 `Class.getResourceAsStream` 在路径语义上的不同**。在涉及文本读取时，显式指定字符集可以避免许多不必要的编码问题。
