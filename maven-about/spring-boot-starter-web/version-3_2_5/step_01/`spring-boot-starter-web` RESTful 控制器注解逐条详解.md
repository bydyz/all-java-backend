# `spring-boot-starter-web` RESTful 控制器注解逐条详解

下面按 15 条逐条展开分析。每条尽量覆盖：**注解来源与定位、核心作用、关键属性、底层机制、对应示例、注意事项与常见坑**。仍只围绕 `spring-boot-starter-web` 引入的 Web MVC 注解展开。

---

## 1. `@RestController`

**来源与定位**

`org.springframework.web.bind.annotation.RestController`，是一个**类级别**注解，用于标识“这个类是一个 REST 控制器”。

**组合关系**

它的定义上带有：

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
@ResponseBody
public @interface RestController { }
```

也就是说，`@RestController` 等价于 `@Controller + @ResponseBody`。它本身没有额外属性。

**核心作用**

- `@Controller` 让该类成为 Spring MVC 的处理器，被 `RequestMappingHandlerMapping` 扫描；
- `@ResponseBody` 让该类中所有方法的返回值都交给 `HttpMessageConverter` 写出响应体，而不是当作视图名解析。

**对应示例**

```java
@RestController
@RequestMapping("/api/all-in-one")
@CrossOrigin(origins = "*")
public class AllInOne { ... }
```

类上标注一次，类内所有 `@GetMapping`、`@PostMapping` 等方法都自动具备“返回 JSON/文本到响应体”的能力。

**注意事项**

- 在 `@RestController` 中再写 `@ResponseBody` 是冗余的；
- 如果类上使用 `@Controller` 而不是 `@RestController`，则每个需要返回数据的方法都要单独加 `@ResponseBody`，否则返回值会被当作视图名；
- `@RestController` 不能用于返回 JSP/Thymeleaf 页面；需要页面渲染时应使用 `@Controller`。

---

## 2. `@RequestMapping`

**来源与定位**

`org.springframework.web.bind.annotation.RequestMapping`，是 Spring MVC **请求映射的基础注解**，可标注在**类**或**方法**上。

**核心作用**

把 HTTP 请求的 URL、方法、参数、请求头、媒体类型等条件，映射到某个 Controller 类或方法上。

**关键属性**

| 属性 | 作用 |
|---|---|
| `value` / `path` | 映射路径，数组类型，可配置多个 |
| `method` | 限定 HTTP 方法，如 `RequestMethod.GET` |
| `params` | 限定请求参数条件，如 `params = "type=admin"` |
| `headers` | 限定请求头条件，如 `headers = "X-API-VERSION=1"` |
| `consumes` | 限定请求体媒体类型，如 `application/json` |
| `produces` | 限定响应媒体类型，如 `application/json;charset=UTF-8` |
| `name` | 映射名称，主要用于文档和调试 |

**类级别与方法级别的组合**

示例中：

```java
@RequestMapping("/api/all-in-one")   // 类级别
@GetMapping("/{id}")                 // 方法级别
```

最终路径是 `/api/all-in-one/{id}`。类级别定义公共前缀，方法级别定义具体子路径，二者拼接。

**底层机制**

启动时，`RequestMappingHandlerMapping` 会扫描所有 Bean 上的 `@RequestMapping` 及其派生注解，构建 `RequestMappingInfo`，注册到映射表中。请求到达时，`DispatcherServlet` 通过 `HandlerMapping` 查找匹配的 `HandlerMethod`。

**注意事项**

- 类级别和方法级别可以同时存在，路径会拼接；
- `method` 不写时默认接受所有 HTTP 方法，这在 RESTful 中通常不推荐；
- 同一路径不同方法可以共存，例如 `GET /users` 和 `POST /users`；
- 路径匹配支持 `*`、`**`、`{var}` 等模式，但 Spring Boot 2.6+ 默认路径匹配策略有变化，需注意 `PathPatternParser` 与 `AntPathMatcher` 的差异。

---

## 3. `@GetMapping`、`@PostMapping`、`@PutMapping`、`@PatchMapping`、`@DeleteMapping`

**来源与定位**

均在 `org.springframework.web.bind.annotation` 包下，是 `@RequestMapping` 的**语义化快捷注解**，只能标注在**方法**上。

**组合关系**

以 `@GetMapping` 为例，其定义等价于：

```java
@RequestMapping(method = RequestMethod.GET)
```

其余四个分别对应 `POST`、`PUT`、`PATCH`、`DELETE`。

**核心作用**

用更清晰的语义表达 HTTP 方法，避免写 `@RequestMapping(method = RequestMethod.GET)` 这样的冗长形式。

**REST 语义对应**

| 注解 | HTTP 方法 | 典型语义 |
|---|---|---|
| `@GetMapping` | GET | 查询资源，应幂等、无副作用 |
| `@PostMapping` | POST | 新建资源，非幂等 |
| `@PutMapping` | PUT | 完整替换资源，幂等 |
| `@PatchMapping` | PATCH | 部分更新资源 |
| `@DeleteMapping` | DELETE | 删除资源，幂等 |

**对应示例**

```java
@GetMapping("/{id}")          // 查询
@PostMapping                  // 新建
@PutMapping("/{id}")          // 完整更新
@PatchMapping("/{id}")        // 部分更新
@DeleteMapping("/{id}")       // 删除
```

**注意事项**

- 这些快捷注解**不能标注在类上**，类级别公共路径仍用 `@RequestMapping`；
- 它们同样支持 `value/path`、`params`、`headers`、`consumes`、`produces` 等属性；
- 语义只是约定，Spring 不会强制 GET 必须幂等；是否幂等由业务实现决定；
- `PUT` 与 `PATCH` 的区别在于：`PUT` 通常要求提交完整资源表示，`PATCH` 只提交变更部分。

---

## 4. `@PathVariable`

**来源与定位**

`org.springframework.web.bind.annotation.PathVariable`，标注在**方法参数**上。

**核心作用**

从 URI 模板中提取变量值，绑定到方法参数。例如 `/{id}` 中的 `{id}` 对应 `@PathVariable Long id`。

**关键属性**

| 属性 | 作用 |
|---|---|
| `value` / `name` | 指定路径变量名；省略时按参数名匹配 |
| `required` | 是否必须存在，默认 `true` |

**对应示例**

```java
@GetMapping("/{id}")
public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
    return ResponseEntity.ok(Map.of("id", id));
}
```

当请求 `GET /api/all-in-one/42` 时，`id` 被绑定为 `42`，并由 Spring 的类型转换器转为 `Long`。

**底层机制**

由 `PathVariableMethodArgumentResolver` 处理。它从 `HandlerMethod` 的 URI 模板变量表中取值，再通过 `ConversionService` 做类型转换。

**注意事项**

- 参数名省略依赖编译时保留参数名（`-parameters`）或调试信息；生产环境建议显式写 `@PathVariable("id")`；
- 路径变量名必须与 `{...}` 中的名称一致，否则取不到值；
- 路径变量不适合放可选过滤条件；可选条件应使用 `@RequestParam`；
- 类型转换失败会抛出 `MethodArgumentTypeMismatchException`，默认返回 400。

---

## 5. `@RequestParam`

**来源与定位**

`org.springframework.web.bind.annotation.RequestParam`，标注在**方法参数**上。

**核心作用**

从查询字符串 `?key=value` 或 `application/x-www-form-urlencoded` 表单体中读取参数，绑定到方法参数。

**关键属性**

| 属性 | 作用 |
|---|---|
| `value` / `name` | 参数名；省略时按方法参数名匹配 |
| `required` | 是否必须，默认 `true` |
| `defaultValue` | 缺省值；设置后隐式等价于 `required = false` |

**对应示例**

```java
@GetMapping
public List<String> list(@RequestParam(required = false) String name,
                         @RequestParam(defaultValue = "0") int page) {
    return List.of("name=" + name, "page=" + page);
}
```

- `name` 可不传，未传时为 `null`；
- `page` 不传时为 `0`。

**底层机制**

由 `RequestParamMethodArgumentResolver` 处理，从 `HttpServletRequest.getParameter*` 中取值，再经类型转换。

**注意事项**

- `required = true` 且未传时，抛出 `MissingServletRequestParameterException`，默认 400；
- 设置 `defaultValue` 后即使不传也不会报错；
- `@RequestParam` 也支持绑定数组或 `List`，例如 `?ids=1&ids=2`；
- 与 `@PathVariable` 的边界：**资源标识用 PathVariable，过滤/分页/排序条件用 RequestParam**；
- 文件上传场景通常用 `@RequestPart` 或 `MultipartFile`，而不是 `@RequestParam` 绑定复杂对象。

---

## 6. `@RequestBody`

**来源与定位**

`org.springframework.web.bind.annotation.RequestBody`，标注在**方法参数**上。

**核心作用**

把整个 HTTP 请求体反序列化为一个 Java 对象，通常用于接收 JSON 或 XML。

**关键属性**

| 属性 | 作用 |
|---|---|
| `required` | 请求体是否必须，默认 `true` |

**对应示例**

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public Map<String, Object> create(@RequestBody Map<String, Object> body) {
    return body;
}
```

