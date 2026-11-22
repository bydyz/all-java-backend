### 如何将 IDEA 中 Spring Boot 项目的接口快速导入 Apifox

将 IDEA 中 Spring Boot 项目的接口快速导入 Apifox，最推荐、最高效的方式是使用官方的 **Apifox Helper** 插件。它可以直接解析本地 Java 源代码，实现**代码零侵入**，一键同步接口文档。

以下是具体的操作步骤：

#### 1. 安装 Apifox Helper 插件
在 IDEA 中打开插件市场，搜索并安装官方插件：
*   **路径**：`File` -> `Settings` (Windows) 或 `IntelliJ IDEA` -> `Settings` (Mac) -> `Plugins` -> `Marketplace`
*   **搜索**：输入 `Apifox Helper` 并点击安装。
*   **重启**：安装完成后，重启 IDEA 使插件生效。

#### 2. 获取 Apifox 访问令牌与项目 ID
在同步之前，需要获取 Apifox 的授权令牌和目标项目的 ID：
*   **获取 API 访问令牌**：
  1. 打开 Apifox 客户端或网页版，点击右上角头像 -> `账号设置`。
  2. 选择 `API 访问令牌`，点击 `新建`。
  3. 输入名称并选择有效期（建议选“无限期”），点击保存。**注意：生成的令牌只显示一次，请务必复制并妥善保存**。
*   **获取项目 ID**：
  1. 在 Apifox 中进入你准备导入接口的目标项目。
  2. 点击左侧底部的 `项目设置` -> `基本设置`。
  3. 在页面中找到并复制你的**项目 ID**。

#### 3. 在 IDEA 中配置插件
回到 IDEA，将获取到的令牌和项目 ID 填入插件设置中：
*   **路径**：`File` -> `Settings` -> `其他设置` (Other Settings) -> `Apifox Helper`。
*   **配置项**：
  1. **Apifox 服务器地址**：SaaS 版默认填 `https://api.apifox.cn` 即可。
  2. **API 访问令牌**：粘贴刚才复制的令牌，点击“测试令牌”确认成功。
  3. **模块项目 ID 配置**：填写你的代码模块名与 Apifox 项目 ID 的映射关系。格式为 `ModuleName:ProjectID`（例如：`mall-admin:2051427`）。如果是多模块项目，可以配置多行。
*   点击 `Apply` 和 `OK` 保存设置。

#### 4. 一键同步接口到 Apifox
配置完成后，即可在 IDEA 中快速上传接口：
*   **单个/多个文件上传**：在 IDEA 左侧项目结构中，右键点击你的 Controller 文件（或包含多个 Controller 的文件夹），在菜单中选择 `Upload to Apifox`。
*   **查看结果**：上传完成后，回到 Apifox 客户端，点击右上角的**刷新**按钮，即可看到自动生成的接口目录、请求参数和返回示例。

---

####  补充方案：通过 Swagger/OpenAPI 导入
如果你的 Spring Boot 项目已经集成了 Swagger（如 `springdoc-openapi-ui` 或 `Knife4j`），并且服务正在运行，你也可以不用插件，直接在 Apifox 中导入：
1. 在 Apifox 中进入项目 -> `项目设置` -> `导入数据`。
2. 选择 `OpenAPI/Swagger` 格式。
3. 选择 **URL 方式导入**，填入你本地或测试环境的 Swagger JSON 地址（例如：`http://localhost:8080/v3/api-docs` 或 `/v2/api-docs`）。
4. 你也可以配置**定时导入**，让 Apifox 每隔几小时自动拉取最新的 Swagger 数据，保持文档同步。

**总结建议**：日常开发中，**Apifox Helper 插件**是体验最好、最实时的方案，写完代码右键一点即可同步；如果项目已有完善的 Swagger 配置且需要自动化集成，URL 定时导入也是不错的选择。
