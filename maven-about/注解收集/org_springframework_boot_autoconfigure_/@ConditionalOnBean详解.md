# @ConditionalOnBean 详解

## 概述

`@ConditionalOnBean` 是 Spring Boot 自动配置条件注解，用于当 BeanFactory 中已包含满足所有指定要求的 bean 时匹配，是自动配置中最常用的条件注解之一。

## 来源

`@ConditionalOnBean` 属于 Spring Boot 框架，包路径：`org.springframework.boot.autoconfigure.condition.ConditionalOnBean`

## 所需依赖

`@ConditionalOnBean` 是 Spring Boot 自动配置功能的核心，需要以下依赖：

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
       └─ spring-boot-autoconfigure  ← 包含 @ConditionalOnBean
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

- 当 BeanFactory 中已包含满足所有指定要求的 bean 时匹配
- 用于根据已存在的 bean 启用自动配置
- 自动配置中最常用的条件注解之一
- 可以指定 bean 类型、名称、注解等

## 默认值

`@ConditionalOnBean` 有以下属性：

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `value` | 默认为空 | 要检查的 bean 类型 |
| `name` | 默认为空 | 要检查的 bean 名称 |
| `annotation` | 默认为空 | 要检查的注解类型 |
| `typeNames` | 默认为空 | 要检查的类型名称 |
| `search` | `SearchStrategy.ALL` | 搜索策略 |

## 使用方式

### 1. 基本使用

```java
@AutoConfiguration
@ConditionalOnBean(DataSource.class)
public class DataSourceAutoConfiguration {
    // 当 BeanFactory 中存在 DataSource bean 时应用
}
```

### 2. 指定 bean 名称

```java
@AutoConfiguration
@ConditionalOnBean(name = "primaryDataSource")
public class DataSourceTransactionAutoConfiguration {
    // 当 BeanFactory 中存在名为 primaryDataSource 的 bean 时应用
}
```

### 3. 指定注解类型

```java
@AutoConfiguration
@ConditionalOnBean(annotation = Repository.class)
public class RepositoryAutoConfiguration {
    // 当 BeanFactory 中存在带有 @Repository 注解的 bean 时应用
}
```

### 4. 结合其他条件注解使用

```java
@AutoConfiguration
@ConditionalOnBean(DataSource.class)
@ConditionalOnProperty(prefix = "spring.datasource", name = "url")
public class DataSourceAutoConfiguration {
    // 当 BeanFactory 中存在 DataSource bean 且配置了数据源 URL 时应用
}
```

## 工作原理

`@ConditionalOnBean` 通过以下机制工作：

1. **bean 检查**：检查 BeanFactory 中是否包含满足条件的 bean
2. **条件评估**：根据 bean 检查结果决定是否应用自动配置
3. **bean 创建**：根据条件评估结果，决定是否创建和配置 bean
4. **搜索策略**：根据搜索策略决定搜索范围

## 与其他注解的关系

### 1. 与 @ConditionalOnMissingBean 的关系

`@ConditionalOnBean` 和 `@ConditionalOnMissingBean` 是一对互补的注解：

```java
@AutoConfiguration
@ConditionalOnBean(DataSource.class)
public class DataSourceTransactionAutoConfiguration {
    // 当存在 DataSource bean 时应用
}

@AutoConfiguration
@ConditionalOnMissingBean(DataSource.class)
public DefaultDataSourceAutoConfiguration defaultDataSourceAutoConfiguration() {
    // 当不存在 DataSource bean 时应用
}
```

### 2. 与 @ConditionalOnClass 的关系

`@ConditionalOnBean` 检查 bean，而 `@ConditionalOnClass` 检查类路径：

```java
@AutoConfiguration
@ConditionalOnBean(DataSource.class)
public class DataSourceTransactionAutoConfiguration {
    // 检查 bean
}

@AutoConfiguration
@ConditionalOnClass(DataSource.class)
public class DataSourceAutoConfiguration {
    // 检查类路径
}
```

## 搜索策略

`@ConditionalOnBean` 的 `search` 属性支持以下搜索策略：

| 策略 | 说明 |
|------|------|
| `SearchStrategy.CURRENT` | 只搜索当前 bean 定义 |
| `SearchStrategy.PARENTS` | 搜索父容器 |
| `SearchStrategy.ALL` | 搜索所有容器（默认） |

## 版本变化

| 版本 | 变化 |
|------|------|
| Spring Boot 1.0.0 | 引入 `@ConditionalOnBean` |
| Spring Boot 2.0.0 | 支持 `annotation` 属性，允许根据注解检查 |

## 最佳实践

1. **优先使用 @ConditionalOnMissingBean** - 通常使用 `@ConditionalOnMissingBean` 允许用户覆盖
2. **明确指定 bean 类型** - 指定自动配置所需的 bean 类型
3. **结合其他条件** - 与 `@ConditionalOnClass` 等其他条件注解一起使用
4. **避免过度使用** - 只在确实需要根据 bean 条件时使用
5. **测试条件效果** - 测试条件注解是否按预期工作

## 注意事项

1. `@ConditionalOnBean` 只能用于自动配置类
2. bean 检查是基于 BeanFactory 的，不是基于类路径的
3. 可以指定 bean 类型、名称、注解等
4. 条件注解只能在自动配置类上使用，不能在普通配置类上使用
5. 用户自定义的 bean 总是优先于自动配置的 bean

## 常见问题

### Q: 为什么我的自动配置没有生效？

A: 检查是否缺少必要的 bean，或者是否使用了正确的条件注解。

### Q: 可以指定多个 bean 类型吗？

A: 可以，`@ConditionalOnBean` 的 `value` 属性接受数组。

### Q: 与 @ConditionalOnMissingBean 的区别？

A: `@ConditionalOnBean` 在存在 bean 时匹配，而 `@ConditionalOnMissingBean` 在不存在 bean 时匹配。

### Q: 如何测试条件效果？

A: 使用 `--debug` 启动参数查看自动配置的条件评估情况。

### Q: 可以在非自动配置类上使用吗？

A: 不可以，条件注解只能在自动配置类上使用。