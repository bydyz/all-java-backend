# @RequestAttribute 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.RequestAttribute`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 4.1

## 功能说明
将请求属性（request attribute）绑定到控制器方法参数。

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
public @interface RequestAttribute {
    String value() default "";
    String name() default "";
    boolean required() default true;
}
```

## 使用示例

### 基本用法
```java
@GetMapping("/profile")
public Profile getProfile(@RequestAttribute("currentUser") User user) {
    return profileService.get(user);
}
```

### 在拦截器中设置属性
```java
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public void preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        User user = authService.getUser(request);
        request.setAttribute("currentUser", user);
    }
}
```

### 可选属性
```java
@GetMapping("/data")
public Data getData(
        @RequestAttribute(value = "cachedData", required = false) Data cached) {
    if (cached != null) {
        return cached;
    }
    return dataService.get();
}
```

### 绑定到 Map
@GetMapping("/all")
public Map<String, Object> getAllAttributes(
        @RequestAttribute Map<String, Object> attributes) {
    return attributes;
}

## 版本演进
| 版本 | 变更 |
|------|------|
| 4.1 | 首次引入 |

## 常见问题

### Q: 请求属性和请求参数的区别？
A: 请求属性是在服务器端设置的（如拦截器），请求参数是客户端发送的（URL、表单）。

### Q: 如何设置请求属性？
A: 使用 `HttpServletRequest.setAttribute()` 方法，通常在拦截器或过滤器中设置。