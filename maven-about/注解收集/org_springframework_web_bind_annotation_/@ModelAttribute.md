# @ModelAttribute 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.ModelAttribute`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 2.5

## 功能说明
将方法参数或返回值绑定到模型属性，暴露给 Web 视图。

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
@Target({PARAMETER, METHOD})
public @interface ModelAttribute {
    String value() default "";
    boolean binding() default true;
}
```

## 使用示例

### 基本用法
```java
@PostMapping("/save")
public String saveUser(@ModelAttribute User user) {
    userService.save(user);
    return "redirect:/users";
}
```

### 方法级别
```java
@ModelAttribute
public void populateModel(Model model) {
    model.addAttribute("departments", departmentService.findAll());
}
```

### 自定义属性名
```java
@PostMapping("/save")
public String saveUser(@ModelAttribute("formUser") User user) {
    userService.save(user);
    return "redirect:/users";
}
```

### 禁用绑定
@GetMapping("/form")
public String getForm(@ModelAttribute(value = "user", binding = false) User user) {
    // 不绑定请求参数到 user，只从模型中获取
    return "form";
}

## 版本演进
| 版本 | 变更 |
|------|------|
| 2.5 | 首次引入 |
| 4.3 | 新增 `binding` 属性 |

## 常见问题

### Q: `@ModelAttribute` 和 `@RequestBody` 的区别？
A: `@ModelAttribute` 用于表单数据绑定，`@RequestBody` 用于 JSON/XML 等请求体反序列化。

### Q: 什么时候使用 `@ModelAttribute`？
A: 传统 MVC 表单提交场景，在 RESTful API 中较少使用。