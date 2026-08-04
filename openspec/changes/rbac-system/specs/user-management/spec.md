## ADDED Requirements

### Requirement: 获取用户列表
系统 SHALL 支持分页查询用户列表，支持按用户名和状态筛选。

#### Scenario: 查询用户列表
- **WHEN** 管理员请求用户列表
- **THEN** 系统返回分页的用户数据，包含用户基本信息和角色列表

### Requirement: 创建用户
系统 SHALL 允许管理员创建新用户。

#### Scenario: 创建用户成功
- **WHEN** 管理员输入有效的用户信息（用户名、密码、角色等）
- **THEN** 系统创建用户并分配指定角色

#### Scenario: 创建用户失败 - 用户名已存在
- **WHEN** 管理员输入已存在的用户名
- **THEN** 系统返回错误提示"用户名已存在"

### Requirement: 更新用户
系统 SHALL 允许管理员更新用户信息。

#### Scenario: 更新用户信息
- **WHEN** 管理员修改用户的昵称、邮箱、手机号等信息
- **THEN** 系统保存更新后的用户信息

#### Scenario: 更新用户角色
- **WHEN** 管理员修改用户的角色
- **THEN** 系统更新用户的角色关联

### Requirement: 删除用户
系统 SHALL 允许管理员删除用户。

#### Scenario: 删除用户成功
- **WHEN** 管理员确认删除用户
- **THEN** 系统删除用户及其关联数据（逻辑删除）

### Requirement: 修改用户状态
系统 SHALL 允许管理员启用或禁用用户。

#### Scenario: 禁用用户
- **WHEN** 管理员禁用用户
- **THEN** 系统将用户状态设置为禁用，该用户无法登录

#### Scenario: 启用用户
- **WHEN** 管理员启用用户
- **THEN** 系统将用户状态设置为启用

### Requirement: 重置用户密码
系统 SHALL 允许管理员重置用户密码。

#### Scenario: 重置密码成功
- **WHEN** 管理员重置用户密码
- **THEN** 系统将用户密码重置为默认密码（123456）
