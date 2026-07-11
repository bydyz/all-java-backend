package org.rc.controller;

import jakarta.validation.Valid;
import org.rc.exception.ResourceNotFoundException;
import org.rc.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 用户 CRUD 控制器 - 演示完整的 RESTful API
 * 
 * 演示内容：
 * 1. @PathVariable - 路径变量
 * 2. @RequestBody - 请求体绑定
 * 3. @Valid - 数据验证
 * 4. ResponseEntity - 完整响应控制
 * 5. @ResponseStatus - 响应状态码
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    // 模拟数据库
    private final Map<Long, User> userDatabase = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    public UserController() {
        // 初始化测试数据
        User user1 = new User(1L, "张三", "zhangsan@example.com", 25, "13800138001");
        User user2 = new User(2L, "李四", "lisi@example.com", 30, "13800138002");
        userDatabase.put(1L, user1);
        userDatabase.put(2L, user2);
        idGenerator.set(3);
    }
    
    /**
     * GET /api/users
     * 获取所有用户列表
     */
    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(userDatabase.values());
    }
    
    /**
     * GET /api/users/{id}
     * 根据 ID 获取单个用户
     * 
     * @param id 用户 ID (从路径中获取)
     * @return 用户对象，如果不存在则返回 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userDatabase.get(id);
        
        if (user == null) {
            throw new ResourceNotFoundException("User", id);
        }
        
        return ResponseEntity.ok(user);
    }
    
    /**
     * POST /api/users
     * 创建新用户
     * 
     * @param user 请求体中的用户数据 (JSON)
     * @return 创建的用户对象
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)  // 返回 201 Created 状态码
    public User createUser(@Valid @RequestBody User user) {
        user.setId(idGenerator.getAndIncrement());
        userDatabase.put(user.getId(), user);
        return user;
    }
    
    /**
     * PUT /api/users/{id}
     * 更新用户信息
     * 
     * @param id 用户 ID
     * @param updatedUser 更新的用户数据
     * @return 更新后的用户对象
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody User updatedUser) {
        
        if (!userDatabase.containsKey(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        
        updatedUser.setId(id);
        userDatabase.put(id, updatedUser);
        
        return ResponseEntity.ok(updatedUser);
    }
    
    /**
     * DELETE /api/users/{id}
     * 删除用户
     * 
     * @param id 用户 ID
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)  // 返回 204 No Content
    public void deleteUser(@PathVariable Long id) {
        if (!userDatabase.containsKey(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        userDatabase.remove(id);
    }
}
