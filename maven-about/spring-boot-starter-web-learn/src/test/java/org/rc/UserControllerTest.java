package org.rc;

import org.junit.jupiter.api.Test;
import org.rc.controller.UserController;
import org.rc.exception.ResourceNotFoundException;
import org.rc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserController 单元测试
 */
@SpringBootTest
class UserControllerTest {
    
    @Autowired
    private UserController userController;
    
    @Test
    void testGetAllUsers() {
        List<User> users = userController.getAllUsers();
        assertNotNull(users);
        assertTrue(users.size() >= 2);
    }
    
    @Test
    void testGetUserById() {
        ResponseEntity<User> response = userController.getUserById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(1L, user.getId());
    }
    
    @Test
    void testGetUserByIdNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> {
            userController.getUserById(999L);
        });
    }
    
    @Test
    void testCreateUser() {
        User newUser = new User(null, "Test User", "test@example.com", 25, "13800138003");
        User created = userController.createUser(newUser);
        
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("Test User", created.getName());
    }
    
    @Test
    void testUpdateUser() {
        User updatedUser = new User(null, "Updated User", "updated@example.com", 26, "13800138011");
        ResponseEntity<User> response = userController.updateUser(1L, updatedUser);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        
        User user = response.getBody();
        assertNotNull(user);
        assertEquals("Updated User", user.getName());
    }
    
    @Test
    void testDeleteUser() {
        User newUser = new User(null, "To Delete", "delete@test.com", 25, "13800138099");
        User created = userController.createUser(newUser);
        
        assertDoesNotThrow(() -> userController.deleteUser(created.getId()));
        
        assertThrows(ResourceNotFoundException.class, () -> {
            userController.getUserById(created.getId());
        });
    }
}
