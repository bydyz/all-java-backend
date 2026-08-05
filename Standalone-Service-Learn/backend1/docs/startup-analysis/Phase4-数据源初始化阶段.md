# Phase 4：数据源初始化阶段

## 概述

Phase 4 是 Spring Boot 启动流程的第四个阶段，主要完成数据源的初始化工作。包括 MySQL 连接池初始化、数据库脚本执行、Redis 连接初始化等。

---

## 4.1 MySQL 连接池初始化

### 4.1.1 HikariCP 连接池配置

`application-dev.yml:3-14`

```yaml
spring:
  # 数据源配置
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/rbac_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: rU^c*sM_SL1.ye
    hikari:
      minimum-idle: 5
      maximum-pool-size: 20
      idle-timeout: 30000
      max-lifetime: 1800000
      connection-timeout: 30000
      connection-test-query: SELECT 1
```

### 4.1.2 连接池参数说明

| 参数 | 值 | 说明 |
|------|-----|------|
| `minimum-idle` | 5 | 最小空闲连接数 |
| `maximum-pool-size` | 20 | 最大连接数 |
| `idle-timeout` | 30000ms | 空闲连接超时时间 |
| `max-lifetime` | 1800000ms | 连接最大生命周期 |
| `connection-timeout` | 30000ms | 连接超时时间 |
| `connection-test-query` | SELECT 1 | 连接测试查询 |

### 4.1.3 连接池初始化流程

```
┌─────────────────────────────────────────────────────────────────┐
│                    HikariCP 连接池初始化流程                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. 创建 HikariDataSource                                       │
│     └── 读取配置参数                                             │
│                                                                 │
│  2. 创建连接池                                                   │
│     ├── minimum-idle = 5                                        │
│     └── maximum-pool-size = 20                                  │
│                                                                 │
│  3. 预创建连接                                                   │
│     └── 创建 5 个空闲连接                                        │
│                                                                 │
│  4. 测试连接                                                     │
│     └── 执行 SELECT 1 验证连接                                   │
│                                                                 │
│  5. 连接池就绪                                                   │
│     └── 可以获取数据库连接                                       │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 4.1.4 数据库连接 URL 解析

```
jdbc:mysql://localhost:3306/rbac_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
```

| 参数 | 值 | 说明 |
|------|-----|------|
| `jdbc:mysql://` | 协议 | MySQL JDBC 协议 |
| `localhost` | 主机 | 数据库服务器地址 |
| `3306` | 端口 | MySQL 默认端口 |
| `rbac_db` | 数据库 | 数据库名称 |
| `useUnicode=true` | 参数 | 使用 Unicode 编码 |
| `characterEncoding=utf-8` | 参数 | 字符编码 |
| `useSSL=false` | 参数 | 禁用 SSL |
| `serverTimezone=Asia/Shanghai` | 参数 | 时区设置 |
| `allowPublicKeyRetrieval=true` | 参数 | 允许公钥检索 |

---

## 4.2 数据库初始化脚本

### 4.2.1 schema.sql 建表脚本

`db/schema.sql:1-112`

