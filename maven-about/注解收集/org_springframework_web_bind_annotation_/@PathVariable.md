# @PathVariable 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.PathVariable`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 3.0

## 功能说明
将 URI 模板变量绑定到控制器方法参数。

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
public @interface PathVariable {
    String value() default "";
    String name() default "";
    boolean required() default true;
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

### 多个路径变量
```java
@GetMapping("/users/{userId}/posts/{postId}")
public Post getPost(
        @PathVariable Long userId,
        @PathVariable Long postId) {
    return postService.findById(userId, postId);
}
```

### 自定义变量名
```java
@GetMapping("/users/{user-id}")
public User getUser(@PathVariable("user-id") Long userId) {
    return userService.findById(userId);
}
```

### 可选路径变量
```java
@GetMapping("/users/{id}/{section}")
public User getUser(
        @PathVariable Long id,
        @PathVariable(required = false) String section) {
    return userService.findById(id, section);
}
```

## 版本演进
| 版本 | 变更 |
|------|------|
| 3.0 | 首次引入 |
| 5.0 | 支持 `@AliasFor` 注解 |

## 常见问题

### Q: 路径变量和查询参数的区别？
A: 路径变量是 URL 路径的一部分（如 `/users/123`），查询参数在 `?` 后面（如 `?page=0`）。

### Q: 如何验证路径变量？
A: 配合 `@Validated` 注解使用：`@GetMapping("/{id}") @Validated public User get(@PathVariable @Positive Long id)`。