请求体 `{"name":"a"}` 会被 Jackson 反序列化为 `Map`。

**底层机制**

由 `RequestResponseBodyMethodProcessor` 处理。它根据请求的 `Content-Type` 选择合适的 `HttpMessageConverter`（如 `MappingJackson2HttpMessageConverter`），读取请求体并反序列化。

**注意事项**

- **一个方法最多只能有一个 `@RequestBody` 参数**，因为请求体只能被消费一次；
- 请求体为空且 `required = true` 时，抛出 `HttpMessageNotReadableException`，默认 400；
- `Content-Type` 必须与转换器匹配，例如 JSON 请求应带 `application/json`；
- `@RequestBody` 与 `@RequestParam`、`@PathVariable` 可以同时使用，因为后者不消费请求体；
- 反序列化失败通常由全局异常处理统一返回 400，而不是让异常直接冒泡。

---

## 7. `@RequestHeader` 与 `@CookieValue`

这两个注解都标注在**方法参数**上，分别从请求头和 Cookie 中取值，属性结构相似。

### 7.1 `@RequestHeader`

**来源**：`org.springframework.web.bind.annotation.RequestHeader`

**核心作用**：读取指定请求头的值。

**关键属性**：

| 属性 | 作用 |
|---|---|
| `value` / `name` | 请求头名称 |
| `required` | 是否必须，默认 `true` |
| `defaultValue` | 缺省值 |

