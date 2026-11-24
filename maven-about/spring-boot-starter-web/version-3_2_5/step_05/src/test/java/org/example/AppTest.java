package org.example;

// 1. 导入必要的类
import org.junit.jupiter.api.Test;          // 引入 @Test 注解
import static org.junit.jupiter.api.Assertions.assertEquals; // 引入断言方法

/**
 * Unit test for simple App.
 */
public class AppTest {

    // 3. @Test 注解告诉 JUnit 这是一个测试方法
    @Test
    void testAdd() {
        System.out.println("666");
    }
}
