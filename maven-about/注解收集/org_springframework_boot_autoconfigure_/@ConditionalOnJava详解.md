# @ConditionalOnJava 详解

## 概述

`@ConditionalOnJava` 是 Spring Boot 自动配置条件注解，用于基于应用程序运行的 JVM 版本进行匹配，通常用于兼容性检查。

## 来源

`@ConditionalOnJava` 属于 Spring Boot 框架，包路径：`org.springframework.boot.autoconfigure.condition.ConditionalOnJava`

## 所需依赖

`@ConditionalOnJava` 是 Spring Boot 自动配置功能的核心，需要以下依赖：

### 最小依赖范围

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-autoconfigure</artifactId>
</dependency>
```

### 依赖传递链

```
spring-boot-starter-web
  └─ spring-boot-starter
       └─ spring-boot-autoconfigure  ← 包含 @ConditionalOnJava
```

### 可用依赖

```xml
<!-- 方式1：使用 Spring Boot Starter（推荐） -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- 方式2：最小依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-autoconfigure</artifactId>
</dependency>
```

> 使用 `spring-boot-starter-web` 可以使用，但引入了不需要的 web 依赖。如果只需要自动配置功能，使用 `spring-boot-autoconfigure` 即可。

## 功能

- 基于应用程序运行的 JVM 版本进行匹配
- 用于兼容性检查
- 支持版本范围检查
- 通常用于确保 JVM 版本兼容性

## 默认值

`@ConditionalOnJava` 有以下属性：

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `value` | 默认为空 | JVM 版本范围 |
| `range` | `Range.EQUAL_OR_GREATER` | 版本比较范围 |

## JVM 版本范围

| 范围 | 说明 |
|------|------|
| `Range.EQUAL_OR_GREATER` | 大于或等于指定版本（默认） |
| `Range.LESS_THAN` | 小于指定版本 |
| `Range.SPECIFIC_VERSION` | 等于指定版本 |
| `Range.BETWEEN` | 在指定版本之间 |

## 使用方式

### 1. 基本使用

```java
@AutoConfiguration
@ConditionalOnJava(JavaVersion.EIGHTEEN)
public class ModernJavaAutoConfiguration {
    // 当 JVM 版本为 Java 18 或更高时应用
}
```

### 2. 指定范围

```java
@AutoConfiguration
@ConditionalOnJava(value = JavaVersion.EIGHTEEN, range = Range.LESS_THAN)
public class LegacyJavaAutoConfiguration {
    // 当 JVM 版本小于 Java 18 时应用
}
```

### 3. 检查特定版本

```java
@AutoConfiguration
@ConditionalOnJava(value = JavaVersion.EIGHTEEN, range = Range.SPECIFIC_VERSION)
public class Java18AutoConfiguration {
    // 当 JVM 版本恰好为 Java 18 时应用
}
```

### 4. 结合其他条件注解使用

```java
@AutoConfiguration
@ConditionalOnJava(JavaVersion.EIGHTEEN)
@ConditionalOnClass(DataSource.class)
@ConditionalOnProperty(prefix = "spring.datasource", name = "url")
public class DataSourceAutoConfiguration {
    // 当 JVM 版本为 Java 18 或更高且类路径上存在 DataSource 类且配置了数据源 URL 时应用
}
```

## 工作原理

`@ConditionalOnJava` 通过以下机制工作：

1. **JVM 版本检查**：检查当前 JVM 版本
2. **版本比较**：根据指定的范围进行版本比较
3. **条件评估**：根据版本比较结果决定是否应用自动配置
4. **bean 创建**：根据条件评估结果，决定是否创建和配置 bean

## 与其他注解的关系

### 1. 与 @ConditionalOnClass 的关系

`@ConditionalOnJava` 检查 JVM 版本，而 `@ConditionalOnClass` 检查类路径：

```java
@AutoConfiguration
@ConditionalOnJava(JavaVersion.EIGHTEEN)
public class ModernJavaAutoConfiguration {
    // 检查 JVM 版本
}

@AutoConfiguration
@ConditionalOnClass(DataSource.class)
public class DataSourceAutoConfiguration {
    // 检查类路径
}
```

### 2. 与 @ConditionalOnProperty 的关系

`@ConditionalOnJava` 检查 JVM 版本，而 `@ConditionalOnProperty` 检查配置属性：

```java
@AutoConfiguration
@ConditionalOnJava(JavaVersion.EIGHTEEN)
public class ModernJavaAutoConfiguration {
    // 检查 JVM 版本
}

@AutoConfiguration
@ConditionalOnProperty(prefix = "spring.datasource", name = "url")
public class DataSourceAutoConfiguration {
    // 检查配置属性
}
```

## 版本变化

| 版本 | 变化 |
|------|------|
| Spring Boot 1.0.0 | 引入 `@ConditionalOnJava` |
| Spring Boot 2.0.0 | 支持 `range` 属性，允许指定版本比较范围 |

## 最佳实践

1. **兼容性检查** - 使用 `@ConditionalOnJava` 确保 JVM 版本兼容性
2. **指定正确的范围** - 根据需要指定版本比较范围
3. **结合其他条件** - 与 `@ConditionalOnClass` 等其他条件注解一起使用
4. **避免过度使用** - 只在确实需要根据 JVM 版本条件时使用
5. **测试条件效果** - 测试条件注解是否按预期工作

## 注意事项

1. `@ConditionalOnJava` 只能用于自动配置类
2. JVM 版本检查是基于运行时的
3. 可以指定版本范围
4. 条件注解只能在自动配置类上使用，不能在普通配置类上使用
5. 用户自定义的 bean 总是优先于自动配置的 bean

## 常见问题

### Q: 为什么我的自动配置没有生效？

A: 检查 JVM 版本是否满足条件，或者是否使用了正确的条件注解。

### Q: 可以指定版本范围吗？

A: 可以，`@ConditionalOnJava` 的 `range` 属性允许指定版本比较范围。

### Q: 与 @ConditionalOnClass 的区别？

A: `@ConditionalOnJava` 检查 JVM 版本，而 `@ConditionalOnClass` 检查类路径。

### Q: 如何测试条件效果？

A: 使用 `--debug` 启动参数查看自动配置的条件评估情况。

### Q: 可以在非自动配置类上使用吗？

A: 不可以，条件注解只能在自动配置类上使用。