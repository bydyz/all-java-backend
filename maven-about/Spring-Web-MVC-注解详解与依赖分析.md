# Spring Web MVC 注解详解与依赖分析

> 包路径：`org.springframework.web.bind.annotation`
> 提供者：Spring Framework（`spring-web` 模块）
> 适用框架：Spring MVC + Spring WebFlux（注解本身在 `spring-web` 中，MVC/WebFlux 各自提供处理机制）

---

## 一、依赖关系分析

### 1.1 最小依赖

要使用 `org.springframework.web.bind.annotation.*` 下的所有注解，**最小依赖**是：

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-web</artifactId>
    <version>6.1.6</version> <!-- 根据实际项目选择版本 -->
</dependency>
```

> 所有注解类（`@RequestMapping`、`@GetMapping`、`@RequestBody` 等）都定义在 `spring-web` 模块中。

### 1.2 依赖的依赖（transitive）

`spring-web` 本身不依赖 `spring-webmvc`。但要**实际运行** Spring MVC（即让注解生效），需要 `spring-webmvc`：

```
spring-webmvc
├── spring-web          ← 注解定义所在
├── spring-aop
├── spring-beans
├── spring-context
├── spring-core
└── spring-expression
```

### 1.3 工程中的实际获取方式

在 Spring Boot 工程中，我们通常不直接引入 `spring-web` 或 `spring-webmvc`，而是通过 **Spring Boot Starter** 获取：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>3.2.5</version>
</dependency>
```

**`spring-boot-starter-web` 的完整依赖树：**

```
spring-boot-starter-web (3.2.5)
├── spring-boot-starter          ← Spring Boot 核心 starter
│   ├── spring-boot              ← 自动配置、条件注解
│   ├── spring-boot-autoconfigure ← 自动配置逻辑
│   ├── spring-context            ← IoC 容器
│   ├── spring-core              ← 核心工具
│   ├── spring-beans             ← Bean 工厂
│   └── jakarta.annotation       ← @Component 等注解
├── spring-boot-starter-json     ← JSON 序列化
│   ├── spring-boot-starter
│   ├── spring-web               ← ★ 注解定义所在
│   ├── jackson-databind         ← Jackson 核心
│   ├── jackson-datatype-jdk8
│   ├── jackson-datatype-jsr310
│   └── jackson-module-parameter-names
├── spring-boot-starter-tomcat   ← 内嵌 Tomcat
│   ├── jakarta.annotation-api
│   ├── tomcat-embed-core
│   ├── tomcat-embed-el
│   └── tomcat-embed-websocket
├── spring-web                   ← ★ 直接依赖
└── spring-webmvc                ← ★ MVC 实现（处理注解的 HandlerMapping/HandlerAdapter）
    ├── spring-aop
    ├── spring-beans
    ├── spring-context
    ├── spring-core
    ├── spring-expression
    └── spring-web
```

### 1.4 总结：三级依赖关系

| 层级 | 依赖 | 说明 |
|------|------|------|
| **工程安装** | `spring-boot-starter-web` | 工程中通常安装的依赖 |
| **直接传递** | `spring-web`、`spring-webmvc`、`spring-boot-starter-json`、`spring-boot-starter-tomcat` | starter 直接引入 |
| **传递依赖** | `spring-core`、`spring-beans`、`spring-context`、`spring-aop`、`spring-expression`、Jackson 全家桶、Tomcat 嵌入式 | 二级传递 |
| **最底层** | `jakarta.annotation-api`、`jackson-annotations`、`logback`、`slf4j` 等 | 三级传递 |

### 1.5 最新版本（Spring Boot 4.1.x / Spring Framework 7.x）

