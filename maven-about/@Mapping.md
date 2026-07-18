# @Mapping 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.Mapping`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 5.0

## 功能说明
元注解，标记自定义的组合请求映射注解。

## 依赖关系

### 最小依赖
```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-web</artifactId>
    <version>6.1.6</version>
</dependency>
```

### 完整依赖链
```
spring-webmvc
├── spring-web          ← 注解定义所在
├── spring-aop
├── spring-beans
├── spring-context
├── spring-core
└── spring-expression
```

### 工程中实际获取方式
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>3.2.5</version>
</dependency>
```

## 注解属性及默认值
```java
@Target(METHOD)
@Retention(RUNTIME)
public @interface Mapping { }
```

## 使用示例

### 创建自定义组合注解
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
public @interface JsonPostMapping {
    @AliasFor(annotation = RequestMapping.class, attribute = "value")
    String[] value() default {};
}
```

### 使用自定义注解
```java
@JsonPostMapping("/users")
public User createUser(@RequestBody User user) {
    return userService.create(user);
}
```

### 创建 REST 风格注解
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
public @interface JsonGetMapping {
    @AliasFor(annotation = RequestMapping.class, attribute = "value")
    String[] value() default {};
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public @interface JsonPostMapping {
    @AliasFor(annotation = RequestMapping.class, attribute = "value")
    String[] value() default {};
}
```

### 限制 HTTP 方法
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping(method = RequestMethod.DELETE)
public @interface DeleteOnly {
    @AliasFor(annotation = RequestMapping.class, attribute = "value")
    String[] value() default {};
}

## 版本演进
| 版本 | 变更 |
|------|------|
| 5.0 | 首次引入 |

## 常见问题

### Q: 为什么要使用 `@Mapping` 元注解？
A: 创建自定义组合注解，统一项目中的映射风格，减少重复配置。

### Q: `@Mapping` 和 `@RequestMapping` 的区别？
A: `@Mapping` 是元注解，用于标记其他注解；`@RequestMapping` 是实际的请求映射注解。