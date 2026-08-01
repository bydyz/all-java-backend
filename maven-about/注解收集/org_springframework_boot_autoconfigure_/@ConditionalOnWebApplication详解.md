# @ConditionalOnWebApplication 详解

## 概述

`@ConditionalOnWebApplication` 是 Spring Boot 自动配置条件注解，用于当应用程序是 Web 应用程序时匹配，是 Web 自动配置中常用的条件注解。

## 来源

`@ConditionalOnWebApplication` 属于 Spring Boot 框架，包路径：`org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication`

## 所需依赖

`@ConditionalOnWebApplication` 是 Spring Boot 自动配置功能的核心，需要以下依赖：

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
       └─ spring-boot-autoconfigure  ← 包含 @ConditionalOnWebApplication
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

- 当应用程序是 Web 应用程序时匹配
- 用于根据 Web 应用程序类型启用自动配置
- 支持检查 Servlet、Reactive 等 Web 应用程序类型
- Web 自动配置中常用的条件注解

## 默认值

`@ConditionalOnWebApplication` 有以下属性：

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `type` | `ConditionalOnWebApplication.Type.SERVLET` | Web 应用程序类型 |

## Web 应用程序类型

| 类型 | 说明 |
|------|------|
| `Type.SERVLET` | Servlet Web 应用程序（默认） |
| `Type.REACTIVE` | Reactive Web 应用程序 |
| `Type.ANY` | 任何 Web 应用程序 |

## 使用方式

### 1. 基本使用

```java
@AutoConfiguration
@ConditionalOnWebApplication
public class WebMvcAutoConfiguration {
    // 当应用程序是 Web 应用程序时应用
}
```

### 2. 指定 Servlet 类型

```java
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletWebAutoConfiguration {
    // 当应用程序是 Servlet Web 应用程序时应用
}
```

### 3. 指定 Reactive 类型

```java
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveWebAutoConfiguration {
    // 当应用程序是 Reactive Web 应用程序时应用
}
```

### 4. 结合其他条件注解使用

```java
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(Servlet.class)
@ConditionalOnProperty(prefix = "server", name = "port", havingValue = "8080")
public class ServletWebAutoConfiguration {
    // 当应用程序是 Servlet Web 应用程序且类路径上存在 Servlet 类且配置了服务器端口时应用
}
```

## 工作原理

`@ConditionalOnWebApplication` 通过以下机制工作：

1. **应用程序检查**：检查当前应用程序是否是 Web 应用程序
2. **类型检查**：检查 Web 应用程序类型
3. **条件评估**：根据检查结果决定是否应用自动配置
4. **bean 创建**：根据条件评估结果，决定是否创建和配置 bean

## 与其他注解的关系

### 1. 与 @ConditionalOnNotWebApplication 的关系

`@ConditionalOnWebApplication` 和 `@ConditionalOnNotWebApplication` 是一对互补的注解：

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

`@ConditionalOnWebApplication` 检查应用程序类型，而 `@ConditionalOnClass` 检查类路径：

```java
@AutoConfiguration
@ConditionalOnWebApplication
public class WebAutoConfiguration {
    // 检查应用程序类型
}

@AutoConfiguration
@ConditionalOnClass(Servlet.class)
public class ServletAutoConfiguration {
    // 检查类路径
}
```

## 版本变化

| 版本 | 变化 |
|------|------|
| Spring Boot 1.0.0 | 引入 `@ConditionalOnWebApplication` |
| Spring Boot 2.0.0 | 支持 `type` 属性，允许指定 Web 应用程序类型 |

## 最佳实践

1. **Web 自动配置** - 使用 `@ConditionalOnWebApplication` 为 Web 应用程序提供特定配置
2. **指定正确的类型** - 根据需要指定 Web 应用程序类型
3. **结合其他条件** - 与 `@ConditionalOnClass` 等其他条件注解一起使用
4. **避免过度使用** - 只在确实需要根据 Web 应用程序类型条件时使用
5. **测试条件效果** - 测试条件注解是否按预期工作

## 注意事项

1. `@ConditionalOnWebApplication` 只能用于自动配置类
2. 应用程序检查是基于 Web 应用程序类型的
3. 默认检查 Servlet Web 应用程序
4. 条件注解只能在自动配置类上使用，不能在普通配置类上使用
5. 用户自定义的 bean 总是优先于自动配置的 bean

## 常见问题

### Q: 为什么我的自动配置没有生效？

A: 检查是否是 Web 应用程序，或者是否使用了正确的条件注解。

### Q: 可以指定 Web 应用程序类型吗？

A: 可以，`@ConditionalOnWebApplication` 的 `type` 属性允许指定 Web 应用程序类型。

### Q: 与 @ConditionalOnNotWebApplication 的区别？

A: `@ConditionalOnWebApplication` 在是 Web 应用程序时匹配，而 `@ConditionalOnNotWebApplication` 在不是 Web 应用程序时匹配。

### Q: 如何测试条件效果？

A: 使用 `--debug` 启动参数查看自动配置的条件评估情况。

### Q: 可以在非自动配置类上使用吗？

A: 不可以，条件注解只能在自动配置类上使用。