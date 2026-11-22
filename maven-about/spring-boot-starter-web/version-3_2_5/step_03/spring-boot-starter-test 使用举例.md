以下是基于你提供的依赖（`spring-boot-starter-web` + `spring-boot-starter-test`，版本 3.2.5）的完整示例，包含一个 Controller 以及对 `spring-boot-starter-test` 的详细使用说明。

---

## 📦 一个简单的 Controller

```java
package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/greeting")
public class GreetingController {

    @GetMapping
    public Map<String, String> greet(@RequestParam(defaultValue = "World") String name) {
        return Map.of("message", "Hello, " + name + "!");
    }

    @PostMapping
    public Map<String, String> echo(@RequestBody Map<String, String> body) {
        return Map.of("received", body.getOrDefault("content", ""));
    }
}
```

这个 Controller 提供了两个端点：`GET /api/greeting` 接收查询参数 `name`，`POST /api/greeting` 接收 JSON 请求体。下面用它来演示 `spring-boot-starter-test` 的实际用法。


## 🧪 `spring-boot-starter-test` 到底包含什么？

`spring-boot-starter-test` 是一个聚合 starter，它在 `test` scope 下自动引入了以下核心测试库：

| 库 | 作用 | 你在 Controller 测试中的角色 |
|---|---|---|
| **JUnit 5 (Jupiter)** | 测试运行框架，负责执行测试、管理生命周期 | 提供 `@Test`、`@BeforeEach` 等注解 |
| **Spring Test & Spring Boot Test** | Spring 官方测试支持，提供 `MockMvc`、测试上下文、`@WebMvcTest`、`@SpringBootTest` 等 | 让你可以在不启动完整应用的情况下模拟 HTTP 请求 |
| **AssertJ** | 流式断言库，`assertThat(x).isEqualTo(y)` | 对响应状态、JSON 字段做可读性极强的断言 |
| **Mockito** | Mocking 框架，创建“假对象”替代真实依赖 | 用 `@MockBean` 隔离 Service 层，让测试只关注 Controller 逻辑 |
| **Hamcrest** | Matcher 库，提供 `equalTo`、`hasSize` 等匹配器 | 可与 `MockMvc` 的 `jsonPath` 配合做复杂断言 |
| **JSONassert / JsonPath** | JSON 断言与路径提取 | `jsonPath("$.message")` 直接定位响应 JSON 中的字段 |

简言之，这个 starter 让你**只加一行依赖，就同时拥有“运行测试的框架 + 模拟 HTTP 的工具 + 隔离依赖的 Mock 工具 + 断言库”**。


## 🎯 核心使用方式一：`@WebMvcTest`（切片测试，推荐用于 Controller）

`@WebMvcTest` 只加载 Web 层（Controller、Jackson、`MockMvc` 等），**不会启动完整的 Spring 上下文，也不会加载 Service、Repository**，因此启动极快，非常适合 Controller 的单元测试。未被加载的依赖（如 Service）需要用 `@MockBean` 注入 Mock 对象。

### 📝 测试代码

```java
package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GreetingController.class)          // 只加载这一个 Controller
class GreetingControllerTest {

    @Autowired
    private MockMvc mockMvc;                   // 模拟 HTTP 请求的核心工具

    // 如果 Controller 依赖了 Service，在这里用 @MockBean 替换
    // @MockBean
    // private SomeService someService;

    @Test
    void greet_shouldReturnDefaultMessage_whenNoNameParam() throws Exception {
        mockMvc.perform(get("/api/greeting"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, World!"));
    }

    @Test
    void greet_shouldReturnCustomMessage_whenNameProvided() throws Exception {
        mockMvc.perform(get("/api/greeting").param("name", "Alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, Alice!"));
    }

    @Test
    void echo_shouldReturnReceivedContent() throws Exception {
        String jsonBody = """
                {"content": "test payload"}
                """;

        mockMvc.perform(post("/api/greeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.received").value("test payload"));
    }
}
```

### 🔍 逐项解释

**`@WebMvcTest(GreetingController.class)`**
只实例化指定的 Controller 以及 Web 相关基础设施，不会扫描 `@Service`、`@Repository` 等其他 Bean。这是它比 `@SpringBootTest` 快得多的根本原因。

