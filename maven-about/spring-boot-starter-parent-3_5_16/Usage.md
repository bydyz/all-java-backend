# Spring Boot Starter Parent 3.5.16 使用指南

## 概述

`spring-boot-starter-parent` 是 Spring Boot 项目的核心父 POM，提供依赖管理、默认配置、打包支持等功能。继承该 POM 后，开发者无需手动管理版本号，也无需配置繁琐的插件参数。

---

## 一、核心作用

### 1. 依赖版本管理

继承 `spring-boot-starter-parent` 后，所有 Spring Boot 相关依赖的版本会自动管理，无需显式指定。

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.16</version>
</parent>

<dependencies>
    <!-- 无需指定版本号，由父 POM 管理 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

**常用依赖（无需指定版本）：**
- `spring-boot-starter-web` - Web 开发（内嵌 Tomcat）
- `spring-boot-starter-data-jpa` - JPA 数据访问
- `spring-boot-starter-data-redis` - Redis 缓存
- `spring-boot-starter-security` - 安全框架
- `spring-boot-starter-test` - 测试支持
- `spring-boot-starter-actuator` - 监控端点

---

### 2. 资源过滤（Maven Filtering）

支持在配置文件中使用 `@..@` 占位符，Maven 会自动替换。

**pom.xml 配置：**
```xml
<properties>
    <java.version>17</java.version>
    <app.version>@project.version@</app.version>
</properties>
```

**application.yml：**
```yaml
app:
  name: @project.artifactId@
  version: @project.version@
  java-version: @java.version@
```

**常用占位符：**
- `@project.version@` - 项目版本
- `@project.artifactId@` - 项目名称
- `@java.version@` - Java 版本

---

### 3. 默认配置

提供合理的默认值：
- Java 编译版本：17
- 源文件编码：UTF-8
- 输出编码：UTF-8
- 资源过滤分隔符：`@..@`

---

### 4. 打包配置

支持打包为可执行的 jar 或 war 文件。

**pom.xml 中启用打包插件：**
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

**打包命令：**
```bash
mvn clean package          # 打包
mvn clean package -DskipTests  # 跳过测试打包
```

**打包后运行：**
```bash
java -jar target/your-app.jar
```

---

## 二、常用 API 说明

### 1. Spring Boot 应用入口

```java
@SpringBootApplication  // 组合注解：@Configuration + @EnableAutoConfiguration + @ComponentScan
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);  // 启动 Spring Boot 应用
    }
}
```

**`@SpringBootApplication` 包含：**
- `@Configuration` - 标记为配置类
- `@EnableAutoConfiguration` - 启用自动配置
- `@ComponentScan` - 组件扫描

---

### 2. REST Controller 示例

```java
@RestController                    // 标记为 REST 控制器
@RequestMapping("/api/users")       // 路由前缀
public class UserController {

    @GetMapping                     // GET 请求
    public List<User> getAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")            // GET /api/users/{id}
    public User getById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping                    // POST 请求
    @ResponseStatus(HttpStatus.CREATED)  // 返回 201 状态码
    public User create(@RequestBody User user) {
        return userService.save(user);
    }

    @PutMapping("/{id}")            // PUT 请求
    public User update(@PathVariable Long id, @RequestBody User user) {
        return userService.update(id, user);
    }

    @DeleteMapping("/{id}")         // DELETE 请求
    @ResponseStatus(HttpStatus.NO_CONTENT)  // 返回 204 状态码
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
```

**常用注解：**
- `@GetMapping` - 处理 GET 请求
- `@PostMapping` - 处理 POST 请求
- `@PutMapping` - 处理 PUT 请求
- `@DeleteMapping` - 处理 DELETE 请求
- `@PathVariable` - 路径参数绑定
- `@RequestParam` - 查询参数绑定
- `@RequestBody` - 请求体绑定

---

### 3. Service 层示例

```java
@Service                           // 标记为服务层组件
@Transactional                     // 类级别事务管理
public class UserService {

    @Autowired                     // 依赖注入
    private UserRepository userRepository;

    @Transactional(readOnly = true)  // 只读事务
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
```

