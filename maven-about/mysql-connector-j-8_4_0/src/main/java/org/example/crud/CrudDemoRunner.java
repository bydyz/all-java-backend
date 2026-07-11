package org.example.crud;

import org.example.model.User;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CrudDemoRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CrudDemoRunner.class);

    private final UserService userService;

    public CrudDemoRunner(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        logger.info("=== CRUD 操作示例 ===");

        // 创建用户
        logger.info("--- 创建用户 ---");
        try {
            User user1 = userService.createUser("zhangsan", "zhangsan@example.com");
            logger.info("创建用户成功: {}", user1);

            User user2 = userService.createUser("lisi", "lisi@example.com");
            logger.info("创建用户成功: {}", user2);

            User user3 = userService.createUser("wangwu", "wangwu@example.com");
            logger.info("创建用户成功: {}", user3);
        } catch (Exception e) {
            logger.error("创建用户失败", e);
        }

        // 查询所有用户
        logger.info("--- 查询所有用户 ---");
        List<User> users = userService.getAllUsers();
        users.forEach(user -> logger.info("用户: {}", user));

        // 根据 ID 查询用户
        logger.info("--- 根据 ID 查询用户 ---");
        Optional<User> userById = userService.getUserById(1L);
        userById.ifPresent(user -> logger.info("找到用户: {}", user));

        // 根据用户名查询用户
        logger.info("--- 根据用户名查询用户 ---");
        Optional<User> userByUsername = userService.getUserByUsername("zhangsan");
        userByUsername.ifPresent(user -> logger.info("找到用户: {}", user));

        // 更新用户
        logger.info("--- 更新用户 ---");
        try {
            User updatedUser = userService.updateUser(1L, "zhangsan_updated", "zhangsan_new@example.com");
            logger.info("更新用户成功: {}", updatedUser);
        } catch (Exception e) {
            logger.error("更新用户失败", e);
        }

        // 查询更新后的用户
        logger.info("--- 查询更新后的用户 ---");
        Optional<User> updatedUser = userService.getUserById(1L);
        updatedUser.ifPresent(user -> logger.info("更新后的用户: {}", user));

        // 获取用户数量
        logger.info("--- 获取用户数量 ---");
        long userCount = userService.getUserCount();
        logger.info("用户总数: {}", userCount);

        // 删除用户
        logger.info("--- 删除用户 ---");
        boolean deleted = userService.deleteUser(3L);
        logger.info("删除用户结果: {}", deleted);

        // 查询删除后的用户数量
        logger.info("--- 查询删除后的用户数量 ---");
        long finalCount = userService.getUserCount();
        logger.info("删除后的用户总数: {}", finalCount);

        logger.info("=== CRUD 操作示例完成 ===");
    }
}