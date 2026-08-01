# @ConditionalOnClass 详解

## 概述

`@ConditionalOnClass` 是 Spring Boot 自动配置条件注解，用于当指定类在类路径上时匹配，是自动配置中最常用的条件注解之一。

## 来源

`@ConditionalOnClass` 属于 Spring Boot 框架，包路径：`org.springframework.boot.autoconfigure.condition.ConditionalOnClass`

## 所需依赖

`@ConditionalOnClass` 是 Spring Boot 自动配置功能的核心，需要以下依赖：

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
       └─ spring-boot-autoconfigure  ← 包含 @ConditionalOnClass
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

- 当指定类在类路径上时匹配
- 用于根据类路径条件启用自动配置
- 自动配置中最常用的条件注解之一
- 可以指定一个或多个类

## 默认值

`@ConditionalOnClass` 有以下属性：

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `value` | 默认为空 | 要检查的类 |
| `name` | 默认为空 | 要检查的类名（字符串形式） |

## 使用方式

### 1. 基本使用

```java
@AutoConfiguration
@ConditionalOnClass(Tomcat.class)
public class TomcatAutoConfiguration {
    // 当类路径上存在 Tomcat 类时应用
}
```

### 2. 指定多个类

```java
@AutoConfiguration
@ConditionalOnClass({DataSource.class, DriverManager.class})
public class DataSourceAutoConfiguration {
    // 当类路径上同时存在 DataSource 和 DriverManager 类时应用
}
```

### 3. 使用类名指定

```java
@AutoConfiguration
@ConditionalOnClass(name = "org.apache.catalina.startup.Tomcat")
public class TomcatAutoConfiguration {
    // 当类路径上存在 Tomcat 类时应用
}
```

### 4. 结合其他条件注解使用

```java
@AutoConfiguration
@ConditionalOnClass(DataSource.class)
@ConditionalOnProperty(prefix = "spring.datasource", name = "url")
public class DataSourceAutoConfiguration {
    // 当类路径上存在 DataSource 类且配置了数据源 URL 时应用
}
```

## 工作原理

`@ConditionalOnClass` 通过以下机制工作：

1. **类路径检查**：检查指定的类是否在类路径上
2. **条件评估**：根据类路径检查结果决定是否应用自动配置
3. **bean 创建**：根据条件评估结果，决定是否创建和配置 bean
4. **懒加载处理**：处理类的懒加载情况

## 与其他注解的关系

### 1. 与 @ConditionalOnMissingClass 的关系

`@ConditionalOnClass` 和 `@ConditionalOnMissingClass` 是一对互补的注解：

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

### 2. 与 @ConditionalOnBean 的关系

`@ConditionalOnClass` 检查类路径，而 `@ConditionalOnBean` 检查 bean：

```java
@AutoConfiguration
@ConditionalOnClass(DataSource.class)
public class DataSourceAutoConfiguration {
    // 检查类路径
}

@AutoConfiguration
@ConditionalOnBean(DataSource.class)
public class DataSourceTransactionAutoConfiguration {
    // 检查 bean
}
```

## 版本变化

| 版本 | 变化 |
|------|------|
| Spring Boot 1.0.0 | 引入 `@ConditionalOnClass` |
| Spring Boot 2.0.0 | 支持 `name` 属性，允许使用类名字符串 |

## 最佳实践

1. **优先使用类引用** - 使用类引用比使用类名字符串更安全
2. **指定核心类** - 指定自动配置所需的类路径核心类
3. **结合其他条件** - 与 `@ConditionalOnMissingBean` 等其他条件注解一起使用
4. **避免过度使用** - 只在确实需要根据类路径条件时使用
5. **测试条件效果** - 测试条件注解是否按预期工作

## 注意事项

1. `@ConditionalOnClass` 只能用于自动配置类
2. 类检查是基于类路径的，不是基于实例的
3. 可以使用类引用或类名字符串指定要检查的类
4. 条件注解只能在自动配置类上使用，不能在普通配置类上使用
5. 用户自定义的 bean 总是优先于自动配置的 bean

## 常见问题

### Q: 为什么我的自动配置没有生效？

A: 检查是否缺少必要的类路径依赖，或者是否使用了正确的条件注解。

### Q: 可以指定多个类吗？

A: 可以，`@ConditionalOnClass` 的 `value` 属性接受数组。

### Q: 与 @ConditionalOnBean 的区别？

A: `@ConditionalOnClass` 检查类路径，而 `@ConditionalOnBean` 检查 bean。

### Q: 如何测试条件效果？

A: 使用 `--debug` 启动参数查看自动配置的条件评估情况。

### Q: 可以在非自动配置类上使用吗？

A: 不可以，条件注解只能在自动配置类上使用。