# @PatchMapping 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.PatchMapping`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 4.3

## 功能说明
处理 HTTP PATCH 请求的快捷注解，等价于 `@RequestMapping(method = RequestMethod.PATCH)`。

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
@RequestMapping(method = RequestMethod.PATCH)
public @interface PatchMapping {
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
@PatchMapping("/users/{id}")
public User updateUser(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
    return userService.partialUpdate(id, updates);
}
```

### 部分更新
```java
@PatchMapping("/users/{id}")
public ResponseEntity<User> partialUpdate(
        @PathVariable Long id,
        @RequestBody UserPatchRequest patch) {
    User updatedUser = userService.partialUpdate(id, patch);
    return ResponseEntity.ok(updatedUser);
}
```

## 版本演进
| 版本 | 变更 |
|------|------|
| 4.3 | 首次引入 |
| 7.0 | 新增 `version()` 属性 |

## 常见问题

### Q: PATCH 和 PUT 的区别？
A: PUT 用于全量替换资源，PATCH 用于部分更新资源，只发送需要修改的字段。

### Q: 什么时候使用 PATCH？
A: 当只需要更新资源的部分字段时，使用 PATCH 更高效，避免传输完整资源。