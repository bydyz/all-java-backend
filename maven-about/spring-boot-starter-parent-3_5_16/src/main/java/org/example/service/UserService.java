package org.example.service;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务层
 * 
 * @Service - 标记为服务层组件
 * @Transactional - 事务管理
 *   - 类级别：所有 public 方法都有事务
 *   - readOnly = true：只读优化
 */
@Service
@Transactional
public class UserService {

    /**
     * @Autowired - 依赖注入
     * 构造函数注入（推荐）或字段注入
     */
    private final UserRepository userRepository;

    /**
     * 构造函数注入（推荐方式）
     * Spring 4.3+ 可以省略 @Autowired
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 查询所有用户
     * @Transactional(readOnly = true) - 只读事务，性能优化
     */
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * 根据 ID 查询用户
     * Optional - Java 8 式的空值处理
     */
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * 根据邮箱查询用户
     */
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * 搜索用户
     */
    @Transactional(readOnly = true)
    public List<User> search(String keyword) {
        return userRepository.searchByKeyword(keyword);
    }

    /**
     * 创建用户
     * @param user 用户对象
     * @return 保存后的用户（包含生成的 ID）
     */
    public User save(User user) {
        // 可以在这里添加业务校验
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("邮箱已存在: " + user.getEmail());
        }
        return userRepository.save(user);
    }

    /**
     * 更新用户
     */
    public User update(Long id, User updateUser) {
        User existing = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("用户不存在: " + id));
        
        // 更新字段
        existing.setName(updateUser.getName());
        existing.setEmail(updateUser.getEmail());
        existing.setPhone(updateUser.getPhone());
        existing.setStatus(updateUser.getStatus());
        
        return userRepository.save(existing);
    }

    /**
     * 删除用户
     */
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("用户不存在: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * 批量删除指定状态的用户
     */
    public void deleteByStatus(User.UserStatus status) {
        userRepository.deleteByStatus(status);
    }
}
