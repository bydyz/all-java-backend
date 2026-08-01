# @AutoConfiguration 详解

## 概述

`@AutoConfiguration` 是 Spring Boot 2.7.0 引入的注解，用于指示一个类提供可以由 Spring Boot 自动应用的配置，是 `@Configuration` 的特殊形式。

## 来源

`@AutoConfiguration` 属于 Spring Boot 框架，包路径：`org.springframework.boot.autoconfigure.AutoConfiguration`

## 所需依赖

`@AutoConfiguration` 是 Spring Boot 自动配置功能的核心，需要以下依赖：

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
       └─ spring-boot-autoconfigure  ← 包含 @AutoConfiguration
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

- 指示一个类提供自动配置
- 作为 `@Configuration` 的特殊形式，用于创建自动配置类
- 元注解包含 `@Configuration(proxyBeanMethods=false)`
- 与 `@AutoConfigureBefore` 和 `@AutoConfigureAfter` 一起使用控制配置顺序

## 默认值

`@AutoConfiguration` 有以下属性：

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `proxyBeanMethods` | `false` | 是否代理 bean 方法（默认为 false，与 @Configuration 不同） |

## 使用方式

### 1. 基本使用

```java
@AutoConfiguration
public class MyAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public MyService myService() {
        return new MyService();
    }
}
```

### 2. 控制配置顺序

```java
@AutoConfiguration
@AutoConfigureBefore(MyOtherAutoConfiguration.class)
public class MyAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public MyService myService() {
        return new MyService();
    }
}
```

### 3. 使用条件注解

```java
@AutoConfiguration
@ConditionalOnClass(Tomcat.class)
@ConditionalOnProperty(prefix = "server.tomcat", name = "uri-encoding", havingValue = "UTF-8")
public class TomcatAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }
}
```

### 4. 注册到自动配置列表

```java
@AutoConfiguration
public class MyAutoConfiguration {
    // 需要在 META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports 中注册
}
```

## 与 @Configuration 的区别

| 特性 | @AutoConfiguration | @Configuration |
|------|-------------------|----------------|
| `proxyBeanMethods` 默认值 | `false` | `true` |
| 用途 | 自动配置类 | 普通配置类 |
| 注册方式 | 需要注册到自动配置列表 | 通过组件扫描或 @Import |
| 条件注解 | 推荐使用 | 可以使用 |
| 性能 | 更好（不代理方法） | 有方法代理开销 |

## 自动配置类注册

在 Spring Boot 2.7.0+ 中，自动配置类需要注册到 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 文件：

```properties
# META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
com.example.MyAutoConfiguration
com.example.AnotherAutoConfiguration
```

## 工作原理

`@AutoConfiguration` 通过以下机制工作：

1. **类标记**：标记一个类为自动配置类
2. **条件评估**：使用条件注解决定是否应用自动配置
3. **bean 创建**：根据条件评估结果，决定是否创建和配置 bean
4. **顺序控制**：通过 `@AutoConfigureBefore` 和 `@AutoConfigureAfter` 控制配置顺序

## 版本变化

| 版本 | 变化 |
|------|------|
| Spring Boot 2.7.0 | 引入 `@AutoConfiguration`，推荐用于创建自动配置类 |
| Spring Boot 3.0.0 | 要求 Java 17+，自动配置类注册方式完全迁移到 `AutoConfiguration.imports` |

## 最佳实践

1. **替代 @Configuration** - 创建自动配置类时使用 `@AutoConfiguration` 替代 `@Configuration`
2. **使用 proxyBeanMethods=false** - 默认就是 false，保持这个设置以获得更好性能
3. **配合条件注解** - 始终使用条件注解，特别是 `@ConditionalOnMissingBean`
4. **控制配置顺序** - 使用 `@AutoConfigureBefore` 和 `@AutoConfigureAfter` 控制配置顺序
5. **注册到自动配置列表** - 确保自动配置类注册到 `AutoConfiguration.imports` 文件

## 注意事项

1. `@AutoConfiguration` 中的 `proxyBeanMethods` 默认为 `false`，与 `@Configuration` 不同
2. 自动配置类需要注册到 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 文件
3. 自动配置类只在满足条件时才应用
4. 用户自定义的 bean 总是优先于自动配置的 bean
5. 自动配置类按特定顺序应用，顺序由 `@AutoConfigureBefore` 和 `@AutoConfigureAfter` 决定

## 常见问题

### Q: 为什么我的自动配置没有生效？

A: 检查是否将自动配置类注册到了 `AutoConfiguration.imports` 文件，或者是否缺少必要的条件。

### Q: 如何控制自动配置的顺序？

A: 使用 `@AutoConfigureBefore` 和 `@AutoConfigureAfter` 注解控制配置顺序。

### Q: 与 @Configuration 的区别？

A: `@AutoConfiguration` 是 `@Configuration` 的特殊形式，`proxyBeanMethods` 默认为 `false`，专门用于自动配置类。

### Q: 如何测试自动配置？

A: 使用 `@SpringBootTest` 或 `@ImportAutoConfiguration` 在测试中导入特定的自动配置类。

### Q: 可以在非自动配置类上使用吗？

A: 可以，但通常只在自动配置类上使用。如果需要在普通配置类上使用，考虑使用 `@Configuration`。