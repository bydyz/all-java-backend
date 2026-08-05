# Phase 6：Web 容器启动阶段

## 概述

Phase 6 是 Spring Boot 启动流程的第六个阶段，主要完成 Web 容器的启动。包括 Tomcat 初始化、DispatcherServlet 注册、HandlerMapping 注册、Knife4j 静态资源注册等。

---

## 6.1 Tomcat 初始化

### 6.1.1 Tomcat 配置

`application.yml:1-4`

```yaml
server:
  port: 8080
  servlet:
    context-path: /
```

### 6.1.2 Tomcat 初始化流程

```
┌─────────────────────────────────────────────────────────────────┐
│                    Tomcat 初始化流程                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. 创建 Tomcat 实例                                             │
│     └── new Tomcat()                                            │
│                                                                 │
│  2. 配置连接器 (Connector)                                       │
│     ├── port: 8080                                              │
│     ├── protocol: HTTP/1.1                                      │
│     └── redirectPort: 8443 (HTTPS)                              │
│                                                                 │
│  3. 配置服务 (Service)                                           │
│     └── name: Catalina                                          │
│                                                                 │
│  4. 配置主机 (Host)                                              │
│     ├── name: localhost                                         │
│     └── appBase: webapps                                        │
│                                                                 │
│  5. 配置上下文 (Context)                                         │
│     ├── path: /                                                 │
│     └── docBase: 应用路径                                        │
│                                                                 │
│  6. 启动 Tomcat                                                 │
│     └── tomcat.start()                                          │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 6.1.3 Tomcat 架构

```
┌─────────────────────────────────────────────────────────────────┐
│                    Tomcat 架构                                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Server                                                         │
│  └── Service                                                    │
│      ├── Connector (HTTP/1.1, port: 8080)                       │
│      │   └── 处理 HTTP 请求                                      │
│      │                                                          │
│      └── Engine (Catalina)                                      │
│          └── Host (localhost)                                   │
│              └── Context (/, 应用上下文)                          │
│                  ├── Wrapper (DispatcherServlet)                 │
│                  │   └── 处理请求                                 │
│                  │                                              │
│                  └── Resources                                 │
│                      └── 静态资源                               │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 6.2 DispatcherServlet 注册

### 6.2.1 DispatcherServlet 配置

Spring Boot 自动配置 `DispatcherServlet`：

```java
// Spring Boot 自动配置
@Bean
public DispatcherServlet dispatcherServlet() {
    return new DispatcherServlet();
}

@Bean
public ServletRegistrationBean<DispatcherServlet> dispatcherServletRegistration() {
    ServletRegistrationBean<DispatcherServlet> registration = new ServletRegistrationBean<>();
    registration.setServlet(dispatcherServlet());
    registration.addUrlMappings("/");
    registration.setName("dispatcherServlet");
    registration.setLoadOnStartup(1);
    return registration;
}
```

### 6.2.2 DispatcherServlet 作用

```
┌─────────────────────────────────────────────────────────────────┐
│                    DispatcherServlet 作用                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  DispatcherServlet 是 Spring MVC 的前端控制器                     │
│                                                                 │
│  职责：                                                         │
│  ├── 接收所有 HTTP 请求                                         │
│  ├── 根据请求 URL 查找 HandlerMapping                           │
│  ├── 调用 HandlerAdapter 执行处理器                              │
│  ├── 处理异常                                                   │
│  ├── 解析视图                                                   │
│  └── 返回响应                                                   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 6.2.3 DispatcherServlet 初始化流程

```
┌─────────────────────────────────────────────────────────────────┐
│                    DispatcherServlet 初始化流程                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. init()                                                      │
│     └── 初始化 Servlet                                           │
│                                                                 │
│  2. initStrategies(context)                                     │
│     ├── initMultipartResolver(context)                          │
│     ├── initLocaleResolver(context)                             │
│     ├── initThemeResolver(context)                              │
│     ├── initHandlerMappings(context)                            │
│     ├── initHandlerAdapters(context)                            │
│     ├── initHandlerExceptionResolvers(context)                  │
│     ├── initRequestToViewNameTranslator(context)                │
│     ├── initViewResolvers(context)                              │
│     └── initFlashMapManager(context)                            │
│                                                                 │
│  3. 初始化完成                                                   │
│     └── 准备处理请求                                             │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 6.3 HandlerMapping 注册