从 Spring Boot 4.0+ 起，`spring-boot-starter-web` 被标记为 **deprecated**，推荐使用：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>
```

---

## 二、所有注解一览

该包下共包含 **28 个注解 + 1 个枚举 + 1 个接口**（截至 Spring Framework 7.0.8）：

| 注解 | 引入版本 | 用途分类 |
|------|----------|----------|
| `@RequestMapping` | 2.5 | 请求映射 |
| `@GetMapping` | 4.3 | 请求映射（GET） |
| `@PostMapping` | 4.3 | 请求映射（POST） |
| `@PutMapping` | 4.3 | 请求映射（PUT） |
| `@DeleteMapping` | 4.3 | 请求映射（DELETE） |
| `@PatchMapping` | 4.3 | 请求映射（PATCH） |
| `@RequestParam` | 2.5 | 参数绑定 |
| `@PathVariable` | 3.0 | 参数绑定 |
| `@RequestBody` | 3.0 | 参数绑定 |
| `@RequestHeader` | 3.0 | 参数绑定 |
| `@CookieValue` | 3.0 | 参数绑定 |
| `@MatrixVariable` | 3.2 | 参数绑定 |
| `@RequestPart` | 3.1 | 参数绑定（文件上传） |
| `@RequestAttribute` | 4.1 | 参数绑定 |
| `@ModelAttribute` | 2.5 | 模型绑定 |
| `@SessionAttributes` | 2.5 | 会话管理 |
| `@SessionAttribute` | 4.3 | 会话管理 |
| `@ResponseBody` | 3.0 | 响应处理 |
| `@ResponseStatus` | 2.5 | 响应处理 |
| `@RestController` | 4.0 | 控制器标记 |
| `@ControllerAdvice` | 3.2 | 全局处理 |
| `@RestControllerAdvice` | 4.3 | 全局处理 |
| `@ExceptionHandler` | 3.0 | 异常处理 |
| `@InitBinder` | 2.5 | 数据绑定初始化 |
| `@CrossOrigin` | 4.2 | CORS 跨域 |
| `@Mapping` | 5.0 | 元注解 |
| `@BindParam` | 7.0 | 参数绑定（新） |
| `RequestMethod` | 2.5 | HTTP 方法枚举 |
| `ValueConstants` | 3.1 | 值常量接口 |

---

## 三、各注解详细说明

### 3.1 请求映射注解

#### `@RequestMapping`

```java
@Target({TYPE, METHOD})
@Retention(RUNTIME)
public @interface RequestMapping {
    String name() default "";
    String[] value() default {};   // @AliasFor path
    String[] path() default {};    // @AliasFor value
    RequestMethod[] method() default {};
    String[] params() default {};
    String[] headers() default {};
    String[] consumes() default {};
    String[] produces() default {};
    String version() default "";   // @since 7.0
}
```

- **用途**：将 HTTP 请求映射到控制器方法
- **可用位置**：类级别（定义基础路径）+ 方法级别
- **默认值**：所有属性默认为空数组/空字符串，`version` 是 7.0 新增
- **注意**：方法级别推荐使用 `@GetMapping` 等组合注解代替

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) { ... }
}
```

---

#### `@GetMapping`

```java
@Target(METHOD)
@RequestMapping(method = RequestMethod.GET)
public @interface GetMapping { ... }
```

- **用途**：处理 HTTP GET 请求的快捷注解
- **引入版本**：4.3
- **等价于**：`@RequestMapping(method = RequestMethod.GET)`
- **属性**：`value`/`path`/`params`/`headers`/`consumes`/`produces`/`name`/`version`（均与 `@RequestMapping` 对应）

---

#### `@PostMapping`

```java
@RequestMapping(method = RequestMethod.POST)
```

- **用途**：处理 HTTP POST 请求
- **引入版本**：4.3

---

#### `@PutMapping`

```java
@RequestMapping(method = RequestMethod.PUT)
```

- **用途**：处理 HTTP PUT 请求
- **引入版本**：4.3

---

#### `@DeleteMapping`

```java
@RequestMapping(method = RequestMethod.DELETE)
```

- **用途**：处理 HTTP DELETE 请求
- **引入版本**：4.3

---

#### `@PatchMapping`

```java
@RequestMapping(method = RequestMethod.PATCH)
```

- **用途**：处理 HTTP PATCH 请求
- **引入版本**：4.3

---

### 3.2 请求参数绑定注解

#### `@RequestParam`

```java
@Target(PARAMETER)
public @interface RequestParam {
    String value() default "";   // @AliasFor name
    String name() default "";    // @AliasFor value
    boolean required() default true;
    String defaultValue() default "\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n";
}
```

- **用途**：将 URL 查询参数、表单数据绑定到方法参数
- **引入版本**：2.5
- **默认值**：`required = true`；设置 `defaultValue` 后 `required` 自动变为 `false`
- **适用场景**：Spring MVC 中查询参数、表单数据、multipart 请求

```java
@GetMapping("/search")
public List<User> search(
    @RequestParam String keyword,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size) { ... }
```

---

#### `@PathVariable`

