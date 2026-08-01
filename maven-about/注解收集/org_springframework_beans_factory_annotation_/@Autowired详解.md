# @Autowired 详解

## 概述

`@Autowired` 是 Spring 框架提供的依赖注入注解，用于自动装配 Bean。

## 来源

`@Autowired` 属于 Spring 框架，包路径：`org.springframework.beans.factory.annotation.Autowired`

## 所需依赖

`@Autowired` 是 Spring 框架核心功能，不需要额外依赖。

### 依赖传递链

```
spring-boot-starter-web
  └─ spring-boot-starter
       └─ spring-core
            └─ spring-beans  ← 包含 @Autowired
```

### 可用依赖

```xml
<!-- 方式1：使用 Spring Boot Starter（会引入 web 相关） -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- 方式2：最小依赖（推荐） -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
</dependency>

<!-- 方式3：最精简依赖 -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-beans</artifactId>
</dependency>
```

> 使用 `spring-boot-starter-web` 可以使用，但引入了不需要的 web 依赖。如果只需要 DI 功能，使用 `spring-context` 或 `spring-beans` 即可。

## 功能

- 自动按类型（byType）注入 Bean
- 可用于构造器、字段、setter 方法、参数
- 默认情况下必须存在匹配的 Bean，否则启动报错

## 默认值

`@Autowired` 有两个属性：

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `required` | `true` | 是否必须注入，false 时允许为 null |
| `required()` | `true` | 注解方法形式的属性 |

## 使用方式

### 1. 字段注入

```java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
}
```

### 2. Setter 注入

```java
@Service
public class UserService {
    private UserRepository userRepository;
    
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

### 3. 构造器注入（推荐）

```java
@Service
public class UserService {
    private final UserRepository userRepository;
    
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

> Spring 4.3+ 如果只有一个构造器，可以省略 `@Autowired`

### 4. 方法参数注入

```java
@Component
public class MyProcessor {
    public void process(@Autowired UserRepository repository) {
        // ...
    }
}
```

## 配合 @Qualifier

当存在多个同类型 Bean 时，使用 `@Qualifier` 指定名称：

```java
@Autowired
@Qualifier("primaryDataSource")
private DataSource dataSource;
```

## required 属性

```java
// 必须注入，否则报错
@Autowired(required = true)
private UserService userService;

// 允许为 null
@Autowired(required = false)
private OptionalService optionalService;
```

## 与 @Resource 的区别

| 特性 | @Autowired | @Resource |
|------|-----------|-----------|
| 来源 | Spring | JSR-250 (JDK) |
| 默认注入方式 | byType | byName |
| 指定名称 | @Qualifier | name 属性 |
| 推荐场景 | Spring 特定 | 通用 Java |

## 版本变化

| 版本 | 变化 |
|------|------|
| Spring 2.5 | 引入 `@Autowired` |
| Spring 4.3 | 单构造器可省略注解 |
| Spring 5.x | 支持反应式类型注入 |

## 最佳实践

1. **优先使用构造器注入** - 不可变、易于测试、依赖清晰
2. **避免字段注入** - 隐藏依赖、难以测试
3. **构造器只有一个时省略注解** - Spring 4.3+ 支持
4. **使用 `required=false` 处理可选依赖**
5. **多实现配合 `@Qualifier` 使用**
