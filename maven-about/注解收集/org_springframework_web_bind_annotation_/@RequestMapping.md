# @RequestMapping 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.RequestMapping`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 2.5

## 功能说明
将 HTTP 请求映射到控制器类或方法。支持类级别（基础路径）和方法级别（具体端点）。

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
@Retention(RUNTIME)
public @interface RequestMapping {
    String name() default "";
    String[] value() default {};   // @AliasFor path
    String[] path() default {};    // @AliasFor value
    RequestMethod[] method() default {};
    String[] params() default {};
    String[] headers() default {};
    String[] consumes() default {};
    String[] produces() default {};
    String version() default "";   // @since 7.0
}
```

## 使用示例

### 基本用法
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }
    
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.create(user);
    }
}
```

### 多方法映射
```java
@RequestMapping(value = "/data", method = {RequestMethod.GET, RequestMethod.POST})
public Data handleData() {
    // 处理 GET 和 POST 请求
}
```

## 版本演进
| 版本 | 变更 |
|------|------|
| 2.5 | 首次引入 |
| 5.0 | 新增 `consumes`、`produces` 属性 |
| 7.0 | 新增 `version()` 属性用于 API 版本控制 |

## 常见问题

### Q: 为什么推荐使用 `@GetMapping` 等组合注解？
A: 组合注解更简洁，且在方法级别使用时不需要重复指定 `method` 属性。

### Q: 类级别和方法级别的 `@RequestMapping` 如何配合？
A: 类级别定义基础路径，方法级别定义具体路径，最终路径为两者拼接。