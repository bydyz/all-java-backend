Java 中每个类型（类、接口、枚举、注解、数组、基本类型、`void`）在 JVM 中都有一个对应的 `java.lang.Class` 对象。获取 `Class` 实例的方式很多，下面按常用方式说明，并给出代码。

> 约定：示例中的类假设可直接访问；实际项目中 `Class.forName` 要写全限定名，内部类用 `$`。

---

## 1. 类字面量：`Xxx.class`

最常用、类型安全、编译期可检查。**不会触发类初始化**（静态代码块不执行）。

```java
Class<Person> c1 = Person.class;
Class<String> c2 = String.class;
Class<Runnable> c3 = Runnable.class;   // 接口
Class<int[]> c4 = int[].class;         // 数组
Class<Integer> c5 = int.class;         // 基本类型
Class<Void> c6 = void.class;           // void
```

特点：

- 不需要对象实例；
- 不触发静态初始化；
- 编译期就能检查类型；
- 可获取接口、数组、基本类型、`void` 的 `Class`。

---

## 2. 对象实例方法：`obj.getClass()`

获取对象的**运行时实际类型**，多态时尤其重要。

```java
class Person {}
class Student extends Person {}

Person p = new Student();

Class<?> c1 = Person.class;
Class<?> c2 = p.getClass();

System.out.println(c1 == c2);        // false
System.out.println(c2.getName());    // 实际类型 Student
System.out.println(c2 == Student.class); // true
```

特点：

- 必须已有对象；
- 返回运行时类型，不是声明类型；
- 不能用于基本类型，因为基本类型没有对象；
- 常用于反射、日志、框架中判断实际类型。

注意：自动装箱后：

```java
Object o = 1; // 实际是 Integer
System.out.println(o.getClass() == Integer.class); // true
System.out.println(o.getClass() == int.class);     // false
```

---

## 3. `Class.forName(String)`

根据类的**二进制名**加载类，默认会**初始化类**，即执行静态代码块。

```java
Class<?> c1 = Class.forName("java.lang.String");
Class<?> c2 = Class.forName("java.util.Map$Entry"); // 内部类用 $
```

JDBC 驱动加载是典型用法：

```java
Class.forName("com.mysql.cj.jdbc.Driver");
```

特点：

- 参数是全限定类名；
- 默认触发类初始化；
- 抛 `ClassNotFoundException`；
- 初始化失败可能抛 `ExceptionInInitializerError`；
- 不能写 `Class.forName("int")`，基本类型要用 `int.class`。

也可以控制是否初始化：

```java
ClassLoader loader = Thread.currentThread().getContextClassLoader();

Class<?> c1 = Class.forName(
        "com.example.Foo",
        false,          // 不初始化
        loader
);
```

`Class.forName(name, false, loader)` 只加载类，不执行静态初始化。

---

## 4. 类加载器：`ClassLoader.loadClass(String)`

通过类加载器按名称加载类。**默认不初始化类**。

```java
ClassLoader loader = Thread.currentThread().getContextClassLoader();
Class<?> c = loader.loadClass("com.example.Foo");
```

特点：

- 更底层，遵循类加载器的双亲委派机制；
- 默认不触发类初始化；
- 可指定类加载器，适合框架、插件、容器场景；
- 抛 `ClassNotFoundException`；
- 不能加载基本类型，如 `loadClass("int")` 会失败。

与 `Class.forName` 的核心区别：

| 方式 | 是否默认初始化 | 类加载器 |
|---|---|---|
| `Class.forName("Foo")` | 是 | 使用调用者类加载器 |
| `Class.forName("Foo", false, loader)` | 否 | 指定 loader |
| `loader.loadClass("Foo")` | 否 | 指定 loader |

---

## 5. 包装类的 `TYPE` 字段

包装类中有一个 `TYPE` 常量，表示对应**基本类型**的 `Class`。

```java
Class<Integer> c1 = int.class;
Class<Integer> c2 = Integer.TYPE;
Class<Integer> c3 = Integer.class;

System.out.println(c1 == c2); // true
System.out.println(c1 == c3); // false

Class<Void> v1 = void.class;
Class<Void> v2 = Void.TYPE;
System.out.println(v1 == v2); // true
```

常见对应关系：

```java
int.class     == Integer.TYPE
long.class    == Long.TYPE
boolean.class == Boolean.TYPE
void.class    == Void.TYPE
```

注意：`int.class` 和 `Integer.class` 不是同一个 `Class` 对象。前者表示基本类型，后者表示引用类型。

---

## 6. 数组类型的 `Class`

数组也是类型，可以用类字面量获取：

```java
Class<?> c1 = String[].class;
Class<?> c2 = int[].class;
Class<?> c3 = String[][].class;
```

也可以通过反射创建数组后获取：

```java
import java.lang.reflect.Array;

Class<?> c4 = Array.newInstance(String.class, 0).getClass();
System.out.println(c4 == String[].class); // true
```

数组的二进制名比较特殊：

```java
System.out.println(String[].class.getName());   // [Ljava.lang.String;
System.out.println(int[].class.getName());      // [I
System.out.println(String[][].class.getName()); // [[Ljava.lang.String;
```

因此可以用 `Class.forName` 获取数组类型：

```java
Class<?> c5 = Class.forName("[Ljava.lang.String;"); // String[]
Class<?> c6 = Class.forName("[I");                  // int[]
Class<?> c7 = Class.forName("[[I");                 // int[][]
```

获取数组组件类型：

