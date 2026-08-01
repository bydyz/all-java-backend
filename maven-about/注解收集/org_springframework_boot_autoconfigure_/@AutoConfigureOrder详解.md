# @AutoConfigureOrder 详解

## 概述

`@AutoConfigureOrder` 是 Spring Boot 自动配置排序注解，用于对自动配置类进行排序，是 `@Order` 注解的自动配置专用变体。

## 来源

`@AutoConfigureOrder` 属于 Spring Boot 框架，包路径：`org.springframework.boot.autoconfigure.AutoConfigureOrder`

## 所需依赖

`@AutoConfigureOrder` 是 Spring Boot 自动配置功能的核心，需要以下依赖：

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
       └─ spring-boot-autoconfigure  ← 包含 @AutoConfigureOrder
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

- 对自动配置类进行排序
- 数值越小，优先级越高
- 用于控制自动配置类的应用顺序
- 是 `@Order` 注解的自动配置专用变体

## 默认值

`@AutoConfigureOrder` 有以下属性：

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `value` | `Ordered.LOWEST_PRECEDENCE` | 排序值，数值越小优先级越高 |

## 使用方式

### 1. 基本使用

```java
@AutoConfiguration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class MyAutoConfiguration {
    // 这个配置会最先应用
}
```

### 2. 指定低优先级

```java
@AutoConfiguration
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
public class MyAutoConfiguration {
    // 这个配置会最后应用
}
```

### 3. 使用具体数值

```java
@AutoConfiguration
@AutoConfigureOrder(100)
public class MyAutoConfiguration {
    // 这个配置会在优先级为 100 的配置中应用
}
```

### 4. 结合条件注解使用

```java
@AutoConfiguration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnClass(DataSource.class)
@ConditionalOnProperty(prefix = "spring.datasource", name = "url")
public class MyDataSourceAutoConfiguration {
    // 这个配置会最先应用
    // 但只在满足条件时才应用
}
```

## 工作原理

`@AutoConfigureOrder` 通过以下机制工作：

1. **排序声明**：声明自动配置类的排序值
2. **顺序收集**：Spring Boot 收集所有自动配置类的排序值
3. **排序处理**：根据排序值对自动配置类进行排序
4. **按序应用**：按照排序后的顺序应用自动配置类

## 与其他注解的关系

### 1. 与 @AutoConfigureBefore/After 的关系

`@AutoConfigureOrder` 提供了数字排序，而 `@AutoConfigureBefore` 和 `@AutoConfigureAfter` 提供了更直观的顺序控制：

```java
@AutoConfiguration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class FirstAutoConfiguration {
}

@AutoConfiguration
@AutoConfigureBefore(AnotherAutoConfiguration.class)
public class BeforeAnotherAutoConfiguration {
}
```

### 2. 与 @Order 的关系

`@AutoConfigureOrder` 是 `@Order` 注解的自动配置专用变体：

```java
@AutoConfiguration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)  // 自动配置专用
public class MyAutoConfiguration {
}

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)  // 通用排序
public class MyComponent {
}
```

## 版本变化

| 版本 | 变化 |
|------|------|
| Spring Boot 1.3.0 | 引入 `@AutoConfigureOrder` |
| Spring Boot 2.7.0 | 推荐与 `@AutoConfiguration` 一起使用 |

## 最佳实践

1. **优先使用 @AutoConfigureBefore/After** - 比 `@AutoConfigureOrder` 更直观
2. **明确排序值** - 使用 `Ordered` 接口的常量或明确的数值
3. **避免过度使用** - 只在确实需要控制顺序时使用
4. **结合条件注解使用** - 确保自动配置类只在满足条件时才应用
5. **测试顺序效果** - 测试自动配置的顺序是否符合预期

## 注意事项

1. `@AutoConfigureOrder` 只能用于自动配置类
2. 数值越小，优先级越高
3. 顺序控制只影响自动配置类的应用顺序，不影响 bean 的创建顺序
4. 用户自定义的 bean 总是优先于自动配置的 bean
5. 可以使用 `Ordered` 接口的常量或明确的数值

## 常见问题

### Q: 为什么我的自动配置顺序不对？

A: 检查是否正确使用了 `@AutoConfigureBefore` 和 `@AutoConfigureAfter`，或者是否使用了 `@AutoConfigureOrder`。

### Q: 排序值的范围是多少？

A: 排序值可以是任何整数，但推荐使用 `Ordered` 接口的常量。

### Q: 与 @AutoConfigureBefore/After 的区别？

A: `@AutoConfigureOrder` 提供了数字排序，而 `@AutoConfigureBefore` 和 `@AutoConfigureAfter` 提供了更直观的顺序控制。

### Q: 如何测试自动配置顺序？

A: 使用 `--debug` 启动参数查看自动配置的加载顺序。

### Q: 顺序控制只影响自动配置类吗？

A: 是的，顺序控制只影响自动配置类的应用顺序，不影响 bean 的创建顺序。