### 6.3.1 注册的 HandlerMapping

```
┌─────────────────────────────────────────────────────────────────┐
│                    HandlerMapping 注册                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. RequestMappingHandlerMapping                                 │
│     └── 处理 @RequestMapping 注解的处理器                        │
│                                                                 │
│  2. BeanNameUrlHandlerMapping                                    │
│     └── 处理 Bean 名称匹配的 URL                                 │
│                                                                 │
│  3. RouterFunctionMapping                                        │
│     └── 处理函数式端点                                            │
│                                                                 │
│  4. SimpleUrlHandlerMapping                                      │
│     └── 处理简单 URL 映射                                        │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 6.3.2 注册的 Handler（控制器）

```
┌─────────────────────────────────────────────────────────────────┐
│                    注册的 Handler 列表                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  AuthController                                                  │
│  ├── POST /api/auth/login                                       │
│  ├── POST /api/auth/logout                                      │
│  └── POST /api/auth/register                                    │
│                                                                 │
│  UserController                                                  │
│  ├── GET /api/user/page                                         │
│  ├── GET /api/user/{id}                                         │
│  ├── POST /api/user                                             │
│  ├── PUT /api/user/{id}                                         │
│  └── DELETE /api/user/{id}                                      │
│                                                                 │
│  RoleController                                                  │
│  ├── GET /api/role/page                                         │
│  ├── GET /api/role/{id}                                         │
│  ├── POST /api/role                                             │
│  ├── PUT /api/role/{id}                                         │
│  └── DELETE /api/role/{id}                                      │
│                                                                 │
│  MenuController                                                  │
│  ├── GET /api/menu/tree                                         │
│  ├── GET /api/menu/{id}                                         │
│  ├── POST /api/menu                                             │
│  ├── PUT /api/menu/{id}                                         │
│  └── DELETE /api/menu/{id}                                      │
│                                                                 │
│  CurrentUserController                                          │
│  ├── GET /api/current-user/info                                 │
│  └── GET /api/current-user/menus                                │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 6.3.3 HandlerMapping 路由表

| HTTP 方法 | URL 路径 | Handler |
|-----------|----------|---------|
| POST | /api/auth/login | AuthController.login() |
| POST | /api/auth/logout | AuthController.logout() |
| POST | /api/auth/register | AuthController.register() |
| GET | /api/user/page | UserController.getUserPage() |
| GET | /api/user/{id} | UserController.getUserById() |
| POST | /api/user | UserController.createUser() |
| PUT | /api/user/{id} | UserController.updateUser() |
| DELETE | /api/user/{id} | UserController.deleteUser() |
| GET | /api/role/page | RoleController.getRolePage() |
| GET | /api/role/{id} | RoleController.getRoleById() |
| POST | /api/role | RoleController.createRole() |
| PUT | /api/role/{id} | RoleController.updateRole() |
| DELETE | /api/role/{id} | RoleController.deleteRole() |
| GET | /api/menu/tree | MenuController.getMenuTree() |
| GET | /api/menu/{id} | MenuController.getMenuById() |
| POST | /api/menu | MenuController.createMenu() |
| PUT | /api/menu/{id} | MenuController.updateMenu() |
| DELETE | /api/menu/{id} | MenuController.deleteMenu() |
| GET | /api/current-user/info | CurrentUserController.getCurrentUser() |
| GET | /api/current-user/menus | CurrentUserController.getCurrentUserMenus() |

---

## 6.4 Knife4j 静态资源注册

### 6.4.1 Knife4j 配置

`Knife4jConfig.java:14-33`

```java
@Configuration
public class Knife4jConfig {
    
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RBAC 权限管理系统 API")
                        .description("基于 Spring Boot 3.5.16 的 RBAC 权限管理系统")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("RC")
                                .email("admin@example.com")))
                .schemaRequirement("Authorization",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                .addSecurityItem(new SecurityRequirement().addList("Authorization"));
    }
}
```