```sql
-- ============================================
-- RBAC 权限管理系统 - 数据库初始化脚本
-- 数据库: MySQL 8.0+
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `rbac_db`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE `rbac_db`;

-- 1. 用户表
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

-- 2. 角色表
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

-- 3. 菜单表
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

-- 4. 用户角色关联表
DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles` (
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`),
  KEY `idx_role_id` (`role_id`),
  CONSTRAINT `fk_ur_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_ur_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 5. 角色菜单关联表
DROP TABLE IF EXISTS `role_menus`;
CREATE TABLE `role_menus` (
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`),
  KEY `idx_menu_id` (`menu_id`),
  CONSTRAINT `fk_rm_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_rm_menu` FOREIGN KEY (`menu_id`) REFERENCES `menus` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';
```

### 4.2.2 表结构说明

```
┌─────────────────────────────────────────────────────────────────┐
│                    RBAC 数据库表结构                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌─────────────┐         ┌─────────────┐                        │
│  │   users     │         │   roles     │                        │
│  ├─────────────┤         ├─────────────┤                        │
│  │ id (PK)     │◀───┐    │ id (PK)     │◀───┐                   │
│  │ username    │    │    │ role_name   │    │                   │
│  │ password    │    │    │ role_key    │    │                   │
│  │ nickname    │    │    │ description │    │                   │
│  │ status      │    │    │ status      │    │                   │
│  └─────────────┘    │    └─────────────┘    │                   │
│         │           │           │           │                   │
│         │           │    ┌─────────────┐    │                   │
│         │           │    │ user_roles  │    │                   │
│         │           │    ├─────────────┤    │                   │
│         └───────────┼───▶│ user_id (FK)│    │                   │
│                     │    │ role_id (FK)│◀───┘                   │
│                     │    └─────────────┘                        │
│                     │           │                               │
│                     │    ┌─────────────┐    ┌─────────────┐    │
│                     │    │ role_menus  │    │   menus     │    │
│                     │    ├─────────────┤    ├─────────────┤    │
│                     │    │ role_id (FK)│◀───│ id (PK)     │    │
│                     │    │ menu_id (FK)│───▶│ menu_name   │    │
│                     │    └─────────────┘    │ parent_id   │    │
│                     │                       │ type        │    │
│                     │                       └─────────────┘    │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 4.3 初始化数据

### 4.3.1 data.sql 初始化脚本

`db/data.sql:1-74`

```sql
-- ============================================
-- RBAC 权限管理系统 - 初始化数据
-- ============================================

USE `rbac_db`;

-- 1. 初始化管理员用户 (密码: admin123)
INSERT INTO `users` (`id`, `username`, `password`, `nickname`, `status`, `create_by`) VALUES
(1, 'admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '超级管理员', 1, 'system'),
(2, 'user', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '普通用户', 1, 'system');

-- 2. 初始化角色
INSERT INTO `roles` (`id`, `role_name`, `role_key`, `description`, `status`, `create_by`) VALUES
(1, '超级管理员', 'admin', '拥有所有权限', 1, 'system'),
(2, '普通用户', 'user', '普通用户角色', 1, 'system');

-- 3. 用户角色关联
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
(1, 1),  -- admin -> 超级管理员
(2, 2);  -- user -> 普通用户

-- 4. 初始化菜单 (包含目录、菜单、按钮)
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

-- 5. 角色菜单关联 (管理员拥有所有权限)
INSERT INTO `role_menus` (`role_id`, `menu_id`) VALUES
-- 管理员拥有所有权限
(1, 15), (1, 16),
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6),
(1, 7), (1, 8), (1, 9), (1, 10),
(1, 11), (1, 12), (1, 13), (1, 14),
-- 普通用户只有查看权限
(2, 15), (2, 16),
(2, 1), (2, 2), (2, 7), (2, 11);
```

### 4.3.2 初始化数据说明

| 数据类型 | 数量 | 说明 |
|----------|------|------|
| 用户 | 2 | admin, user |
| 角色 | 2 | 超级管理员, 普通用户 |
| 菜单 | 16 | 目录、菜单、按钮 |
| 用户角色关联 | 2 | admin→超级管理员, user→普通用户 |
| 角色菜单关联 | 16 | 管理员拥有所有权限，普通用户只有查看权限 |

---

## 4.4 Redis 连接初始化

### 4.4.1 Redis 配置

`application-dev.yml:16-29`

```yaml
spring:
  # Redis 配置
  data:
    redis:
      host: localhost
      port: 6379
      password:
      database: 0
      timeout: 10000
      lettuce:
        pool:
          max-active: 8
          max-idle: 8
          min-idle: 0
          max-wait: -1ms
```

### 4.4.2 Redis 连接池参数

| 参数 | 值 | 说明 |
|------|-----|------|
| `host` | localhost | Redis 主机地址 |
| `port` | 6379 | Redis 端口 |
| `database` | 0 | 数据库编号 |
| `timeout` | 10000ms | 连接超时时间 |
| `max-active` | 8 | 最大活动连接数 |
| `max-idle` | 8 | 最大空闲连接数 |
| `min-idle` | 0 | 最小空闲连接数 |
| `max-wait` | -1ms | 获取连接最大等待时间 |

### 4.4.3 Lettuce 客户端配置

```
┌─────────────────────────────────────────────────────────────────┐
│                    Lettuce 连接池配置                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  LettuceClient                                                   │
│     │                                                           │
│     ├── 连接池                                                   │
│     │   ├── max-active: 8 (最大活动连接)                         │
│     │   ├── max-idle: 8 (最大空闲连接)                           │
│     │   ├── min-idle: 0 (最小空闲连接)                           │
│     │   └── max-wait: -1ms (无限等待)                           │
│     │                                                           │
│     ├── 连接配置                                                 │
│     │   ├── host: localhost                                     │
│     │   ├── port: 6379                                          │
│     │   ├── database: 0                                         │
│     │   └── timeout: 10000ms                                    │
│     │                                                           │
│     └── 连接测试                                                 │
│         └── PING 命令                                           │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 4.5 数据源初始化时序图

```
┌─────────────────────────────────────────────────────────────────┐
│                    Phase 4 数据源初始化时序图                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ApplicationContext        DataSource          MySQL            │
│        │                       │                 │              │
│        │  初始化数据源          │                 │              │
│        │──────────────────────▶│                 │              │
│        │                       │                 │              │
│        │                       │  1. 创建连接池   │              │
│        │                       │     (HikariCP)  │              │
│        │                       │────────────────▶│              │
│        │                       │                 │              │
│        │                       │  2. 预创建连接   │              │
│        │                       │     (5个连接)    │              │
│        │                       │────────────────▶│              │
│        │                       │                 │              │
│        │                       │  3. 执行 schema.sql            │
│        │                       │     ├── 创建 users 表          │
│        │                       │     ├── 创建 roles 表          │
│        │                       │     ├── 创建 menus 表          │
│        │                       │     ├── 创建 user_roles 表     │
│        │                       │     └── 创建 role_menus 表     │
│        │                       │────────────────▶│              │
│        │                       │                 │              │
│        │                       │  4. 执行 data.sql              │
│        │                       │     ├── 插入用户数据            │
│        │                       │     ├── 插入角色数据            │
│        │                       │     ├── 插入菜单数据            │
│        │                       │     └── 插入关联数据            │
│        │                       │────────────────▶│              │
│        │                       │                 │              │
│        │  初始化 Redis         │                 │              │
│        │──────────────────────▶│                 │              │
│        │                       │                 │              │
│        │                       │  5. 创建 Lettuce 连接          │
│        │                       │────────────────▶│ (Redis)      │
│        │                       │                 │              │
│        │  数据源初始化完成      │                 │              │
│        │◀──────────────────────│                 │              │
│        │                       │                 │              │
└─────────────────────────────────────────────────────────────────┘
```

---

## 4.6 脚本执行机制

### 4.6.1 Spring Boot 自动执行

Spring Boot 会自动执行 `classpath:db/schema.sql` 和 `classpath:db/data.sql`：

```yaml
spring:
  sql:
    init:
      mode: always  # always: 每次启动都执行; embedded: 仅嵌入式数据库; never: 不执行
      schema-locations: classpath:db/schema.sql
      data-locations: classpath:db/data.sql
```

### 4.6.2 执行顺序

```
┌─────────────────────────────────────────────────────────────────┐
│                    脚本执行顺序                                  │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. schema.sql                                                  │
│     ├── DROP TABLE IF EXISTS (删除旧表)                          │
│     ├── CREATE TABLE (创建新表)                                  │
│     └── 添加索引和约束                                           │
│                                                                 │
│  2. data.sql                                                    │
│     ├── INSERT INTO users (插入用户)                             │
│     ├── INSERT INTO roles (插入角色)                             │
│     ├── INSERT INTO user_roles (插入用户角色关联)                 │
│     ├── INSERT INTO menus (插入菜单)                             │
│     └── INSERT INTO role_menus (插入角色菜单关联)                 │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 4.7 RedisTemplate 配置

`RedisConfig.java:20-43`

```java
@Bean
public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    
    // 使用 Jackson2JsonRedisSerializer 序列化值
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
    
    Jackson2JsonRedisSerializer<Object> jsonSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
    StringRedisSerializer stringSerializer = new StringRedisSerializer();
    
    // key 使用 String 序列化
    template.setKeySerializer(stringSerializer);
    template.setHashKeySerializer(stringSerializer);
    
    // value 使用 JSON 序列化
    template.setValueSerializer(jsonSerializer);
    template.setHashValueSerializer(jsonSerializer);
    
    template.afterPropertiesSet();
    return template;
}
```

### 4.7.1 序列化方式

| 序列化方式 | 应用场景 | 说明 |
|------------|----------|------|
| `StringRedisSerializer` | Key | 字符串序列化，可读性好 |
| `Jackson2JsonRedisSerializer` | Value | JSON 序列化，支持复杂对象 |

---

## 4.8 MyBatis Plus 配置

`MybatisPlusConfig.java:16-46`

```java
@Configuration
public class MybatisPlusConfig {
    
    /**
     * 分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
    
    /**
     * 自动填充处理器
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
                this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
            }
            
            @Override
            public void updateFill(MetaObject metaObject) {
                this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
            }
        };
    }
}
```

### 4.8.1 分页插件配置

```
┌─────────────────────────────────────────────────────────────────┐
│                    MyBatis Plus 分页插件                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  MybatisPlusInterceptor                                         │
│     │                                                           │
│     └── PaginationInnerInterceptor                             │
│         ├── DbType.MYSQL (数据库类型)                            │
│         └── 支持分页查询                                         │
│                                                                 │
│  使用示例：                                                      │
│  Page<User> page = new Page<>(1, 10);                           │
│  Page<User> result = userMapper.selectPage(page, wrapper);      │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 4.8.2 自动填充配置

```
┌─────────────────────────────────────────────────────────────────┐
│                    MyBatis Plus 自动填充                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  插入时自动填充：                                                │
│  ├── createTime → LocalDateTime.now()                           │
│  └── updateTime → LocalDateTime.now()                           │
│                                                                 │
│  更新时自动填充：                                                │
│  └── updateTime → LocalDateTime.now()                           │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 4.9 数据源初始化完成标志

当以下条件满足时，数据源初始化阶段完成：

- [x] MySQL 连接池创建完成
- [x] 数据库连接测试通过
- [x] schema.sql 执行完成
- [x] data.sql 执行完成
- [x] Redis 连接创建完成
- [x] RedisTemplate 配置完成
- [x] MyBatis Plus 配置完成

**下一步：** 进入 Phase 5（Security 过滤器链配置阶段），开始配置 Spring Security。

---

## 4.10 常见问题

### Q1: 数据库连接失败怎么办？

**可能原因：**
1. MySQL 服务未启动
2. 连接参数配置错误
3. 用户名密码错误

**排查方法：**
```bash
# 检查 MySQL 服务状态
systemctl status mysql

# 测试数据库连接
mysql -h localhost -u root -p

# 查看日志
tail -f /var/log/mysql/error.log
```

### Q2: Redis 连接失败怎么办？

**可能原因：**
1. Redis 服务未启动
2. 连接参数配置错误
3. 网络问题

**排查方法：**
```bash
# 检查 Redis 服务状态
systemctl status redis

# 测试 Redis 连接
redis-cli ping

# 查看日志
tail -f /var/log/redis/redis-server.log
```

### Q3: 如何修改数据库初始化行为？

**配置选项：**

```yaml
spring:
  sql:
    init:
      mode: always  # always | embedded | never
      schema-locations: classpath:db/schema.sql
      data-locations: classpath:db/data.sql
      sql-encoding: UTF-8
      separator: ;
```

---

## 4.11 下一步

数据源初始化阶段完成后，Spring 将进入 Phase 5（Security 过滤器链配置阶段），开始：
1. 配置 CORS 跨域
2. 配置 Security 过滤器链
3. 配置 JWT 认证

详见 [Phase 5：Security 过滤器链配置阶段](./Phase5-Security过滤器链配置阶段.md)
