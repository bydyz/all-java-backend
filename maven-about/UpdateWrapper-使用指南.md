# MyBatis-Plus UpdateWrapper 使用指南

## 1. 功能来源

**UpdateWrapper** 是 **MyBatis-Plus** 框架提供的条件构造器，用于构建 `UPDATE` 语句的 `WHERE` 条件和 `SET` 子句。

- **包路径**: `com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper`
- **继承关系**: `UpdateWrapper` → `AbstractWrapper` → `AbstractLambdaWrapper`

---

## 2. 依赖分析

### 2.1 最小依赖

使用 `UpdateWrapper` 需要的最小依赖是：

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-core</artifactId>
    <version>3.5.16</version>
</dependency>
```

但实际项目中通常不直接使用 `mybatis-plus-core`，而是使用 Starter 依赖。

### 2.2 工程中常用的依赖

| Spring Boot 版本 | 推荐依赖 | 版本 |
|-----------------|---------|------|
| **Spring Boot 2.x** | `mybatis-plus-boot-starter` | 3.4.x / 3.5.x |
| **Spring Boot 3.x** | `mybatis-plus-spring-boot3-starter` | 3.5.x |

```xml
<!-- Spring Boot 2.x 项目 -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.3.2</version>
</dependency>

<!-- Spring Boot 3.x 项目 -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
    <version>3.5.16</version>
</dependency>
```

### 2.3 依赖传递关系 (以 3.5.16 为例)

```
mybatis-plus-spring-boot3-starter (3.5.16)
├── mybatis-plus (3.5.16)
│   ├── mybatis-plus-core (3.5.16)        ← UpdateWrapper 在此模块
│   ├── mybatis-plus-annotation (3.5.16)
│   ├── mybatis-plus-spring (3.5.16)
│   └── mybatis (3.5.19)
├── mybatis-spring (3.0.5)
├── mybatis-plus-spring-boot-autoconfigure (3.5.16)
├── spring-boot-autoconfigure (3.2.5)
└── spring-boot-starter-jdbc (3.2.5)
```

### 2.4 依赖层级总结

| 层级 | 依赖 | 说明 |
|-----|------|------|
| **第1层** (直接引入) | `mybatis-plus-spring-boot3-starter` | Spring Boot 3.x Starter |
| **第2层** | `mybatis-plus` | 核心聚合模块 |
| **第3层** | `mybatis-plus-core` | **UpdateWrapper 所在模块** |
| **第3层** | `mybatis-plus-annotation` | 注解定义模块 |
| **第4层** | `mybatis` | MyBatis 原生框架 |

---

## 3. UpdateWrapper 核心特性

### 3.1 与 QueryWrapper 的区别

| 特性 | QueryWrapper | UpdateWrapper |
|-----|-------------|---------------|
| 用途 | 构建 `SELECT` 查询条件 | 构建 `UPDATE` 更新条件 |
| 特有方法 | `select()` 指定查询字段 | `set()` 设置更新字段 |
| 泛型 | `QueryWrapper<T>` | `UpdateWrapper<T>` |

### 3.2 Lambda 版本

```java
// UpdateWrapper 的 Lambda 版本
LambdaUpdateWrapper<T> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();

// 或者通过 UpdateWrapper 获取
UpdateWrapper<T> wrapper = new UpdateWrapper<>();
LambdaUpdateWrapper<T> lambdaWrapper = wrapper.lambda();
```

---

## 4. 使用示例

### 4.1 基础用法 (字符串字段名)

```java
@Test
public void testUpdateByWrapper() {
    UpdateWrapper<SysRole> updateWrapper = new UpdateWrapper<>();
    updateWrapper.eq("role_code", "testManager")
                 .set("description", "通过条件修改");
    int rows = sysRoleMapper.update(null, updateWrapper);
    System.out.println("条件修改行数: " + rows);
}
```

### 4.2 Lambda 写法 (推荐)

```java
@Test
public void testLambdaUpdate() {
    LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
    wrapper.eq(User::getUsername, "admin")
           .set(User::getStatus, 1)
           .set(User::getUpdateTime, LocalDateTime.now());
    
    userMapper.update(null, wrapper);
}
```

### 4.3 通过 Service 使用

```java
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    public boolean updateStatusBatch(List<Long> ids, Integer status) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(User::getId, ids)
                     .set(User::getStatus, status);
        return this.update(updateWrapper);
    }
}
```

### 4.4 链式调用 (Service 层)

```java
// 3.5.x 版本支持的链式写法
userService.lambdaUpdate()
    .gt(User::getAge, 39)
    .likeRight(User::getName, "王")
    .set(User::getEmail, "w39@baomidou.com")
    .update();
