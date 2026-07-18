# @RequestHeader 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.RequestHeader`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 3.0

## 功能说明
将 HTTP 请求头绑定到控制器方法参数。

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
public @interface RequestHeader {
    String value() default "";
    String name() default "";
    boolean required() default true;
    String defaultValue() default "\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n";
}
```

## 使用示例

### 基本用法
```java
@GetMapping("/data")
public Data getData(@RequestHeader("Authorization") String authHeader) {
    // 使用认证头
    return dataService.get();
}
```

### 带默认值
```java
@GetMapping("/info")
public Info getInfo(
        @RequestHeader(value = "Accept-Language", defaultValue = "zh-CN") String lang) {
    return infoService.get(lang);
}
```

### 可选请求头
```java
@GetMapping("/analytics")
public Analytics getAnalytics(
        @RequestHeader(value = "X-User-Id", required = false) String userId) {
    return analyticsService.get(userId);
}
```

### 绑定多个值
@GetMapping("/headers")
public Map<String, String> getHeaders(
        @RequestHeader Map<String, String> headers) {
    return headers;
}

## 版本演进
| 版本 | 变更 |
|------|------|
| 3.0 | 首次引入 |
| 5.0 | 支持 `@AliasFor` 注解 |

## 常见问题

### Q: 常用的请求头有哪些？
A: `Authorization`（认证）、`Content-Type`（内容类型）、`Accept-Language`（语言）、`User-Agent`（客户端信息）等。

### Q: 如何获取所有请求头？
A: 使用 `@RequestHeader Map<String, String> headers` 或直接注入 `HttpServletRequest`。