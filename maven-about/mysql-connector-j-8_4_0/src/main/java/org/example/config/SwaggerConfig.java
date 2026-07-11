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