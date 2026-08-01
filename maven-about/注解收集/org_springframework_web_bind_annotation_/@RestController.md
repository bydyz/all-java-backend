# @RestController 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.RestController`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 4.0

## 功能说明
组合注解 = `@Controller` + `@ResponseBody`，标记 RESTful 控制器。

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
@Target(TYPE)
@Controller
@ResponseBody
public @interface RestController {
    String value() default "";
}
```

## 使用示例

### 基本用法
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }
    
    @PostMapping
    public User create(@RequestBody User user) {
        return userService.create(user);
    }
}
```

### 带路径前缀
```java
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    // 所有方法的路径都以 /api/v1/users 开头
}
```

### 混合控制器
```java
@Controller  // 返回视图
public class PageController {
    @GetMapping("/page")
    public String getPage() {
        return "page";
    }
}

@RestController  // 返回 JSON
public class ApiController {
    @GetMapping("/api/data")
    public Data getData() {
        return dataService.get();
    }
}
```

## 版本演进
| 版本 | 变更 |
|------|------|
| 4.0 | 首次引入 |

## 常见问题

### Q: `@RestController` 和 `@Controller` 的区别？
A: `@Controller` 用于传统 MVC，方法返回视图名；`@RestController` 用于 RESTful API，方法返回响应体。

### Q: 如何在 `@RestController` 中返回视图？
A: 使用 `@ModelAttribute` 和 `ModelAndView`，或单独创建 `@Controller` 类处理视图。