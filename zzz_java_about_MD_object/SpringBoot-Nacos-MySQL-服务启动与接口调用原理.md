# 深度解析：Spring Boot + Nacos + MySQL 系统的服务启动与接口调用原理

---

## Step 1: 概念意义

### 一句话定义
Spring Boot + Nacos + MySQL 系统是一个**微服务架构**的典型组合：Spring Boot 提供应用框架和自动配置，Nacos 提供服务注册发现与配置管理，MySQL 提供持久化存储。

### 详细定义
- **Spring Boot**：简化 Spring 开发的框架，内嵌 Tomcat，通过**自动配置**和**起步依赖**实现"开箱即用"
- **Nacos**：阿里巴巴开源的**服务注册中心 + 配置中心**，管理微服务的动态发现和配置
- **MySQL**：关系型数据库，通过 JDBC + 连接池（如 HikariCP）接入 Spring Boot

### 解决的核心问题
- 服务如何自动发现彼此？→ Nacos
- 配置如何动态修改？→ Nacos Config
- 数据如何持久化？→ MySQL + JPA/MyBatis
- 如何快速搭建独立运行的服务？→ Spring Boot

---

## Step 2: 演进历程

| 时代 | 方案 | 问题 |
|------|------|------|
| 早期 | SSH/SSM + Tomcat 手动部署 | 配置繁琐、依赖冲突 |
| Spring Boot 时代 | Spring Boot + 外部配置中心 | 解决了配置繁琐，但服务地址硬编码 |
| 微服务时代 | Spring Boot + Eureka + MySQL | Netflix Eureka 维护停滞 |
| 当前主流 | Spring Boot + Nacos + MySQL | Nacos 集成了注册+配置，功能更强 |

---

## Step 3: 服务启动原理

### 3.1 整体启动流程

```
Spring Boot 启动入口 (main)
    │
    ├── 1. 创建 SpringApplication
    │   ├── 推断应用类型 (WebApplicationType)
    │   ├── 加载 ApplicationContextInitializer
    │   └── 加载 ApplicationListener
    │
    ├── 2. 启动 SpringApplication.run()
    │   ├── 2.1 监听器启动 (SpringApplicationRunListeners)
    │   ├── 2.2 准备 Environment (加载 application.yml / Nacos Config)
    │   ├── 2.3 创建 ApplicationContext
    │   ├── 2.4 准备 Context (BeanFactory 后置处理)
    │   ├── 2.5 刷新 Context (refresh) ← 核心
    │   │   ├── BeanFactory 初始化
    │   │   ├── 执行 BeanFactoryPostProcessor
    │   │   ├── 注册 BeanPostProcessor
    │   │   ├── 初始化 MessageSource
    │   │   ├── 初始化事件广播器
    │   │   ├── **内嵌 Web 容器启动 (Tomcat)**
    │   │   ├── 注册 Servlet、Filter
    │   │   └── **实例化所有单例 Bean** ← 关键
    │   │       ├── 数据源 Bean (DataSource → HikariCP)
    │   │       ├── MyBatis/JPA Bean (连接 MySQL)
    │   │       ├── **Nacos 注册 Bean (@EnableDiscoveryClient)**
    │   │       ├── Controller / Service / Repository
    │   │       └── ...
    │   └── 2.6 发布 ContextRefreshedEvent
    │
    └── 3. Nacos 注册完成，服务可用
```

### 3.2 Nacos 注册时机

当 `NacosAutoServiceRegistration` 监听到 `WebServerInitializedEvent`（内嵌 Tomcat 启动完成事件）时，向 Nacos Server 发送注册请求，上报：

- **服务名**：`spring.application.name`
- **IP + 端口**：本机地址 + `server.port`
- **元数据**：版本号、集群名等

```
Tomcat 启动 → 发布 WebServerInitializedEvent
    → NacosAutoServiceRegistration.onApplicationEvent()
    → NamingService.registerInstance(serviceName, ip, port)
    → Nacos Server 将服务信息写入其存储
```

### 3.3 MySQL 连接时机

Bean 实例化阶段，**数据源 Bean** 初始化时，HikariCP 连接池创建到 MySQL 的 TCP 连接：

```
DataSource Bean @Bean
    → HikariDataSource 初始化
    → HikariPool 构造函数
    → 创建与 MySQL 的物理连接
    → 连接池就绪
```

