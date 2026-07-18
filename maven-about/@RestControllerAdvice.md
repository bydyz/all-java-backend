# @RestControllerAdvice 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.RestControllerAdvice`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 4.3

## 功能说明
组合注解 = `@ControllerAdvice` + `@ResponseBody`，异常处理方法直接返回响应体。

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
@ControllerAdvice
@ResponseBody
public @interface RestControllerAdvice {
    String name() default "";
    String[] value() default {};
    String[] basePackages() default {};
    Class<?>[] basePackageClasses() default {};
    Class<?>[] assignableTypes() default {};
    Class<? extends Annotation>[] annotations() default {};
}
```

## 使用示例

### 基本用法
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(ResourceNotFoundException ex) {
        return new ErrorResponse("NOT_FOUND", ex.getMessage());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
            .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }
}
```

### 限定范围
```java
@RestControllerAdvice(basePackages = "com.example.api")
public class ApiExceptionHandler {
    // 只处理 com.example.api 包下控制器的异常
}
```

### 返回 ProblemDetail（6.0+）
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleGeneral(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR, "服务器内部错误");
        problem.setTitle("Internal Server Error");
        return problem;
    }
}
```

## 版本演进
| 版本 | 变更 |
|------|------|
| 4.3 | 首次引入 |
| 6.0 | 支持 `ProblemDetail` 返回类型 |

## 常见问题

### Q: `@RestControllerAdvice` 和 `@ControllerAdvice` 的区别？
A: `@RestControllerAdvice` = `@ControllerAdvice` + `@ResponseBody`，异常处理方法直接返回 JSON/XML。

### Q: 何时使用 `@RestControllerAdvice`？
A: 在 RESTful API 项目中，异常处理方法需要返回 JSON 响应时使用。