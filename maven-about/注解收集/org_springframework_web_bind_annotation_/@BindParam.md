# @BindParam 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.BindParam`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 7.0

## 功能说明
将 Web 请求中的值（查询参数、路径变量等）绑定到 Java 对象的字段上。

## 依赖关系

### 最小依赖
```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-web</artifactId>
    <version>7.0.0</version>
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
    <version>4.0.0</version>
</dependency>
```

## 注解属性及默认值
```java
@Target({})
public @interface BindParam {
    String value() default "";
    String name() default "";
}
```

## 使用示例

### 基本用法
```java
public class UserQuery {
    @BindParam("name")
    private String userName;
    
    @BindParam("age")
    private Integer userAge;
}

@GetMapping("/users")
public List<User> search(UserQuery query) {
    // 自动绑定查询参数到对象字段
    return userService.search(query);
}
```

### 自定义字段名
```java
public class SearchRequest {
    @BindParam("q")
    private String query;
    
    @BindParam("per_page")
    private int pageSize;
}

@GetMapping("/search")
public List<Result> search(SearchRequest request) {
    return searchService.search(request);
}
```

### 与路径变量配合
```java
public class ResourceRequest {
    @BindParam("id")
    private Long resourceId;
    
    @BindParam("version")
    private String apiVersion;
}

@GetMapping("/resources/{id}/{version}")
public Resource getResource(ResourceRequest request) {
    return resourceService.get(request);
}
```

## 版本演进
| 版本 | 变更 |
|------|------|
| 7.0 | 首次引入 |

## 常见问题

### Q: `@BindParam` 和 `@RequestParam` 的区别？
A: `@BindParam` 用于对象字段绑定，`@RequestParam` 用于方法参数绑定。

### Q: 什么时候使用 `@BindParam`？
A: 当需要将多个请求参数绑定到一个对象时，使用 `@BindParam` 更清晰。

### Q: 是否支持嵌套对象？
A: 当前版本不支持嵌套绑定，需要手动处理。