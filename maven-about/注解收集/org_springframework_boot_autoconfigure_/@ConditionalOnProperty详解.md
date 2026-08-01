# @ConditionalOnProperty 详解

## 概述

`@ConditionalOnProperty` 是 Spring Boot 自动配置条件注解，用于检查指定属性是否具有特定值，是自动配置中最常用的条件注解之一，用于根据配置属性启用自动配置。

## 来源

`@ConditionalOnProperty` 属于 Spring Boot 框架，包路径：`org.springframework.boot.autoconfigure.condition.ConditionalOnProperty`

## 所需依赖

`@ConditionalOnProperty` 是 Spring Boot 自动配置功能的核心，需要以下依赖：

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
       └─ spring-boot-autoconfigure  ← 包含 @ConditionalOnProperty
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

- 检查指定属性是否具有特定值
- 用于根据配置属性启用自动配置
- 自动配置中最常用的条件注解之一
- 支持前缀、属性名、期望值等

## 默认值

`@ConditionalOnProperty` 有以下属性：

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `prefix` | 默认为空 | 属性前缀 |
| `name` | 默认为空 | 属性名 |
| `havingValue` | 默认为空 | 期望的值 |
| `matchIfMissing` | `false` | 属性缺失时是否匹配 |
| `havingValue` | 默认为空 | 期望的值 |
| `name` | 默认为空 | 属性名 |

## 使用方式

### 1. 基本使用

```java
@AutoConfiguration
@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "redis")
public class RedisCacheAutoConfiguration {
    // 当配置了 spring.cache.type=redis 时应用
}
```

### 2. 检查属性是否存在

```java
@AutoConfiguration
@ConditionalOnProperty(prefix = "spring.cache", name = "type")
public class CacheAutoConfiguration {
    // 当配置了 spring.cache.type 属性时应用
}
```

### 3. 属性缺失时匹配

```java
@AutoConfiguration
@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "simple", matchIfMissing = true)
public class SimpleCacheAutoConfiguration {
    // 当配置了 spring.cache.type=simple 或未配置 spring.cache.type 时应用
}
```

### 4. 结合其他条件注解使用

```java
@AutoConfiguration
@ConditionalOnProperty(prefix = "spring.datasource", name = "url")
@ConditionalOnClass(DataSource.class)
@ConditionalOnMissingBean(DataSource.class)
public class DataSourceAutoConfiguration {
    // 当配置了 spring.datasource.url 且类路径上存在 DataSource 类且不存在 DataSource bean 时应用
}
```

## 工作原理

`@ConditionalOnProperty` 通过以下机制工作：

1. **属性解析**：解析指定的属性
2. **条件评估**：根据属性值决定是否应用自动配置
3. **bean 创建**：根据条件评估结果，决定是否创建和配置 bean
4. **默认值处理**：处理属性缺失的情况

## 与其他注解的关系

### 1. 与 @ConditionalOnClass 的关系

`@ConditionalOnProperty` 检查配置属性，而 `@ConditionalOnClass` 检查类路径：

```java
@AutoConfiguration
@ConditionalOnProperty(prefix = "spring.datasource", name = "url")
public class DataSourceAutoConfiguration {
    // 检查配置属性
}

@AutoConfiguration
@ConditionalOnClass(DataSource.class)
public class DataSourceAutoConfiguration {
    // 检查类路径
}
```

### 2. 与 @ConditionalOnBean 的关系

`@ConditionalOnProperty` 检查配置属性，而 `@ConditionalOnBean` 检查 bean：

```java
@AutoConfiguration
@ConditionalOnProperty(prefix = "spring.datasource", name = "url")
public class DataSourceAutoConfiguration {
    // 检查配置属性
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
| Spring Boot 1.1.0 | 引入 `@ConditionalOnProperty` |
| Spring Boot 2.0.0 | 支持 `matchIfMissing` 属性 |

## 最佳实践

1. **允许用户配置** - 使用 `@ConditionalOnProperty` 允许用户通过配置启用/禁用自动配置
2. **提供默认值** - 使用 `matchIfMissing` 提供默认值
3. **结合其他条件** - 与 `@ConditionalOnClass` 等其他条件注解一起使用
4. **避免过度使用** - 只在确实需要根据配置属性条件时使用
5. **测试条件效果** - 测试条件注解是否按预期工作

## 注意事项

1. `@ConditionalOnProperty` 只能用于自动配置类
2. 属性检查是基于配置属性的，不是基于类路径的
3. 可以指定属性前缀、名称、期望值等
4. 条件注解只能在自动配置类上使用，不能在普通配置类上使用
5. 用户自定义的 bean 总是优先于自动配置的 bean

## 常见问题

### Q: 为什么我的自动配置没有生效？

A: 检查是否配置了正确的属性值，或者是否使用了正确的条件注解。

### Q: 如何处理属性缺失的情况？

A: 使用 `matchIfMissing = true` 属性，在属性缺失时匹配。

### Q: 与 @ConditionalOnBean 的区别？

A: `@ConditionalOnProperty` 检查配置属性，而 `@ConditionalOnBean` 检查 bean。

### Q: 如何测试条件效果？

A: 使用 `--debug` 启动参数查看自动配置的条件评估情况。

### Q: 可以在非自动配置类上使用吗？

A: 不可以，条件注解只能在自动配置类上使用。