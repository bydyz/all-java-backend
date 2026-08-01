# @ConditionalOnThreading 详解

## 概述

`@ConditionalOnThreading` 是 Spring Boot 自动配置条件注解，用于当指定的线程处于活动状态时匹配，通常用于线程相关的自动配置，如虚拟线程支持。

## 来源

`@ConditionalOnThreading` 属于 Spring Boot 框架，包路径：`org.springframework.boot.autoconfigure.condition.ConditionalOnThreading`

## 所需依赖

`@ConditionalOnThreading` 是 Spring Boot 自动配置功能的核心，需要以下依赖：

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
       └─ spring-boot-autoconfigure  ← 包含 @ConditionalOnThreading
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

- 当指定的线程处于活动状态时匹配
- 用于线程相关的自动配置
- 支持虚拟线程等新特性
- 通常用于 Java 19+ 的虚拟线程支持

## 默认值

`@ConditionalOnThreading` 有以下属性：

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `value` | 默认为空 | 要检查的线程类型 |

## 线程类型

| 类型 | 说明 |
|------|------|
| `Threading.VIRTUAL` | 虚拟线程（Java 19+） |
| `Threading.PLATFORM` | 平台线程 |

## 使用方式

### 1. 基本使用

```java
@AutoConfiguration
@ConditionalOnThreading(Threading.VIRTUAL)
public class VirtualThreadAutoConfiguration {
    // 当使用虚拟线程时应用
}
```

### 2. 指定平台线程

```java
@AutoConfiguration
@ConditionalOnThreading(Threading.PLATFORM)
public class PlatformThreadAutoConfiguration {
    // 当使用平台线程时应用
}
```

### 3. 结合其他条件注解使用

```java
@AutoConfiguration
@ConditionalOnThreading(Threading.VIRTUAL)
@ConditionalOnClass(name = "java.lang.VirtualThread")
@ConditionalOnProperty(prefix = "spring.threads.virtual", name = "enabled", havingValue = "true")
public class VirtualThreadAutoConfiguration {
    // 当使用虚拟线程且类路径上存在 VirtualThread 类且启用了虚拟线程时应用
}
```

### 4. 用于 Web 服务器

```java
@AutoConfiguration
@ConditionalOnThreading(Threading.VIRTUAL)
@ConditionalOnWebApplication
@ConditionalOnClass(Tomcat.class)
public class TomcatVirtualThreadAutoConfiguration {
    // 当使用虚拟线程且是 Web 应用程序且类路径上存在 Tomcat 类时应用
}
```

## 工作原理

`@ConditionalOnThreading` 通过以下机制工作：

1. **线程检测**：检测当前使用的线程类型
2. **条件评估**：根据线程检测结果决定是否应用自动配置
3. **bean 创建**：根据条件评估结果，决定是否创建和配置 bean
4. **新特性支持**：支持 Java 19+ 的虚拟线程等新特性

## 与其他注解的关系

### 1. 与 @ConditionalOnClass 的关系

`@ConditionalOnThreading` 检查线程类型，而 `@ConditionalOnClass` 检查类路径：

```java
@AutoConfiguration
@ConditionalOnThreading(Threading.VIRTUAL)
public class VirtualThreadAutoConfiguration {
    // 检查线程类型
}

@AutoConfiguration
@ConditionalOnClass(name = "java.lang.VirtualThread")
public class VirtualThreadAutoConfiguration {
    // 检查类路径
}
```

### 2. 与 @ConditionalOnProperty 的关系

`@ConditionalOnThreading` 检查线程类型，而 `@ConditionalOnProperty` 检查配置属性：

```java
@AutoConfiguration
@ConditionalOnThreading(Threading.VIRTUAL)
public class VirtualThreadAutoConfiguration {
    // 检查线程类型
}

@AutoConfiguration
@ConditionalOnProperty(prefix = "spring.threads.virtual", name = "enabled", havingValue = "true")
public class VirtualThreadAutoConfiguration {
    // 检查配置属性
}
```

## 版本变化

| 版本 | 变化 |
|------|------|
| Spring Boot 3.2.0 | 引入 `@ConditionalOnThreading` |

## 最佳实践

1. **虚拟线程支持** - 使用 `@ConditionalOnThreading` 为虚拟线程提供自动配置
2. **Java 版本兼容** - 确保 Java 版本支持相应的线程类型
3. **结合其他条件** - 与 `@ConditionalOnClass` 等其他条件注解一起使用
4. **避免过度使用** - 只在确实需要线程相关配置时使用
5. **测试条件效果** - 测试条件注解是否按预期工作

## 注意事项

1. `@ConditionalOnThreading` 只能用于自动配置类
2. 线程检测是基于运行时环境的
3. 需要 Java 19+ 支持虚拟线程
4. 条件注解只能在自动配置类上使用，不能在普通配置类上使用
5. 用户自定义的 bean 总是优先于自动配置的 bean

## 常见问题

### Q: 为什么我的自动配置没有生效？

A: 检查是否使用了正确的线程类型，或者是否使用了正确的条件注解。

### Q: 可以指定多个线程类型吗？

A: 可以，`@ConditionalOnThreading` 的 `value` 属性接受数组。

### Q: 与 @ConditionalOnClass 的区别？

A: `@ConditionalOnThreading` 检查线程类型，而 `@ConditionalOnClass` 检查类路径。

### Q: 如何测试条件效果？

A: 使用 `--debug` 启动参数查看自动配置的条件评估情况。

### Q: 可以在非自动配置类上使用吗？

A: 不可以，条件注解只能在自动配置类上使用。