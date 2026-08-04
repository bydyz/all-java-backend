## ADDED Requirements

### Requirement: 分配角色菜单权限
系统 SHALL 允许管理员为角色分配菜单权限。

#### Scenario: 分配权限成功
- **WHEN** 管理员为角色选择菜单权限
- **THEN** 系统保存角色与菜单的关联关系

### Requirement: 获取用户有权限的菜单
系统 SHALL 根据用户角色返回有权限的菜单列表。

#### Scenario: 获取用户菜单树
- **WHEN** 已登录用户请求菜单树
- **THEN** 系统返回该用户有权限访问的菜单树（仅目录和菜单）

### Requirement: 动态路由生成
前端系统 SHALL 根据用户权限动态生成路由。

#### Scenario: 生成动态路由
- **WHEN** 用户登录成功
- **THEN** 前端请求后端获取有权限的菜单，动态添加到路由

#### Scenario: 访问无权限路由
- **WHEN** 用户尝试访问无权限的路由
- **THEN** 前端重定向到 404 页面

### Requirement: 按钮级权限控制
前端系统 SHALL 根据用户权限控制按钮的显示。

#### Scenario: 显示有权限的按钮
- **WHEN** 用户拥有某个按钮的权限
- **THEN** 前端显示该按钮

#### Scenario: 隐藏无权限的按钮
- **WHEN** 用户没有某个按钮的权限
- **THEN** 前端隐藏该按钮（通过 v-permission 指令）

### Requirement: 获取当前用户信息
系统 SHALL 提供获取当前登录用户信息的接口。

#### Scenario: 获取用户信息成功
- **WHEN** 已登录用户请求用户信息
- **THEN** 系统返回用户基本信息、角色列表和权限列表
