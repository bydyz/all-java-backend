## 1. 方法定义与核心语义

`getClass()` 定义在 `java.lang.Object` 中：

```java
public final native Class<?> getClass();
```

关键点：

| 修饰/返回      | 含义                                   |
|------------|--------------------------------------|
| `public`   | 任意对象都可调用                             |
| `final`    | 不能被重写                                |
| `native`   | 由 JVM 实现                             |
| `Class<?>` | 返回该对象的**运行时类**对应的 `Class` 对象         |
| 无参         | 没有参数                                 |
| 可能异常       | 对 `null` 调用会抛 `NullPointerException` |

核心语义：

```java
obj.getClass()
```

返回的不是“变量声明类型”，而是“对象实际运行时类型”。

例如：

```java
Object o = "hello";
System.out.println(o.getClass()); // class java.lang.String
```

虽然 `o` 的静态类型是 `Object`，但实际对象是 `String`，所以返回 `String.class`。

---

## 2. 基础示例

```java
public class GetClassBasic {
    public static void main(String[] args) {
        Object o = "hello";

        Class<?> c = o.getClass();

        System.out.println(c);                 // class java.lang.String
        System.out.println(c.getName());       // java.lang.String
        System.out.println(c.getSimpleName()); // String
        System.out.println(c == String.class); // true
        System.out.println(c == Object.class); // false
    }
}
```

输出：

```text
class java.lang.String
java.lang.String
String
true
false
```

说明：

- `c == String.class` 为 `true`，因为同一个类在同一个类加载器下通常对应同一个 `Class` 对象。
- `getClass()` 返回的是运行时类型，不是声明类型。

---

## 3. 多态场景：运行时类型 vs 声明类型

```java
class Animal {
}

class Dog extends Animal {
}

public class PolymorphismDemo {
    public static void main(String[] args) {
        Animal a = new Dog();

        System.out.println(a.getClass().getName());        // Dog
        System.out.println(a.getClass() == Dog.class);     // true
        System.out.println(a.getClass() == Animal.class);  // false
        System.out.println(a instanceof Dog);              // true
        System.out.println(a instanceof Animal);           // true
    }
}
```

输出：

```text
Dog
true
false
true
true
```

对比：

| 表达式                                        | 含义                   |
|--------------------------------------------|----------------------|
| `a.getClass() == Dog.class`                | 精确判断运行时类是否为 `Dog`    |
| `a instanceof Dog`                         | 判断对象是否可视为 `Dog`，包含子类 |
| `Dog.class.isInstance(a)`                  | 反射版 `instanceof`     |
| `Animal.class.isAssignableFrom(Dog.class)` | 判断类型之间的可赋值关系         |

示例：

```java
Animal a = new Dog();

System.out.println(Animal.class.isAssignableFrom(a.getClass())); // true
System.out.println(Dog.class.isAssignableFrom(Animal.class));    // false
```

---

## 4. 数组对象的 `getClass()`

数组也是对象，因此可以调用 `getClass()`。

```java
public class ArrayClassDemo {
    public static void main(String[] args) {
        int[] ints = new int[3];
        String[] strs = new String[3];

        System.out.println(ints.getClass().getName()); // [I
        System.out.println(ints.getClass().isArray()); // true
        System.out.println(ints.getClass().getComponentType()); // int

        System.out.println(strs.getClass().getName()); // [Ljava.lang.String;
        System.out.println(strs.getClass().getComponentType() == String.class); // true

        System.out.println(ints.getClass().getSuperclass()); // class java.lang.Object
    }
}
```

输出：

```text
[I
true
int
[Ljava.lang.String;
true
class java.lang.Object
```

说明：

- `[I` 表示 `int[]`
- `[Ljava.lang.String;` 表示 `String[]`
- `getComponentType()` 获取数组元素类型
- 数组类的父类通常是 `Object`

---

## 5. 匿名类、枚举、泛型擦除

### 5.1 匿名类

```java
public class AnonymousDemo {
    public static void main(String[] args) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
            }
        };

        System.out.println(r.getClass().getName());          // 例如 AnonymousDemo$1
        System.out.println(r.getClass().isAnonymousClass()); // true
    }
}
```

输出类似：

```text
AnonymousDemo$1
true
```

匿名类名由编译器生成，通常包含 `$1`、`$2` 等，不建议依赖具体名称。

---

### 5.2 枚举

