# MyBatis-Plus QueryWrapper 完整详解

---

## 1. QueryWrapper 是谁提供的功能

`QueryWrapper` 由 **MyBatis-Plus** 框架提供，隶属于 **苞米豆（baomidou）** 团队开发。

**完整包路径：**
```
com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
```

**类继承关系：**
```
AbstractWrapper<T, String, QueryWrapper<T>>
    └── QueryWrapper<T> implements Query<QueryWrapper<T>, T, String>
```

- `AbstractWrapper`：所有 Wrapper 的抽象基类，提供 `eq`、`ne`、`like`、`between` 等条件构造方法
- `QueryWrapper`：在基类基础上增加 `select()` 方法（指定查询字段）和 `lambda()` 方法

**同级别的 Wrapper：**

| 类名 | 用途 |
|---|---|
| `QueryWrapper<T>` | 普通查询条件构造器 |
| `LambdaQueryWrapper<T>` | Lambda 版查询条件构造器（推荐） |
| `UpdateWrapper<T>` | 更新条件构造器 |
| `LambdaUpdateWrapper<T>` | Lambda 版更新条件构造器 |
| `Wrappers` | 静态工厂类，快速创建各种 Wrapper |

---

## 2. 依赖层级详解

MyBatis-Plus 采用模块化设计，依赖从最小到最大分为 **5 个层级**：

### 层级总览

```
层级5 (Starter)      mybatis-plus-boot-starter / spring-boot3-starter / spring-boot4-starter
    │
层级4 (聚合)         mybatis-plus (聚合模块，自动引入下层全部)
    │
层级3 (扩展)         mybatis-plus-extension + mybatis-plus-spring
    │
层级2 (核心)         mybatis-plus-core (QueryWrapper 在这里)
    │
层级1 (注解)         mybatis-plus-annotation
    │
外部依赖            mybatis / mybatis-spring
```

---

### 层级1：最小依赖（注解模块）

**只想要注解（`@TableName`、`@TableField` 等）时使用：**

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-annotation</artifactId>
    <version>3.5.16</version>
</dependency>
```

| 内容 | 说明 |
|---|---|
| 包含 | `@TableName`、`@TableId`、`@TableField`、`@TableLogic` 等注解 |
| 不包含 | Wrapper、SQL 构建器、Mapper 增强等 |

---

### 层级2：核心依赖（包含 QueryWrapper）

**只需要 QueryWrapper 等核心功能时使用：**

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-core</artifactId>
    <version>3.5.16</version>
</dependency>
```

| 内容 | 说明 |
|---|---|
| 包含 | `QueryWrapper`、`LambdaQueryWrapper`、`UpdateWrapper`、SQL 构建器、元数据解析 |
| 自动引入 | `mybatis-plus-annotation`（层级1） |
| 外部依赖 | `mybatis:3.5.19` |

> **这是使用 QueryWrapper 的最小依赖**

---

### 层级3：扩展依赖（Mapper 增强 + 分页）

**需要 `BaseMapper` 增强方法、`IService`、分页插件时使用：**

```xml
<!-- 核心 + 扩展 -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-core</artifactId>
    <version>3.5.16</version>
</dependency>
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-extension</artifactId>
    <version>3.5.16</version>
</dependency>

<!-- 如需 Spring 集成 -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-spring</artifactId>
    <version>3.5.16</version>
</dependency>
```

| 模块 | 包含内容 |
|---|---|
| `mybatis-plus-extension` | `IService`、`ServiceImpl`、`BaseMapper` 增强、分页插件、条件构造器工具 |
| `mybatis-plus-spring` | MyBatis 与 Spring 的桥接层 |

---

### 层级4：聚合依赖（一键引入全部）

**不想逐个引入，使用聚合模块一次性引入全部：**

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus</artifactId>
    <version>3.5.16</version>
</dependency>
```

| 内容 | 说明 |
|---|---|
| 自动引入 | `mybatis-plus-core` + `mybatis-plus-annotation` + `mybatis-plus-extension` + `mybatis-plus-spring` |
| 等价于 | 手动引入层级1~3的全部模块 |

---

### 层级5：Starter 依赖（Spring Boot 自动配置）

**Spring Boot 项目推荐使用，自动配置 + 全部功能：**

```xml
<!-- Spring Boot 2.x -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.3</version>
</dependency>

<!-- Spring Boot 3.x（Jakarta EE） -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
    <version>3.5.16</version>
</dependency>

<!-- Spring Boot 4.x -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-spring-boot4-starter</artifactId>
    <version>3.5.16</version>