```java
@Target(PARAMETER)
public @interface PathVariable {
    String value() default "";
    String name() default "";
    boolean required() default true;
}
```

- **用途**：将 URI 模板变量绑定到方法参数
- **引入版本**：3.0
- **默认值**：`required = true`

```java
@GetMapping("/{id}")
public User getUser(@PathVariable Long id) { ... }

@GetMapping("/{id}/posts/{postId}")
public Post getPost(@PathVariable Long id, @PathVariable Long postId) { ... }
```

---

#### `@RequestBody`

```java
@Target(PARAMETER)
public @interface RequestBody {
    boolean required() default true;
}
```

- **用途**：将请求体（JSON/XML 等）反序列化为 Java 对象
- **引入版本**：3.0
- **默认值**：`required = true`
- **依赖**：需要 `HttpMessageConverter`（通常由 Jackson 提供）

```java
@PostMapping
public User createUser(@RequestBody @Valid User user) { ... }
```

---

#### `@RequestHeader`

```java
@Target(PARAMETER)
public @interface RequestHeader {
    String value() default "";
    String name() default "";
    boolean required() default true;
    String defaultValue() default "\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n";
}
```

- **用途**：将 HTTP 请求头绑定到方法参数
- **引入版本**：3.0
- **默认值**：`required = true`

---

#### `@CookieValue`

```java
@Target(PARAMETER)
public @interface CookieValue {
    String value() default "";
    String name() default "";
    boolean required() default true;
    String defaultValue() default "\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n";
}
```

- **用途**：将 HTTP Cookie 值绑定到方法参数
- **引入版本**：3.0
- **默认值**：`required = true`

---

#### `@MatrixVariable`

```java
@Target(PARAMETER)
public @interface MatrixVariable {
    String value() default "";
    String name() default "";
    String pathVar() default "\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n";
    boolean required() default true;
}
```

- **用途**：将 URL 路径段中的键值对绑定到方法参数（如 `/cars;color=red;year=2024`）
- **引入版本**：3.2
- **默认值**：`required = true`
- **注意**：需要启用 `ConfigurerAdapter.setPathSegmentDelimiter()`

---

#### `@RequestPart`

```java
@Target(PARAMETER)
public @interface RequestPart {
    String value() default "";
    String name() default "";
    boolean required() default true;
}
```

- **用途**：将 `multipart/form-data` 请求的某一部分绑定到方法参数
- **引入版本**：3.1
- **默认值**：`required = true`

---

#### `@RequestAttribute`

```java
@Target(PARAMETER)
public @interface RequestAttribute {
    String value() default "";
    String name() default "";
    boolean required() default true;
}
```

- **用途**：将请求属性（request attribute）绑定到方法参数
- **引入版本**：4.1
- **默认值**：`required = true`

---

### 3.3 模型与会话注解

#### `@ModelAttribute`

```java
@Target({PARAMETER, METHOD})
public @interface ModelAttribute {
    String value() default "";
    boolean binding() default true;
}
```

- **用途**：将方法参数或返回值绑定到模型属性，暴露给 Web 视图
- **引入版本**：2.5
- **默认值**：`binding = true`

```java
@PostMapping("/save")
public String saveUser(@ModelAttribute User user) { ... }
```

---

#### `@SessionAttributes`

```java
@Target(TYPE)
public @interface SessionAttributes {
    String[] value() default {};
    String[] names() default {};
    Trial[] types() default {};
}
```

- **用途**：声明控制器使用的会话属性，将模型中的指定属性存入 HTTP Session
- **引入版本**：2.5

```java
@Controller
@SessionAttributes({"user", "cart"})
public class CheckoutController { ... }
```

---

#### `@SessionAttribute`

```java
@Target(PARAMETER)
public @interface SessionAttribute {
    String value() default "";
    String name() default "";
    boolean required() default true;
}
```

- **用途**：将方法参数绑定到会话中的某个属性
- **引入版本**：4.3
- **默认值**：`required = true`

---

### 3.4 响应处理注解

#### `@ResponseBody`

```java
@Target({TYPE, METHOD})
public @interface ResponseBody { }
```

- **用途**：指示方法返回值直接绑定到 HTTP 响应体（通过 `HttpMessageConverter` 序列化）
- **引入版本**：3.0
- **类型级别可用**（4.0+）：标记在类上后，该类所有方法默认返回响应体

