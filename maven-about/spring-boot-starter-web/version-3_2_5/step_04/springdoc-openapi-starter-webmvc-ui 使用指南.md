`springdoc-openapi-starter-webmvc-ui` 是一个为 Spring Boot Web 应用**自动生成 OpenAPI 3 规范文档，并内置 Swagger UI 交互式界面的工具库**。

你无需手动编写大量文档，它会自动扫描你的 `@RestController`，根据代码结构生成结构化的 API 描述（JSON/YAML 格式），并提供一个可视化的网页供你浏览和测试接口。

### 📌 针对 Spring Boot 3.2.5 的版本选择

根据官方兼容性矩阵，**Spring Boot 3.2.x** 对应的 `springdoc-openapi` 版本范围是 **`2.3.x` 至 `2.5.x`**。在这个范围内，**推荐使用 `2.5.0` 版本**，它在兼容性范围内，并且有较多使用案例可供参考。

在 `pom.xml` 中添加依赖：

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.5.0</version>
</dependency>
```

### 🚀 代码详解：从零开始使用

添加上述依赖后，你**几乎不需要任何额外配置**，启动 Spring Boot 应用即可生效。

#### 1. 自动生成的访问入口

启动应用后，可以直接访问以下默认地址：

- **Swagger UI 交互界面**：`http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON 描述文件**：`http://localhost:8080/v3/api-docs`
- **OpenAPI YAML 描述文件**：`http://localhost:8080/v3/api-docs.yaml`

#### 2. 编写一个简单的 Controller

工具会自动扫描并文档化你的接口。例如，一个简单的用户管理 Controller：

```java
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping
    public List<String> getUsers() {
        return List.of("Alice", "Bob");
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable Long id) {
        return "User " + id;
    }

    @PostMapping
    public String createUser(@RequestBody String user) {
        return "Created: " + user;
    }
}
```

完成上述步骤并启动应用后，Swagger UI 页面里就会出现 `/users` 相关的三个接口，你可以直接点击 "Try it out" 进行在线测试。

#### 3. 使用注解增强文档可读性

虽然零配置可用，但通过 Swagger 注解（来自 `io.swagger.v3.oas.annotations` 包）可以显著提升文档质量。

**在 Controller 类上**，使用 `@Tag` 进行分组和描述：

```java
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "用户管理", description = "用户的增删改查接口")
@RestController
@RequestMapping("/users")
public class UserController {
    // ...
}
```

**在方法上**，使用 `@Operation` 描述接口用途，使用 `@Parameter` 描述参数：

```java
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Operation(summary = "根据ID获取用户", description = "返回指定ID的用户详细信息")
@GetMapping("/{id}")
public String getUserById(
        @Parameter(description = "用户ID", example = "1")
        @PathVariable Long id) {
    return "User " + id;
}
```

### ⚙️ 常用配置项

你可以在 `application.properties` 或 `application.yml` 中调整默认行为：

```properties
# application.properties
# 自定义 Swagger UI 的访问路径
springdoc.swagger-ui.path=/docs

# 自定义 OpenAPI 描述文件的路径
springdoc.api-docs.path=/api-docs

# 按字母顺序排列标签（分组）
springdoc.swagger-ui.tagsSorter=alpha

# 按HTTP方法（GET, POST等）排序操作
springdoc.swagger-ui.operationsSorter=method

# 仅扫描指定的包，便于大型项目聚焦
springdoc.packages-to-scan=com.yourcompany.controller
```

总的来说，`springdoc-openapi-starter-webmvc-ui` 的核心价值在于**自动化**。你只需专注编写业务逻辑，它负责将你的代码转化为一份实时、可交互的 API 文档。
