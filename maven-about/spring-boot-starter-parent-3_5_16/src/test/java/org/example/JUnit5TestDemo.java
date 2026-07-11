package org.example;

import org.example.entity.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 测试示例
 * 
 * JUnit 5 (Jupiter) 常用注解：
 *   @Test              - 标记测试方法
 *   @BeforeEach        - 每个测试方法前执行
 *   @AfterEach         - 每个测试方法后执行
 *   @BeforeAll         - 所有测试前执行（静态方法）
 *   @AfterAll          - 所有测试后执行（静态方法）
 *   @DisplayName       - 自定义测试显示名称
 *   @Disabled          - 禁用测试
 *   @Nested            - 嵌套测试类
 *   @Tag               - 标签分组
 */
class JUnit5TestDemo {

    private User testUser;

    /**
     * @BeforeAll - 在所有测试方法之前执行一次
     * 必须是 static 方法
     */
    @BeforeAll
    static void beforeAll() {
        System.out.println(">>> 所有测试开始前执行（仅一次）");
    }

    /**
     * @BeforeEach - 在每个测试方法之前执行
     * 用于初始化测试数据
     */
    @BeforeEach
    void setUp() {
        testUser = new User("张三", "zhangsan@example.com", "13800138000");
        testUser.setId(1L);
        System.out.println(">>> 初始化测试用户: " + testUser);
    }

    /**
     * @AfterEach - 在每个测试方法之后执行
     * 用于清理资源
     */
    @AfterEach
    void tearDown() {
        System.out.println(">>> 清理测试数据");
        testUser = null;
    }

    /**
     * @AfterAll - 在所有测试方法之后执行一次
     * 必须是 static 方法
     */
    @AfterAll
    static void afterAll() {
        System.out.println(">>> 所有测试完成后执行（仅一次）");
    }

    // ====== 基本断言测试 ======

    /**
     * @Test - 标记为测试方法
     * @DisplayName - 自定义测试显示名称
     */
    @Test
    @DisplayName("测试用户创建 - 基本断言")
    void testUserCreation() {
        // assertEquals - 验证两个值相等
        assertEquals("张三", testUser.getName(), "用户名应该是张三");
        assertEquals("zhangsan@example.com", testUser.getEmail());

        // assertNotEquals - 验证两个值不相等
        assertNotEquals("李四", testUser.getName());

        // assertNotNull - 验证对象不为 null
        assertNotNull(testUser.getId(), "用户ID不应为 null");
        assertNotNull(testUser.getName());
    }

    /**
     * 布尔断言
     */
    @Test
    @DisplayName("测试布尔断言")
    void testBooleanAssertions() {
        // assertTrue - 验证条件为 true
        assertTrue(testUser.getName().length() > 0, "用户名长度应该大于0");

        // assertFalse - 验证条件为 false
        assertFalse(testUser.getName().isEmpty(), "用户名不应为空");

        // assertSame / assertNotSame - 验证引用相同/不同
        User sameUser = testUser;
        assertSame(testUser, sameUser, "应该是同一个对象");
    }

    /**
     * 数组和集合断言
     */
    @Test
    @DisplayName("测试数组和集合断言")
    void testArrayAndCollectionAssertions() {
        String[] expected = {"张三", "李四"};
        String[] actual = {"张三", "李四"};

        // assertArrayEquals - 验证数组内容相等
        assertArrayEquals(expected, actual, "数组内容应该相同");

        // assertIterableEquals - 验证 Iterable 内容相等
        assertIterableEquals(
            java.util.Arrays.asList(expected), 
            java.util.Arrays.asList(actual)
        );
    }

    /**
     * 异常断言
     */
    @Test
    @DisplayName("测试异常断言")
    void testExceptionAssertions() {
        // assertThrows - 验证抛出指定异常
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> {
                throw new RuntimeException("测试异常");
            }
        );
        assertEquals("测试异常", exception.getMessage());

        // assertDoesNotThrow - 验证不抛出异常
        assertDoesNotThrow(() -> {
            // 正常操作，不应抛出异常
            String name = testUser.getName();
            assertNotNull(name);
        });
    }

    /**
     * 超时断言
     */
    @Test
    @DisplayName("测试超时断言")
    void testTimeout() {
        // assertTimeout - 验证操作在指定时间内完成
        assertTimeout(
            java.time.Duration.ofSeconds(1),
            () -> {
                // 模拟快速操作
                Thread.sleep(100);
                System.out.println("操作在 1 秒内完成");
            }
        );
    }

    // ====== 嵌套测试 ======

    /**
     * @Nested - 嵌套测试类，用于组织相关测试
     */
    @Nested
    @DisplayName("用户状态测试")
    class UserStatusTests {

        @Test
        @DisplayName("默认状态应该是 ACTIVE")
        void testDefaultStatus() {
            User newUser = new User();
            assertEquals(User.UserStatus.ACTIVE, newUser.getStatus());
        }

        @Test
        @DisplayName("可以修改用户状态")
        void testStatusChange() {
            testUser.setStatus(User.UserStatus.INACTIVE);
            assertEquals(User.UserStatus.INACTIVE, testUser.getStatus());
        }
    }

    // ====== 禁用测试 ======

    /**
     * @Disabled - 禁用测试，不执行
     * 可以添加原因说明
     */
    @Test
    @Disabled("此测试暂时跳过，待修复后启用")
    @DisplayName("跳过的测试示例")
    void skippedTest() {
        // 这个测试不会被执行
        fail("这个测试不应该被执行");
    }
}
