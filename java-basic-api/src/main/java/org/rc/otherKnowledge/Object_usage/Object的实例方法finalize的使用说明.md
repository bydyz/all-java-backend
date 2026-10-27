# Object.finalize() 代码详解

## 1. 方法定义

```java
// java.lang.Object 中的原始定义（Java 8 源码）
protected void finalize() throws Throwable { }
```

- `protected`：只有子类和同包可以访问/重写，**外部不能直接调用**。
- 空实现：`Object` 本身不做任何事。
- 从 **Java 9** 起标注 `@Deprecated(since="9")`，从 **Java 18** 起标注 `@Deprecated(since="18", forRemoval=true)`（JEP
  421）。因此下面的示例在 JDK 18+ 编译会出现 `removal` 警告，示例中统一加 `@SuppressWarnings("removal")`。

---

## 2. 调用时机（生命周期）

```
对象可达 ──► 不可达
              │
              ▼
   GC 判定：重写了 finalize() 且从未执行过？
        │是                          │否
        ▼                            ▼
 放入 Finalizer 队列           本轮/下轮直接回收
 （对象被 FinalizerReference 强引用，仍存活）
        │
        ▼
 Finalizer 线程（单线程，低优先级）调用 finalize()
        │
        ▼
 再下一轮 GC：若仍不可达 → 真正回收
```

关键点：

- **不保证被执行**：JVM 退出、finalizer 线程忙、内存充足时可能永远不执行。
- **每个对象最多执行一次**。
- **由单独的 `Finalizer` 线程执行**，不是调用 GC 的线程。
- **异常会被吞掉**，不会传播。
- **延长对象生命周期至少一轮 GC**。

---

## 3. 代码示例

### 3.1 观察 finalize 被调用与执行线程

```java
@SuppressWarnings("removal")
class Demo {
    private final int id;
    Demo(int id) { this.id = id; }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("finalize() id=" + id
                + " thread=" + Thread.currentThread().getName());
        super.finalize(); // 惯例调用父类；若父类是 Object 则非必需
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 3; i++) {
            new Demo(i);          // 创建后立即不可达
        }
        System.gc();              // 只是"建议"GC
        Thread.sleep(1000);       // 等 Finalizer 线程调度
        System.out.println("main 结束");
    }
}
```

可能输出（顺序不保证，可能只打印部分）：

```
finalize() id=2 thread=Finalizer
finalize() id=0 thread=Finalizer
finalize() id=1 thread=Finalizer
main 结束
```

**结论**：finalize 在 `Finalizer` 线程执行、时机不可控、顺序不可控。

---

### 3.2 只调用一次 + 对象"复活"（resurrection）

```java
@SuppressWarnings("removal")
public class Resurrection {
    static Resurrection SAVE;   // 静态强引用

    @Override
    protected void finalize() throws Throwable {
        System.out.println("finalize 被调用，对象复活");
        SAVE = this;            // 把自己重新挂到静态引用上
    }

    public static void main(String[] args) throws Exception {
        Resurrection r = new Resurrection();
        r = null;

        System.gc(); Thread.sleep(500);
        System.out.println("第一次 GC 后 SAVE = " + SAVE);   // 非 null

        SAVE = null;            // 再次不可达
        System.gc(); Thread.sleep(500);
        System.out.println("第二次 GC 后 SAVE = " + SAVE);   // null
    }
}
```

典型输出：

```
finalize 被调用，对象复活
第一次 GC 后 SAVE = Resurrection@...
第二次 GC 后 SAVE = null
```

**结论**：

1. `finalize` 只调用一次，第二次不可达时直接回收，**没有再打印"finalize 被调用"**。
2. 复活是 finalize 的经典"黑魔法"，也是它被废弃的原因之一 —— 让对象生命周期无法预测。

---

### 3.3 finalize 抛异常被吞掉

```java
@SuppressWarnings("removal")
public class ExceptionInFinalize {
    @Override
    protected void finalize() throws Throwable {
        throw new RuntimeException("finalize 中的异常");
    }

    public static void main(String[] args) throws Exception {
        new ExceptionInFinalize();
        System.gc();
        Thread.sleep(500);
        System.out.println("主线程正常退出，异常被忽略");
    }
}
```

输出：

```
主线程正常退出，异常被忽略
```

**结论**：`finalize` 抛出的任何异常都被 `Finalizer` 线程忽略（规范：*Any exception thrown by finalize is ignored*
），既不会导致进程崩溃，也不会出现在主线程的调用栈中。这让 finalize 中出错的资源清理**无法被发现**。

---

### 3.4 阻塞 Finalizer 线程 → 内存泄漏 / OOM

`Finalizer` 是**单线程**。一旦某个 `finalize` 卡住（IO、锁、sleep），后面所有待终结对象都会排队，导致这些对象**无法被回收**。

```java
@SuppressWarnings("removal")
public class BlockFinalizer {
    static class Big {
        byte[] data = new byte[1024 * 1024];   // 1 MB

        @Override
        protected void finalize() throws Throwable {
            Thread.sleep(10_000);              // 模拟耗时操作（如关闭网络连接）
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10_000; i++) {
            new Big();                          // 全部进入 Finalizer 队列
        }
        System.out.println("创建完毕");
        // 运行时可配合：
        //   jcmd <pid> GC.class_histogram
        //   jmap -finalizerinfo <pid>
        // 观察 Finalizer 队列堆积，最终 java.lang.OutOfMemoryError: Java heap space
    }
}
```

**结论**：finalize 的**串行执行**会把 GC 的回收速度变成"单线程执行用户代码"的速度，是 finalize 的最大性能陷阱。

