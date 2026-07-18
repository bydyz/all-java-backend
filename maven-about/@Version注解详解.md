# @Version 注解详解

## 一、@Version 是谁提供的功能

`@Version` 是 **MyBatis-Plus** 框架提供的注解，用于标记乐观锁字段，实现乐观锁功能。

- **全限定类名**: `com.baomidou.mybatisplus.annotation.Version`
- **所属项目**: [MyBatis-Plus](https://github.com/baomidou/mybatis-plus)
- **提供方**: 包头团队（baomidou）
- **引入版本**: MyBatis-Plus 3.0 起提供

## 二、最小依赖范围

### 1. 最小依赖（仅使用注解）

如果你只需要 `@Version` 注解本身，**最小依赖**是：

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-annotation</artifactId>
    <version>3.5.17</version>
</dependency>
```

该依赖的特性：
- **无额外依赖**：`mybatis-plus-annotation` 是一个纯注解包，不依赖任何其他库
- **包体积小**：仅包含注解定义，约 13KB
- **版本与 MyBatis-Plus 主版本保持一致**

### 2. 最小依赖的依赖链

```
mybatis-plus-annotation (3.5.17)
└── 无其他依赖
```

由于 `mybatis-plus-annotation` 没有依赖其他库，因此：
- **最小依赖的依赖**：无
- **再上一级依赖**：无

## 三、实际工程中使用的依赖

在实际项目中，我们通常不会只引入 `mybatis-plus-annotation`，而是根据 Spring Boot 版本选择对应的 Starter：

| Spring Boot 版本 | 推荐依赖 |
|------------------|----------|
| **Spring Boot 2.x** | `mybatis-plus-boot-starter` |
| **Spring Boot 3.x** | `mybatis-plus-spring-boot3-starter` |
| **Spring Boot 4.x** | `mybatis-plus-spring-boot4-starter` |

### 依赖关系对比

```
mybatis-plus-boot-starter (Spring Boot 2.x)
├── mybatis-plus-core
│   └── mybatis-plus-annotation  ← @Version 在这里
│   └── mybatis (3.5.x)
├── mybatis-plus-extension
└── mybatis-spring-boot-starter

mybatis-plus-spring-boot3-starter (Spring Boot 3.x)
├── mybatis-plus-core
│   └── mybatis-plus-annotation  ← @Version 在这里
│   └── mybatis (3.5.x)
├── mybatis-plus-extension
└── mybatis-spring-boot-starter (3.0.x+)

mybatis-plus-spring-boot4-starter (Spring Boot 4.x)
├── mybatis-plus-core
│   └── mybatis-plus-annotation  ← @Version 在这里
│   └── mybatis (3.5.x)
├── mybatis-plus-extension
└── mybatis-spring-boot-starter (4.0.x+)
```

### 本工程实际使用的依赖

本项目中使用 MyBatis-Plus 的模块均通过以下方式引入：

```xml
<!-- Spring Boot 2.x 项目 -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.4.1</version>
</dependency>

<!-- Spring Boot 3.x 项目 -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
    <version>3.5.16</version>
</dependency>
```

## 四、@Version 的默认值

### 注解属性

`@Version` 注解没有任何属性，它只是一个标记注解。

### 使用要求

1. **字段类型**：支持 `int`、`Integer`、`long`、`Long`、`Date`、`Timestamp`、`LocalDateTime` 等类型
2. **初始值**：需要在实体类中设置初始值（通常为 0 或 1）
3. **插件配置**：需要在配置类中注册乐观锁插件

### 乐观锁工作原理

当实体类字段标记了 `@Version` 后：

1. **更新操作**：自动在 WHERE 条件中追加版本号检查
   - 例如：`UPDATE user SET name = 'test', version = 1 WHERE id = 1 AND version = 0`

2. **版本号递增**：更新成功后，版本号自动 +1

3. **更新失败**：如果版本号不匹配，更新失败（影响行数为 0）

## 五、不同版本使用方法

### 1. MyBatis-Plus 3.0.x ~ 3.3.x

```java
import com.baomidou.mybatisplus.annotation.Version;

@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    
    @Version
    private Integer version;
}
```

**配置乐观锁插件**：
```java
@Configuration
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }
}
```

**使用方式**：
```java
// 查询用户
User user = userMapper.selectById(1);
// user.version = 0

// 更新用户
user.setUsername("new name");
int rows = userMapper.updateById(user);
// 实际执行：UPDATE user SET username = 'new name', version = 1 WHERE id = 1 AND version = 0

