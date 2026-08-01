# @PutMapping 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.PutMapping`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 4.3

## 功能说明
处理 HTTP PUT 请求的快捷注解，等价于 `@RequestMapping(method = RequestMethod.PUT)`。

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
@RequestMapping(method = RequestMethod.PUT)
public @interface PutMapping {
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
@PutMapping("/users/{id}")
public User updateUser(@PathVariable Long id, @RequestBody User user) {
    return userService.update(id, user);
}
```

### 全量更新
```java
@PutMapping("/users/{id}")
public ResponseEntity<User> updateUser(
        @PathVariable Long id,
        @RequestBody @Valid UserUpdateRequest request) {
    User updatedUser = userService.update(id, request);
    return ResponseEntity.ok(updatedUser);
}
```

## 版本演进
| 版本 | 变更 |
|------|------|
| 4.3 | 首次引入 |
| 7.0 | 新增 `version()` 属性 |

## 常见问题

### Q: PUT 和 PATCH 的区别？
A: PUT 通常用于全量替换资源，PATCH 用于部分更新资源。

### Q: `@PutMapping` 是否要求请求体必须存在？
A: 是的，通常需要配合 `@RequestBody` 使用，但 `@RequestBody` 的 `required` 属性默认为 `true`。