# ValueConstants 接口详解

## 基本信息
- **包路径**: `org.springframework.web.bind.annotation.ValueConstants`
- **提供者**: Spring Framework (`spring-web` 模块)
- **引入版本**: Spring 3.1

## 功能说明
注解中表示"未设置值"的占位常量（内部使用）。

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

## 接口定义
```java
public interface ValueConstants {
    String DEFAULT_NONE = "\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n";
}
```

## 使用示例

### 内部使用
```java
// 在 @RequestParam 等注解中使用
@Target(PARAMETER)
public @interface RequestParam {
    String defaultValue() default ValueConstants.DEFAULT_NONE;
}
```

### 判断是否设置了值
```java
public void checkValue(String value) {
    if (ValueConstants.DEFAULT_NONE.equals(value)) {
        // 未设置值
    } else {
        // 已设置值
    }
}
```

### 在自定义注解中使用
```java
@Target(PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomParam {
    String value() default ValueConstants.DEFAULT_NONE;
}

public void process(@CustomParam(required = false) String param) {
    if (ValueConstants.DEFAULT_NONE.equals(param)) {
        // 使用默认逻辑
    }
}
```

## 版本演进
| 版本 | 变更 |
|------|------|
| 3.1 | 首次引入 |

## 常见问题

### Q: 为什么使用这么奇怪的字符串作为默认值？
A: 这个特殊字符串（包含 Unicode 控制字符）几乎不可能在实际使用中出现，确保能区分"未设置"和"设置了空字符串"。

### Q: 在业务代码中需要使用吗？
A: 通常不需要，这是框架内部使用的常量。只有在自定义注解或框架扩展时才可能用到。

### Q: 如何判断参数是否被设置？
A: 使用 `ValueConstants.DEFAULT_NONE.equals(value)` 判断。