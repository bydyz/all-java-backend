`Thread` 类是 Java 多线程编程的基石，它封装了操作系统底层的线程机制。理解其构造器与常用 API，是掌握并发编程的第一步。

### 🧱 构造器：如何创建线程对象

`new Thread(...)` 只是在内存中创建了一个 Java 对象，**并不会立即启动一个操作系统线程**。真正的线程启动发生在调用 `start()` 方法时。`Thread` 类提供了多个重载构造器，核心区别在于是否指定 **`Runnable` 任务**、**线程名称**和**线程组**。

| 构造器 | 说明 |
| :--- | :--- |
| `Thread()` | 创建一个线程对象，需要**继承 `Thread` 并重写 `run()` 方法**来定义任务。 |
| `Thread(Runnable target)` | 传入一个 **`Runnable` 任务对象**，`Thread` 启动后会执行该任务的 `run()` 方法。 |
| `Thread(String name)` | 创建线程并指定名称，便于调试和监控。 |
| `Thread(Runnable target, String name)` | 同时指定**任务**和**线程名**，是最常用的组合。 |
| `Thread(ThreadGroup group, Runnable target)` | 将线程归属到指定的**线程组**，用于批量管理（实际开发中较少直接使用）。 |
| `Thread(ThreadGroup group, Runnable target, String name, long stackSize)` | 最完整的构造器，还可以**建议线程栈大小**。`stackSize` 的行为高度依赖平台，通常设置为 `0` 表示忽略。 |

**代码示例：两种创建线程的方式**

```java
// 方式一：继承 Thread，重写 run()
class MyThread extends Thread {
    public MyThread(String name) { super(name); }
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " 正在执行 (继承Thread)");
    }
}

// 方式二：实现 Runnable，作为任务传入
class MyTask implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " 正在执行 (实现Runnable)");
    }
}

public class ThreadCreationDemo {
    public static void main(String[] args) {
        MyThread t1 = new MyThread("线程-A");
        t1.start(); // 启动线程，JVM 会调用 t1.run()

        Thread t2 = new Thread(new MyTask(), "线程-B");
        t2.start();
    }
}
```

> **推荐**：更推荐**实现 `Runnable` 接口**的方式，因为它将“任务”与“执行机制”解耦，更符合面向对象设计原则。

### ⚙️ 核心生命周期与控制 API

这部分方法直接决定线程是运行、暂停还是等待。

#### `start()` vs `run()`
- **`start()`**：**启动一个新线程**，新线程会独立执行，并由 JVM 自动调用其 `run()` 方法。
- **`run()`**：**不会启动新线程**，它只是一个普通方法，直接在调用它的当前线程（如 `main`）中同步执行。

```java
Thread t = new Thread(() -> System.out.println("运行在: " + Thread.currentThread().getName()));

t.run();  // 输出 "运行在: main" (由 main 线程直接执行)
t.start(); // 输出 "运行在: Thread-0" (新线程执行)
```
**务必通过 `start()` 来启动线程**，直接调用 `run()` 就失去了多线程的意义。

#### `sleep(long millis)`：暂停当前线程
让**当前正在执行的线程**暂停指定毫秒数，进入 `TIMED_WAITING` 状态，**但不会释放持有的锁**。它是一个**静态方法**，写在哪个线程里就暂停哪个线程。

```java
try {
    System.out.println("开始睡眠 1 秒...");
    Thread.sleep(1000); // 当前线程（如 main）睡眠 1 秒
    System.out.println("睡眠结束");
} catch (InterruptedException e) {
    // 睡眠期间被中断会抛出此异常
    Thread.currentThread().interrupt(); // 恢复中断标志
}
```

#### `join()`：等待另一个线程结束
**当前线程**会阻塞，直到调用 `join()` 的那个**目标线程**执行完毕。常用于保证线程间的执行顺序。

