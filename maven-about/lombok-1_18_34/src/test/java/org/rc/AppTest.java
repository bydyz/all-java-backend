package org.rc;

import org.junit.jupiter.api.Test;
import org.rc.entity.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Lombok 注解功能测试
 */
public class AppTest {

    /**
     * 测试 @Getter/@Setter
     */
    @Test
    void testGetterSetter() {
        User user = new User();

        // 测试 setter
        user.setName("张三");
        user.setAge(25);
        user.setEmail("zhangsan@example.com");

        // 测试 getter
        assertEquals("张三", user.getName());
        assertEquals(25, user.getAge());
        assertEquals("zhangsan@example.com", user.getEmail());

        // id 有初始值（时间戳）
        assertNotNull(user.getId());

        // password 禁用了 getter，无法访问
        // user.getPassword(); // 编译错误
    }

    /**
     * 测试 @ToString
     */
    @Test
    void testToString() {
        Product product = new Product("P001", "笔记本电脑", 5999.0, "电子产品");

        String str = product.toString();
        // @ToString(of = {"name", "price"}) 只包含这两个字段
        assertTrue(str.contains("笔记本电脑"));
        assertTrue(str.contains("5999.0"));
        // category 不包含在 toString 中
        assertFalse(str.contains("电子产品"));
    }

    /**
     * 测试 @EqualsAndHashCode
     */
    @Test
    void testEqualsAndHashCode() {
        Product p1 = new Product("P001", "手机", 2999.0, "电子产品");
        Product p2 = new Product("P001", "电脑", 8999.0, "电子产品");
        Product p3 = new Product("P002", "手机", 2999.0, "电子产品");

        // id 相同，equals 返回 true
        assertEquals(p1, p2);

        // id 不同，equals 返回 false（即使其他字段相同）
        assertNotEquals(p1, p3);

        // hashCode 基于 id
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    /**
     * 测试 @Data（组合了 @Getter/@Setter/@ToString/@EqualsAndHashCode）
     */
    @Test
    void testData() {
        Person person = new Person();
        person.setName("李四");
        person.setAge(30);

        assertEquals("李四", person.getName());
        assertEquals(30, person.getAge());

        // toString 输出格式: Person(name=李四, age=30)
        String str = person.toString();
        assertTrue(str.contains("李四"));
        assertTrue(str.contains("30"));
    }

    /**
     * 测试 @Builder
     */
    @Test
    void testBuilder() {
        // Builder 模式链式构建
        Account account = Account.builder()
                .username("admin")
                .email("admin@example.com")
                .level(1)
                .build();

        assertEquals("admin", account.getUsername());
        assertEquals("admin@example.com", account.getEmail());
        assertEquals(1, account.getLevel());
    }

    /**
     * 测试 @Value（不可变对象）
     */
    @Test
    void testValue() {
        // @RequiredArgsConstructor 生成构造函数
        Config config = new Config("localhost", 8080, "production");

        assertEquals("localhost", config.getHost());
        assertEquals(8080, config.getPort());
        assertEquals("production", config.getEnvironment());

        // @Value 使类为 final，没有 setter
        // config.setHost("new-host"); // 编译错误
    }

    /**
     * 测试 @Slf4j + @SneakyThrows
     */
    @Test
    void testOrderWithLogAndSneakyThrows() {
        Order order = Order.builder()
                .orderId("ORD-001")
                .productName("手机")
                .quantity(2)
                .totalPrice(5998.0)
                .build();

        // formatOrder() 内部使用 log 和 @SneakyThrows
        String result = order.formatOrder();
        assertNotNull(result);
        assertTrue(result.contains("ORD-001"));
        assertTrue(result.contains("手机"));
    }

    /**
     * 测试 @ToString(exclude) 和 @EqualsAndHashCode(of)
     */
    @Test
    void testEmployeeAnnotations() {
        Employee emp1 = Employee.builder()
                .employeeId("E001")
                .name("王五")
                .department("技术部")
                .salary(15000.0)
                .build();

        Employee emp2 = Employee.builder()
                .employeeId("E001")
                .name("王五二号")
                .department("产品部")
                .salary(20000.0)
                .build();

        // employeeId 相同，equals 返回 true
        assertEquals(emp1, emp2);

        // 测试自定义方法
        assertEquals("王*", emp1.getMaskedName());
    }

    /**
     * 测试 Builder 的可选字段
     */
    @Test
    void testBuilderWithOptionalFields() {
        // 只设置部分字段
        Order order = Order.builder()
                .orderId("ORD-002")
                .build();

        assertEquals("ORD-002", order.getOrderId());
        assertNull(order.getProductName());
        assertEquals(0, order.getQuantity());
        assertEquals(0.0, order.getTotalPrice());
    }

    /**
     * 测试 @NoArgsConstructor 和 @AllArgsConstructor
     */
    @Test
    void testConstructors() {
        // 无参构造
        Account account1 = new Account();
        assertNotNull(account1);

        // 全参构造
        Account account2 = new Account("user1", "user1@example.com", 2);
        assertEquals("user1", account2.getUsername());
        assertEquals(2, account2.getLevel());
    }
}
