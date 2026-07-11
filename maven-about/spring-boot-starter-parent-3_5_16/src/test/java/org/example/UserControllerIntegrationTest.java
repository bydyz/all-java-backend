package org.example;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * REST Controller 集成测试
 * 
 * @SpringBootTest - 启动完整 Spring 上下文
 * @AutoConfigureMockMvc - 自动配置 MockMvc
 * 
 * MockMvc 用于模拟 HTTP 请求，测试 Controller 层
 * 无需启动真实的 HTTP 服务器
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerIntegrationTest {

    /**
     * MockMvc - 模拟 HTTP 请求
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * 注入 UserRepository 用于准备测试数据
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * @BeforeEach - 每个测试前准备数据
     */
    @BeforeEach
    void setUp() {
        // 清空数据
        userRepository.deleteAll();
        
        // 准备测试数据
        userRepository.save(new User("张三", "zhangsan@example.com", "13800138000"));
        userRepository.save(new User("李四", "lisi@example.com", "13900139000"));
    }

    /**
     * 测试 GET /api/users - 查询所有用户
     * 
     * MockMvc 用法：
     *   perform() - 执行请求
     *   andExpect() - 断言结果
     *   andReturn() - 返回结果
     */
    @Test
    @Order(1)
    @DisplayName("测试查询所有用户")
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users")                    // 发送 GET 请求
                .contentType(MediaType.APPLICATION_JSON))     // 指定 Content-Type
            .andExpect(status().isOk())                      // 断言 HTTP 200
            .andExpect(jsonPath("$.length()").value(2));      // 断言返回 2 个用户
    }

    /**
     * 测试 GET /api/users/{id} - 根据 ID 查询
     * 
     * jsonPath() 用于提取 JSON 数据进行断言
     *   $.xxx - 根字段
     *   $[0].xxx - 数组第一个元素的字段
     */
    @Test
    @Order(2)
    @DisplayName("测试根据 ID 查询用户")
    void testGetUserById() throws Exception {
        mockMvc.perform(get("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("张三"))
            .andExpect(jsonPath("$.email").value("zhangsan@example.com"));
    }

    /**
     * 测试 GET /api/users/999 - 查询不存在的用户
     */
    @Test
    @Order(3)
    @DisplayName("测试查询不存在的用户")
    void testGetUserNotFound() throws Exception {
        mockMvc.perform(get("/api/users/999")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());               // 断言 HTTP 404
    }

    /**
     * 测试 POST /api/users - 创建用户
     * 
     * content() 用于设置请求体
     */
    @Test
    @Order(4)
    @DisplayName("测试创建用户")
    void testCreateUser() throws Exception {
        String userJson = """
            {
                "name": "王五",
                "email": "wangwu@example.com",
                "phone": "13700137000"
            }
            """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
            .andExpect(status().isCreated())                 // 断言 HTTP 201
            .andExpect(jsonPath("$.name").value("王五"))
            .andExpect(jsonPath("$.email").value("wangwu@example.com"));
    }

    /**
     * 测试 PUT /api/users/{id} - 更新用户
     */
    @Test
    @Order(5)
    @DisplayName("测试更新用户")
    void testUpdateUser() throws Exception {
        String updateUserJson = """
            {
                "name": "张三丰",
                "email": "zhangsanfeng@example.com",
                "phone": "13600136000"
            }
            """;

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateUserJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("张三丰"))
            .andExpect(jsonPath("$.email").value("zhangsanfeng@example.com"));
    }

    /**
     * 测试 DELETE /api/users/{id} - 删除用户
     */
    @Test
    @Order(6)
    @DisplayName("测试删除用户")
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());              // 断言 HTTP 204
    }
}
