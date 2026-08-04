## Context

项目 `Standalone-Service-Learn/backend1` 是一个基于 Spring Boot 3.5.16 的 RBAC 权限管理系统。系统采用前后端分离架构，后端包含完整的用户认证、授权和 CRUD 功能。

**当前状态**：
- 代码结构完整，包含 Entity、DTO、Service、Controller、Security、Config 等模块
- 数据库使用 MySQL，缓存使用 Redis
- 安全框架使用 Spring Security + JWT
- ORM 使用 MyBatis Plus
- API 文档使用 Knife4j

**约束条件**：
- 文档必须结合工程实际代码进行说明
- 时序图使用 ASCII 格式，便于在命令行和 Markdown 中查看
- 文档位置必须放在 `backend1` 目录下

## Goals / Non-Goals

**Goals:**
- 生成一份详细的 Spring Boot 启动流程分析文档
- 文档包含完整的启动时序图（ASCII 格式）
- 文档结合工程代码，引用具体的类和方法
- 文档覆盖从 `main()` 到服务就绪的完整流程
- 文档包含请求处理流程的补充说明

**Non-Goals:**
- 不修改任何现有代码
- 不添加新的功能模块
- 不涉及前端相关分析
- 不涉及性能优化或架构改进

## Decisions

### 1. 文档格式：Markdown + ASCII 时序图

**选择**: 使用 Markdown 格式，时序图使用 ASCII 字符绘制

**理由**:
- Markdown 是最通用的技术文档格式
- ASCII 时序图可以在任何终端和 Markdown 查看器中显示
- 不依赖外部工具（如 PlantUML、Mermaid）
- 便于版本控制和代码审查

**替代方案**:
- PlantUML：需要额外的渲染工具
- Mermaid：某些 Markdown 渲染器不支持
- 纯文本：格式不够清晰

### 2. 文档结构：按启动阶段划分

**选择**: 将启动过程分为 6 个阶段进行分析

**理由**:
- 符合 Spring Boot 的实际启动流程
- 每个阶段职责清晰，便于理解
- 便于定位问题和调试

**阶段划分**:
1. 准备阶段（SpringApplication 初始化）
2. 扫描阶段（组件扫描）
3. 创建阶段（Bean 创建与依赖注入）
4. 数据源阶段（MySQL/Redis 连接）
5. Security 阶段（过滤器链配置）
6. Web 容器阶段（Tomcat 启动）

### 3. 代码引用方式：文件路径 + 行号

**选择**: 使用 `文件名:行号` 格式引用代码

**理由**:
- 可以精确定位到具体代码位置
- 便于读者查看源代码
- 符合技术文档的最佳实践

## Risks / Trade-offs

### 风险
1. **代码变更导致文档过时**
   - 缓解：文档基于当前代码版本，后续代码变更时需要同步更新文档

2. **ASCII 时序图可读性**
   - 缓解：使用清晰的格式和对齐，确保在等宽字体下可读

### 权衡
1. **详细程度 vs 可读性**: 选择适中的详细程度，既包含关键细节又不过于冗长
2. **完整性 vs 实用性**: 覆盖完整的启动流程，但重点放在核心组件上

## Migration Plan

### 部署步骤
1. 创建 `Standalone-Service-Learn/backend1/STARTUP_ANALYSIS.md` 文件
2. 写入文档内容
3. 验证文档格式和内容

### 回滚策略
1. 删除 `STARTUP_ANALYSIS.md` 文件
2. 恢复到文档创建前的状态

## Open Questions

1. 是否需要添加代码示例片段？
2. 是否需要包含故障排查指南？
3. 是否需要中英文双语版本？
