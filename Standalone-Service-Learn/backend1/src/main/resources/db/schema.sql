-- ============================================
-- RBAC 权限管理系统 - 数据库初始化脚本
-- 数据库: MySQL 8.0+
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `rbac_db`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE `rbac_db`;

-- -------------------------------------------
-- 1. 用户表
-- -------------------------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像地址',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态 0:禁用 1:启用',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记 0:未删除 1:已删除',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建者',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- -------------------------------------------
-- 2. 角色表
-- -------------------------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
  `role_key` VARCHAR(50) NOT NULL COMMENT '角色标识',
  `description` VARCHAR(200) DEFAULT NULL COMMENT '描述',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态 0:禁用 1:启用',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建者',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_key` (`role_key`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- -------------------------------------------
-- 3. 菜单表
-- -------------------------------------------
DROP TABLE IF EXISTS `menus`;
CREATE TABLE `menus` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父菜单ID',
  `path` VARCHAR(200) DEFAULT NULL COMMENT '路由路径',
  `component` VARCHAR(200) DEFAULT NULL COMMENT '组件路径',
  `redirect` VARCHAR(200) DEFAULT NULL COMMENT '重定向地址',
  `icon` VARCHAR(50) DEFAULT NULL COMMENT '图标',
  `title` VARCHAR(50) DEFAULT NULL COMMENT '显示标题',
  `hidden` TINYINT NOT NULL DEFAULT 0 COMMENT '是否隐藏 0:显示 1:隐藏',
  `keep_alive` TINYINT NOT NULL DEFAULT 0 COMMENT '是否缓存 0:不缓存 1:缓存',
  `permission` VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
  `type` CHAR(1) NOT NULL COMMENT '类型 D:目录 M:菜单 B:按钮',
  `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态 0:禁用 1:启用',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建者',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_type` (`type`),
  KEY `idx_sort` (`sort`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单表';

-- -------------------------------------------
-- 4. 用户角色关联表
-- -------------------------------------------
DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles` (
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`),
  KEY `idx_role_id` (`role_id`),
  CONSTRAINT `fk_ur_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_ur_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- -------------------------------------------
-- 5. 角色菜单关联表
-- -------------------------------------------
DROP TABLE IF EXISTS `role_menus`;
CREATE TABLE `role_menus` (
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`),
  KEY `idx_menu_id` (`menu_id`),
  CONSTRAINT `fk_rm_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_rm_menu` FOREIGN KEY (`menu_id`) REFERENCES `menus` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';

-- -------------------------------------------
-- 6. 数据结构示例表
-- -------------------------------------------
DROP TABLE IF EXISTS `demos`;
CREATE TABLE `demos` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` VARCHAR(100) NOT NULL COMMENT '名称',
  `description` TEXT COMMENT '描述',
  `age` INT DEFAULT NULL COMMENT '年龄',
  `amount` BIGINT DEFAULT NULL COMMENT '金额',
  `score` FLOAT DEFAULT NULL COMMENT '分数',
  `price` DOUBLE DEFAULT NULL COMMENT '价格',
  `big_decimal` DECIMAL(18,2) DEFAULT NULL COMMENT '高精度数值',
  `enabled` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用 0:禁用 1:启用',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态 0:禁用 1:启用',
  `birthday` DATE DEFAULT NULL COMMENT '生日',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记 0:未删除 1:已删除',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建者',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据结构示例表';

-- -------------------------------------------
-- 7. 数据类型演示表
-- -------------------------------------------
DROP TABLE IF EXISTS `data_type_demo`;
CREATE TABLE `data_type_demo` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `string_val` VARCHAR(200) DEFAULT NULL COMMENT '字符串',
  `integer_val` INT DEFAULT NULL COMMENT '整数',
  `long_val` BIGINT DEFAULT NULL COMMENT '长整数',
  `float_val` FLOAT DEFAULT NULL COMMENT '单精度浮点',
  `double_val` DOUBLE DEFAULT NULL COMMENT '双精度浮点',
  `boolean_val` TINYINT(1) DEFAULT NULL COMMENT '布尔值',
  `decimal_val` DECIMAL(18,2) DEFAULT NULL COMMENT '高精度数值',
  `date_val` DATE DEFAULT NULL COMMENT '日期',
  `datetime_val` DATETIME DEFAULT NULL COMMENT '日期时间',
  `list_val` VARCHAR(500) DEFAULT NULL COMMENT '列表(JSON)',
  `set_val` VARCHAR(500) DEFAULT NULL COMMENT '集合(JSON)',
  `map_val` TEXT COMMENT '映射(JSON)',
  `array_val` VARCHAR(500) DEFAULT NULL COMMENT '数组(JSON)',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态 0:禁用 1:启用',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记 0:未删除 1:已删除',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建者',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据类型演示表';

-- -------------------------------------------
-- 8. HTTP 请求方式演示表
-- -------------------------------------------
DROP TABLE IF EXISTS `http_method_demo`;
CREATE TABLE `http_method_demo` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `method_name` VARCHAR(20) NOT NULL COMMENT '请求方式名称(GET/POST/PUT/DELETE/PATCH/HEAD/OPTIONS)',
  `annotation` VARCHAR(50) NOT NULL COMMENT 'Spring注解(@GetMapping等)',
  `description` VARCHAR(200) DEFAULT NULL COMMENT '请求方式说明',
  `request_body` TINYINT NOT NULL DEFAULT 0 COMMENT '是否支持请求体 0:否 1:是',
  `idempotent` TINYINT NOT NULL DEFAULT 1 COMMENT '是否幂等 0:否 1:是',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态 0:禁用 1:启用',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记 0:未删除 1:已删除',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建者',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_method_name` (`method_name`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='HTTP请求方式演示表';

-- -------------------------------------------
-- 9. 接口传参方式演示表
-- -------------------------------------------
DROP TABLE IF EXISTS `param_type_demo`;
CREATE TABLE `param_type_demo` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `annotation` VARCHAR(50) NOT NULL COMMENT '注解名称',
  `description` VARCHAR(200) DEFAULT NULL COMMENT '传参方式说明',
  `param_source` VARCHAR(50) NOT NULL COMMENT '参数来源(URL/Header/Cookie/Body/Path)',
  `usage_scenario` VARCHAR(200) DEFAULT NULL COMMENT '使用场景',
  `example` VARCHAR(500) DEFAULT NULL COMMENT '示例',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态 0:禁用 1:启用',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记 0:未删除 1:已删除',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建者',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新者',
  `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_annotation` (`annotation`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='接口传参方式演示表';
