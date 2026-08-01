# @DeleteMapping 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.DeleteMapping`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 4.3

## 功能说明
处理 HTTP DELETE 请求的快捷注解，等价于 `@RequestMapping(method = RequestMethod.DELETE)`。

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
@RequestMapping(method = RequestMethod.DELETE)
public @interface DeleteMapping {
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
@DeleteMapping("/users/{id}")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void deleteUser(@PathVariable Long id) {
    userService.delete(id);
}
```

### 返回删除结果
```java
@DeleteMapping("/users/{id}")
public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    boolean deleted = userService.delete(id);
    if (deleted) {
        return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
}
```

## 版本演进
| 版本 | 变更 |
|------|------|
| 4.3 | 首次引入 |
| 7.0 | 新增 `version()` 属性 |

## 常见问题

### Q: DELETE 请求通常返回什么状态码？
A: 通常返回 `204 No Content`（删除成功）或 `404 Not Found`（资源不存在）。

### Q: DELETE 请求需要请求体吗？
A: 通常不需要，但技术上可以携带请求体。