# Java后端分层架构详解

> 本文档详细解释 Java 后端项目中 Controller、Service、Mapper、DTO 层各自的含义、作用及代码示例。

---

## 目录

- [1. 为什么需要分层架构](#1-为什么需要分层架构)
- [2. Controller 层（接口层）](#2-controller-层接口层)
- [3. Service 层（业务逻辑层）](#3-service-层业务逻辑层)
- [4. Mapper 层（数据访问层）](#4-mapper-层数据访问层)
- [5. DTO / VO 层（数据传输对象）](#5-dto--vo-层数据传输对象)
- [6. 各层协作流程](#6-各层协作流程)
- [7. 本项目实际代码示例](#7-本项目实际代码示例)
- [8. 常见问题与最佳实践](#8-常见问题与最佳实践)

---

## 1. 为什么需要分层架构

分层架构（Layered Architecture）是 Java Web 项目最常用的设计模式，核心思想是**职责分离**：

```
┌─────────────────────────────────────┐
│        Controller（接口层）          │  ← 接收请求，返回响应
├─────────────────────────────────────┤
│        Service（业务逻辑层）         │  ← 处理业务逻辑
├─────────────────────────────────────┤
│        Mapper（数据访问层）          │  ← 操作数据库
├─────────────────────────────────────┤
│        Database（数据库）            │  ← 存储数据
└─────────────────────────────────────┘
```

**分层的好处**：
- **职责单一**：每层只关注自己的职责，代码更清晰
- **易于维护**：修改某一层不影响其他层
- **便于测试**：可以针对单层进行单元测试
- **代码复用**：Service 层可被多个 Controller 调用

---

## 2. Controller 层（接口层）

### 2.1 定义与作用

Controller 层是**接口层**，也称为表现层（Presentation Layer），负责：
- 接收前端（客户端）发送的 HTTP 请求
- 解析请求参数（路径参数、查询参数、请求体等）
- 调用 Service 层处理业务逻辑
- 将处理结果封装成 HTTP 响应返回给客户端

### 2.2 代码示例

```java
@RestController                          // = @Controller + @ResponseBody
@RequestMapping("/api/users")            // 路由前缀
@RequiredArgsConstructor                 // 构造函数注入（推荐）
public class UserController {

    private final UserService userService;

    // 查询所有用户
    @GetMapping                          // GET /api/users
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    // 根据ID查询用户
    @GetMapping("/{id}")                  // GET /api/users/1
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    // 创建用户
    @PostMapping                         // POST /api/users
    @ResponseStatus(HttpStatus.CREATED)  // 返回 201 状态码
    public User create(@Valid @RequestBody CreateUserRequest request) {
        return userService.create(request);
    }

    // 更新用户
    @PutMapping("/{id}")                  // PUT /api/users/1
    public User update(@PathVariable Long id, 
                       @Valid @RequestBody UpdateUserRequest request) {
        return userService.update(id, request);
    }

    // 删除用户
    @DeleteMapping("/{id}")               // DELETE /api/users/1
    @ResponseStatus(HttpStatus.NO_CONTENT) // 返回 204 状态码
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
```

### 2.3 常用注解

| 注解 | 作用 |
|------|------|
| `@RestController` | 标记为 RESTful 控制器，自动序列化返回值为 JSON |
| `@RequestMapping` | 映射 HTTP 请求路径 |
| `@GetMapping` | 映射 GET 请求 |
| `@PostMapping` | 映射 POST 请求 |
| `@PutMapping` | 映射 PUT 请求 |
| `@DeleteMapping` | 映射 DELETE 请求 |
| `@PathVariable` | 绑定路径参数（`/users/{id}`） |
| `@RequestBody` | 绑定请求体 JSON 数据 |
| `@RequestParam` | 绑定查询参数（`?keyword=xxx`） |
| `@Valid` | 启动参数校验 |

### 2.4 注意事项

- Controller 层**不写业务逻辑**，只做参数接收和响应封装
- Controller 调用 Service，Service 调用 Mapper，**禁止跨层调用**
- 返回值推荐使用 `ResponseEntity<T>` 控制 HTTP 状态码

---

## 3. Service 层（业务逻辑层）

### 3.1 定义与作用

Service 层是**业务逻辑层**，负责：
- 实现具体的业务逻辑（如用户注册、订单创建、权限校验等）
- 调用 Mapper 层进行数据库操作
- 管理事务（事务的开启、提交、回滚）
- 数据转换（Entity ↔ DTO）

### 3.2 接口 + 实现分离模式（推荐）

```java
// Service 接口
public interface UserService {
    List<User> findAll();
    User findById(Long id);
    User create(CreateUserRequest request);
    User update(Long id, UpdateUserRequest request);
    void delete(Long id);
}

// Service 实现
@Service                                  // 标记为 Spring Bean
@Transactional                           // 类级别事务管理
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)       // 只读事务优化
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));
    }

    @Override
    public User create(CreateUserRequest request) {
        // 业务校验：邮箱是否已存在
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("邮箱已被注册");
        }
        
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setStatus(UserStatus.ACTIVE);
        
        return userRepository.save(user);
    }

    @Override
    public User update(Long id, UpdateUserRequest request) {
        User user = findById(id);
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }
}
```

### 3.3 MyBatis-Plus 模式（本项目常用）

```java
// Service 接口 — 继承 IService<T> 自动获得 CRUD 方法
public interface UserService extends IService<User> {
    User getByUsername(String username);
    boolean updateStatusBatch(List<Long> ids, Integer status);
}

// Service 实现 — 继承 ServiceImpl<Mapper, Entity>
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> 
        implements UserService {

    @Override
    public User getByUsername(String username) {
        // 使用 LambdaQueryWrapper 构建查询条件
        return lambdaQuery()
                .eq(User::getUsername, username)
                .one();
    }

    @Override
    public boolean updateStatusBatch(List<Long> ids, Integer status) {
        // 使用 LambdaUpdateWrapper 构建更新条件
        return lambdaUpdate()
                .in(User::getId, ids)
                .set(User::getStatus, status)
                .update();
    }
}
```

### 3.4 常用注解

| 注解 | 作用 |
|------|------|
| `@Service` | 标记为服务层组件 |
| `@Transactional` | 声明式事务管理 |
| `@Transactional(readOnly = true)` | 只读事务优化（不加写锁） |

### 3.5 注意事项

- Service 层是**业务逻辑的核心**，所有业务规则都在这里实现
- 事务注解 `@Transactional` 应加在 Service 层，而不是 Controller 层
- Service 层只调用 Mapper，不直接操作数据库连接

---

## 4. Mapper 层（数据访问层）

### 4.1 定义与作用

Mapper 层（也称 DAO 层、Repository 层）是**数据访问层**，负责：
- 与数据库进行交互（增删改查 CRUD）
- 封装 SQL 操作，屏蔽底层数据库细节
- 将数据库记录映射为 Java 对象（ORM）

### 4.2 MyBatis-Plus Mapper（本项目主流）

```java
@Mapper                                    // 标记为 MyBatis Mapper
public interface UserMapper extends BaseMapper<User> {
    // 继承 BaseMapper 后自动获得以下方法，无需编写 XML：
    // - insert(T entity)           插入
    // - deleteById(Serializable id) 删除
    // - updateById(T entity)       更新
    // - selectById(Serializable id) 查询单个
    // - selectList(Wrapper)        查询列表
    // - selectPage(Page, Wrapper)  分页查询
    
    // 自定义复杂查询（需要编写 XML）
    @Select("SELECT * FROM user WHERE username = #{username}")
    User selectByUsername(@Param("username") String username);
}
```

### 4.3 Spring Data JPA Repository

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 方法名自动派生查询（无需写 SQL）
    List<User> findByName(String name);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    
    // @Query 自定义查询
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmailQuery(@Param("email") String email);
    
    // 原生 SQL
    @Query(value = "SELECT * FROM users WHERE name LIKE %:keyword%", 
           nativeQuery = true)
    List<User> searchByKeyword(@Param("keyword") String keyword);
}
```

### 4.4 MyBatis XML Mapper

```xml
<!-- UserMapper.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.mapper.UserMapper">
    
    <!-- 自定义查询 -->
    <select id="selectByUsername" resultType="User">
        SELECT * FROM user WHERE username = #{username}
    </select>
    
    <!-- 复杂关联查询 -->
    <select id="selectWithOrders" resultType="UserVO">
        SELECT u.*, o.id as order_id, o.amount
        FROM user u
        LEFT JOIN orders o ON u.id = o.user_id
        WHERE u.id = #{id}
    </select>
</mapper>
```

### 4.5 常用注解

| 注解 | 作用 |
|------|------|
| `@Mapper` | 标记为 MyBatis Mapper 接口 |
| `@Repository` | 标记为数据访问组件（通用） |
| `@Select` / `@Insert` / `@Update` / `@Delete` | 注解方式编写 SQL |

### 4.6 注意事项

- Mapper 层只做**数据访问**，不包含业务逻辑
- 简单 CRUD 使用框架内置方法，复杂查询再写自定义 SQL
- 启动类需要添加 `@MapperScan("com.xxx.mapper")` 扫描 Mapper 接口

---

## 5. DTO / VO 层（数据传输对象）

### 5.1 定义与作用

DTO（Data Transfer Object）是**数据传输对象**，用于在各层之间传递数据：
- **Entity（实体类）**：对应数据库表结构，包含所有字段
- **DTO（数据传输对象）**：用于接收前端请求参数，可能只包含部分字段
- **VO（视图对象）**：用于返回给前端的数据，可能包含额外信息

### 5.2 为什么需要 DTO/VO

假设数据库 User 表有 20 个字段，但创建用户时只需要 3 个字段：

```java
// ❌ 不好的做法：直接用 Entity 接收请求
@PostMapping
public User create(@RequestBody User user) {
    // 问题：用户可以传入 id、createTime、status 等不该由前端设置的字段
}

// ✅ 好的做法：用 DTO 接收请求
@PostMapping
public User create(@RequestBody CreateUserRequest request) {
    // 只接收需要的字段：name, email, password
}
```

### 5.3 代码示例

```java
// ========== 请求 DTO（接收前端参数）==========

@Data
public class CreateUserRequest {
    @NotBlank(message = "用户名不能为空")
    private String name;
    
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码至少6位")
    private String password;
}

@Data
public class UpdateUserRequest {
    @NotBlank(message = "用户名不能为空")
    private String name;
    
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
}

// ========== 响应 VO（返回给前端）==========

@Data
public class UserVO {
    private Long id;
    private String name;
    private String email;
    private String status;
    private LocalDateTime createTime;
}

// ========== 查询条件 VO ==========

@Data
public class UserQueryVo {
    private String name;        // 用户名（模糊查询）
    private String email;       // 邮箱（精确查询）
    private Integer status;     // 状态
    private LocalDateTime startTime;  // 创建时间范围
    private LocalDateTime endTime;
}
```

### 5.4 命名规范

| 类型 | 后缀 | 用途 |
|------|------|------|
| Entity | 无后缀或 `Entity` | 对应数据库表 |
| 请求 DTO | `Request` / `DTO` | 接收前端参数 |
| 响应 VO | `VO` / `Response` | 返回给前端 |
| 查询条件 | `Query` / `QueryVo` | 封装查询条件 |

### 5.5 注意事项

- DTO/VO 是**纯数据类**，不包含业务逻辑
- DTO/VO 应该实现 `Serializable` 接口（如果需要序列化）
- 不要把 Entity 直接暴露给前端，防止敏感数据泄露

---

## 6. 各层协作流程

### 6.1 完整请求处理链路

以「创建用户」为例：

```
前端 → Controller → Service → Mapper → 数据库
         ↓           ↓         ↓
        参数解析    业务逻辑   SQL执行
        参数校验    事务管理   结果映射
        响应封装    数据转换   
```

### 6.2 代码执行流程

```java
// 1. 前端发送 POST /api/users 请求
//    Body: {"name": "张三", "email": "zhangsan@example.com", "password": "123456"}

// 2. Controller 接收请求
@PostMapping
public User create(@Valid @RequestBody CreateUserRequest request) {
    return userService.create(request);  // 调用 Service
}

// 3. Service 处理业务逻辑
@Service
public class UserServiceImpl implements UserService {
    public User create(CreateUserRequest request) {
        // 3.1 业务校验
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("邮箱已被注册");
        }
        
        // 3.2 转换 DTO → Entity
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encrypt(request.getPassword()));
        
        // 3.3 调用 Mapper 保存
        return userRepository.save(user);
    }
}

// 4. Mapper 执行 SQL
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // save() 方法自动执行 INSERT 语句
}

// 5. 数据库保存数据
// INSERT INTO user (name, email, password) VALUES ('张三', 'zhangsan@example.com', '加密后密码')

// 6. 返回响应给前端
// Response: {"id": 1, "name": "张三", "email": "zhangsan@example.com", "status": "ACTIVE"}
```

---

## 7. 本项目实际代码示例

### 7.1 项目技术栈

| 子项目 | Spring Boot | 持久层框架 | Java 版本 |
|--------|-------------|-----------|-----------|
| `guigu-auth-parent-learn` | 2.3.6.RELEASE | MyBatis-Plus 3.4.1 | 1.8 |
| `baseon-com_h2database_h2` | 3.2.5 | MyBatis-Plus 3.5.16 | 17 |
| `spring-boot-starter-parent-3_5_16` | 3.5.16 | Spring Data JPA | 17 |
| `mysql-connector-j-8_4_0` | 3.5.16 | Spring JDBC | 17 |

### 7.2 guigu-auth 项目目录结构

```
guigu-auth-parent-learn/step1
├── model/                          # Model 层（独立模块）
│   └── src/main/java/com/rc/model/
│       ├── base/BaseEntity.java    # 公共实体基类
│       ├── system/                 # 数据库实体
│       │   ├── SysUser.java
│       │   ├── SysRole.java
│       │   └── SysMenu.java
│       └── vo/                     # 视图/传输对象
│           ├── LoginVo.java
│           ├── RouterVo.java
│           └── SysRoleQueryVo.java
└── service-system/                 # 业务服务层
    └── src/main/java/com/rc/system/
        ├── Step1.java              # 启动类
        └── mapper/
            └── SysRoleMapper.java  # Mapper 接口
```

### 7.3 BaseEntity 示例

```java
@Data
public class BaseEntity implements Serializable {
    @TableId(type = IdType.AUTO)
    private String id;
    
    @TableField("create_time")
    private Date createTime;
    
    @TableField("update_time")
    private Date updateTime;
    
    @TableLogic                      // 逻辑删除
    @TableField("is_deleted")
    private Integer isDeleted;
    
    @TableField(exist = false)       // 不映射数据库字段
    private Map<String,Object> param = new HashMap<>();
}
```

### 7.4 SysRoleMapper 示例

```java
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {
    // 继承 BaseMapper 自动获得 CRUD 方法
    // 复杂查询可以添加自定义方法 + XML 映射
}
```

---

## 8. 常见问题与最佳实践

### 8.1 常见错误

```java
// ❌ 错误1：Controller 里写业务逻辑
@RestController
public class UserController {
    @PostMapping
    public User create(@RequestBody User user) {
        // 不应该在这里做业务校验、数据转换等
        return userRepository.save(user);
    }
}

// ❌ 错误2：Service 里写 SQL
@Service
public class UserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public User findByEmail(String email) {
        // 不应该直接写 SQL，应该用 Mapper
        return jdbcTemplate.queryForObject("SELECT * FROM user WHERE email = ?", ...);
    }
}

// ❌ 错误3：直接暴露 Entity 给前端
@RestController
public class UserController {
    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {
        // Entity 可能包含密码、敏感字段，不应该直接返回
        return userService.findById(id);
    }
}
```

### 8.2 最佳实践

| 实践 | 说明 |
|------|------|
| Controller 只做参数接收和响应封装 | 不写业务逻辑 |
| Service 处理所有业务逻辑 | 包含事务管理、数据校验、业务规则 |
| Mapper 只做数据访问 | 复杂查询再写自定义 SQL |
| 使用 DTO/VO 传递数据 | 不要直接暴露 Entity |
| 构造函数注入优于 @Autowired | 更安全、更易测试 |
| @Transactional 加在 Service 层 | 而不是 Controller 层 |

### 8.3 分层架构总结

| 层次 | 职责 | 常用注解 | 调用关系 |
|------|------|---------|---------|
| Controller | 接收请求，返回响应 | `@RestController`, `@RequestMapping` | 调用 Service |
| Service | 业务逻辑，事务管理 | `@Service`, `@Transactional` | 调用 Mapper |
| Mapper | 数据库操作 | `@Mapper`, `@Repository` | 操作数据库 |
| DTO/VO | 数据传输 | `@Data` | 各层间传递 |

---

## 参考资料

- [MyBatis-Plus 官方文档](https://baomidou.com/)
- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [Spring Data JPA 官方文档](https://spring.io/projects/spring-data-jpa)
