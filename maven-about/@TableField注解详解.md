# @TableField 注解详解

## 一、@TableField 是谁提供的功能

`@TableField` 是 **MyBatis-Plus** 框架提供的注解，用于标识实体类中的非主键字段，告诉 MyBatis-Plus 如何将实体类字段映射到数据库表列。

- **全限定类名**: `com.baomidou.mybatisplus.annotation.TableField`
- **所属项目**: [MyBatis-Plus](https://github.com/baomidou/mybatis-plus)
- **提供方**: 包头团队（baomidou）
- **引入版本**: MyBatis-Plus 3.0 起提供

## 二、最小依赖范围

### 1. 最小依赖（仅使用注解）

如果你只需要 `@TableField` 注解本身，**最小依赖**是：

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
│   └── mybatis-plus-annotation  ← @TableField 在这里
│   └── mybatis (3.5.x)
├── mybatis-plus-extension
└── mybatis-spring-boot-starter

mybatis-plus-spring-boot3-starter (Spring Boot 3.x)
├── mybatis-plus-core
│   └── mybatis-plus-annotation  ← @TableField 在这里
│   └── mybatis (3.5.x)
├── mybatis-plus-extension
└── mybatis-spring-boot-starter (3.0.x+)

mybatis-plus-spring-boot4-starter (Spring Boot 4.x)
├── mybatis-plus-core
│   └── mybatis-plus-annotation  ← @TableField 在这里
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

## 四、@TableField 的默认值

### 注解属性及默认值

| 属性 | 类型 | 是否必须 | 默认值 | 说明 |
|------|------|----------|--------|------|
| `value` | `String` | 否 | `""` (空字符串) | 数据库字段名，默认按字段名映射 |
| `exist` | `boolean` | 否 | `true` | 是否为数据库表字段，false 表示非表字段 |
| `condition` | `String` | 否 | `""` | 字段 WHERE 查询比较条件 |
| `update` | `String` | 否 | `""` | 字段 UPDATE SET 部分注入表达式 |
| `insertStrategy` | `FieldStrategy` | 否 | `FieldStrategy.DEFAULT` | INSERT 时字段验证策略 |
| `updateStrategy` | `FieldStrategy` | 否 | `FieldStrategy.DEFAULT` | UPDATE 时字段验证策略 |
| `whereStrategy` | `FieldStrategy` | 否 | `FieldStrategy.DEFAULT` | WHERE 条件中字段验证策略 |
| `fill` | `FieldFill` | 否 | `FieldFill.DEFAULT` | 字段自动填充策略 |
| `select` | `boolean` | 否 | `true` | 是否进行 SELECT 查询 |
| `keepGlobalFormat` | `boolean` | 否 | `false` | 是否保持使用全局 columnFormat |
| `property` | `String` | 否 | `""` | 属性名（@since 3.4.4） |
| `jdbcType` | `JdbcType` | 否 | `JdbcType.UNDEFINED` | JDBC 类型 |
| `typeHandler` | `Class<? extends TypeHandler>` | 否 | `UnknownTypeHandler.class` | 类型处理器 |
| `javaType` | `boolean` | 否 | `false` | 是否辅助追加 javaType |
| `numericScale` | `String` | 否 | `""` | 小数点后保留位数 |

### FieldStrategy 枚举值

| 值 | 说明 |
|----|------|
| `DEFAULT` | 默认策略，跟随全局配置 |
| `ALWAYS` | 总是拼接该字段（无论值是否为 null） |
| `NOT_NULL` | 字段值不为 null 时才拼接 |
| `NOT_EMPTY` | 字段值不为空时才拼接（字符串类型判空串，其他类型判 null） |
| `NEVER` | 从不拼接该字段 |

### FieldFill 枚举值

| 值 | 说明 |
|----|------|
| `DEFAULT` | 默认不填充，依赖数据库默认值或手动设置 |
| `INSERT` | INSERT 操作时自动填充 |
| `UPDATE` | UPDATE 操作时自动填充 |
| `INSERT_UPDATE` | INSERT 和 UPDATE 操作时都自动填充 |

## 五、不同版本使用方法

### 1. MyBatis-Plus 3.0.x ~ 3.3.x

```java
import com.baomidou.mybatisplus.annotation.TableField;

public class User {
    @TableField("user_name")
    private String name;

    @TableField(exist = false)
    private String transientField;
}
```

**此版本支持的属性**：
- `value` - 数据库字段名
- `exist` - 是否为数据库字段
- `condition` - 查询条件
- `update` - 更新表达式
- `select` - 是否查询
- `keepGlobalFormat` - 全局格式（@since 3.1.1）
- `jdbcType` / `typeHandler` / `numericScale`（@since 3.1.2）

**此版本不支持的属性**：
- `insertStrategy` / `updateStrategy` / `whereStrategy`（@since 3.1.2 才有）
- `property`（@since 3.4.4）

### 2. MyBatis-Plus 3.4.x

```java
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;

public class User {
    @TableField("user_name")
    private String name;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.ALWAYS)
    private Integer status;

    @TableField(exist = false)
    private String transientField;
}
```

**此版本变化**：
- 新增 `insertStrategy`、`updateStrategy`、`whereStrategy` 策略属性
- 更精细地控制 INSERT/UPDATE/WHERE 中的字段行为
- 策略属性优于 `el` 注解使用

### 3. MyBatis-Plus 3.5.x（当前最新）

```java
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import org.apache.ibatis.type.JdbcType;

public class User {
    @TableField("user_name")
    private String name;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(select = false)
    private String password;

    @TableField(condition = "%s > #{%s}")
    private Integer age;

    @TableField(update = "%s+1")
    private Integer version;

    @TableField(jdbcType = JdbcType.VARCHAR, typeHandler = CustomTypeHandler.class)
    private CustomType customField;

    @TableField(numericScale = "2")
    private BigDecimal price;

    @TableField(exist = false)
    private String transientField;
}
```

**此版本变化**：
- 新增 `property` 属性（@since 3.4.4）
- 完全支持所有策略属性
- 完善了 `typeHandler`、`jdbcType`、`numericScale` 的配合使用

### 4. 版本对比表

| 特性 | 3.0.x ~ 3.1.x | 3.1.2 ~ 3.3.x | 3.4.x | 3.5.x+ |
|------|----------------|----------------|--------|---------|
| `value` | ✅ | ✅ | ✅ | ✅ |
| `exist` | ✅ | ✅ | ✅ | ✅ |
| `condition` | ✅ | ✅ | ✅ | ✅ |
| `update` | ✅ | ✅ | ✅ | ✅ |
| `select` | ✅ | ✅ | ✅ | ✅ |
| `keepGlobalFormat` | ❌ | ✅ 新增 | ✅ | ✅ |
| `jdbcType` | ❌ | ✅ 新增 | ✅ | ✅ |
| `typeHandler` | ❌ | ✅ 新增 | ✅ | ✅ |
| `numericScale` | ❌ | ✅ 新增 | ✅ | ✅ |
| `insertStrategy` | ❌ | ✅ 新增 | ✅ | ✅ |
| `updateStrategy` | ❌ | ✅ 新增 | ✅ | ✅ |
| `whereStrategy` | ❌ | ✅ 新增 | ✅ | ✅ |
| `property` | ❌ | ❌ | ❌ | ✅ 新增 |
| `javaType` | ❌ | ❌ | ✅ 新增 | ✅ |

## 六、使用示例

### 1. 基础用法：映射数据库字段名

当实体类字段名与数据库列名不一致时，使用 `value` 指定列名：

```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_name")  // 数据库列名为 user_name
    private String username;

    @TableField("create_time")
    private LocalDateTime createTime;
}
```

### 2. 非数据库字段：exist = false

实体类中存在但数据库表中不存在的字段，使用 `exist = false`：

```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    @TableField(exist = false)  // 数据库中不存在此字段
    private String confirmPassword;

    @TableField(exist = false)
    private List<String> roles;
}
```

### 3. 字段自动填充：fill

配合 MetaObjectHandler 实现自动填充创建时间、更新时间等：

```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(fill = FieldFill.INSERT)           // 插入时填充
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)    // 插入和更新时都填充
    private LocalDateTime updateTime;
}
```

### 4. 不参与查询：select = false

敏感字段（如密码）不参与 SELECT 查询：

```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    @TableField(select = false)  // 查询时不返回此字段
    private String password;
}
```

### 5. 自定义查询条件：condition

自定义实体查询时的 WHERE 条件表达式：

```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    @TableField(condition = "%s > #{%s}")  // 查询时使用大于条件
    private Integer age;
}
```

### 6. 自定义更新表达式：update

更新时使用自定义表达式：

```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(update = "%s+1")  // 更新时自动 +1
    private Integer version;

    @TableField(update = "now()")  // 更新时使用数据库当前时间
    private LocalDateTime updateTime;
}
```

### 7. 精细控制插入/更新/更新策略

```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY)  // 插入时非空才插入
    private String nickname;

    @TableField(updateStrategy = FieldStrategy.ALWAYS)     // 更新时总是更新
    private String description;

    @TableField(whereStrategy = FieldStrategy.NOT_NULL)    // WHERE 条件中非空才拼接
    private Integer status;
}
```

### 8. 自定义类型处理器

```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "birth_date", typeHandler = CustomDateTypeHandler.class)
    private LocalDate birthDate;

    @TableField(value = "money", jdbcType = JdbcType.DECIMAL, numericScale = "2")
    private BigDecimal money;
}
```

## 七、最佳实践

1. **Spring Boot 3.x 项目**使用 `mybatis-plus-spring-boot3-starter` 依赖
2. **字段命名规范**：当实体类字段名遵循驼峰命名且与数据库列名匹配时，可省略 `@TableField` 注解
3. **敏感数据保护**：对密码等敏感字段使用 `select = false` 避免查询返回
4. **自动填充**：创建时间、更新时间等字段使用 `fill` 属性配合 MetaObjectHandler 实现自动填充
5. **非数据库字段**：实体类中用于临时计算的字段务必使用 `exist = false`，避免 SQL 报错
6. **策略选择**：
   - 插入时需要空值也入库 → `insertStrategy = FieldStrategy.ALWAYS`
   - 更新时需要空值也更新 → `updateStrategy = FieldStrategy.ALWAYS`
   - WHERE 条件需要严格判断 → `whereStrategy = FieldStrategy.NOT_EMPTY`
7. **避免使用已废弃的属性**：保持使用最新版本推荐的属性配置方式

## 八、参考链接

- [MyBatis-Plus 官方文档](https://baomidou.com/)
- [MyBatis-Plus 注解配置](https://baomidou.com/en/reference/annotation/)
- [MyBatis-Plus GitHub](https://github.com/baomidou/mybatis-plus)
- [Maven Repository - mybatis-plus-annotation](https://mvnrepository.com/artifact/com.baomidou/mybatis-plus-annotation)
- [TableField 源码](https://github.com/baomidou/mybatis-plus/blob/3.0/mybatis-plus-annotation/src/main/java/com/baomidou/mybatisplus/annotation/TableField.java)