**`@Autowired MockMvc`**
`MockMvc` 是 Spring Test 提供的“假 HTTP 客户端”。你调用 `perform(...)` 时，请求并不会真正走网络栈，而是直接进入 DispatcherServlet，因此不需要启动 Tomcat，也不需要 `RANDOM_PORT`。

**`get("/api/greeting").param("name", "Alice")`**
`param()` 对应 URL 上的 `?name=Alice`。这是测试 `@RequestParam` 的标准方式。

**`post(...).contentType(APPLICATION_JSON).content(jsonBody)`**
`contentType` 设置 `Content-Type` 头，`content` 设置请求体。这是测试 `@RequestBody` 的标准方式。

**`jsonPath("$.message").value("Hello, World!")`**
`jsonPath` 来自 `spring-boot-starter-test` 中包含的 **JsonPath** 库。`$` 代表根节点，`.message` 定位字段，`value()` 断言其值。对于数组或嵌套对象，还可以使用 `jsonPath("$.items[0].name")` 或 `jsonPath("$", hasSize(2))`（配合 Hamcrest）。

**`status().isOk()`**
断言 HTTP 状态码为 200。类似的还有 `isCreated()`、`isNotFound()`、`isBadRequest()`。

**`@MockBean` 的角色**
如果你的 Controller 注入了 Service，`@WebMvcTest` 不会自动提供它，必须用 `@MockBean` 创建一个 Mock 并注入。你可以用 `when(service.method(...)).thenReturn(...)` 控制其行为，用 `verify(service).method(...)` 验证调用。


## 🌐 核心使用方式二：`@SpringBootTest`（集成测试，验证完整链路）

当你需要验证 Controller 与真实的 Service、数据库、安全配置等全部协同工作时，使用 `@SpringBootTest`。它会启动**完整的 Spring 应用上下文**，是集成测试的标准方式。

### 📝 测试代码（随机端口 + `TestRestTemplate`）

```java
package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GreetingIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void greet_shouldReturnHelloWorld() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/greeting",
                String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Hello, World!");
    }
}
```

### 🔍 与 `@WebMvcTest` 的关键区别

| | `@WebMvcTest` | `@SpringBootTest` |
|---|---|---|
| 加载范围 | 仅 Web 层 | 完整应用上下文 |
| 启动速度 | 快（毫秒级～秒级） | 慢（秒级～十秒级） |
| HTTP 模拟方式 | `MockMvc`（不启动服务器） | `TestRestTemplate` / `WebTestClient`（真实 HTTP） |
| 依赖处理 | 用 `@MockBean` 隔离 | 使用真实 Bean |
| 适用场景 | Controller 逻辑的单元测试 | 端到端集成测试 |

`webEnvironment = RANDOM_PORT` 会让 Spring Boot 启动一个内嵌服务器并随机分配端口，`@LocalServerPort` 将端口注入测试类。`TestRestTemplate` 是 `spring-boot-starter-test` 提供的、专为测试设计的 REST 客户端。断言部分使用 **AssertJ** 的 `assertThat(...)`，这是 starter 中包含的流式断言库，可读性优于 JUnit 原生的 `assertEquals`。


## ⚠️ 一个容易忽略的细节

在 `@WebMvcTest` 中，如果 Controller 被 `@RestController` 标注，Spring Boot 会自动配置 Jackson 并启用 JSON 序列化/反序列化，无需额外配置。但如果响应中包含自定义类型或日期格式，可能需要 `@JsonComponent` 或在测试中通过 `@Import` 引入相关配置类。

另外，`@MockBean` 在 Spring Boot 3.4+ 中已被标记为 deprecated，建议迁移到 `@MockitoBean`。你使用的是 3.2.5，`@MockBean` 仍然完全可用，但写新代码时可以留意这一趋势。


## 💎 总结

`spring-boot-starter-test` 的设计哲学是 **“一站式测试工具箱”** ：你不需要逐一手动引入 JUnit、Mockito、AssertJ、JsonPath，一行依赖就覆盖了从**测试运行（JUnit 5）** 到**HTTP 模拟（MockMvc / TestRestTemplate）** 、从**依赖隔离（Mockito `@MockBean`）** 到**断言（AssertJ / JsonPath / Hamcrest）** 的完整链路。对于 Controller 测试，**优先使用 `@WebMvcTest` + `MockMvc`** 做快速、聚焦的切片测试；只有在需要验证跨层协作时，才动用 `@SpringBootTest` 启动完整上下文。
