Java 反射的核心，是允许程序在**运行时**检查、访问并修改类自身的结构与行为。它打破了编译期“写死”的静态限制，使代码具备了动态性，这也是 Spring、MyBatis、Jackson 等几乎所有主流框架的底层基石。

### 🔍 反射的本质：`Class` 对象

JVM 加载一个类时，会在内存中为其创建一个**唯一**的 `java.lang.Class` 对象，这个对象就像类的“模板”或“元数据入口”，包含了该类的全部信息（类名、构造器、字段、方法、注解等）。反射的所有操作，都始于获取这个 `Class` 对象。

获取 `Class` 对象主要有三种方式，它们指向同一个对象，但行为有细微差别：

```java
// 方式一：类名.class —— 编译期已知，不触发静态初始化
Class<?> c1 = User.class;

// 方式二：对象.getClass() —— 已有实例时使用
User user = new User();
Class<?> c2 = user.getClass();

// 方式三：Class.forName(全限定名) —— 最常用，会触发类的静态初始化（执行静态代码块）
Class<?> c3 = Class.forName("com.example.User");
```

> **提示**：`Class.forName` 与 `ClassLoader.loadClass` 的关键区别在于，前者默认执行静态初始化块，后者只加载不初始化。JDBC 驱动注册 `Class.forName("com.mysql.cj.jdbc.Driver")` 正是利用了这一特性。

---

### 🛠️ 反射核心 API 与用法示例

为了方便演示，先定义一个测试类 `User`，它包含公有/私有构造器、公有/私有字段和方法：

```java
package com.entity;

public class User {
    public Integer age;
    private String username;
    public static String NATION = "中国";

    public User() {}
    private User(String username) { this.username = username; }
    public User(String username, Integer age) { this.username = username; this.age = age; }

    public void login(String password) { System.out.println("登录成功，密码：" + password); }
    private String getSecret() { return "私密数据"; }
    public static void showInfo() { System.out.println("用户静态信息"); }

    // getter / setter 省略
}
```

#### 构造器 `Constructor`：动态创建实例

通过 `Class` 对象获取构造器，再用 `newInstance` 创建对象。对于私有构造器，必须调用 `setAccessible(true)` 关闭访问检查。

```java
Class<?> clazz = Class.forName("com.entity.User");

// 1. 获取公有构造器（指定参数类型）
Constructor<?> publicCtor = clazz.getConstructor(String.class, Integer.class);
User user1 = (User) publicCtor.newInstance("张三", 25);

// 2. 获取私有构造器（本类声明的任意访问级别）
Constructor<?> privateCtor = clazz.getDeclaredConstructor(String.class);
privateCtor.setAccessible(true);          // 暴力开启私有访问
User user2 = (User) privateCtor.newInstance("李四");
```

> **注意**：`Class.newInstance()` 已被废弃，因为它只能调用无参构造器且异常处理不透明，应始终使用 `Constructor.newInstance()`。
>
> **API 选择**：`getConstructor()` 仅返回**公有**构造器（可含父类）；`getDeclaredConstructor()` 返回本类**所有访问级别**的构造器，但需配合 `setAccessible` 才能实例化私有构造。

#### 字段 `Field`：读写属性（包括私有字段）

`Field.get(obj)` 读取字段值，`Field.set(obj, value)` 设置值。操作静态字段时，对象参数传 `null` 即可。

```java
Class<?> clazz = User.class;
User user = new User();

// 公有字段
Field ageField = clazz.getField("age");
ageField.set(user, 20);
System.out.println(ageField.get(user));          // 20

// 私有字段：必须 setAccessible(true)
Field nameField = clazz.getDeclaredField("username");
nameField.setAccessible(true);
nameField.set(user, "王五");
System.out.println(nameField.get(user));         // 王五

// 静态字段
Field nationField = clazz.getField("NATION");
System.out.println(nationField.get(null));       // 中国
nationField.set(null, "中华");
```

#### 方法 `Method`：动态调用方法

`Method.invoke(target, args...)` 执行方法。第一个参数是目标对象（静态方法传 `null`），后续是方法实参。私有方法同样需要 `setAccessible(true)`。

