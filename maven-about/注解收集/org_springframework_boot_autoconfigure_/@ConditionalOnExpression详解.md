# @ConditionalOnExpression 详解

## 概述

`@ConditionalOnExpression` 是 Spring Boot 自动配置条件注解，用于基于 SpEL 表达式值的条件元素配置注解，允许使用复杂的条件判断。

## 来源

`@ConditionalOnExpression` 属于 Spring Boot 框架，包路径：`org.springframework.boot.autoconfigure.condition.ConditionalOnExpression`

## 所需依赖

`@ConditionalOnExpression` 是 Spring Boot 自动配置功能的核心，需要以下依赖：

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
       └─ spring-boot-autoconfigure  ← 包含 @ConditionalOnExpression
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

- 基于 SpEL 表达式值的条件元素配置
- 允许使用复杂的条件判断
- 支持访问环境属性、bean 等
- 用于复杂的条件逻辑

## 默认值

`@ConditionalOnExpression` 有以下属性：

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `value` | 默认为空 | SpEL 表达式 |
| `expression` | 默认为空 | SpEL 表达式（别名） |

## 使用方式

### 1. 基本使用

```java
@AutoConfiguration
@ConditionalOnExpression("${spring.cache.enabled:true}")
public class CacheAutoConfiguration {
    // 当 spring.cache.enabled 为 true 时应用
}
```

### 2. 复杂表达式

```java
@AutoConfiguration
@ConditionalOnExpression("'${spring.cache.type}'.equals('redis') and '${spring.cache.enabled:true}'.equals('true')")
public class RedisCacheAutoConfiguration {
    // 当 spring.cache.type 为 redis 且 spring.cache.enabled 为 true 时应用
}
```

### 3. 检查属性是否存在

```java
@AutoConfiguration
@ConditionalOnExpression("@environment.containsProperty('spring.datasource.url')")
public class DataSourceAutoConfiguration {
    // 当环境变量中存在 spring.datasource.url 属性时应用
}
```

### 4. 结合其他条件注解使用

```java
@AutoConfiguration
@ConditionalOnExpression("${spring.cache.enabled:true}")
@ConditionalOnClass(CacheManager.class)
@ConditionalOnMissingBean(CacheManager.class)
public class CacheAutoConfiguration {
    // 当 spring.cache.enabled 为 true 且类路径上存在 CacheManager 类且不存在 CacheManager bean 时应用
}
```

## 工作原理

`@ConditionalOnExpression` 通过以下机制工作：

1. **表达式解析**：解析 SpEL 表达式
2. **表达式评估**：评估 SpEL 表达式
3. **条件评估**：根据表达式评估结果决定是否应用自动配置
4. **bean 创建**：根据条件评估结果，决定是否创建和配置 bean

## SpEL 表达式示例

### 1. 检查属性值

```java
@ConditionalOnExpression("${spring.datasource.url:} != ''")
@ConditionalOnExpression("${spring.datasource.url:} matches 'jdbc:mysql://.*'")
```

### 2. 检查多个属性

```java
@ConditionalOnExpression("'${spring.cache.type:}'.equals('redis') and '${spring.cache.enabled:true}'.equals('true')")
@ConditionalOnExpression("'${spring.profiles.active:}'.contains('dev') or '${spring.profiles.active:}'.contains('test')")
```

### 3. 检查环境变量

```java
@ConditionalOnExpression("@environment.containsProperty('spring.datasource.url')")
@ConditionalOnExpression("@environment.getProperty('spring.datasource.url', '') != ''")
```

### 4. 复杂逻辑

```java
@ConditionalOnExpression("'${app.feature.enabled:false}'.equals('true') and ('${app.feature.mode:}'.equals('active') or '${app.feature.mode:}'.equals('passive'))")
```

## 与其他注解的关系

### 1. 与 @ConditionalOnProperty 的关系

`@ConditionalOnExpression` 提供了更灵活的表达式，而 `@ConditionalOnProperty` 更简单：

```java
@AutoConfiguration
@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "redis")
public class RedisCacheAutoConfiguration {
    // 简单的属性检查
}

@AutoConfiguration
@ConditionalOnExpression("${spring.cache.type:} == 'redis'")
public class RedisCacheAutoConfiguration {
    // 复杂的表达式检查
}
```

### 2. 与 @ConditionalOnClass 的关系

`@ConditionalOnExpression` 检查表达式，而 `@ConditionalOnClass` 检查类路径：

```java
@AutoConfiguration
@ConditionalOnExpression("${spring.cache.enabled:true}")
public class CacheAutoConfiguration {
    // 检查表达式
}

@AutoConfiguration
@ConditionalOnClass(CacheManager.class)
public class CacheAutoConfiguration {
    // 检查类路径
}
```

## 版本变化

| 版本 | 变化 |
|------|------|
| Spring Boot 1.0.0 | 引入 `@ConditionalOnExpression` |

## 最佳实践

1. **复杂条件判断** - 使用 `@ConditionalOnExpression` 进行复杂的条件判断
2. **保持表达式简单** - 表达式应尽量简单，避免过于复杂
3. **结合其他条件** - 与 `@ConditionalOnClass` 等其他条件注解一起使用
4. **避免过度使用** - 只在确实需要复杂条件判断时使用
5. **测试条件效果** - 测试条件注解是否按预期工作

## 注意事项

1. `@ConditionalOnExpression` 只能用于自动配置类
2. 表达式是基于 SpEL 的，需要了解 SpEL 语法
3. 表达式应该尽量简单，避免过于复杂
4. 条件注解只能在自动配置类上使用，不能在普通配置类上使用
5. 用户自定义的 bean 总是优先于自动配置的 bean

## 常见问题

### Q: 为什么我的自动配置没有生效？

A: 检查 SpEL 表达式是否正确，或者是否使用了正确的条件注解。

### Q: 可以使用哪些 SpEL 语法？

A: 可以使用标准的 SpEL 语法，包括属性访问、方法调用、条件判断等。

### Q: 与 @ConditionalOnProperty 的区别？

A: `@ConditionalOnExpression` 提供了更灵活的表达式，而 `@ConditionalOnProperty` 更简单。

### Q: 如何测试条件效果？

A: 使用 `--debug` 启动参数查看自动配置的条件评估情况。

### Q: 可以在非自动配置类上使用吗？

A: 不可以，条件注解只能在自动配置类上使用。