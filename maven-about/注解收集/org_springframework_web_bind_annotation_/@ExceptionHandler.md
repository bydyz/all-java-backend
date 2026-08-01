# @ExceptionHandler 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.ExceptionHandler`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 3.0

## 功能说明
处理控制器方法中抛出的异常。

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
public @interface ExceptionHandler {
    Class<? extends Throwable>[] value() default {};  // @AliasFor exception
    Class<? extends Throwable>[] exception() default {}; // @since 6.2
    String[] produces() default {};                    // @since 6.2
}
```

## 使用示例

### 基本用法
```java
@ExceptionHandler(ResourceNotFoundException.class)
@ResponseStatus(HttpStatus.NOT_FOUND)
public ErrorResponse handleNotFound(ResourceNotFoundException ex) {
    return new ErrorResponse("NOT_FOUND", ex.getMessage());
}
```

### 处理多种异常
```java
@ExceptionHandler({UserNotFoundException.class, OrderNotFoundException.class})
@ResponseStatus(HttpStatus.NOT_FOUND)
public ErrorResponse handleNotFound(RuntimeException ex) {
    return new ErrorResponse("NOT_FOUND", ex.getMessage());
}
```

### 在控制器内部使用
```java
@RestController
public class UserController {
    
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    }
    
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(UserNotFoundException ex) {
        return new ErrorResponse("USER_NOT_FOUND", ex.getMessage());
    }
}
```

### 获取请求信息
```java
@ExceptionHandler(Exception.class)
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public ErrorResponse handleGeneral(Exception ex, HttpServletRequest request) {
    return new ErrorResponse("ERROR", ex.getMessage(), request.getRequestURI());
}
```

### 返回 ResponseEntity
@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
    ErrorResponse error = new ErrorResponse("ERROR", ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
}

## 版本演进
| 版本 | 变更 |
|------|------|
| 3.0 | 首次引入 |
| 6.2 | 新增 `exception()` 和 `produces()` 属性 |

## 常见问题

### Q: 支持哪些返回类型？
A: `ResponseEntity`、`@ResponseBody` 方法、`ModelAndView`、`String`（视图名）、`void`、`ProblemDetail`/`ErrorResponse`（6.0+）。

### Q: 支持哪些参数类型？
A: 异常参数、`WebRequest`、`HttpServletRequest/Response`、`Session`、`Locale`、`InputStream/OutputStream`、`Model` 等。