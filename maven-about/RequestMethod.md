# RequestMethod 枚举详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.RequestMethod`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 2.5

## 功能说明
HTTP 请求方法枚举，用于 `@RequestMapping(method = ...)`。

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

## 枚举值
```java
public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE
}
```

## 使用示例

### 基本用法
```java
@RequestMapping(value = "/data", method = RequestMethod.GET)
public Data getData() {
    return dataService.get();
}
```

### 多方法映射
```java
@RequestMapping(value = "/data", method = {RequestMethod.GET, RequestMethod.POST})
public Data handleData() {
    // 处理 GET 和 POST 请求
}
```

### 在组合注解中使用
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.POST)
public @interface PostOnly { }

@PostOnly
public User create(@RequestBody User user) {
    return userService.create(user);
}
```

### 获取请求方法
@GetMapping("/info")
public String getInfo(HttpServletRequest request) {
    RequestMethod method = RequestMethod.valueOf(request.getMethod());
    return "当前请求方法: " + method;
}

## 各方法说明
| 方法 | 说明 | 幂等性 | 有请求体 |
|------|------|--------|----------|
| GET | 获取资源 | 是 | 否 |
| HEAD | 获取资源元数据 | 是 | 否 |
| POST | 创建资源 | 否 | 是 |
| PUT | 替换资源 | 是 | 是 |
| PATCH | 部分更新资源 | 否 | 是 |
| DELETE | 删除资源 | 是 | 否 |
| OPTIONS | 获取支持的方法 | 是 | 否 |
| TRACE | 回显请求 | 是 | 否 |

## 版本演进
| 版本 | 变更 |
|------|------|
| 2.5 | 首次引入 |

## 常见问题

### Q: 什么是幂等性？
A: 幂等性指多次执行同一请求产生的效果与执行一次相同。GET、PUT、DELETE 是幂等的。

### Q: 为什么推荐使用 `@GetMapping` 等组合注解？
A: 组合注解更简洁，不需要手动指定 `RequestMethod`，代码更易读。