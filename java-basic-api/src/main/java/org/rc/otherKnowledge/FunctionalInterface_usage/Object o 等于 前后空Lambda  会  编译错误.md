# `Object o = () -> {};` 编译错误说明

`Object o = () -> {};` 这行代码无法编译通过，根本原因在于 **Lambda 表达式对“目标类型”有严格要求，而 `Object` 并不是一个函数式接口 (Functional Interface)**。

## 🎯 核心原因：`Object` 不是函数式接口

Lambda 表达式在 Java 中本身没有独立的“类型”。编译器在遇到 Lambda 时，需要根据它所在的上下文（即“目标类型”）来决定将其转换为什么具体类型。这个目标类型**必须是一个函数式接口**——也就是**只包含一个抽象方法**的接口。

`java.lang.Object` 是所有类的父类，但它本身**不是一个接口**，更不具备“单一抽象方法”的特征。因此，编译器无法将 `() -> {}` 这个 Lambda 表达式“适配”到 `Object` 类型上。

## 💻 编译器报错信息与代码示例

当你尝试编译包含该语句的代码时，编译器会给出明确的错误。

**❌ 错误代码示例**
```java
public class LambdaErrorDemo {
    public static void main(String[] args) {
        // 编译错误：The target type of this expression must be a functional interface
        Object o = () -> {};
    }
}
```
编译器报错信息通常为：
`The target type of this expression must be a functional interface`

## ✅ 如何修正？对比正确写法

要修正这个错误，你需要为 Lambda 表达式提供一个真正的函数式接口作为目标类型。

**方案一：使用标准的函数式接口（推荐）**
最直接的方式是使用 JDK 自带的 `Runnable` 接口，它恰好是一个无参、无返回值的函数式接口。
```java
// 正确写法：将 Lambda 赋值给 Runnable 类型
Runnable r = () -> {};
// 因为 Runnable 是 Object 的子类型，你也可以再将其赋值给 Object
Object o = r;
```
这种写法和传统的匿名内部类在语义上是等价的。

**方案二：通过强制类型转换（不常用但合法）**
你也可以先将 Lambda 强制转换为一个函数式接口，然后再赋值给 `Object`。
```java
// 通过强制转换，为 Lambda 明确指定目标类型
Object o = (Runnable) () -> {};
```
这样写是合法的，因为强制转换 `(Runnable)` 为 Lambda 表达式提供了必要的目标类型信息，使其能被成功转换，而 `Runnable` 实例本身也是一个 `Object`。

## 💎 总结

`Object o = () -> {};` 编译失败，本质上是**类型不匹配**的问题。Lambda 表达式需要一个“函数式接口”作为它的“归宿”，而 `Object` 无法充当这个角色。解决之道就是为它指定一个合适的函数式接口，如 `Runnable`。
