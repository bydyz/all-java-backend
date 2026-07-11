package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 启动类
 * 
 * @SpringBootApplication 是一个组合注解，包含：
 *   1. @Configuration      - 标记为配置类
 *   2. @EnableAutoConfiguration - 启用自动配置
 *   3. @ComponentScan      - 组件扫描（扫描当前包及子包）
 */
@SpringBootApplication
public class SpringBootStarterParent3_5_16Application {

    /**
     * 主方法 - Spring Boot 应用入口
     * 
     * SpringApplication.run() 做了以下事情：
     *   1. 创建 Spring ApplicationContext
     *   2. 启动内嵌的 Web 服务器（Tomcat）
     *   3. 扫描并注册 Bean
     *   4. 启动应用
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringBootStarterParent3_5_16Application.class, args);
    }
}