```java
Class<?> component = String[].class.getComponentType(); // String.class
Class<?> component2 = int[].class.getComponentType();   // int.class
```

---

## 7. 通过反射 API 间接获取关联类型的 `Class`

这些不是最常用的“入口方式”，但可以从已有反射对象得到关联类型。

```java
// 父类
Class<?> superClass = Student.class.getSuperclass(); // Person.class

// 接口
Class<?>[] interfaces = Student.class.getInterfaces();

// 数组成员类型
Class<?> componentType = String[].class.getComponentType(); // String.class

// 方法声明所在类
Class<?> declaringClass =
        String.class.getMethod("length").getDeclaringClass(); // String.class

// 注解实例的注解类型
Deprecated annotation = String.class.getAnnotation(Deprecated.class);
Class<? extends java.lang.annotation.Annotation> annotationType =
        annotation.annotationType();
```

动态代理也可以生成代理类 `Class`：

```java
import java.lang.reflect.Proxy;

Class<?> proxyClass = Proxy.getProxyClass(
        Runnable.class.getClassLoader(),
        Runnable.class
);
```

---

## 8. 综合示例

```java
import java.lang.reflect.Array;

public class GetClassDemo {

    static class Person {
        static {
            System.out.println("Person 初始化");
        }
    }

    static class Student extends Person {
        static {
            System.out.println("Student 初始化");
        }
    }

    public static void main(String[] args) throws Exception {
        // 1. 类字面量：不触发初始化
        Class<?> c1 = Person.class;

        // 2. 对象.getClass()：运行时实际类型
        Person p = new Student();          // 这里会初始化 Person、Student
        Class<?> c2 = p.getClass();        // GetClassDemo$Student
        System.out.println(c1 == c2);      // false
        System.out.println(c2.getName());  // GetClassDemo$Student

        // 3. Class.forName：默认初始化
        Class<?> c3 = Class.forName("GetClassDemo$Person");

        // 不初始化
        Class<?> c4 = Class.forName(
                "GetClassDemo$Person",
                false,
                GetClassDemo.class.getClassLoader()
        );

        // 4. ClassLoader.loadClass：不初始化
        ClassLoader loader = GetClassDemo.class.getClassLoader();
        Class<?> c5 = loader.loadClass("GetClassDemo$Person");

        // 5. 基本类型与包装类 TYPE
        Class<Integer> c6 = int.class;
        Class<Integer> c7 = Integer.TYPE;
        Class<Integer> c8 = Integer.class;

        System.out.println(c6 == c7); // true
        System.out.println(c6 == c8); // false

        Class<Void> c9 = void.class;
        System.out.println(c9 == Void.TYPE); // true

        // 6. 数组
        Class<?> c10 = String[].class;
        Class<?> c11 = int[].class;
        Class<?> c12 = Array.newInstance(String.class, 0).getClass();

        System.out.println(c10 == c12);       // true
        System.out.println(c10.getName());    // [Ljava.lang.String;
        System.out.println(c11.getName());    // [I

        Class<?> c13 = Class.forName("[Ljava.lang.String;");
        Class<?> c14 = Class.forName("[I");

        // 7. 间接获取
        Class<?> superClass = Student.class.getSuperclass(); // Person
        Class<?> componentType = String[].class.getComponentType(); // String
        Class<?> declaringClass =
                String.class.getMethod("length").getDeclaringClass(); // String
    }
}
```

---

## 9. 方式对比总结

| 方式 | 示例 | 是否触发初始化 | 典型场景 |
|---|---|---:|---|
| 类字面量 | `Person.class` | 否 | 最常用、类型安全 |
| 对象方法 | `obj.getClass()` | 不额外触发 | 获取运行时实际类型 |
| `Class.forName` | `Class.forName("pkg.Person")` | 是 | JDBC 驱动、按名加载 |
| `Class.forName` 可控初始化 | `Class.forName(name, false, loader)` | 否 | 框架延迟加载 |
| 类加载器 | `loader.loadClass(name)` | 否 | 插件、容器、自定义加载器 |
| 包装类 `TYPE` | `Integer.TYPE` | 否 | 基本类型 `Class` |
| 基本类型字面量 | `int.class`、`void.class` | 否 | 基本类型、`void` |
| 数组字面量 | `String[].class` | 否 | 数组类型 |
| 反射创建数组 | `Array.newInstance(...).getClass()` | 已创建数组 | 运行时确定数组类型 |
| 反射成员 | `method.getDeclaringClass()` | 不额外触发 | 从成员反推声明类 |

---

## 10. 注意事项

1. **同一类加载器下，同一类型的 `Class` 对象唯一**，可用 `==` 比较：

   ```java
   System.out.println(String.class == Class.forName("java.lang.String")); // true
   ```

2. **不同类加载器加载同名类，得到的 `Class` 可能不相等**：

   ```java
   loader1.loadClass("com.example.Foo") == loader2.loadClass("com.example.Foo")
   // 可能为 false
   ```

3. `Class.forName` 使用**二进制名**：内部类用 `$`，数组用 `[L...;` 等形式。

4. **基本类型不能用 `Class.forName("int")`**，要用 `int.class` 或 `Integer.TYPE`。

5. 获取 `Class` 对象不等于创建对象；若要实例化，现代写法是：

   ```java
   Object obj = clazz.getDeclaredConstructor().newInstance();
   ```

   `Class.newInstance()` 已废弃，不建议使用。

6. `Class.forName` 默认会执行静态初始化，可能产生副作用；只想加载类而不初始化时，用 `Class.forName(name, false, loader)` 或 `loader.loadClass(name)`。
