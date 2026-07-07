package org.rc.mybatisplus.controller;

import org.rc.mybatisplus.entity.User;
import org.rc.mybatisplus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 * 提供 RESTful API 接口
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取所有用户
     */
    @GetMapping
    public Map<String, Object> getAllUsers() {
        Map<String, Object> result = new HashMap<>();
        List<User> users = userService.list();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", users);
        return result;
    }

    /**
     * 根据ID获取用户
     */
    @GetMapping("/{id}")
    public Map<String, Object> getUserById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        User user = userService.getById(id);
        if (user != null) {
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", user);
        } else {
            result.put("code", 404);
            result.put("message", "用户不存在");
            result.put("data", null);
        }
        return result;
    }

    /**
     * 创建用户
     */
    @PostMapping
    public Map<String, Object> createUser(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        boolean success = userService.save(user);
        if (success) {
            result.put("code", 200);
            result.put("message", "用户创建成功");
            result.put("data", user);
        } else {
            result.put("code", 500);
            result.put("message", "用户创建失败");
            result.put("data", null);
        }
        return result;
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public Map<String, Object> updateUser(@PathVariable Long id, @RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        user.setId(id);
        boolean success = userService.updateById(user);
        if (success) {
            result.put("code", 200);
            result.put("message", "用户更新成功");
            result.put("data", user);
        } else {
            result.put("code", 500);
            result.put("message", "用户更新失败");
            result.put("data", null);
        }
        return result;
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteUser(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        boolean success = userService.removeById(id);
        if (success) {
            result.put("code", 200);
            result.put("message", "用户删除成功");
        } else {
            result.put("code", 500);
            result.put("message", "用户删除失败");
        }
        return result;
    }

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "Application is running");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }
}
