# Spring Boot 配置 Swagger 指南

## 概述

Swagger 是一个 API 文档工具，可以自动生成 REST API 文档并提供在线测试界面。

本项目使用 **SpringDoc OpenAPI**（兼容 Spring Boot 3.x）。

---

## 配置步骤

### 1. 添加依赖

在 `pom.xml` 中添加：

```xml
<!-- SpringDoc OpenAPI (Swagger UI) -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.6</version>
</dependency>
```

### 2. 创建配置类

新建 `config/SwaggerConfig.java`：

```java
package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("用户管理 API")
                        .version("1.0")
                        .description("Spring Boot + MySQL 示例项目 API 文档")
                        .contact(new Contact()
                                .name("Admin")
                                .email("admin@example.com")));
    }
}
```

### 3. 启动服务

```bash
mvn spring-boot:run
```

---

## 访问地址

| 地址 | 说明 |
|------|------|
| http://localhost:8080/swagger-ui.html | Swagger UI 界面（推荐） |
| http://localhost:8080/swagger-ui/index.html | Swagger UI 界面（备用） |
| http://localhost:8080/v3/api-docs | OpenAPI 3.0 JSON 文档 |
| http://localhost:8080/v3/api-docs/swagger-config | Swagger 配置信息 |

---

## 使用说明

### 查看 API 文档

打开浏览器访问 `http://localhost:8080/swagger-ui.html`，可以看到：

- 所有 REST API 接口列表
- 每个接口的请求参数和响应格式
- 接口的请求方式（GET/POST/PUT/DELETE）

### 在线测试 API

1. 点击要测试的接口
2. 点击 `Try it out` 按钮
3. 填写请求参数
4. 点击 `Execute` 执行请求
5. 查看响应结果

### 导出 API 文档

- JSON 格式：访问 `/v3/api-docs`
- 可以使用 Swagger Codegen 生成客户端代码

---

## 常用配置项

### 修改接口路径

如果需要修改 Swagger 的访问路径，在 `application.properties` 中添加：

```properties
# Swagger UI 路径
springdoc.swagger-ui.path=/swagger-ui.html
# API 文档路径
springdoc.api-docs.path=/v3/api-docs
```

### 配置扫描范围

默认扫描所有 `@RestController` 注解的类。如需限制扫描范围：

```java
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API 文档")
                        .version("1.0"));
    }
}
```

### 添加认证配置

如需添加 API Key 或 OAuth2 认证：

```java
@Bean
public OpenAPI customOpenAPI() {
    return new OpenAPI()
            .info(new Info().title("API 文档").version("1.0"))
            .addSecurityItem(new SecurityRequirement().addList("Bearer"))
            .components(new Components()
                    .addSecuritySchemes("Bearer",
                            new SecurityScheme()
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT")));
}
```

---

## 依赖版本对照

| Spring Boot 版本 | 推荐 SpringDoc 版本 |
|------------------|---------------------|
| 3.x.x | 2.x.x |
| 2.x.x | 1.x.x |

---

## 常见问题

### 1. Swagger UI 页面空白

检查是否引入了正确的依赖，Spring Boot 3.x 必须使用 `springdoc-openapi-starter-webmvc-ui`。

### 2. 接口不显示

确保 Controller 类上有 `@RestController` 或 `@Controller` 注解。

### 3. 中文乱码

在配置类中添加：

```java
@Bean
public OpenAPI customOpenAPI() {
    return new OpenAPI()
            .info(new Info()
                    .title("API 文档")
                    .description("支持中文描述"));
}
```

---

## 项目结构

```
src/main/java/org/example/
├── config/
│   └── SwaggerConfig.java     # Swagger 配置类
├── controller/
│   └── UserController.java    # REST API 控制器
└── ...
```

---

## 参考链接

- SpringDoc 官方文档：https://springdoc.org/
- Swagger UI 官方文档：https://swagger.io/tools/swagger-ui/
- OpenAPI 规范：https://spec.openapis.org/oas/latest.html
