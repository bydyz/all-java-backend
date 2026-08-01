# @ConditionalOnNotWebApplication 详解

## 概述

`@ConditionalOnNotWebApplication` 是 Spring Boot 自动配置条件注解，用于当应用程序不是 Web 应用程序时匹配，通常用于非 Web 环境的自动配置。

## 来源

`@ConditionalOnNotWebApplication` 属于 Spring Boot 框架，包路径：`org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication`

## 所需依赖

`@ConditionalOnNotWebApplication` 是 Spring Boot 自动配置功能的核心，需要以下依赖：

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
       └─ spring-boot-autoconfigure  ← 包含 @ConditionalOnNotWebApplication
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

- 当应用程序不是 Web 应用程序时匹配
- 用于非 Web 环境的自动配置
- 与 `@ConditionalOnWebApplication` 相反
- 用于命令行工具、批处理等非 Web 应用

## 默认值

`@ConditionalOnNotWebApplication` 没有额外属性，它是一个简单的条件注解。

## 使用方式

### 1. 基本使用

```java
@AutoConfiguration
@ConditionalOnNotWebApplication
public class CommandLineRunnerAutoConfiguration {
    // 当应用程序不是 Web 应用程序时应用
}
```

### 2. 结合其他条件注解使用

```java
@AutoConfiguration
@ConditionalOnNotWebApplication
@ConditionalOnClass(DataSource.class)
@ConditionalOnProperty(prefix = "spring.datasource", name = "url")
public class CommandLineDataSourceAutoConfiguration {
    // 当应用程序不是 Web 应用程序且类路径上存在 DataSource 类且配置了数据源 URL 时应用
}
```

### 3. 用于批处理应用

```java
@AutoConfiguration
@ConditionalOnNotWebApplication
@ConditionalOnClass(BatchJob.class)
public class BatchAutoConfiguration {
    // 当应用程序不是 Web 应用程序且类路径上存在 BatchJob 类时应用
}
```

### 4. 用于命令行工具

```java
@AutoConfiguration
@ConditionalOnNotWebApplication
@ConditionalOnProperty(prefix = "app.cli", name = "enabled", havingValue = "true")
public class CommandLineToolAutoConfiguration {
    // 当应用程序不是 Web 应用程序且启用了 CLI 工具时应用
}
```

## 工作原理

`@ConditionalOnNotWebApplication` 通过以下机制工作：

1. **应用程序检查**：检查当前应用程序是否是 Web 应用程序
2. **条件评估**：根据检查结果决定是否应用自动配置
3. **bean 创建**：根据条件评估结果，决定是否创建和配置 bean
4. **非 Web 环境**：确保配置只在非 Web 环境中应用

## 与其他注解的关系

### 1. 与 @ConditionalOnWebApplication 的关系

`@ConditionalOnNotWebApplication` 和 `@ConditionalOnWebApplication` 是一对互补的注解：

```java
@AutoConfiguration
@ConditionalOnWebApplication
public class WebAutoConfiguration {
    // 当是 Web 应用程序时应用
}

@AutoConfiguration
@ConditionalOnNotWebApplication
public class CommandLineRunnerAutoConfiguration {
    // 当不是 Web 应用程序时应用
}
```

### 2. 与 @ConditionalOnClass 的关系

`@ConditionalOnNotWebApplication` 检查应用程序类型，而 `@ConditionalOnClass` 检查类路径：

```java
@AutoConfiguration
@ConditionalOnNotWebApplication
public class CommandLineRunnerAutoConfiguration {
    // 检查应用程序类型
}

@AutoConfiguration
@ConditionalOnClass(DataSource.class)
public class DataSourceAutoConfiguration {
    // 检查类路径
}
```

## 版本变化

| 版本 | 变化 |
|------|------|
| Spring Boot 1.0.0 | 引入 `@ConditionalOnNotWebApplication` |

## 最佳实践

1. **非 Web 环境配置** - 使用 `@ConditionalOnNotWebApplication` 为非 Web 环境提供特定配置
2. **批处理应用** - 用于批处理、命令行工具等非 Web 应用
3. **结合其他条件** - 与 `@ConditionalOnClass` 等其他条件注解一起使用
4. **避免过度使用** - 只在确实需要根据应用程序类型条件时使用
5. **测试条件效果** - 测试条件注解是否按预期工作

## 注意事项

1. `@ConditionalOnNotWebApplication` 只能用于自动配置类
2. 应用程序检查是基于 Web 应用程序类型的
3. 与 `@ConditionalOnWebApplication` 相反
4. 条件注解只能在自动配置类上使用，不能在普通配置类上使用
5. 用户自定义的 bean 总是优先于自动配置的 bean

## 常见问题

### Q: 为什么我的自动配置没有生效？

A: 检查是否是 Web 应用程序，或者是否使用了正确的条件注解。

### Q: 与 @ConditionalOnWebApplication 的区别？

A: `@ConditionalOnNotWebApplication` 在不是 Web 应用程序时匹配，而 `@ConditionalOnWebApplication` 在是 Web 应用程序时匹配。

### Q: 如何测试条件效果？

A: 使用 `--debug` 启动参数查看自动配置的条件评估情况。

### Q: 可以在非自动配置类上使用吗？

A: 不可以，条件注解只能在自动配置类上使用。

### Q: 适用于哪些场景？

A: 适用于批处理、命令行工具、数据迁移等非 Web 应用场景。