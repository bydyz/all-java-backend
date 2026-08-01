# @ConditionalOnMissingClass 详解

## 概述

`@ConditionalOnMissingClass` 是 Spring Boot 自动配置条件注解，用于当指定类不在类路径上时匹配，通常用于提供替代实现。

## 来源

`@ConditionalOnMissingClass` 属于 Spring Boot 框架，包路径：`org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass`

## 所需依赖

`@ConditionalOnMissingClass` 是 Spring Boot 自动配置功能的核心，需要以下依赖：

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
       └─ spring-boot-autoconfigure  ← 包含 @ConditionalOnMissingClass
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

- 当指定类不在类路径上时匹配
- 用于提供替代实现
- 与 `@ConditionalOnClass` 相反
- 可以指定一个或多个类

## 默认值

`@ConditionalOnMissingClass` 有以下属性：

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `value` | 默认为空 | 要检查的类 |
| `name` | 默认为空 | 要检查的类名（字符串形式） |

## 使用方式

### 1. 基本使用

```java
@AutoConfiguration
@ConditionalOnMissingClass("org.apache.catalina.startup.Tomcat")
public class JettyAutoConfiguration {
    // 当类路径上不存在 Tomcat 类时应用
}
```

### 2. 指定多个类

```java
@AutoConfiguration
@ConditionalOnMissingClass({"org.apache.catalina.startup.Tomcat", "org.eclipse.jetty.server.Server"})
public class UndertowAutoConfiguration {
    // 当类路径上不存在 Tomcat 和 Jetty 类时应用
}
```

### 3. 使用类引用指定

```java
@AutoConfiguration
@ConditionalOnMissingClass(Tomcat.class)
public class JettyAutoConfiguration {
    // 当类路径上不存在 Tomcat 类时应用
}
```

### 4. 结合其他条件注解使用

```java
@AutoConfiguration
@ConditionalOnMissingClass("org.apache.catalina.startup.Tomcat")
@ConditionalOnProperty(prefix = "server", name = "type", havingValue = "jetty")
public class JettyAutoConfiguration {
    // 当类路径上不存在 Tomcat 类且配置了服务器类型为 jetty 时应用
}
```

## 工作原理

`@ConditionalOnMissingClass` 通过以下机制工作：

1. **类路径检查**：检查指定的类是否在类路径上
2. **条件评估**：根据类路径检查结果决定是否应用自动配置
3. **bean 创建**：根据条件评估结果，决定是否创建和配置 bean
4. **替代实现**：提供默认的替代实现

## 与其他注解的关系

### 1. 与 @ConditionalOnClass 的关系

`@ConditionalOnMissingClass` 和 `@ConditionalOnClass` 是一对互补的注解：

```java
@AutoConfiguration
@ConditionalOnClass(Tomcat.class)
public class TomcatAutoConfiguration {
    // 当存在 Tomcat 类时应用
}

@AutoConfiguration
@ConditionalOnMissingClass("org.apache.catalina.startup.Tomcat")
public class JettyAutoConfiguration {
    // 当不存在 Tomcat 类时应用
}
```

### 2. 与 @ConditionalOnMissingBean 的关系

`@ConditionalOnMissingClass` 检查类路径，而 `@ConditionalOnMissingBean` 检查 bean：

```java
@AutoConfiguration
@ConditionalOnMissingClass("org.apache.catalina.startup.Tomcat")
public class JettyAutoConfiguration {
    // 检查类路径
}

@AutoConfiguration
@ConditionalOnMissingBean
public MyService myService() {
    // 检查 bean
}
```

## 版本变化

| 版本 | 变化 |
|------|------|
| Spring Boot 1.0.0 | 引入 `@ConditionalOnMissingClass` |
| Spring Boot 2.0.0 | 支持 `name` 属性，允许使用类名字符串 |

## 最佳实践

1. **提供替代实现** - 用于在缺少特定类时提供替代实现
2. **优先使用类引用** - 使用类引用比使用类名字符串更安全
3. **结合其他条件** - 与 `@ConditionalOnMissingBean` 等其他条件注解一起使用
4. **避免过度使用** - 只在确实需要根据类路径条件时使用
5. **测试条件效果** - 测试条件注解是否按预期工作

## 注意事项

1. `@ConditionalOnMissingClass` 只能用于自动配置类
2. 类检查是基于类路径的，不是基于实例的
3. 可以使用类引用或类名字符串指定要检查的类
4. 条件注解只能在自动配置类上使用，不能在普通配置类上使用
5. 用户自定义的 bean 总是优先于自动配置的 bean

## 常见问题

### Q: 为什么我的自动配置没有生效？

A: 检查是否缺少必要的类路径依赖，或者是否使用了正确的条件注解。

### Q: 可以指定多个类吗？

A: 可以，`@ConditionalOnMissingClass` 的 `value` 属性接受数组。

### Q: 与 @ConditionalOnClass 的区别？

A: `@ConditionalOnMissingClass` 在类不存在时匹配，而 `@ConditionalOnClass` 在类存在时匹配。

### Q: 如何测试条件效果？

A: 使用 `--debug` 启动参数查看自动配置的条件评估情况。

### Q: 可以在非自动配置类上使用吗？

A: 不可以，条件注解只能在自动配置类上使用。