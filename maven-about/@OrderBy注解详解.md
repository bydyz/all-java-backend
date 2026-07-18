# @OrderBy 注解详解

## 一、@OrderBy 是谁提供的功能

`@OrderBy` 是 **MyBatis-Plus** 框架提供的注解，用于指定实体类的默认排序规则。

- **全限定类名**: `com.baomidou.mybatisplus.annotation.OrderBy`
- **所属项目**: [MyBatis-Plus](https://github.com/baomidou/mybatis-plus)
- **提供方**: 包头团队（baomidou）
- **引入版本**: MyBatis-Plus 3.0 起提供

## 二、最小依赖范围

### 1. 最小依赖（仅使用注解）

如果你只需要 `@OrderBy` 注解本身，**最小依赖**是：

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
│   └── mybatis-plus-annotation  ← @OrderBy 在这里
│   └── mybatis (3.5.x)
├── mybatis-plus-extension
└── mybatis-spring-boot-starter

mybatis-plus-spring-boot3-starter (Spring Boot 3.x)
├── mybatis-plus-core
│   └── mybatis-plus-annotation  ← @OrderBy 在这里
│   └── mybatis (3.5.x)
├── mybatis-plus-extension
└── mybatis-spring-boot-starter (3.0.x+)

mybatis-plus-spring-boot4-starter (Spring Boot 4.x)
├── mybatis-plus-core
│   └── mybatis-plus-annotation  ← @OrderBy 在这里
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

## 四、@OrderBy 的默认值

### 注解属性及默认值

| 属性 | 类型 | 是否必须 | 默认值 | 说明 |
|------|------|----------|--------|------|
| `value` | `String` | 否 | `""` (空字符串) | 排序 SQL 片段 |
| `isDesc` | `boolean` | 否 | `false` | 是否降序排序 |
| `sort` | `int` | 否 | `0` | 排序优先级（值越小优先级越高） |

### 默认值行为

1. **`value` 默认值**：当 `value` 为空字符串时，使用字段名作为排序字段
   - 例如：`@OrderBy` 标记在 `createTime` 字段上 → `ORDER BY create_time ASC`

2. **`isDesc` 默认值**：默认为 `false`（升序排序）
   - `false` → `ASC`（升序）
   - `true` → `DESC`（降序）

3. **`sort` 默认值**：默认为 `0`
   - 当多个字段都标记了 `@OrderBy` 时，按 `sort` 值从小到大排序
   - 值相同时，按字段声明顺序排序

### 排序工作原理

当实体类字段标记了 `@OrderBy` 后：

1. **查询操作**：自动追加 ORDER BY 子句
   - 例如：`SELECT * FROM user ORDER BY create_time DESC`

2. **多字段排序**：按 `sort` 值排序
   - 例如：`ORDER BY sort1 ASC, sort2 DESC`

3. **自定义排序**：可以使用 `value` 属性自定义排序 SQL
   - 例如：`@OrderBy(value = "LENGTH(name) ASC")`

## 五、不同版本使用方法

### 1. MyBatis-Plus 3.0.x ~ 3.3.x

```java
import com.baomidou.mybatisplus.annotation.OrderBy;

@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    
    @OrderBy(isDesc = true)
    private LocalDateTime createTime;
}
```

**此版本支持的属性**：
- `value` - 排序 SQL 片段
- `isDesc` - 是否降序
- `sort` - 排序优先级

### 2. MyBatis-Plus 3.4.x

```java
import com.baomidou.mybatisplus.annotation.OrderBy;

@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    
    @OrderBy(isDesc = true, sort = 1)
    private LocalDateTime createTime;
    
    @OrderBy(isDesc = false, sort = 2)
    private Integer id;
}
```

**此版本变化**：
- 支持多字段排序
- 支持自定义排序优先级

### 3. MyBatis-Plus 3.5.x（当前最新）

```java
import com.baomidou.mybatisplus.annotation.OrderBy;

@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    
    @OrderBy(value = "LENGTH(name) DESC")
    private String name;
    
    @OrderBy(isDesc = true)
    private LocalDateTime createTime;
}
```

**此版本变化**：
- 完全支持所有属性
- 支持自定义排序 SQL 片段

### 4. 版本对比表

| 特性 | 3.0.x ~ 3.3.x | 3.4.x | 3.5.x+ |
|------|----------------|--------|---------|
| `value` | ✅ | ✅ | ✅ |
| `isDesc` | ✅ | ✅ | ✅ |
| `sort` | ✅ | ✅ | ✅ |
| 多字段排序 | ✅ | ✅ | ✅ |
| 自定义 SQL | ✅ | ✅ | ✅ |

## 六、使用示例

### 1. 基础用法：单字段排序

最常见的用法，按创建时间降序排序：

```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    
    @OrderBy(isDesc = true)
    private LocalDateTime createTime;
}
```

查询时自动追加排序：
```java
List<User> users = userMapper.selectList(null);
// 实际执行：SELECT * FROM user ORDER BY create_time DESC
```

### 2. 多字段排序

按多个字段排序：

```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    
    @OrderBy(isDesc = true, sort = 1)
    private LocalDateTime createTime;
    
    @OrderBy(isDesc = false, sort = 2)
    private Integer id;
}
```

查询时自动追加排序：
```java
List<User> users = userMapper.selectList(null);
// 实际执行：SELECT * FROM user ORDER BY create_time DESC, id ASC
```

### 3. 自定义排序 SQL

使用自定义 SQL 片段排序：

```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    
    @OrderBy(value = "LENGTH(name) DESC")
    private String name;
}
```

查询时自动追加排序：
```java
List<User> users = userMapper.selectList(null);
// 实际执行：SELECT * FROM user ORDER BY LENGTH(name) DESC
```

### 4. 升序排序

默认升序排序：

```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    
    @OrderBy  // 默认升序
    private Integer sort;
}
```

查询时自动追加排序：
```java
List<User> users = userMapper.selectList(null);
// 实际执行：SELECT * FROM user ORDER BY sort ASC
```

### 5. 配合 QueryWrapper

可以覆盖默认排序：

```java
QueryWrapper<User> wrapper = new QueryWrapper<>();
wrapper.orderByDesc("create_time");

List<User> users = userMapper.selectList(wrapper);
// 实际执行：SELECT * FROM user ORDER BY create_time DESC
// 会覆盖实体类中的 @OrderBy 注解
```

### 6. 禁用默认排序

使用 `@InterceptorIgnore` 禁用默认排序：

```java
@InterceptorIgnore(tenant = "true")
public List<User> selectWithoutOrder() {
    return userMapper.selectList(null);
}
```

或者使用自定义 SQL：
```java
@Select("SELECT * FROM sys_user")
List<User> selectWithoutOrder();
```

## 七、最佳实践

1. **字段命名规范**：建议使用 `create_time`、`update_time` 等时间字段作为排序字段
2. **排序优先级**：使用 `sort` 属性控制多字段排序的优先级
3. **性能考虑**：为排序字段添加索引，提高查询性能
4. **默认排序**：通常使用 `create_time DESC` 作为默认排序
5. **覆盖排序**：使用 QueryWrapper 可以覆盖实体类中的默认排序
6. **禁用排序**：不需要排序时，使用自定义 SQL 或 `@InterceptorIgnore`

## 八、常见问题

### Q1: @OrderBy 注解不生效怎么办？

检查以下几点：
1. 字段类型是否正确
2. 是否配置了 MyBatis-Plus 插件
3. 是否使用了 QueryWrapper 覆盖排序

### Q2: 多字段排序顺序不对？

检查 `sort` 属性值，值越小优先级越高。

### Q3: 如何禁用默认排序？

使用自定义 SQL 或 `@InterceptorIgnore` 注解。

### Q4: @OrderBy 和 QueryWrapper 的关系？

QueryWrapper 的排序会覆盖实体类中的 `@OrderBy` 注解。

## 九、参考链接

- [MyBatis-Plus 官方文档](https://baomidou.com/)
- [MyBatis-Plus 注解配置](https://baomidou.com/en/reference/annotation/)
- [MyBatis-Plus GitHub](https://github.com/baomidou/mybatis-plus)
- [Maven Repository - mybatis-plus-annotation](https://mvnrepository.com/artifact/com.baomidou/mybatis-plus-annotation)
