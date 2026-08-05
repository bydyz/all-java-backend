# Phase 1：准备阶段

## 概述

Phase 1 是 Spring Boot 启动流程的第一个阶段，主要完成 `SpringApplication` 的初始化工作。这个阶段在 `SpringApplication.run()` 方法被调用后立即执行，为后续的组件扫描、Bean 创建等阶段做好准备。

---

## 1.1 启动入口：main 方法

`RbacApplication.java:14-16`

```java
public static void main(String[] args) {
    SpringApplication.run(RbacApplication.class, args);
}
```

### 执行流程

```
┌─────────────────────────────────────────────────────────────────┐
│                    main 方法执行流程                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. JVM 启动                                                    │
│     └── 加载 RbacApplication 类                                 │
│                                                                 │
│  2. 执行 main(args)                                             │
│     └── 调用 SpringApplication.run()                            │
│                                                                 │
│  3. SpringApplication.run() 返回                                │
│     └── 应用启动完成，监听端口 8080                               │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 注解说明

`RbacApplication.java:10-11`

```java
@SpringBootApplication
@MapperScan("com.rc.rbac.mapper")
```

| 注解 | 作用 | 说明 |
|------|------|------|
| `@SpringBootApplication` | 组合注解 | 包含 `@SpringBootConfiguration`、`@EnableAutoConfiguration`、`@ComponentScan` |
| `@MapperScan("com.rc.rbac.mapper")` | MyBatis Mapper 扫描 | 扫描指定包下的 Mapper 接口 |

---

## 1.2 准备环境（Environment）

`SpringApplication.run()` 首先创建并配置 `Environment` 对象，用于存储所有配置信息。

### 1.2.1 加载配置文件

系统按以下顺序加载配置文件：

```
┌─────────────────────────────────────────────────────────────────┐
│                    配置文件加载顺序                                │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. application.yml                                             │
│     └── 基础配置（端口、应用名等）                                 │
│                                                                 │
│  2. application-dev.yml                                         │
│     └── 开发环境配置（数据库、Redis 等）                           │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 1.2.2 application.yml 配置

`application.yml:1-37`

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

# MyBatis Plus 配置
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

# Knife4j 配置
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

### 1.2.3 application-dev.yml 配置

`application-dev.yml:1-42`

```yaml
spring:
  # 数据源配置
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
  
  # Redis 配置
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

# JWT 配置
jwt:
  secret: rbac-secret-key-must-be-at-least-256-bits-long-for-hs256
  expiration: 86400000  # 24小时 (毫秒)
  header: Authorization
  prefix: "Bearer "

# 日志配置
logging:
  level:
    com.rc.rbac: debug
    org.springframework.security: debug
```

### 1.2.4 Profile 激活机制

`application.yml:7-8`

```yaml
spring:
  profiles:
    active: dev
```

**Profile 激活流程：**

```
┌─────────────────────────────────────────────────────────────────┐
│                    Profile 激活机制                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. 读取 spring.profiles.active                                 │
│     └── 值: "dev"                                               │
│                                                                 │
│  2. 加载 application-dev.yml                                     │
│     └── 覆盖/补充 application.yml 中的配置                       │
│                                                                 │
│  3. 最终配置生效                                                 │
│     ├── server.port = 8080                                      │
│     ├── spring.datasource.url = jdbc:mysql://...                │
│     ├── spring.data.redis.host = localhost                      │
│     └── jwt.secret = rbac-secret-key-...                        │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 1.3 初始化 SpringApplication

`SpringApplication.run()` 内部会创建 `SpringApplication` 实例并进行初始化。

### 1.3.1 设置 Web 应用类型

```java
// Spring Boot 自动检测应用类型
// 由于存在 spring-boot-starter-web 依赖，设置为 SERVLET
private WebApplicationType webApplicationType;
```

**应用类型判断：**

| 类型 | 条件 | 说明 |
|------|------|------|
| `SERVLET` | 存在 `DispatcherServlet` | 传统 Web 应用（本项目） |
| `REACTIVE` | 存在 `DispatcherHandler` | 响应式 Web 应用 |
| `NONE` | 无 Web 相关依赖 | 非 Web 应用 |

### 1.3.2 设置初始化器（Initializers）

```java
// ApplicationContextInitializer 列表
// 用于在 ApplicationContext 刷新之前对其进行配置
private List<ApplicationContextInitializer<?>> initializers;
```

**主要初始化器：**

```
┌─────────────────────────────────────────────────────────────────┐
│                    ApplicationContextInitializer                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. ConfigurationWarningsApplicationContextInitializer          │
│     └── 警告配置问题（如重复 Bean 定义）                          │
│                                                                 │
│  2. ServerPortInfoApplicationContextInitializer                 │
│     └── 将服务器端口绑定到 Environment                            │
│                                                                 │
│  3. SharedMetadataReaderFactoryContextInitializer                │
│     └── 共享元数据读取器，加速组件扫描                             │
│                                                                 │
│  4. ConditionEvaluationReport                                   │
│     └── 条件评估报告                                             │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 1.3.3 设置监听器（Listeners）

```java
// ApplicationListener 列表
// 用于监听 ApplicationEvent
private List<ApplicationListener<?>> listeners;
```

**主要监听器：**

