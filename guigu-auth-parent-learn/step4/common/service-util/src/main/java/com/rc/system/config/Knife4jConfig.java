package com.rc.system.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;

/**
 * Knife4j (Swagger2) 配置类
 *
 * <p>访问地址（启动服务后）：http://localhost:端口号/doc.html</p>
 */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfig {

    @Bean
    public Docket adminApiConfig(){
        // 全局参数：为所有接口自动添加 token 请求头
        List<Parameter> pars = new ArrayList<>();
        ParameterBuilder tokenPar = new ParameterBuilder();
        tokenPar.name("token")
                .description("用户token")
                .defaultValue("")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        pars.add(tokenPar.build());

        // 创建 Docket：每个 Docket 对应一个分组，可配置扫描路径和路径过滤规则
        Docket adminApi = new Docket(DocumentationType.SWAGGER_2)
                .groupName("adminApi")                              // 分组名称，在 Knife4j 下拉菜单中显示
                .apiInfo(adminApiInfo())                            // API 文档基本信息（标题、描述、版本等）
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.rc")) // 扫描指定包下的接口
                .paths(PathSelectors.regex("/admin/.*"))            // 只匹配 /admin/ 开头的路径
                .build()
                .globalOperationParameters(pars);                   // 为所有接口添加全局参数
        return adminApi;
    }

    private ApiInfo adminApiInfo(){

        // 使用 ApiInfoBuilder 构建了一个 ApiInfo 对象，设置了 API 文档的标题、描述、版本和联系人信息。
        return new ApiInfoBuilder()
                .title("后台管理系统-API文档")
                .description("本文档描述了后台管理系统微服务接口定义")
                .version("1.0")
                .contact(new Contact("rc", "http://one.rc.com", "bydyz98@163.com"))
                .build();
    }

}

