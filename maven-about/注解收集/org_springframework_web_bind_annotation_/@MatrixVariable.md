# @MatrixVariable 注解详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.MatrixVariable`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 3.2

## 功能说明
将 URL 路径段中的键值对绑定到控制器方法参数（如 `/cars;color=red;year=2024`）。

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
public @interface MatrixVariable {
    String value() default "";
    String name() default "";
    String pathVar() default "\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n";
    boolean required() default true;
}
```

## 使用示例

### 基本用法
```java
// URL: /cars;color=red;year=2024
@GetMapping("/cars/{carId}")
public Car getCar(
        @PathVariable String carId,
        @MatrixVariable String color) {
    return carService.get(carId, color);
}
```

### 多个矩阵变量
```java
// URL: /cars;color=red;year=2024
@GetMapping("/cars/{carId}")
public Car getCar(
        @PathVariable String carId,
        @MatrixVariable String color,
        @MatrixVariable int year) {
    return carService.get(carId, color, year);
}
```

### 指定路径段
```java
// URL: /owners/123/cars;color=red
@GetMapping("/owners/{ownerId}/cars/{carId}")
public Car getCar(
        @PathVariable String ownerId,
        @PathVariable String carId,
        @MatrixVariable(pathVar = "carId") String color) {
    return carService.get(ownerId, carId, color);
}
```

### 绑定到 Map
@GetMapping("/cars/{carId}")
public Car getCar(
        @PathVariable String carId,
        @MatrixVariable Map<String, String> matrixVars) {
    return carService.get(carId, matrixVars);
}

## 版本演进
| 版本 | 变更 |
|------|------|
| 3.2 | 首次引入 |

## 常见问题

### Q: 矩阵变量和查询参数的区别？
A: 矩阵变量在 URL 路径段中，用分号分隔；查询参数在 `?` 后面，用 `&` 分隔。

### Q: 如何启用矩阵变量支持？
A: 需要配置 `WebMvcConfigurer.setPathSegmentDelimiter()` 或使用 `RequestMappingHandlerMapping`。