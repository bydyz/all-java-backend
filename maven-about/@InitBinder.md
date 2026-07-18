# @InitBinder 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.InitBinder`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 2.5

## 功能说明
标识初始化 `WebDataBinder` 的方法，用于自定义请求参数绑定规则。

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
public @interface InitBinder {
    String[] value() default {};
}
```

## 使用示例

### 基本用法
```java
@InitBinder
public void initBinder(WebDataBinder binder) {
    binder.setDisallowedFields("password");
}
```

### 限定字段
```java
@InitBinder("user")
public void initUserBinder(WebDataBinder binder) {
    binder.setDisallowedFields("id", "createdAt");
}
```

### 注册自定义编辑器
```java
@InitBinder
public void initBinder(WebDataBinder binder) {
    binder.registerCustomEditor(LocalDate.class, 
        new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), false));
    binder.registerCustomEditor(Money.class, 
        new MoneyEditor());
}
```

### 设置验证器
```java
@InitBinder
public void initBinder(WebDataBinder binder) {
    binder.setValidator(new CustomValidator());
}
```

### 在 @ControllerAdvice 中全局使用
```java
@ControllerAdvice
public class GlobalBindingAdvice {
    
    @InitBinder
    public void globalInitBinder(WebDataBinder binder) {
        binder.setDisallowedFields("password", "secret");
        binder.registerCustomEditor(LocalDate.class, 
            new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), false));
    }
}
```

## 版本演进
| 版本 | 变更 |
|------|------|
| 2.5 | 首次引入 |

## 常见问题

### Q: `@InitBinder` 方法什么时候执行？
A: 每次请求处理前执行，在 `@RequestMapping` 方法之前。

### Q: 如何全局应用？
A: 在 `@ControllerAdvice` 类中定义 `@InitBinder` 方法，对所有控制器生效。

### Q: 常见用途有哪些？
A: 禁用字段、注册自定义编辑器、设置日期格式、配置验证器等。