---

### 3.5 System.gc() / System.runFinalization() 只是"提示"

```java
@SuppressWarnings("removal")
public class SuggestOnly {
    @Override
    protected void finalize() { System.out.println("finalized"); }

    public static void main(String[] args) {
        new SuggestOnly();
        System.gc();              // 规范：只是"建议"，JVM 可以忽略
        System.runFinalization(); // 建议"运行待终结对象"
        // 不 sleep 的话，Finalizer 线程往往还没跑完 main 就结束了，
        // 此时这些对象可能永远不会被 finalize（JVM 退出）
    }
}
```

用 `-XX:+DisableExplicitGC` 启动可让 `System.gc()` 完全失效，输出变为空。

---

## 4. 为什么 finalize 被废弃

| 问题       | 说明                                                 |
|----------|----------------------------------------------------|
| 执行时机不可预测 | 依赖 GC，可能永不执行（JVM 退出）                               |
| 阻塞 GC    | 单线程 Finalizer，慢 finalize 拖垮回收，甚至 OOM               |
| 只执行一次    | 对象复活后无第二次机会，资源泄漏                                   |
| 异常被吞     | 清理失败完全不可见                                          |
| 复活与安全漏洞  | `finalize` 可在半初始化对象上被调用（参考 `FileInputStream` 历史漏洞） |
| 性能       | 带 finalize 的对象分配/回收都显著更慢                           |

---

## 5. 正确替代方案

### 5.1 首选：`try-with-resources`（显式关闭）

```java
public class Resource implements AutoCloseable {
    public Resource()         { System.out.println("打开资源"); }
    @Override public void close() { System.out.println("关闭资源"); }

    public static void main(String[] args) {
        try (Resource r = new Resource()) {
            // 使用 r
        }   // 编译器保证 close() 一定被调用，包括异常路径
    }
}
```

**规则**：能用 try-with-resources 就绝不用 finalize。

### 5.2 Java 9+：`java.lang.ref.Cleaner`

适用于"用户忘记 close，兜底清理"的场景。Cleaner 的逻辑和 finalize 类似，但**不延长对象生命周期**、清理动作独立、异常可见、清理线程池可控。

```java
import java.lang.ref.Cleaner;

public class CleanerDemo implements AutoCloseable {

    // 清理动作必须不持有外部对象引用，否则形成环，永远不清理
    private static final class State implements Runnable {
        private volatile boolean closed;
        @Override public void run() {
            if (!closed) {
                closed = true;
                System.out.println("Cleaner 释放资源");
            }
        }
    }

    private static final Cleaner CLEANER = Cleaner.create();

    private final State state;
    private final Cleaner.Cleanable cleanable;

    public CleanerDemo() {
        this.state = new State();
        this.cleanable = CLEANER.register(this, state);  // 注册：this -> Cleanable -> State
    }

    @Override
    public void close() {          // 正常路径：显式清理
        cleanable.clean();         // 保证 State.run() 只跑一次
    }

    public static void main(String[] args) throws Exception {
        new CleanerDemo();         // 忘记 close
        System.gc();
        Thread.sleep(500);         // Cleaner 线程是守护线程，主线程退出前给它机会
        System.out.println("main 结束");
    }
}
```

要点：

- 只有 `this` 不可达时，`Cleaner` 才会触发 `State.run()`。
- `State` 绝不能持有 `this`（例如作为内部非静态类），否则形成可达环，永远不清理 —— 这是最常见的 Cleaner 误用。
- `Cleaner` 线程是**守护线程**，JVM 退出时可能来不及跑，所以它只是"兜底"，不能代替 `close()`。

### 5.3 `PhantomReference` + `ReferenceQueue`

最底层、最灵活的方案，适合自定义缓存/资源池，用于实现"对象被回收后释放堆外内存"。JDK 内部的 `DirectByteBuffer` 就是这套机制。

```java
import java.lang.ref.*;
import java.util.*;

public class PhantomDemo {
    static final ReferenceQueue<Object> QUEUE = new ReferenceQueue<>();
    static final List<PhantomReference<Object>> REFS = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        Object obj = new Object();
        REFS.add(new PhantomReference<>(obj, QUEUE));  // 注册

        obj = null;
        System.gc();
        Reference<?> ref = QUEUE.remove(2000);          // 阻塞等待回收通知
        System.out.println("对象已被回收，可以释放堆外资源: " + (ref != null));
    }
}
```

- `PhantomReference.get()` 永远返回 `null`，无法"复活"对象。
- 只有通过 `ReferenceQueue` 才能知道对象已被回收，是 Cleaner 的底层实现原理。

---

## 6. 总结

| 维度       | finalize       | Cleaner            | try-with-resources / close() |
|----------|----------------|--------------------|------------------------------|
| 确定性      | ❌ 不可预测         | ⚠️ 仅兜底             | ✅ 编译期保证                      |
| 执行线程     | 单线程 Finalizer  | Cleaner 线程         | 调用者线程                        |
| 是否延长对象寿命 | ✅ 至少一轮 GC      | ❌ 不延长              | ❌                            |
| 异常处理     | 被吞掉            | 可被捕获               | 正常抛出                         |
| 能否多次     | ❌ 一次           | ✅ 由 Cleanable 保证一次 | ✅ 幂等设计                       |
| 推荐度      | ❌ 已 forRemoval | ⚠️ 仅兜底             | ✅ 首选                         |

**一句话**：`finalize()` 只在"历史代码阅读题"和"面试八股"里出现；新代码中一律用 `try-with-resources`
显式释放，必要时用 `Cleaner` 做兜底，绝不重写 `finalize()`。
