# @ResponseStatus 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.ResponseStatus`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 2.5

## 功能说明
指定方法或异常类返回的 HTTP 状态码。

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
@Target({TYPE, METHOD})
public @interface ResponseStatus {
    HttpStatus value() default HttpStatus.OK;
    String reason() default "";
}
```

## 使用示例

### 创建资源
```java
@PostMapping("/users")
@ResponseStatus(HttpStatus.CREATED)
public User create(@RequestBody User user) {
    return userService.create(user);
}
```

### 成功无返回值
```java
@DeleteMapping("/{id}")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void delete(@PathVariable Long id) {
    userService.delete(id);
}
```

### 异常类
```java
@ResponseStatus(HttpStatus.NOT_FOUND, reason = "用户不存在")
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("用户不存在: " + id);
    }
}
```

### 自定义原因
@PutMapping("/{id}")
@ResponseStatus(value = HttpStatus.OK, reason = "更新成功")
public User update(@PathVariable Long id, @RequestBody User user) {
    return userService.update(id, user);
}

## 版本演进
| 版本 | 变更 |
|------|------|
| 2.5 | 首次引入 |

## 常见问题

### Q: `@ResponseStatus` 和 `ResponseEntity` 的区别？
A: `@ResponseStatus` 只设置状态码，`ResponseEntity` 可以同时设置状态码、响应头和响应体。

### Q: 异常处理时如何使用？
A: 在异常类上添加 `@ResponseStatus`，当异常抛出时自动设置对应状态码。