# Java `instanceof` 详解

## 一、Type 是类的情况

```java
class A { }
class B extends A { }
class C extends B { }

public class Test {
    public static void main(String[] args) {
        A obj = new C();          // 静态类型是 A，运行时类型是 C

        System.out.println(obj instanceof A);   // true  —— 本身就是 A
        System.out.println(obj instanceof B);   // true  —— A 的子类
        System.out.println(obj instanceof C);   // true  —— 孙子类
        System.out.println(obj instanceof Object);// true  —— 根类

        A a = new A();
        System.out.println(a instanceof C);     // false —— 不是 C 的实例
    }
}
```

要点：`obj` 变量的**声明类型（静态类型）不影响判断**，`instanceof` 看的是 `new` 出来的真实对象。

---

## 二、Type 是接口的情况

```java
interface Flyable { }

class Animal { }
class Bird extends Animal implements Flyable { }
class Sparrow extends Bird { }

public class Test2 {
    public static void main(String[] args) {
        Animal obj = new Sparrow();

        System.out.println(obj instanceof Flyable); // true —— 实现类的子类的实例
        System.out.println(obj instanceof Animal);  // true
        System.out.println(obj instanceof Bird);    // true
        System.out.println(obj instanceof Sparrow); // true

        Animal plain = new Animal();
        System.out.println(plain instanceof Flyable); // false —— 没有实现该接口
    }
}
```

接口同样遵循"实现类 + 实现类的任意层级子类都算"的规则。

---

## 三、必须注意的 6 个边界情况

### 1. `null instanceof X` 永远返回 `false`（不抛异常）

```java
Object o = null;
System.out.println(o instanceof Object); // false
```

### 2. 编译期类型不兼容会直接报错

```java
String s = "hello";
// System.out.println(s instanceof Integer);
// ❌ 编译错误：incompatible types，String 是 final，不可能有 Integer 子类

class A { }
class B { }
A a = new A();
// System.out.println(a instanceof B);
// ❌ 编译错误：两个不相干的类
```

但如果左边静态类型是**非 final 类**，右边的接口/类**有可能**被其子类实现/继承，编译器就放行：

```java
interface I { }
class A { }        // 非 final

A a = new A();
System.out.println(a instanceof I);
// ✅ 编译通过（A 的子类将来可能实现 I），运行时输出 false
```

同理，若左边静态类型是**接口**，几乎任何类都可能是它的实现，一般都能编译通过。

### 3. 数组也有特殊规则

```java
int[] arr = {1, 2, 3};
System.out.println(arr instanceof int[]);              // true
System.out.println(arr instanceof Object);             // true
System.out.println(arr instanceof Cloneable);          // true
System.out.println(arr instanceof java.io.Serializable);// true
```

### 4. 泛型擦除：不能带具体泛型参数

```java
List<String> list = new ArrayList<>();
// System.out.println(list instanceof List<String>); // ❌ 编译错误
System.out.println(list instanceof List);           // ✅ true（原始类型）
System.out.println(list instanceof List<?>);        // ✅ true（通配符可以）
```

### 5. Java 16+ 模式匹配（顺手提一下）

```java
Object obj = "hello";
if (obj instanceof String s) {     // 判断 + 强转 + 绑定变量一步完成
    System.out.println(s.length()); // 5
}
```

### 6. `instanceof` 与 `getClass() ==` 的区别

```java
A obj = new C();

obj instanceof A        // true （包含子类）
obj.getClass() == A.class // false（必须精确相等）
```

---

## 四、总结对照表

| 场景                                        | `obj instanceof Type` 结果                 |
|-------------------------------------------|------------------------------------------|
| obj 是 Type 的实例                            | `true`                                   |
| obj 是 Type 子类/孙类…的实例                      | `true`                                   |
| Type 是接口，obj 是实现类/其子类实例                   | `true`                                   |
| `obj == null`                             | `false`                                  |
| obj 与 Type 无任何继承/实现关系                     | 编译报错（final 或封闭类）或运行时 `false`（可能存在子类实现接口） |
| Type 带具体泛型参数 `List<String>`               | 编译错误（擦除），用 `List` 或 `List<?>`            |
| 数组 vs `Object`/`Cloneable`/`Serializable` | `true`                                   |

所以你的理解完全正确：**`instanceof` = 运行时类型的"是不是（含继承/实现链上任意一层）"判断**
，只是在写代码时还要注意编译期静态类型和 `null` 的限制。
