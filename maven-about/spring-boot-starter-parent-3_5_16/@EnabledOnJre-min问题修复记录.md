# @EnabledOnJre min 方法无法解析问题修复记录

## 问题描述

`ConditionalTestDemo.java` 中使用 `@EnabledOnJre(min = JRE.JAVA_17)` 时，IDE 和编译器报错：

```
Cannot resolve method 'min'
位置: @interface org.junit.jupiter.api.condition.EnabledOnJre
```

涉及代码行：
- 第 61 行：`@EnabledOnJre(min = JRE.JAVA_17)`
- 第 118 行：`@EnabledOnJre(min = JRE.JAVA_17)`

## 根本原因

**`min` 属性不属于 `@EnabledOnJre` 注解。**

在 JUnit 5 中，JRE 版本条件注解分为两组：

| 注解 | 用途 | 支持的属性 |
|------|------|-----------|
| `@EnabledOnJre` | 匹配**特定** JRE 版本 | `value`（JRE 数组）、`versions`（int 数组） |
| `@EnabledForJreRange` | 匹配**范围**内的 JRE 版本 | `min`、`max`、`minVersion`、`maxVersion` |

`min` 和 `max` 是 `@EnabledForJreRange` / `@DisabledForJreRange` 的属性，用于指定版本范围的上下界。

## 环境信息

- Spring Boot：3.5.16
- JUnit Jupiter：5.12.2
- Java：17

## 修复方案

将 `@EnabledOnJre(min = ...)` 替换为 `@EnabledForJreRange(min = ...)`。

### 修改前

```java
@EnabledOnJre(min = JRE.JAVA_17)
```

### 修改后

```java
@EnabledForJreRange(min = JRE.JAVA_17)
```

## 修改位置

| 文件 | 行号 | 修改内容 |
|------|------|---------|
| `ConditionalTestDemo.java` | 61 | `@EnabledOnJre(min = ...)` → `@EnabledForJreRange(min = ...)` |
| `ConditionalTestDemo.java` | 118 | `@EnabledOnJre(min = ...)` → `@EnabledForJreRange(min = ...)` |

## JUnit 5 JRE 条件注解速查

### @EnabledOnJre — 匹配特定版本

```java
// 在 Java 17 上执行
@EnabledOnJre(JRE.JAVA_17)

// 在 Java 17 或 21 上执行
@EnabledOnJre({JRE.JAVA_17, JRE.JAVA_21})
```

### @EnabledForJreRange — 匹配版本范围

```java
// Java 17 及以上
@EnabledForJreRange(min = JRE.JAVA_17)

// Java 17 ~ 21
@EnabledForJreRange(min = JRE.JAVA_17, max = JRE.JAVA_21)

// Java 21 及以下
@EnabledForJreRange(max = JRE.JAVA_21)

// 使用整数版本号（JUnit 5.12+ 支持）
@EnabledForJreRange(minVersion = 17)
@EnabledForJreRange(minVersion = 17, maxVersion = 21)
```

### @DisabledOnJre / @DisabledForJreRange — 禁用对应版本

用法与上述 Enabled 版本对称，将 `Enabled` 替换为 `Disabled` 即可。

## 验证

修复后执行 `mvn test-compile`，`ConditionalTestDemo.java` 相关编译错误消除。

```
mvn test-compile
```

剩余编译错误均来自 `MockitoTestDemo.java`（缺少 `User`、`UserRepository`、`UserService` 类），与本问题无关。
