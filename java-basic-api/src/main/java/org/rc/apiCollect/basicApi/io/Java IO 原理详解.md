# Java IO 原理详解

Java IO 的底层逻辑，本质上是一段**从“单向流”向“双向通道 + 多路复用 + 异步回调”持续演进的历史**。理解这条演进线索，比孤立记忆各类 API 更重要。

---

## 一、传统 java.io：以“流”为中心的阻塞模型

`java.io` 包的核心抽象是**流**。数据像水流一样从源流向目标，且方向是单向的：输入流负责读，输出流负责写。

### 1.1 字节流与字符流的分层

| 维度 | 字节流 | 字符流 |
|---|---|---|
| 基类 | `InputStream` / `OutputStream` | `Reader` / `Writer` |
| 数据单位 | 8 位字节 | 16 位字符 |
| 适用场景 | 二进制文件、网络原始数据、图片、音视频 | 文本文件 |
| 编码处理 | 不涉及 | 内部完成编码/解码 |
| 缓冲行为 | 节点流本身通常无缓冲 | 字符流内部常带缓冲区 |

字符流的本质是 **“字节流 + 字符集”**：`InputStreamReader` 内部持有 `StreamDecoder`，按指定字符集将字节解码为字符；`OutputStreamWriter` 则反向编码。如果直接用字节流读取文本，多字节字符可能在缓冲区边界处被截断，导致乱码；字符流把这层复杂性封装掉了。

### 1.2 节点流与过滤流的组合：装饰器模式

`java.io` 的设计精髓在于**节点流直接对接数据源，过滤流包装节点流以增强功能**。`BufferedInputStream` 就是典型例子：它不直接与文件对话，而是接收一个 `InputStream`，在内部维护 8 KB 缓冲区，减少系统调用次数。

这一设计的底层是**装饰器模式**。`FilterInputStream` 持有一个 `protected volatile InputStream in`，所有子类都基于这个被包装的流来工作：

```java
public class FilterInputStream extends InputStream {
    protected volatile InputStream in;
    protected FilterInputStream(InputStream in) { this.in = in; }
    // read() 等方法委托给 in，子类可在委托前后插入额外逻辑
}
```

这意味着你可以**层层嵌套**，每层只做一件事：

```java
try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(
            new FileInputStream("app.log"), StandardCharsets.UTF_8))) {
    String line;
    while ((line = reader.readLine()) != null) {
        System.out.println(line);
    }
}
```

`FileInputStream` 读字节 → `InputStreamReader` 解码 → `BufferedReader` 缓冲并按行切分，职责清晰分离。

此外，`InputStreamReader` 本身也是**适配器模式**的体现：它把字节流的 `InputStream` 接口适配为字符流的 `Reader` 接口，解决了“已有字节流、但需要字符 API”的接口不兼容问题。

### 1.3 传统 IO 的阻塞本质

在 `java.io` 下，`read()` 调用会**阻塞当前线程**，直到有数据可读、到达流末尾或抛出异常。在网络场景中，这催生了经典的 **BIO 模型**：`ServerSocket.accept()` 阻塞等待连接，每个连接交给一个独立线程处理，而该线程在 `read()` 上再次阻塞。

```java
ServerSocket server = new ServerSocket(8080);
while (true) {
    Socket client = server.accept();          // 阻塞：等连接
    new Thread(() -> {
        // 内部 read() 再次阻塞：等数据
    }).start();
}
```

这种“一连接一线程”模型在连接数上升时，线程栈（默认约 1 MB）和上下文切换成本会迅速成为瓶颈，也就是 C10K 问题的根源。

---

## 二、NIO：面向缓冲区、基于通道、依靠选择器

JDK 1.4 引入的 `java.nio` 不是对 `java.io` 的修补，而是**换了一套底层抽象**：用 **Buffer** 替代流的单向读写，用 **Channel** 替代单向流，用 **Selector** 实现单线程管理多连接。

### 2.1 Buffer：可重复读写的内存视图

`Buffer` 是一个线性容器，内部维护四个关键指针：

- **capacity**：容量上限；
- **limit**：当前可读/可写的边界；
- **position**：下一个读写位置；
- **mark**：可选的回退标记。

写模式下，`position` 从 0 开始递增；**切换到读模式必须调用 `flip()`**，它将 `limit` 设为当前 `position`，并把 `position` 归零。忘记 `flip()` 是 NIO 最经典的错误之一。

```java
ByteBuffer buf = ByteBuffer.allocate(1024);
buf.put("hello".getBytes(StandardCharsets.UTF_8)); // 写模式，position = 5
buf.flip();                                         // 切换读模式：limit=5, position=0
byte[] out = new byte[buf.remaining()];
buf.get(out);                                       // 读出 "hello"
buf.clear();                                        // 复位为写模式（数据未擦除）
```

`allocate` 分配的是**堆内内存**；`allocateDirect` 分配的是**堆外直接内存**。后者在做 IO 时少一次从堆内到堆外的拷贝，但分配和释放成本更高，适合长期复用的缓冲区。

### 2.2 Channel：双向数据管道

与单向的流不同，`Channel` 可以同时读和写。常见实现包括 `FileChannel`、`SocketChannel`、`ServerSocketChannel`、`DatagramChannel`。`FileChannel` 不支持非阻塞模式，但支持 `transferTo` 等零拷贝操作；`SocketChannel` 和 `ServerSocketChannel` 则可以通过 `configureBlocking(false)` 进入非阻塞模式。

