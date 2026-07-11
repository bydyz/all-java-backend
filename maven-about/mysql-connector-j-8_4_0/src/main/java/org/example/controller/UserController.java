package org.example.controller;

import org.example.model.User;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取所有用户
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("获取所有用户");
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * 根据 ID 获取用户
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        logger.info("根据 ID 获取用户: {}", id);
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 创建用户
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        logger.info("创建用户: {}", request);
        try {
            User user = userService.createUser(request.getUsername(), request.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            logger.error("创建用户失败", e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        logger.info("更新用户，ID: {}", id);
        try {
            User user = userService.updateUser(id, request.getUsername(), request.getEmail());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            logger.error("更新用户失败", e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("删除用户，ID: {}", id);
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 获取用户数量
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getUserCount() {
        logger.info("获取用户数量");
        long count = userService.getUserCount();
        return ResponseEntity.ok(count);
    }

    // 请求体类
    public static class CreateUserRequest {
        private String username;
        private String email;

        public CreateUserRequest() {
        }

        public CreateUserRequest(String username, String email) {
            this.username = username;
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class UpdateUserRequest {
        private String username;
        private String email;

        public UpdateUserRequest() {
        }

        public UpdateUserRequest(String username, String email) {
            this.username = username;
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}