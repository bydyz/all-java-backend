# @ConditionalOnCheckpointRestore 详解

## 概述

`@ConditionalOnCheckpointRestore` 是 Spring Boot 自动配置条件注解，用于当使用协调检查点恢复时匹配，通常用于 GraalVM 原生镜像支持。

## 来源

`@ConditionalOnCheckpointRestore` 属于 Spring Boot 框架，包路径：`org.springframework.boot.autoconfigure.condition.ConditionalOnCheckpointRestore`

## 所需依赖

`@ConditionalOnCheckpointRestore` 是 Spring Boot 自动配置功能的核心，需要以下依赖：

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
       └─ spring-boot-autoconfigure  ← 包含 @ConditionalOnCheckpointRestore
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

- 当使用协调检查点恢复时匹配
- 用于 GraalVM 原生镜像支持
- 检查是否在原生镜像环境中运行
- 通常用于原生编译的 Spring Boot 应用

## 默认值

`@ConditionalOnCheckpointRestore` 没有额外属性，它是一个简单的条件注解。

## 使用方式

### 1. 基本使用

```java
@AutoConfiguration
@ConditionalOnCheckpointRestore
public class CheckpointRestoreAutoConfiguration {
    // 当使用协调检查点恢复时应用
}
```

### 2. 结合其他条件注解使用

```java
@AutoConfiguration
@ConditionalOnCheckpointRestore
@ConditionalOnClass(name = "org.graalvm.nativeimage.ImageInfo")
public class GraalVMAutoConfiguration {
    // 当使用协调检查点恢复且类路径上存在 GraalVM 类时应用
}
```

### 3. 用于原生镜像配置

```java
@AutoConfiguration
@ConditionalOnCheckpointRestore
@ConditionalOnProperty(prefix = "spring.native", name = "image-name")
public class NativeImageAutoConfiguration {
    // 当使用协调检查点恢复且配置了原生镜像名称时应用
}
```

### 4. 用于 GraalVM 原生编译

```java
@AutoConfiguration
@ConditionalOnCheckpointRestore
@ConditionalOnClass(name = "org.springframework.aot.hint.RuntimeHints")
public class GraalVMHintsAutoConfiguration {
    // 当使用协调检查点恢复且类路径上存在 Spring AOT 提示类时应用
}
```

## 工作原理

`@ConditionalOnCheckpointRestore` 通过以下机制工作：

1. **环境检测**：检测是否在协调检查点恢复环境中
2. **条件评估**：根据环境检测结果决定是否应用自动配置
3. **bean 创建**：根据条件评估结果，决定是否创建和配置 bean
4. **原生支持**：为 GraalVM 原生镜像提供特定配置

## 与其他注解的关系

### 1. 与 @ConditionalOnClass 的关系

`@ConditionalOnCheckpointRestore` 检查环境，而 `@ConditionalOnClass` 检查类路径：

```java
@AutoConfiguration
@ConditionalOnCheckpointRestore
public class CheckpointRestoreAutoConfiguration {
    // 检查环境
}

@AutoConfiguration
@ConditionalOnClass(name = "org.graalvm.nativeimage.ImageInfo")
public class GraalVMAutoConfiguration {
    // 检查类路径
}
```

### 2. 与 @ConditionalOnProperty 的关系

`@ConditionalOnCheckpointRestore` 检查环境，而 `@ConditionalOnProperty` 检查配置属性：

```java
@AutoConfiguration
@ConditionalOnCheckpointRestore
public class CheckpointRestoreAutoConfiguration {
    // 检查环境
}

@AutoConfiguration
@ConditionalOnProperty(prefix = "spring.native", name = "image-name")
public class NativeImageAutoConfiguration {
    // 检查配置属性
}
```

## 版本变化

| 版本 | 变化 |
|------|------|
| Spring Boot 3.2.0 | 引入 `@ConditionalOnCheckpointRestore` |

## 最佳实践

1. **原生镜像支持** - 使用 `@ConditionalOnCheckpointRestore` 为 GraalVM 原生镜像提供自动配置
2. **结合其他条件** - 与 `@ConditionalOnClass` 等其他条件注解一起使用
3. **避免过度使用** - 只在确实需要原生镜像特定配置时使用
4. **测试条件效果** - 测试条件注解是否按预期工作
5. **确保 GraalVM 兼容** - 确保应用兼容 GraalVM 原生编译

## 注意事项

1. `@ConditionalOnCheckpointRestore` 只能用于自动配置类
2. 环境检测是基于 GraalVM 运行时的
3. 通常用于原生编译的 Spring Boot 应用
4. 条件注解只能在自动配置类上使用，不能在普通配置类上使用
5. 用户自定义的 bean 总是优先于自动配置的 bean

## 常见问题

### Q: 为什么我的自动配置没有生效？

A: 检查是否在协调检查点恢复环境中运行，或者是否使用了正确的条件注解。

### Q: 与 @ConditionalOnClass 的区别？

A: `@ConditionalOnCheckpointRestore` 检查环境，而 `@ConditionalOnClass` 检查类路径。

### Q: 如何测试条件效果？

A: 使用 `--debug` 启动参数查看自动配置的条件评估情况。

### Q: 可以在非自动配置类上使用吗？

A: 不可以，条件注解只能在自动配置类上使用。

### Q: 适用于哪些场景？

A: 适用于 GraalVM 原生镜像、协调检查点恢复等场景。