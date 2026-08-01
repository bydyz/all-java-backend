# @KeySequence 注解详解

## 一、@KeySequence 是谁提供的功能

`@KeySequence` 是 **MyBatis-Plus** 框架提供的注解，用于指定序列主键策略（主要用于 Oracle 数据库）。

- **全限定类名**: `com.baomidou.mybatisplus.annotation.KeySequence`
- **所属项目**: [MyBatis-Plus](https://github.com/baomidou/mybatis-plus)
- **提供方**: 包头团队（baomidou）
- **引入版本**: MyBatis-Plus 3.0 起提供

## 二、最小依赖范围

### 1. 最小依赖（仅使用注解）

如果你只需要 `@KeySequence` 注解本身，**最小依赖**是：

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
│   └── mybatis-plus-annotation  ← @KeySequence 在这里
│   └── mybatis (3.5.x)
├── mybatis-plus-extension
└── mybatis-spring-boot-starter

mybatis-plus-spring-boot3-starter (Spring Boot 3.x)
├── mybatis-plus-core
│   └── mybatis-plus-annotation  ← @KeySequence 在这里
│   └── mybatis (3.5.x)
├── mybatis-plus-extension
└── mybatis-spring-boot-starter (3.0.x+)

mybatis-plus-spring-boot4-starter (Spring Boot 4.x)
├── mybatis-plus-core
│   └── mybatis-plus-annotation  ← @KeySequence 在这里
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

## 四、@KeySequence 的默认值

### 注解属性及默认值

| 属性 | 类型 | 是否必须 | 默认值 | 说明 |
|------|------|----------|--------|------|
| `value` | `String` | 是 | 无 | 序列名称 |
| `configClass` | `Class<?>` | 否 | `KeySequence.class` | 配置类 |

### 使用要求

1. **数据库支持**：需要数据库支持序列（如 Oracle、PostgreSQL）
2. **主键类型**：主键字段需要使用 `@TableId(type = IdType.INPUT)`
3. **序列存在**：数据库中需要先创建对应的序列

### 序列主键工作原理

当实体类使用 `@KeySequence` 后：

1. **插入操作**：MyBatis-Plus 会先从序列获取下一个值
   - 例如：`SELECT seq_user.NEXTVAL FROM DUAL`

2. **设置主键**：将获取的值设置到实体类的主键字段

3. **执行插入**：使用获取的主键值执行 INSERT 操作

## 五、不同版本使用方法

### 1. MyBatis-Plus 3.0.x ~ 3.3.x

```java
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;

@TableName("sys_user")
@KeySequence("seq_user")
public class User {
    @TableId(type = IdType.INPUT)
    private Long id;
    private String username;
}
```

**数据库序列创建**：
```sql
CREATE SEQUENCE seq_user
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;
```

**使用方式**：
```java
User user = new User();
user.setUsername("test");
userMapper.insert(user);
// 自动获取序列值设置到 id 字段
```

### 2. MyBatis-Plus 3.4.x

```java
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;

@TableName("sys_user")
@KeySequence(value = "seq_user", configClass = MyKeyGenerator.class)
public class User {
    @TableId(type = IdType.INPUT)
    private Long id;
    private String username;
}
```

**此版本变化**：
- 支持自定义 KeyGenerator 配置类
- 可以自定义序列值获取逻辑

### 3. MyBatis-Plus 3.5.x（当前最新）

```java
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;

@TableName("sys_user")
@KeySequence("seq_user")
public class User {
    @TableId(type = IdType.INPUT)
    private Long id;
    private String username;
}
```

**此版本变化**：
- 完全支持所有属性
- 推荐使用默认配置

### 4. 版本对比表

| 特性 | 3.0.x ~ 3.3.x | 3.4.x | 3.5.x+ |
|------|----------------|--------|---------|
| `value` | ✅ | ✅ | ✅ |
| `configClass` | ❌ | ✅ 新增 | ✅ |
| 自定义 KeyGenerator | ❌ | ✅ | ✅ |

## 六、使用示例

### 1. 基础用法：Oracle 序列

最常见的用法，使用 Oracle 序列：

```java
@TableName("sys_user")
@KeySequence("seq_user")
public class User {
    @TableId(type = IdType.INPUT)
    private Long id;
    private String username;
}
```

数据库序列创建：
```sql
CREATE SEQUENCE seq_user
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;
```

### 2. PostgreSQL 序列

使用 PostgreSQL 序列：

```java
@TableName("sys_user")
@KeySequence("seq_user")
public class User {
    @TableId(type = IdType.INPUT)
    private Long id;
    private String username;
}
```

数据库序列创建：
```sql
CREATE SEQUENCE seq_user
    START WITH 1
    INCREMENT BY 1;
```

### 3. 自定义 KeyGenerator

自定义序列值获取逻辑：

```java
import com.baomidou.mybatisplus.extension.plugins.key.KeyGenerator;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class MyKeyGenerator implements KeyGenerator {
    @Override
    public Object processKeyGenerator(SqlSessionFactory sqlSessionFactory) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.selectOne("SELECT seq_user.NEXTVAL FROM DUAL");
        }
    }
}
```

```java
@TableName("sys_user")
@KeySequence(value = "seq_user", configClass = MyKeyGenerator.class)
public class User {
    @TableId(type = IdType.INPUT)
    private Long id;
    private String username;
}
```

### 4. 批量插入

批量插入时，MyBatis-Plus 会为每条记录获取序列值：

```java
List<User> users = new ArrayList<>();
for (int i = 0; i < 10; i++) {
    User user = new User();
    user.setUsername("user" + i);
    users.add(user);
}

// 批量插入
userMapper.insertBatch(users);
// 每条记录都会获取序列值
```

### 5. 配合全局配置

```yaml
# application.yml
mybatis-plus:
  global-config:
    db-config:
      id-type: input
```

```java
@TableName("sys_user")
@KeySequence("seq_user")
public class User {
    @TableId
    private Long id;
    private String username;
}
```

### 6. 多表使用不同序列

```java
@TableName("sys_user")
@KeySequence("seq_user")
public class User {
    @TableId(type = IdType.INPUT)
    private Long id;
    private String username;
}

@TableName("sys_order")
@KeySequence("seq_order")
public class Order {
    @TableId(type = IdType.INPUT)
    private Long id;
    private String orderNo;
}
```

## 七、最佳实践

1. **序列命名规范**：建议使用 `seq_表名` 的命名方式
2. **序列初始值**：根据业务需求设置合适的初始值和步长
3. **主键类型**：推荐使用 `Long` 类型，避免使用 `Integer` 溢出
4. **批量插入**：批量插入时注意序列值的获取性能
5. **自定义 KeyGenerator**：特殊需求时可以自定义 KeyGenerator
6. **数据库兼容性**：注意不同数据库的序列语法差异

## 八、常见问题

### Q1: 为什么使用 `@KeySequence` 插入数据报错？

检查以下几点：
1. 数据库是否支持序列
2. 序列是否存在
3. 主键类型是否为 `IdType.INPUT`

### Q2: 批量插入时序列值重复？

MyBatis-Plus 会为每条记录获取序列值，不会重复。如果重复，检查序列步长设置。

### Q3: 如何在 MySQL 中使用序列？

MySQL 不支持序列，需要使用自增主键（`IdType.AUTO`）。

### Q4: 序列值如何预获取？

可以使用缓存序列值：
```sql
CREATE SEQUENCE seq_user
    START WITH 1
    INCREMENT BY 1
    CACHE 20;
```

## 九、参考链接

- [MyBatis-Plus 官方文档](https://baomidou.com/)
- [MyBatis-Plus 主键策略](https://baomidou.com/en/features/id-generator/)
- [MyBatis-Plus GitHub](https://github.com/baomidou/mybatis-plus)
- [Maven Repository - mybatis-plus-annotation](https://mvnrepository.com/artifact/com.baomidou/mybatis-plus-annotation)
