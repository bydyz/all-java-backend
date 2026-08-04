## Why

项目 `Standalone-Service-Learn/backend1` 是一个基于 Spring Boot 3.5.16 的 RBAC 权限管理系统，但缺少对系统启动流程的详细文档。对于团队协作和新人上手来说，理解从 `RbacApplication.main()` 到服务就绪的完整流程至关重要。需要生成一份包含时序图的详细分析文档，帮助开发者快速理解系统的初始化过程。

## What Changes

- 创建 `Standalone-Service-Learn/backend1/STARTUP_ANALYSIS.md` 文档
- 文档包含 Spring Boot 启动的 6 个阶段详细分析
- 文档包含完整的启动时序图（ASCII 格式）
- 文档结合工程代码进行说明，引用具体类和方法
- 文档包含请求处理流程的补充说明

## Capabilities

### New Capabilities
- `startup-documentation`: Spring Boot 启动流程分析文档，包含时序图和代码引用

### Modified Capabilities

（无）

## Impact

- **文档**: 新增 `Standalone-Service-Learn/backend1/STARTUP_ANALYSIS.md`
- **代码**: 无代码变更，纯文档输出
- **依赖**: 无新增依赖
