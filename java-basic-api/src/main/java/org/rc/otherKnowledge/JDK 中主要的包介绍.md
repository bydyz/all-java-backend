JDK 的包数量庞大（Java SE 21 的 `java.base` 模块本身就导出数十个包），但从**日常开发的覆盖面和依赖层次**
来看，真正构成主干的是少数几个包，其余包大多围绕这些主干做垂直或横向延伸。下面按“基石层 → 数据处理层 → 通信与集成层 →
模块化视图”展开。

---

## 基石层：每个 Java 程序都绕不开

### `java.lang`：语言本身的类库化

`java.lang` 是唯一**无需 `import` 即可使用**的包，它定义了 Java 语言在运行时的基本契约。

核心构成：

- **`Object`**：整个类层次结构的根，`equals`、`hashCode`、`toString`、`getClass` 等约定方法均源于此。
- **`Class`**：运行时的类元数据载体，反射的入口。
- **包装类**：`Integer`、`Long`、`Double`、`Boolean`、`Character` 等，在原始类型与引用类型之间架桥，并承载了装箱/拆箱的语义。
- **`String` / `StringBuilder` / `StringBuffer`**：字符串是不可变对象，`StringBuilder` 用于单线程拼接，`StringBuffer`
  保留同步语义。
- **`System` / `Runtime` / `ProcessBuilder` / `ClassLoader`**：系统级操作——标准流、环境变量、外部进程、类加载。
- **`Throwable` 体系**：`Exception` 与 `Error` 的分野，是 Java 异常处理模型的根基。

`java.lang` 的特点是**稳定性极高**，Java 语言规范中许多语义直接绑定到这个包中的类型（如 `Object` 的方法契约、`String`
的不可变性），因此它的演进非常克制。

### `java.lang.reflect` 与 `java.lang.invoke`：运行时的“第二套语言”

这两个包是 `java.lang` 的延伸，但解决的是完全不同的问题。

**`java.lang.reflect`** 提供**运行时自省**能力：在程序运行期间获取类的方法、字段、构造器、注解信息，并动态调用。JDK
原生动态代理（`Proxy` + `InvocationHandler`）就是基于它实现的——`Proxy.newProxyInstance`
在运行时生成一个实现指定接口的代理类，将调用转发给 `InvocationHandler`。Spring AOP 的 JDK 代理路径底层用的正是这套机制。

**`java.lang.invoke`** 则更底层：它提供 `MethodHandle`、`CallSite` 等原语，是 JVM 层面**动态调用**
的直接抽象。`invokedynamic` 指令的运行时支撑就在这个包里。日常业务代码很少直接使用，但它是 Lambda
表达式、字符串拼接（`StringConcatFactory`）以及 Kotlin/Scala 等 JVM 语言编译产物能够高效运行的底层基础设施。

两者的分工可以这样理解：**`reflect` 面向“我想要在运行时操作类”，`invoke` 面向“我想要在运行时高效地链接一个调用点”**。

---

## 数据处理层：集合、I/O 与时间

### `java.util`：集合框架的主场，但不限于集合

`java.util` 的核心是**Java 集合框架
**：`List`（`ArrayList`、`LinkedList`）、`Set`（`HashSet`、`TreeSet`）、`Map`（`HashMap`、`TreeMap`）、`Queue`/`Deque`（`ArrayDeque`、`PriorityQueue`
）构成日常数据组织的基本工具。

但 `java.util` 的覆盖面远不止集合：

- **`Arrays` / `Collections`**：静态工具方法，排序、查找、包装。
- **`Optional`**：显式表达“可能缺失”的容器，替代返回 `null` 的一种风格选择。
- **`Random` / `SplittableRandom`**：随机数生成。
- **`Base64`**：编码解码。
- **`Objects`**：`equals`、`hash`、`requireNonNull` 等空安全辅助。
- **遗留类**：`Date`、`Calendar`、`Vector`、`Hashtable` 仍在，但新代码应优先使用 `java.time` 和并发集合。

`java.util` 的一个关键设计特征是**接口与实现分离**：`List` 接口不关心底层是数组还是链表，`Map`
接口不关心哈希表还是红黑树。这种分离使得同一个算法可以作用于不同的数据结构，也是集合框架能够长期演进而不破坏兼容性的原因。

### `java.util.concurrent`：并发从“手写”走向“组装”

`java.util.concurrent`（JUC）是 Java 并发编程的分水岭。在它之前，并发主要依赖 `synchronized`、`wait/notify` 等原语；在它之后，并发更多是
**选用合适的并发构件并正确组装**。

JUC 的骨架大致分为五块：

