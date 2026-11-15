# Java 中 Class 类的常用方法详解

`java.lang.Class` 是 Java 反射机制的入口。JVM 在加载每一个类时，都会为之创建一个 `Class` 对象，程序在运行期通过它来读取类的元数据（类名、字段、方法、构造器、父类、接口、注解等），并动态创建实例或调用方法。下面按功能分类说明常用方法，每个方法尽量配一段可运行的示例。

---

## 获取 Class 对象的三种方式

在调用 `Class` 的方法之前，先要拿到 `Class` 对象。常见的有三种途径：

```java
// 1. 类字面常量（最安全，不触发类初始化）
Class<?> clazz1 = String.class;

// 2. 通过对象实例
String str = "hello";
Class<?> clazz2 = str.getClass();

// 3. 通过全限定名（动态性最强，会触发类初始化）
Class<?> clazz3 = Class.forName("java.lang.String");
```

其中 `Class.forName()` 会触发类的初始化（执行静态代码块），而 `.class` 方式不会。

---

## 获取类的基本信息

### `getName()`、`getSimpleName()`、`getPackage()`

```java
Class<?> clazz = Student.class; // 假设 com.example.Student

System.out.println(clazz.getName());          // com.example.Student
System.out.println(clazz.getSimpleName());    // Student
System.out.println(clazz.getPackage().getName()); // com.example
```

`getName()` 返回全限定名，`getSimpleName()` 只返回类名，`getPackage()` 返回包信息。

### `getSuperclass()`、`getInterfaces()`

```java
Class<?> superClass = clazz.getSuperclass();
System.out.println(superClass.getName());     // java.lang.Object（若 Student 无显式父类）

Class<?>[] interfaces = clazz.getInterfaces();
for (Class<?> itf : interfaces) {
    System.out.println(itf.getSimpleName());
}
```

`getSuperclass()` 返回直接父类的 `Class`（接口或 `Object` 的父类为 `null`）；`getInterfaces()` 返回该类直接实现的接口（不包括父类间接实现的）。

### 类型检查：`isInterface()`、`isEnum()`、`isAnnotation()`、`isPrimitive()`

```java
System.out.println(clazz.isInterface());  // false
System.out.println(clazz.isEnum());       // false
System.out.println(clazz.isAnnotation()); // false
System.out.println(int.class.isPrimitive()); // true
```

这些方法用于判断 `Class` 所代表的类型类别，常用于框架中根据类型走不同分支逻辑。

---

## 获取构造器

### `getConstructors()` / `getDeclaredConstructors()`

```java
Constructor<?>[] publicCtors = clazz.getConstructors();
// 只返回 public 构造器

Constructor<?>[] allCtors = clazz.getDeclaredConstructors();
// 返回所有访问级别的构造器（public / protected / 包级 / private）
```

### `getConstructor(Class<?>...)` / `getDeclaredConstructor(Class<?>...)`

```java
// 获取指定参数类型的公有构造器
Constructor<?> ctor = clazz.getConstructor(String.class, int.class);
Object obj = ctor.newInstance("张三", 20);

// 若构造器是 private，用 getDeclaredConstructor + setAccessible
Constructor<?> privateCtor = clazz.getDeclaredConstructor();
privateCtor.setAccessible(true);
Object obj2 = privateCtor.newInstance();
```

`getConstructor` 只能找到 `public` 构造器；`getDeclaredConstructor` 可以找到任意访问级别的构造器，但访问非 `public` 构造器前需要 `setAccessible(true)`。

---

## 获取字段

### `getFields()` / `getDeclaredFields()`

```java
Field[] publicFields = clazz.getFields();
// 返回所有 public 字段，包括从父类继承的 public 字段

Field[] allFields = clazz.getDeclaredFields();
// 返回本类声明的所有字段（含 private、protected、包级），不包括继承字段
```

### `getField(String)` / `getDeclaredField(String)`

