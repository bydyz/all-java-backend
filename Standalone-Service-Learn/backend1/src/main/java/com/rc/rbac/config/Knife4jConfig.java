package com.rc.rbac.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Knife4j 配置
 */
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
                .servers(List.of(new Server().url("/").description("本地服务")))
                .schemaRequirement("Authorization",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                .addSecurityItem(new SecurityRequirement().addList("Authorization"));
    }

    /**
     * Knife4j 只认接口（Operation）级别的 security，文档级的全局 security 不会被继承到调试面板。
     * 这里在最终生成的每个 Operation 上显式添加 security 引用，使 Authorize 鉴权对全部请求生效。
     */
    @Bean
    public GlobalOpenApiCustomizer globalSecurity() {
        return openApi -> {
            if (openApi.getPaths() != null) {
                openApi.getPaths().forEach((path, pathItem) ->
                        pathItem.readOperations().forEach(operation ->
                                operation.addSecurityItem(new SecurityRequirement().addList("Authorization"))));
            }
        };
    }
}
