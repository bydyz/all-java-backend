# @CrossOrigin 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.CrossOrigin`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 4.2

## 功能说明
启用跨域资源共享（CORS），允许特定来源的请求。

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
public @interface CrossOrigin {
    String[] value() default {};
    String[] origins() default {};
    String[] allowedHeaders() default {};
    String[] exposedHeaders() default {};
    RequestMethod[] methods() default {};
    String[] allowedCredentials() default {};
    String[] maxAge() default {};
}
```

## 使用示例

### 基本用法
```java
@CrossOrigin(origins = "http://localhost:3000")
@GetMapping("/data")
public Data getData() {
    return dataService.get();
}
```

### 允许所有来源
```java
@CrossOrigin(origins = "*")
@GetMapping("/public")
public PublicData getPublicData() {
    return publicDataService.get();
}
```

### 类级别使用
```java
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class ApiController {
    // 类中所有方法都支持 CORS
}
```

### 详细配置
```java
@CrossOrigin(
    origins = {"http://localhost:3000", "https://example.com"},
    methods = {RequestMethod.GET, RequestMethod.POST},
    allowedHeaders = {"Authorization", "Content-Type"},
    exposedHeaders = {"X-Custom-Header"},
    maxAge = "3600",
    allowCredentials = "true"
)
@GetMapping("/data")
public Data getData() {
    return dataService.get();
}
```

### 全局配置
```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600);
    }
}
```

## 版本演进
| 版本 | 变更 |
|------|------|
| 4.2 | 首次引入 |

## 常见问题

### Q: `@CrossOrigin` 和全局 CORS 配置的区别？
A: `@CrossOrigin` 是细粒度的，可以控制到具体方法；全局配置是粗粒度的，对所有请求生效。

### Q: 开发环境如何允许所有来源？
A: 使用 `@CrossOrigin(origins = "*")` 或在全局配置中设置 `allowedOrigins("*")`。