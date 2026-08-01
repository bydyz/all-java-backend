# @ConditionalOnBooleanProperties 详解

## 概述

`@ConditionalOnBooleanProperties` 是 Spring Boot 自动配置条件注解，是容器注解，用于聚合多个 `@ConditionalOnBooleanProperty` 注解，允许同时检查多个布尔属性。

## 来源

`@ConditionalOnBooleanProperties` 属于 Spring Boot 框架，包路径：`org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperties`

## 所需依赖

`@ConditionalOnBooleanProperties` 是 Spring Boot 自动配置功能的核心，需要以下依赖：

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
       └─ spring-boot-autoconfigure  ← 包含 @ConditionalOnBooleanProperties
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

- 聚合多个 `@ConditionalOnBooleanProperty` 注解
- 允许同时检查多个布尔属性
- 用于需要多个布尔属性条件的场景
- 提供更简洁的多条件检查

## 默认值

`@ConditionalOnBooleanProperties` 有以下属性：

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `value` | 默认为空 | 要聚合的 `@ConditionalOnBooleanProperty` 注解 |

## 使用方式

### 1. 基本使用

```java
@AutoConfiguration
@ConditionalOnBooleanProperties({
    @ConditionalOnBooleanProperty(name = "spring.cache.enabled"),
    @ConditionalOnBooleanProperty(name = "spring.cache.type", havingValue = "true")
})
public class CacheAutoConfiguration {
    // 当 spring.cache.enabled 为 true 且 spring.cache.type 为 true 时应用
}
```

### 2. 指定前缀

```java
@AutoConfiguration
@ConditionalOnBooleanProperties({
    @ConditionalOnBooleanProperty(prefix = "spring.cache", name = "enabled"),
    @ConditionalOnBooleanProperty(prefix = "spring.cache", name = "local")
})
public class LocalCacheAutoConfiguration {
    // 当 spring.cache.enabled 为 true 且 spring.cache.local 为 true 时应用
}
```

### 3. 使用 matchIfMissing

```java
@AutoConfiguration
@ConditionalOnBooleanProperties({
    @ConditionalOnBooleanProperty(name = "spring.cache.enabled", matchIfMissing = true),
    @ConditionalOnBooleanProperty(name = "spring.cache.local", matchIfMissing = true)
})
public class CacheAutoConfiguration {
    // 当 spring.cache.enabled 为 true 或未配置，且 spring.cache.local 为 true 或未配置时应用
}
```

### 4. 结合其他条件注解使用

```java
@AutoConfiguration
@ConditionalOnBooleanProperties({
    @ConditionalOnBooleanProperty(name = "spring.cache.enabled"),
    @ConditionalOnBooleanProperty(name = "spring.cache.local")
})
@ConditionalOnClass(CacheManager.class)
@ConditionalOnMissingBean(CacheManager.class)
public class CacheAutoConfiguration {
    // 当两个布尔属性都为 true 且类路径上存在 CacheManager 类且不存在 CacheManager bean 时应用
}
```

## 工作原理

`@ConditionalOnBooleanProperties` 通过以下机制工作：

1. **条件聚合**：聚合多个 `@ConditionalOnBooleanProperty` 注解
2. **条件评估**：分别评估每个条件
3. **逻辑与**：所有条件都满足时才应用自动配置
4. **bean 创建**：根据条件评估结果，决定是否创建和配置 bean

## 与其他注解的关系

### 1. 与 @ConditionalOnBooleanProperty 的关系

`@ConditionalOnBooleanProperties` 是 `@ConditionalOnBooleanProperty` 的容器注解：

```java
@AutoConfiguration
@ConditionalOnBooleanProperties({
    @ConditionalOnBooleanProperty(name = "spring.cache.enabled"),
    @ConditionalOnBooleanProperty(name = "spring.cache.local")
})
public class CacheAutoConfiguration {
    // 聚合多个条件
}

@AutoConfiguration
@ConditionalOnBooleanProperty(name = "spring.cache.enabled")
public class CacheAutoConfiguration {
    // 单个条件
}
```

### 2. 与 @ConditionalOnProperties 的关系

`@ConditionalOnBooleanProperties` 专门用于布尔属性，而 `@ConditionalOnProperties` 用于所有属性：

```java
@AutoConfiguration
@ConditionalOnBooleanProperties({
    @ConditionalOnBooleanProperty(name = "spring.cache.enabled"),
    @ConditionalOnBooleanProperty(name = "spring.cache.local")
})
public class CacheAutoConfiguration {
    // 专门用于布尔属性
}

@AutoConfiguration
@ConditionalOnProperties({
    @ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "redis"),
    @ConditionalOnProperty(prefix = "spring.cache", name = "enabled", havingValue = "true")
})
public class RedisCacheAutoConfiguration {
    // 用于所有属性
}
```

## 版本变化

| 版本 | 变化 |
|------|------|
| Spring Boot 3.1.0 | 引入 `@ConditionalOnBooleanProperties` |

## 最佳实践

1. **多条件检查** - 使用 `@ConditionalOnBooleanProperties` 进行多个布尔属性检查
2. **保持条件简单** - 每个条件应尽量简单
3. **结合其他条件** - 与 `@ConditionalOnClass` 等其他条件注解一起使用
4. **避免过度使用** - 只在确实需要多个布尔属性条件时使用
5. **测试条件效果** - 测试条件注解是否按预期工作

## 注意事项

1. `@ConditionalOnBooleanProperties` 只能用于自动配置类
2. 是容器注解，用于聚合多个 `@ConditionalOnBooleanProperty`
3. 所有条件都满足时才应用自动配置
4. 条件注解只能在自动配置类上使用，不能在普通配置类上使用
5. 用户自定义的 bean 总是优先于自动配置的 bean

## 常见问题

### Q: 为什么我的自动配置没有生效？

A: 检查是否所有布尔属性条件都满足，或者是否使用了正确的条件注解。

### Q: 可以指定多个条件吗？

A: 可以，`@ConditionalOnBooleanProperties` 的 `value` 属性接受多个 `@ConditionalOnBooleanProperty`。

### Q: 与 @ConditionalOnProperties 的区别？

A: `@ConditionalOnBooleanProperties` 专门用于布尔属性，而 `@ConditionalOnProperties` 用于所有属性。

### Q: 如何测试条件效果？

A: 使用 `--debug` 启动参数查看自动配置的条件评估情况。

### Q: 可以在非自动配置类上使用吗？

A: 不可以，条件注解只能在自动配置类上使用。