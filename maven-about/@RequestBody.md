# @RequestBody 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.RequestBody`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 3.0

## 功能说明
将请求体（JSON/XML 等）反序列化为 Java 对象。

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
@Target(PARAMETER)
public @interface RequestBody {
    boolean required() default true;
}
```

## 使用示例

### 基本用法
```java
@PostMapping("/users")
public User createUser(@RequestBody User user) {
    return userService.create(user);
}
```

### 带验证
```java
@PostMapping("/users")
public User createUser(@RequestBody @Valid UserCreateRequest request) {
    return userService.create(request);
}
```

### 可选请求体
```java
@PostMapping("/users")
public User createUser(@RequestBody(required = false) User user) {
    if (user == null) {
        return User.defaultUser();
    }
    return userService.create(user);
}
```

### 接收多种类型
@PostMapping("/webhook")
public ResponseEntity<Void> handleWebhook(@RequestBody String payload) {
    // 处理原始字符串
    return ResponseEntity.ok().build();
}

## 版本演进
| 版本 | 变更 |
|------|------|
| 3.0 | 首次引入 |
| 4.1 | 支持 `ReadableByteChannel` |
| 6.0 | 支持 `ProblemDetail` 返回类型 |

## 常见问题

### Q: `@RequestBody` 需要什么依赖才能工作？
A: 需要 `HttpMessageConverter` 实现，通常由 `spring-boot-starter-json`（Jackson）提供。

### Q: 如何处理 JSON 解析错误？
A: 使用 `@ControllerAdvice` + `@ExceptionHandler(HttpMessageNotReadableException.class)` 全局处理。