</dependency>
```

| Starter | 适用场景 | 自动引入 |
|---|---|---|
| `mybatis-plus-boot-starter` | Spring Boot 2.x + JDK 8 | 层级4 聚合模块 + 自动配置 |
| `mybatis-plus-spring-boot3-starter` | Spring Boot 3.x + JDK 17 | 层级4 聚合模块 + Jakarta EE |
| `mybatis-plus-spring-boot4-starter` | Spring Boot 4.x + JDK 21 | 层级4 聚合模块 + 最新兼容 |

> **注意**：3.5.9+ 版本需额外引入 `mybatis-plus-jsqlparser`（JDK 11+）或 `mybatis-plus-jsqlparser-4.9`（JDK 8），否则分页插件无法使用

---

### 依赖层级对照表

| 层级 | artifactId | 包含 QueryWrapper | 包含 Mapper 增强 | 包含自动配置 |
|---|---|:---:|:---:|:---:|
| 1 | `mybatis-plus-annotation` | ✗ | ✗ | ✗ |
| 2 | `mybatis-plus-core` | ✓ | ✗ | ✗ |
| 3 | `mybatis-plus-extension` | ✓ | ✓ | ✗ |
| 4 | `mybatis-plus`（聚合） | ✓ | ✓ | ✗ |
| 5 | `mybatis-plus-boot-starter` | ✓ | ✓ | ✓ |

---

### 日常工程推荐选择

| 你的场景 | 推荐依赖 |
|---|---|
| 纯 Java 项目，只需要 QueryWrapper | `mybatis-plus-core`（层级2） |
| Spring Boot 2.x 项目 | `mybatis-plus-boot-starter`（层级5） |
| Spring Boot 3.x 项目 | `mybatis-plus-spring-boot3-starter`（层级5） |
| 想精细控制依赖范围 | `mybatis-plus-core` + `mybatis-plus-extension`（层级2+3） |

---

## 3. 有无默认值

**QueryWrapper 没有默认值**，每次使用必须手动创建实例：

```java
// 方式一：直接 new（最常用）
QueryWrapper<SysRole> wrapper = new QueryWrapper<>();

// 方式二：使用 Wrappers 工具类
QueryWrapper<User> wrapper = Wrappers.query();

// 方式三：链式创建
QueryWrapper<User> wrapper = new QueryWrapper<User>()
    .eq("name", "张三");

// 方式四：从 QueryWrapper 获取 LambdaQueryWrapper
LambdaQueryWrapper<User> lambdaWrapper = new QueryWrapper<User>().lambda();
```

---

## 4. 不同版本的使用方法差异

### 版本演进路线

| 大版本 | JDK 要求 | 关键变化 |
|---|---|---|
| 2.x | JDK 6+ | 初始版本，API 较为简陋 |
| 3.0.x | JDK 8+ | 引入 Lambda 表达式支持，引入 `Wrappers` 工具类 |
| 3.1.x ~ 3.3.x | JDK 8+ | 持续增强 Wrapper API，添加 `apply`、`exists`、`nested` 等 |
| 3.4.x | JDK 8+ | 稳定版本，广泛用于 Spring Boot 2.x 项目 |
| 3.5.x（含 3.5.16） | JDK 8+ / 11+ | 模块化重构（3.5.9 起拆分 jsqlparser），支持 Spring Boot 3/4 |

### 核心差异对比

**1) 依赖引入方式变化：**

- **3.4.x 及之前**：单一 Starter
  ```xml
  <artifactId>mybatis-plus-boot-starter</artifactId>
  <version>3.4.1</version>
  ```

- **3.5.9 起**：拆分为 Starter + jsqlparser，使用 BOM 管理
  ```xml
  <artifactId>mybatis-plus-bom</artifactId>
  <artifactId>mybatis-plus-jsqlparser</artifactId>      <!-- JDK 11+ -->
  <artifactId>mybatis-plus-jsqlparser-4.9</artifactId>  <!-- JDK 8 -->
  ```

**2) Spring Boot 3.x 必须使用 Jakarta EE 版本：**
- Spring Boot 3.x 使用 `jakarta.*` 包名（替代 `javax.*`）
- 必须使用 `mybatis-plus-spring-boot3-starter`
- 使用 `mybatis-plus-boot-starter` 会报 `ClassNotFoundException`

**3) Wrapper API 本身的稳定性：**
- QueryWrapper 的核心 API 在 3.0.7 之后基本保持不变
- 不同版本之间 API 兼容性很好，主要差异在于底层实现优化和新增辅助方法

---

## 5. QueryWrapper 核心 API 速查

### 比较操作

| 方法 | SQL 等价 | 示例 |
|---|---|---|
| `eq(column, val)` | `column = val` | `wrapper.eq("role_name", "管理员")` |
| `ne(column, val)` | `column <> val` | `wrapper.ne("role_code", "test")` |
| `gt(column, val)` | `column > val` | `wrapper.gt("id", 5)` |
| `ge(column, val)` | `column >= val` | `wrapper.ge("age", 18)` |
| `lt(column, val)` | `column < val` | `wrapper.lt("age", 30)` |
| `le(column, val)` | `column <= val` | `wrapper.le("age", 30)` |

**条件控制参数：** 每个方法都支持 `boolean condition` 参数，为 `false` 时不拼接该条件：
```java
String name = null;
wrapper.eq(name != null, "role_name", name); // name 为 null 时不拼接
```

### 模糊查询

| 方法 | SQL 等价 | 示例 |
|---|---|---|
| `like(column, val)` | `LIKE '%val%'` | `wrapper.like("name", "管理员")` |
| `notLike(column, val)` | `NOT LIKE '%val%'` | `wrapper.notLike("name", "测试")` |
| `likeLeft(column, val)` | `LIKE '%val'` | `wrapper.likeLeft("name", "管理员")` |
| `likeRight(column, val)` | `LIKE 'val%'` | `wrapper.likeRight("name", "系统")` |

### 范围查询

| 方法 | SQL 等价 | 示例 |
|---|---|---|
| `between(column, v1, v2)` | `BETWEEN v1 AND v2` | `wrapper.between("age", 18, 30)` |
| `in(column, values)` | `IN (v1, v2, ...)` | `wrapper.in("id", Arrays.asList(1,2,3))` |
| `inSql(column, sql)` | `IN (sql)` | `wrapper.inSql("id", "SELECT id FROM t WHERE id < 3")` |

### 空值判断

| 方法 | SQL 等价 |
|---|---|
| `isNull(column)` | `column IS NULL` |
| `isNotNull(column)` | `column IS NOT NULL` |

### 排序

| 方法 | SQL 等价 | 示例 |
|---|---|---|
| `orderByAsc(columns...)` | `ORDER BY col ASC` | `wrapper.orderByAsc("id")` |
| `orderByDesc(columns...)` | `ORDER BY col DESC` | `wrapper.orderByDesc("id")` |
| `orderBy(condition, isAsc, columns...)` | `ORDER BY col ASC/DESC` | `wrapper.orderBy(true, false, "id")` |

### 指定查询字段

```java
wrapper.select("id", "role_name", "role_code");
```

### 逻辑组合

```java
// OR 条件
wrapper.eq("role_code", "test").or().like("name", "管理员");

