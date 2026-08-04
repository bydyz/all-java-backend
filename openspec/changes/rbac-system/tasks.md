## 1. 项目基础搭建

- [ ] 1.1 创建后端 Spring Boot 项目结构
- [ ] 1.2 创建前端 Vue3 项目结构
- [ ] 1.3 配置后端 application.yml 和 application-dev.yml
- [ ] 1.4 配置前端 vite.config.ts 和 tsconfig.json

## 2. 数据库设计

- [ ] 2.1 创建数据库建表脚本 (schema.sql)
- [ ] 2.2 创建初始化数据脚本 (data.sql)
- [ ] 2.3 创建用户表 (users) 及相关索引
- [ ] 2.4 创建角色表 (roles) 及相关索引
- [ ] 2.5 创建菜单表 (menus) 及相关索引
- [ ] 2.6 创建用户角色关联表 (user_roles)
- [ ] 2.7 创建角色菜单关联表 (role_menus)

## 3. 后端实体类和 Mapper

- [ ] 3.1 创建 User 实体类
- [ ] 3.2 创建 Role 实体类
- [ ] 3.3 创建 Menu 实体类
- [ ] 3.4 创建 UserRole 关联实体
- [ ] 3.5 创建 RoleMenu 关联实体
- [ ] 3.6 创建 UserMapper 接口
- [ ] 3.7 创建 RoleMapper 接口
- [ ] 3.8 创建 MenuMapper 接口
- [ ] 3.9 创建 UserRoleMapper 接口
- [ ] 3.10 创建 RoleMenuMapper 接口

## 4. 后端 DTO 和公共类

- [ ] 4.1 创建 Result 统一响应类
- [ ] 4.2 创建 PageResult 分页响应类
- [ ] 4.3 创建 Constants 常量类
- [ ] 4.4 创建 MenuType 枚举
- [ ] 4.5 创建 StatusEnum 枚举
- [ ] 4.6 创建 LoginRequest 请求类
- [ ] 4.7 创建 RegisterRequest 请求类
- [ ] 4.8 创建 UserCreateRequest 请求类
- [ ] 4.9 创建 UserUpdateRequest 请求类
- [ ] 4.10 创建 RoleCreateRequest 请求类
- [ ] 4.11 创建 RoleUpdateRequest 请求类
- [ ] 4.12 创建 MenuCreateRequest 请求类
- [ ] 4.13 创建 MenuUpdateRequest 请求类
- [ ] 4.14 创建 LoginResponse 响应类
- [ ] 4.15 创建 UserResponse 响应类
- [ ] 4.16 创建 RoleResponse 响应类
- [ ] 4.17 创建 MenuTreeResponse 响应类

## 5. 后端安全模块

- [ ] 5.1 创建 JwtTokenProvider 工具类
- [ ] 5.2 创建 JwtAuthenticationFilter 过滤器
- [ ] 5.3 创建 UserDetailsServiceImpl 用户详情服务
- [ ] 5.4 创建 SecurityUtils 安全工具类
- [ ] 5.5 配置 SecurityConfig 安全配置

## 6. 后端 Service 层

- [ ] 6.1 创建 UserService 接口
- [ ] 6.2 创建 UserServiceImpl 实现类
- [ ] 6.3 创建 RoleService 接口
- [ ] 6.4 创建 RoleServiceImpl 实现类
- [ ] 6.5 创建 MenuService 接口
- [ ] 6.6 创建 MenuServiceImpl 实现类

## 7. 后端 Controller 层

- [ ] 7.1 创建 AuthController 认证控制器
- [ ] 7.2 创建 UserController 用户控制器
- [ ] 7.3 创建 RoleController 角色控制器
- [ ] 7.4 创建 MenuController 菜单控制器

## 8. 后端配置类

- [ ] 8.1 创建 CorsConfig 跨域配置
- [ ] 8.2 创建 RedisConfig Redis 配置
- [ ] 8.3 创建 MybatisPlusConfig MyBatis Plus 配置
- [ ] 8.4 创建 Knife4jConfig 接口文档配置
- [ ] 8.5 创建 RbacApplication 启动类

## 9. 前端基础配置

- [ ] 9.1 创建 package.json 依赖配置
- [ ] 9.2 创建 main.ts 入口文件
- [ ] 9.3 创建 App.vue 根组件
- [ ] 9.4 创建全局样式 index.scss

## 10. 前端工具和配置

- [ ] 10.1 创建 utils/auth.ts Token 管理
- [ ] 10.2 创建 utils/request.ts Axios 封装
- [ ] 10.3 创建 utils/validate.ts 验证工具
- [ ] 10.4 创建 directives/permission.ts 权限指令

## 11. 前端 API 层

- [ ] 11.1 创建 api/auth.ts 认证接口
- [ ] 11.2 创建 api/user.ts 用户接口
- [ ] 11.3 创建 api/role.ts 角色接口
- [ ] 11.4 创建 api/menu.ts 菜单接口
- [ ] 11.5 创建 api/permission.ts 权限接口

## 12. 前端状态管理

- [ ] 12.1 创建 store/user.ts 用户状态管理
- [ ] 12.2 创建 store/app.ts 应用状态管理

## 13. 前端路由

- [ ] 13.1 创建 router/staticRoutes.ts 静态路由
- [ ] 13.2 创建 router/index.ts 路由配置和守卫

## 14. 前端页面组件

- [ ] 14.1 创建 views/login/index.vue 登录页面
- [ ] 14.2 创建 views/layout/index.vue 布局页面
- [ ] 14.3 创建 views/dashboard/index.vue 仪表盘页面
- [ ] 14.4 创建 components/Breadcrumb/index.vue 面包屑组件

## 15. 前端系统管理页面

- [ ] 15.1 创建 views/system/user/index.vue 用户管理页面
- [ ] 15.2 创建 views/system/role/index.vue 角色管理页面
- [ ] 15.3 创建 views/system/menu/index.vue 菜单管理页面
