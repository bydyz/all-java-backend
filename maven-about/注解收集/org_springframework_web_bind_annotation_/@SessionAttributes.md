# @SessionAttributes 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.SessionAttributes`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 2.5

## 功能说明
声明控制器使用的会话属性，将模型中的指定属性存入 HTTP Session。

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
public @interface SessionAttributes {
    String[] value() default {};
    String[] names() default {};
    Trial[] types() default {};
}
```

## 使用示例

### 基本用法
```java
@Controller
@SessionAttributes({"user", "cart"})
public class CheckoutController {
    
    @GetMapping("/checkout")
    public String checkout(Model model) {
        // user 和 cart 会自动存入 session
        return "checkout";
    }
}
```

### 按类型存储
```java
@Controller
@SessionAttributes(types = {User.class, Cart.class})
public class CheckoutController {
    // ...
}
```

### 混合使用
```java
@Controller
@SessionAttributes(value = {"order"}, types = {User.class})
public class OrderController {
    // ...
}
```

### 清除会话属性
```java
@GetMapping("/logout")
public String logout(SessionStatus status) {
    status.setComplete();  // 清除所有 @SessionAttributes
    return "redirect:/login";
}
```

## 版本演进
| 版本 | 变更 |
|------|------|
| 2.5 | 首次引入 |

## 常见问题

### Q: `@SessionAttributes` 和 `HttpSession` 的区别？
A: `@SessionAttributes` 是声明式的，自动管理；`HttpSession` 是编程式的，需要手动操作。

### Q: 如何清除特定的会话属性？
A: 使用 `SessionStatus.setComplete()` 会清除所有 `@SessionAttributes` 管理的属性，无法清除特定属性。