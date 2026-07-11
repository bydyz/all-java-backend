package org.example;

import org.example.model.User;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void testCreateUser() {
        // 测试创建用户
        User user = userService.createUser("testuser", "test@example.com");
        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void testGetAllUsers() {
        // 测试获取所有用户
        List<User> users = userService.getAllUsers();
        assertNotNull(users);
    }

    @Test
    void testGetUserById() {
        // 先创建一个用户
        User user = userService.createUser("testuser2", "test2@example.com");
        
        // 根据 ID 获取用户
        Optional<User> foundUser = userService.getUserById(user.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("testuser2", foundUser.get().getUsername());
    }

    @Test
    void testUpdateUser() {
        // 先创建一个用户
        User user = userService.createUser("testuser3", "test3@example.com");
        
        // 更新用户
        User updatedUser = userService.updateUser(user.getId(), "updateduser", "updated@example.com");
        assertEquals("updateduser", updatedUser.getUsername());
        assertEquals("updated@example.com", updatedUser.getEmail());
    }

    @Test
    void testDeleteUser() {
        // 先创建一个用户
        User user = userService.createUser("testuser4", "test4@example.com");
        
        // 删除用户
        boolean deleted = userService.deleteUser(user.getId());
        assertTrue(deleted);
        
        // 验证用户已被删除
        Optional<User> deletedUser = userService.getUserById(user.getId());
        assertFalse(deletedUser.isPresent());
    }
}