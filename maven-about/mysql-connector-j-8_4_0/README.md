# Spring Boot + MySQL 示例项目

这个项目展示了如何使用 Spring Boot 3.5.16 和 MySQL Connector/J 构建应用程序。

## 项目结构

```
src/main/java/org/example/
├── Application.java           # Spring Boot 主应用类
├── config/
│   └── SwaggerConfig.java     # Swagger 配置类
├── controller/
│   └── UserController.java    # REST API 控制器
├── crud/
│   └── CrudDemoRunner.java    # CRUD 操作示例
├── database/
│   ├── DatabaseConnectionDemo.java    # 数据库连接示例
│   └── DatabaseDemoRunner.java        # 数据库连接运行器
├── exception/
│   └── GlobalExceptionHandler.java    # 全局异常处理
├── model/
│   └── User.java              # 用户实体类
├── repository/
│   └── UserRepository.java    # 数据访问层
└── service/
    └── UserService.java       # 业务逻辑层
```

## 功能示例

### 1. 基础数据库连接

- 使用 `JdbcTemplate` 进行数据库连接
- 测试数据库连接状态
- 执行简单查询

### 2. CRUD 操作

- **创建用户**：使用 `UserService.createUser()` 方法
- **查询用户**：支持根据 ID 和用户名查询
- **更新用户**：使用 `UserService.updateUser()` 方法
- **删除用户**：使用 `UserService.deleteUser()` 方法

### 3. REST API

- `GET /api/users` - 获取所有用户
- `GET /api/users/{id}` - 根据 ID 获取用户
- `POST /api/users` - 创建用户
- `PUT /api/users/{id}` - 更新用户
- `DELETE /api/users/{id}` - 删除用户
- `GET /api/users/count` - 获取用户数量

## 配置说明

数据库配置在 `src/main/resources/application.properties` 中：

```properties
# MySQL 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/testdb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

## 运行步骤

1. **确保 MySQL 服务器正在运行**

2. **创建数据库**
   ```sql
   CREATE DATABASE testdb;
   ```

3. **修改数据库配置**
   - 编辑 `src/main/resources/application.properties` 文件
   - 修改数据库连接信息（用户名、密码等）

4. **编译和运行**
   ```bash
   # 使用 Maven 编译
   mvn clean compile
   
   # 运行应用程序
   mvn spring-boot:run
   ```

5. **测试 REST API**
   ```bash
   # 创建用户
   curl -X POST -H "Content-Type: application/json" -d '{"username":"zhangsan","email":"zhangsan@example.com"}' http://localhost:8080/api/users
   
   # 获取所有用户
   curl http://localhost:8080/api/users
   
   # 获取用户数量
   curl http://localhost:8080/api/users/count
   ```

6. **访问 Swagger UI**
   - 启动服务后，打开浏览器访问：http://localhost:8080/swagger-ui.html
   - API 文档：http://localhost:8080/v3/api-docs

## 技术栈

- **Spring Boot 3.5.16**
- **MySQL Connector/J** - MySQL 数据库驱动
- **Spring JDBC** - 数据库访问
- **Spring Web** - REST API 支持
- **SpringDoc OpenAPI** - Swagger API 文档
- **JUnit 5** - 单元测试

## 注意事项

1. 确保 MySQL 服务器版本与驱动兼容
2. 如果使用 MySQL 8.0+，需要添加 `allowPublicKeyRetrieval=true` 参数
3. 生产环境中请使用更强的密码和适当的权限配置
4. 建议使用连接池（如 HikariCP）来优化数据库连接性能

## 扩展功能

这个项目可以进一步扩展为：

- 添加用户认证和授权
- 实现分页和排序功能
- 添加数据验证
- ✅ 已实现 RESTful API 文档（Swagger）
- 添加日志记录和监控
- 实现数据库迁移（Flyway 或 Liquibase）