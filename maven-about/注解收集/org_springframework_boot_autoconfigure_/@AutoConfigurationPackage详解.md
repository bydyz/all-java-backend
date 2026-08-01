# @AutoConfigurationPackage 详解

## 概述

`@AutoConfigurationPackage` 是 Spring Boot 自动配置包注册注解，用于将包注册到 `AutoConfigurationPackages`，确定默认的自动配置包扫描范围。

## 来源

`@AutoConfigurationPackage` 属于 Spring Boot 框架，包路径：`org.springframework.boot.autoconfigure.AutoConfigurationPackage`

## 所需依赖

`@AutoConfigurationPackage` 是 Spring Boot 自动配置功能的核心，需要以下依赖：

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
       └─ spring-boot-autoconfigure  ← 包含 @AutoConfigurationPackage
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

- 将包注册到 `AutoConfigurationPackages`
- 确定默认的自动配置包扫描范围
- 被 `@EnableAutoConfiguration` 自动包含
- 用于自动配置的包扫描

## 默认值

`@AutoConfigurationPackage` 有以下属性：

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `basePackages` | 默认为空 | 要注册的基础包 |
| `basePackageClasses` | 默认为空 | 要注册的基础包类 |

## 使用方式

### 1. 基本使用

```java
@Configuration
@AutoConfigurationPackage
public class MyConfiguration {
    // 将当前类所在的包注册到 AutoConfigurationPackages
}
```

### 2. 指定基础包

```java
@Configuration
@AutoConfigurationPackage(basePackages = "com.example.config")
public class MyConfiguration {
    // 将 com.example.config 包注册到 AutoConfigurationPackages
}
```

### 3. 指定基础包类

```java
@Configuration
@AutoConfigurationPackage(basePackageClasses = {MyApplication.class, Config.class})
public class MyConfiguration {
    // 将 MyApplication 和 Config 类所在的包注册到 AutoConfigurationPackages
}
```

### 4. 与 @EnableAutoConfiguration 一起使用

```java
@Configuration
@EnableAutoConfiguration
@AutoConfigurationPackage
public class MyConfiguration {
    // @EnableAutoConfiguration 会自动包含 @AutoConfigurationPackage
}
```

## 工作原理

`@AutoConfigurationPackage` 通过以下机制工作：

1. **包注册**：将指定的包注册到 `AutoConfigurationPackages`
2. **包扫描**：确定自动配置的包扫描范围
3. **bean 发现**：在注册的包中发现自动配置类
4. **配置应用**：根据发现的自动配置类应用配置

## 与其他注解的关系

### 1. 与 @EnableAutoConfiguration 的关系

`@EnableAutoConfiguration` 自动包含 `@AutoConfigurationPackage`：

```java
@SpringBootApplication  // 包含 @EnableAutoConfiguration，自动包含 @AutoConfigurationPackage
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

### 2. 与 @ComponentScan 的关系

`@AutoConfigurationPackage` 与 `@ComponentScan` 不同：

```java
@SpringBootApplication  // 包含 @ComponentScan
public class MyApplication {
}

@Configuration
@AutoConfigurationPackage  // 只注册包，不扫描组件
public class MyConfiguration {
}
```

## 版本变化

| 版本 | 变化 |
|------|------|
| Spring Boot 1.3.0 | 引入 `@AutoConfigurationPackage` |
| Spring Boot 2.7.0 | 推荐与 `@AutoConfiguration` 一起使用 |

## 最佳实践

1. **通常不需要手动使用** - 通常被 `@EnableAutoConfiguration` 自动包含
2. **指定正确的包范围** - 确保注册的包范围正确
3. **避免重复注册** - 避免重复注册相同的包
4. **结合条件注解使用** - 确保自动配置类只在满足条件时才应用
5. **测试包扫描效果** - 测试自动配置的包扫描是否符合预期

## 注意事项

1. `@AutoConfigurationPackage` 通常被 `@EnableAutoConfiguration` 自动包含
2. 它只注册包，不扫描组件
3. 可以指定多个基础包或基础包类
4. 注册的包范围决定了自动配置的扫描范围
5. 用户自定义的 bean 总是优先于自动配置的 bean

## 常见问题

### Q: 为什么我的自动配置类没有被发现？

A: 检查是否正确注册了包，或者是否缺少必要的条件。

### Q: 如何指定多个基础包？

A: 使用 `basePackages` 属性或 `basePackageClasses` 属性指定多个包。

### Q: 与 @ComponentScan 的区别？

A: `@AutoConfigurationPackage` 只注册包，不扫描组件；`@ComponentScan` 会扫描组件。

### Q: 如何测试包扫描效果？

A: 使用 `--debug` 启动参数查看自动配置的加载情况。

### Q: 可以在非自动配置类上使用吗？

A: 可以，但通常只在需要注册包时使用。