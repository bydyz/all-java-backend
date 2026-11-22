# Spring Boot Web Starter 使用指南

## 概述

`spring-boot-starter-web` 是 Spring Boot 中用于构建 Web 应用程序的核心 starter。它包含了：

- **Spring MVC** - 基于 Servlet 的 Web 框架
- **内嵌 Tomcat** - 默认的 Web 容器
- **JSON 处理** - Jackson JSON 序列化/反序列化
- **数据验证** - Hibernate Validator
- **错误处理** - 默认错误页面

## 核心功能与常用 API

### 1. RESTful 控制器注解

| 注解 | 作用 | 示例 |
|------|------|------|
| `@RestController` | 标记 RESTful 控制器 | `@RestController` |
| `@RequestMapping` | 映射 HTTP 请求 | `@RequestMapping("/api")` |
| `@GetMapping` | 处理 GET 请求 | `@GetMapping("/users")` |
| `@PostMapping` | 处理 POST 请求 | `@PostMapping("/users")` |
| `@PutMapping` | 处理 PUT 请求 | `@PutMapping("/users/{id}")` |
| `@DeleteMapping` | 处理 DELETE 请求 | `@DeleteMapping("/users/{id}")` |

### 2. 请求参数绑定

| 注解 | 作用 | 位置 |
|------|------|------|
| `@PathVariable` | 路径变量 | URL 路径中 |
| `@RequestParam` | 查询参数 | URL 查询字符串 |
| `@RequestBody` | 请求体 | 请求体 JSON |
| `@RequestHeader` | 请求头 | HTTP 头 |
| `@CookieValue` | Cookie 值 | Cookie |

### 3. 响应处理

| 类/注解 | 作用 |
|---------|------|
| `ResponseEntity` | 完全控制 HTTP 响应 |
| `@ResponseStatus` | 指定响应状态码 |

---

## 代码示例

### 示例 1：基础 REST 控制器

```java
@RestController
@RequestMapping("/api/hello")
public class HelloController {
    
    @GetMapping
    public String sayHello() {
        return "Hello, Spring Boot!";
    }
}
```

### 示例 2：路径变量

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    // GET /api/users/123
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }
}
```

### 示例 3：查询参数

```java
@RestController
@RequestMapping("/api/search")
public class SearchController {
    
    // GET /api/search?keyword=spring&page=0
    @GetMapping
    public List<Item> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page) {
        return searchService.search(keyword, page);
    }
}
```

### 示例 4：请求体绑定（JSON）

```java
@RestController
@RequestMapping("/api/users")
public class UserApiController {
    
    // POST /api/users
    // Body: {"name": "John", "email": "john@example.com"}
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.create(user);
    }
}
```

### 示例 5：完整响应控制

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Product product = productService.findById(id);
        
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok()
                .header("X-Custom-Header", "value")
                .body(product);
    }
}
```

### 示例 6：自定义响应状态码

```java
@RestController
@RequestMapping("/api/items")
public class ItemController {
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)  // 返回 201 Created
    public Item createItem(@RequestBody Item item) {
        return itemService.create(item);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)  // 返回 204 No Content
    public void deleteItem(@PathVariable Long id) {
        itemService.delete(id);
    }
}
```

### 示例 7：复杂请求参数组合

```java
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    // GET /api/orders?status=pending&startDate=2024-01-01&endDate=2024-12-31
    @GetMapping
    public List<Order> getOrders(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        
        return orderService.findByFilters(status, startDate, endDate);
    }
}
```

### 示例 8：请求头和 Cookie

```java
@RestController
@RequestMapping("/api/info")
public class InfoController {
    
    @GetMapping
    public Map<String, String> getInfo(
            @RequestHeader("User-Agent") String userAgent,
            @CookieValue(value = "sessionId", defaultValue = "") String sessionId) {
        
        Map<String, String> info = new HashMap<>();
        info.put("userAgent", userAgent);
        info.put("sessionId", sessionId);
        return info;
    }
}
```

### 示例 9：文件上传

```java
@RestController
@RequestMapping("/api/upload")
public class FileUploadController {
    
    @PostMapping
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "请选择文件";
        }
        
        // 处理文件上传逻辑
        String fileName = file.getOriginalFilename();
        long fileSize = file.getSize();
        
        return "文件上传成功: " + fileName + " (" + fileSize + " bytes)";
    }
}
```

### 示例 10：全局异常处理

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(ResourceNotFoundException ex) {
        return new ErrorResponse("NOT_FOUND", ex.getMessage());
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(IllegalArgumentException ex) {
        return new ErrorResponse("BAD_REQUEST", ex.getMessage());
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneral(Exception ex) {
        return new ErrorResponse("INTERNAL_ERROR", "服务器内部错误");
    }
}
```

### 示例 11：数据验证

```java
@RestController
@RequestMapping("/api/users")
public class ValidatedUserController {
    
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.create(user));
    }
}

// User 类需要添加验证注解
public class User {
    @NotBlank(message = "用户名不能为空")
    private String name;
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @Min(value = 18, message = "年龄必须大于18")
    private int age;
}
```

### 示例 12：CORS 配置

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
```

---

## 常见应用场景

1. **RESTful API 服务** - 构建后端微服务
2. **Web 应用后端** - 为前端提供数据接口
3. **BFF (Backend For Frontend)** - 为特定前端定制接口
4. **内部服务通信** - 微服务间的 HTTP 调用

## 最佳实践

1. 使用 `@RestController` 而不是 `@Controller` + `@ResponseBody`
2. 优先使用 `ResponseEntity` 进行细粒度的响应控制
3. 对所有输入使用 `@Valid` 进行验证
4. 使用全局异常处理器统一错误响应格式
5. 合理使用 `@ResponseStatus` 标注操作结果
