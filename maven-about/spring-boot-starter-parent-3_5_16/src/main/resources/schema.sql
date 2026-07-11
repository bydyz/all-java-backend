-- ================================================
-- Spring Boot Demo 数据库初始化脚本
-- MySQL 8.0+
-- ================================================

-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS `my_test_database` 
  DEFAULT CHARACTER SET utf8mb4 
  DEFAULT COLLATE utf8mb4_unicode_ci;

-- 2. 使用数据库
USE `my_test_database`;

-- 3. 创建 users 表
DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `name` VARCHAR(50) NOT NULL COMMENT '用户名',
  `email` VARCHAR(255) NOT NULL COMMENT '邮箱地址',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号码',
  `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-激活, INACTIVE-未激活, DELETED-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_email` (`email`) COMMENT '邮箱唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 4. 插入测试数据
INSERT INTO `users` (`name`, `email`, `phone`, `status`) VALUES
  ('张三', 'zhangsan@example.com', '13800138000', 'ACTIVE'),
  ('李四', 'lisi@example.com', '13900139000', 'ACTIVE'),
  ('王五', 'wangwu@example.com', '13700137000', 'INACTIVE');

-- 5. 验证
SELECT * FROM `users`;
