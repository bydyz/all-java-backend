# @ConditionalOnResource 详解

## 概述

`@ConditionalOnResource` 是 Spring Boot 自动配置条件注解，用于当指定资源在类路径上时匹配，通常用于根据资源文件启用自动配置。

## 来源

`@ConditionalOnResource` 属于 Spring Boot 框架，包路径：`org.springframework.boot.autoconfigure.condition.ConditionalOnResource`

## 所需依赖

`@ConditionalOnResource` 是 Spring Boot 自动配置功能的核心，需要以下依赖：

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
       └─ spring-boot-autoconfigure  ← 包含 @ConditionalOnResource
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

- 当指定资源在类路径上时匹配
- 用于根据资源文件启用自动配置
- 检查资源文件是否存在
- 可以指定一个或多个资源

## 默认值

`@ConditionalOnResource` 有以下属性：

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `resources` | 默认为空 | 要检查的资源路径 |
| `resources` | 默认为空 | 要检查的资源路径（别名） |

## 使用方式

### 1. 基本使用

```java
@AutoConfiguration
@ConditionalOnResource(resources = "classpath:schema.sql")
public class DataSourceInitializationAutoConfiguration {
    // 当类路径上存在 schema.sql 文件时应用
}
```

### 2. 指定多个资源

```java
@AutoConfiguration
@ConditionalOnResource(resources = {"classpath:schema.sql", "classpath:data.sql"})
public class DataSourceInitializationAutoConfiguration {
    // 当类路径上同时存在 schema.sql 和 data.sql 文件时应用
}
```

### 3. 指定其他路径

```java
@AutoConfiguration
@ConditionalOnResource(resources = "classpath:config/application-custom.yml")
public class CustomConfigurationAutoConfiguration {
    // 当类路径上存在 config/application-custom.yml 文件时应用
}
```

### 4. 结合其他条件注解使用

```java
@AutoConfiguration
@ConditionalOnResource(resources = "classpath:schema.sql")
@ConditionalOnClass(DataSource.class)
@ConditionalOnProperty(prefix = "spring.datasource", name = "url")
public class DataSourceInitializationAutoConfiguration {
    // 当类路径上存在 schema.sql 文件且存在 DataSource 类且配置了数据源 URL 时应用
}
```

## 工作原理

`@ConditionalOnResource` 通过以下机制工作：

1. **资源检查**：检查指定的资源文件是否在类路径上
2. **条件评估**：根据资源检查结果决定是否应用自动配置
3. **bean 创建**：根据条件评估结果，决定是否创建和配置 bean
4. **文件存在性**：检查文件是否存在

## 与其他注解的关系

### 1. 与 @ConditionalOnClass 的关系

`@ConditionalOnResource` 检查资源文件，而 `@ConditionalOnClass` 检查类：

```java
@AutoConfiguration
@ConditionalOnResource(resources = "classpath:schema.sql")
public class DataSourceInitializationAutoConfiguration {
    // 检查资源文件
}

@AutoConfiguration
@ConditionalOnClass(DataSource.class)
public class DataSourceAutoConfiguration {
    // 检查类
}
```

### 2. 与 @ConditionalOnProperty 的关系

`@ConditionalOnResource` 检查资源文件，而 `@ConditionalOnProperty` 检查配置属性：

```java
@AutoConfiguration
@ConditionalOnResource(resources = "classpath:schema.sql")
public class DataSourceInitializationAutoConfiguration {
    // 检查资源文件
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
| Spring Boot 1.1.0 | 引入 `@ConditionalOnResource` |

## 最佳实践

1. **根据资源文件启用** - 使用 `@ConditionalOnResource` 根据资源文件启用自动配置
2. **指定正确的资源路径** - 确保资源路径正确
3. **结合其他条件** - 与 `@ConditionalOnClass` 等其他条件注解一起使用
4. **避免过度使用** - 只在确实需要根据资源文件条件时使用
5. **测试条件效果** - 测试条件注解是否按预期工作

## 注意事项

1. `@ConditionalOnResource` 只能用于自动配置类
2. 资源检查是基于类路径的，不是基于文件系统的
3. 可以指定一个或多个资源路径
4. 条件注解只能在自动配置类上使用，不能在普通配置类上使用
5. 用户自定义的 bean 总是优先于自动配置的 bean

## 常见问题

### Q: 为什么我的自动配置没有生效？

A: 检查是否缺少必要的资源文件，或者是否使用了正确的条件注解。

### Q: 可以指定多个资源吗？

A: 可以，`@ConditionalOnResource` 的 `resources` 属性接受数组。

### Q: 与 @ConditionalOnClass 的区别？

A: `@ConditionalOnResource` 检查资源文件，而 `@ConditionalOnClass` 检查类。

### Q: 如何测试条件效果？

A: 使用 `--debug` 启动参数查看自动配置的条件评估情况。

### Q: 可以在非自动配置类上使用吗？

A: 不可以，条件注解只能在自动配置类上使用。