```java
Class<?> clazz = User.class;
User user = new User();

// 公有方法
Method loginMethod = clazz.getMethod("login", String.class);
loginMethod.invoke(user, "123456");             // 登录成功，密码：123456

// 私有方法
Method secretMethod = clazz.getDeclaredMethod("getSecret");
secretMethod.setAccessible(true);
String secret = (String) secretMethod.invoke(user);
System.out.println(secret);                      // 私密数据

// 静态方法（目标对象传 null）
Method staticMethod = clazz.getMethod("showInfo");
staticMethod.invoke(null);                       // 用户静态信息
```

---

### 🎯 反射的典型应用场景与代码

反射的价值在于解决“编译时不知道具体类型”的问题。

#### 场景一：配置驱动的动态对象创建

框架往往只从配置文件读到类名字符串，需要动态实例化并调用方法。以下示例模拟根据用户输入或配置，动态绘制不同图形，新增图形时无需修改 `if-else` 逻辑：

```java
public interface Shape { void draw(); }
public class Circle implements Shape {
    public void draw() { System.out.println("⚪ 绘制圆形"); }
}
public class Square implements Shape {
    public void draw() { System.out.println("⬜ 绘制正方形"); }
}

public class DynamicShapeDemo {
    public static void main(String[] args) throws Exception {
        String shapeType = "Circle";   // 可来自配置文件或用户输入
        String className = "com.example." + shapeType;

        Class<?> shapeClass = Class.forName(className);
        Constructor<?> ctor = shapeClass.getDeclaredConstructor();
        Shape shape = (Shape) ctor.newInstance();

        Method drawMethod = shapeClass.getMethod("draw");
        drawMethod.invoke(shape);
    }
}
```

#### 场景二：JDK 动态代理——AOP 的底层机制

动态代理（`java.lang.reflect.Proxy`）完全基于反射：代理对象在运行时生成，所有方法调用都被路由到 `InvocationHandler`，在其中通过 `Method.invoke` 反射调用目标方法，从而可以在调用前后织入日志、事务、权限等逻辑。

```java
import java.lang.reflect.*;

public class LogInvocationHandler implements InvocationHandler {
    private final Object target;

    public LogInvocationHandler(Object target) { this.target = target; }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("[日志] 即将调用: " + method.getName());
        Object result = method.invoke(target, args);   // 反射调用真实方法
        System.out.println("[日志] 调用完成: " + method.getName());
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T target, Class<T> interfaceType) {
        return (T) Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class<?>[]{interfaceType},
                new LogInvocationHandler(target)
        );
    }
}

// 使用
Shape circle = new Circle();
Shape proxy = LogInvocationHandler.createProxy(circle, Shape.class);
proxy.draw();   // 自动输出日志，再执行 Circle.draw()
```

Spring AOP 的 JDK 代理路径，底层走的正是这套机制。

---

### ⚠️ 性能考量与最佳实践

反射并非没有代价。由于涉及动态类型解析，JVM 无法像直接调用那样做充分的静态优化，**未预热时反射调用可能比直接调用慢两个数量级**，即便预热后通常也慢数倍。

工程中应遵循以下原则：

1. **优先直接调用**：只有在编译期无法确定类型、需要动态能力时才使用反射。
2. **缓存反射元数据**：`Class`、`Method`、`Field`、`Constructor` 的查找涉及字符串匹配与遍历，**务必在初始化阶段缓存到 `static final` 或 `ConcurrentHashMap`**，避免在循环或高频路径中反复调用 `getDeclaredMethod`。
3. **让调用“热”起来**：JVM 对反射有“预热”机制（默认前 15 次使用 Native 实现，之后生成字节码版本的 `MethodAccessor`），高频调用的反射路径在预热后性能会显著改善。如果调用次数极少，预热反而带来额外开销。
4. **谨慎使用 `setAccessible(true)`**：它会关闭 Java 的访问检查，破坏封装。在 JDK 9+ 模块系统下，访问未 `opens` 的包会抛出 `InaccessibleObjectException`，JDK 17 默认强封装后旧式反射访问 `sun.misc.*` 的代码会直接失败。
5. **性能极敏感路径考虑 `MethodHandle`**：JDK 7+ 的 `MethodHandle` 在充分预热后可以接近直接调用性能，是比传统反射更现代的替代方案。

---

**总结**：反射是 Java 从静态走向动态的关键桥梁，它的价值不在于替代直接调用，而在于为框架层提供一种“运行时感知并操作任意类”的通用能力。理解 `Class` → `Constructor` / `Field` / `Method` 这条主线，再结合动态代理这一典型复合场景，就能把握反射在实际工程中的定位与边界。
