-- ============================================
-- RBAC 权限管理系统 - 初始化数据
-- ============================================

USE `rbac_db`;

-- -------------------------------------------
-- 1. 初始化管理员用户 (密码: admin123)
-- -------------------------------------------
INSERT INTO `users` (`id`, `username`, `password`, `nickname`, `status`, `create_by`) VALUES
(1, 'admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '超级管理员', 1, 'system'),
(2, 'user', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '普通用户', 1, 'system');

-- -------------------------------------------
-- 2. 初始化角色
-- -------------------------------------------
INSERT INTO `roles` (`id`, `role_name`, `role_key`, `description`, `status`, `create_by`) VALUES
(1, '超级管理员', 'admin', '拥有所有权限', 1, 'system'),
(2, '普通用户', 'user', '普通用户角色', 1, 'system');

-- -------------------------------------------
-- 3. 用户角色关联
-- -------------------------------------------
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
(1, 1),  -- admin -> 超级管理员
(2, 2);  -- user -> 普通用户

-- -------------------------------------------
-- 4. 初始化菜单 (包含目录、菜单、按钮)
-- -------------------------------------------
INSERT INTO `menus` (`id`, `menu_name`, `parent_id`, `path`, `component`, `redirect`, `icon`, `title`, `hidden`, `keep_alive`, `permission`, `type`, `sort`, `status`) VALUES

-- 系统管理目录
(1, '系统管理', 0, '/system', 'Layout', '/system/user', 'Setting', '系统管理', 0, 0, NULL, 'D', 1, 1),

-- 用户管理菜单
(2, '用户管理', 1, 'user', 'system/user/index', NULL, 'User', '用户管理', 0, 0, NULL, 'M', 1, 1),
-- 用户管理按钮
(3, '用户新增', 2, NULL, NULL, NULL, NULL, NULL, 0, 0, 'user:add', 'B', 1, 1),
(4, '用户编辑', 2, NULL, NULL, NULL, NULL, NULL, 0, 0, 'user:edit', 'B', 2, 1),
(5, '用户删除', 2, NULL, NULL, NULL, NULL, NULL, 0, 0, 'user:delete', 'B', 3, 1),
(6, '用户禁用', 2, NULL, NULL, NULL, NULL, NULL, 0, 0, 'user:disable', 'B', 4, 1),

-- 角色管理菜单
(7, '角色管理', 1, 'role', 'system/role/index', NULL, 'Tickets', '角色管理', 0, 0, NULL, 'M', 2, 1),
-- 角色管理按钮
(8, '角色新增', 7, NULL, NULL, NULL, NULL, NULL, 0, 0, 'role:add', 'B', 1, 1),
(9, '角色编辑', 7, NULL, NULL, NULL, NULL, NULL, 0, 0, 'role:edit', 'B', 2, 1),
(10, '角色删除', 7, NULL, NULL, NULL, NULL, NULL, 0, 0, 'role:delete', 'B', 3, 1),

-- 菜单管理菜单
(11, '菜单管理', 1, 'menu', 'system/menu/index', NULL, 'Menu', '菜单管理', 0, 0, NULL, 'M', 3, 1),
-- 菜单管理按钮
(12, '菜单新增', 11, NULL, NULL, NULL, NULL, NULL, 0, 0, 'menu:add', 'B', 1, 1),
(13, '菜单编辑', 11, NULL, NULL, NULL, NULL, NULL, 0, 0, 'menu:edit', 'B', 2, 1),
(14, '菜单删除', 11, NULL, NULL, NULL, NULL, NULL, 0, 0, 'menu:delete', 'B', 3, 1),

-- 首页目录
(15, '首页', 0, '/dashboard', 'Layout', NULL, 'HomeFilled', '首页', 0, 0, NULL, 'D', 0, 1),
-- 首页菜单
(16, '首页', 15, 'index', 'dashboard/index', NULL, 'HomeFilled', '首页', 0, 0, NULL, 'M', 1, 1);

-- -------------------------------------------
-- 5. 角色菜单关联 (管理员拥有所有权限)
-- -------------------------------------------
INSERT INTO `role_menus` (`role_id`, `menu_id`) VALUES
-- 管理员拥有所有权限
(1, 15), (1, 16),
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6),
(1, 7), (1, 8), (1, 9), (1, 10),
(1, 11), (1, 12), (1, 13), (1, 14),
-- 普通用户只有查看权限
(2, 15), (2, 16),
(2, 1), (2, 2), (2, 7), (2, 11);
