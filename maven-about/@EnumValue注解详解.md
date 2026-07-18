# @EnumValue 注解详解

## 一、@EnumValue 是谁提供的功能

`@EnumValue` 是 **MyBatis-Plus** 框架提供的注解，用于标记枚举类中的值字段，实现通用枚举功能。

- **全限定类名**: `com.baomidou.mybatisplus.annotation.EnumValue`
- **所属项目**: [MyBatis-Plus](https://github.com/baomidou/mybatis-plus)
- **提供方**: 包头团队（baomidou）
- **引入版本**: MyBatis-Plus 3.0 起提供

## 二、最小依赖范围

### 1. 最小依赖（仅使用注解）

如果你只需要 `@EnumValue` 注解本身，**最小依赖**是：

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
│   └── mybatis-plus-annotation  ← @EnumValue 在这里
│   └── mybatis (3.5.x)
├── mybatis-plus-extension
└── mybatis-spring-boot-starter

mybatis-plus-spring-boot3-starter (Spring Boot 3.x)
├── mybatis-plus-core
│   └── mybatis-plus-annotation  ← @EnumValue 在这里
│   └── mybatis (3.5.x)
├── mybatis-plus-extension
└── mybatis-spring-boot-starter (3.0.x+)

mybatis-plus-spring-boot4-starter (Spring Boot 4.x)
├── mybatis-plus-core
│   └── mybatis-plus-annotation  ← @EnumValue 在这里
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

## 四、@EnumValue 的默认值

### 注解属性

`@EnumValue` 注解没有任何属性，它只是一个标记注解。

### 使用要求

1. **枚举类**：需要标记在枚举类的某个字段上
2. **字段类型**：支持 `int`、`Integer`、`String`、`Long` 等类型
3. **唯一性**：标记的字段值需要唯一

### 通用枚举工作原理

当枚举类字段标记了 `@EnumValue` 后：

1. **插入操作**：自动将枚举值转换为标记字段的值
   - 例如：`UserStatus.ACTIVE` → `status = 1`

2. **查询操作**：自动将数据库值转换为枚举对象
   - 例如：`status = 1` → `UserStatus.ACTIVE`

3. **更新操作**：自动将枚举值转换为标记字段的值

## 五、不同版本使用方法

### 1. MyBatis-Plus 3.0.x ~ 3.3.x

```java
import com.baomidou.mybatisplus.annotation.EnumValue;

public enum UserStatus {
    ACTIVE(1, "活跃"),
    INACTIVE(2, "未激活"),
    DELETED(3, "已删除");
    
    @EnumValue
    private final int code;
    
    private final String desc;
    
    UserStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }
}
```

**配置枚举包扫描**：
```yaml
# application.yml
mybatis-plus:
  configuration:
    default-enum-type-handler: com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler
  type-enums-package: com.example.enums
```

**使用方式**：
```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    
    private UserStatus status;
}

// 插入数据
User user = new User();
user.setUsername("test");
user.setStatus(UserStatus.ACTIVE);
userMapper.insert(user);
// 实际插入：INSERT INTO user (username, status) VALUES ('test', 1)

// 查询数据
User user = userMapper.selectById(1);
// user.getStatus() == UserStatus.ACTIVE
```

### 2. MyBatis-Plus 3.4.x

```java
import com.baomidou.mybatisplus.annotation.EnumValue;

public enum UserStatus {
    ACTIVE(1, "活跃"),
    INACTIVE(2, "未激活"),
    DELETED(3, "已删除");
    
    @EnumValue
    private final int code;
    
    private final String desc;
    
    UserStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }
}
```

**此版本变化**：
- 支持自动配置枚举处理器
- 无需手动配置 `type-enums-package`

### 3. MyBatis-Plus 3.5.x（当前最新）

```java
import com.baomidou.mybatisplus.annotation.EnumValue;

public enum UserStatus {
    ACTIVE(1, "活跃"),
    INACTIVE(2, "未激活"),
    DELETED(3, "已删除");
    
    @EnumValue
    private final int code;
    
    private final String desc;
    
    UserStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }
}
```

**此版本变化**：
- 完全支持自动配置
- 推荐使用最新版本

### 4. 版本对比表

| 特性 | 3.0.x ~ 3.3.x | 3.4.x | 3.5.x+ |
|------|----------------|--------|---------|
| `@EnumValue` 标记 | ✅ | ✅ | ✅ |
| 自动配置枚举处理器 | ❌ | ✅ 新增 | ✅ |
| 手动配置 `type-enums-package` | ✅ | ✅ | ✅ |

## 六、使用示例

### 1. 基础用法：整数类型枚举

最常见的用法，使用整数类型枚举：

```java
public enum UserStatus {
    ACTIVE(1, "活跃"),
    INACTIVE(2, "未激活"),
    DELETED(3, "已删除");
    
    @EnumValue
    private final int code;
    
    private final String desc;
    
    UserStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }
}
```

数据库表结构：
```sql
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    status INT DEFAULT 1
);
```

### 2. 字符串类型枚举

使用字符串类型枚举：

```java
public enum UserStatus {
    ACTIVE("ACTIVE", "活跃"),
    INACTIVE("INACTIVE", "未激活"),
    DELETED("DELETED", "已删除");
    
    @EnumValue
    private final String code;
    
    private final String desc;
    
    UserStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }
}
```

数据库表结构：
```sql
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    status VARCHAR(20) DEFAULT 'ACTIVE'
);
```

### 3. 多个字段标记

枚举类中有多个字段，可以标记任意一个：

```java
public enum OrderStatus {
    PENDING(1, "待支付", "PENDING"),
    PAID(2, "已支付", "PAID"),
    SHIPPED(3, "已发货", "SHIPPED");
    
    @EnumValue
    private final int code;
    
    private final String desc;
    
    @EnumValue
    private final String codeStr;
    
    OrderStatus(int code, String desc, String codeStr) {
        this.code = code;
        this.desc = desc;
        this.codeStr = codeStr;
    }
    
    // getter 方法
}
```

### 4. 配合全局配置

```yaml
# application.yml
mybatis-plus:
  configuration:
    default-enum-type-handler: com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler
  type-enums-package: com.example.enums
```

### 5. 自定义枚举处理器

```java
public class CustomEnumTypeHandler extends BaseTypeHandler<Enum<?>> {
    private Class<Enum> type;
    
    public CustomEnumTypeHandler(Class<Enum> type) {
        this.type = type;
    }
    
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Enum parameter, JdbcType jdbcType) throws SQLException {
        // 自定义逻辑
    }
    
    @Override
    public Enum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // 自定义逻辑
    }
    
    @Override
    public Enum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        // 自定义逻辑
    }
    
    @Override
    public Enum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        // 自定义逻辑
    }
}
```

### 6. 枚举值为 null 的处理

```java
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    
    private UserStatus status;  // 可以为 null
}
```

当数据库值为 null 时，`status` 字段会设置为 null。

## 七、最佳实践

1. **枚举命名规范**：建议使用全大写加下划线的命名方式
2. **字段类型选择**：
   - 推荐使用 `int` 或 `Integer` 类型，性能好
   - 字符串类型适合需要可读性的场景
3. **唯一性保证**：确保 `@EnumValue` 标记的字段值唯一
4. **默认值设置**：数据库中枚举字段应设置默认值
5. **null 处理**：考虑枚举值为 null 的情况
6. **性能考虑**：整数类型比字符串类型性能好

## 八、常见问题

### Q1: 枚举值转换失败怎么办？

检查以下几点：
1. `@EnumValue` 标记的字段值是否唯一
2. 数据库中的值是否与枚举值匹配
3. 是否配置了枚举处理器

### Q2: 如何处理数据库中的 null 值？

当数据库值为 null 时，枚举字段会设置为 null。需要在代码中处理 null 情况。

### Q3: 如何在查询条件中使用枚举？

```java
QueryWrapper<User> wrapper = new QueryWrapper<>();
wrapper.eq("status", UserStatus.ACTIVE.getCode());
List<User> users = userMapper.selectList(wrapper);
```

### Q4: 枚举类可以继承其他类吗？

不建议，枚举类不能继承其他类（Java 限制）。

## 九、参考链接

- [MyBatis-Plus 官方文档](https://baomidou.com/)
- [MyBatis-Plus 通用枚举](https://baomidou.com/en/features/generic-enums/)
- [MyBatis-Plus GitHub](https://github.com/baomidou/mybatis-plus)
- [Maven Repository - mybatis-plus-annotation](https://mvnrepository.com/artifact/com.baomidou/mybatis-plus-annotation)