---

#### `@ResponseStatus`

```java
@Target({TYPE, METHOD})
public @interface ResponseStatus {
    HttpStatus value() default HttpStatus.OK;
    String reason() default "";
}
```

- **用途**：指定方法或异常类返回的 HTTP 状态码
- **引入版本**：2.5
- **默认值**：`value = HttpStatus.OK`（200）

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public User create(@RequestBody User user) { ... }

@ResponseStatus(HttpStatus.NOT_FOUND, reason = "用户不存在")
public class UserNotFoundException extends RuntimeException { ... }
```

---

### 3.5 控制器与全局处理注解

#### `@RestController`

```java
@Target(TYPE)
@Controller
@ResponseBody
public @interface RestController {
    String value() default "";
}
```

- **用途**：组合注解 = `@Controller` + `@ResponseBody`，标记 RESTful 控制器
- **引入版本**：4.0
- **默认值**：`value = ""`

---

#### `@ControllerAdvice`

```java
@Target(TYPE)
@Component
public @interface ControllerAdvice {
    String name() default "";
    String[] value() default {};           // @AliasFor basePackages
    String[] basePackages() default {};    // @AliasFor value
    Class<?>[] basePackageClasses() default {};
    Class<?>[] assignableTypes() default {};
    Class<? extends Annotation>[] annotations() default {};
}
```

- **用途**：声明全局的 `@ExceptionHandler`、`@InitBinder`、`@ModelAttribute` 方法，共享给多个控制器
- **引入版本**：3.2
- **选择器**：`basePackages`、`basePackageClasses`、`assignableTypes`、`annotations`，多个选择器之间为 OR 逻辑
- **排序**：通过 `Ordered` 接口或 `@Order` 注解控制优先级

---

#### `@RestControllerAdvice`

```java
@ControllerAdvice
@ResponseBody
public @interface RestControllerAdvice {
    String name() default "";
    String[] value() default {};
    String[] basePackages() default {};
    Class<?>[] basePackageClasses() default {};
    Class<?>[] assignableTypes() default {};
    Class<? extends Annotation>[] annotations() default {};
}
```

- **用途**：组合注解 = `@ControllerAdvice` + `@ResponseBody`，异常处理方法直接返回响应体
- **引入版本**：4.3

---

#### `@ExceptionHandler`

```java
@Target(METHOD)
public @interface ExceptionHandler {
    Class<? extends Throwable>[] value() default {};  // @AliasFor exception
    Class<? extends Throwable>[] exception() default {}; // @since 6.2
    String[] produces() default {};                    // @since 6.2
}
```

- **用途**：处理控制器方法中抛出的异常
- **引入版本**：3.0
- **支持的返回类型**：`ResponseEntity`、`@ResponseBody` 方法、`ModelAndView`、`String`（视图名）、`void`、`ProblemDetail`/`ErrorResponse`（6.0+）
- **支持的参数类型**：异常参数、`WebRequest`、`HttpServletRequest/Response`、`Session`、`Locale`、`InputStream/OutputStream`、`Model` 等

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(ResourceNotFoundException ex) {
        return new ErrorResponse("NOT_FOUND", ex.getMessage());
    }
}
```

---

### 3.6 数据绑定与跨域注解

#### `@InitBinder`

```java
@Target(METHOD)
public @interface InitBinder {
    String[] value() default {};
}
```

- **用途**：标识初始化 `WebDataBinder` 的方法，用于自定义请求参数绑定规则
- **引入版本**：2.5

```java
@InitBinder
public void initBinder(WebDataBinder binder) {
    binder.setDisallowedFields("password");
    binder.registerCustomEditor(LocalDate.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), false));
}
```

---

#### `@CrossOrigin`

```java
@Target({TYPE, METHOD})
public @interface CrossOrigin {
    String[] value() default {};
    String[] origins() default {};
    String[] allowedHeaders() default {};
    String[] exposedHeaders() default {};
    RequestMethod[] methods() default {};
    String[] allowedCredentials() default {};
    String[] maxAge() default {};
}
```

- **用途**：启用跨域资源共享（CORS），允许特定来源的请求
- **引入版本**：4.2
- **默认值**：所有属性默认为空数组（空 = 允许所有）

```java
@CrossOrigin(origins = "http://localhost:3000", maxAge = "3600")
@GetMapping("/data")
public Data getData() { ... }
```

---

