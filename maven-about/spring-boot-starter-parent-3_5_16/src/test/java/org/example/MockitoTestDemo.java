package org.example;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Mockito 单元测试示例
 * 
 * Mockito 是 Java 中最流行的 Mock 框架
 * 
 * 常用注解：
 *   @Mock          - 创建 Mock 对象
 *   @InjectMocks   - 注入 Mock 对象
 *   @Spy           - 创建 Spy 对象（部分 Mock）
 * 
 * 常用方法：
 *   when(...).thenReturn(...)  - 当调用时返回指定值
 *   verify(...)                - 验证方法被调用
 *   doReturn(...).when(...)    - 指定返回值
 *   doThrow(...).when(...)     - 指定抛出异常
 */
class MockitoTestDemo {

    /**
     * @Mock - 创建 Mock 对象
     * Mock 对象默认返回 null、0、false 等默认值
     */
    @Mock
    private UserRepository mockUserRepository;

    /**
     * @InjectMocks - 自动注入 Mock 到被测试类
     * 创建 UserService 实例并注入 Mock 的 UserRepository
     */
    @InjectMocks
    private UserService userService;

    private AutoCloseable closeable;

    /**
     * @BeforeEach - 每个测试前初始化 Mockito
     */
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    /**
     * 基本 Mock 测试 - when().thenReturn()
     * 
     * 模拟 repository 的返回值
     */
    @Test
    @DisplayName("测试查询用户 - Mock 返回值")
    void testFindById() {
        // 准备测试数据
        User mockUser = new User("张三", "zhangsan@example.com", "13800138000");
        mockUser.setId(1L);

        // 设置 Mock 行为
        // 当调用 findById(1L) 时，返回 mockUser
        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // 执行测试
        Optional<User> result = userService.findById(1L);

        // 验证结果
        assertTrue(result.isPresent(), "应该找到用户");
        assertEquals("张三", result.get().getName());
        assertEquals("zhangsan@example.com", result.get().getEmail());

        // 验证方法被调用
        verify(mockUserRepository, times(1)).findById(1L);
    }

    /**
     * Mock 不存在的情况
     */
    @Test
    @DisplayName("测试查询不存在的用户")
    void testFindByIdNotFound() {
        // 设置 Mock 返回空 Optional
        when(mockUserRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(999L);

        assertFalse(result.isPresent(), "不应该找到用户");
        verify(mockUserRepository).findById(999L);
    }

    /**
     * Mock 列表返回
     */
    @Test
    @DisplayName("测试查询所有用户")
    void testFindAll() {
        // 准备多个用户
        List<User> mockUsers = Arrays.asList(
            new User("张三", "zhangsan@example.com", "13800138000"),
            new User("李四", "lisi@example.com", "13900139000")
        );

        // 设置 Mock 返回列表
        when(mockUserRepository.findAll()).thenReturn(mockUsers);

        List<User> result = userService.findAll();

        assertEquals(2, result.size());
        verify(mockUserRepository).findAll();
    }

    /**
     * Mock 保存操作
     */
    @Test
    @DisplayName("测试保存用户")
    void testSaveUser() {
        User newUser = new User("王五", "wangwu@example.com", "13700137000");
        User savedUser = new User("王五", "wangwu@example.com", "13700137000");
        savedUser.setId(1L);

        // 设置 Mock 返回保存后的对象
        when(mockUserRepository.save(newUser)).thenReturn(savedUser);
        when(mockUserRepository.existsByEmail("wangwu@example.com")).thenReturn(false);

        User result = userService.save(newUser);

        assertNotNull(result.getId(), "保存后应该有 ID");
        assertEquals(1L, result.getId());
        verify(mockUserRepository).save(newUser);
    }

    /**
     * Mock 抛出异常
     */
    @Test
    @DisplayName("测试保存重复邮箱")
    void testSaveDuplicateEmail() {
        User duplicateUser = new User("张三", "zhangsan@example.com", "13800138000");

        // 设置 Mock 抛出异常
        when(mockUserRepository.existsByEmail("zhangsan@example.com")).thenReturn(true);

        // 验证抛出异常
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> userService.save(duplicateUser)
        );

        assertTrue(exception.getMessage().contains("邮箱已存在"));
        verify(mockUserRepository, never()).save(any());
    }

    /**
     * Mock 删除操作
     */
    @Test
    @DisplayName("测试删除用户")
    void testDeleteUser() {
        when(mockUserRepository.existsById(1L)).thenReturn(true);

        userService.delete(1L);

        // 验证删除方法被调用
        verify(mockUserRepository).deleteById(1L);
    }

    /**
     * Mock 删除不存在的用户
     */
    @Test
    @DisplayName("测试删除不存在的用户")
    void testDeleteUserNotFound() {
        when(mockUserRepository.existsById(999L)).thenReturn(false);

        assertThrows(
            RuntimeException.class,
            () -> userService.delete(999L)
        );

        // 验证 deleteById 没有被调用
        verify(mockUserRepository, never()).deleteById(any());
    }

    /**
     * verify - 验证方法调用次数
     */
    @Test
    @DisplayName("验证方法调用次数")
    void testVerifyCallCount() {
        when(mockUserRepository.findAll()).thenReturn(Arrays.asList());

        // 调用两次
        userService.findAll();
        userService.findAll();

        // 验证被调用了 2 次
        verify(mockUserRepository, times(2)).findAll();

        // 其他验证方式：
        // verify(mock, never()).someMethod();    // 从未调用
        // verify(mock, atLeast(1)).someMethod(); // 至少调用1次
        // verify(mock, atMost(5)).someMethod();  // 最多调用5次
    }

    /**
     * verifyNoMoreInteractions - 验证没有其他交互
     */
    @Test
    @DisplayName("验证没有其他交互")
    void testNoOtherInteractions() {
        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(new User()));

        userService.findById(1L);

        // 验证只有 findById 被调用，没有其他方法
        verify(mockUserRepository).findById(1L);
        verifyNoMoreInteractions(mockUserRepository);
    }
}
