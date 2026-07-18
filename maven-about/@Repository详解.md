# @Repository 注解详解

## 1. 来源

`@Repository` 是 **Spring Framework** 提供的注解，属于 `org.springframework.stereotype` 包。

> 自 Spring 2.0 版本引入，是 Spring 的 stereotype 注解之一

---

## 2. 版本历史

| 版本 | 说明 |
|------|------|
| **2.0** | 引入 `@Repository`，仅作为持久层组件的标记 |
| **2.5** | 作为 `@Component` 的特化，支持类路径扫描自动检测 |
| **6.0+** | 支持 Jakarta EE（`jakarta.persistence.*`） |
| **7.0+** | 最新稳定版，属性保持兼容 |

---

## 3. 注解定义与默认值

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Repository {

    /**
     * Bean 名称的别名（等同于 value）
     */
    @AliasFor(annotation = Component.class)
    String value() default "";
}
```

### 关键默认值

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `value` | `""` | 空字符串；为空时由 Spring 自动生成 Bean 名称（类名首字母小写） |

---

## 4. 核心功能

`@Repository` 提供以下三大核心功能：

### 4.1 组件扫描自动注册

作为 `@Component` 的特化，标注了 `@Repository` 的类会被 Spring 自动扫描并注册为 Bean。

```java
@Repository
public class UserRepository {
    // Spring 会自动创建该类的 Bean 实例
}
```

### 4.2 异常翻译（Exception Translation）

这是 `@Repository` 最重要的功能。配合 `PersistenceExceptionTranslationPostProcessor`，可以将持久层特定的异常（如 JDBC、JPA、Hibernate 异常）转换为 Spring 的非受检异常体系（`DataAccessException`）。

```java
// 无需捕获 SQLException，Spring 会自动转换为 DataAccessException
@Repository
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public User findById(Long id) {
        // 如果抛出 SQLException，会被转换为 DataAccessException
        return jdbcTemplate.queryForObject(
            "SELECT * FROM users WHERE id = ?", 
            userRowMapper, 
            id
        );
    }
}
```

### 4.3 语义明确

在分层架构中，`@Repository` 明确标识该类属于数据访问层，便于：
- 工具识别和代码分析
- AOP 切面的精准匹配
- 团队协作时的代码理解

---

## 5. 使用示例

### 5.1 MyBatis Mapper 接口

```java
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {
    // MyBatis-Plus 自动提供 CRUD 方法
}
```

**配套配置：**
```java
@SpringBootApplication
@MapperScan("com.rc.system.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 5.2 Spring Data JPA Repository

```java
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // 方法名查询
    List<User> findByName(String name);
    
    // JPQL 查询
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);
    
    // 原生 SQL 查询
    @Query(value = "SELECT * FROM users WHERE name LIKE %:keyword%", 
           nativeQuery = true)
    List<User> searchByKeyword(@Param("keyword") String keyword);
}
```

### 5.3 传统 DAO 类（JdbcTemplate）

```java
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@Repository
public class UserRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        return user;
    };
    
    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, userRowMapper, id);
    }
    
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, userRowMapper);
    }
}
```

### 5.4 Hibernate/JPA 实体管理器

```java
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class UserRepository {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }
    
    public User save(User user) {
        if (user.getId() == null) {
            entityManager.persist(user);
            return user;
        } else {
            return entityManager.merge(user);
        }
    }
}
```

---

## 6. @Repository vs 其他 stereotype 注解对比

| 注解 | 层次 | 主要功能 | 典型用途 |
|------|------|----------|----------|
| `@Component` | 通用 | 通用组件标记 | 无法明确分类的组件 |
| `@Repository` | 持久层 | 异常翻译 + 组件标记 | DAO、数据访问类 |
| `@Service` | 业务层 | 业务逻辑标记 | Service 类 |
| `@Controller` | 表现层 | 请求处理标记 | Spring MVC Controller |

**选择建议：**
- 数据访问层：使用 `@Repository`（获得异常翻译）
- 业务逻辑层：使用 `@Service`
- 控制器层：使用 `@Controller` 或 `@RestController`
- 通用组件：使用 `@Component`

---

## 7. 异常翻译机制详解

### 7.1 工作原理

```
PersistenceExceptionTranslationPostProcessor
        ↓
拦截所有 @Repository 标注的 Bean
        ↓
包装为 Proxy
        ↓
捕获 PersistenceException
        ↓
转换为 DataAccessException（非受检异常）
```

### 7.2 启用异常翻译

#### 7.2.1 Spring Boot 自动配置（推荐）

在 Spring Boot 项目中，异常翻译功能**无需手动配置**，只需引入相应的 starter 依赖即可自动启用。

**依赖选择：**

| 场景 | 推荐依赖 | 说明 |
|------|----------|------|
| JPA/Hibernate 项目 | `spring-boot-starter-data-jpa` | 包含 JPA 相关依赖和自动配置 |
| JDBC/MyBatis 项目 | `spring-boot-starter-jdbc` | 包含 JDBC 相关依赖和自动配置 |
| 两者都需要 | 同时引入 | 不会冲突，自动配置会智能识别 |

**Maven 示例：**
```xml
<!-- 方式1：JPA 项目 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- 方式2：JDBC 项目 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>

<!-- 方式3：同时引入（不推荐，选择一个即可） -->
<!-- Spring Boot 会自动检测并配置，不会重复注册 -->
```

**自动配置原理：**

Spring Boot 通过以下机制自动启用异常翻译：

1. **依赖检测**：当 classpath 中存在 `spring-tx` 和 `spring-jdbc` 或 `spring-data-jpa` 时，自动配置生效
2. **条件注解**：自动配置类使用 `@ConditionalOnClass`、`@ConditionalOnBean` 等条件注解
3. **自动注册**：`PersistenceExceptionTranslationPostProcessor` 被自动注册为 Bean
4. **代理创建**：为所有 `@Repository` 标注的 Bean 创建代理，拦截持久层异常

**相关自动配置类：**
- `org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration`
- `org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration`
- `org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration`

**验证自动配置是否生效：**

```java
// 方法1：查看启动日志
// 启动时会打印类似日志：
// PersistenceExceptionTranslationPostProcessor defined in class path resource
// [org/springframework/boot/autoconfigure/transaction/TransactionAutoConfiguration.class]

// 方法2：检查 Bean 是否存在
@Autowired
private ApplicationContext context;

public void checkExceptionTranslation() {
    boolean exists = context.containsBean(
        "org.springframework.transaction.config.internalPersistenceExceptionTranslationPostProcessor"
    );
    System.out.println("异常翻译后置处理器是否注册: " + exists);
}

// 方法3：查看自动配置报告
// 启动时添加 --debug 参数
// java -jar your-app.jar --debug
// 在控制台搜索 "PersistenceExceptionTranslation"
```

#### 7.2.2 手动配置（非 Spring Boot）

在传统 Spring 项目（非 Spring Boot）中，需要手动注册 `PersistenceExceptionTranslationPostProcessor`。

**Java 配置方式（推荐）：**
```java
@Configuration
public class DataConfig {
    
    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
```

**XML 配置方式：**
```xml
<!-- spring-tx 提供了该后置处理器 -->
<bean class="org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor"/>
```

**注解驱动方式（Spring 3.1+）：**
```java
@Configuration
@EnableTransactionManagement  // 启用事务管理
public class DataConfig {
    // @EnableTransactionManagement 会间接启用异常翻译
}
```

#### 7.2.3 自动配置的条件

自动配置生效需要满足以下条件：

1. **依赖条件**：
   - `spring-tx` 模块（提供 `PersistenceExceptionTranslationPostProcessor`）
   - `spring-jdbc` 或 `spring-data-jpa` 模块（提供持久层支持）

2. **Bean 条件**：
   - 存在 `DataSource` Bean（JDBC 场景）
   - 或存在 `EntityManagerFactory` Bean（JPA 场景）

3. **排除条件**：
   - 未设置 `spring.dao.exceptiontranslation.enabled=false`（Spring Boot 2.5+）

**禁用自动配置（如需）：**
```yaml
# application.yml
spring:
  dao:
    exceptiontranslation:
      enabled: false  # 禁用异常翻译（不推荐）
```

### 7.3 异常转换示例

#### 7.3.1 常见异常类型映射

| 原始异常（JDBC/JPA/Hibernate） | Spring DataAccessException 子类 | 说明 |
|-------------------------------|--------------------------------|------|
| `SQLException` | `DataAccessException` | 基类，所有持久层异常的父类 |
| `DuplicateKeyException` | `DuplicateKeyException` | 唯一键冲突 |
| `DataIntegrityViolationException` | `DataIntegrityViolationException` | 数据完整性约束违反 |
| `DataAccessException` | `DataAccessException` | 数据访问异常基类 |
| `EmptyResultDataAccessException` | `EmptyResultDataAccessException` | 查询结果为空 |
| `IncorrectResultSizeDataAccessException` | `IncorrectResultSizeDataAccessException` | 查询结果数量不正确 |
| `InvalidDataAccessApiUsageException` | `InvalidDataAccessApiUsageException` | 数据访问 API 使用错误 |
| `InvalidDataAccessResourceUsageException` | `InvalidDataAccessResourceUsageException` | 数据访问资源使用错误 |
| `PermissionDeniedDataAccessException` | `PermissionDeniedDataAccessException` | 权限不足 |
| `TransientDataAccessException` | `TransientDataAccessException` | 临时性数据访问异常 |
| `DataRetrievalFailureException` | `DataRetrievalFailureException` | 数据检索失败 |

#### 7.3.2 完整示例

```java
@Repository
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public void insertUser(User user) {
        try {
            jdbcTemplate.update(
                "INSERT INTO users (username, email) VALUES (?, ?)",
                user.getUsername(),
                user.getEmail()
            );
        } catch (DuplicateKeyException e) {
            // Spring 自动将 SQLException 转换为 DuplicateKeyException
            // 可以捕获特定异常进行处理
            throw new BusinessException("用户名已存在", e);
        } catch (DataIntegrityViolationException e) {
            // 数据完整性约束违反（如外键约束）
            throw new BusinessException("数据完整性错误", e);
        }
        // 其他异常会作为 DataAccessException 的子类抛出
    }
    
    public User findById(Long id) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE id = ?", 
                userRowMapper, 
                id
            );
        } catch (EmptyResultDataAccessException e) {
            // 查询结果为空
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            // 查询结果多于一条
            throw new BusinessException("查询结果不唯一", e);
        }
    }
}
```

#### 7.3.3 异常处理最佳实践

```java
@Repository
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    // 1. 捕获特定异常（推荐）
    public void insertUser(User user) {
        try {
            jdbcTemplate.update(
                "INSERT INTO users (username, email) VALUES (?, ?)",
                user.getUsername(),
                user.getEmail()
            );
        } catch (DuplicateKeyException e) {
            throw new BusinessException("用户名已存在", e);
        }
    }
    
    // 2. 使用 @ExceptionHandler 统一处理（在 Controller 层）
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(DataAccessException e) {
        // 统一处理持久层异常
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("数据访问错误: " + e.getMessage());
    }
    
    // 3. 使用 try-catch 包装为业务异常
    public void updateUser(User user) {
        try {
            jdbcTemplate.update(
                "UPDATE users SET email = ? WHERE id = ?",
                user.getEmail(),
                user.getId()
            );
        } catch (DataAccessException e) {
            // 记录日志
            logger.error("更新用户失败", e);
            // 抛出业务异常
            throw new BusinessException("更新用户失败", e);
        }
    }
}
```

---

## 8. 常见问题

### Q1: @Repository 不生效？

检查以下几点：
1. 确保类在 `@ComponentScan` 扫描路径下
2. 确保类没有被 `excludeFilters` 排除
3. 确保没有手动排除该类的 Bean 注册

```java
// 排除所有 @Repository
@ComponentScan(
    basePackages = "com.example",
    excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = Repository.class)
)
```

### Q2: MyBatis Mapper 需要同时加 @Repository 和 @Mapper 吗？

**不需要。** 两种方式二选一：
- 方式一：在 Mapper 接口上加 `@Mapper`（每个接口单独加）
- 方式二：在配置类上加 `@MapperScan`（推荐，一次配置即可）

如果使用了 `@MapperScan`，可以省略 `@Repository`，因为 MyBatis-Spring 会自动处理。

### Q3: @Repository 和 @Component 有什么区别？

- **功能上**：`@Repository` 是 `@Component` 的特化，额外提供异常翻译
- **语义上**：`@Repository` 明确表示数据访问层，`@Component` 是通用标记
- **建议**：在数据访问层始终使用 `@Repository`，而不是 `@Component`

### Q4: Spring Data JPA 的 @Repository 可以省略吗？

在 Spring Boot 中，继承 `JpaRepository` 的接口会自动被识别为 Repository，可以省略 `@Repository`。但显式添加可以提高代码可读性。

---

## 9. 最佳实践

1. **始终使用 `@Repository`**：在数据访问层使用 `@Repository` 而非 `@Component`
2. **配合异常翻译**：确保 `PersistenceExceptionTranslationPostProcessor` 已启用
3. **包结构清晰**：将 Repository 类放在 `repository` 或 `mapper` 包下
4. **命名规范**：接口命名为 `XxxRepository` 或 `XxxMapper`，实现类命名为 `XxxRepositoryImpl` 或 `XxxMapperImpl`

---

## 10. 常见依赖场景分析

### 10.1 spring-boot-starter-web 是否可以使用 @Repository？

**答案：可以使用，但只有组件标记功能，没有异常翻译功能。**

| 依赖组合 | @Repository 组件扫描 | 异常翻译 | 适用场景 |
|----------|---------------------|----------|----------|
| 只有 `spring-boot-starter-web` | ✅ 可以 | ❌ 不自动启用 | 不需要数据库的项目 |
| `spring-boot-starter-web` + `spring-boot-starter-jdbc` | ✅ 可以 | ✅ 自动启用 | JDBC/JdbcTemplate 项目 |
| `spring-boot-starter-web` + `spring-boot-starter-data-jpa` | ✅ 可以 | ✅ 自动启用 | JPA/Hibernate 项目 |
| `spring-boot-starter-web` + `mybatis-spring-boot-starter` | ✅ 可以 | ✅ 自动启用 | MyBatis 项目 |

**依赖传递关系：**
```
spring-boot-starter-web
    └── spring-boot-starter
        └── spring-context  ← 包含 @Repository 注解
```

**异常翻译依赖传递关系：**
```
spring-boot-starter-jdbc
    └── spring-jdbc
        └── spring-tx  ← 包含 PersistenceExceptionTranslationPostProcessor

spring-boot-starter-data-jpa
    └── spring-data-jpa
        └── spring-tx  ← 包含 PersistenceExceptionTranslationPostProcessor
```

### 10.2 验证示例

```java
// 场景1：只有 spring-boot-starter-web
// @Repository 可以使用，但只作为组件标记
@Repository
public class UserRepository {
    // 可以正常注册为 Bean
    // 但不会进行异常翻译（ SQLException 不会被转换）
}

// 场景2：spring-boot-starter-web + spring-boot-starter-jdbc
// @Repository 完整功能，包括异常翻译
@Repository
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public void insert(User user) {
        // SQLException 会被自动转换为 DataAccessException
        jdbcTemplate.update("INSERT INTO users ...", ...);
    }
}
```

### 10.3 项目结构示例

**只需要 Web 功能（不需要数据库）：**
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

**需要 Web + JDBC 数据库访问：**
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jdbc</artifactId>
    </dependency>
</dependencies>
```

**需要 Web + JPA 数据库访问：**
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
</dependencies>
```

---

## 11. 参考资料

- [Spring Framework @Repository API 文档](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Repository.html)
- [Spring DAO Support 文档](https://docs.spring.io/spring/reference/core/beans/classpath-scanning.html)
- [Spring Data JPA 官方文档](https://spring.io/projects/spring-data-jpa)
- [Spring Boot Starters 文档](https://docs.spring.io/spring-boot/reference/starter.html)
