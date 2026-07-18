# @PostMapping 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.PostMapping`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 4.3

## 功能说明
处理 HTTP POST 请求的快捷注解，等价于 `@RequestMapping(method = RequestMethod.POST)`。

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
@RequestMapping(method = RequestMethod.POST)
public @interface PostMapping {
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
@PostMapping("/users")
public User createUser(@RequestBody User user) {
    return userService.create(user);
}
```

### 返回状态码
```java
@PostMapping("/users")
@ResponseStatus(HttpStatus.CREATED)
public User createUser(@RequestBody User user) {
    return userService.create(user);
}
```

### 文件上传
@PostMapping("/upload")
public ResponseEntity<String> upload(@RequestPart MultipartFile file) {
    // 处理文件上传
    return ResponseEntity.ok("上传成功");
}

## 版本演进
| 版本 | 变更 |
|------|------|
| 4.3 | 首次引入 |
| 7.0 | 新增 `version()` 属性 |

## 常见问题

### Q: `@PostMapping` 和 `@RequestMapping(method=POST)` 的区别？
A: 功能完全相同，`@PostMapping` 更简洁，是 Spring 4.3 引入的语法糖。

### Q: 如何验证请求体？
A: 配合 `@Valid` 注解使用：`@PostMapping @Valid @RequestBody User user`。