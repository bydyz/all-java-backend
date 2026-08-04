# Spring Boot RBAC 系统启动流程分析

## 概述

本文档详细分析 `Standalone-Service-Learn/backend1` 项目的 Spring Boot 启动流程，从 `RbacApplication.main()` 开始，到服务成功启动并监听端口的完整过程。

**技术栈：**
- Spring Boot 3.5.16
- Spring Security + JWT
- MyBatis Plus
- MySQL + Redis
- Knife4j (API 文档)

---

## 一、启动入口：main 方法

`RbacApplication.java:14-16`

```java
public static void main(String[] args) {
    SpringApplication.run(RbacApplication.class, args);
}
```

这是程序的唯一入口。`SpringApplication.run()` 触发整个 Spring Boot 的启动流程。

**注解说明：**
- `@SpringBootApplication`: 组合注解，包含 `@SpringBootConfiguration`、`@EnableAutoConfiguration`、`@ComponentScan`
- `@MapperScan("com.rc.rbac.mapper")`: 扫描 MyBatis Mapper 接口

---

## 二、Spring Boot 启动阶段划分

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
│  │  数据源     │    │  Web容器    │    │  服务就绪   │         │
│  └─────────────┘    └─────────────┘    └─────────────┘         │
│        │                  │                  │                  │
│        ▼                  ▼                  ▼                  │
│  MySQL/Redis连接      Tomcat启动         Accepting requests    │
│  数据库初始化         端口8080监听        ready!                │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 三、Phase 1：准备阶段

`SpringApplication.run()` 首先执行：

1. **记录启动时间**
2. **准备环境（Environment）**
   - 读取 `application.yml` + `application-dev.yml`
   - 解析 `spring.profiles.active: dev`，激活 dev 配置

3. **初始化 SpringApplication**
   - 设置 `webApplicationType = SERVLET`（非 Reactive）
   - 设置 `initializers`（ApplicationContextInitializer）
   - 设置 `listeners`（ApplicationListener）

4. **加载 application.yml 配置**

   `application.yml:1-8`
   ```yaml
   server:
     port: 8080
     servlet:
       context-path: /
   spring:
     profiles:
       active: dev
   ```

   `application-dev.yml:1-42`
   - MySQL 数据源：`jdbc:mysql://localhost:3306/rbac_db`
   - Redis 配置：`localhost:6379`
   - JWT 配置：`secret` + `expiration: 86400000`（24小时）

---

## 四、Phase 2：组件扫描阶段

`RbacApplication.java:10-11`
```java
@SpringBootApplication
@MapperScan("com.rc.rbac.mapper")
```

`@SpringBootApplication` = `@SpringBootConfiguration` + `@EnableAutoConfiguration` + `@ComponentScan`

**扫描范围：`com.rc.rbac.*`**

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

## 五、Phase 3：Bean 创建与依赖注入

Spring 按依赖关系创建 Bean：

**5.1 基础设施 Bean 先创建**

```
PasswordEncoder (BCryptPasswordEncoder)
         │
         ▼
AuthenticationManager
         │
         ▼
JwtTokenProvider
    ├── @Value("${jwt.secret}") → "rbac-secret-key-..."
    └── @Value("${jwt.expiration}") → 86400000
         │
         ▼
UserMapper (MyBatis Plus)
         │
         ▼
UserDetailsServiceImpl
    └── 依赖 UserMapper
```

**5.2 安全配置 Bean**

```
SecurityConfig
    ├── 依赖 JwtAuthenticationFilter
    └── 创建 SecurityFilterChain
```

**5.3 缓存配置 Bean**

```
RedisConfig
    └── 创建 RedisTemplate<String, Object>
        ├── KeySerializer: StringRedisSerializer
        └── ValueSerializer: Jackson2JsonRedisSerializer
```

**5.4 MyBatis Plus 配置 Bean**

```
MybatisPlusConfig
    ├── MybatisPlusInterceptor
    │   └── PaginationInnerInterceptor (MySQL分页)
    └── MetaObjectHandler
        ├── insertFill: createTime, updateTime
        └── updateFill: updateTime
```

