package org.example;

import org.example.entity.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 参数化测试示例
 * 
 * 参数化测试允许使用不同的输入参数多次运行同一测试
 * 
 * 常用注解：
 *   @ParameterizedTest    - 标记为参数化测试
 *   @ValueSource          - 提供简单值
 *   @CsvSource            - 提供 CSV 格式的多参数
 *   @CsvFileSource        - 从 CSV 文件读取参数
 *   @MethodSource         - 从方法提供参数
 *   @EnumSource           - 从枚举提供参数
 */
class ParameterizedTestDemo {

    /**
     * @ValueSource - 提供简单值
     * 每个值作为单独参数传入
     * 
     * types = String.class 指定参数类型
     */
    @ParameterizedTest
    @DisplayName("字符串长度测试 - ValueSource")
    @ValueSource(strings = {"张三", "李四", "王五"})
    void testStringLength(String name) {
        assertTrue(name.length() > 0, "名字不能为空");
        assertTrue(name.length() <= 2, "名字应该是2个字");
    }

    /**
     * @ValueSource - 整数参数
     */
    @ParameterizedTest
    @DisplayName("数字范围测试 - ValueSource")
    @ValueSource(ints = {1, 2, 3, 5, 8, 13})
    void testPositiveNumbers(int number) {
        assertTrue(number > 0, "数字应该是正数");
        assertTrue(number < 100, "数字应该小于100");
    }

    /**
     * @CsvSource - CSV 格式多参数
     * 每行是一个测试用例，逗号分隔参数
     * 
     * 第一列: 用户名
     * 第二列: 邮箱
     * 第三列: 预期的用户名长度
     */
    @ParameterizedTest
    @DisplayName("用户创建测试 - CsvSource")
    @CsvSource({
        "张三, zhangsan@example.com, 2",
        "李四, lisi@example.com, 2",
        "'张三丰', zhangsanfeng@example.com, 3"
    })
    void testUserCreationWithCsv(String name, String email, int expectedLength) {
        User user = new User(name, email, "13800138000");
        
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(expectedLength, user.getName().length());
    }

    /**
     * @CsvSource - 带引号的字符串和空值处理
     * 
     * quoteCharacter = '\'' 指定引号字符
     */
    @ParameterizedTest
    @DisplayName("字符串处理测试")
    @CsvSource({
        "Hello, World, 11",
        "'Java Spring', Boot, 15",
        "'Spring Boot', 3.x, 13"
    })
    void testStringProcessing(String first, String second, int expectedLength) {
        String result = first + " " + second;
        assertEquals(expectedLength, result.length());
    }

    /**
     * @MethodSource - 从静态方法提供参数
     * 方法返回 Stream、Iterable、Iterator 或数组
     */
    @ParameterizedTest
    @DisplayName("用户验证测试 - MethodSource")
    @MethodSource("validUserProvider")
    void testValidUsers(User user) {
        assertNotNull(user.getName(), "用户名不应为空");
        assertNotNull(user.getEmail(), "邮箱不应为空");
        assertTrue(user.getEmail().contains("@"), "邮箱格式应该正确");
    }

    /**
     * 提供测试数据的静态方法
     * 方法名需要与 @MethodSource 的值匹配
     */
    static Stream<User> validUserProvider() {
        return Stream.of(
            new User("张三", "zhangsan@example.com", "13800138000"),
            new User("李四", "lisi@example.com", "13900139000"),
            new User("王五", "wangwu@example.com", "13700137000")
        );
    }

    /**
     * @MethodSource - 多参数
     * 返回 Stream<Arguments>
     */
    @ParameterizedTest
    @DisplayName("字符串包含测试 - MethodSource 多参数")
    @MethodSource("stringProvider")
    void testStringContains(String text, String substring, boolean expected) {
        assertEquals(expected, text.contains(substring));
    }

    static Stream<Arguments> stringProvider() {
        return Stream.of(
            Arguments.of("Hello World", "World", true),
            Arguments.of("Hello World", "Java", false),
            Arguments.of("Spring Boot", "Spring", true)
        );
    }

    /**
     * @EnumSource - 从枚举值提供参数
     * 可以指定枚举类和可选的枚举值
     */
    @ParameterizedTest
    @DisplayName("用户状态测试 - EnumSource")
    @EnumSource(value = User.UserStatus.class, names = {"ACTIVE", "INACTIVE"})
    void testUserStatus(User.UserStatus status) {
        assertNotNull(status);
        assertNotEquals(User.UserStatus.DELETED, status);
    }

    /**
     * @CsvSource - 测试数学运算
     */
    @ParameterizedTest
    @DisplayName("数学运算测试")
    @CsvSource({
        "1, 2, 3",
        "5, 5, 10",
        "0, 0, 0",
        "-1, 1, 0"
    })
    void testAddition(int a, int b, int expected) {
        assertEquals(expected, a + b);
    }

    /**
     * @ValueSource - 测试字符串转换
     */
    @ParameterizedTest
    @DisplayName("字符串转换测试")
    @ValueSource(strings = {"123", "456", "789", "0"})
    void testStringToInteger(String numberStr) {
        // 不应抛出异常
        assertDoesNotThrow(() -> {
            int number = Integer.parseInt(numberStr);
            assertTrue(number >= 0, "数字应该是非负数");
        });
    }
}