### 3.7 元注解与辅助

#### `@Mapping`

```java
@Target(METHOD)
@Retention(RUNTIME)
public @interface Mapping { }
```

- **用途**：元注解，标记自定义的组合请求映射注解
- **引入版本**：5.0

---

#### `RequestMethod`（枚举）

```java
public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE
}
```

- **用途**：HTTP 请求方法枚举，用于 `@RequestMapping(method = ...)`

---

#### `ValueConstants`（接口）

```java
public interface ValueConstants {
    String DEFAULT_NONE = "\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n";
}
```

- **用途**：注解中表示"未设置值"的占位常量（内部使用）

---

#### `@BindParam`

```java
@Target({})
public @interface BindParam {
    String value() default "";
    String name() default "";
}
```

- **用途**：将 Web 请求中的值（查询参数、路径变量等）绑定到 Java 对象的字段上
- **引入版本**：7.0（Spring Framework 7.x 新增）

---

## 四、版本演进速览

| 版本 | 重要变更 |
|------|----------|
| **2.5** | `@RequestMapping`、`@RequestParam`、`@ModelAttribute`、`@InitBinder`、`@SessionAttributes`、`@ResponseStatus` 首次引入 |
| **3.0** | `@PathVariable`、`@RequestBody`、`@ResponseBody`、`@RequestHeader`、`@CookieValue`、`@ExceptionHandler` 引入 |
| **3.1** | `@RequestPart` 引入；`ValueConstants` 接口引入 |
| **3.2** | `@ControllerAdvice`、`@MatrixVariable` 引入 |
| **4.0** | `@RestController` 引入；`@ResponseBody` 支持类级别使用 |
| **4.1** | `@RequestAttribute` 引入 |
| **4.2** | `@CrossOrigin` 引入 |
| **4.3** | `@GetMapping`/`@PostMapping`/`@PutMapping`/`@DeleteMapping`/`@PatchMapping`、`@SessionAttribute`、`@RestControllerAdvice` 引入 |
| **5.0** | `@Mapping` 元注解引入 |
| **6.2** | `@ExceptionHandler` 新增 `exception()` 和 `produces()` 属性 |
| **7.0** | `@RequestMapping` 新增 `version()` 属性（API 版本控制）；`@BindParam` 新增 |

---

## 五、工程使用示例

### 5.1 pom.xml 配置

```xml
<!-- 方式一：Spring Boot 工程（推荐） -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>3.2.5</version>
</dependency>

<!-- 方式二：非 Spring Boot 工程（最小依赖） -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>6.1.6</version>
</dependency>

<!-- 方式三：Spring Boot 4.x+ 新 starter（推荐新项目使用） -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>
```

### 5.2 完整控制器示例

```java
@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UserController {

    @GetMapping
    public Page<User> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return userService.findAll(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return userService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody @Valid UserCreateRequest request) {
        return userService.create(request);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody @Valid UserUpdateRequest request) {
        return userService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
```

### 5.3 全局异常处理示例

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Resource Not Found");
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
            .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleGeneral(Exception ex) {
        return ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR, "服务器内部错误");
    }
}
```

---

## 六、常见问题

### Q：为什么引入了 `spring-boot-starter-web` 还是报错 `NoClassDefFoundError`？

A：检查是否同时引入了冲突的依赖，或者使用了错误的版本。确保 Spring Boot 版本与 Spring Framework 版本兼容。

### Q：`spring-web` 和 `spring-webmvc` 的区别？

A：`spring-web` 包含 HTTP 客户端、`HttpMessageConverter`、以及所有注解定义。`spring-webmvc` 是 Spring MVC 的实现，包含 `HandlerMapping`、`HandlerAdapter`、`ViewResolver` 等。注解在 `spring-web` 中定义，MVC 在 `spring-webmvc` 中处理。

### Q：非 Spring Boot 项目如何使用？

A：直接引入 `spring-webmvc` 依赖即可获取所有注解。需要额外配置 `DispatcherServlet` 和 `HandlerMapping`。

### Q：Spring WebFlux 能用这些注解吗？

A：可以。`spring-webflux` 同样支持 `@RequestMapping`、`@GetMapping` 等注解，通过 `RequestMappingHandlerMapping` 和 `RequestMappingHandlerAdapter` 处理。但 `@RequestParam` 在 WebFlux 中只映射查询参数（不包含表单数据）。
