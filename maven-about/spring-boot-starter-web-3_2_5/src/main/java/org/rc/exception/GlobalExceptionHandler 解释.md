# 全局异常处理器（`@RestControllerAdvice` + `@ExceptionHandler`）使用说明

`@RestControllerAdvice` + `@ExceptionHandler` 是 **Spring MVC 的自动异常映射机制**。定义好之后，你 **不需要在任何地方调用它**，它会在 Spring 启动时被自动注册为 `HandlerExceptionResolver`（具体是 `ExceptionHandlerExceptionResolver`），当 Controller 调用链中有异常抛出且未被捕获时，由 Spring 自动匹配并调用对应方法返回响应。

---

## 一、前提条件（不满足则不生效）

1. **被组件扫描到**：`org.rc.exception` 必须在 `@SpringBootApplication` 启动类所在包的同级或子包内；否则需手动 `@ComponentScan("org.rc.exception")` 或 `@Import(GlobalExceptionHandler.class)`。
2. **`ErrorResponse` 能被 Jackson 序列化**：有 getter 或为 `record`，否则返回 500 序列化错误。
3. **异常必须"冒泡"到 DispatcherServlet**：Controller / Service 内部 `try-catch` 吞掉的异常不会触发。

---

## 二、触发方式（三种常见写法）

### 1. Controller 直接抛

```java
@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        // 抛出 → 被 handleNotFound 捕获 → 404
        throw new ResourceNotFoundException("用户不存在: " + id);
    }
}
```

### 2. Service 抛，Controller 不捕获

```java
@Service
public class UserService {
    public User findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("用户不存在: " + id));
    }
}
```

### 3. 参数校验抛 `IllegalArgumentException`

```java
public void updateAge(int age) {
    if (age < 0) throw new IllegalArgumentException("年龄不能为负");
}
```

这三种都会自动走 `GlobalExceptionHandler`，前端分别收到：

```json
// 404
{ "code": "NOT_FOUND", "message": "用户不存在: 1" }

// 400
{ "code": "BAD_REQUEST", "message": "年龄不能为负" }

// 500
{ "code": "INTERNAL_ERROR", "message": "服务器内部错误: xxx" }
```

（具体字段名取决于你的 `ErrorResponse` 定义。）

---

## 三、异常匹配规则

- **精确优先**：`ResourceNotFoundException` 会优先匹配 `handleNotFound`，不会落到 `handleGeneral`。
- **父子关系**：`Exception.class` 是兜底，所有未匹配的异常都到这里。建议这里 **记录日志** 并 **隐藏堆栈细节**（生产环境不要直接 `ex.getMessage()` 返回）。
- **同一异常多个 handler**：抛 `IllegalStateException` 时，`handleBadRequest`（`IllegalArgumentException`）**不会** 命中，因为 `IllegalStateException` 不是 `IllegalArgumentException` 的子类；它会落到 `handleGeneral`。请确认你的异常继承关系。

---

## 四、容易踩的坑

### 1. Spring 内置异常被兜底成 500

以下异常 **不会** 自动变 400/404，会被 `handleGeneral` 吃掉返回 500：

- `MethodArgumentNotValidException`（`@Valid` 校验失败）
- `MissingServletRequestParameterException`
- `HttpMessageNotReadableException`（JSON 解析失败）
- `MethodArgumentTypeMismatchException`
- `HttpRequestMethodNotSupportedException`

如需正确状态码，补充 handler：

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ErrorResponse> handleValid(MethodArgumentNotValidException ex) {
    String msg = ex.getBindingResult().getFieldErrors().stream()
        .map(e -> e.getField() + ": " + e.getDefaultMessage())
        .collect(Collectors.joining("; "));
    return ResponseEntity.badRequest()
        .body(new ErrorResponse("VALIDATION_FAILED", msg));
}
```

### 2. Filter / Interceptor 里的异常抓不到

`@RestControllerAdvice` 只作用于 **DispatcherServlet 内的 Controller 调用链**。`OncePerRequestFilter`、Servlet `Filter`、异步线程、`@Async` 中抛出的异常不会经过它，需要在 Filter 自身 catch 或使用 `HandlerExceptionResolver` 手动处理。

### 3. 响应已被写出

如果 Controller 里已经 `response.getWriter().write(...)` 后抛异常，会报 `IllegalStateException: getOutputStream() has already been called`。确保异常发生在写响应之前。

### 4. 多个 `@ControllerAdvice` 顺序

若你有多个 Advice，用 `@Order(Ordered.HIGHEST_PRECEDENCE)` 控制优先级；否则顺序不确定。

### 5. 限定作用范围（可选）

```java
@RestControllerAdvice(basePackages = "org.rc.controller")
// 或
@RestControllerAdvice(assignableTypes = UserController.class)
```

---

## 五、验证方式

启动应用后用 curl 测试即可：

```bash
curl -i http://localhost:8080/users/999
# HTTP/1.1 404
# {"code":"NOT_FOUND","message":"用户不存在: 999"}

curl -i "http://localhost:8080/users?age=-1"
# HTTP/1.1 400
```

如需查看匹配过程，可在 handler 中打断点或加日志 `log.warn("caught: {}", ex.getClass())`。

---

## 六、小结

| 你做的事 | 谁来做 |
|---|---|
| 写 `@RestControllerAdvice` 类 | Spring 启动时自动注册 |
| 在 Service/Controller `throw new XxxException()` | 你 |
| 捕获 & 转 HTTP 状态码 & JSON | **框架自动**（通过匹配 `@ExceptionHandler`） |
| 什么都不用做 | 只要异常冒泡出 Controller 方法即可 |

所以"如何使用"的答案就是：**照常抛异常即可，不要吞异常，确保 `ErrorResponse` 可序列化、包被扫描到。**