| 板块        | 代表类型                                                              | 解决的问题             |
|-----------|-------------------------------------------------------------------|-------------------|
| **执行器**   | `ExecutorService`、`ThreadPoolExecutor`、`ScheduledExecutorService` | 线程池化，任务提交与生命周期管理  |
| **异步结果**  | `Future`、`CompletableFuture`                                      | 获取异步计算结果、编排多步异步流程 |
| **并发容器**  | `ConcurrentHashMap`、`CopyOnWriteArrayList`、`BlockingQueue` 系列     | 高并发下的安全集合访问       |
| **同步器**   | `CountDownLatch`、`Semaphore`、`CyclicBarrier`、`Phaser`             | 线程之间的协调与等待        |
| **原子类与锁** | `AtomicInteger`、`LongAdder`、`ReentrantLock`、`StampedLock`         | 无锁/细粒度锁编程         |

底层实现上，JUC 大量依赖 **CAS（Compare-And-Swap）** 和 `sun.misc.Unsafe`（在较新版本中逐步被 `VarHandle`
替代）来避免重量级锁的开销。`CompletableFuture` 的引入（Java 8）则把异步编程从“回调嵌套”推进到了可组合的声明式风格。

### `java.io` 与 `java.nio`：两代 I/O 模型

**`java.io`** 是经典的**流式阻塞 I/O**：`InputStream`/`OutputStream`（字节流）、`Reader`/`Writer`（字符流），配合 `Buffered*`
装饰器提升吞吐。`File` 类负责路径抽象，`Serializable`
接口提供对象序列化（但官方文档明确警告：反序列化不可信数据本质上是危险的，应严格校验）。`java.io` 的模型直观，适合简单文件读写和低并发连接场景。

**`java.nio`** 是 Java 1.4 引入的**面向缓冲区、通道和选择器**的 I/O 模型，核心抽象为：`Buffer`（数据容器，维护
position/limit/capacity）、`Channel`（双向 I/O 连接，如 `FileChannel`、`SocketChannel`）、`Selector`
（多路复用就绪选择）。`java.nio.file` 子包进一步用 `Path` 和 `Files` 替代了 `File` 的诸多笨拙之处，提供了更现代的路径操作和文件属性访问。

两代 I/O 的关系不是替代而是**分工**：`java.io` 适合简单同步场景；`java.nio` 的非阻塞能力是构建高并发网络服务器（Netty、Undertow
等）的基础。日常文件操作中，`Files.readAllBytes` / `Files.writeString` 这类便捷方法已经让 `nio.file` 成为更推荐的入口。

### `java.time`：日期时间的正解

`java.time`（JSR 310，Java 8 引入）取代了 `java.util.Date`/`Calendar` 的混乱模型。核心类型按**是否带时区**和**是否带时间**
两个维度划分：

- **`LocalDate` / `LocalTime` / `LocalDateTime`**：无时区的日期、时间或两者组合，适合表示“生日”“会议时间”等本地概念。
- **`Instant`**：时间线上的一个瞬时点（UTC 基准），适合时间戳和日志。
- **`ZonedDateTime`**：带时区规则的完整日期时间。
- **`Duration` / `Period`**：时间量（时分秒）与日期间隔（年月日）。

这个包的设计原则是**不可变**和**线程安全**，所有“修改”操作返回新对象。`java.time.format.DateTimeFormatter`
负责解析和格式化，`java.time.temporal` 提供字段和单位的底层调整能力。

---

## 通信与集成层

### `java.net`：网络编程的基石

`java.net` 提供两类抽象：**高层**的 `URL`/`URI`/`URLConnection`（面向资源访问），和**低层**的 `Socket`/`ServerSocket`
（面向连接）以及 `DatagramSocket`（UDP）。`InetAddress` 封装 IP 地址。这个包本身提供的是相对原语的网络能力，实际应用中常被
Netty、OkHttp 等库封装，但理解 `Socket` 和 `URLConnection` 的语义仍然是排查网络问题的基础。

Java 11 起新增的 `java.net.http` 模块（`HttpClient`）提供了现代化的 HTTP/2 和 WebSocket 客户端，与旧的 `HttpURLConnection`
并存，新项目应优先使用 `java.net.http`。

### `java.sql`：JDBC 核心 API

`java.sql` 定义了 Java 访问关系型数据库的标准接口：`DriverManager` 获取连接，`Connection`
代表会话，`Statement`/`PreparedStatement`/`CallableStatement` 执行 SQL，`ResultSet` 承载结果集。`javax.sql`
是其可选扩展（连接池、`DataSource`、分布式事务等）。