### 6.4.2 Knife4j 资源路径

```
┌─────────────────────────────────────────────────────────────────┐
│                    Knife4j 静态资源路径                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  /doc.html              → Knife4j UI 界面                       │
│  /webjars/**            → Knife4j 静态资源                      │
│  /swagger-resources/**  → Swagger 资源                          │
│  /v3/api-docs/**        → OpenAPI 文档                          │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 6.4.3 Knife4j 配置参数

`application.yml:28-37`

```yaml
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

| 配置项 | 值 | 说明 |
|--------|-----|------|
| `springdoc.swagger-ui.path` | /swagger-ui.html | Swagger UI 路径 |
| `springdoc.api-docs.path` | /v3/api-docs | OpenAPI 文档路径 |
| `knife4j.enable` | true | 启用 Knife4j |
| `knife4j.setting.language` | zh_cn | 中文界面 |

---

## 6.5 Web 容器启动时序图

```
┌─────────────────────────────────────────────────────────────────┐
│                    Phase 6 Web 容器启动时序图                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ApplicationContext        Tomcat              DispatcherServlet │
│        │                    │                        │          │
│        │  启动 Web 容器      │                        │          │
│        │───────────────────▶│                        │          │
│        │                    │                        │          │
│        │                    │  1. 初始化 Tomcat       │          │
│        │                    │     ├── Connector(8080)│          │
│        │                    │     ├── Service        │          │
│        │                    │     ├── Engine         │          │
│        │                    │     └── Host           │          │
│        │                    │                        │          │
│        │                    │  2. 注册 DispatcherServlet          │
│        │                    │───────────────────────▶│          │
│        │                    │                        │          │
│        │                    │                        │ 3. 初始化 │
│        │                    │                        │ Handler  │
│        │                    │                        │ Mappings │
│        │                    │                        │          │
│        │                    │  4. 注册 HandlerMappings            │
│        │                    │───────────────────────▶│          │
│        │                    │                        │          │
│        │                    │  5. 注册 Knife4j 资源   │          │
│        │                    │───────────────────────▶│          │
│        │                    │                        │          │
│        │                    │  6. 启动 Tomcat         │          │
│        │                    │     └── 监听端口 8080   │          │
│        │                    │                        │          │
│        │                    │  7. Tomcat 启动完成     │          │
│        │                    │◀───────────────────────│          │
│        │                    │                        │          │
│        │  服务启动完成       │                        │          │
│        │◀───────────────────│                        │          │
│        │                    │                        │          │
└─────────────────────────────────────────────────────────────────┘
```

---

## 6.6 服务启动完成

### 6.6.1 启动日志

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.5.16)

2026-08-05 23:58:00.000  INFO  --- [           main] com.rc.rbac.RbacApplication : Starting RbacApplication using Java 17.0.2 with PID 12345
2026-08-05 23:58:00.001  INFO  --- [           main] com.rc.rbac.RbacApplication : No active profile set, falling back to default profiles: default
2026-08-05 23:58:01.000  INFO  --- [           main] o.s.b.w.e.t.TomcatWebServer : Tomcat initialized with port(s): 8080 (http)
2026-08-05 23:58:01.001  INFO  --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/] : Initializing Spring embedded WebApplicationContext
2026-08-05 23:58:01.002  INFO  --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1000 ms
2026-08-05 23:58:02.000  INFO  --- [           main] o.s.b.w.e.t.TomcatWebServer : Tomcat started on port(s): 8080 (http) with context path ''
2026-08-05 23:58:02.001  INFO  --- [           main] com.rc.rbac.RbacApplication : Started RbacApplication in 2.001 seconds (process running for 2.500)
```

### 6.6.2 启动完成标志

```
┌─────────────────────────────────────────────────────────────────┐
│                    启动完成标志                                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ✓ Tomcat started on port(s): 8080 (http)                       │
│  ✓ Started RbacApplication in X.XXX seconds                     │
│                                                                 │
│  服务已就绪，可以处理请求！                                       │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 6.7 端口监听

### 6.7.1 端口配置

`application.yml:1-2`

