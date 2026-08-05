# Phase 7：完整启动时序图

## 概述

Phase 7 展示了 Spring Boot RBAC 系统的完整启动时序图，从 JVM 启动到服务就绪的整个过程。通过时序图可以清晰地看到各个组件之间的交互关系和执行顺序。

---

## 7.1 启动流程概览

```
┌─────────────────────────────────────────────────────────────────┐
│                    Spring Boot 启动生命周期                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐         │
│  │   Phase 1   │───▶│   Phase 2   │───▶│   Phase 3   │         │
│  │  准备阶段   │    │  扫描阶段   │    │  创建阶段   │         │
│  └─────────────┘    └─────────────┘    └─────────────┘         │
│        │                  │                  │                  │
│        ▼                  ▼                  ▼                  │
│  SpringApplication   组件扫描/解析     BeanFactory 创建        │
│  初始化              配置类            所有单例 Bean            │
│                                                                 │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐         │
│  │   Phase 4   │───▶│   Phase 5   │───▶│   Phase 6   │         │
│  │  数据源     │    │  Security   │    │  Web容器    │         │
│  └─────────────┘    └─────────────┘    └─────────────┘         │
│        │                  │                  │                  │
│        ▼                  ▼                  ▼                  │
│  MySQL/Redis连接      过滤器链配置      Tomcat启动              │
│  数据库初始化         JWT认证           端口8080监听             │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 7.2 完整启动时序图

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                    RBAC 系统启动时序图                                           │
└─────────────────────────────────────────────────────────────────────────────────┘

  JVM                    SpringApplication           ApplicationContext
   │                           │                              │
   │  main(args)               │                              │
   │──────────────────────────▶│                              │
   │                           │                              │
   │                           │  ┌───────────────────────────┤
   │                           │  │ Phase 1: 准备阶段          │
   │                           │  │                           │
   │                           │  │ 1. 记录启动时间            │
   │                           │  │ 2. 创建 Environment       │
   │                           │  │ 3. 加载配置文件            │
   │                           │  │    ├── application.yml    │
   │                           │  │    └── application-dev.yml│
   │                           │  │ 4. 设置 webApplicationType│
   │                           │  │ 5. 设置 initializers      │
   │                           │  │ 6. 设置 listeners         │
   │                           │  └───────────────────────────┤
   │                           │                              │
   │                           │  refresh()                   │
   │                           │─────────────────────────────▶│
   │                           │                              │
   │                           │  ┌───────────────────────────┤
   │                           │  │ Phase 2: 扫描阶段          │
   │                           │  │                           │
   │                           │  │ 1. 扫描 @Configuration    │
   │                           │  │    ├── SecurityConfig     │
   │                           │  │    ├── CorsConfig         │
   │                           │  │    ├── RedisConfig        │
   │                           │  │    ├── MybatisPlusConfig  │
   │                           │  │    └── Knife4jConfig      │
   │                           │  │                           │
   │                           │  │ 2. 扫描 @Component        │
   │                           │  │    ├── JwtTokenProvider   │
   │                           │  │    └── JwtAuthFilter      │
   │                           │  │                           │
   │                           │  │ 3. 扫描 @Service          │
   │                           │  │    ├── UserDetailsSvc     │
   │                           │  │    ├── UserService        │
   │                           │  │    ├── RoleService        │
   │                           │  │    └── MenuService        │
   │                           │  │                           │
   │                           │  │ 4. 扫描 @RestController   │
   │                           │  │    ├── AuthController     │
   │                           │  │    ├── UserController     │
   │                           │  │    ├── RoleController     │
   │                           │  │    ├── MenuController     │
   │                           │  │    └── CurrentUserCtrl    │
   │                           │  │                           │
   │                           │  │ 5. 扫描 @Mapper           │
   │                           │  │    ├── UserMapper         │
   │                           │  │    ├── RoleMapper         │
   │                           │  │    ├── MenuMapper         │
   │                           │  │    ├── UserRoleMapper     │
   │                           │  │    └── RoleMenuMapper     │
   │                           │  └───────────────────────────┤
   │                           │                              │
   │                           │  ┌───────────────────────────┤
   │                           │  │ Phase 3: 创建阶段          │
   │                           │  │                           │
   │                           │  │ 1. 基础设施 Bean           │
   │                           │  │    ├── PasswordEncoder    │
   │                           │  │    ├── AuthenticationMgr  │
   │                           │  │    ├── JwtTokenProvider   │
   │                           │  │    └── JwtAuthFilter      │
   │                           │  │                           │
   │                           │  │ 2. 配置 Bean              │
   │                           │  │    ├── SecurityConfig     │
   │                           │  │    ├── CorsConfig         │
   │                           │  │    ├── RedisConfig        │
   │                           │  │    ├── MybatisPlusConfig  │
   │                           │  │    └── Knife4jConfig      │
   │                           │  │                           │
   │                           │  │ 3. 服务 Bean              │
   │                           │  │    ├── UserDetailsSvc     │
   │                           │  │    ├── UserService        │
   │                           │  │    ├── RoleService        │
   │                           │  │    └── MenuService        │
   │                           │  │                           │
   │                           │  │ 4. 控制器 Bean            │
   │                           │  │    ├── AuthController     │
   │                           │  │    ├── UserController     │
   │                           │  │    ├── RoleController     │
   │                           │  │    ├── MenuController     │
   │                           │  │    └── CurrentUserCtrl    │
   │                           │  └───────────────────────────┤
   │                           │                              │
   │                           │  ┌───────────────────────────┤
   │                           │  │ Phase 4: 数据源阶段        │
   │                           │  │                           │
   │                           │  │ 1. MySQL 连接池           │
   │                           │  │    ├── HikariCP 初始化    │
   │                           │  │    ├── 连接测试            │
   │                           │  │    └── 预创建连接          │
   │                           │  │                           │
   │                           │  │ 2. 执行数据库脚本          │
   │                           │  │    ├── schema.sql         │
   │                           │  │    └── data.sql           │
   │                           │  │                           │
   │                           │  │ 3. Redis 连接             │
   │                           │  │    ├── Lettuce 初始化     │
   │                           │  │    └── 连接池配置          │
   │                           │  │                           │
   │                           │  │ 4. MyBatis Plus 配置      │
   │                           │  │    ├── 分页插件            │
   │                           │  │    └── 自动填充            │
   │                           │  └───────────────────────────┤
   │                           │                              │
   │                           │  ┌───────────────────────────┤
   │                           │  │ Phase 5: Security 阶段    │
   │                           │  │                           │
   │                           │  │ 1. CORS 配置              │
   │                           │  │    └── CorsFilter         │
   │                           │  │                           │
   │                           │  │ 2. Security 配置          │
   │                           │  │    ├── CSRF: disabled     │
   │                           │  │    ├── Session: STATELESS │
   │                           │  │    ├── Public URLs        │
   │                           │  │    └── JWT Filter         │
   │                           │  │                           │
   │                           │  │ 3. 密码编码器              │
   │                           │  │    └── BCryptPasswordEncoder│
   │                           │  │                           │
   │                           │  │ 4. 认证管理器              │
   │                           │  │    └── AuthenticationManager│
   │                           │  └───────────────────────────┤
   │                           │                              │
   │                           │  ┌───────────────────────────┤
   │                           │  │ Phase 6: Web 容器阶段      │
   │                           │  │                           │
   │                           │  │ 1. Tomcat 初始化          │
   │                           │  │    ├── Connector (8080)   │
   │                           │  │    ├── Service            │
   │                           │  │    ├── Engine             │
   │                           │  │    └── Host               │
   │                           │  │                           │
   │                           │  │ 2. DispatcherServlet 注册 │
   │                           │  │                           │
   │                           │  │ 3. HandlerMapping 注册    │
   │                           │  │    ├── AuthController     │
   │                           │  │    ├── UserController     │
   │                           │  │    ├── RoleController     │
   │                           │  │    ├── MenuController     │
   │                           │  │    └── CurrentUserCtrl    │
   │                           │  │                           │
   │                           │  │ 4. Knife4j 资源注册       │
   │                           │  │    ├── /doc.html          │
   │                           │  │    └── /v3/api-docs       │
   │                           │  │                           │
   │                           │  │ 5. Tomcat 启动完成        │
   │                           │  │    └── 端口 8080 监听     │
   │                           │  └───────────────────────────┤
   │                           │                              │
   │                           │  服务启动完成                  │
   │                           │◀─────────────────────────────│
   │                           │                              │
   │                           │  输出: "Started RbacApplication"
   │  启动完成                  │                              │
   │◀──────────────────────────│                              │
   │                           │                              │
   │  端口8080监听             │                              │
   │  Ready to serve           │                              │
   │                           │                              │
```

