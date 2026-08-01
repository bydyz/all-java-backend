# @GetMapping 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.GetMapping`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 4.3

## 功能说明
处理 HTTP GET 请求的快捷注解，等价于 `@RequestMapping(method = RequestMethod.GET)`。

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
@Target(METHOD)
@RequestMapping(method = RequestMethod.GET)
public @interface GetMapping {
    String name() default "";
    String[] value() default {};
    String[] path() default {};
    String[] params() default {};
    String[] headers() default {};
    String[] consumes() default {};
    String[] produces() default {};
    String version() default "";  // @since 7.0
}
```

## 使用示例

### 基本用法
```java
@GetMapping("/users/{id}")
public User getUser(@PathVariable Long id) {
    return userService.findById(id);
}
```

### 带查询参数
```java
@GetMapping("/search")
public List<User> search(
    @RequestParam String keyword,
    @RequestParam(defaultValue = "0") int page) {
    return userService.search(keyword, page);
}
```

### 限制媒体类型
@GetMapping(value = "/data", produces = "application/json")
public Data getData() {
    return dataService.get();
}

## 版本演进
| 版本 | 变更 |
|------|------|
| 4.3 | 首次引入 |
| 7.0 | 新增 `version()` 属性 |

## 常见问题

### Q: `@GetMapping` 和 `@RequestMapping` 的区别？
A: `@GetMapping` 是 `@RequestMapping` 的组合注解，专门用于 GET 请求，更简洁。

### Q: 能否在类级别使用 `@GetMapping`？
A: 不能，`@GetMapping` 只能用于方法级别。