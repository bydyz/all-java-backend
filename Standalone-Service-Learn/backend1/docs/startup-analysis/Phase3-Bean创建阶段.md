# Phase 3：Bean 创建阶段

## 概述

Phase 3 是 Spring Boot 启动流程的第三个阶段，主要完成 Bean 的创建和依赖注入。Spring 会根据 Phase 2 生成的 BeanDefinition 列表，按依赖关系顺序创建所有单例 Bean，并完成依赖注入。

---

## 3.1 Bean 创建流程

```
┌─────────────────────────────────────────────────────────────────┐
│                    Bean 创建流程                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. 解析 BeanDefinition                                         │
│     └── 确定 Bean 的类型、作用域、依赖关系                        │
│                                                                 │
│  2. 解决依赖                                                    │
│     └── 创建依赖的 Bean（递归）                                  │
│                                                                 │
│  3. 实例化 Bean                                                 │
│     ├── 构造函数注入                                            │
│     └── 工厂方法注入                                            │
│                                                                 │
│  4. 属性填充                                                    │
│     └── @Autowired / @Value 注入                                │
│                                                                 │
│  5. 初始化 Bean                                                 │
│     ├── @PostConstruct                                         │
│     └── InitializingBean.afterPropertiesSet()                  │
│                                                                 │
│  6. 注册到单例池                                                │
│     └── singletonObjects.put(beanName, bean)                   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 3.2 基础设施 Bean

### 3.2.1 PasswordEncoder

`SecurityConfig.java:59-62`

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

**作用：**
- 密码加密和验证
- 使用 BCrypt 算法

**创建流程：**

```
┌─────────────────────────────────────────────────────────────────┐
│                    PasswordEncoder 创建流程                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. Spring 检测到 @Bean 方法                                    │
│                                                                 │
│  2. 调用 passwordEncoder() 方法                                 │
│     └── return new BCryptPasswordEncoder()                      │
│                                                                 │
│  3. 将 Bean 注册到 BeanFactory                                  │
│     └── beanName = "passwordEncoder"                            │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 3.2.2 AuthenticationManager

`SecurityConfig.java:67-70`

```java
@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
}
```

**作用：**
- 管理认证请求
- 委托给 ProviderManager 处理

**依赖：**
- `AuthenticationConfiguration`（Spring 自动配置）

### 3.2.3 JwtTokenProvider

`JwtTokenProvider.java:17-25`

```java
@Slf4j
@Component
public class JwtTokenProvider {
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration}")
    private Long jwtExpiration;
    
    // ...
}
```

**依赖注入：**

```
┌─────────────────────────────────────────────────────────────────┐
│                    JwtTokenProvider 依赖注入                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  @Value("${jwt.secret}")                                        │
│  └── 值: "rbac-secret-key-must-be-at-least-256-bits-long-..."  │
│                                                                 │
│  @Value("${jwt.expiration}")                                    │
│  └── 值: 86400000 (24小时)                                      │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 3.2.4 RedisTemplate

`RedisConfig.java:20-43`

```java
@Bean
public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    
    // 使用 Jackson2JsonRedisSerializer 序列化值
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
    
    Jackson2JsonRedisSerializer<Object> jsonSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
    StringRedisSerializer stringSerializer = new StringRedisSerializer();
    
    // key 使用 String 序列化
    template.setKeySerializer(stringSerializer);
    template.setHashKeySerializer(stringSerializer);
    
    // value 使用 JSON 序列化
    template.setValueSerializer(jsonSerializer);
    template.setHashValueSerializer(jsonSerializer);
    
    template.afterPropertiesSet();
    return template;
}
```

**依赖：**
- `RedisConnectionFactory`（Spring 自动配置）

**序列化配置：**

| 序列化方式 | 应用场景 | 说明 |
|------------|----------|------|
| `StringRedisSerializer` | Key | 字符串序列化 |
| `Jackson2JsonRedisSerializer` | Value | JSON 序列化 |

---

## 3.3 安全配置 Bean

### 3.3.1 JwtAuthenticationFilter

`JwtAuthenticationFilter.java:24-31`

```java
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final StringRedisTemplate redisTemplate;
    
    // ...
}
```

**依赖注入：**

```
┌─────────────────────────────────────────────────────────────────┐
│                    JwtAuthenticationFilter 依赖注入              │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  JwtTokenProvider                                               │
│  └── 已在 Phase 3.2.3 创建                                      │
│                                                                 │
│  UserDetailsService                                             │
│  └── UserDetailsServiceImpl (Phase 3.4)                         │
│                                                                 │
│  StringRedisTemplate                                            │
│  └── Spring 自动配置                                             │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 3.3.2 SecurityFilterChain

