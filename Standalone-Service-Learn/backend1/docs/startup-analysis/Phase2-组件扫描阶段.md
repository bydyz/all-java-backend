# Phase 2：组件扫描阶段

## 概述

Phase 2 是 Spring Boot 启动流程的第二个阶段，主要完成组件扫描工作。Spring 会扫描指定包下的所有类，识别带有 `@Configuration`、`@Component`、`@Service`、`@Controller` 等注解的类，并生成对应的 Bean 定义。

---

## 2.1 扫描范围

`RbacApplication.java:10-11`

```java
@SpringBootApplication
@MapperScan("com.rc.rbac.mapper")
```

### 2.1.1 扫描包路径

```
┌─────────────────────────────────────────────────────────────────┐
│                    组件扫描范围                                    │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  基础包: com.rc.rbac                                             │
│                                                                 │
│  @SpringBootApplication 扫描范围:                                │
│  └── com.rc.rbac.* (所有子包)                                    │
│                                                                 │
│  @MapperScan 扫描范围:                                           │
│  └── com.rc.rbac.mapper (仅 Mapper 接口)                        │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 2.1.2 注解组合说明

`@SpringBootApplication` 是一个组合注解，等价于：

```java
@SpringBootConfiguration      // 标识这是一个配置类
@EnableAutoConfiguration       // 启用自动配置
@ComponentScan                 // 启用组件扫描
```

---

## 2.2 发现的组件分类

```
┌─────────────────────────────────────────────────────────────────┐
│                    组件扫描发现的类                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  @Configuration (5个配置类)                                     │
│  ├── SecurityConfig.java      → SecurityFilterChain             │
│  ├── CorsConfig.java          → CorsFilter                     │
│  ├── RedisConfig.java         → RedisTemplate                   │
│  ├── MybatisPlusConfig.java   → Interceptor + MetaObjectHandler │
│  └── Knife4jConfig.java       → OpenAPI                        │
│                                                                 │
│  @Component (2个组件)                                           │
│  ├── JwtTokenProvider.java    → JWT Token 管理                  │
│  └── JwtAuthenticationFilter.java → 请求认证过滤器              │
│                                                                 │
│  @Service (4个服务)                                             │
│  ├── UserDetailsServiceImpl.java → UserDetailsService          │
│  ├── UserServiceImpl.java        → UserService                │
│  ├── RoleServiceImpl.java        → RoleService                │
│  └── MenuServiceImpl.java        → MenuService                │
│                                                                 │
│  @RestController (5个控制器)                                    │
│  ├── AuthController.java      → /api/auth/*                    │
│  ├── UserController.java      → /api/user/*                    │
│  ├── RoleController.java      → /api/role/*                    │
│  ├── MenuController.java      → /api/menu/*                    │
│  └── CurrentUserController.java → /api/current-user/*          │
│                                                                 │
│  @Mapper (通过 @MapperScan)                                     │
│  ├── UserMapper.java          → users 表                       │
│  ├── RoleMapper.java          → roles 表                       │
│  ├── MenuMapper.java          → menus 表                       │
│  ├── UserRoleMapper.java      → user_roles 表                  │
│  └── RoleMenuMapper.java      → role_menus 表                  │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 2.3 配置类详解

### 2.3.1 SecurityConfig

`SecurityConfig.java:23-27`

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    // ... 配置内容见 Phase 5
}
```

**职责：**
- 配置 Spring Security 过滤器链
- 定义密码编码器
- 配置认证管理器

### 2.3.2 CorsConfig

`CorsConfig.java:12-13`

```java
@Configuration
public class CorsConfig {
    
    @Bean
    public CorsFilter corsFilter() {
        // ... 详细配置见 Phase 5
    }
}
```

**职责：**
- 配置跨域资源共享（CORS）
- 允许所有来源访问 API

### 2.3.3 RedisConfig

`RedisConfig.java:17-18`

```java
@Configuration
public class RedisConfig {
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        // ... 详细配置见 Phase 4
    }
}
```

**职责：**
- 配置 RedisTemplate
- 设置序列化方式

### 2.3.4 MybatisPlusConfig

`MybatisPlusConfig.java:16-17`

```java
@Configuration
public class MybatisPlusConfig {
    
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // ... 详细配置见 Phase 4
    }
    
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        // ... 详细配置见 Phase 4
    }
}
```

**职责：**
- 配置 MyBatis Plus 分页插件
- 配置自动填充处理器

### 2.3.5 Knife4jConfig

`Knife4jConfig.java:14-15`

```java
@Configuration
public class Knife4jConfig {
    
    @Bean
    public OpenAPI openAPI() {
        // ... 详细配置见 Phase 6
    }
}
```

**职责：**
- 配置 Knife4j API 文档
- 设置 JWT Bearer Token 认证

---

## 2.4 组件详解

### 2.4.1 @Component 组件

#### JwtTokenProvider

`JwtTokenProvider.java:17-19`

```java
@Slf4j
@Component
public class JwtTokenProvider {
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration}")
    private Long jwtExpiration;
    
    // ... 详细实现见 Phase 5
}
```

**职责：**
- 生成 JWT Token
- 验证 JWT Token
- 从 Token 中提取用户信息

#### JwtAuthenticationFilter

`JwtAuthenticationFilter.java:24-27`

```java
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final StringRedisTemplate redisTemplate;
    
    // ... 详细实现见 Phase 5
}
```

**职责：**
- 拦截 HTTP 请求
- 从请求头提取 JWT Token
- 验证 Token 并设置认证信息

### 2.4.2 @Service 服务

#### UserDetailsServiceImpl

`UserDetailsServiceImpl.java:18-20`

```java
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final UserMapper userMapper;
    
    // ... 详细实现见 Phase 5
}
```

**职责：**
- 实现 `UserDetailsService` 接口
- 根据用户名加载用户详情

#### UserServiceImpl

`UserServiceImpl.java:43-45`

```java
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    // ... 详细实现见 Phase 8
}
```

**职责：**
- 实现用户相关业务逻辑
- 登录、登出、注册等功能

### 2.4.3 @RestController 控制器

#### AuthController

`AuthController.java:19-23`

```java
@Tag(name = "认证管理", description = "登录、登出、注册接口")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    
    // ... 详细实现见 Phase 8
}
```

**职责：**
- 处理认证相关请求
- 登录、登出、注册接口

---

## 2.5 组件扫描时序图

```
┌─────────────────────────────────────────────────────────────────┐
│                    Phase 2 组件扫描时序图                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  SpringApplication          ClassPathBeanDefinitionScanner     │
│        │                              │                         │
│        │  扫描 com.rc.rbac.*          │                         │
│        │─────────────────────────────▶│                         │
│        │                              │                         │
│        │                              │  1. 扫描 @Configuration │
│        │                              │     ├── SecurityConfig  │
│        │                              │     ├── CorsConfig      │
│        │                              │     ├── RedisConfig     │
│        │                              │     ├── MybatisPlusConfig│
│        │                              │     └── Knife4jConfig   │
│        │                              │                         │
│        │                              │  2. 扫描 @Component     │
│        │                              │     ├── JwtTokenProvider│
│        │                              │     └── JwtAuthFilter   │
│        │                              │                         │
│        │                              │  3. 扫描 @Service       │
│        │                              │     ├── UserDetailsSvc  │
│        │                              │     ├── UserService     │
│        │                              │     ├── RoleService     │
│        │                              │     └── MenuService     │
│        │                              │                         │
│        │                              │  4. 扫描 @RestController│
│        │                              │     ├── AuthController  │
│        │                              │     ├── UserController  │
│        │                              │     ├── RoleController  │
│        │                              │     ├── MenuController  │
│        │                              │     └── CurrentUserCtrl │
│        │                              │                         │
│        │                              │  5. 扫描 @Mapper        │
│        │                              │     ├── UserMapper      │
│        │                              │     ├── RoleMapper      │
│        │                              │     ├── MenuMapper      │
│        │                              │     ├── UserRoleMapper  │
│        │                              │     └── RoleMenuMapper  │
│        │                              │                         │
│        │  生成 BeanDefinition 列表     │                         │
│        │◀─────────────────────────────│                         │
│        │                              │                         │
│        │  进入 Phase 3                 │                         │
│        │  (Bean 创建)                  │                         │
│        │                              │                         │
└─────────────────────────────────────────────────────────────────┘
```

---

## 2.6 Bean 定义生成

### 2.6.1 BeanDefinition 结构

```java
// 每个被扫描的类都会生成一个 BeanDefinition
public interface BeanDefinition {
    
    // Bean 类名
    String getBeanClassName();
    
    // 作用域（singleton/prototype）
    String getScope();
    
    // 是否懒加载
    boolean isLazyInit();
    
    // 依赖的其他 Bean
    String[] getDependsOn();
    
    // 初始化方法
    String getInitMethodName();
    
    // 销毁方法
    String getDestroyMethodName();
}
```

### 2.6.2 本项目的 Bean 定义

```
┌─────────────────────────────────────────────────────────────────┐
│                    BeanDefinition 列表                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Bean Name                    │ 类型           │ Scope          │
│  ─────────────────────────────┼───────────────┼────────────────│
│  securityConfig               │ Configuration │ singleton      │
│  corsConfig                   │ Configuration │ singleton      │
│  redisConfig                  │ Configuration │ singleton      │
│  mybatisPlusConfig            │ Configuration │ singleton      │
│  knife4jConfig                │ Configuration │ singleton      │
│  jwtTokenProvider             │ Component     │ singleton      │
│  jwtAuthenticationFilter      │ Component     │ singleton      │
│  userDetailsServiceImpl       │ Service       │ singleton      │
│  userServiceImpl              │ Service       │ singleton      │
│  roleServiceImpl              │ Service       │ singleton      │
│  menuServiceImpl              │ Service       │ singleton      │
│  authController               │ RestController│ singleton      │
│  userController               │ RestController│ singleton      │
│  roleController               │ RestController│ singleton      │
│  menuController               │ RestController│ singleton      │
│  currentUserController        │ RestController│ singleton      │
│  userMapper                   │ Mapper        │ singleton      │
│  roleMapper                   │ Mapper        │ singleton      │
│  menuMapper                   │ Mapper        │ singleton      │
│  userRoleMapper               │ Mapper        │ singleton      │
│  roleMenuMapper               │ Mapper        │ singleton      │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 2.7 注解处理流程

### 2.7.1 @Configuration 处理

```
┌─────────────────────────────────────────────────────────────────┐
│                    @Configuration 处理流程                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. 扫描带有 @Configuration 注解的类                             │
│                                                                 │
│  2. 生成 Full 模式的 BeanDefinition                              │
│     └── 使用 CGLIB 代理                                         │
│                                                                 │
│  3. 解析 @Bean 方法                                             │
│     └── 每个 @Bean 方法生成一个 BeanDefinition                   │
│                                                                 │
│  4. 注册到 BeanFactory                                          │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 2.7.2 @Component 处理

```
┌─────────────────────────────────────────────────────────────────┐
│                    @Component 处理流程                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. 扫描带有 @Component 注解的类                                 │
│     └── 包括 @Service、@Controller、@Repository 等              │
│                                                                 │
│  2. 生成 Standard 模式的 BeanDefinition                          │
│     └── 不使用 CGLIB 代理                                       │
│                                                                 │
│  3. 解析构造函数注入                                             │
│     └── 处理 @RequiredArgsConstructor                          │
│                                                                 │
│  4. 注册到 BeanFactory                                          │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 2.8 依赖关系分析

### 2.8.1 依赖关系图

```
┌─────────────────────────────────────────────────────────────────┐
│                    Bean 依赖关系图                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│                        ┌─────────────┐                          │
│                        │ UserMapper  │                          │
│                        └──────┬──────┘                          │
│                               │                                 │
│                               ▼                                 │
│                    ┌─────────────────────┐                      │
│                    │ UserDetailsSvcImpl  │                      │
│                    └──────────┬──────────┘                      │
│                               │                                 │
│                               ▼                                 │
│  ┌─────────────┐    ┌─────────────────────┐                     │
│  │ JwtToken    │───▶│ JwtAuthFilter       │                     │
│  │ Provider    │    └─────────────────────┘                     │
│  └─────────────┘              │                                │
│                               │                                 │
│                               ▼                                 │
│                    ┌─────────────────────┐                      │
│                    │   SecurityConfig    │                      │
│                    └─────────────────────┘                      │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 2.8.2 依赖注入顺序

```
┌─────────────────────────────────────────────────────────────────┐
│                    依赖注入顺序                                  │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. 无依赖的 Bean 先创建                                        │
│     ├── UserMapper (无依赖)                                     │
│     ├── RoleMapper (无依赖)                                     │
│     └── MenuMapper (无依赖)                                     │
│                                                                 │
│  2. 依赖基础 Bean 的组件                                         │
│     ├── UserDetailsServiceImpl (依赖 UserMapper)                │
│     └── JwtTokenProvider (无外部依赖)                            │
│                                                                 │
│  3. 依赖多个 Bean 的组件                                         │
│     ├── JwtAuthenticationFilter (依赖 JwtTokenProvider +        │
│     │                             UserDetailsService +           │
│     │                             StringRedisTemplate)          │
│     └── UserServiceImpl (依赖多个 Mapper + Service)             │
│                                                                 │
│  4. 依赖复杂组件的配置类                                         │
│     └── SecurityConfig (依赖 JwtAuthenticationFilter)           │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 2.9 扫描结果统计

| 组件类型 | 数量 | 说明 |
|----------|------|------|
| @Configuration | 5 | 配置类 |
| @Component | 2 | 基础组件 |
| @Service | 4 | 业务服务 |
| @RestController | 5 | REST 控制器 |
| @Mapper | 5 | 数据访问层 |
| **总计** | **21** | - |

---

## 2.10 扫描阶段完成标志

当以下条件满足时，扫描阶段完成：

- [x] 指定包路径下的所有类扫描完成
- [x] 所有注解处理完成
- [x] BeanDefinition 列表生成完成
- [x] 依赖关系分析完成
- [x] BeanDefinition 注册到 BeanFactory

**下一步：** 进入 Phase 3（Bean 创建阶段），开始创建和初始化所有单例 Bean。

---

## 2.11 常见问题

### Q1: 为什么某个 Bean 没有被扫描到？

**可能原因：**
1. 包路径不在扫描范围内
2. 类没有添加正确的注解
3. 条件注解不满足（如 `@ConditionalOnProperty`）

**排查方法：**
```bash
# 启动时添加 debug 参数
java -jar app.jar --debug

# 查看 Bean 定义注册情况
logging.level.org.springframework.context.annotation=debug
```

### Q2: 如何排除某些 Bean 的扫描？

**方法一：** 使用 `@ComponentScan` 的 excludeFilters

```java
@ComponentScan(
    basePackages = "com.rc.rbac",
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = { SomeClass.class }
    )
)
```

**方法二：** 使用条件注解

```java
@ConditionalOnProperty(name = "app.feature.enabled", havingValue = "true")
@Component
public class ConditionalBean {
    // ...
}
```

---

## 2.12 下一步

扫描阶段完成后，Spring 将进入 Phase 3（Bean 创建阶段），开始：
1. 创建所有单例 Bean
2. 执行依赖注入
3. 调用初始化方法

详见 [Phase 3：Bean 创建阶段](./Phase3-Bean创建阶段.md)