```java
enum Color {
    RED,
    GREEN {
        @Override
        public String toString() {
            return "green";
        }
    }
}

public class EnumDemo {
    public static void main(String[] args) {
        System.out.println(Color.RED.getClass()); // class Color

        System.out.println(Color.GREEN.getClass().getName());        // 例如 Color$1
        System.out.println(Color.GREEN.getDeclaringClass().getName()); // Color
        System.out.println(Color.class.getSuperclass());             // class java.lang.Enum
    }
}
```

说明：

- 普通枚举常量的 `getClass()` 通常是枚举类本身。
- 如果枚举常量带类体，`getClass()` 可能是编译器生成的子类。
- `getDeclaringClass()` 可得到常量所属的枚举类型。

---

### 5.3 泛型擦除

```java
import java.util.ArrayList;
import java.util.List;

public class GenericErasureDemo {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();

        System.out.println(list.getClass() == ArrayList.class); // true
        System.out.println(list.getClass().getName());          // java.util.ArrayList
    }
}
```

说明：

- 泛型信息在运行时被擦除。
- `List<String>` 和 `List<Integer>` 的运行时对象类型都可能是 `ArrayList`。
- `getClass()` 不能直接获取 `List<String>` 这种泛型参数信息。

---

## 6. 配合反射使用

`getClass()` 是反射的常见入口。

```java
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

class Person {
    private String name;

    public Person() {
    }

    public void hello() {
        System.out.println("hello");
    }
}

public class ReflectionDemo {
    public static void main(String[] args) throws Exception {
        Object obj = new Person();

        Class<?> clazz = obj.getClass();

        System.out.println(clazz.getName()); // Person

        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            System.out.println(m.getName());
        }

        Constructor<?> constructor = clazz.getDeclaredConstructor();
        Object newObj = constructor.newInstance();

        System.out.println(newObj.getClass() == Person.class); // true
    }
}
```

说明：

- `obj.getClass()` 得到运行时类。
- 再通过 `Class` 对象获取构造器、方法、字段、注解等信息。
- 可用于框架、序列化、依赖注入、动态代理等场景。

---

## 7. 常见易错点

### 7.1 对 `null` 调用会抛异常

```java
Object o = null;
o.getClass(); // NullPointerException
```

因为 `getClass()` 是实例方法，必须先有对象。

---

### 7.2 基本类型不能直接调用 `getClass()`

```java
// int x = 1;
// x.getClass(); // 编译错误
```

基本类型不是对象。但可以使用类字面量：

```java
Class<?> c1 = int.class;
Class<?> c2 = Integer.valueOf(1).getClass();

System.out.println(c1);        // int
System.out.println(c2);        // class java.lang.Integer
System.out.println(c1 == c2);  // false
System.out.println(Integer.TYPE == int.class); // true
```

---

### 7.3 `getClass()` 与 `instanceof` 不同

```java
class A {}
class B extends A {}

A a = new B();

System.out.println(a.getClass() == A.class); // false
System.out.println(a instanceof A);          // true
```

- `getClass() == X.class`：精确类型判断。
- `instanceof X`：是否属于 X 或其子类型。

---

### 7.4 类加载器不同可能导致 `Class` 对象不同

同一个类名，如果被不同类加载器加载，得到的 `Class` 对象可能不相等：

```java
Class<?> c = obj.getClass();
System.out.println(c.getClassLoader());
```

因此在插件化、热部署、多类加载器环境中，不能只依赖类名判断类型。

---

### 7.5 在构造器中调用 `getClass()` 要小心

```java
class Parent {
    Parent() {
        System.out.println(getClass());
    }
}

class Child extends Parent {
    int value = 1;
}

new Child();
```

输出类似：

```text
class Child
```

在父类构造器中调用 `getClass()`，可能已经返回子类类型，但此时子类字段可能尚未初始化，因此要谨慎。

---

## 8. 总结

`Object.getClass()` 的核心作用：

```java
Class<?> clazz = obj.getClass();
```

它返回对象的**运行时类**的 `Class` 对象。

主要用途：

1. 获取对象真实类型；
2. 精确类型比较：`obj.getClass() == Xxx.class`；
3. 反射入口：获取方法、字段、构造器、注解；
4. 数组、匿名类、枚举、代理等运行时类型分析；
5. 配合 `isInstance`、`isAssignableFrom` 做类型判断。

注意：

- `getClass()` 是 `final native`，不能重写；
- 对 `null` 调用抛 `NullPointerException`；
- 基本类型不能直接调用；
- 它返回运行时类型，不是声明类型；
- 精确类型判断用 `getClass() ==`，包含子类型判断用 `instanceof`；
- 泛型参数在运行时会被擦除，`getClass()` 不能直接获取泛型实参。
