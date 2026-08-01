# @ConditionalOnSingleCandidate 详解

## 概述

`@ConditionalOnSingleCandidate` 是 Spring Boot 自动配置条件注解，用于当 BeanFactory 中已包含指定类的 bean 并且可以确定单个候选者时匹配，通常用于确保只有一个候选 bean。

## 来源

`@ConditionalOnSingleCandidate` 属于 Spring Boot 框架，包路径：`org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate`

## 所需依赖

`@ConditionalOnSingleCandidate` 是 Spring Boot 自动配置功能的核心，需要以下依赖：

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
       └─ spring-boot-autoconfigure  ← 包含 @ConditionalOnSingleCandidate
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

- 当 BeanFactory 中已包含指定类的 bean 并且可以确定单个候选者时匹配
- 用于确保只有一个候选 bean
- 通常用于数据源、事务管理器等场景
- 与 `@ConditionalOnBean` 类似，但更严格

## 默认值

`@ConditionalOnSingleCandidate` 有以下属性：

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `value` | 默认为空 | 要检查的 bean 类型 |
| `typeNames` | 默认为空 | 要检查的类型名称 |
| `search` | `SearchStrategy.ALL` | 搜索策略 |

## 使用方式

### 1. 基本使用

```java
@AutoConfiguration
@ConditionalOnSingleCandidate(DataSource.class)
public class DataSourceTransactionManagerAutoConfiguration {
    // 当 BeanFactory 中存在单个 DataSource bean 时应用
}
```

### 2. 指定搜索策略

```java
@AutoConfiguration
@ConditionalOnSingleCandidate(value = DataSource.class, search = SearchStrategy.CURRENT)
public class DataSourceTransactionManagerAutoConfiguration {
    // 只在当前容器中搜索单个 DataSource bean
}
```

### 3. 结合其他条件注解使用

```java
@AutoConfiguration
@ConditionalOnSingleCandidate(DataSource.class)
@ConditionalOnClass(PlatformTransactionManager.class)
@ConditionalOnProperty(prefix = "spring.datasource", name = "url")
public class DataSourceTransactionManagerAutoConfiguration {
    // 当 BeanFactory 中存在单个 DataSource bean 且类路径上存在 PlatformTransactionManager 类且配置了数据源 URL 时应用
}
```

### 4. 用于事务管理

```java
@AutoConfiguration
@ConditionalOnSingleCandidate(DataSource.class)
public class DataSourceTransactionManagerAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
```

## 工作原理

`@ConditionalOnSingleCandidate` 通过以下机制工作：

1. **bean 检查**：检查 BeanFactory 中是否包含指定类型的 bean
2. **单候选者检查**：检查是否只有一个候选者
3. **条件评估**：根据检查结果决定是否应用自动配置
4. **bean 创建**：根据条件评估结果，决定是否创建和配置 bean

## 与其他注解的关系

### 1. 与 @ConditionalOnBean 的关系

`@ConditionalOnSingleCandidate` 比 `@ConditionalOnBean` 更严格：

```java
@AutoConfiguration
@ConditionalOnBean(DataSource.class)
public class DataSourceAutoConfiguration {
    // 当存在 DataSource bean 时应用（可能多个）
}

@AutoConfiguration
@ConditionalOnSingleCandidate(DataSource.class)
public class DataSourceTransactionManagerAutoConfiguration {
    // 当存在单个 DataSource bean 时应用
}
```

### 2. 与 @ConditionalOnMissingBean 的关系

`@ConditionalOnSingleCandidate` 检查单个候选者，而 `@ConditionalOnMissingBean` 检查不存在：

```java
@AutoConfiguration
@ConditionalOnSingleCandidate(DataSource.class)
public class DataSourceTransactionManagerAutoConfiguration {
    // 当存在单个 DataSource bean 时应用
}

@AutoConfiguration
@ConditionalOnMissingBean(DataSource.class)
public DefaultDataSourceAutoConfiguration defaultDataSourceAutoConfiguration() {
    // 当不存在 DataSource bean 时应用
}
```

## 搜索策略

`@ConditionalOnSingleCandidate` 的 `search` 属性支持以下搜索策略：

| 策略 | 说明 |
|------|------|
| `SearchStrategy.CURRENT` | 只搜索当前 bean 定义 |
| `SearchStrategy.PARENTS` | 搜索父容器 |
| `SearchStrategy.ALL` | 搜索所有容器（默认） |

## 版本变化

| 版本 | 变化 |
|------|------|
| Spring Boot 1.0.0 | 引入 `@ConditionalOnSingleCandidate` |
| Spring Boot 2.0.0 | 支持 `typeNames` 属性 |

## 最佳实践

1. **确保单候选者** - 使用 `@ConditionalOnSingleCandidate` 确保只有一个候选 bean
2. **指定搜索策略** - 根据需要指定搜索策略
3. **结合其他条件** - 与 `@ConditionalOnClass` 等其他条件注解一起使用
4. **避免过度使用** - 只在确实需要确保单候选者时使用
5. **测试条件效果** - 测试条件注解是否按预期工作

## 注意事项

1. `@ConditionalOnSingleCandidate` 只能用于自动配置类
2. 检查是基于 BeanFactory 的，不是基于类路径的
3. 需要确保只有一个候选 bean
4. 条件注解只能在自动配置类上使用，不能在普通配置类上使用
5. 用户自定义的 bean 总是优先于自动配置的 bean

## 常见问题

### Q: 为什么我的自动配置没有生效？

A: 检查是否只有一个候选 bean，或者是否使用了正确的条件注解。

### Q: 与 @ConditionalOnBean 的区别？

A: `@ConditionalOnSingleCandidate` 要求只有一个候选 bean，而 `@ConditionalOnBean` 只要求存在 bean。

### Q: 如何测试条件效果？

A: 使用 `--debug` 启动参数查看自动配置的条件评估情况。

### Q: 可以在非自动配置类上使用吗？

A: 不可以，条件注解只能在自动配置类上使用。

### Q: 适用于哪些场景？

A: 适用于数据源、事务管理器等需要确保单候选者的场景。