```yaml
server:
  port: 8080
```

### 6.7.2 端口监听验证

```bash
# 查看端口监听状态
netstat -an | grep 8080

# 输出示例：
# TCP    0.0.0.0:8080           0.0.0.0:0              LISTENING
# TCP    [::]:8080              [::]:0                 LISTENING
```

### 6.7.3 服务访问验证

```bash
# 测试服务是否正常
curl http://localhost:8080/api/auth/login

# 输出示例：
# {"code":400,"message":"用户名或密码不能为空","data":null}
```

---

## 6.8 请求处理流程概览

```
┌─────────────────────────────────────────────────────────────────┐
│                    请求处理流程概览                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Client                                                         │
│    │                                                            │
│    │  HTTP Request                                              │
│    │──────────────────────────▶                                 │
│    │                                                            │
│    │                               ┌─────────────────────┐      │
│    │                               │ Tomcat (port: 8080) │      │
│    │                               └──────────┬──────────┘      │
│    │                                          │                 │
│    │                                          ▼                 │
│    │                               ┌─────────────────────┐      │
│    │                               │   CorsFilter       │      │
│    │                               └──────────┬──────────┘      │
│    │                                          │                 │
│    │                                          ▼                 │
│    │                               ┌─────────────────────┐      │
│    │                               │ SecurityFilterChain │      │
│    │                               └──────────┬──────────┘      │
│    │                                          │                 │
│    │                                          ▼                 │
│    │                               ┌─────────────────────┐      │
│    │                               │ JwtAuthFilter       │      │
│    │                               └──────────┬──────────┘      │
│    │                                          │                 │
│    │                                          ▼                 │
│    │                               ┌─────────────────────┐      │
│    │                               │ DispatcherServlet   │      │
│    │                               └──────────┬──────────┘      │
│    │                                          │                 │
│    │                                          ▼                 │
│    │                               ┌─────────────────────┐      │
│    │                               │ HandlerMapping      │      │
│    │                               └──────────┬──────────┘      │
│    │                                          │                 │
│    │                                          ▼                 │
│    │                               ┌─────────────────────┐      │
│    │                               │ Controller          │      │
│    │                               └──────────┬──────────┘      │
│    │                                          │                 │
│    │  HTTP Response                           │                 │
│    │◀──────────────────────────│                 │                 │
│    │                                                            │
└─────────────────────────────────────────────────────────────────┘
```

---

## 6.9 Web 容器启动完成标志

当以下条件满足时，Web 容器启动阶段完成：

- [x] Tomcat 初始化完成
- [x] DispatcherServlet 注册完成
- [x] HandlerMapping 注册完成
- [x] Knife4j 静态资源注册完成
- [x] Tomcat 启动完成
- [x] 端口 8080 监听完成
- [x] 服务启动完成

**下一步：** 服务已就绪，可以处理客户端请求！

---

## 6.10 常见问题

### Q1: 端口被占用怎么办？

**可能原因：**
1. 其他应用占用了 8080 端口
2. 之前的应用未正确关闭

**排查方法：**
```bash
# 查看端口占用
netstat -ano | findstr :8080

# 杀掉占用端口的进程
taskkill /PID <PID> /F
```

### Q2: 如何修改端口？

**方法一：** 修改 `application.yml`
```yaml
server:
  port: 9090
```

**方法二：** 命令行参数
```bash
java -jar app.jar --server.port=9090
```

### Q3: 如何启用 HTTPS？

**方法一：** 修改 `application.yml`
```yaml
server:
  port: 8443
  ssl:
    key-store: classpath:keystore.p12
    key-store-password: password
    key-store-type: PKCS12
```

**方法二：** 命令行参数
```bash
java -jar app.jar --server.port=8443 --server.ssl.key-store=classpath:keystore.p12 --server.ssl.key-store-password=password
```

---

## 6.11 下一步

Web 容器启动阶段完成后，服务已就绪，可以处理客户端请求！

- 查看完整启动时序图：[Phase 7：完整启动时序图](./Phase7-完整启动时序图.md)
- 查看请求处理流程：[Phase 8：请求处理流程](./Phase8-请求处理流程.md)