---

## 7.3 各阶段详细说明

### 7.3.1 Phase 1：准备阶段

```
┌─────────────────────────────────────────────────────────────────┐
│                    Phase 1 详细流程                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. 记录启动时间                                                 │
│     └── long startTime = System.currentTimeMillis()             │
│                                                                 │
│  2. 创建 Environment                                            │
│     └── StandardServletEnvironment                              │
│                                                                 │
│  3. 加载配置文件                                                 │
│     ├── application.yml                                         │
│     └── application-dev.yml                                     │
│                                                                 │
│  4. 设置 webApplicationType                                     │
│     └── WebApplicationType.SERVLET                              │
│                                                                 │
│  5. 设置 initializers                                           │
│     ├── ConfigurationWarnings                                  │
│     ├── ServerPortInfo                                         │
│     └── SharedMetadataReader                                   │
│                                                                 │
│  6. 设置 listeners                                              │
│     ├── LoggingApplicationListener                              │
│     ├── ConfigFileApplicationListener                           │
│     └── AnsiBackgroundApplicationListener                       │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 7.3.2 Phase 2：扫描阶段

```
┌─────────────────────────────────────────────────────────────────┐
│                    Phase 2 详细流程                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  扫描包路径: com.rc.rbac.*                                       │
│                                                                 │
│  发现的组件:                                                     │
│  ├── @Configuration: 5个                                        │
│  │   ├── SecurityConfig                                         │
│  │   ├── CorsConfig                                             │
│  │   ├── RedisConfig                                            │
│  │   ├── MybatisPlusConfig                                      │
│  │   └── Knife4jConfig                                          │
│  │                                                              │
│  ├── @Component: 2个                                            │
│  │   ├── JwtTokenProvider                                       │
│  │   └── JwtAuthenticationFilter                                │
│  │                                                              │
│  ├── @Service: 4个                                              │
│  │   ├── UserDetailsServiceImpl                                 │
│  │   ├── UserServiceImpl                                        │
│  │   ├── RoleServiceImpl                                        │
│  │   └── MenuServiceImpl                                        │
│  │                                                              │
│  ├── @RestController: 5个                                       │
│  │   ├── AuthController                                         │
│  │   ├── UserController                                         │
│  │   ├── RoleController                                         │
│  │   ├── MenuController                                         │
│  │   └── CurrentUserController                                  │
│  │                                                              │
│  └── @Mapper: 5个                                               │
│      ├── UserMapper                                             │
│      ├── RoleMapper                                             │
│      ├── MenuMapper                                             │
│      ├── UserRoleMapper                                         │
│      └── RoleMenuMapper                                         │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 7.3.3 Phase 3：创建阶段

