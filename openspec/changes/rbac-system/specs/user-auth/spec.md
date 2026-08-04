## ADDED Requirements

### Requirement: 用户登录
系统 SHALL 允许用户使用用户名和密码进行登录认证。

#### Scenario: 登录成功
- **WHEN** 用户输入正确的用户名和密码
- **THEN** 系统返回 JWT token 和用户信息（包括角色和权限列表）

#### Scenario: 登录失败 - 用户名不存在
- **WHEN** 用户输入不存在的用户名
- **THEN** 系统返回错误提示"用户名不存在"

#### Scenario: 登录失败 - 密码错误
- **WHEN** 用户输入错误的密码
- **THEN** 系统返回错误提示"密码错误"

#### Scenario: 登录失败 - 用户被禁用
- **WHEN** 用户状态为禁用
- **THEN** 系统返回错误提示"用户已被禁用"

### Requirement: 用户登出
系统 SHALL 允许已登录用户退出登录。

#### Scenario: 登出成功
- **WHEN** 已登录用户点击登出
- **THEN** 系统删除 Redis 中的 token，清除登录状态

### Requirement: 用户注册
系统 SHALL 允许新用户注册账号。

#### Scenario: 注册成功
- **WHEN** 用户输入有效的用户名、密码和其他信息
- **THEN** 系统创建用户账号，分配默认角色（普通用户）

#### Scenario: 注册失败 - 用户名已存在
- **WHEN** 用户输入已存在的用户名
- **THEN** 系统返回错误提示"用户名已存在"

### Requirement: Token 验证
系统 SHALL 在每次请求时验证 JWT token 的有效性。

#### Scenario: Token 有效
- **WHEN** 请求携带有效的 JWT token 且 token 存在于 Redis
- **THEN** 系统允许访问受保护的资源

#### Scenario: Token 无效或过期
- **WHEN** 请求携带无效或过期的 JWT token
- **THEN** 系统返回 401 未授权错误
