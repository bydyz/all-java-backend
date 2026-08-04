## Why

构建一个完整的 RBAC（基于角色的访问控制）权限管理系统，用于管理用户、角色和菜单权限。该系统提供前后端分离的架构，支持动态路由和按钮级权限控制，适用于需要精细化权限管理的企业级应用。

## What Changes

- 创建 Spring Boot 3.5.16 后端服务，包含完整的用户认证、授权和 CRUD 功能
- 创建 Vue3 + Element Plus 前端工程，提供可视化的管理界面
- 实现 JWT + Redis 的认证方案，支持 token 主动失效
- 实现动态路由，根据用户权限动态生成前端路由
- 实现按钮级权限控制，精确控制用户操作权限
- 提供完整的数据库初始化脚本

## Capabilities

### New Capabilities
- `user-auth`: 用户认证模块，包括登录、登出、注册功能
- `user-management`: 用户管理模块，包括用户的增删改查、状态管理
- `role-management`: 角色管理模块，包括角色的增删改查
- `menu-management`: 菜单管理模块，包括菜单的增删改查、树形结构管理
- `permission-control`: 权限控制模块，包括角色菜单权限分配、动态路由生成、按钮级权限控制

### Modified Capabilities

（无，这是全新项目）

## Impact

- **后端代码**: 创建完整的 Spring Boot 项目结构，包含 entity、mapper、service、controller、config、security 等模块
- **前端代码**: 创建完整的 Vue3 项目结构，包含 views、components、api、store、router、utils 等模块
- **数据库**: 创建 5 张表（users、roles、menus、user_roles、role_menus）及相关索引
- **依赖引入**: Spring Security、MyBatis Plus、JWT、Redis、Element Plus、Pinia、Vue Router
- **API 接口**: 创建认证、用户、角色、菜单相关的 RESTful API