```
┌─────────────────────────────────────────────────────────────────┐
│                    Phase 3 详细流程                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Bean 创建顺序:                                                  │
│                                                                 │
│  1. 无依赖 Bean                                                 │
│     ├── UserMapper                                              │
│     ├── RoleMapper                                              │
│     └── MenuMapper                                              │
│                                                                 │
│  2. 基础设施 Bean                                                │
│     ├── PasswordEncoder (BCryptPasswordEncoder)                 │
│     ├── AuthenticationManager                                   │
│     ├── JwtTokenProvider                                        │
│     │   ├── @Value("${jwt.secret}")                             │
│     │   └── @Value("${jwt.expiration}")                         │
│     └── JwtAuthenticationFilter                                 │
│         ├── JwtTokenProvider                                    │
│         ├── UserDetailsService                                  │
│         └── StringRedisTemplate                                 │
│                                                                 │
│  3. 服务 Bean                                                   │
│     ├── UserDetailsServiceImpl                                  │
│     │   └── UserMapper                                          │
│     ├── UserServiceImpl                                         │
│     │   ├── UserMapper                                          │
│     │   ├── RoleMapper                                          │
│     │   ├── UserRoleMapper                                      │
│     │   ├── PasswordEncoder                                     │
│     │   ├── AuthenticationManager                               │
│     │   ├── JwtTokenProvider                                    │
│     │   ├── StringRedisTemplate                                 │
│     │   └── MenuService                                         │
│     ├── RoleServiceImpl                                         │
│     └── MenuServiceImpl                                         │
│                                                                 │
│  4. 控制器 Bean                                                  │
│     ├── AuthController (UserService)                            │
│     ├── UserController (UserService)                            │
│     ├── RoleController (RoleService)                            │
│     ├── MenuController (MenuService)                            │
│     └── CurrentUserController (UserService)                     │
│                                                                 │
│  5. 配置 Bean                                                   │
│     ├── SecurityConfig (JwtAuthenticationFilter)                │
│     ├── CorsConfig                                              │
│     ├── RedisConfig                                             │
│     ├── MybatisPlusConfig                                       │
│     └── Knife4jConfig                                           │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 7.3.4 Phase 4：数据源阶段

```
┌─────────────────────────────────────────────────────────────────┐
│                    Phase 4 详细流程                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. MySQL 连接池初始化                                           │
│     ├── Driver: com.mysql.cj.jdbc.Driver                       │
│     ├── URL: jdbc:mysql://localhost:3306/rbac_db               │
│     ├── Username: root                                          │
│     ├── Password: rU^c*sM_SL1.ye                               │
│     ├── minimum-idle: 5                                         │
│     └── maximum-pool-size: 20                                   │
│                                                                 │
│  2. 执行 schema.sql                                             │
│     ├── CREATE DATABASE rbac_db                                 │
│     ├── CREATE TABLE users                                      │
│     ├── CREATE TABLE roles                                      │
│     ├── CREATE TABLE menus                                      │
│     ├── CREATE TABLE user_roles                                 │
│     └── CREATE TABLE role_menus                                 │
│                                                                 │
│  3. 执行 data.sql                                               │
│     ├── INSERT INTO users (admin, user)                         │
│     ├── INSERT INTO roles (超级管理员, 普通用户)                   │
│     ├── INSERT INTO user_roles                                  │
│     ├── INSERT INTO menus (16个菜单项)                           │
│     └── INSERT INTO role_menus                                  │
│                                                                 │
│  4. Redis 连接初始化                                             │
│     ├── Host: localhost                                         │
│     ├── Port: 6379                                              │
│     ├── Database: 0                                             │
│     └── Timeout: 10000ms                                        │
│                                                                 │
│  5. MyBatis Plus 配置                                           │
│     ├── PaginationInnerInterceptor (MySQL 分页)                 │
│     └── MetaObjectHandler (自动填充 createTime, updateTime)     │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 7.3.5 Phase 5：Security 阶段

