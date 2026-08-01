# @AutoConfigureBefore 详解

## 概述

`@AutoConfigureBefore` 是 Spring Boot 自动配置顺序控制注解，用于指示自动配置应在其他指定的自动配置类之前应用。

## 来源

`@AutoConfigureBefore` 属于 Spring Boot 框架，包路径：`org.springframework.boot.autoconfigure.AutoConfigureBefore`

## 所需依赖

`@AutoConfigureBefore` 是 Spring Boot 自动配置功能的核心，需要以下依赖：

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
       └─ spring-boot-autoconfigure  ← 包含 @AutoConfigureBefore
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

- 指示自动配置应在其他指定的自动配置类之前应用
- 用于控制自动配置的顺序
- 与 `@AutoConfigureAfter` 一起使用，形成完整的顺序控制

## 默认值

`@AutoConfigureBefore` 有以下属性：

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `value` | 默认为空 | 要在其之前应用的自动配置类 |
| `before` | 默认为空 | 要在其之前应用的自动配置类（别名） |

## 使用方式

### 1. 基本使用

```java
@AutoConfiguration
@AutoConfigureBefore(MyOtherAutoConfiguration.class)
public class MyAutoConfiguration {
    // 这个配置会在 MyOtherAutoConfiguration 之前应用
}
```

### 2. 指定多个配置类

```java
@AutoConfiguration
@AutoConfigureBefore({
    MyOtherAutoConfiguration.class,
    AnotherAutoConfiguration.class
})
public class MyAutoConfiguration {
    // 这个配置会在 MyOtherAutoConfiguration 和 AnotherAutoConfiguration 之前应用
}
```

### 3. 使用类名指定

```java
@AutoConfiguration
@AutoConfigureBefore("org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration")
public class MyAutoConfiguration {
    // 这个配置会在 DataSourceAutoConfiguration 之前应用
}
```

### 4. 结合条件注解使用

```java
@AutoConfiguration
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@ConditionalOnClass(DataSource.class)
@ConditionalOnProperty(prefix = "spring.datasource", name = "url")
public class MyDataSourceAutoConfiguration {
    // 这个配置会在 DataSourceAutoConfiguration 之前应用
    // 但只在满足条件时才应用
}
```

## 工作原理

`@AutoConfigureBefore` 通过以下机制工作：

1. **顺序声明**：声明当前自动配置类应该在指定的自动配置类之前应用
2. **依赖分析**：Spring Boot 分析所有自动配置类的顺序依赖关系
3. **拓扑排序**：根据顺序依赖关系对自动配置类进行拓扑排序
4. **按序应用**：按照排序后的顺序应用自动配置类

## 与其他注解的关系

### 1. 与 @AutoConfigureAfter 的关系

`@AutoConfigureBefore` 和 `@AutoConfigureAfter` 是一对互补的注解：

```java
@AutoConfiguration
@AutoConfigureBefore(MyOtherAutoConfiguration.class)
public class BeforeAutoConfiguration {
}

@AutoConfiguration
@AutoConfigureAfter(MyAutoConfiguration.class)
public class AfterAutoConfiguration {
}
```

### 2. 与 @AutoConfigureOrder 的关系

`@AutoConfigureBefore` 和 `@AutoConfigureAfter` 提供了更直观的顺序控制，而 `@AutoConfigureOrder` 提供了数字排序：

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

## 版本变化

| 版本 | 变化 |
|------|------|
| Spring Boot 1.0.0 | 引入 `@AutoConfigureBefore` |
| Spring Boot 2.7.0 | 推荐与 `@AutoConfiguration` 一起使用 |

## 最佳实践

1. **明确顺序依赖** - 只在确实需要控制顺序时使用
2. **避免循环依赖** - 确保顺序依赖关系是单向的，避免循环依赖
3. **优先使用 @AutoConfigureBefore/After** - 比 `@AutoConfigureOrder` 更直观
4. **结合条件注解使用** - 确保自动配置类只在满足条件时才应用
5. **测试顺序效果** - 测试自动配置的顺序是否符合预期

## 注意事项

1. `@AutoConfigureBefore` 只能用于自动配置类
2. 顺序依赖关系必须是单向的，不能有循环依赖
3. 顺序控制只影响自动配置类的应用顺序，不影响 bean 的创建顺序
4. 用户自定义的 bean 总是优先于自动配置的 bean
5. 可以使用类名或类引用指定要控制顺序的自动配置类

## 常见问题

### Q: 为什么我的自动配置顺序不对？

A: 检查是否正确使用了 `@AutoConfigureBefore` 和 `@AutoConfigureAfter`，或者是否使用了 `@AutoConfigureOrder`。

### Q: 可以指定多个自动配置类吗？

A: 可以，`@AutoConfigureBefore` 的 `value` 和 `before` 属性都接受数组。

### Q: 与 @AutoConfigureOrder 的区别？

A: `@AutoConfigureBefore` 和 `@AutoConfigureAfter` 提供了更直观的顺序控制，而 `@AutoConfigureOrder` 提供了数字排序。

### Q: 如何测试自动配置顺序？

A: 使用 `--debug` 启动参数查看自动配置的加载顺序。

### Q: 顺序控制只影响自动配置类吗？

A: 是的，顺序控制只影响自动配置类的应用顺序，不影响 bean 的创建顺序。