**常用注解：**
- `@Service` - 服务层组件
- `@Autowired` - 依赖注入
- `@Transactional` - 事务管理

---

### 4. Repository 数据访问

```java
@Repository                        // 标记为数据访问层组件
public interface UserRepository extends JpaRepository<User, Long> {
    
    // 方法名查询
    List<User> findByLastName(String lastName);
    
    // JPQL 查询
    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(@Param("email") String email);
    
    // 原生 SQL 查询
    @Query(value = "SELECT * FROM users WHERE status = :status", nativeQuery = true)
    List<User> findByStatus(@Param("status") String status);
}
```

---

## 三、配置文件示例

### application.yml

```yaml
server:
  port: 8080                    # 服务端口
  servlet:
    context-path: /api          # 上下文路径

spring:
  application:
    name: my-app               # 应用名称
  
  # 数据源配置
  datasource:
    url: jdbc:mysql://localhost:3306/mydb
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  # JPA 配置
  jpa:
    hibernate:
      ddl-auto: update         # 自动更新表结构
    show-sql: true             # 显示 SQL
  
  # Redis 配置
  data:
    redis:
      host: localhost
      port: 6379

# Actuator 监控端点
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics  # 暴露的端点
  endpoint:
    health:
      show-details: when-authorized   # 健康检查详情
```

---

## 四、测试类示例

```java
@SpringBootTest                        // Spring Boot 测试支持
class ApplicationTests {

    @Autowired
    private ApplicationContext context;  // 注入 Spring 上下文

    @Test
    void contextLoads() {
        // 验证 Spring 上下文正常加载
        assertNotNull(context);
    }

    @Test
    void testBeanExists() {
        // 验证 Bean 存在
        assertTrue(context.containsBean("userRepository"));
    }
}
```

**常用测试注解：**
- `@SpringBootTest` - 完整 Spring Boot 测试
- `@WebMvcTest` - Web 层单元测试
- `@DataJpaTest` - JPA 层测试
- `@MockBean` - Mock Bean

---

## 五、项目结构建议

```
src/
├── main/
│   ├── java/
│   │   └── com/example/app/
│   │       ├── Application.java          # 启动类
│   │       ├── controller/               # 控制器层
│   │       │   └── UserController.java
│   │       ├── service/                  # 服务层
│   │       │   └── UserService.java
│   │       ├── repository/               # 数据访问层
│   │       │   └── UserRepository.java
│   │       ├── entity/                   # 实体类
│   │       │   └── User.java
│   │       └── config/                   # 配置类
│   │           └── AppConfig.java
│   └── resources/
│       ├── application.yml               # 主配置文件
│       ├── application-dev.yml           # 开发环境
│       └── application-prod.yml          # 生产环境
└── test/
    └── java/
        └── com/example/app/
            └── ApplicationTests.java
```

---

## 六、常用 Maven 命令

```bash
# 运行应用
mvn spring-boot:run

# 打包
mvn clean package

# 打包并跳过测试
mvn clean package -DskipTests

# 运行测试
mvn test

# 打包为可执行 jar 后运行
java -jar target/your-app.jar

# 指定配置文件运行
java -jar target/your-app.jar --spring.profiles.active=prod
```

---

## 七、常见问题

### Q: 如何覆盖 Spring Boot 默认的依赖版本？

```xml
<properties>
    <!-- 覆盖特定依赖版本 -->
    <jackson.version>2.15.0</jackson.version>
</properties>
```

### Q: 如何禁用自动配置？

```java
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
```

### Q: 如何配置多环境？

使用 `spring.profiles.active` 属性指定激活的环境：
```bash
java -jar app.jar --spring.profiles.active=dev
```

---

## 参考资料

- [Spring Boot 官方文档](https://docs.spring.io/spring-boot/docs/3.5.16/reference/html/)
- [Spring Boot Starter Parent](https://docs.spring.io/spring-boot/docs/3.5.16/reference/htmlsingle/#using.build-systems.parent-pom)