```
┌─────────────────────────────────────────────────────────────────┐
│                    Phase 5 详细流程                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. CORS 配置                                                   │
│     ├── Origin: *                                               │
│     ├── Header: *                                               │
│     ├── Method: *                                               │
│     ├── Credentials: true                                       │
│     └── MaxAge: 3600                                            │
│                                                                 │
│  2. Security 配置                                               │
│     ├── CSRF: disabled                                          │
│     ├── Session: STATELESS                                      │
│     ├── Public URLs:                                            │
│     │   ├── /api/auth/login                                     │
│     │   ├── /api/auth/register                                  │
│     │   ├── /doc.html                                           │
│     │   ├── /webjars/**                                         │
│     │   ├── /swagger-resources/**                               │
│     │   └── /v3/api-docs/**                                     │
│     └── All other: authenticated                                │
│                                                                 │
│  3. JWT 过滤器                                                  │
│     ├── 位置: UsernamePasswordAuthenticationFilter 之前          │
│     └── 功能: 验证 JWT Token                                     │
│                                                                 │
│  4. 密码编码器                                                   │
│     └── BCryptPasswordEncoder                                   │
│                                                                 │
│  5. 认证管理器                                                   │
│     └── ProviderManager                                         │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 7.3.6 Phase 6：Web 容器阶段

```
┌─────────────────────────────────────────────────────────────────┐
│                    Phase 6 详细流程                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. Tomcat 初始化                                               │
│     ├── Connector: HTTP/1.1, port: 8080                        │
│     ├── Service: Catalina                                       │
│     ├── Engine: Catalina                                        │
│     └── Host: localhost                                         │
│                                                                 │
│  2. DispatcherServlet 注册                                      │
│     ├── URL Mappings: /                                         │
│     ├── Load on Startup: 1                                      │
│     └── Name: dispatcherServlet                                 │
│                                                                 │
│  3. HandlerMapping 注册                                         │
│     ├── AuthController:                                         │
│     │   ├── POST /api/auth/login                                │
│     │   ├── POST /api/auth/logout                               │
│     │   └── POST /api/auth/register                             │
│     ├── UserController:                                         │
│     │   ├── GET /api/user/page                                  │
│     │   ├── GET /api/user/{id}                                  │
│     │   ├── POST /api/user                                      │
│     │   ├── PUT /api/user/{id}                                  │
│     │   └── DELETE /api/user/{id}                               │
│     ├── RoleController:                                         │
│     │   └── ...                                                 │
│     ├── MenuController:                                         │
│     │   └── ...                                                 │
│     └── CurrentUserController:                                  │
│         └── ...                                                 │
│                                                                 │
│  4. Knife4j 资源注册                                            │
│     ├── /doc.html                                               │
│     ├── /webjars/**                                             │
│     ├── /swagger-resources/**                                   │
│     └── /v3/api-docs/**                                         │
│                                                                 │
│  5. Tomcat 启动完成                                              │
│     └── "Tomcat started on port 8080"                           │
│                                                                 │
│  6. Spring Boot 输出                                            │
│     └── "Started RbacApplication in X.XXX seconds"              │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 7.4 组件依赖关系图

```
┌─────────────────────────────────────────────────────────────────┐
│                    组件依赖关系图                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│                         ┌─────────────┐                         │
│                         │ UserMapper  │                         │
│                         └──────┬──────┘                         │
│                                │                                │
│                                ▼                                │
│                     ┌─────────────────────┐                     │
│                     │ UserDetailsSvcImpl  │                     │
│                     └──────────┬──────────┘                     │
│                                │                                │
│                                ▼                                │
│  ┌─────────────┐    ┌─────────────────────┐                     │
│  │ JwtToken    │───▶│ JwtAuthFilter       │                     │
│  │ Provider    │    └─────────────────────┘                     │
│  └─────────────┘              │                                │
│                               │                                │
│                               ▼                                │
│                    ┌─────────────────────┐                      │
│                    │   SecurityConfig    │                      │
│                    └─────────────────────┘                      │
│                                                                 │
│  ┌─────────────┐    ┌─────────────────────┐                     │
│  │ UserService │───▶│   AuthController    │                     │
│  └─────────────┘    └─────────────────────┘                     │
│                                                                 │
│  ┌─────────────┐    ┌─────────────────────┐                     │
│  │ RoleService │───▶│   RoleController    │                     │
│  └─────────────┘    └─────────────────────┘                     │
│                                                                 │
│  ┌─────────────┐    ┌─────────────────────┐                     │
│  │ MenuService │───▶│   MenuController    │                     │
│  └─────────────┘    └─────────────────────┘                     │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 7.5 启动时间线

```
┌─────────────────────────────────────────────────────────────────┐
│                    启动时间线                                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  T0: 0ms      JVM 启动                                          │
│  T1: 100ms    SpringApplication 初始化                           │
│  T2: 200ms    Environment 创建                                   │
│  T3: 300ms    配置文件加载完成                                    │
│  T4: 400ms    组件扫描开始                                       │
│  T5: 800ms    组件扫描完成                                       │
│  T6: 1000ms   Bean 创建开始                                     │
│  T7: 1500ms   Bean 创建完成                                     │
│  T8: 1600ms   数据源初始化开始                                   │
│  T9: 1800ms   数据库脚本执行完成                                  │
│  T10: 1900ms  Redis 连接完成                                    │
│  T11: 2000ms  Security 配置完成                                 │
│  T12: 2100ms  Tomcat 初始化完成                                 │
│  T13: 2200ms  HandlerMapping 注册完成                           │
│  T14: 2300ms  Tomcat 启动完成                                   │
│  T15: 2500ms  服务启动完成                                      │
│                                                                 │
│  总耗时: 约 2.5 秒                                               │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 7.6 启动完成标志

当以下条件满足时，启动完成：

- [x] JVM 启动完成
- [x] SpringApplication 初始化完成
- [x] Environment 创建完成
- [x] 配置文件加载完成
- [x] 组件扫描完成
- [x] Bean 创建完成
- [x] 数据源初始化完成
- [x] Security 配置完成
- [x] Tomcat 启动完成
- [x] 端口 8080 监听完成
- [x] 服务启动完成

**服务已就绪，可以处理客户端请求！**

---

## 7.7 下一步

- 查看请求处理流程：[Phase 8：请求处理流程](./Phase8-请求处理流程.md)