**示例片段**：

```java
@GetMapping("/headers")
public Map<String, String> headers(
        @RequestHeader("User-Agent") String userAgent,
        @CookieValue(value = "sessionId", required = false) String sessionId) { ... }
```

**机制**：由 `RequestHeaderMethodArgumentResolver` 处理，调用 `HttpServletRequest.getHeader`。

**注意**：

- 请求头名称大小写不敏感；
- 多值请求头可绑定为 `List<String>` 或数组；
- 缺少必需请求头会抛 `MissingRequestHeaderException`，默认 400。

### 7.2 `@CookieValue`

**来源**：`org.springframework.web.bind.annotation.CookieValue`

**核心作用**：读取指定 Cookie 的值。

**关键属性**：同 `@RequestHeader`，支持 `value/name`、`required`、`defaultValue`。

**机制**：由 `ServletCookieValueMethodArgumentResolver` 处理，从 `HttpServletRequest.getCookies()` 中查找。

**注意**：

- Cookie 不存在且 `required = true` 时抛 `MissingRequestCookieException`；
- 示例中 `required = false` 使缺失时传 `null`，避免 400；
- Cookie 值通常需要 URL 解码，Spring 会按容器行为处理。

---

## 8. `@RequestPart`

**来源与定位**

`org.springframework.web.bind.annotation.RequestPart`，标注在**方法参数**上，用于 `multipart/form-data` 请求中的单个 part。

**核心作用**

把 multipart 请求中的某个 part 绑定到方法参数。该 part 可以是文件（`MultipartFile`），也可以是带 `Content-Type` 的复杂对象（由 `HttpMessageConverter` 反序列化）。

**关键属性**

| 属性 | 作用 |
|---|---|
| `value` / `name` | part 名称 |
| `required` | 是否必须，默认 `true` |

**对应示例**

```java
@PostMapping("/{id}/avatar")
public Map<String, Object> uploadAvatar(
        @PathVariable Long id,
        @RequestPart("file") MultipartFile file) {
    return Map.of("id", id, "filename", String.valueOf(file.getOriginalFilename()));
}
```

**底层机制**

由 `RequestPartMethodArgumentResolver` 处理。它从 `MultipartHttpServletRequest` 中按名称取 part；如果目标是 `MultipartFile`，直接绑定；如果是复杂对象，则根据该 part 的 `Content-Type` 选择 `HttpMessageConverter` 反序列化。

**与 `@RequestParam` 的区别**

- `@RequestParam` 主要用于简单字符串或表单字段；
- `@RequestPart` 更强调“multipart 的某一部分”，支持按 part 的 `Content-Type` 反序列化为对象；
- 文件上传两者都能绑定 `MultipartFile`，但复杂 JSON part 应使用 `@RequestPart`。

