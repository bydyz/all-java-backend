package org.rc;

import org.junit.jupiter.api.Test;
import org.rc.controller.HelloController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * HelloController 单元测试
 */
@SpringBootTest
class HelloControllerTest {
    
    @Autowired
    private HelloController helloController;
    
    @Test
    void testSayHello() {
        String result = helloController.sayHello();
        assertNotNull(result);
        assertEquals("Hello, Spring Boot Web Starter!", result);
    }
    
    @Test
    void testGreeting() {
        String result = helloController.greeting();
        assertNotNull(result);
        assertEquals("欢迎学习 Spring Boot Web Starter!", result);
    }
}
