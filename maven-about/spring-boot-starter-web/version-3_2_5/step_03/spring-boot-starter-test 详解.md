`spring-boot-starter-test` 是 Spring Boot 官方提供的**测试依赖启动器**。它的核心作用是将测试一个 Spring Boot 应用所需的几乎所有常用库“打包”在一起，你只需要引入这一个依赖，就能获得一套完整、兼容的测试工具链，无需再逐个寻找和解决 JUnit、Mockito 等库之间的版本兼容问题。

### 📦 它到底包含了什么？

引入 `spring-boot-starter-test` 后，你会自动获得以下主要测试库（作用域限定为 `test`）：

| 库 | 主要用途 |
| :--- | :--- |
| **JUnit 5 (Jupiter)** | Java 单元测试的行业标准框架。 |
| **Spring Test & Spring Boot Test** | 提供 `@SpringBootTest`、`@WebMvcTest` 等注解，以及 `MockMvc`、`TestRestTemplate` 等工具，用于 Spring 环境下的测试。 |
| **Mockito** | 用于创建“模拟对象（Mock）”，在单元测试中隔离外部依赖。 |
| **AssertJ** | 提供流式（fluent）断言，让测试代码的断言部分更易读。 |
| **Hamcrest** | 提供匹配器（Matcher）对象，常与 JUnit 配合用于更灵活的断言。 |
| **JSONassert & JsonPath** | 专门用于对 JSON 格式的响应内容进行断言和解析。 |

### 🔗 你的工程应该安装哪个版本？

**结论：直接使用 `3.2.5` 版本。**

由于你已使用 `spring-boot-starter-web 3.2.5`，Spring Boot 生态的最佳实践是**保持所有官方 Starter 版本一致**。混用不同版本（如 web 用 3.2.5，test 用 3.3.x）可能导致依赖冲突或自动配置异常。Maven 中央仓库也确实存在 `spring-boot-starter-test:3.2.5` 这个版本。

**最推荐的做法：不写版本号**。如果你使用了 Spring Boot 的 Parent POM 或 BOM（通常是 `spring-boot-starter-parent`），那么无需为 `spring-boot-starter-test` 指定 `<version>`，它会自动与你的 Spring Boot 版本（3.2.5）对齐。

```xml
<!-- 推荐方式：由 Spring Boot 父 POM 管理版本，自动为 3.2.5 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

如果你的工程没有使用 Spring Boot 的依赖管理，才需要显式指定版本：
```xml
<!-- 不推荐：需手动维护版本一致性 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <version>3.2.5</version>
    <scope>test</scope>
</dependency>
```
Gradle 的写法类似（通常也无需写版本）：
```groovy
dependencies {
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

---

### 💻 代码详解：如何使用它？

`spring-boot-starter-test` 支持多种粒度的测试，下面从单元测试、切片测试到集成测试，用代码展示其典型用法。

#### 1. 纯单元测试 (Unit Test) - 不启动 Spring

用于测试独立的类（如 Service、工具类），速度最快。利用 JUnit 5 和 Mockito。

```java
// 被测类示例
@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public String getUserName(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return user.getName().toUpperCase();
    }
}

// 测试类
@ExtendWith(MockitoExtension.class) // JUnit 5 的 Mockito 扩展
class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository; // 创建模拟的 Repository

    @InjectMocks
    private UserService userService; // 将被测 Service 注入 Mock

    @Test
    void getUserName_shouldReturnUpperCaseName() {
        // 1. 设定 Mock 行为 (Stubbing)
        User mockUser = new User(1L, "alice");
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // 2. 执行被测方法
        String result = userService.getUserName(1L);

        // 3. 使用 AssertJ 进行流式断言
        assertThat(result).isEqualTo("ALICE");
        // 4. 验证 Mock 交互
        verify(userRepository).findById(1L);
    }
}
```
此测试完全在内存中运行，`userRepository` 是一个模拟对象，不会触及真实数据库。

#### 2. Web 层切片测试 (Slice Test) - 只启动 Web 层

使用 `@WebMvcTest`，只加载 Controller 相关的 Spring 组件，不启动完整的应用上下文和数据库，速度较快。

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    // constructor...
    @GetMapping("/{id}/name")
    public String getUserName(@PathVariable Long id) {
        return userService.getUserName(id);
    }
}

// 测试类
@WebMvcTest(UserController.class) // 只加载 Web 层，不加载 Service/Repository
class UserControllerSliceTest {

    @Autowired
    private MockMvc mockMvc; // 用于模拟 HTTP 请求

    @MockBean // 将 Controller 依赖的 Service 替换为 Mock Bean
    private UserService userService;

    @Test
    void getUserName_shouldReturnNameFromService() throws Exception {
        // 设定 Mock Service 的返回值
        when(userService.getUserName(1L)).thenReturn("ALICE");

        // 执行 GET 请求并进行断言
        mockMvc.perform(get("/api/users/1/name"))
                .andExpect(status().isOk()) // 断言状态码 200
                .andExpect(content().string("ALICE")); // 断言响应体

        verify(userService).getUserName(1L);
    }
}
```
`@MockBean` 会用 Mockito 的模拟对象替换 Spring 上下文中的真实 Bean，让你可以专注于测试 Controller 的路由、参数绑定和响应序列化等行为。

#### 3. 集成测试 (Integration Test) - 启动完整应用

使用 `@SpringBootTest`，加载完整的应用程序上下文，进行端到端的验证。

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc // 在完整上下文中配置 MockMvc
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // 也可以使用 TestRestTemplate

    @Autowired
    private UserRepository userRepository; // 可以注入真实的 Repository

    @Test
    @Transactional // 测试后自动回滚，不污染数据库
    void createAndRetrieveUser_shouldWorkEndToEnd() throws Exception {
        // 通过真实 Repository 准备数据（或通过 API 创建）
        userRepository.save(new User(null, "bob"));

        // 通过 HTTP 请求验证完整链路
        mockMvc.perform(get("/api/users/1/name"))
                .andExpect(status().isOk())
                .andExpect(content().string("BOB"));
    }
}
```
`webEnvironment = RANDOM_PORT` 会让应用在随机端口启动，提供更接近真实的 Servlet 环境。`@Transactional` 确保测试方法结束后，数据库操作被回滚，保持测试的独立性。

---

总结一下，对于你的 3.2.5 工程，引入 `spring-boot-starter-test` 依赖（无需指定版本号）即可获得全套测试工具。它支持你从快速的纯单元测试，到轻量的 Web 层切片测试，再到完整的集成测试，覆盖应用测试的各个层次。
