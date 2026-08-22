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

-- -------------------------------------------
-- 6. 数据结构示例数据
-- -------------------------------------------
INSERT INTO `demos` (`name`, `description`, `age`, `amount`, `score`, `price`, `big_decimal`, `enabled`, `status`, `birthday`, `create_by`) VALUES
-- 基础数据类型示例
('张三', '这是一条测试数据，用于演示各种数据类型', 25, 10000, 88.5, 99.99, 123456.78, 1, 1, '1998-05-20', 'system'),
('李四', '包含特殊字符的数据：<script>alert(1)</script>', 30, 50000, 95.0, 199.99, 888888.88, 1, 1, '1993-10-15', 'system'),
('王五', NULL, 28, 30000, 76.5, 50.00, 66666.66, 0, 1, '1995-08-08', 'system'),
('赵六', 'Unicode测试：你好世界 🌍🎉', 35, 100000, 92.0, 299.99, 999999.99, 1, 1, '1988-12-25', 'system'),
('钱七', '金额较大的测试数据', 22, 999999999, 85.5, 0.01, 0.01, 1, 1, '2001-03-14', 'system'),
('孙八', '禁用状态的记录', 40, 20000, 70.0, 150.00, 55555.55, 1, 0, '1983-07-07', 'system'),
('周九', '已删除标记的记录', 27, 15000, 88.0, 88.88, 77777.77, 1, 1, '1996-11-11', 'system'),
('吴十', '空描述的测试', 33, 8000, 60.0, 250.00, 44444.44, 0, 0, '1990-02-28', 'system'),
('郑十一', '高精度数值测试', 29, 60000, 99.9, 1000.00, 123456789.12, 1, 1, '1994-06-16', 'system'),
('王十二', '边界值测试', 0, 0, 0.0, 0.0, 0.00, 1, 1, '2023-01-01', 'system'),
('冯十三', '负数年龄测试（边界）', 150, 9999999999, 100.0, 999999.99, 99999999.99, 1, 1, '1873-01-01', 'system'),
('陈十四', '特殊字符：!@#$%^&*()', 26, 12000, 82.3, 75.50, 33333.33, 1, 1, '1997-04-04', 'system'),
('楚十五', '中日韩混合：日本語テスト', 31, 45000, 91.0, 180.00, 22222.22, 1, 1, '1992-09-09', 'system'),
('卫十六', '超长描述文本用于测试文本字段的存储能力，这是一段非常长的描述文本，包含了各种字符和换行符\n，用于验证数据库对长文本的处理能力。', 24, 7000, 78.8, 120.00, 11111.11, 1, 1, '1999-12-31', 'system'),
('蒋十七', '小数精度测试', 38, 35000, 88.88, 888.88, 888888.88, 1, 1, '1985-05-05', 'system'),
('沈十八', '最大整数值测试', 21, 2147483647, 50.0, 300.00, 100000.00, 1, 1, '2002-08-18', 'system'),
('韩十九', 'UTF-8编码测试：😊🎉🚀', 34, 28000, 94.5, 160.00, 66666.66, 1, 1, '1989-03-03', 'system'),
('杨二十', '最后一条测试数据', 29, 18000, 87.0, 95.00, 55555.55, 1, 1, '1994-07-20', 'system');

-- -------------------------------------------
-- 7. 数据类型演示数据
-- -------------------------------------------
INSERT INTO `data_type_demo` (`string_val`, `integer_val`, `long_val`, `float_val`, `double_val`, `boolean_val`, `decimal_val`, `date_val`, `datetime_val`, `list_val`, `set_val`, `map_val`, `array_val`, `status`, `create_by`) VALUES
('Hello World', 42, 123456789, 3.14, 3.141592653589793, 1, 99999.99, '2024-01-15', NOW(), '["苹果","香蕉","橙子"]', '[1,2,3,4,5]', '{"name":"张三","age":25,"city":"北京"}', '["a","b","c"]', 1, 'system'),
('测试字符串', -100, -999999999, -2.5, -123.456, 0, 0.01, '2000-12-31', '2024-06-15 14:30:00', '["项目A","项目B"]', '[10,20,30]', '{"key":"value","num":123}', '["x","y","z"]', 1, 'system'),
('Unicode测试：你好世界🌍', 0, 0, 0.0, 0.0, 1, 0.00, '1970-01-01', '1970-01-01 00:00:00', '[]', '[]', '{}', '[]', 1, 'system'),
('特殊字符：!@#$%^&*()', 2147483647, 9223372036854775807, 3.40282E38, 1.7976931348623157E308, 1, 99999999999999.99, '9999-12-31', '9999-12-31 23:59:59', '["最大值测试"]', '[2147483647]', '{"max":true}', '["max"]', 1, 'system'),
('空值测试', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 'system');

-- -------------------------------------------
-- 8. HTTP 请求方式演示数据
-- -------------------------------------------
INSERT INTO `http_method_demo` (`method_name`, `annotation`, `description`, `request_body`, `idempotent`, `status`, `create_by`) VALUES
('GET', '@GetMapping', '获取资源，查询数据', 0, 1, 1, 'system'),
('POST', '@PostMapping', '创建资源，提交数据', 1, 0, 1, 'system'),
('PUT', '@PutMapping', '更新资源，全量替换', 1, 1, 1, 'system'),
('DELETE', '@DeleteMapping', '删除资源', 0, 1, 1, 'system'),
('PATCH', '@PatchMapping', '部分更新资源', 1, 1, 1, 'system'),
('HEAD', '@RequestMapping(method=HEAD)', '获取资源的元数据头信息', 0, 1, 1, 'system'),
('OPTIONS', '@RequestMapping(method=OPTIONS)', '查询服务器支持的请求方法', 0, 1, 1, 'system');

-- -------------------------------------------
-- 9. 接口传参方式演示数据
-- -------------------------------------------
INSERT INTO `param_type_demo` (`annotation`, `description`, `param_source`, `usage_scenario`, `example`, `status`, `create_by`) VALUES
('@RequestParam', '查询参数或表单参数', 'URL', '获取URL查询字符串中的参数，如 ?name=xxx', '/api/user?name=张三&age=25', 1, 'system'),
('@PathVariable', '路径变量', 'URL', '获取URL路径中的变量，如 /user/{id}', '/api/user/123', 1, 'system'),
('@RequestBody', '请求体', 'Body', '接收JSON/XML等请求体数据，用于POST/PUT', '{"name":"张三","age":25}', 1, 'system'),
('@RequestHeader', '请求头', 'Header', '获取HTTP请求头中的值', 'Authorization: Bearer xxx', 1, 'system'),
('@CookieValue', 'Cookie', 'Cookie', '获取Cookie中的值', 'JSESSIONID=xxx', 1, 'system'),
('@ModelAttribute', '模型属性', 'Body/Form', '将表单数据绑定到对象，自动转换类型', '表单提交的key=value对', 1, 'system'),
('@MatrixVariable', '矩阵变量', 'URL', 'URL路径分号后的键值对，如 /user;q=1;r=2', '/api/user;name=张三;age=25', 1, 'system'),
('@RequestPart', 'multipart部分', 'Body', '文件上传时获取文件或JSON部分', 'multipart/form-data', 1, 'system');
