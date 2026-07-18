# @RequestParam 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.RequestParam`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 2.5

## 功能说明
将 URL 查询参数、表单数据绑定到控制器方法参数。

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
public @interface RequestParam {
    String value() default "";   // @AliasFor name
    String name() default "";    // @AliasFor value
    boolean required() default true;
    String defaultValue() default "\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n";
}
```

## 使用示例

### 基本用法
```java
@GetMapping("/search")
public List<User> search(@RequestParam String keyword) {
    return userService.search(keyword);
}
```

### 带默认值
```java
@GetMapping("/users")
public Page<User> list(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    return userService.findAll(PageRequest.of(page, size));
}
```

### 可选参数
```java
@GetMapping("/filter")
public List<User> filter(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Integer age) {
    return userService.filter(name, age);
}
```

### 绑定多个参数到集合
@GetMapping("/users")
public List<User> list(@RequestParam List<Long> ids) {
    return userService.findByIds(ids);
}

## 版本演进
| 版本 | 变更 |
|------|------|
| 2.5 | 首次引入 |
| 5.0 | 支持 `@AliasFor` 注解 |

## 常见问题

### Q: 设置 `defaultValue` 后 `required` 属性会怎样？
A: 设置 `defaultValue` 后，`required` 自动变为 `false`。

### Q: 如何绑定多个同名参数？
A: 使用 `List` 或数组类型：`@RequestParam List<String> values`。