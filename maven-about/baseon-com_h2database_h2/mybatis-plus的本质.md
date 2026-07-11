# MyBatis-Plus 的本质：运行时必须连接数据库

## 核心本质

MyBatis-Plus 是一个 **ORM（对象关系映射）** 框架，其存在的唯一目的就是通过 Java 代码与数据库中的表进行交互。因此，在运行时，框架**必须**连接一个真实存在的数据库，否则无法工作。

## 三要素缺一不可

在 Spring Boot 项目中使用 MyBatis-Plus，以下三个要素必须同时具备：

1. **MyBatis-Plus Starter 依赖**
   - 提供框架核心功能

2. **数据库驱动依赖**
   - 如 MySQL 驱动，建立 Java 与数据库之间的通信协议

3. **数据源配置**
   - 数据库连接信息：URL、用户名、密码等

## 必要配置详解

### 1. 添加数据库驱动依赖

仅引入 `mybatis-plus-boot-starter` 是不够的，必须同时添加对应的 JDBC 驱动。

```xml
<!-- MySQL 驱动依赖 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 2. 配置数据源

在 Spring Boot 配置文件中提供数据库连接信息。

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_database?useUnicode=true&characterEncoding=utf8
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
```

## 没有数据库的后果

如果在配置中缺少正确的数据库连接，Spring Boot 项目将无法启动，并抛出以下异常之一：

```
Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.
```

或

```
CommunicationsException: Communications link failure
```

## 代码生成器场景

💡 **唯一的例外情况**

使用 MyBatis-Plus 的代码生成器（Code Generator）生成实体类、Mapper 等文件时，**仍需连接数据库**。

代码生成器需要读取表结构（TableInfo）来生成对应的实体类和接口。手动硬编码表字段信息的情况极少，因此连数据库依然是常态。

## 总结

你的理解非常准确：**MyBatis-Plus = 操作数据库的工具，使用它 = 必须连接数据库。**

在 Spring Boot 项目中，MyBatis-Plus 的运行环境由以下三个部分组成：

- ✅ `mybatis-plus-boot-starter`：提供框架功能
- ✅ 数据库驱动（如 `mysql-connector`）：建立通信协议
- ✅ `spring.datasource` 配置：提供连接信息

三者缺一不可，共同构成了 MyBatis-Plus 的完整运行环境。