```java
Field nameField = clazz.getDeclaredField("name");
nameField.setAccessible(true); // 若是 private 字段

Object fieldValue = nameField.get(obj);   // 读取字段值
nameField.set(obj, "李四");               // 修改字段值
```

`getField` 只能获取 `public` 字段（含继承），`getDeclaredField` 可以获取本类声明的任意访问级别字段，但不含继承。

---

## 获取方法

### `getMethods()` / `getDeclaredMethods()`

```java
Method[] publicMethods = clazz.getMethods();
// 返回所有 public 方法，包括从父类继承的 public 方法

Method[] declaredMethods = clazz.getDeclaredMethods();
// 返回本类声明的所有方法（public / protected / private），不包括继承方法
```

### `getMethod(String, Class<?>...)` / `getDeclaredMethod(String, Class<?>...)`

```java
Method sayHello = clazz.getDeclaredMethod("sayHello", String.class);
sayHello.setAccessible(true);
Object result = sayHello.invoke(obj, "World");
```

`getMethod` 只查找 `public` 方法（包括继承的）；`getDeclaredMethod` 查找本类声明的任意访问级别方法，不查找继承方法。

---

## 类型兼容性判断：`isAssignableFrom` 与 `isInstance`

### `isAssignableFrom(Class<?> cls)`

判断**当前 `Class` 所代表的类型**是否是参数 `cls` 的**父类或父接口**（或相同类型）。

```java
System.out.println(Object.class.isAssignableFrom(String.class));  // true
System.out.println(String.class.isAssignableFrom(Object.class));  // false
System.out.println(Number.class.isAssignableFrom(Integer.class)); // true
```

典型用途是在插件框架中判断某个类是否实现了指定接口：

```java
if (Plugin.class.isAssignableFrom(clazz)) {
    Plugin plugin = (Plugin) clazz.getDeclaredConstructor().newInstance();
    plugin.execute();
}
```

`isAssignableFrom` 的判断逻辑是“**我是不是你的父类型**”。

### `isInstance(Object obj)`

判断**指定对象**是否是当前 `Class` 所代表类型的实例（运行时等价于 `obj instanceof 当前类型`）。

```java
Object obj = "hello";
System.out.println(String.class.isInstance(obj)); // true
System.out.println(Integer.class.isInstance(obj)); // false
```

`isInstance` 是 `instanceof` 的**运行时动态版本**，适用于编译期不知道具体类型、只能在运行期拿到 `Class` 对象的场景。

---

## 创建实例与常用实用方法

### `newInstance()`（已弃用）与推荐替代方式

`Class.newInstance()` 只能调用无参构造器，且在 Java 9 中已被标记为 `@Deprecated`。推荐使用：

```java
Object obj = clazz.getDeclaredConstructor().newInstance();
```

如果构造器有参数，则用 `getDeclaredConstructor(...).newInstance(args)`。

### `getEnumConstants()`

如果 `Class` 对象代表枚举类型，返回枚举常量数组；否则返回 `null`。

```java
enum Color { RED, GREEN, BLUE }

Color[] colors = Color.class.getEnumConstants();
// [RED, GREEN, BLUE]
```

### `getAnnotation(Class<A>)` / `isAnnotationPresent(Class<A>)`

用于读取运行期保留的注解，是 ORM、依赖注入等框架的基础手段。

```java
if (clazz.isAnnotationPresent(Table.class)) {
    Table table = clazz.getAnnotation(Table.class);
    System.out.println(table.name());
}
```

`isAnnotationPresent` 判断是否存在指定注解，`getAnnotation` 返回注解实例；若不存在则返回 `null`。

---

## 补充说明

- **`getXxx` 与 `getDeclaredXxx` 的核心区别**：前者只处理 `public` 成员（方法还包括从父类继承的 public 方法），后者处理本类声明的所有访问级别成员，但不包括继承成员。需要访问私有成员时，先 `getDeclaredXxx` 拿到对象，再调用 `setAccessible(true)`。
- **`Class` 对象由 JVM 在类加载时创建**，不能通过 `new Class(...)` 构造，只能通过上述三种途径获取。
