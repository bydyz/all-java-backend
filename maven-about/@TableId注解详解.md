# @TableId 注解详解

## 一、@TableId 是谁提供的功能

`@TableId` 是 **MyBatis-Plus** 框架提供的注解，用于标识实体类中的主键字段。

- **全限定类名**: `com.baomidou.mybatisplus.annotation.TableId`
- **所属项目**: [MyBatis-Plus](https://github.com/baomidou/mybatis-plus)
- **提供方**: 包头团队（baomidou）

## 二、最小依赖范围

### 1. 最小依赖（仅使用注解）

如果你只需要 `@TableId` 注解本身，**最小依赖**是：

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
│   └── mybatis-plus-annotation  ← @TableId 在这里
│   └── mybatis (3.5.x)
├── mybatis-plus-extension
└── mybatis-spring-boot-starter

mybatis-plus-spring-boot3-starter (Spring Boot 3.x)
├── mybatis-plus-core
│   └── mybatis-plus-annotation  ← @TableId 在这里
│   └── mybatis (3.5.x)
├── mybatis-plus-extension
└── mybatis-spring-boot-starter (3.0.x+)

mybatis-plus-spring-boot4-starter (Spring Boot 4.x)
├── mybatis-plus-core
│   └── mybatis-plus-annotation  ← @TableId 在这里
│   └── mybatis (3.5.x)
├── mybatis-plus-extension
└── mybatis-spring-boot-starter (4.0.x+)
```

## 四、@TableId 的默认值

### 注解属性及默认值

| 属性 | 类型 | 是否必须 | 默认值 | 说明 |
|------|------|----------|--------|------|
| `value` | `String` | 否 | `""` (空字符串) | 主键字段名，默认按字段名映射 |
| `type` | `IdType` | 否 | `IdType.NONE` | 主键类型/生成策略 |

### IdType 枚举值

| 值 | 说明 |
|----|------|
| `AUTO` | 数据库 ID 自增（需数据库配合） |
| `NONE` | 未设置主键类型（默认值） |
| `INPUT` | 用户输入 ID（手动赋值） |
| `ASSIGN_ID` | 基于雪花算法生成全局唯一 ID（推荐） |
| `ASSIGN_UUID` | 生成 UUID 字符串 |
| `ID_WORKER` | 已废弃，请使用 `ASSIGN_ID` |
| `ID_WORKER_STR` | 已废弃，请使用 `ASSIGN_UUID` |
| `UUID` | 已废弃，请使用 `ASSIGN_UUID` |

## 五、不同版本使用方法

### 1. MyBatis-Plus 3.0.x ~ 3.3.x

```java
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private String name;
}
```

**此版本支持的 IdType**：
- `AUTO` - 数据库自增
- `NONE` - 不设置
- `INPUT` - 用户输入
- `ID_WORKER` - 雪花算法（Long 类型）
- `UUID` - UUID 字符串
- `ID_WORKER_STR` - 雪花算法字符串

### 2. MyBatis-Plus 3.4.x

```java
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private String name;
}
```

**此版本变化**：
- 新增 `ASSIGN_ID` 替代 `ID_WORKER`（语义更清晰）
- 新增 `ASSIGN_UUID` 替代 `ID_WORKER_STR`
- `ID_WORKER` 和 `ID_WORKER_STR` 标记为废弃但仍然可用

### 3. MyBatis-Plus 3.5.x（当前最新）

```java
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

public class User {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @TableId(value = "code", type = IdType.ASSIGN_UUID)
    private String code;
    
    private String name;
}
```

**此版本变化**：
- 完全移除废弃的 `ID_WORKER`、`ID_WORKER_STR`、`UUID` 枚举值
- 推荐使用 `ASSIGN_ID` 和 `ASSIGN_UUID`
- 新增 `ASSIGN_ID` 和 `ASSIGN_UUID` 作为标准命名

### 4. 版本对比表

| 特性 | 3.0.x ~ 3.3.x | 3.4.x | 3.5.x+ |
|------|----------------|--------|---------|
| `IdType.AUTO` | ✅ | ✅ | ✅ |
| `IdType.NONE` | ✅ | ✅ | ✅ |
| `IdType.INPUT` | ✅ | ✅ | ✅ |
| `IdType.ID_WORKER` | ✅ | ✅ (废弃) | ❌ 移除 |
| `IdType.ID_WORKER_STR` | ✅ | ✅ (废弃) | ❌ 移除 |
| `IdType.UUID` | ✅ | ✅ (废弃) | ❌ 移除 |
| `IdType.ASSIGN_ID` | ❌ | ✅ 新增 | ✅ 推荐 |
| `IdType.ASSIGN_UUID` | ❌ | ✅ 新增 | ✅ 推荐 |

## 六、使用示例

### 基础用法（数据库自增）

```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String username;
    private String password;
}
```

### 雪花算法生成 ID

```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    private String username;
}
```

### UUID 生成

```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;  // 注意：UUID 类型应为 String
    
    private String username;
}
```

### 指定数据库字段名

```java
@TableName("sys_user")
public class User {
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long id;
    
    private String username;
}
```

### 手动输入 ID

```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.INPUT)
    private Long id;  // 需要在插入前手动赋值
    
    private String username;
}
```

## 七、最佳实践

1. **Spring Boot 3.x 项目**使用 `mybatis-plus-spring-boot3-starter` 依赖
2. **主键策略选择**：
   - 数据库支持自增 → 使用 `IdType.AUTO`
   - 分布式环境 → 使用 `IdType.ASSIGN_ID`（雪花算法）
   - 需要字符串主键 → 使用 `IdType.ASSIGN_UUID`
3. **避免使用废弃的枚举值**：`ID_WORKER`、`ID_WORKER_STR`、`UUID` 在 3.5.x 版本已移除
4. **当字段名为 `id` 时**，可以省略 `@TableId` 注解，MyBatis-Plus 会自动识别

## 八、参考链接

- [MyBatis-Plus 官方文档](https://baomidou.com/)
- [MyBatis-Plus GitHub](https://github.com/baomidou/mybatis-plus)
- [Maven Repository - mybatis-plus-annotation](https://mvnrepository.com/artifact/com.baomidou/mybatis-plus-annotation)