// 嵌套条件
wrapper.nested(w -> w.eq("role_code", "test1").or().eq("role_code", "test2"));
```

### 高级方法

| 方法 | 说明 | 示例 |
|---|---|---|
| `apply(sql, params)` | 拼接自定义 SQL（有参数化保护） | `wrapper.apply("date_format(create_time,'%Y-%m') = {0}", "2024-01")` |
| `last(sql)` | 在 SQL 末尾追加内容（有 SQL 注入风险） | `wrapper.last("LIMIT 3")` |
| `exists(sql)` | EXISTS 子查询 | `wrapper.exists("SELECT 1 FROM t WHERE id = 1")` |
| `groupBy(columns...)` | GROUP BY 分组 | `wrapper.groupBy("status")` |

### Lambda 方式（推荐生产使用）

```java
LambdaQueryWrapper<User> lambdaWrapper = new QueryWrapper<User>().lambda();
lambdaWrapper.eq(User::getUsername, "张三")
             .like(User::getName, "张")
             .orderByDesc(User::getId);
```

---

## 6. 版本兼容性速查表

| 你的情况 | 推荐版本 | Starter 依赖 |
|---|---|---|
| Spring Boot 2.3.x + JDK 8 | 3.4.1 | `mybatis-plus-boot-starter:3.4.1` |
| Spring Boot 2.7.x + JDK 8 | 3.5.3 | `mybatis-plus-boot-starter:3.5.3` |
| Spring Boot 3.x + JDK 17 | 3.5.16 | `mybatis-plus-spring-boot3-starter:3.5.16` |
| Spring Boot 4.x + JDK 21 | 3.5.16 | `mybatis-plus-spring-boot4-starter:3.5.16` |

---

## 7. 重要提醒

1. **引入 MyBatis-Plus 后不要再单独引入 MyBatis 和 MyBatis-Spring**，避免版本冲突
2. **3.5.9+ 版本**需要额外引入 `mybatis-plus-jsqlparser`（JDK 11+）或 `mybatis-plus-jsqlparser-4.9`（JDK 8），否则分页插件无法使用
3. **Spring Boot 3.x** 必须使用 `mybatis-plus-spring-boot3-starter`，不能用 `mybatis-plus-boot-starter`
4. **推荐使用 Lambda 方式**，避免字段名拼写错误，重构友好