```

---

## 5. UpdateWrapper 常用方法

### 5.1 条件方法 (继承自 AbstractWrapper)

| 方法 | 说明 | SQL 示例 |
|-----|------|---------|
| `eq` | 等于 | `WHERE field = value` |
| `ne` | 不等于 | `WHERE field != value` |
| `gt` | 大于 | `WHERE field > value` |
| `ge` | 大于等于 | `WHERE field >= value` |
| `lt` | 小于 | `WHERE field < value` |
| `le` | 小于等于 | `WHERE field <= value` |
| `between` | 范围 | `WHERE field BETWEEN v1 AND v2` |
| `like` | 模糊匹配 | `WHERE field LIKE '%value%'` |
| `likeRight` | 右模糊 | `WHERE field LIKE 'value%'` |
| `likeLeft` | 左模糊 | `WHERE field LIKE '%value'` |
| `in` | IN 查询 | `WHERE field IN (v1, v2)` |
| `isNull` | 为空 | `WHERE field IS NULL` |
| `isNotNull` | 不为空 | `WHERE field IS NOT NULL` |
| `or` | OR 连接 | `WHERE ... OR ...` |
| `and` | AND 连接 | `WHERE ... AND ...` |

### 5.2 UpdateWrapper 特有方法

| 方法 | 说明 | 示例 |
|-----|------|------|
| `set` | 设置更新字段 | `set("column", "value")` |
| `setSql` | 直接拼接 SQL | `setSql("column = column + 1")` |

---

## 6. 版本差异

### 6.1 3.4.x 版本

```java
// 基础用法
UpdateWrapper<User> wrapper = new UpdateWrapper<>();
wrapper.eq("name", "张三").set("age", 20);
userMapper.update(null, wrapper);

// Lambda
LambdaUpdateWrapper<User> lambdaWrapper = new LambdaUpdateWrapper<>();
lambdaWrapper.eq(User::getName, "张三").set(User::getAge, 20);
```

### 6.2 3.5.x 版本 (推荐)

```java
// 新增链式调用支持
userService.lambdaUpdate()
    .eq(User::getName, "张三")
    .set(User::getAge, 20)
    .update();

// Wrappers 工具类
LambdaUpdateWrapper<User> wrapper = Wrappers.<User>lambdaUpdate();
wrapper.eq(User::getName, "张三").set(User::getAge, 20);
```

### 6.3 版本兼容性

| MyBatis-Plus 版本 | Spring Boot 版本 | Java 版本 |
|------------------|-----------------|----------|
| 3.4.x | 2.x | 8+ |
| 3.5.x | 2.x / 3.x | 8+ / 17+ |

---

## 7. 默认值说明

### 7.1 UpdateWrapper 无默认值

`UpdateWrapper` 本身不包含默认值，所有字段值需要通过 `set()` 方法显式设置。

### 7.2 相关默认配置

```yaml
# application.yml 配置
mybatis-plus:
  global-config:
    db-config:
      # 主键策略默认值
      id-type: ASSIGN_ID
      # 逻辑删除默认值
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0
  configuration:
    # 驼峰命名映射默认开启
    map-underscore-to-camel-case: true
```

---

## 8. 最佳实践

### 8.1 推荐使用 Lambda 写法

```java
// ✅ 推荐 - 编译时检查字段名
LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
wrapper.eq(User::getName, "张三").set(User::getAge, 20);

// ❌ 不推荐 - 运行时才报错
UpdateWrapper<User> wrapper = new UpdateWrapper<>();
wrapper.eq("name", "张三").set("age", 20);
```

### 8.2 注意事项

1. **必须设置条件**: 使用 `UpdateWrapper` 时务必设置 `WHERE` 条件，否则可能更新全部数据
2. **entity 与 wrapper 的关系**: `entity` 中设置的字段会被作为 `SET` 子句，`wrapper` 中通过 `set()` 设置的字段也会被作为 `SET` 子句
3. **空值处理**: `entity` 为 `null` 时，只使用 `wrapper.set()` 设置的字段

---

## 9. 常见问题

### Q1: 为什么不推荐使用 `setSql()`?

`setSql()` 存在 SQL 注入风险，应优先使用 `set()` 方法。

### Q2: UpdateWrapper 和 LambdaUpdateWrapper 如何选择?

- **开发环境**: 推荐 `LambdaUpdateWrapper`，编译时检查字段名
- **动态字段名**: 需要动态拼接字段名时使用 `UpdateWrapper`

### Q3: 如何实现自增?

```java
// 方式1: 使用 setSql
wrapper.setSql("age = age + 1");

// 方式2: 使用 apply (推荐)
wrapper.apply("age = age + 1");
```

---

## 10. 参考资料

- [MyBatis-Plus 官方文档](https://baomidou.com/)
- [条件构造器详解](https://baomidou.com/guides/wrapper/)
- [UpdateWrapper API 文档](https://javadoc.io/doc/com.baomidou/mybatis-plus-core/latest/com/baomidou/mybatisplus/core/conditions/update/UpdateWrapper.html)

---

> **文档生成时间**: 2026-07-18  
> **基于项目版本**: mybatis-plus 3.5.16, Spring Boot 3.2.5
