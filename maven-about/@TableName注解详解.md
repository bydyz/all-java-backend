# @TableName 注解详解

## 一、@TableName 是谁提供的功能

`@TableName` 是 **MyBatis-Plus** 框架提供的注解，用于指定实体类对应的数据库表名。

- **全限定类名**: `com.baomidou.mybatisplus.annotation.TableName`
- **所属项目**: [MyBatis-Plus](https://github.com/baomidou/mybatis-plus)
- **提供方**: 包头团队（baomidou）
- **引入版本**: MyBatis-Plus 3.0 起提供

## 二、最小依赖范围

### 1. 最小依赖（仅使用注解）

如果你只需要 `@TableName` 注解本身，**最小依赖**是：

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
│   └── mybatis-plus-annotation  ← @TableName 在这里
│   └── mybatis (3.5.x)
├── mybatis-plus-extension
└── mybatis-spring-boot-starter

mybatis-plus-spring-boot3-starter (Spring Boot 3.x)
├── mybatis-plus-core
│   └── mybatis-plus-annotation  ← @TableName 在这里
│   └── mybatis (3.5.x)
├── mybatis-plus-extension
└── mybatis-spring-boot-starter (3.0.x+)

mybatis-plus-spring-boot4-starter (Spring Boot 4.x)
├── mybatis-plus-core
│   └── mybatis-plus-annotation  ← @TableName 在这里
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

## 四、@TableName 的默认值

### 注解属性及默认值

| 属性 | 类型 | 是否必须 | 默认值 | 说明 |
|------|------|----------|--------|------|
| `value` | `String` | 否 | `""` (空字符串) | 数据库表名，默认使用类名 |
| `schema` | `String` | 否 | `""` | 数据库 schema |
| `catalog` | `String` | 否 | `""` | 数据库 catalog |
| `resultMap` | `String` | 否 | `""` | 外部 resultMap 的 id |
| `autoResultMap` | `boolean` | 否 | `false` | 是否自动构建 resultMap |
| `keepGlobalPrefix` | `boolean` | 否 | `false` | 是否保持使用全局表前缀 |
| `excludeProperty` | `String[]` | 否 | `{}` | 需要排除的属性 |

### 默认值行为

1. **`value` 默认值**：当 `value` 为空字符串时，MyBatis-Plus 会使用类名作为表名
   - 例如：`User` 类 → 表名 `user`
   - 例如：`SysUser` 类 → 表名 `sys_user`

2. **`autoResultMap` 默认值**：默认为 `false`
   - 当设置为 `true` 时，MyBatis-Plus 会自动根据字段注解构建 resultMap
   - 当实体类中有 `typeHandler` 等复杂映射时，建议设置为 `true`

3. **`keepGlobalPrefix` 默认值**：默认为 `false`
   - 当设置为 `true` 时，会保留全局配置的表前缀

## 五、不同版本使用方法

### 1. MyBatis-Plus 3.0.x ~ 3.3.x

```java
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
}
```

**此版本支持的属性**：
- `value` - 表名
- `schema` - 数据库 schema
- `catalog` - 数据库 catalog
- `resultMap` - 外部 resultMap
- `excludeProperty` - 排除属性

**此版本不支持的属性**：
- `autoResultMap`（@since 3.1.1）
- `keepGlobalPrefix`（@since 3.1.1）

### 2. MyBatis-Plus 3.4.x

```java
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "sys_user", autoResultMap = true)
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> roles;
}
```

**此版本变化**：
- 新增 `autoResultMap` 属性
- 新增 `keepGlobalPrefix` 属性
- 当实体类中有复杂类型（如 JSON）时，建议设置 `autoResultMap = true`

### 3. MyBatis-Plus 3.5.x（当前最新）

```java
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "sys_user", autoResultMap = true, keepGlobalPrefix = true)
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> roles;
    
    @TableField(exist = false)
    private String transientField;
}
```

**此版本变化**：
- 完全支持所有属性
- 推荐使用 `autoResultMap = true` 配合复杂类型字段

### 4. 版本对比表

| 特性 | 3.0.x ~ 3.1.x | 3.1.1 ~ 3.3.x | 3.4.x | 3.5.x+ |
|------|----------------|----------------|--------|---------|
| `value` | ✅ | ✅ | ✅ | ✅ |
| `schema` | ✅ | ✅ | ✅ | ✅ |
| `catalog` | ✅ | ✅ | ✅ | ✅ |
| `resultMap` | ✅ | ✅ | ✅ | ✅ |
| `excludeProperty` | ✅ | ✅ | ✅ | ✅ |
| `autoResultMap` | ❌ | ✅ 新增 | ✅ | ✅ |
| `keepGlobalPrefix` | ❌ | ✅ 新增 | ✅ | ✅ |

## 六、使用示例

### 1. 基础用法：指定表名

最简单的用法，直接指定实体类对应的表名：

```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String email;
}
```

### 2. 使用 Schema

当需要指定数据库 schema 时：

```java
@TableName(schema = "mydb", value = "sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
}
```

### 3. 自动构建 ResultMap

当实体类中有复杂类型字段（如 JSON、List）时：

```java
@TableName(value = "sys_user", autoResultMap = true)
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> roles;
    
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> config;
}
```

### 4. 保留全局前缀

当全局配置了表前缀时，使用 `keepGlobalPrefix = true` 保留前缀：

```yaml
# application.yml
mybatis-plus:
  global-config:
    db-config:
      table-prefix: sys_
```

```java
@TableName(keepGlobalPrefix = true)
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
}
// 最终表名：sys_user
```

### 5. 排除属性

排除不需要映射的属性：

```java
@TableName(value = "sys_user", excludeProperty = {"password", "salt"})
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String salt;
}
```

### 6. 配合全局配置

```yaml
# application.yml
mybatis-plus:
  global-config:
    db-config:
      table-prefix: t_
      id-type: auto
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0
```

```java
@TableName("user")  // 最终表名：t_user
public class User {
    @TableId
    private Long id;
    private String username;
    private Integer deleted;
}
```

## 七、最佳实践

1. **表名命名规范**：建议使用小写加下划线的命名方式，与数据库表名保持一致
2. **使用 autoResultMap**：当实体类中有复杂类型字段（如 JSON、List、Map）时，务必设置 `autoResultMap = true`
3. **全局配置配合**：利用全局配置减少重复代码，如表前缀、逻辑删除字段等
4. **Schema 处理**：当数据库有多个 schema 时，建议使用 `schema` 属性明确指定
5. **排除属性**：对于不需要映射的敏感字段（如密码、盐值），使用 `excludeProperty` 排除
6. **保持简洁**：当类名与表名一致时，可省略 `@TableName` 注解

## 八、参考链接

- [MyBatis-Plus 官方文档](https://baomidou.com/)
- [MyBatis-Plus 注解配置](https://baomidou.com/en/reference/annotation/)
- [MyBatis-Plus GitHub](https://github.com/baomidou/mybatis-plus)
- [Maven Repository - mybatis-plus-annotation](https://mvnrepository.com/artifact/com.baomidou/mybatis-plus-annotation)
