# @ConditionalOnCloudPlatform 详解

## 概述

`@ConditionalOnCloudPlatform` 是 Spring Boot 自动配置条件注解，用于当指定的云平台处于活动状态时匹配，通常用于云平台特定的自动配置。

## 来源

`@ConditionalOnCloudPlatform` 属于 Spring Boot 框架，包路径：`org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform`

## 所需依赖

`@ConditionalOnCloudPlatform` 是 Spring Boot 自动配置功能的核心，需要以下依赖：

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
       └─ spring-boot-autoconfigure  ← 包含 @ConditionalOnCloudPlatform
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

- 当指定的云平台处于活动状态时匹配
- 用于云平台特定的自动配置
- 支持多种云平台
- 用于云环境中的特定配置

## 默认值

`@ConditionalOnCloudPlatform` 有以下属性：

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `value` | 默认为空 | 要检查的云平台 |

## 支持的云平台

| 云平台 | 说明 |
|--------|------|
| `CloudPlatform.CLOUD_FOUNDRY` | Cloud Foundry |
| `CloudPlatform.HEROKU` | Heroku |
| `CloudPlatform.AMAZON_BEANSTALK` | AWS Elastic Beanstalk |
| `CloudPlatform.AZURE` | Microsoft Azure |

## 使用方式

### 1. 基本使用

```java
@AutoConfiguration
@ConditionalOnCloudPlatform(CloudPlatform.CLOUD_FOUNDRY)
public class CloudFoundryAutoConfiguration {
    // 当运行在 Cloud Foundry 上时应用
}
```

### 2. 检查 Heroku

```java
@AutoConfiguration
@ConditionalOnCloudPlatform(CloudPlatform.HEROKU)
public class HerokuAutoConfiguration {
    // 当运行在 Heroku 上时应用
}
```

### 3. 检查 AWS

```java
@AutoConfiguration
@ConditionalOnCloudPlatform(CloudPlatform.AMAZON_BEANSTALK)
public class AwsAutoConfiguration {
    // 当运行在 AWS Elastic Beanstalk 上时应用
}
```

### 4. 结合其他条件注解使用

```java
@AutoConfiguration
@ConditionalOnCloudPlatform(CloudPlatform.CLOUD_FOUNDRY)
@ConditionalOnClass(DataSource.class)
@ConditionalOnProperty(prefix = "spring.datasource", name = "url")
public class CloudFoundryDataSourceAutoConfiguration {
    // 当运行在 Cloud Foundry 上且类路径上存在 DataSource 类且配置了数据源 URL 时应用
}
```

## 工作原理

`@ConditionalOnCloudPlatform` 通过以下机制工作：

1. **云平台检测**：检测当前运行的云平台
2. **条件评估**：根据云平台检测结果决定是否应用自动配置
3. **bean 创建**：根据条件评估结果，决定是否创建和配置 bean
4. **环境特定**：提供云环境中的特定配置

## 与其他注解的关系

### 1. 与 @ConditionalOnProperty 的关系

`@ConditionalOnCloudPlatform` 检查云平台，而 `@ConditionalOnProperty` 检查配置属性：

```java
@AutoConfiguration
@ConditionalOnCloudPlatform(CloudPlatform.CLOUD_FOUNDRY)
public class CloudFoundryAutoConfiguration {
    // 检查云平台
}

@AutoConfiguration
@ConditionalOnProperty(prefix = "spring.cloud", name = "platform", havingValue = "cloud_foundry")
public class CloudFoundryAutoConfiguration {
    // 检查配置属性
}
```

### 2. 与 @ConditionalOnClass 的关系

`@ConditionalOnCloudPlatform` 检查云平台，而 `@ConditionalOnClass` 检查类路径：

```java
@AutoConfiguration
@ConditionalOnCloudPlatform(CloudPlatform.CLOUD_FOUNDRY)
public class CloudFoundryAutoConfiguration {
    // 检查云平台
}

@AutoConfiguration
@ConditionalOnClass(DataSource.class)
public class DataSourceAutoConfiguration {
    // 检查类路径
}
```

## 版本变化

| 版本 | 变化 |
|------|------|
| Spring Boot 1.0.0 | 引入 `@ConditionalOnCloudPlatform` |
| Spring Boot 2.0.0 | 支持更多云平台 |

## 最佳实践

1. **云平台特定配置** - 使用 `@ConditionalOnCloudPlatform` 提供云平台特定的配置
2. **指定正确的云平台** - 确保指定正确的云平台
3. **结合其他条件** - 与 `@ConditionalOnClass` 等其他条件注解一起使用
4. **避免过度使用** - 只在确实需要云平台特定配置时使用
5. **测试条件效果** - 测试条件注解是否按预期工作

## 注意事项

1. `@ConditionalOnCloudPlatform` 只能用于自动配置类
2. 云平台检测是基于环境的
3. 可以指定多个云平台
4. 条件注解只能在自动配置类上使用，不能在普通配置类上使用
5. 用户自定义的 bean 总是优先于自动配置的 bean

## 常见问题

### Q: 为什么我的自动配置没有生效？

A: 检查是否运行在正确的云平台上，或者是否使用了正确的条件注解。

### Q: 可以指定多个云平台吗？

A: 可以，`@ConditionalOnCloudPlatform` 的 `value` 属性接受数组。

### Q: 与 @ConditionalOnProperty 的区别？

A: `@ConditionalOnCloudPlatform` 检查云平台，而 `@ConditionalOnProperty` 检查配置属性。

### Q: 如何测试条件效果？

A: 使用 `--debug` 启动参数查看自动配置的条件评估情况。

### Q: 可以在非自动配置类上使用吗？

A: 不可以，条件注解只能在自动配置类上使用。