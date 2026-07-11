package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 创建用户
     */
    public User createUser(String username, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("用户名已存在: " + username);
        }
        User user = new User(username, email);
        return userRepository.save(user);
    }

    /**
     * 获取所有用户
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * 根据 ID 获取用户
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * 根据用户名获取用户
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 更新用户
     */
    public User updateUser(Long id, String username, String email) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            throw new RuntimeException("用户未找到，ID: " + id);
        }

        User user = existingUser.get();
        user.setUsername(username);
        user.setEmail(email);
        return userRepository.update(user);
    }

    /**
     * 删除用户
     */
    public boolean deleteUser(Long id) {
        return userRepository.deleteById(id);
    }

    /**
     * 获取用户数量
     */
    public long getUserCount() {
        return userRepository.count();
    }
}