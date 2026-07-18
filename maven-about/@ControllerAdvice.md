# @ControllerAdvice 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.ControllerAdvice`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 3.2

## 功能说明
声明全局的 `@ExceptionHandler`、`@InitBinder`、`@ModelAttribute` 方法，共享给多个控制器。

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
@Target(TYPE)
@Component
public @interface ControllerAdvice {
    String name() default "";
    String[] value() default {};           // @AliasFor basePackages
    String[] basePackages() default {};    // @AliasFor value
    Class<?>[] basePackageClasses() default {};
    Class<?>[] assignableTypes() default {};
    Class<? extends Annotation>[] annotations() default {};
}
```

## 使用示例

### 基本用法
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(ResourceNotFoundException ex) {
        return new ErrorResponse("NOT_FOUND", ex.getMessage());
    }
}
```

### 限定包范围
```java
@ControllerAdvice(basePackages = "com.example.api")
public class ApiExceptionHandler {
    // 只处理 com.example.api 包下控制器的异常
}
```

### 限定控制器类型
```java
@ControllerAdvice(assignableTypes = UserController.class)
public class UserExceptionHandler {
    // 只处理 UserController 的异常
}
```

### 限定注解
```java
@ControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler {
    // 只处理 @RestController 标记的控制器异常
}
```

### 初始化数据绑定
```java
@ControllerAdvice
public class GlobalBindingAdvice {
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("password");
        binder.registerCustomEditor(LocalDate.class, 
            new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), false));
    }
}
```

## 版本演进
| 版本 | 变更 |
|------|------|
| 3.2 | 首次引入 |

## 常见问题

### Q: `@ControllerAdvice` 和 `@Component` 的区别？
A: `@ControllerAdvice` 包含 `@Component`，是 Spring MVC 特有的，用于全局控制器增强。

### Q: 如何控制优先级？
A: 使用 `Ordered` 接口或 `@Order` 注解：`@Order(Ordered.HIGHEST_PRECEDENCE)`。