非阻塞模式下，`read()` 可能返回 0（无数据可读），`write()` 可能只写入部分字节。因此**写操作必须循环直到 `buffer.hasRemaining()` 为 false**，否则会出现“半包”问题。

### 2.3 Selector：多路复用的核心

`Selector` 是 NIO 实现**单线程管理多连接**的关键。你把一个或多个 `Channel` 注册到 `Selector` 上，并声明关心的事件类型，然后通过 `select()` 等待事件就绪。

四种就绪事件：

| 事件常量 | 触发条件 | 典型用途 |
|---|---|---|
| `OP_ACCEPT` | 新连接完成三次握手 | 服务端接受连接 |
| `OP_READ` | 内核接收缓冲区有数据 | 读取客户端报文 |
| `OP_WRITE` | 内核发送缓冲区可写 | 大报文发送 |
| `OP_CONNECT` | 客户端连接就绪 | 客户端建立连接 |

一个简化的 NIO 回显服务端如下：

```java
Selector selector = Selector.open();
ServerSocketChannel server = ServerSocketChannel.open();
server.configureBlocking(false);
server.bind(new InetSocketAddress(8080));
server.register(selector, SelectionKey.OP_ACCEPT);

while (true) {
    selector.select();                          // 阻塞直到有事件就绪
    Iterator<SelectionKey> it = selector.selectedKeys().iterator();
    while (it.hasNext()) {
        SelectionKey key = it.next();
        it.remove();                            // 必须移除，否则重复处理

        if (key.isAcceptable()) {
            SocketChannel client = server.accept();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
        } else if (key.isReadable()) {
            SocketChannel client = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int n = client.read(buffer);
            if (n == -1) {
                client.close();
            } else {
                buffer.flip();
                client.write(buffer);           // 回显
                buffer.clear();
            }
        }
    }
}
```

`selector.selectedKeys()` 返回的集合**不会自动清理**，必须显式调用 `it.remove()`，否则同一个事件会被反复处理。

底层实现上，Linux 使用 `epoll`，macOS 使用 `kqueue`，Windows 使用 `IOCP` 的封装。线程数不再与连接数挂钩，这是 NIO 相对 BIO 的根本性改进。

---

## 三、AIO：真正的异步回调

NIO 虽然不阻塞在 `read()` 上，但**事件监听本身仍然是同步的**——线程需要主动调用 `select()` 来“询问”哪些通道就绪。AIO（JDK 7 引入，`java.nio.channels` 下的异步通道）则把“等待”这一步也交给操作系统：发起读写后立即返回，内核完成 IO 后通过回调或 `Future` 通知用户线程。

核心组件是 `AsynchronousServerSocketChannel` 和 `CompletionHandler`。服务端不再有显式的事件循环，而是通过回调链驱动：

```java
AsynchronousServerSocketChannel server =
        AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(8080));

server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
    @Override
    public void completed(AsynchronousSocketChannel client, Void attachment) {
        server.accept(null, this);              // 继续接受下一个连接
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        client.read(buffer, null, new CompletionHandler<Integer, Void>() {
            @Override
            public void completed(Integer n, Void att) {
                if (n == -1) { /* 连接关闭 */ return; }
                buffer.flip();
                client.write(buffer);           // 异步回显
            }
            @Override
            public void failed(Throwable exc, Void att) { /* 异常处理 */ }
        });
    }
    @Override
    public void failed(Throwable exc, Void attachment) { /* 异常处理 */ }
});
```

AIO 的编程模型是**回调驱动**的，不再需要手动管理 `Selector` 和事件循环。但在 Linux 上，JDK 的 AIO 实现底层仍依赖 `epoll` 的模拟，真正的内核级异步 IO（如 `io_uring`）在 JDK 层面的覆盖有限，因此 Netty 等主流框架最终选择了基于 NIO 的 Reactor 模型，而非 AIO。

---

## 四、零拷贝：NIO 之上的性能优化

传统文件发送路径是：**磁盘 → 内核页缓存 → 用户缓冲区 → socket 缓冲区 → 网卡**，涉及 4 次数据拷贝和多次上下文切换。NIO 提供了两种绕过用户态的手段：

- **`FileChannel.transferTo`**：在 Linux 上可能走 `sendfile`，数据不进入用户态，直接在内核中从文件页缓存送往 socket 缓冲区。
- **`MappedByteBuffer`（mmap）**：将文件映射到进程地址空间，读写直接作用于页缓存，适合随机访问大文件。

```java
// sendfile 路径
try (FileChannel in = FileChannel.open(Paths.get("source.bin"), StandardOpenOption.READ);
     FileChannel out = FileChannel.open(Paths.get("target.bin"),
             StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
    in.transferTo(0, in.size(), out);
}
```

零拷贝不是“没有拷贝”，而是**减少 CPU 参与的数据拷贝次数和用户态/内核态切换次数**，在高吞吐文件传输和消息中间件（如 Kafka）中是关键优化手段。

---

## 总结

Java IO 的演进可以概括为三次抽象层次的提升：

1. **`java.io`**：用**流**统一了字节与字符、节点与过滤，装饰器与适配器让组合成为可能；但阻塞模型决定了它在高并发网络场景下的天花板。
2. **NIO**：用 **Buffer + Channel + Selector** 把“单向等待”变成“多路复用的事件驱动”，一线程可管理多连接，是 Netty 等高性能框架的底座。
3. **AIO**：把等待完全交给操作系统，以回调驱动；但在 Linux 生态下，NIO 的 Reactor 模型仍是更成熟、更广泛采用的选择。

理解这条演进线，也就理解了 Java IO 中“为什么会有这么多类”以及“什么时候该用哪一层”。