**5.5 API 文档配置 Bean**

```
Knife4jConfig
    └── OpenAPI
        ├── title: "RBAC 权限管理系统 API"
        └── SecurityScheme: JWT Bearer Token
```

---

## 六、Phase 4：数据源初始化

**6.1 MySQL 连接池初始化**

```
HikariPool (spring.datasource.hikari)
    ├── minimum-idle: 5
    ├── maximum-pool-size: 20
    ├── idle-timeout: 30000ms
    └── max-lifetime: 1800000ms
         │
         ▼
    连接 jdbc:mysql://localhost:3306/rbac_db
```

**6.2 执行数据库脚本**

`schema.sql` 建表：
```sql
-- 5张核心表
users          (用户表)
roles          (角色表)
menus          (菜单表)
user_roles     (用户-角色关联)
role_menus     (角色-菜单关联)
```

`data.sql` 初始化数据：
```sql
-- 2个用户
admin (密码: admin123, bcrypt加密)
user  (密码: admin123, bcrypt加密)

-- 2个角色
超级管理员 (admin)
普通用户   (user)

-- 16个菜单项 (含目录、菜单、按钮)
-- 用户关联：admin→超级管理员, user→普通用户
-- 菜单关联：管理员拥有所有权限，普通用户只有查看权限
```

**6.3 Redis 连接初始化**

```
LettuceClient (spring.data.redis)
    ├── host: localhost
    ├── port: 6379
    ├── database: 0
    └── timeout: 10000ms
```

---

## 七、Phase 5：Security 过滤器链配置

`SecurityConfig.java:34-54`

