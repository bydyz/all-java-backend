## ADDED Requirements

### Requirement: 获取菜单树
系统 SHALL 支持获取菜单的树形结构。

#### Scenario: 获取完整菜单树
- **WHEN** 管理员请求菜单树
- **THEN** 系统返回包含所有菜单（目录、菜单、按钮）的树形结构

#### Scenario: 按类型获取菜单树
- **WHEN** 管理员请求指定类型的菜单树
- **THEN** 系统返回仅包含该类型菜单的树形结构

### Requirement: 创建菜单
系统 SHALL 允许管理员创建新菜单。

#### Scenario: 创建目录菜单
- **WHEN** 管理员创建类型为"目录"的菜单
- **THEN** 系统创建目录菜单，设置 component 为 Layout

#### Scenario: 创建页面菜单
- **WHEN** 管理员创建类型为"菜单"的菜单
- **THEN** 系统创建页面菜单，设置路由路径和组件路径

#### Scenario: 创建按钮权限
- **WHEN** 管理员创建类型为"按钮"的菜单
- **THEN** 系统创建按钮权限，设置权限标识

### Requirement: 更新菜单
系统 SHALL 允许管理员更新菜单信息。

#### Scenario: 更新菜单信息
- **WHEN** 管理员修改菜单的名称、图标、路径等信息
- **THEN** 系统保存更新后的菜单信息

### Requirement: 删除菜单
系统 SHALL 允许管理员删除菜单。

#### Scenario: 删除菜单成功
- **WHEN** 管理员确认删除菜单
- **THEN** 系统删除菜单及其关联数据

#### Scenario: 删除菜单失败 - 存在子菜单
- **WHEN** 管理员尝试删除存在子菜单的菜单
- **THEN** 系统返回错误提示"存在子菜单，不允许删除"
