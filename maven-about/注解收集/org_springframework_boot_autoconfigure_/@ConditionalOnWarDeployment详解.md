# @ConditionalOnWarDeployment 详解

## 概述

`@ConditionalOnWarDeployment` 是 Spring Boot 自动配置条件注解，用于当应用程序是传统 WAR 部署时匹配，通常用于 WAR 部署特定的自动配置。

## 来源

`@ConditionalOnWarDeployment` 属于 Spring Boot 框架，包路径：`org.springframework.boot.autoconfigure.condition.ConditionalOnWarDeployment`

## 所需依赖

`@ConditionalOnWarDeployment` 是 Spring Boot 自动配置功能的核心，需要以下依赖：

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
       └─ spring-boot-autoconfigure  ← 包含 @ConditionalOnWarDeployment
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

- 当应用程序是传统 WAR 部署时匹配
- 用于 WAR 部署特定的自动配置
- 检查应用程序是否以 WAR 方式部署
- 通常用于传统应用服务器环境

## 默认值

`@ConditionalOnWarDeployment` 没有额外属性，它是一个简单的条件注解。

## 使用方式

### 1. 基本使用

```java
@AutoConfiguration
@ConditionalOnWarDeployment
public class WarDeploymentAutoConfiguration {
    // 当应用程序是 WAR 部署时应用
}
```

### 2. 结合其他条件注解使用

```java
@AutoConfiguration
@ConditionalOnWarDeployment
@ConditionalOnClass(DataSource.class)
@ConditionalOnProperty(prefix = "spring.datasource", name = "jndi-name")
public class WarDataSourceAutoConfiguration {
    // 当应用程序是 WAR 部署且类路径上存在 DataSource 类且配置了 JNDI 名称时应用
}
```

### 3. 用于 JNDI 数据源

```java
@AutoConfiguration
@ConditionalOnWarDeployment
@ConditionalOnJndi("java:comp/env/jdbc/myDS")
public class JndiDataSourceAutoConfiguration {
    // 当应用程序是 WAR 部署且能查找 JNDI 位置时应用
}
```

### 4. 用于传统应用服务器

```java
@AutoConfiguration
@ConditionalOnWarDeployment
@ConditionalOnClass(Servlet.class)
public class ServletContainerAutoConfiguration {
    // 当应用程序是 WAR 部署且类路径上存在 Servlet 类时应用
}
```

## 工作原理

`@ConditionalOnWarDeployment` 通过以下机制工作：

1. **部署检查**：检查应用程序是否以 WAR 方式部署
2. **条件评估**：根据部署检查结果决定是否应用自动配置
3. **bean 创建**：根据条件评估结果，决定是否创建和配置 bean
4. **传统环境**：确保配置只在传统 WAR 部署环境中应用

## 与其他注解的关系

### 1. 与 @ConditionalOnNotWarDeployment 的关系

`@ConditionalOnWarDeployment` 和 `@ConditionalOnNotWarDeployment` 是一对互补的注解：

```java
@AutoConfiguration
@ConditionalOnWarDeployment
public class WarDeploymentAutoConfiguration {
    // 当是 WAR 部署时应用
}

@AutoConfiguration
@ConditionalOnNotWarDeployment
public class EmbeddedWebServerAutoConfiguration {
    // 当不是 WAR 部署时应用
}
```

### 2. 与 @ConditionalOnWebApplication 的关系

`@ConditionalOnWarDeployment` 检查部署方式，而 `@ConditionalOnWebApplication` 检查应用程序类型：

```java
@AutoConfiguration
@ConditionalOnWarDeployment
public class WarDeploymentAutoConfiguration {
    // 检查部署方式
}

@AutoConfiguration
@ConditionalOnWebApplication
public class WebAutoConfiguration {
    // 检查应用程序类型
}
```

## 版本变化

| 版本 | 变化 |
|------|------|
| Spring Boot 1.0.0 | 引入 `@ConditionalOnWarDeployment` |

## 最佳实践

1. **WAR 部署配置** - 使用 `@ConditionalOnWarDeployment` 为 WAR 部署提供特定配置
2. **传统环境** - 用于传统应用服务器环境
3. **结合其他条件** - 与 `@ConditionalOnJndi` 等其他条件注解一起使用
4. **避免过度使用** - 只在确实需要 WAR 部署特定配置时使用
5. **测试条件效果** - 测试条件注解是否按预期工作

## 注意事项

1. `@ConditionalOnWarDeployment` 只能用于自动配置类
2. 部署检查是基于应用程序的打包方式
3. 与 `@ConditionalOnNotWarDeployment` 相反
4. 条件注解只能在自动配置类上使用，不能在普通配置类上使用
5. 用户自定义的 bean 总是优先于自动配置的 bean

## 常见问题

### Q: 为什么我的自动配置没有生效？

A: 检查是否以 WAR 方式部署，或者是否使用了正确的条件注解。

### Q: 与 @ConditionalOnNotWarDeployment 的区别？

A: `@ConditionalOnWarDeployment` 在是 WAR 部署时匹配，而 `@ConditionalOnNotWarDeployment` 在不是 WAR 部署时匹配。

### Q: 如何测试条件效果？

A: 使用 `--debug` 启动参数查看自动配置的条件评估情况。

### Q: 可以在非自动配置类上使用吗？

A: 不可以，条件注解只能在自动配置类上使用。

### Q: 适用于哪些场景？

A: 适用于传统应用服务器、JNDI 数据源等 WAR 部署特定场景。