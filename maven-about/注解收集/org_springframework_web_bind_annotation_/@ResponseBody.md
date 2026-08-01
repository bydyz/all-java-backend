# @ResponseBody 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.ResponseBody`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 3.0

## 功能说明
指示方法返回值直接绑定到 HTTP 响应体（通过 `HttpMessageConverter` 序列化）。

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
public @interface ResponseBody { }
```

## 使用示例

### 基本用法
```java
@GetMapping("/users/{id}")
@ResponseBody
public User getUser(@PathVariable Long id) {
    return userService.findById(id);
}
```

### 类级别使用（4.0+）
```java
@RestController  // 等价于 @Controller + @ResponseBody
public class UserController {
    
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }
}
```

### 混合使用
```java
@Controller
public class PageController {
    
    @GetMapping("/page")
    public String getPage() {
        return "page";  // 返回视图名
    }
    
    @GetMapping("/api/data")
    @ResponseBody
    public Data getData() {
        return dataService.get();  // 返回 JSON
    }
}
```

### 返回 ResponseEntity
@GetMapping("/users/{id}")
@ResponseBody
public ResponseEntity<User> getUser(@PathVariable Long id) {
    return userService.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
}

## 版本演进
| 版本 | 变更 |
|------|------|
| 3.0 | 首次引入 |
| 4.0 | 支持类级别使用 |

## 常见问题

### Q: `@ResponseBody` 和 `@RestController` 的区别？
A: `@RestController` 是 `@Controller` + `@ResponseBody` 的组合注解，类中所有方法都自动添加 `@ResponseBody`。

### Q: 如何自定义序列化？
A: 配置 `HttpMessageConverter`，如 Jackson 的 `ObjectMapper` 配置。