```
┌─────────────────────────────────────────────────────────────────┐
│                    Security 过滤器链                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  HTTP Request                                                   │
│       │                                                         │
│       ▼                                                         │
│  ┌──────────────────────────────────────────┐                  │
│  │ 1. CorsFilter (CorsConfig.java:16)      │                  │
│  │    - 允许所有来源                         │                  │
│  │    - 允许所有方法                         │                  │
│  └──────────────────────────────────────────┘                  │
│       │                                                         │
│       ▼                                                         │
│  ┌──────────────────────────────────────────┐                  │
│  │ 2. SecurityFilterChain                  │                  │
│  │    ├── CSRF: disabled                   │                  │
│  │    ├── Session: STATELESS               │                  │
│  │    ├── Public URLs:                      │                  │
│  │    │   ├── /api/auth/login              │                  │
│  │    │   ├── /api/auth/register           │                  │
│  │    │   ├── /doc.html                    │                  │
│  │    │   ├── /webjars/**                  │                  │
│  │    │   ├── /swagger-resources/**        │                  │
│  │    │   └── /v3/api-docs/**              │                  │
│  │    └── All other: authenticated         │                  │
│  └──────────────────────────────────────────┘                  │
│       │                                                         │
│       ▼                                                         │
│  ┌──────────────────────────────────────────┐                  │
│  │ 3. JwtAuthenticationFilter              │                  │
│  │    (在 UsernamePasswordAuthFilter 之前)  │                  │
│  │    (JwtAuthenticationFilter.java:34)     │                  │
│  └──────────────────────────────────────────┘                  │
│       │                                                         │
│       ▼                                                         │
│  Controller / Resource                                          │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

**关键配置说明：**

1. **CSRF 禁用**：`SecurityConfig.java:37`
   - REST API 不需要 CSRF 保护

2. **无状态会话**：`SecurityConfig.java:38`
   - `SessionCreationPolicy.STATELESS`：不创建 HTTP Session

3. **公开 URL**：`SecurityConfig.java:39-48`
   - 登录、注册接口无需认证
   - Knife4j 文档接口无需认证

4. **JWT 过滤器**：`SecurityConfig.java:51`
   - 在 `UsernamePasswordAuthenticationFilter` 之前执行
   - 从请求头提取 JWT Token 并验证

---

## 八、Phase 6：Web 容器启动与服务就绪

```
┌─────────────────────────────────────────────────────────────────┐
│                    Tomcat 容器启动                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. Tomcat 初始化                                               │
│     └── port: 8080, context-path: /                            │
│                                                                 │
│  2. DispatcherServlet 注册                                      │
│     └── Spring MVC 前端控制器                                   │
│                                                                 │
│  3. HandlerMapping 注册                                        │
│     ├── AuthController                                          │
│     │   ├── POST /api/auth/login                               │
│     │   ├── POST /api/auth/logout                              │
│     │   └── POST /api/auth/register                            │
│     ├── UserController                                          │
│     ├── RoleController                                          │
│     ├── MenuController                                          │
│     └── CurrentUserController                                   │
│                                                                 │
│  4. Knife4j 静态资源注册                                       │
│     ├── /doc.html (Knife4j UI)                                 │
│     └── /v3/api-docs (OpenAPI spec)                            │
│                                                                 │
│  5. Tomcat 启动完成                                             │
│     └── "Tomcat started on port 8080"                          │
│                                                                 │
│  6. Spring Boot 输出                                            │
│     └── "Started RbacApplication in X.XXX seconds"             │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 九、完整启动时序图

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                    RBAC 系统启动时序图                                           │
└─────────────────────────────────────────────────────────────────────────────────┘

  JVM                    SpringApplication           ApplicationContext
   │                           │                              │
   │  main(args)               │                              │
   │──────────────────────────▶│                              │
   │                           │  初始化                      │
   │                           │  ├── webApplicationType      │
   │                           │  ├── environment             │
   │                           │  └── initializers/listeners  │
   │                           │                              │
   │                           │  refresh()                   │
   │                           │─────────────────────────────▶│
   │                           │                              │
   │                           │   ┌──────────────────────────┤
   │                           │   │  组件扫描 (Phase 2)       │
   │                           │   │  @SpringBootApplication  │
   │                           │   │  @MapperScan             │
   │                           │   │                          │
   │                           │   │  发现:                    │
   │                           │   │  ├── 5个 @Configuration   │
   │                           │   │  ├── 2个 @Component       │
   │                           │   │  ├── 4个 @Service         │
   │                           │   │  ├── 5个 @RestController  │
   │                           │   │  └── 5个 @Mapper          │
   │                           │   └──────────────────────────┤
   │                           │                              │
   │                           │   ┌──────────────────────────┤
   │                           │   │  Bean 创建 (Phase 3)     │
   │                           │   │                          │
   │                           │   │  基础设施:                 │
   │                           │   │  ├── PasswordEncoder      │
   │                           │   │  ├── AuthenticationManager│
   │                           │   │  ├── JwtTokenProvider     │
   │                           │   │  └── JwtAuthFilter        │
   │                           │   │                          │
   │                           │   │  配置:                    │
   │                           │   │  ├── SecurityConfig       │
   │                           │   │  ├── CorsConfig           │
   │                           │   │  ├── RedisConfig          │
   │                           │   │  ├── MybatisPlusConfig    │
   │                           │   │  └── Knife4jConfig        │
   │                           │   │                          │
   │                           │   │  服务:                    │
   │                           │   │  ├── UserDetailsService   │
   │                           │   │  ├── UserService          │
   │                           │   │  ├── RoleService          │
   │                           │   │  └── MenuService          │
   │                           │   │                          │
   │                           │   │  控制器:                  │
   │                           │   │  ├── AuthController       │
   │                           │   │  ├── UserController       │
   │                           │   │  ├── RoleController       │
   │                           │   │  ├── MenuController       │
   │                           │   │  └── CurrentUserController│
   │                           │   └──────────────────────────┤
   │                           │                              │
   │                           │   ┌──────────────────────────┤
   │                           │   │  数据源 (Phase 4)        │
   │                           │   │                          │
   │                           │   │  MySQL:                   │
   │                           │   │  ├── HikariCP连接池       │
   │                           │   │  ├── 执行 schema.sql     │
   │                           │   │  └── 执行 data.sql       │
   │                           │   │                          │
   │                           │   │  Redis:                   │
   │                           │   │  └── Lettuce连接          │
   │                           │   └──────────────────────────┤
   │                           │                              │
   │                           │   ┌──────────────────────────┤
   │                           │   │  Security配置 (Phase 5)  │
   │                           │   │                          │
   │                           │   │  SecurityFilterChain:    │
   │                           │   │  ├── CSRF: disabled       │
   │                           │   │  ├── Session: STATELESS   │
   │                           │   │  ├── Public URLs          │
   │                           │   │  └── JwtAuthFilter        │
   │                           │   └──────────────────────────┤
   │                           │                              │
   │                           │   ┌──────────────────────────┤
   │                           │   │  Web容器 (Phase 6)       │
   │                           │   │                          │
   │                           │   │  Tomcat:                 │
   │                           │   │  ├── port: 8080          │
   │                           │   │  ├── DispatcherServlet   │
   │                           │   │  ├── HandlerMappings     │
   │                           │   │  └── Knife4j资源         │
   │                           │   └──────────────────────────┤
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

