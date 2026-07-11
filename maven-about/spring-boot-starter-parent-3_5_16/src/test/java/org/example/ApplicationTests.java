package org.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Spring Boot 启动测试
 * 
 * @SpringBootTest 会：
 *   1. 启动完整的 Spring ApplicationContext
 *   2. 启动内嵌 Web 服务器
 *   3. 扫描并注册所有 Bean
 * 
 * 这是一个集成测试，验证 Spring Boot 应用能否正常启动
 */
@SpringBootTest
class ApplicationTests {

    /**
     * 注入 Spring ApplicationContext
     * 用于验证 Spring 容器是否正常工作
     */
    @Autowired
    private ApplicationContext context;

    /**
     * 测试 Spring 上下文是否能正常加载
     * 如果应用配置有误，这个测试会失败
     */
    @Test
    void contextLoads() {
        assertNotNull(context, "Spring ApplicationContext 不应为 null");
    }

    /**
     * 测试 Bean 是否存在
     */
    @Test
    void testBeansExist() {
        // 验证 UserRepository 被注册
        assertTrue(context.containsBean("userRepository"), 
            "userRepository Bean 应该存在");
        
        // 验证 UserService 被注册
        assertTrue(context.containsBean("userService"), 
            "userService Bean 应该存在");
        
        // 验证 UserController 被注册
        assertTrue(context.containsBean("userController"), 
            "userController Bean 应该存在");
    }
}