```java
Thread worker = new Thread(() -> {
    try { Thread.sleep(1500); } catch (InterruptedException e) {}
    System.out.println("Worker 线程执行完毕");
});
worker.start();

System.out.println("主线程等待 Worker 结束...");
try {
    worker.join(); // main 线程会在这里等待，直到 worker 运行结束
} catch (InterruptedException e) {}
System.out.println("现在主线程继续执行");
```

#### `yield()`：提示让出 CPU
一个**静态方法**，提示调度器当前线程愿意让出 CPU 使用权，让同优先级的其他线程有机会运行。但这只是一个**提示**，调度器完全可以忽略。

```java
Thread.yield(); // 当前线程主动让出 CPU 的“建议”
```

### 🛠️ 其他关键状态与属性 API

#### 中断机制：`interrupt()` / `isInterrupted()` / `interrupted()`
Java 采用**协作式中断**，`interrupt()` 只是设置一个中断标志，或唤醒处于阻塞（`sleep`, `wait`, `join`）状态的线程（抛 `InterruptedException`），是否停止由目标线程自己决定。

| 方法 | 作用 |
| :--- | :--- |
| `interrupt()` | **实例方法**，设置目标线程的中断标志，或中断其阻塞状态。 |
| `isInterrupted()` | **实例方法**，检查目标线程的中断标志，**不会清除标志**。 |
| `interrupted()` | **静态方法**，检查**当前线程**的中断标志，并**清除标志**。 |

```java
Thread t = new Thread(() -> {
    while (!Thread.currentThread().isInterrupted()) {
        // 执行任务...
    }
    System.out.println("线程通过检查中断标志退出了循环");
});
t.start();

// 稍后
t.interrupt(); // 设置 t 线程的中断标志
```

#### 线程名称与身份
- `getName()` / `setName(String)`：获取或设置线程名，对调试非常有用。
- `currentThread()`：**静态方法**，返回当前正在执行的线程对象的引用。

```java
Thread t = new Thread(() -> {
    System.out.println("当前线程名: " + Thread.currentThread().getName());
}, "我的工作线程");
t.start(); // 输出: 当前线程名: 我的工作线程
```

#### 守护线程：`setDaemon(boolean)`
守护线程是为其他线程提供服务的**后台线程**。当 JVM 中所有**非守护线程**都结束时，JVM 会直接退出，**不会等待守护线程**执行完毕。

```java
Thread daemon = new Thread(() -> {
    while (true) {
        // 模拟后台监控任务
    }
});
daemon.setDaemon(true); // 必须在 start() 之前设置
daemon.start();
// main 线程结束后，JVM 会直接退出，不会等待这个守护线程
```

#### 优先级：`setPriority(int)` / `getPriority()`
设置线程优先级（1~10），但**高优先级并不保证先执行**，只是被调度的概率可能更高，行为高度依赖操作系统。

#### 状态查询：`isAlive()` / `getState()`
- `isAlive()`：线程已启动且尚未终止时返回 `true`。
- `getState()`：返回 `Thread.State` 枚举值（`NEW`, `RUNNABLE`, `BLOCKED`, `WAITING`, `TIMED_WAITING`, `TERMINATED`），是分析线程生命周期的精确工具。

### ⚠️ 务必避开的过时方法

以下方法因为**不安全**或**易导致死锁**，已被标记为**废弃（Deprecated）**，**不应在新代码中使用**：
- **`stop()`**：强制终止线程，会立即释放锁，可能导致数据不一致。
- **`suspend()` / `resume()`**：挂起线程时不释放锁，极易引发死锁。
- **`destroy()`**：从未被真正实现。

现代 Java 中，停止线程的正确方式是**使用 `interrupt()` 配合标志位检查**，实现优雅终止。

`Thread` 类的 API 设计清晰地划分了“对象创建”、“生命周期控制”与“状态查询”的职责。理解 `start` 与 `run` 的本质区别、`sleep` 的静态语义以及 `interrupt` 的协作式中断模型，是写出正确并发代码的关键。
