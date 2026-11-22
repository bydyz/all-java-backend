`spring-boot-starter-validation` 是 Spring Boot 提供的**参数校验起步依赖**，它底层整合了 Hibernate Validator（JSR-380 / Jakarta Bean Validation 的实现），让你能通过注解（如 `@NotBlank`、`@Email`、`@Size`）声明式地校验请求参数、配置属性等，而不用手写大量的 `if-else` 判断。

对于已使用 `spring-boot-starter-web 3.2.5` 的工程，直接安装**同版本 3.2.5** 的 `spring-boot-starter-validation` 即可，二者版本严格对齐。

---

### 📦 如何添加依赖

从 Spring Boot 2.3 开始，校验组件已从 `spring-boot-starter-web` 中移除，需要单独显式引入。

**Maven 写法：**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
    <version>3.2.5</version>
</dependency>
```

**Gradle 写法：**

```groovy
implementation 'org.springframework.boot:spring-boot-starter-validation:3.2.5'
```

> **注意**：如果你的项目使用了 Spring Boot 的 Parent POM 或 BOM 管理，`<version>` 可以省略，由 BOM 统一管理版本，但当前你已指定 web 为 3.2.5，validation 也应保持 3.2.5 以避免兼容性问题。

---

### 📝 代码详解：从 DTO 到全局异常处理

#### 🏷️ 在 DTO / 实体类上声明约束

使用 `jakarta.validation.constraints.*` 下的注解（注意 Spring Boot 3.x 已从 `javax.validation` 迁移到 `jakarta.validation`）。

```java
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public record CreateUserRequest(

    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度必须在 2~20 之间")
    String username,

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    String email,

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 64, message = "密码长度必须在 8~64 之间")
    String password,

    @NotNull(message = "年龄不能为空")
    @Min(value = 18, message = "年龄不能小于 18 岁")
    @Max(value = 120, message = "年龄不能大于 120 岁")
    Integer age,

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "手机号格式不正确")
    String phone,

    @PastOrPresent(message = "出生日期不能晚于今天")
    LocalDate birthDate,

    @NotEmpty(message = "角色列表不能为空")
    List<@NotBlank(message = "角色名不能为空白") String> roles
) {}
```

**常用约束注解速查：**

| 注解 | 作用 |
|---|---|
| `@NotNull` | 不能为 `null`（允许空字符串） |
| `@NotBlank` | 不能为 `null`，且去除首尾空格后长度 > 0（仅限字符串） |
| `@NotEmpty` | 不能为 `null`，且长度/大小 > 0（字符串、集合、数组） |
| `@Size(min, max)` | 长度/大小在指定范围内 |
| `@Min` / `@Max` | 数值边界 |
| `@Email` | 邮箱格式校验 |
| `@Pattern(regexp)` | 正则匹配 |
| `@Past` / `@Future` | 日期必须在过去 / 未来 |

---

#### 🎯 在 Controller 中触发校验

在需要校验的方法参数上添加 `@Valid`（或 `@Validated`），Spring 会在进入方法体之前自动执行校验，校验失败则抛出 `MethodArgumentNotValidException`。

```java
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @PostMapping
    public ResponseEntity<String> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        // 能执行到这里，说明 request 已经通过了全部约束校验
        return ResponseEntity.ok("用户创建成功：" + request.username());
    }

    @GetMapping("/{id}")
    public String getUser(
            @PathVariable @Positive(message = "用户 ID 必须为正数") Long id) {
        return "查询用户：" + id;
    }
}
```

> **`@Valid` 与 `@Validated` 的区别**：`@Valid` 是 Jakarta 标准注解，用于触发校验；`@Validated` 是 Spring 的注解，额外支持**分组校验**（`groups`），且可以标注在类上以开启方法级校验。日常简单场景用 `@Valid` 即可。

---

#### 🛡️ 全局异常处理（返回友好的错误信息）

默认情况下，校验失败会返回 Spring 的 400 错误，但信息不友好。建议通过 `@RestControllerAdvice` 统一捕获并格式化错误。

```java
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalValidationHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("参数校验失败");

        // 收集所有字段错误：fieldName -> errorMessage
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage() != null
                                ? fieldError.getDefaultMessage()
                                : "无效值",
                        (existing, replacement) -> existing + "; " + replacement
                ));

        problem.setProperty("errors", errors);
        return problem;
    }
}
```

这样当请求体中的 `username` 为空时，客户端会收到类似以下的响应：

```json
{
  "type": "about:blank",
  "title": "参数校验失败",
  "status": 400,
  "errors": {
    "username": "用户名不能为空"
  }
}
```

---

### 💡 关键注意事项

1. **务必使用 `jakarta.validation` 而非 `javax.validation`**：Spring Boot 3.x 基于 Jakarta EE 9+，旧的 `javax` 包名下的注解不会生效。
2. **`spring-boot-starter-web` 不包含校验**：从 2.3 起已分离，不单独引入 `spring-boot-starter-validation`，所有 `@NotBlank`、`@Valid` 等注解都不会起作用。
3. **版本对齐**：`spring-boot-starter-validation` 的版本应与 `spring-boot-starter-web` 保持一致（本场景中为 **3.2.5**），该 starter 会自动引入匹配的 `hibernate-validator`（3.2.5 对应 8.0.1.Final）及 `tomcat-embed-el`。
4. **嵌套校验**：如果 DTO 的字段本身是另一个对象或集合，需要在对应字段上也标注 `@Valid`，校验才会级联进入内部对象。
