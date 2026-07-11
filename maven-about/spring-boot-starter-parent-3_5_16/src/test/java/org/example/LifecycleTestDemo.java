package org.example;

import org.example.entity.User;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 测试生命周期和执行顺序示例
 * 
 * JUnit 5 测试生命周期：
 *   1. @BeforeAll  - 所有测试之前（仅一次）
 *   2. @BeforeEach - 每个测试之前
 *   3. @Test       - 执行测试
 *   4. @AfterEach  - 每个测试之后
 *   5. @AfterAll   - 所有测试之后（仅一次）
 * 
 * 执行顺序：
 *   - 默认：方法名字母顺序
 *   - @Order - 指定顺序
 *   @TestMethodOrder - 指定排序策略
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)  // 使用 @Order 注解排序
class LifecycleTestDemo {

    /**
     * 共享的测试数据
     * 在整个测试类中共享
     */
    private static List<User> sharedUsers;

    private User currentUser;

    /**
     * @BeforeAll - 在所有测试之前执行一次
     * 必须是 static 方法
     * 
     * 用途：
     * - 数据库连接
     * - 加载配置
     * - 初始化共享资源
     */
    @BeforeAll
    static void beforeAll() {
        System.out.println("=== @BeforeAll: 初始化共享数据 ===");
        sharedUsers = new ArrayList<>();
        sharedUsers.add(new User("张三", "zhangsan@example.com", "13800138000"));
        sharedUsers.add(new User("李四", "lisi@example.com", "13900139000"));
        sharedUsers.add(new User("王五", "wangwu@example.com", "13700137000"));
    }

    /**
     * @BeforeEach - 在每个测试之前执行
     * 非 static 方法
     * 
     * 用途：
     * - 准备测试数据
     * - 重置状态
     * - 每个测试独立的数据
     */
    @BeforeEach
    void setUp() {
        System.out.println("=== @BeforeEach: 准备当前测试数据 ===");
        currentUser = new User("测试用户", "test@example.com", "13500135000");
    }

    /**
     * @AfterEach - 在每个测试之后执行
     * 
     * 用途：
     * - 清理测试数据
     * - 释放资源
     * - 重置状态
     */
    @AfterEach
    void tearDown() {
        System.out.println("=== @AfterEach: 清理当前测试数据 ===");
        currentUser = null;
    }

    /**
     * @AfterAll - 在所有测试之后执行一次
     * 必须是 static 方法
     * 
     * 用途：
     * - 关闭数据库连接
     * - 清理共享资源
     * - 生成测试报告
     */
    @AfterAll
    static void afterAll() {
        System.out.println("=== @AfterAll: 清理共享数据 ===");
        sharedUsers.clear();
        sharedUsers = null;
    }

    /**
     * @Order(1) - 指定执行顺序
     * 数字越小越先执行
     */
    @Test
    @Order(1)
    @DisplayName("第一个测试 - 验证共享数据")
    void testSharedData() {
        assertNotNull(sharedUsers, "共享数据不应为 null");
        assertEquals(3, sharedUsers.size(), "应该有3个用户");
        System.out.println("共享用户数量: " + sharedUsers.size());
    }

    @Test
    @Order(2)
    @DisplayName("第二个测试 - 验证当前用户初始化")
    void testCurrentUserInitialization() {
        assertNotNull(currentUser, "当前用户不应为 null");
        assertEquals("测试用户", currentUser.getName());
        System.out.println("当前用户: " + currentUser.getName());
    }

    @Test
    @Order(3)
    @DisplayName("第三个测试 - 添加用户到共享列表")
    void testAddUserToSharedList() {
        sharedUsers.add(currentUser);
        assertEquals(4, sharedUsers.size(), "添加后应该有4个用户");
        System.out.println("添加后用户数量: " + sharedUsers.size());
    }

    @Test
    @Order(4)
    @DisplayName("第四个测试 - 验证添加结果")
    void testVerifyAddResult() {
        // 注意：由于 @BeforeEach 重新初始化 currentUser
        // 这里需要检查 sharedUsers 中是否包含之前的用户
        assertTrue(sharedUsers.size() >= 4, "应该至少有4个用户");
    }

    /**
     * 测试异常场景下的生命周期
     */
    @Test
    @Order(5)
    @DisplayName("第五个测试 - 异常场景")
    void testExceptionScenario() {
        assertThrows(
            IllegalArgumentException.class,
            () -> {
                throw new IllegalArgumentException("测试异常");
            }
        );
        // 即使测试抛出异常，@AfterEach 仍然会执行
        System.out.println("异常测试完成");
    }

    /**
     * 测试超时
     */
    @Test
    @Order(6)
    @DisplayName("第六个测试 - 超时测试")
    void testTimeout() {
        assertTimeout(
            java.time.Duration.ofSeconds(1),
            () -> {
                // 模拟快速操作
                Thread.sleep(50);
                System.out.println("操作在 1 秒内完成");
            }
        );
    }
}
