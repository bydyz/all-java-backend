# @TableLogic 注解详解

## 一、@TableLogic 是谁提供的功能

`@TableLogic` 是 **MyBatis-Plus** 框架提供的注解，用于标记逻辑删除字段，实现数据的逻辑删除功能。

- **全限定类名**: `com.baomidou.mybatisplus.annotation.TableLogic`
- **所属项目**: [MyBatis-Plus](https://github.com/baomidou/mybatis-plus)
- **提供方**: 包头团队（baomidou）
- **引入版本**: MyBatis-Plus 3.0 起提供

## 二、最小依赖范围

### 1. 最小依赖（仅使用注解）

如果你只需要 `@TableLogic` 注解本身，**最小依赖**是：

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
│   └── mybatis-plus-annotation  ← @TableLogic 在这里
│   └── mybatis (3.5.x)
├── mybatis-plus-extension
└── mybatis-spring-boot-starter

mybatis-plus-spring-boot3-starter (Spring Boot 3.x)
├── mybatis-plus-core
│   └── mybatis-plus-annotation  ← @TableLogic 在这里
│   └── mybatis (3.5.x)
├── mybatis-plus-extension
└── mybatis-spring-boot-starter (3.0.x+)

mybatis-plus-spring-boot4-starter (Spring Boot 4.x)
├── mybatis-plus-core
│   └── mybatis-plus-annotation  ← @TableLogic 在这里
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

## 四、@TableLogic 的默认值

### 注解属性及默认值

| 属性 | 类型 | 是否必须 | 默认值 | 说明 |
|------|------|----------|--------|------|
| `value` | `String` | 否 | `""` (空字符串) | 逻辑未删除值（数据库中表示"未删除"的值） |
| `delval` | `String` | 否 | `""` (空字符串) | 逻辑删除值（数据库中表示"已删除"的值） |

### 默认值行为

1. **`value` 默认值**：当 `value` 为空字符串时，会使用全局配置的逻辑未删除值
   - 全局配置：`mybatis-plus.global-config.db-config.logic-not-delete-value`
   - 默认全局值：`"0"`

2. **`delval` 默认值**：当 `delval` 为空字符串时，会使用全局配置的逻辑删除值
   - 全局配置：`mybatis-plus.global-config.db-config.logic-delete-value`
   - 默认全局值：`"1"`

### 逻辑删除工作原理

当实体类字段标记了 `@TableLogic` 后：

1. **查询操作**：自动追加 WHERE 条件 `WHERE 逻辑未删除字段 = 逻辑未删除值`
   - 例如：`SELECT * FROM user WHERE deleted = 0`

2. **删除操作**：自动转换为 UPDATE 操作
   - 例如：`DELETE FROM user WHERE id = 1` → `UPDATE user SET deleted = 1 WHERE id = 1`

3. **更新操作**：自动追加 WHERE 条件
   - 例如：`UPDATE user SET name = 'test' WHERE id = 1` → `UPDATE user SET name = 'test' WHERE id = 1 AND deleted = 0`

4. **插入操作**：不受影响，正常插入

## 五、不同版本使用方法

### 1. MyBatis-Plus 3.0.x ~ 3.3.x

```java
import com.baomidou.mybatisplus.annotation.TableLogic;

@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    
    @TableLogic
    private Integer deleted;
}
```

**此版本支持的属性**：
- `value` - 逻辑未删除值
- `delval` - 逻辑删除值

**使用方式**：
```java
// 删除操作
userMapper.deleteById(1);
// 实际执行：UPDATE user SET deleted = 1 WHERE id = 1 AND deleted = 0

// 查询操作
userMapper.selectList(null);
// 实际执行：SELECT * FROM user WHERE deleted = 0
```

### 2. MyBatis-Plus 3.4.x

```java
import com.baomidou.mybatisplus.annotation.TableLogic;

@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;
}
```

**此版本变化**：
- 支持自定义逻辑未删除值和删除值
- 可以使用字符串类型字段

### 3. MyBatis-Plus 3.5.x（当前最新）

```java
import com.baomidou.mybatisplus.annotation.TableLogic;

@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    
    @TableLogic(value = "N", delval = "Y")
    private String deleted;
}
```

**此版本变化**：
- 完全支持所有属性
- 支持字符串类型的逻辑删除字段

### 4. 版本对比表

| 特性 | 3.0.x ~ 3.3.x | 3.4.x | 3.5.x+ |
|------|----------------|--------|---------|
| `value` | ✅ | ✅ | ✅ |
| `delval` | ✅ | ✅ | ✅ |
| 自定义值 | ✅ | ✅ | ✅ |
| 字符串类型字段 | ✅ | ✅ | ✅ |

## 六、使用示例

### 1. 基础用法：整数类型字段

最常见的用法，使用整数类型字段：

```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    
    @TableLogic
    private Integer deleted;
}
```

数据库表结构：
```sql
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    deleted INT DEFAULT 0
);
```

### 2. 字符串类型字段

使用字符串类型字段：

```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    
    @TableLogic(value = "N", delval = "Y")
    private String deleted;
}
```

数据库表结构：
```sql
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    deleted CHAR(1) DEFAULT 'N'
);
```

### 3. 自定义值

自定义逻辑未删除值和删除值：

```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    
    @TableLogic(value = "ACTIVE", delval = "DELETED")
    private String status;
}
```

### 4. 配合全局配置

```yaml
# application.yml
mybatis-plus:
  global-config:
    db-config:
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0
```

```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    
    @TableLogic  // 使用全局配置的值
    private Integer deleted;
}
```

### 5. 强制查询已删除数据

有时需要查询已删除的数据，可以使用 `@InterceptorIgnore`：

```java
@InterceptorIgnore(tenant = "true")
public List<User> selectDeletedUsers() {
    return userMapper.selectList(new QueryWrapper<User>().eq("deleted", 1));
}
```

或者使用自定义 SQL：

```java
@Select("SELECT * FROM sys_user WHERE deleted = 1")
List<User> selectDeletedUsers();
```

### 6. 批量删除

```java
// 批量删除
userMapper.deleteBatchIds(Arrays.asList(1, 2, 3));
// 实际执行：UPDATE user SET deleted = 1 WHERE id IN (1, 2, 3) AND deleted = 0

// 批量查询
userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
// 实际执行：SELECT * FROM user WHERE id IN (1, 2, 3) AND deleted = 0
```

## 七、最佳实践

1. **字段命名规范**：建议使用 `deleted` 或 `is_deleted` 作为逻辑删除字段名
2. **默认值设置**：数据库中逻辑删除字段应设置默认值（通常为 0 或 'N'）
3. **索引优化**：为逻辑删除字段添加索引，提高查询性能
4. **全局配置**：使用全局配置减少重复代码
5. **强制查询**：需要查询已删除数据时，使用自定义 SQL 或 `@InterceptorIgnore`
6. **数据清理**：定期清理已删除的数据，避免数据膨胀
7. **备份策略**：逻辑删除前建议先备份数据

## 八、常见问题

### Q1: 如何查询已删除的数据？

使用自定义 SQL：
```java
@Select("SELECT * FROM sys_user WHERE deleted = 1")
List<User> selectDeletedUsers();
```

### Q2: 逻辑删除字段可以为空吗？

不建议为空，因为查询时会自动追加 WHERE 条件。如果字段为空，可能导致查询结果不正确。

### Q3: 如何禁用逻辑删除？

可以通过全局配置禁用：
```yaml
mybatis-plus:
  global-config:
    db-config:
      logic-delete-field: ""  # 禁用逻辑删除
```

### Q4: 逻辑删除会影响唯一约束吗？

不会，逻辑删除只是将数据标记为已删除，不会真正删除数据。如果需要唯一约束，需要额外处理。

## 九、参考链接

- [MyBatis-Plus 官方文档](https://baomidou.com/)
- [MyBatis-Plus 逻辑删除](https://baomidou.com/en/features/logic-delete/)
- [MyBatis-Plus GitHub](https://github.com/baomidou/mybatis-plus)
- [Maven Repository - mybatis-plus-annotation](https://mvnrepository.com/artifact/com.baomidou/mybatis-plus-annotation)
