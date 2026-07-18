# @SessionAttribute 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.SessionAttribute`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 4.3

## 功能说明
将方法参数绑定到会话中的某个属性。

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
public @interface SessionAttribute {
    String value() default "";
    String name() default "";
    boolean required() default true;
}
```

## 使用示例

### 基本用法
```java
@GetMapping("/profile")
public Profile getProfile(@SessionAttribute("currentUser") User user) {
    return profileService.get(user);
}
```

### 可选会话属性
```java
@GetMapping("/cart")
public Cart getCart(
        @SessionAttribute(value = "cart", required = false) Cart cart) {
    if (cart == null) {
        return new Cart();
    }
    return cart;
}
```

### 与 @SessionAttributes 配合
```java
@Controller
@SessionAttributes("user")
public class UserController {
    
    @GetMapping("/dashboard")
    public String dashboard(@SessionAttribute("user") User user, Model model) {
        // user 来自 session
        return "dashboard";
    }
}
```

### 绑定到 Map
@GetMapping("/session")
public Map<String, Object> getSessionAttributes(
        @SessionAttribute Map<String, Object> sessionAttrs) {
    return sessionAttrs;
}

## 版本演进
| 版本 | 变更 |
|------|------|
| 4.3 | 首次引入 |

## 常见问题

### Q: `@SessionAttribute` 和 `@SessionAttributes` 的区别？
A: `@SessionAttribute` 用于方法参数，从 session 读取属性；`@SessionAttributes` 用于类，声明哪些模型属性需要存储到 session。

### Q: 如何设置会话属性？
A: 使用 `@SessionAttributes` 在类级别声明，或使用 `HttpSession.setAttribute()` 手动设置。