JDBC 的设计是**接口驱动**的：JDK 只定义契约，具体实现由数据库厂商的驱动提供。这种模式使得 Java 应用可以在不修改代码的情况下切换底层数据库（当然，SQL
方言差异仍需处理）。

### `java.text` 与 `java.math`

**`java.text`** 处理**与自然语言无关的文本、日期、数字和消息格式化
**：`DateFormat`、`NumberFormat`、`MessageFormat`、`Collator` 等。在 `java.time`
出现后，日期格式化的主力已迁移到 `DateTimeFormatter`，但 `java.text` 在数字格式化和国际化消息方面仍有不可替代的位置。

**`java.math`** 提供**任意精度算术**：`BigInteger` 用于任意精度整数，`BigDecimal`
用于任意精度十进制数（金融计算的标配，避免 `double` 的精度陷阱）。

### `javax.xml`（`java.xml` 模块）：JAXP

`java.xml` 模块定义 Java API for XML Processing（JAXP），涵盖 **DOM**（树形解析）、**SAX**（事件驱动流式解析）、**StAX**
（拉式流式解析）以及 **XSLT 转换**和**验证**能力。这个包在 Web 服务、配置文件和文档处理中仍有广泛应用，尽管 JSON 在许多场景下已部分取代
XML。

---

## 模块化视角：包的“上层建筑”

Java 9 引入模块系统后，JDK 的包不再是散落的命名空间，而是被组织进约 30+ 个标准模块中。其中 **`java.base` 是所有模块的根**
，自动被依赖，无需显式声明。

`java.base`
导出的核心包构成上面“基石层”和大部分“数据处理层”的内容：`java.lang`、`java.util`、`java.io`、`java.nio`、`java.net`、`java.math`、`java.text`、`java.time`、`java.security`
等。

其余模块按功能切分，例如：

- **`java.sql`**：JDBC API
- **`java.xml`**：JAXP
- **`java.desktop`**：AWT、Swing、JavaBeans、Image I/O 等桌面能力
- **`java.logging`**：`java.util.logging`
- **`java.management`**：JMX
- **`java.net.http`**：现代 HTTP 客户端
- **`java.compiler`**：注解处理与编译器 API

这种切分让 `jlink` 可以按需裁剪运行时镜像，也强制应用在 `module-info.java` 中显式声明依赖，使模块边界从“约定”变为“编译期约束”。

---

### 一张速查表

| 包 / 模块                   | 一句话定位           | 最该记住的类型                                                                   |
|--------------------------|-----------------|---------------------------------------------------------------------------|
| `java.lang`              | 语言运行时基座         | `Object`、`String`、`Integer`、`Throwable`、`System`                          |
| `java.lang.reflect`      | 运行时自省与动态代理      | `Class`、`Method`、`Proxy`、`InvocationHandler`                              |
| `java.lang.invoke`       | JVM 动态调用原语      | `MethodHandle`、`CallSite`                                                 |
| `java.util`              | 集合 + 通用工具       | `List`/`Map`/`Set`、`ArrayList`、`HashMap`、`Optional`                       |
| `java.util.concurrent`   | 并发构件库           | `ExecutorService`、`CompletableFuture`、`ConcurrentHashMap`、`BlockingQueue` |
| `java.io`                | 流式阻塞 I/O        | `InputStream`/`OutputStream`、`Reader`/`Writer`、`File`                     |
| `java.nio`               | 缓冲区 + 通道 + 多路复用 | `ByteBuffer`、`Channel`、`Selector`、`Path`/`Files`                          |
| `java.time`              | 现代日期时间          | `LocalDate`、`Instant`、`ZonedDateTime`、`Duration`                          |
| `java.net`               | 网络通信原语          | `Socket`、`ServerSocket`、`URL`、`HttpClient`（`java.net.http`）               |
| `java.sql`               | JDBC 核心         | `Connection`、`PreparedStatement`、`ResultSet`                              |
| `java.math`              | 任意精度算术          | `BigInteger`、`BigDecimal`                                                 |
| `java.text`              | 语言无关的格式化        | `NumberFormat`、`MessageFormat`                                            |
| `javax.xml` / `java.xml` | XML 处理          | `DocumentBuilder`、`SAXParser`、`XPath`                                     |

理解这些包的**分工逻辑**——哪些是语言基座、哪些负责数据组织、哪些处理 I/O 与通信、哪些在模块化后独立成域——比记住具体的类名更有长期价值。当遇到新的
API 时，先判断它落在哪个层次，再决定是否引入，往往能避免“用 `Vector` 写 2026 年的代码”或“在简单文件读写中过早引入 NIO
通道”这类错配。
