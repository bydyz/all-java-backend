# @RequestPart 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.RequestPart`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 3.1

## 功能说明
将 `multipart/form-data` 请求的某一部分绑定到控制器方法参数。

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
public @interface RequestPart {
    String value() default "";
    String name() default "";
    boolean required() default true;
}
```

## 使用示例

### 基本用法
```java
@PostMapping("/upload")
public ResponseEntity<String> upload(
        @RequestPart MultipartFile file,
        @RequestPart String description) {
    // 处理文件和描述
    return ResponseEntity.ok("上传成功");
}
```

### 接收 JSON 对象
```java
@PostMapping("/upload")
public ResponseEntity<String> upload(
        @RequestPart MultipartFile file,
        @RequestPart @Valid UserMetadata metadata) {
    // 处理文件和元数据
    return ResponseEntity.ok("上传成功");
}
```

### 可选部分
```java
@PostMapping("/upload")
public ResponseEntity<String> upload(
        @RequestPart MultipartFile file,
        @RequestPart(required = false) String description) {
    // 描述是可选的
    return ResponseEntity.ok("上传成功");
}
```

### 绑定到 List
@PostMapping("/upload")
public ResponseEntity<String> upload(
        @RequestPart List<MultipartFile> files) {
    // 处理多个文件
    return ResponseEntity.ok("上传成功");
}

## 版本演进
| 版本 | 变更 |
|------|------|
| 3.1 | 首次引入 |

## 常见问题

### Q: `@RequestPart` 和 `@RequestParam` 的区别？
A: `@RequestPart` 用于 `multipart/form-data` 的各个部分，支持二进制数据；`@RequestParam` 用于表单字段。

### Q: 如何限制文件大小？
A: 在 `application.properties` 中配置：
```properties
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=50MB
```