## 十、请求处理流程（以登录为例）

启动后，当收到 HTTP 请求时：

```
┌─────────────────────────────────────────────────────────────────┐
│                    请求处理时序 (以登录为例)                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Client                  Server                                │
│   │                       │                                    │
│   │  POST /api/auth/login │                                    │
│   │  {username, password} │                                    │
│   │──────────────────────▶│                                    │
│   │                       │                                    │
│   │                       │  1. CorsFilter                     │
│   │                       │     └── 跨域处理                   │
│   │                       │                                    │
│   │                       │  2. SecurityFilterChain            │
│   │                       │     ├── 匹配 permitAll URL         │
│   │                       │     └── 放行 (不需要认证)          │
│   │                       │                                    │
│   │                       │  3. JwtAuthenticationFilter        │
│   │                       │     └── 无token, 放行              │
│   │                       │                                    │
│   │                       │  4. AuthController.login()         │
│   │                       │     └── UserServiceImpl.login()    │
│   │                       │         ├── authenticationManager  │
│   │                       │         │   .authenticate()        │
│   │                       │         │   └── 调用UserDetailsService│
│   │                       │         │       .loadUserByUsername()│
│   │                       │         │       └── UserMapper查询  │
│   │                       │         │           ├── selectByUsername()
│   │                       │         │           └── selectPermissions()
│   │                       │         │                           │
│   │                       │         ├── JwtTokenProvider        │
│   │                       │         │   .generateToken()       │
│   │                       │         │                           │
│   │                       │         └── Redis存储token          │
│   │                       │             .set("token:xxx","1")  │
│   │                       │                                    │
│   │  Response:            │                                    │
│   │  {                   │                                    │
│   │    "code": 200,      │                                    │
│   │    "data": {         │                                    │
│   │      "token": "eyJhbG",│                                  │
│   │      "userInfo": {...}│                                    │
│   │    }                 │                                    │
│   │  }                   │                                    │
│   │◀──────────────────────│                                    │
│   │                       │                                    │
└─────────────────────────────────────────────────────────────────┘
```

**登录流程详解：**

1. **请求进入**：`AuthController.java:29`
   - 接收 `LoginRequest`（username, password）

2. **认证管理器**：`UserServiceImpl.java:59-61`
   - `authenticationManager.authenticate()` 验证用户
   - 调用 `UserDetailsServiceImpl.loadUserByUsername()`

3. **用户详情加载**：`UserDetailsServiceImpl.java:25-47`
   - `UserMapper.selectByUsername()` 查询用户
   - `UserMapper.selectPermissionsByUserId()` 查询权限

4. **生成 Token**：`JwtTokenProvider.java:30-41`
   - 使用 JWT 生成令牌
   - 包含用户名、签发时间、过期时间

5. **存储 Token**：`UserServiceImpl.java:91-95`
   - Redis 存储：`token:{token}` → `{userId}`
   - 过期时间：24小时

6. **返回响应**：`AuthController.java:32`
   - 包含 token 和用户信息

---

## 附录：核心配置文件

### application.yml

