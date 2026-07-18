# @CookieValue 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.CookieValue`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 3.0

## 功能说明
将 HTTP Cookie 值绑定到控制器方法参数。

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
public @interface CookieValue {
    String value() default "";
    String name() default "";
    boolean required() default true;
    String defaultValue() default "\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n";
}
```

## 使用示例

### 基本用法
```java
@GetMapping("/profile")
public Profile getProfile(@CookieValue("sessionId") String sessionId) {
    return profileService.get(sessionId);
}
```

### 带默认值
```java
@GetMapping("/preferences")
public Preferences getPreferences(
        @CookieValue(value = "theme", defaultValue = "light") String theme) {
    return preferencesService.get(theme);
}
```

### 可选 Cookie
```java
@GetMapping("/analytics")
public Analytics getAnalytics(
        @CookieValue(value = "trackingId", required = false) String trackingId) {
    return analyticsService.get(trackingId);
}
```

### 获取 Cookie 对象
@GetMapping("/cookies")
public Cookie[] getCookies(HttpServletRequest request) {
    return request.getCookies();
}

## 版本演进
| 版本 | 变更 |
|------|------|
| 3.0 | 首次引入 |
| 5.0 | 支持 `@AliasFor` 注解 |

## 常见问题

### Q: 常用的 Cookie 有哪些？
A: `sessionId`（会话标识）、`theme`（主题偏好）、`language`（语言设置）、`trackingId`（跟踪ID）等。

### Q: 如何设置 Cookie？
A: 使用 `HttpServletResponse.addCookie()` 或 `ResponseCookie` 构建器。