**注意事项**

- 请求必须是 `multipart/form-data`；
- Spring Boot 需配置 `MultipartFile` 相关限制（如 `spring.servlet.multipart.max-file-size`）；
- part 名称必须与 `@RequestPart("file")` 一致；
- 缺少 part 且 `required = true` 时抛 `MissingServletRequestPartException`。

---

## 9. `@ModelAttribute`

**来源与定位**

`org.springframework.web.bind.annotation.ModelAttribute`，可标注在**方法参数**或**方法**上。

**核心作用**

- 标注在参数上：把多个请求参数按属性名绑定到一个对象上；
- 标注在方法上：该方法会在同 Controller 的每个处理方法之前执行，用于向模型添加公共属性。

**关键属性**

| 属性 | 作用 |
|---|---|
| `value` / `name` | 模型属性名 |
| `binding` | 是否执行数据绑定，默认 `true` |

**对应示例**

```java
@GetMapping("/search")
public Map<String, Object> search(
        @ModelAttribute("query") Map<String, String> query) {
    return Map.of("query", query);
}
```

示例用 `Map` 占位，实际项目通常绑定查询 POJO：请求参数按属性名自动填充到对象字段。

**底层机制**

由 `ModelAttributeMethodProcessor` 处理。它创建目标对象，使用 `WebDataBinder` 将请求参数按名称绑定到对象属性，并可将绑定结果放入 `Model`。

**与 `@RequestParam` 的区别**

- `@RequestParam` 一次绑定一个参数；
- `@ModelAttribute` 一次绑定多个参数到一个对象，适合参数较多的查询或表单；
- `@ModelAttribute` 默认从查询参数和表单字段取值，不读取 JSON 请求体；读取 JSON 请求体应使用 `@RequestBody`。

**注意事项**

- 绑定失败可能产生 `BindingResult`，可紧跟在参数后接收；
- 复杂嵌套属性绑定需要注意安全，避免过度绑定（Mass Assignment）；
- 方法级 `@ModelAttribute` 常用于填充下拉框数据等公共模型属性。

---

## 10. `@ResponseBody`

**来源与定位**

`org.springframework.web.bind.annotation.ResponseBody`，可标注在**类**或**方法**上。

**核心作用**

把方法返回值通过 `HttpMessageConverter` 序列化后写入响应体，而不是解析为视图名。

**对应示例**

```java
@GetMapping("/response-body")
@ResponseBody
public String responseBody() {
    return "plain text";
}
```

在 `@RestController` 类中，这个注解是冗余的，因为 `@RestController` 已经包含 `@ResponseBody`。

**底层机制**

由 `RequestResponseBodyMethodProcessor` 处理返回值。它根据 `Accept` 头和返回值类型选择 `HttpMessageConverter`，将对象写为 JSON/XML/文本。

**注意事项**

- `@RestController` 下无需重复标注；
- 在 `@Controller` 中，只有需要返回数据的方法才加 `@ResponseBody`；
- 返回值是 `String` 且使用 `@ResponseBody` 时，会被当作响应内容而不是视图名；
- 若同时存在视图解析需求，应避免在类级别使用 `@ResponseBody`。

---

## 11. `@ResponseStatus`

**来源与定位**

`org.springframework.web.bind.annotation.ResponseStatus`，可标注在**方法**、**异常类**上。

**核心作用**

显式指定响应的 HTTP 状态码和可选原因短语。

**关键属性**

| 属性 | 作用 |
|---|---|
| `value` / `code` | HTTP 状态码，如 `HttpStatus.CREATED` |
| `reason` | 原因短语；设置后会调用 `sendError`，通常不推荐在 REST API 中滥用 |

**对应示例**

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public Map<String, Object> create(@RequestBody Map<String, Object> body) { ... }

