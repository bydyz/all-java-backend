# @ConditionalOnJndi 详解

## 概述

`@ConditionalOnJndi` 是 Spring Boot 自动配置条件注解，用于基于 JNDI InitialContext 的可用性和查找特定位置的能力进行匹配，通常用于 JNDI 相关的自动配置。

## 来源

`@ConditionalOnJndi` 属于 Spring Boot 框架，包路径：`org.springframework.boot.autoconfigure.condition.ConditionalOnJndi`

## 所需依赖

`@ConditionalOnJndi` 是 Spring Boot 自动配置功能的核心，需要以下依赖：

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
       └─ spring-boot-autoconfigure  ← 包含 @ConditionalOnJndi
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

- 基于 JNDI InitialContext 的可用性进行匹配
- 检查是否能查找特定 JNDI 位置
- 用于 JNDI 相关的自动配置
- 通常用于企业级应用

## 默认值

`@ConditionalOnJndi` 有以下属性：

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `value` | 默认为空 | 要检查的 JNDI 位置 |
| `lookup` | 默认为空 | 要查找的 JNDI 位置 |
| `type` | 默认为空 | 要查找的类型 |

## 使用方式

### 1. 基本使用

```java
@AutoConfiguration
@ConditionalOnJndi("java:comp/env/jdbc/myDS")
public class JndiDataSourceAutoConfiguration {
    // 当能查找 JNDI 位置 java:comp/env/jdbc/myDS 时应用
}
```

### 2. 指定查找位置

```java
@AutoConfiguration
@ConditionalOnJndi(lookup = "java:comp/env/jdbc/myDS")
public class JndiDataSourceAutoConfiguration {
    // 当能查找 JNDI 位置 java:comp/env/jdbc/myDS 时应用
}
```

### 3. 指定类型

```java
@AutoConfiguration
@ConditionalOnJndi(lookup = "java:comp/env/jdbc/myDS", type = "javax.sql.DataSource")
public class JndiDataSourceAutoConfiguration {
    // 当能查找 JNDI 位置且类型为 DataSource 时应用
}
```

### 4. 结合其他条件注解使用

```java
@AutoConfiguration
@ConditionalOnJndi("java:comp/env/jdbc/myDS")
@ConditionalOnClass(DataSource.class)
@ConditionalOnProperty(prefix = "spring.datasource", name = "jndi-name")
public class JndiDataSourceAutoConfiguration {
    // 当能查找 JNDI 位置且类路径上存在 DataSource 类且配置了 JNDI 名称时应用
}
```

## 工作原理

`@ConditionalOnJndi` 通过以下机制工作：

1. **InitialContext 检查**：检查 JNDI InitialContext 是否可用
2. **位置查找**：尝试查找指定的 JNDI 位置
3. **条件评估**：根据查找结果决定是否应用自动配置
4. **bean 创建**：根据条件评估结果，决定是否创建和配置 bean

## 与其他注解的关系

### 1. 与 @ConditionalOnClass 的关系

`@ConditionalOnJndi` 检查 JNDI 可用性，而 `@ConditionalOnClass` 检查类路径：

```java
@AutoConfiguration
@ConditionalOnJndi("java:comp/env/jdbc/myDS")
public class JndiDataSourceAutoConfiguration {
    // 检查 JNDI 可用性
}

@AutoConfiguration
@ConditionalOnClass(DataSource.class)
public class DataSourceAutoConfiguration {
    // 检查类路径
}
```

### 2. 与 @ConditionalOnProperty 的关系

`@ConditionalOnJndi` 检查 JNDI 可用性，而 `@ConditionalOnProperty` 检查配置属性：

```java
@AutoConfiguration
@ConditionalOnJndi("java:comp/env/jdbc/myDS")
public class JndiDataSourceAutoConfiguration {
    // 检查 JNDI 可用性
}

@AutoConfiguration
@ConditionalOnProperty(prefix = "spring.datasource", name = "url")
public class DataSourceAutoConfiguration {
    // 检查配置属性
}
```

## 版本变化

| 版本 | 变化 |
|------|------|
| Spring Boot 1.0.0 | 引入 `@ConditionalOnJndi` |

## 最佳实践

1. **JNDI 数据源** - 使用 `@ConditionalOnJndi` 为 JNDI 数据源提供自动配置
2. **指定正确的 JNDI 位置** - 确保 JNDI 位置正确
3. **结合其他条件** - 与 `@ConditionalOnClass` 等其他条件注解一起使用
4. **避免过度使用** - 只在确实需要 JNDI 相关配置时使用
5. **测试条件效果** - 测试条件注解是否按预期工作

## 注意事项

1. `@ConditionalOnJndi` 只能用于自动配置类
2. JNDI 检查是基于 InitialContext 的
3. 可以指定多个 JNDI 位置
4. 条件注解只能在自动配置类上使用，不能在普通配置类上使用
5. 用户自定义的 bean 总是优先于自动配置的 bean

## 常见问题

### Q: 为什么我的自动配置没有生效？

A: 检查 JNDI InitialContext 是否可用，或者是否使用了正确的条件注解。

### Q: 可以指定多个 JNDI 位置吗？

A: 可以，`@ConditionalOnJndi` 的 `value` 属性接受数组。

### Q: 与 @ConditionalOnClass 的区别？

A: `@ConditionalOnJndi` 检查 JNDI 可用性，而 `@ConditionalOnClass` 检查类路径。

### Q: 如何测试条件效果？

A: 使用 `--debug` 启动参数查看自动配置的条件评估情况。

### Q: 可以在非自动配置类上使用吗？

A: 不可以，条件注解只能在自动配置类上使用。