`SecurityConfig.java:34-54`

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers(
                "/api/auth/login",
                "/api/auth/register",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources/**",
                "/v3/api-docs/**"
            ).permitAll()
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
}
```

**依赖：**
- `JwtAuthenticationFilter`（Phase 3.3.1）

---

## 3.4 服务层 Bean

### 3.4.1 UserDetailsServiceImpl

`UserDetailsServiceImpl.java:18-22`

```java
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final UserMapper userMapper;
    
    // ...
}
```

**依赖：**
- `UserMapper`（MyBatis Mapper）

**实现的接口：**

```java
public interface UserDetailsService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
```

### 3.4.2 UserServiceImpl

`UserServiceImpl.java:43-54`

```java
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate redisTemplate;
    private final MenuService menuService;
    
    // ...
}
```

**依赖注入：**

```
┌─────────────────────────────────────────────────────────────────┐
│                    UserServiceImpl 依赖注入                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  UserMapper            → 数据库用户操作                          │
│  RoleMapper            → 数据库角色操作                          │
│  UserRoleMapper        → 用户角色关联操作                        │
│  PasswordEncoder       → 密码加密                               │
│  AuthenticationManager → 认证管理                               │
│  JwtTokenProvider      → JWT Token 生成                         │
│  StringRedisTemplate   → Redis 操作                             │
│  MenuService           → 菜单服务                               │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 3.4.3 RoleServiceImpl

**依赖：**
- `RoleMapper`
- `RoleMenuMapper`

### 3.4.4 MenuServiceImpl

**依赖：**
- `MenuMapper`
- `RoleMenuMapper`

---

## 3.5 MyBatis Plus 配置 Bean

### 3.5.1 MybatisPlusInterceptor

`MybatisPlusConfig.java:22-27`

```java
@Bean
public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    return interceptor;
}
```

**作用：**
- 添加分页插件
- 支持 MySQL 分页查询

### 3.5.2 MetaObjectHandler

`MybatisPlusConfig.java:32-46`

```java
@Bean
public MetaObjectHandler metaObjectHandler() {
    return new MetaObjectHandler() {
        @Override
        public void insertFill(MetaObject metaObject) {
            this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
            this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        }
        
        @Override
        public void updateFill(MetaObject metaObject) {
            this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        }
    };
}
```

**作用：**
- 自动填充 `createTime` 和 `updateTime` 字段

---

## 3.6 控制器 Bean

### 3.6.1 AuthController

`AuthController.java:19-25`

```java
@Tag(name = "认证管理", description = "登录、登出、注册接口")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    
    // ...
}
```

**依赖：**
- `UserService`

**处理的请求：**

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/auth/login | 用户登录 |
| POST | /api/auth/logout | 用户登出 |
| POST | /api/auth/register | 用户注册 |

### 3.6.2 UserController

**依赖：**
- `UserService`

**处理的请求：**

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/user/page | 分页查询用户 |
| GET | /api/user/{id} | 获取用户详情 |
| POST | /api/user | 创建用户 |
| PUT | /api/user/{id} | 更新用户 |
| DELETE | /api/user/{id} | 删除用户 |

### 3.6.3 RoleController

**依赖：**
- `RoleService`

### 3.6.4 MenuController

**依赖：**
- `MenuService`

### 3.6.5 CurrentUserController

**依赖：**
- `UserService`

---

## 3.7 Mapper Bean

### 3.7.1 UserMapper

```java
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    User selectByUsername(@Param("username") String username);
    
    List<String> selectPermissionsByUserId(@Param("userId") Long userId);
}
```

### 3.7.2 RoleMapper

```java
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    
    List<Role> selectRolesByUserId(@Param("userId") Long userId);
}
```

### 3.7.3 MenuMapper

```java
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    
    List<Menu> selectMenusByUserId(@Param("userId") Long userId);
}
```

### 3.7.4 UserRoleMapper

```java
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
    
    void deleteByUserId(@Param("userId") Long userId);
}
```

### 3.7.5 RoleMenuMapper

```java
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    
    void deleteByRoleId(@Param("roleId") Long roleId);
}
```

---

## 3.8 Bean 创建时序图

```
┌─────────────────────────────────────────────────────────────────┐
│                    Phase 3 Bean 创建时序图                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ApplicationContext        BeanFactory         SingletonObjects │
│        │                       │                      │         │
│        │  refresh()            │                      │         │
│        │──────────────────────▶│                      │         │
│        │                       │                      │         │
│        │                       │  1. 创建无依赖 Bean   │         │
│        │                       │     ├── UserMapper   │         │
│        │                       │     ├── RoleMapper   │         │
│        │                       │     └── MenuMapper   │         │
│        │                       │─────────────────────▶│         │
│        │                       │                      │         │
│        │                       │  2. 创建依赖 Bean     │         │
│        │                       │     ├── PasswordEncoder        │
│        │                       │     ├── AuthManager  │         │
│        │                       │     ├── JwtTokenProvider       │
│        │                       │─────────────────────▶│         │
│        │                       │                      │         │
│        │                       │  3. 创建服务 Bean     │         │
│        │                       │     ├── UserDetailsSvc         │
│        │                       │     ├── UserService  │         │
│        │                       │     ├── RoleService  │         │
│        │                       │     └── MenuService  │         │
│        │                       │─────────────────────▶│         │
│        │                       │                      │         │
│        │                       │  4. 创建控制器 Bean   │         │
│        │                       │     ├── AuthController         │
│        │                       │     ├── UserController         │
│        │                       │     ├── RoleController         │
│        │                       │     ├── MenuController         │
│        │                       │     └── CurrentUserCtrl        │
│        │                       │─────────────────────▶│         │
│        │                       │                      │         │
│        │                       │  5. 创建配置 Bean     │         │
│        │                       │     ├── SecurityConfig         │
│        │                       │     ├── CorsConfig   │         │
│        │                       │     ├── RedisConfig  │         │
│        │                       │     ├── MybatisPlusConfig      │
│        │                       │     └── Knife4jConfig          │
│        │                       │─────────────────────▶│         │
│        │                       │                      │         │
│        │  Bean 创建完成        │                      │         │
│        │◀──────────────────────│                      │         │
│        │                       │                      │         │
└─────────────────────────────────────────────────────────────────┘
```

---

## 3.9 依赖注入机制

### 3.9.1 构造函数注入

```java
@Service
@RequiredArgsConstructor  // Lombok 自动生成构造函数
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final UserMapper userMapper;  // 自动注入
    
    // Lombok 生成的构造函数：
    // public UserDetailsServiceImpl(UserMapper userMapper) {
    //     this.userMapper = userMapper;
    // }
}
```

### 3.9.2 字段注入

```java
@Component
public class JwtTokenProvider {
    
    @Value("${jwt.secret}")  // 自动注入配置值
    private String jwtSecret;
    
    @Value("${jwt.expiration}")
    private Long jwtExpiration;
}
```

### 3.9.3 Setter 注入

```java
@Component
public class SomeBean {
    
    private OtherBean otherBean;
    
    @Autowired
    public void setOtherBean(OtherBean otherBean) {
        this.otherBean = otherBean;
    }
}
```

---

## 3.10 初始化回调

### 3.10.1 @PostConstruct

```java
@Component
public class SomeBean {
    
    @PostConstruct
    public void init() {
        // 在依赖注入完成后执行
        System.out.println("Bean 初始化完成");
    }
}
```

### 3.10.2 InitializingBean

```java
@Component
public class SomeBean implements InitializingBean {
    
    @Override
    public void afterPropertiesSet() throws Exception {
        // 在属性填充完成后执行
        System.out.println("属性设置完成");
    }
}
```

### 3.10.3 init-method

```java
@Component(initMethod = "init")
public class SomeBean {
    
    public void init() {
        // 自定义初始化方法
    }
}
```

---

## 3.11 Bean 生命周期

```
┌─────────────────────────────────────────────────────────────────┐
│                    Bean 完整生命周期                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. 实例化 (Instantiation)                                      │
│     └── 调用构造函数                                             │
│                                                                 │
│  2. 属性填充 (Populate Properties)                              │
│     └── 依赖注入                                                 │
│                                                                 │
│  3. BeanNameAware.setBeanName()                                 │
│     └── 设置 Bean 名称                                          │
│                                                                 │
│  4. BeanFactoryAware.setBeanFactory()                           │
│     └── 设置 BeanFactory                                        │
│                                                                 │
│  5. ApplicationContextAware.setApplicationContext()              │
│     └── 设置 ApplicationContext                                 │
│                                                                 │
│  6. BeanPostProcessor.postProcessBeforeInitialization()          │
│     └── 前置处理                                                 │
│                                                                 │
│  7. @PostConstruct                                              │
│     └── 初始化注解                                               │
│                                                                 │
│  8. InitializingBean.afterPropertiesSet()                       │
│     └── 初始化回调                                               │
│                                                                 │
│  9. init-method                                                 │
│     └── 自定义初始化方法                                         │
│                                                                 │
│  10. BeanPostProcessor.postProcessAfterInitialization()         │
│      └── 后置处理（AOP 代理在此创建）                            │
│                                                                 │
│  11. Bean 就绪，可以使用                                         │
│                                                                 │
│  12. @PreDestroy                                                │
│      └── 销毁注解                                               │
│                                                                 │
│  13. DisposableBean.destroy()                                   │
│      └── 销毁回调                                               │
│                                                                 │
│  14. destroy-method                                             │
│      └── 自定义销毁方法                                         │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 3.12 创建阶段完成标志

当以下条件满足时，Bean 创建阶段完成：

- [x] 所有无依赖 Bean 创建完成
- [x] 所有有依赖 Bean 创建完成
- [x] 依赖注入完成
- [x] 初始化回调执行完成
- [x] 所有 Bean 注册到单例池

**下一步：** 进入 Phase 4（数据源初始化阶段），开始初始化 MySQL 和 Redis 连接。

---

## 3.13 常见问题

### Q1: 循环依赖如何解决？

**Spring 的解决方案：**
1. 三级缓存机制
2. 提前暴露半成品 Bean

```java
// 一级缓存：完整的 Bean
private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

// 二级缓存：早期暴露的 Bean
private final Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>();

// 三级缓存：Bean 工厂
private final Map<String, ObjectFactory<?>> singletonFactories = new ConcurrentHashMap<>();
```

### Q2: 如何自定义初始化顺序？

**方法一：** 使用 `@DependsOn`

```java
@Component
@DependsOn("beanA")
public class BeanB {
    // BeanB 会在 BeanA 之后创建
}
```

**方法二：** 使用 `@Order`

```java
@Component
@Order(1)
public class BeanA {
    // 优先级较高
}
```

---

## 3.14 下一步

Bean 创建阶段完成后，Spring 将进入 Phase 4（数据源初始化阶段），开始：
1. 初始化 MySQL 连接池
2. 执行数据库初始化脚本
3. 初始化 Redis 连接

详见 [Phase 4：数据源初始化阶段](./Phase4-数据源初始化阶段.md)