@DeleteMapping("/{id}")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void delete(@PathVariable Long id) { ... }
```

- 新建成功返回 `201 Created`；
- 删除成功返回 `204 No Content`，方法可返回 `void`。

**底层机制**

`@ResponseStatus` 由 `ResponseStatusExceptionResolver` 或返回值处理器处理。方法上的注解会在响应写出前设置状态码；异常类上的注解会在该异常抛出时映射为对应状态码。

**注意事项**

- `@ResponseStatus` 是**静态**状态码，不能在方法体内动态改变；需要动态状态码时用 `ResponseEntity`；
- `reason` 会触发容器错误页机制，可能覆盖响应体，REST API 中通常只用状态码；
- 标注在自定义异常类上时，该异常被抛出会自动映射状态码，但仍建议配合全局异常处理统一响应结构；
- `204` 响应不应有响应体，方法返回 `void` 更合适。

---

## 12. `ResponseEntity<T>`

**来源与定位**

`org.springframework.http.ResponseEntity<T>`，**不是注解**，而是控制器返回值类型。它属于 `spring-web`，由 `spring-boot-starter-web` 间接引入。

**核心作用**

在方法体内**动态**控制响应的状态码、响应头和响应体。

**常用构建方式**

```java
ResponseEntity.ok(body);                              // 200 + body
ResponseEntity.status(HttpStatus.CREATED).body(body); // 201 + body
ResponseEntity.noContent().build();                   // 204
ResponseEntity.badRequest().body(error);              // 400 + error
```

**对应示例**

```java
@GetMapping("/{id}")
public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
    return ResponseEntity.ok(Map.of("id", id));
}
```

**底层机制**

由 `HttpEntityMethodProcessor` 处理。它读取 `ResponseEntity` 中的状态码、头信息和 body，再通过 `HttpMessageConverter` 写出 body。

**与 `@ResponseStatus` 的区别**

| 维度 | `@ResponseStatus` | `ResponseEntity` |
|---|---|---|
| 状态码 | 静态、编译期确定 | 运行期动态决定 |
| 响应头 | 不方便设置 | 可自由设置 |
| 响应体 | 由方法返回值决定 | 可显式包装 |
| 适用场景 | 固定状态码的简单接口 | 需要条件分支、动态头、错误体 |

**注意事项**

- 泛型 `T` 是响应体类型；
- 可以只返回 `ResponseEntity<Void>` 表示无响应体；
- 使用 `ResponseEntity` 时，方法返回值不再被当作视图名；
- 与全局异常处理结合时，异常处理器也可以返回 `ResponseEntity`。

---

## 13. `@ExceptionHandler`

**来源与定位**

`org.springframework.web.bind.annotation.ExceptionHandler`，标注在**方法**上，用于处理异常。

**核心作用**

捕获当前 Controller（或全局 Advice）中指定类型的异常，并用该方法的返回值作为响应。

**关键属性**

| 属性 | 作用 |
|---|---|
| `value` | 要处理的异常类型数组，可指定多个 |

**对应示例**

```java
@ExceptionHandler(IllegalArgumentException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)
public Map<String, Object> handleIllegalArgument(IllegalArgumentException ex) {
    return Map.of("error", String.valueOf(ex.getMessage()));
}
```

**底层机制**

`ExceptionHandlerExceptionResolver` 在请求处理抛出异常后，查找匹配的 `@ExceptionHandler` 方法。匹配遵循“**最具体异常优先**”：`IllegalArgumentException` 的处理器优先于 `RuntimeException`，`RuntimeException` 优先于 `Exception`。

**返回值处理**

`@ExceptionHandler` 方法的返回值处理方式与普通控制器方法类似：

- 返回对象 + `@ResponseBody`/`@RestControllerAdvice`：序列化为响应体；
- 返回 `ResponseEntity`：动态控制状态码和 body；
- 配合 `@ResponseStatus`：指定状态码。

**注意事项**

- 局部 `@ExceptionHandler` 只对当前 Controller 生效；
- 全局处理应放在 `@RestControllerAdvice` 类中；
- 同一异常类型在局部和全局都存在时，局部优先；
- 异常处理方法本身也可能抛异常，需避免无限递归；
- 不建议直接捕获 `Exception` 后返回 200，会掩盖真实错误。

---

## 14. `@RestControllerAdvice`

**来源与定位**

`org.springframework.web.bind.annotation.RestControllerAdvice`，类级别注解。

**组合关系**

等价于 `@ControllerAdvice + @ResponseBody`。`@ControllerAdvice` 本身是 `@Component` 的派生注解，会被组件扫描。

**核心作用**

定义**全局**的控制器增强逻辑，最常用于全局异常处理，也可用于全局数据绑定、全局模型属性等。

**关键属性**

| 属性 | 作用 |
|---|---|
| `basePackages` / `basePackageClasses` | 限定作用的包范围 |
| `assignableTypes` | 限定作用的 Controller 类型 |
| `annotations` | 限定带有某注解的 Controller |

不写属性时，默认作用于所有 Controller。

**对应示例**

```java
@RestControllerAdvice
public class AllInOneAdvice {
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleIllegalState(IllegalStateException ex) {
        return Map.of("error", String.valueOf(ex.getMessage()));
    }
}
```

**底层机制**

`@ControllerAdvice` 类会被 `ExceptionHandlerExceptionResolver` 等组件识别，其中的 `@ExceptionHandler`、`@ModelAttribute`、`@InitBinder` 方法会应用到匹配的 Controller 上。

**注意事项**

- `@RestControllerAdvice` 中的方法返回值默认写入响应体；若用 `@ControllerAdvice`，需要额外 `@ResponseBody` 或返回 `ResponseEntity`；
- 多个 Advice 的异常处理顺序可通过 `@Order` 控制；
- 全局异常处理应返回统一错误结构，避免泄露堆栈；
- 不建议把业务逻辑放进 Advice，它只负责横切关注点。

---

## 15. `@CrossOrigin`

**来源与定位**

`org.springframework.web.bind.annotation.CrossOrigin`，可标注在**类**或**方法**上。

**核心作用**

声明该 Controller 或方法的跨域访问规则，Spring MVC 会在响应中添加相应的 CORS 头。

**关键属性**

| 属性 | 作用 |
|---|---|
| `origins` / `value` | 允许的来源，如 `"*"` 或具体域名 |
| `methods` | 允许的 HTTP 方法 |
| `allowedHeaders` | 允许的请求头 |
| `exposedHeaders` | 暴露给浏览器的响应头 |
| `allowCredentials` | 是否允许携带凭证 |
| `maxAge` | 预检结果缓存时间（秒） |

**对应示例**

```java
@RestController
@RequestMapping("/api/all-in-one")
@CrossOrigin(origins = "*")
public class AllInOne { ... }
```

类级别标注后，该类所有接口都应用该 CORS 配置；方法级别标注可覆盖类级别配置。

**底层机制**

`AbstractHandlerMapping` 会读取 `@CrossOrigin` 配置，生成 `CorsConfiguration`，在请求处理前后通过 `CorsProcessor` 写入 `Access-Control-Allow-*` 响应头，并处理 `OPTIONS` 预检请求。

**注意事项**

- `allowCredentials = true` 时，`origins` 不能为 `"*"`，必须指定具体来源；
- `@CrossOrigin` 只对 Spring MVC 处理的请求生效；如果请求被安全过滤器提前拦截，需要在 Spring Security 中额外配置 CORS；
- 生产环境不建议无脑 `origins = "*"`，应限制可信来源；
- 全局 CORS 可通过 `WebMvcConfigurer#addCorsMappings` 配置，`@CrossOrigin` 适合局部覆盖。