```yaml
server:
  port: 8080
  servlet:
    context-path: /

spring:
  profiles:
    active: dev
  application:
    name: rbac
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
    default-property-inclusion: non_null

mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      id-type: auto
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0

springdoc:
  swagger-ui:
    path: /swagger-ui.html
  api-docs:
    path: /v3/api-docs
knife4j:
  enable: true
  setting:
    language: zh_cn
```

### application-dev.yml

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/rbac_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: rU^c*sM_SL1.ye
    hikari:
      minimum-idle: 5
      maximum-pool-size: 20
      idle-timeout: 30000
      max-lifetime: 1800000
      connection-timeout: 30000
      connection-test-query: SELECT 1
  
  data:
    redis:
      host: localhost
      port: 6379
      password:
      database: 0
      timeout: 10000
      lettuce:
        pool:
          max-active: 8
          max-idle: 8
          min-idle: 0
          max-wait: -1ms

jwt:
  secret: rbac-secret-key-must-be-at-least-256-bits-long-for-hs256
  expiration: 86400000
  header: Authorization
  prefix: "Bearer "

logging:
  level:
    com.rc.rbac: debug
    org.springframework.security: debug
```

---

## 附录：数据库表结构

### users (用户表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 用户ID，自增主键 |
| username | VARCHAR(50) | 用户名，唯一 |
| password | VARCHAR(100) | 密码，BCrypt加密 |
| nickname | VARCHAR(50) | 昵称 |
| email | VARCHAR(100) | 邮箱 |
| phone | VARCHAR(20) | 手机号 |
| avatar | VARCHAR(255) | 头像地址 |
| status | TINYINT | 状态 0:禁用 1:启用 |
| deleted | TINYINT | 删除标记 0:未删除 1:已删除 |
| create_by | VARCHAR(50) | 创建者 |
| create_time | DATETIME | 创建时间 |
| update_by | VARCHAR(50) | 更新者 |
| update_time | DATETIME | 更新时间 |

### roles (角色表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 角色ID，自增主键 |
| role_name | VARCHAR(50) | 角色名称 |
| role_key | VARCHAR(50) | 角色标识，唯一 |
| description | VARCHAR(200) | 描述 |
| status | TINYINT | 状态 0:禁用 1:启用 |
| deleted | TINYINT | 删除标记 |
| create_by | VARCHAR(50) | 创建者 |
| create_time | DATETIME | 创建时间 |
| update_by | VARCHAR(50) | 更新者 |
| update_time | DATETIME | 更新时间 |

### menus (菜单表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 菜单ID，自增主键 |
| menu_name | VARCHAR(50) | 菜单名称 |
| parent_id | BIGINT | 父菜单ID，默认0 |
| path | VARCHAR(200) | 路由路径 |
| component | VARCHAR(200) | 组件路径 |
| redirect | VARCHAR(200) | 重定向地址 |
| icon | VARCHAR(50) | 图标 |
| title | VARCHAR(50) | 显示标题 |
| hidden | TINYINT | 是否隐藏 0:显示 1:隐藏 |
| keep_alive | TINYINT | 是否缓存 0:不缓存 1:缓存 |
| permission | VARCHAR(100) | 权限标识 |
| type | CHAR(1) | 类型 D:目录 M:菜单 B:按钮 |
| sort | INT | 排序 |
| status | TINYINT | 状态 0:禁用 1:启用 |
| deleted | TINYINT | 删除标记 |
| create_by | VARCHAR(50) | 创建者 |
| create_time | DATETIME | 创建时间 |
| update_by | VARCHAR(50) | 更新者 |
| update_time | DATETIME | 更新时间 |

### user_roles (用户-角色关联表)

| 字段 | 类型 | 说明 |
|------|------|------|
| user_id | BIGINT | 用户ID，外键 |
| role_id | BIGINT | 角色ID，外键 |

### role_menus (角色-菜单关联表)

| 字段 | 类型 | 说明 |
|------|------|------|
| role_id | BIGINT | 角色ID，外键 |
| menu_id | BIGINT | 菜单ID，外键 |
