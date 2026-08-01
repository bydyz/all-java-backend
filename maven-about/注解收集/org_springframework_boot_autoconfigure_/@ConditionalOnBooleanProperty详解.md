# @ConditionalOnBooleanProperty 详解

## 概述

`@ConditionalOnBooleanProperty` 是 Spring Boot 自动配置条件注解，用于检查指定属性是否具有特定的布尔值，是 `@ConditionalOnProperty` 的简化版本，专门用于布尔属性。

## 来源

`@ConditionalOnBooleanProperty` 属于 Spring Boot 框架，包路径：`org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty`

## 所需依赖

`@ConditionalOnBooleanProperty` 是 Spring Boot 自动配置功能的核心，需要以下依赖：

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
       └─ spring-boot-autoconfigure  ← 包含 @ConditionalOnBooleanProperty
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

- 检查指定属性是否具有特定的布尔值
- 是 `@ConditionalOnProperty` 的简化版本
- 专门用于布尔属性检查
- 支持前缀、属性名、期望值等

## 默认值

`@ConditionalOnBooleanProperty` 有以下属性：

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `prefix` | 默认为空 | 属性前缀 |
| `name` | 默认为空 | 属性名 |
| `havingValue` | `true` | 期望的布尔值 |
| `matchIfMissing` | `false` | 属性缺失时是否匹配 |

## 使用方式

### 1. 基本使用

```java
@AutoConfiguration
@ConditionalOnBooleanProperty(name = "spring.cache.enabled")
public class CacheAutoConfiguration {
    // 当 spring.cache.enabled 为 true 时应用
}
```

### 2. 指定期望值

```java
@AutoConfiguration
@ConditionalOnBooleanProperty(name = "spring.cache.enabled", havingValue = false)
public class DisabledCacheAutoConfiguration {
    // 当 spring.cache.enabled 为 false 时应用
}
```

### 3. 属性缺失时匹配

```java
@AutoConfiguration
@ConditionalOnBooleanProperty(name = "spring.cache.enabled", matchIfMissing = true)
public class CacheAutoConfiguration {
    // 当 spring.cache.enabled 为 true 或未配置时应用
}
```

### 4. 结合其他条件注解使用

```java
@AutoConfiguration
@ConditionalOnBooleanProperty(name = "spring.cache.enabled")
@ConditionalOnClass(CacheManager.class)
@ConditionalOnMissingBean(CacheManager.class)
public class CacheAutoConfiguration {
    // 当 spring.cache.enabled 为 true 且类路径上存在 CacheManager 类且不存在 CacheManager bean 时应用
}
```

## 工作原理

`@ConditionalOnBooleanProperty` 通过以下机制工作：

1. **属性解析**：解析指定的布尔属性
2. **条件评估**：根据属性值决定是否应用自动配置
3. **bean 创建**：根据条件评估结果，决定是否创建和配置 bean
4. **布尔检查**：专门处理布尔属性

## 与其他注解的关系

### 1. 与 @ConditionalOnProperty 的关系

`@ConditionalOnBooleanProperty` 是 `@ConditionalOnProperty` 的简化版本：

```java
@AutoConfiguration
@ConditionalOnBooleanProperty(name = "spring.cache.enabled")
public class CacheAutoConfiguration {
    // 简化的布尔属性检查
}

@AutoConfiguration
@ConditionalOnProperty(prefix = "spring.cache", name = "enabled", havingValue = "true")
public class CacheAutoConfiguration {
    // 完整的属性检查
}
```

### 2. 与 @ConditionalOnClass 的关系

`@ConditionalOnBooleanProperty` 检查配置属性，而 `@ConditionalOnClass` 检查类路径：

```java
@AutoConfiguration
@ConditionalOnBooleanProperty(name = "spring.cache.enabled")
public class CacheAutoConfiguration {
    // 检查配置属性
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
| Spring Boot 3.1.0 | 引入 `@ConditionalOnBooleanProperty` |

## 最佳实践

1. **简化布尔检查** - 使用 `@ConditionalOnBooleanProperty` 简化布尔属性检查
2. **提供默认值** - 使用 `matchIfMissing` 提供默认值
3. **结合其他条件** - 与 `@ConditionalOnClass` 等其他条件注解一起使用
4. **避免过度使用** - 只在确实需要检查布尔属性时使用
5. **测试条件效果** - 测试条件注解是否按预期工作

## 注意事项

1. `@ConditionalOnBooleanProperty` 只能用于自动配置类
2. 专门用于布尔属性检查
3. 是 `@ConditionalOnProperty` 的简化版本
4. 条件注解只能在自动配置类上使用，不能在普通配置类上使用
5. 用户自定义的 bean 总是优先于自动配置的 bean

## 常见问题

### Q: 为什么我的自动配置没有生效？

A: 检查是否配置了正确的布尔属性值，或者是否使用了正确的条件注解。

### Q: 与 @ConditionalOnProperty 的区别？

A: `@ConditionalOnBooleanProperty` 是 `@ConditionalOnProperty` 的简化版本，专门用于布尔属性。

### Q: 如何测试条件效果？

A: 使用 `--debug` 启动参数查看自动配置的条件评估情况。

### Q: 可以在非自动配置类上使用吗？

A: 不可以，条件注解只能在自动配置类上使用。

### Q: 适用于哪些场景？

A: 适用于检查功能开关、特性启用等布尔属性场景。