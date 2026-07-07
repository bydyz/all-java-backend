# MyBatis-Plus Spring Boot 应用使用指南

## ✅ 应用状态

应用已成功修复并可以启动！

## 🚀 如何启动应用

### 方法1：使用 Maven
```bash
cd E:\Project\AAA_All_MINE\all-java-backend\maven-about\mybatis-plus
mvn spring-boot:run
```

### 方法2：打包后运行
```bash
mvn clean package
java -jar target/mybatis-plus-1.0.jar
```

## 📍 API 接口

应用启动成功后，您可以访问以下 API 接口：

### 1. 应用健康检查
- **URL**: `http://localhost:8080/health`
- **方法**: GET
- **说明**: 检查应用是否正常运行

### 2. 获取所有用户
- **URL**: `http://localhost:8080/api/users`
- **方法**: GET
- **说明**: 查询系统中的所有用户

### 3. 获取单个用户
- **URL**: `http://localhost:8080/api/users/{id}`
- **方法**: GET
- **说明**: 根据用户 ID 查询用户信息

### 4. 创建用户
- **URL**: `http://localhost:8080/api/users`
- **方法**: POST
- **请求头**: `Content-Type: application/json`
- **请求体**:
  ```json
  {
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com",
    "age": 25,
    "status": 1
  }
  ```

### 5. 更新用户
- **URL**: `http://localhost:8080/api/users/{id}`
- **方法**: PUT
- **请求头**: `Content-Type: application/json`
- **请求体**:
  ```json
  {
    "username": "updateduser",
    "email": "updated@example.com",
    "age": 30
  }
  ```

### 6. 删除用户
- **URL**: `http://localhost:8080/api/users/{id}`
- **方法**: DELETE
- **说明**: 根据用户 ID 删除用户

### 7. H2 控制台
- **URL**: `http://localhost:8080/h2-console`
- **说明**: 访问 H2 数据库管理界面

## 📝 测试示例

### 使用浏览器测试
直接在浏览器中打开以下 URL：

1. http://localhost:8080/health
2. http://localhost:8080/api/users

### 使用 curl 测试
```bash
# 测试健康检查
curl http://localhost:8080/health

# 获取所有用户
curl http://localhost:8080/api/users

# 创建新用户
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"testuser\",\"password\":\"password123\",\"email\":\"test@example.com\",\"age\":25,\"status\":1}"
```

### 使用 Postman 测试
1. 打开 Postman
2. 创建新的请求
3. 选择 HTTP 方法（GET/POST/PUT/DELETE）
4. 输入请求 URL
5. 如果是 POST/PUT 请求，添加请求头 `Content-Type: application/json`
6. 添加请求体（JSON 格式）
7. 发送请求

## 🛑 停止应用

按 `Ctrl+C` 或关闭命令行窗口即可停止应用。

## 📊 数据库信息

- **数据库类型**: H2 内存数据库
- **连接地址**: `jdbc:h2:mem:testdb`
- **用户名**: `sa`
- **密码**: （空）
- **控制台**: http://localhost:8080/h2-console

## 🔧 修复的问题

1. ✅ 编码问题（GBK → UTF-8）
2. ✅ 缺少 Spring Boot Maven 插件
3. ✅ SQL 语法不兼容（MySQL → H2）
4. ✅ 缺少 Web 依赖
5. ✅ 缺少 Controller 类
6. ✅ 应用启动后立即退出的问题

## 📁 项目结构

```
mybatis-plus/
├── src/main/java/org/rc/mybatisplus/
│   ├── Application.java          # 主应用类
│   ├── controller/
│   │   └── UserController.java   # 用户控制器
│   ├── config/
│   │   └── MybatisPlusConfig.java # MyBatis-Plus 配置
│   ├── entity/
│   │   └── User.java             # 用户实体
│   ├── handler/
│   │   └── AutoFillHandler.java  # 自动填充处理器
│   ├── mapper/
│   │   └── UserMapper.java       # 用户 Mapper
│   └── service/
│       ├── UserService.java      # 用户服务接口
│       └── impl/
│           └── UserServiceImpl.java # 用户服务实现
├── src/main/resources/
│   ├── application.yml            # 应用配置
│   └── schema.sql                 # 数据库表结构
└── pom.xml                        # Maven 配置文件
```

## 🎉 成功启动日志示例

```
Started Application in 1.4 seconds (process running for 1.545)
==================================
应用已启动，按 Ctrl+C 停止
API 接口：
  健康检查: GET http://localhost:8080/health
  获取所有用户: GET http://localhost:8080/api/users
==================================
```

## ⚠️ 注意事项

1. 应用使用 H2 内存数据库，重启后会清空数据
2. 确保端口 8080 没有被其他应用占用
3. 使用 Maven 2.0 或更高版本
4. 确保 Java 17 或更高版本已安装
5. 如果看到端口占用错误，请停止占用 8080 端口的进程
