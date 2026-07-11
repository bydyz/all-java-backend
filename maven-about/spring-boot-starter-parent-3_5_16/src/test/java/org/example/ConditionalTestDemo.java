package org.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 条件测试示例
 * 
 * 条件测试允许根据环境条件决定是否执行测试
 * 
 * 常用注解：
 *   @EnabledOnOs        - 操作系统条件
 *   @DisabledOnOs       - 禁用的操作系统
 *   @EnabledOnJre       - Java 版本条件
 *   @DisabledOnJre      - 禁用的 Java 版本
 *   @EnabledIfSystemProperty - 系统属性条件
 *   @EnabledIfEnvironmentVariable - 环境变量条件
 */
class ConditionalTestDemo {

    /**
     * @EnabledOnOs - 仅在 Windows 上执行
     * 
     * OS.WINDOWS - Windows 系统
     * OS.LINUX   - Linux 系统
     * OS.MAC     - macOS 系统
     */
    @Test
    @DisplayName("仅在 Windows 上执行")
    @EnabledOnOs(OS.WINDOWS)
    void testOnWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        assertTrue(os.contains("windows"), "应该运行在 Windows 上");
        System.out.println("当前操作系统: " + os);
    }

    /**
     * @DisabledOnOs - 在 Windows 上禁用
     */
    @Test
    @DisplayName("在 Windows 上禁用")
    @DisabledOnOs(OS.WINDOWS)
    void testNotOnWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        assertFalse(os.contains("windows"), "不应该运行在 Windows 上");
    }

    /**
     * @EnabledOnJre - Java 版本条件
     * 
     * JRE.JAVA_17 - Java 17
     * JRE.JAVA_21 - Java 21
     * 
     * min = 最低版本
     * max = 最高版本
     */
    @Test
    @DisplayName("仅在 Java 17+ 上执行")
    @EnabledOnJre(min = JRE.JAVA_17)
    void testOnJava17Plus() {
        int version = Runtime.version().feature();
        assertTrue(version >= 17, "Java 版本应该 >= 17");
        System.out.println("Java 版本: " + version);
    }

    /**
     * @DisabledOnJre - 在特定 Java 版本上禁用
     */
    @Test
    @DisplayName("在 Java 21 上禁用")
    @DisabledOnJre(JRE.JAVA_21)
    void testDisabledOnJava21() {
        int version = Runtime.version().feature();
        assertNotEquals(21, version, "不应该在 Java 21 上执行");
    }

    /**
     * @EnabledIfSystemProperty - 系统属性条件
     * 
     * 常用系统属性：
     *   os.name        - 操作系统名称
     *   java.version   - Java 版本
     *   user.dir       - 当前工作目录
     */
    @Test
    @DisplayName("系统属性条件测试")
    @EnabledIfSystemProperty(named = "os.name", matches = ".*Windows.*")
    void testWithSystemProperty() {
        String osName = System.getProperty("os.name");
        assertNotNull(osName);
        assertTrue(osName.contains("Windows"));
        System.out.println("系统属性 os.name: " + osName);
    }

    /**
     * @EnabledIfEnvironmentVariable - 环境变量条件
     * 
     * 用于测试环境特定的配置
     */
    @Test
    @DisplayName("环境变量条件测试")
    @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".+")
    void testWithEnvironmentVariable() {
        String javaHome = System.getenv("JAVA_HOME");
        assertNotNull(javaHome, "JAVA_HOME 环境变量应该存在");
        System.out.println("JAVA_HOME: " + javaHome);
    }

    /**
     * 组合条件测试
     * 多个条件同时满足才执行
     */
    @Test
    @DisplayName("组合条件测试 - Windows + Java 17+")
    @EnabledOnOs(OS.WINDOWS)
    @EnabledOnJre(min = JRE.JAVA_17)
    void testCombinedConditions() {
        String os = System.getProperty("os.name").toLowerCase();
        int version = Runtime.version().feature();
        
        assertTrue(os.contains("windows"));
        assertTrue(version >= 17);
        System.out.println("Windows + Java " + version);
    }

    /**
     * 自定义条件 - 使用 @EnabledIf
     * 
     * 可以使用自定义的条件方法
     */
    @Test
    @DisplayName("自定义条件测试")
    @Disabled("演示自定义条件（需要实现条件方法）")
    void testCustomCondition() {
        // 如果有自定义条件方法，可以这样使用：
        // @EnabledIf("isDevelopmentEnvironment")
        // static boolean isDevelopmentEnvironment() {
        //     return "dev".equals(System.getenv("ENV"));
        // }
    }
}