---

## 机制层面的补充：这些注解是如何被处理的

把上述注解串起来看，Spring MVC 的核心处理链是：

1. **`RequestMappingHandlerMapping`** 扫描 `@Controller`/`@RestController` 上的 `@RequestMapping` 及其派生注解，建立 URL 到 `HandlerMethod` 的映射；
2. 请求到达 `DispatcherServlet` 后，查找匹配的 `HandlerMethod`；
3. **`HandlerMethodArgumentResolver`** 链负责解析方法参数：
  - `@PathVariable` → `PathVariableMethodArgumentResolver`
  - `@RequestParam` → `RequestParamMethodArgumentResolver`
  - `@RequestBody` → `RequestResponseBodyMethodProcessor`
  - `@RequestHeader` → `RequestHeaderMethodArgumentResolver`
  - `@CookieValue` → `ServletCookieValueMethodArgumentResolver`
  - `@RequestPart` → `RequestPartMethodArgumentResolver`
  - `@ModelAttribute` → `ModelAttributeMethodProcessor`
4. 方法执行后，**`HandlerMethodReturnValueHandler`** 链负责写出响应：
  - `@ResponseBody`/`@RestController` → `RequestResponseBodyMethodProcessor`
  - `ResponseEntity` → `HttpEntityMethodProcessor`
  - `@ResponseStatus` → 状态码解析器参与设置状态
5. 异常发生时，**`HandlerExceptionResolver`** 链处理，其中 `ExceptionHandlerExceptionResolver` 负责 `@ExceptionHandler` 与 `@RestControllerAdvice`。

理解这条链路后，各个注解就不是孤立记忆点，而是分别挂在“映射—参数解析—返回值处理—异常处理—跨域”这几个扩展点上。