---

## Step 4: 接口调用原理

### 完整请求链路

```
外部请求
    │
    ├── 1. HTTP 请求到达
    │   └── 请求到达服务所在机器的 server.port (如 8080)
    │
    ├── 2. Tomcat 接收
    │   ├── Acceptor 线程 accept TCP 连接
    │   ├── 将 Socket 封装为 SocketWrapper
    │   └── 交给 Worker 线程处理
    │
    ├── 3. 进入 Servlet 容器
    │   ├── CoyoteAdapter.service() → 将请求转化为 Request/Response 对象
    │   └── 传递给 Engine → Host → Context → Wrapper
    │
    ├── 4. Filter 链
    │   ├── CharacterEncodingFilter（编码）
    │   ├── 自定义 Filter（鉴权、日志等）
    │   └── Spring Security Filter（如有）
    │
    ├── 5. DispatcherServlet 分发
    │   ├── HandlerMapping 查找匹配的 @RequestMapping
    │   │   └── 找到对应 Controller 方法
    │   ├── HandlerAdapter 执行前处理
    │   │   ├── 参数解析 (HandlerMethodArgumentResolver)
    │   │   │   └── @RequestParam、@RequestBody、@PathVariable 等
    │   │   ├── 数据绑定 & 校验 (@Valid)
    │   │   └── ...
    │   │
    │   ├── 6. Controller 方法执行
    │   │   ├── 6.1 @Aspect 切面（日志、事务等）
    │   │   ├── 6.2 Service 层（业务逻辑）
    │   │   │   ├── 远程调用其他微服务（如有）
    │   │   │   │   └── 从 Nacos 获取目标服务地址
    │   │   │   │   └── 通过 RestTemplate/Feign 发起 HTTP 调用
    │   │   │   └── 6.3 Repository / DAO 层
    │   │   │       ├── MyBatis Mapper / JPA Repository
    │   │   │       ├── 从 HikariCP 连接池获取 Connection
    │   │   │       ├── 执行 SQL 请求到 MySQL
    │   │   │       └── 返回结果 (ResultSet → Entity/DTO)
    │   │   │
    │   │   └── 6.4 返回响应数据
    │   │
    │   └── HandlerAdapter 执行后处理
    │       └── 返回值处理 (HandlerMethodReturnValueHandler)
    │           └── @ResponseBody → Jackson 序列化为 JSON
    │
    └── 7. 响应写出
        ├── Tomcat Response Buffer 写入 HTTP 响应
        └── 客户端收到 JSON/XML 响应
```

### 关键细节说明

#### 服务间调用（Feign / RestTemplate + Nacos）

```
ServiceA 调用 ServiceB
    → Feign 动态代理拦截调用
    → 从 Nacos 获取 serviceB 的可用实例列表 (IP:Port)
    → 负载均衡 (Ribbon / Spring Cloud LoadBalancer)
    → 发起 HTTP 请求到 ServiceB
    → ServiceB 重复上述 1-7 流程处理请求
```

#### 事务管理

```
@Transactional
    → 切面拦截 Service 方法
    → 从 DataSourceTransactionManager 获取 Connection
    → 设置 autoCommit = false
    → 执行业务逻辑 (多个 SQL)
    → 成功 → commit()
    → 失败 → rollback()
    → 归还 Connection 到连接池
```

---

## 概念卡片

```
┌──────────────────────────────────────────────────────────┐
│  系统: Spring Boot + Nacos + MySQL 微服务系统             │
├──────────────────────────────────────────────────────────┤
│  启动三阶段:                                             │
│  ① Spring Boot 容器初始化 + 自动配置                     │
│  ② Nacos 服务注册到注册中心                              │
│  ③ MySQL 连接池建立                                     │
├──────────────────────────────────────────────────────────┤
│  接口调用七阶段:                                         │
│  HTTP → Tomcat → Filter → DispatcherServlet             │
│  → Controller → Service/DAO → MySQL → JSON 响应         │
├──────────────────────────────────────────────────────────┤
│  核心特性: 自动配置 / 服务注册发现 / 配置中心 / 开箱即用 │
└──────────────────────────────────────────────────────────┘
```

---

*生成时间：2026-05-23*