if (rows == 0) {
    throw new RuntimeException("更新失败，数据已被修改");
}
```

### 2. MyBatis-Plus 3.4.x

```java
import com.baomidou.mybatisplus.annotation.Version;

@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    
    @Version
    private Integer version;
}
```

**此版本变化**：
- 乐观锁插件配置方式不变
- 支持更多字段类型

### 3. MyBatis-Plus 3.5.x（当前最新）

```java
import com.baomidou.mybatisplus.annotation.Version;

@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    
    @Version
    private LocalDateTime updateTime;
}
```

**此版本变化**：
- 支持 `LocalDateTime` 类型
- 推荐使用 `LocalDateTime` 替代 `Date`

### 4. 版本对比表

| 特性 | 3.0.x ~ 3.3.x | 3.4.x | 3.5.x+ |
|------|----------------|--------|---------|
| `int`/`Integer` | ✅ | ✅ | ✅ |
| `long`/`Long` | ✅ | ✅ | ✅ |
| `Date` | ✅ | ✅ | ✅ |
| `Timestamp` | ✅ | ✅ | ✅ |
| `LocalDateTime` | ❌ | ❌ | ✅ 新增 |

## 六、使用示例

### 1. 基础用法：整数类型字段

最常见的用法，使用整数类型字段：

```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    
    @Version
    private Integer version;
}
```

数据库表结构：
```sql
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    version INT DEFAULT 0
);
```

### 2. 日期时间类型字段

使用日期时间类型字段：

```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    
    @Version
    private LocalDateTime updateTime;
}
```

数据库表结构：
```sql
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

### 3. 配合自动填充

```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    
    @Version
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer version;
}
```

```java
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "version", Integer.class, 0);
    }
    
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "version", Integer.class, 1);
    }
}
```

### 4. 强制更新（跳过乐观锁）

有时需要强制更新，可以使用 `UpdateWrapper`：

```java
UpdateWrapper<User> wrapper = new UpdateWrapper<>();
wrapper.eq("id", 1)
       .set("username", "new name")
       .set("version", 0);  // 强制设置版本号

int rows = userMapper.update(null, wrapper);
```

### 5. 批量更新（不支持乐观锁）

```java
// 注意：批量更新不支持乐观锁
List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
users.forEach(user -> user.setUsername("new name"));
userMapper.updateBatchById(users);
// 批量更新时不会追加版本号检查
```

### 6. 乐观锁异常处理

```java
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    
    @Transactional
    public void updateUser(Long id, String username) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        user.setUsername(username);
        int rows = userMapper.updateById(user);
        
        if (rows == 0) {
            throw new RuntimeException("更新失败，数据已被其他用户修改");
        }
    }
}
```

## 七、最佳实践

1. **字段类型选择**：
   - 推荐使用 `Integer` 或 `Long` 类型
   - 避免使用 `Date` 类型，推荐使用 `LocalDateTime`

2. **初始值设置**：
   - 整数类型：初始值通常为 0 或 1
   - 日期时间类型：使用 `@TableField(fill = FieldFill.INSERT)` 自动填充

3. **插件配置**：
   - 必须配置乐观锁插件
   - 建议在配置类中统一配置

4. **异常处理**：
   - 更新失败时需要捕获异常
   - 给用户友好的提示信息

5. **批量更新注意**：
   - 批量更新不支持乐观锁
   - 需要逐个更新或使用自定义 SQL

6. **性能考虑**：
   - 乐观锁适合读多写少的场景
   - 写多读少的场景考虑使用悲观锁

## 八、常见问题

### Q1: 乐观锁更新失败怎么办？

捕获异常并提示用户：
```java
int rows = userMapper.updateById(user);
if (rows == 0) {
    throw new RuntimeException("更新失败，数据已被其他用户修改");
}
```

### Q2: 如何强制更新？

使用 `UpdateWrapper` 强制设置版本号：
```java
UpdateWrapper<User> wrapper = new UpdateWrapper<>();
wrapper.eq("id", 1).set("version", 0);
```

### Q3: 批量更新支持乐观锁吗？

不支持，批量更新时不会追加版本号检查。

### Q4: 乐观锁字段可以为空吗？

不建议为空，因为更新时会追加版本号检查。如果字段为空，可能导致更新失败。

## 九、参考链接

- [MyBatis-Plus 官方文档](https://baomidou.com/)
- [MyBatis-Plus 乐观锁](https://baomidou.com/en/features/optimistic-locker/)
- [MyBatis-Plus GitHub](https://github.com/baomidou/mybatis-plus)
- [Maven Repository - mybatis-plus-annotation](https://mvnrepository.com/artifact/com.baomidou/mybatis-plus-annotation)