```
┌─────────────────────────────────────────────────────────────────┐
│                    ApplicationListener                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. LoggingApplicationListener                                  │
│     └── 配置日志系统                                             │
│                                                                 │
│  2. ConfigFileApplicationListener                               │
│     └── 加载 application.yml 配置文件                            │
│                                                                 │
│  3. AnsiBackgroundApplicationListener                           │
│     └── 配置 ANSI 背景色                                        │
│                                                                 │
│  4. EnvironmentPostProcessorApplicationListener                 │
│     └── 后处理 Environment                                      │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 1.4 准备阶段时序图

```
┌─────────────────────────────────────────────────────────────────┐
│                    Phase 1 准备阶段时序图                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  main()                SpringApplication          Environment   │
│    │                        │                         │         │
│    │  SpringApplication     │                         │         │
│    │  .run(args)            │                         │         │
│    │───────────────────────▶│                         │         │
│    │                        │                         │         │
│    │                        │  1. 记录启动时间          │         │
│    │                        │                         │         │
│    │                        │  2. 创建 Environment    │         │
│    │                        │────────────────────────▶│         │
│    │                        │                         │         │
│    │                        │  3. 加载配置文件          │         │
│    │                        │     ├── application.yml │         │
│    │                        │     └── application-    │         │
│    │                        │         dev.yml         │         │
│    │                        │────────────────────────▶│         │
│    │                        │                         │         │
│    │                        │  4. 设置 webApplication │         │
│    │                        │     Type = SERVLET      │         │
│    │                        │                         │         │
│    │                        │  5. 设置 initializers   │         │
│    │                        │                         │         │
│    │                        │  6. 设置 listeners      │         │
│    │                        │                         │         │
│    │                        │  准备阶段完成            │         │
│    │                        │─────────────────────────│         │
│    │                        │                         │         │
│    │                        │  进入 Phase 2           │         │
│    │                        │  (组件扫描)              │         │
│    │                        │                         │         │
└─────────────────────────────────────────────────────────────────┘
```

---

## 1.5 配置项解析

### 1.5.1 服务器配置

| 配置项 | 值 | 说明 |
|--------|-----|------|
| `server.port` | 8080 | HTTP 监听端口 |
| `server.servlet.context-path` | / | 应用上下文路径 |

### 1.5.2 数据库配置

| 配置项 | 值 | 说明 |
|--------|-----|------|
| `spring.datasource.url` | jdbc:mysql://localhost:3306/rbac_db | 数据库连接 URL |
| `spring.datasource.username` | root | 数据库用户名 |
| `spring.datasource.hikari.minimum-idle` | 5 | 最小空闲连接数 |
| `spring.datasource.hikari.maximum-pool-size` | 20 | 最大连接数 |

### 1.5.3 Redis 配置

| 配置项 | 值 | 说明 |
|--------|-----|------|
| `spring.data.redis.host` | localhost | Redis 主机 |
| `spring.data.redis.port` | 6379 | Redis 端口 |
| `spring.data.redis.database` | 0 | 数据库编号 |

### 1.5.4 JWT 配置

| 配置项 | 值 | 说明 |
|--------|-----|------|
| `jwt.secret` | rbac-secret-key-... | JWT 签名密钥 |
| `jwt.expiration` | 86400000 | Token 过期时间（24小时） |
| `jwt.header` | Authorization | Token 请求头 |

---

## 1.6 准备阶段关键点

### 1.6.1 配置优先级

```
┌─────────────────────────────────────────────────────────────────┐
│                    配置优先级（从高到低）                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. 命令行参数                                                   │
│     └── --server.port=9090                                      │
│                                                                 │
│  2. Java 系统属性                                                │
│     └── -Dserver.port=9090                                      │
│                                                                 │
│  3. 操作系统环境变量                                              │
│     └── SERVER_PORT=9090                                        │
│                                                                 │
│  4. application-{profile}.yml                                   │
│     └── application-dev.yml                                     │
│                                                                 │
│  5. application.yml                                             │
│     └── 基础配置                                                 │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 1.6.2 配置文件加载顺序

```
┌─────────────────────────────────────────────────────────────────┐
│                    配置文件加载顺序                                │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  classpath:                                                     │
│  ├── application.yml                                            │
│  ├── application-{profile}.yml                                  │
│  └── application-{profile}.properties                           │
│                                                                 │
│  file:                                                          │
│  ├── ./application.yml                                          │
│  └── ./application-{profile}.yml                                │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 1.7 准备阶段完成标志

当以下条件满足时，准备阶段完成：

- [x] `Environment` 对象创建完成
- [x] 配置文件加载完成（application.yml + application-dev.yml）
- [x] `WebApplicationType` 设置为 `SERVLET`
- [x] `ApplicationContextInitializer` 列表设置完成
- [x] `ApplicationListener` 列表设置完成

**下一步：** 进入 Phase 2（组件扫描阶段），开始扫描并解析所有组件。

---

## 1.8 常见问题

### Q1: 为什么配置文件没有生效？

**可能原因：**
1. Profile 未正确激活
2. 配置文件路径不正确
3. 配置项拼写错误

**排查方法：**
```bash
# 检查激活的 Profile
java -jar app.jar --debug | grep "Active profiles"

# 检查配置加载情况
java -jar app.jar --debug | grep "Loaded"
```

### Q2: 如何修改默认端口？

**方法一：** 修改 `application.yml`
```yaml
server:
  port: 9090
```

**方法二：** 命令行参数
```bash
java -jar app.jar --server.port=9090
```

**方法三：** 环境变量
```bash
export SERVER_PORT=9090
java -jar app.jar
```

---

## 1.9 下一步

准备阶段完成后，Spring Boot 将进入 Phase 2（组件扫描阶段），开始：
1. 扫描 `com.rc.rbac.*` 包下的所有组件
2. 解析 `@Configuration`、`@Component`、`@Service`、`@RestController` 等注解
3. 生成 Bean 定义

详见 [Phase 2：组件扫描阶段](./Phase2-组件扫描阶段.md)
