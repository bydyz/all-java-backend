package org.example.controller;

import org.example.entity.User;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户 REST 控制器
 * 
 * @RestController - 等于 @Controller + @ResponseBody
 *   所有方法的返回值都会自动序列化为 JSON
 * 
 * @RequestMapping - 路由前缀
 * 
 * 常用注解：
 *   @GetMapping     - 处理 GET 请求（查询）
 *   @PostMapping    - 处理 POST 请求（创建）
 *   @PutMapping     - 处理 PUT 请求（更新）
 *   @DeleteMapping  - 处理 DELETE 请求（删除）
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * 构造函数注入
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /api/users
     * 查询所有用户
     * 
     * @return 用户列表，HTTP 200
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    /**
     * GET /api/users/{id}
     * 根据 ID 查询单个用户
     * 
     * @PathVariable - 从路径中提取参数
     *   /api/users/123 -> id = 123
     * 
     * @return 用户对象，HTTP 200；不存在则 HTTP 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findById(id)
            .map(ResponseEntity::ok)                                    // 找到 -> 200
            .orElse(ResponseEntity.notFound().build());                // 未找到 -> 404
    }

    /**
     * GET /api/users/search?keyword=xxx
     * 搜索用户
     * 
     * @RequestParam - 从查询参数中提取
     *   /api/users/search?keyword=test -> keyword = "test"
     */
    @GetMapping("/search")
    public List<User> searchUsers(@RequestParam String keyword) {
        return userService.search(keyword);
    }

    /**
     * POST /api/users
     * 创建新用户
     * 
     * @RequestBody - 从请求体中提取 JSON 并反序列化为对象
     * 
     * 请求示例：
     *   POST /api/users
     *   Content-Type: application/json
     *   {"name": "张三", "email": "zhangsan@example.com", "phone": "13800138000"}
     * 
     * @return 创建的用户，HTTP 201
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)  // 返回 201 Created
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }

    /**
     * PUT /api/users/{id}
     * 更新用户信息
     * 
     * @return 更新后的用户，HTTP 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User updated = userService.update(id, user);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/users/{id}
     * 删除用户
     * 
     * @return 无内容，HTTP 204
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)  // 返回 204 No Content
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
