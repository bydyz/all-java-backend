# Hibernate 注解详解

## 一、Hibernate 注解是谁提供的功能

Hibernate 注解是 **Hibernate ORM** 框架提供的扩展注解，用于实现更高级的对象关系映射功能。

### 注解来源

| 注解类型 | 提供方 | 包路径 |
|----------|--------|--------|
| **Hibernate 6.x（新版）** | Hibernate ORM | `org.hibernate.annotations.*` |
| **Hibernate 5.x（旧版）** | Hibernate ORM | `org.hibernate.annotations.*` |

### 主要注解列表

#### 动态 SQL 注解
- `@DynamicInsert` - 动态生成 INSERT SQL，仅包含非 null 列
- `@DynamicUpdate` - 动态生成 UPDATE SQL，仅包含已修改的列
- `@SQLInsert` - 自定义 INSERT SQL
- `@SQLUpdate` - 自定义 UPDATE SQL
- `@SQLDelete` - 自定义 DELETE SQL

#### 过滤注解
- `@Where` - 在实体/集合加载时添加自定义 SQL WHERE 条件
- `@Filter` - 运行时动态过滤（需配合 `@FilterDef`）
- `@FilterDef` - 定义过滤器

#### 性能优化注解
- `@BatchSize` - 批量加载延迟集合/代理的大小
- `@Fetch` - 定义加载策略
- `@FetchProfile` - 定义抓取配置文件

#### 缓存注解
- `@Cache` - 指定二级缓存策略

#### 类型映射注解
- `@Type` - 自定义类型映射
- `@JavaType` - 指定 Java 类型
- `@JdbcType` - 指定 JDBC 类型
- `@JdbcTypeCode` - 指定 JDBC 类型代码

#### 其他注解
- `@NaturalId` - 标记自然键字段
- `@NotFound` - 关联找不到时的处理
- `@Immutable` - 标记实体/集合为不可变
- `@OptimisticLocking` - 乐观锁策略配置
- `@Formula` - 基于 SQL 表达式的计算列
- `@Generated` - 标记数据库生成的字段
- `@Parent` - 标记父对象引用
- `@LazyCollection` - 集合懒加载策略
- `@LazyGroup` - 懒加载组

## 二、最小依赖范围

### 1. 最小依赖（仅使用注解）

如果你只需要 Hibernate 注解本身，**最小依赖**是：

#### Hibernate 6.x（新版）

```xml
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>6.4.4.Final</version>
</dependency>
```

#### Hibernate 5.x（旧版）

```xml
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>5.6.15.Final</version>
</dependency>
```

### 2. 最小依赖的依赖链

#### Hibernate 6.x

```
hibernate-core (6.4.4.Final)
├── jakarta.persistence-api (3.1.0)
├── jakarta.transaction-api (2.0.1)
├── jboss-logging (3.5.3.Final)
├── hibernate-commons-annotations (6.0.6.Final)
├── jandex (3.1.2)
├── classmate (1.5.1)
├── jakarta.xml.bind-api (4.0.2)
└── byte-buddy (1.14.11)
```

#### Hibernate 5.x

```
hibernate-core (5.6.15.Final)
├── javax.persistence-api (2.2)
├── jboss-logging (3.4.3.Final)
├── hibernate-commons-annotations (5.1.2.Final)
├── jandex (2.4.2.Final)
├── classmate (1.5.1)
├── javax.activation-api (1.2.0)
└── byte-buddy (1.12.23)
```

### 3. 实际工程中使用的依赖

在实际项目中，我们通常不会直接引入 `hibernate-core`，而是通过 Spring Boot Starter 引入：

| Spring Boot 版本 | 推荐依赖 |
|------------------|----------|
| **Spring Boot 2.x** | `spring-boot-starter-data-jpa` |
| **Spring Boot 3.x** | `spring-boot-starter-data-jpa` |
| **Spring Boot 4.x** | `spring-boot-starter-data-jpa` |

#### 依赖关系

```
spring-boot-starter-data-jpa
├── spring-data-jpa
├── hibernate-core                    ← Hibernate 注解在这里
├── spring-data-commons
├── spring-boot-starter-aop
├── spring-boot-starter-jdbc
└── spring-boot-autoconfigure
```

## 三、Hibernate 注解的默认值

### 动态 SQL 注解

| 注解 | 属性 | 默认值 | 说明 |
|------|------|--------|------|
| `@DynamicInsert` | 无 | - | 标记注解，无属性 |
| `@DynamicUpdate` | 无 | - | 标记注解，无属性 |

### 过滤注解

| 注解 | 属性 | 默认值 | 说明 |
|------|------|--------|------|
| `@Filter` | `name` | 无 | 过滤器名称（必须指定） |
| `@Filter` | `condition` | `""` | 过滤条件 |
| `@Filter` | `enabled` | `true` | 是否启用 |
| `@FilterDef` | `name` | 无 | 过滤器名称（必须指定） |
| `@FilterDef` | `parameters` | `{}` | 参数定义 |

### 性能优化注解

| 注解 | 属性 | 默认值 | 说明 |
|------|------|--------|------|
| `@BatchSize` | `size` | 无 | 批量大小（必须指定） |
| `@BatchSize` | `fetch` | `FetchType.LAZY` | 获取策略 |
| `@Fetch` | `value` | `FetchMode.SELECT` | 加载策略 |
| `@Fetch` | `subgraph` | `""` | 子图名称 |

### 缓存注解

| 注解 | 属性 | 默认值 | 说明 |
|------|------|--------|------|
| `@Cache` | `usage` | 无 | 缓存并发策略（必须指定） |
| `@Cache` | `region` | `""` | 缓存区域名 |
| `@Cache` | `include` | `"all"` | 包含策略 |

### 类型映射注解

| 注解 | 属性 | 默认值 | 说明 |
|------|------|--------|------|
| `@Type` | `value` | 无 | 类型处理器类（必须指定） |
| `@Type` | `parameters` | `{}` | 类型参数 |
| `@JavaType` | `value` | 无 | Java 类型（必须指定） |
| `@JdbcType` | `value` | 无 | JDBC 类型（必须指定） |
| `@JdbcTypeCode` | `value` | 无 | JDBC 类型代码（必须指定） |

### 其他注解

| 注解 | 属性 | 默认值 | 说明 |
|------|------|--------|------|
| `@NaturalId` | `mutable` | `false` | 是否可变 |
| `@NotFound` | `action` | `EXCEPTION` | 找不到时的处理 |
| `@Immutable` | 无 | - | 标记注解，无属性 |
| `@OptimisticLocking` | `type` | `VersionType.NONE` | 乐观锁类型 |
| `@OptimisticLocking` | `dirtyCheck` | `false` | 是否脏检查 |
| `@OptimisticLocking` | `fieldChanged` | `""` | 字段变更回调 |
| `@Formula` | `value` | 无 | SQL 表达式（必须指定） |
| `@Generated` | `value` | 无 | 生成时机（必须指定） |
| `@Parent` | 无 | - | 标记注解，无属性 |
| `@LazyCollection` | `value` | `LazyCollectionOption.TRUE` | 懒加载策略 |
| `@LazyGroup` | `name` | 无 | 懒加载组名称 |

## 四、不同版本使用方法

### 1. Hibernate 5.x

```java
import org.hibernate.annotations.*;

@Entity
@Table(name = "sys_user")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String username;
    
    @NaturalId(mutable = false)
    @Column(nullable = false, unique = true)
    private String email;
    
    @Version
    private Integer version;
    
    @BatchSize(size = 20)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();
    
    @Formula("LENGTH(username)")
    private int usernameLength;
    
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private Map<String, Object> config;
    
    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
}
```

**依赖**：
```xml
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>5.6.15.Final</version>
</dependency>
```

### 2. Hibernate 6.x

```java
import org.hibernate.annotations.*;

@Entity
@Table(name = "sys_user")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String username;
    
    @NaturalId(mutable = false)
    @Column(nullable = false, unique = true)
    private String email;
    
    @Version
    private Integer version;
    
    @BatchSize(size = 20)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();
    
    @Formula("LENGTH(username)")
    private int usernameLength;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String, Object> config;
    
    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
}
```

**依赖**：
```xml
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>6.4.4.Final</version>
</dependency>
```

### 3. 版本对比表

| 特性 | Hibernate 5.x | Hibernate 6.x |
|------|---------------|---------------|
| 包名 | `org.hibernate.annotations` | `org.hibernate.annotations` |
| Java 版本 | Java 8+ | Java 11+ |
| Jakarta 支持 | 通过 `hibernate-core` | 原生支持 |
| JSON 类型 | `@Type(type = "json")` | `@JdbcTypeCode(SqlTypes.JSON)` |
| 性能优化 | 完整 | 增强 |
| 兼容性 | 向后兼容 | 向后兼容 |

## 五、使用示例

### 1. 动态 SQL

#### @DynamicInsert

```java
@Entity
@Table(name = "sys_user")
@DynamicInsert
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    
    private String email;
    
    private LocalDateTime createTime;
}
```

**效果**：
- 插入时只包含非 null 字段
- 例如：如果 `email` 为 null，则 SQL 为：
  ```sql
  INSERT INTO sys_user (username, create_time) VALUES (?, ?)
  ```
  而不是：
  ```sql
  INSERT INTO sys_user (username, email, create_time) VALUES (?, ?, ?)
  ```

#### @DynamicUpdate

```java
@Entity
@Table(name = "sys_user")
@DynamicUpdate
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    
    private String email;
    
    private LocalDateTime updateTime;
}
```

**效果**：
- 更新时只包含已修改的字段
- 例如：只修改 `username`，则 SQL 为：
  ```sql
  UPDATE sys_user SET username = ? WHERE id = ?
  ```
  而不是：
  ```sql
  UPDATE sys_user SET username = ?, email = ?, update_time = ? WHERE id = ?
  ```

### 2. 动态过滤

#### @Where

```java
@Entity
@Table(name = "sys_user")
@Where(clause = "deleted = 0")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    
    private Integer deleted;
}
```

**效果**：
- 查询时自动追加 WHERE 条件
- 例如：`SELECT * FROM sys_user WHERE deleted = 0 AND ...`

#### @Filter

```java
@Entity
@Table(name = "sys_user")
@FilterDef(name = "ageFilter", parameters = @ParamDef(name = "minAge", type = "integer"))
@Filter(name = "ageFilter", condition = "age >= :minAge")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    
    private Integer age;
}
```

**使用方式**：
```java
session.enableFilter("ageFilter").setParameter("minAge", 18);
List<User> users = session.createQuery("FROM User", User.class).getResultList();
// 实际执行：SELECT * FROM sys_user WHERE age >= 18
```

### 3. 性能优化

#### @BatchSize

```java
@Entity
@Table(name = "sys_user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    
    @BatchSize(size = 20)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();
}
```

**效果**：
- 批量加载关联集合
- 例如：查询 10 个用户，每个用户的订单会批量加载，而不是逐个加载
- 减少 N+1 查询问题

#### @Fetch

```java
@Entity
@Table(name = "sys_user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();
}
```

**FetchMode 类型**：
- `SELECT`：默认，逐个查询
- `JOIN`：使用 JOIN 查询
- `SUBSELECT`：使用子查询

### 4. 二级缓存

```java
@Entity
@Table(name = "sys_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "userCache")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();
}
```

**CacheConcurrencyStrategy 类型**：
- `READ_ONLY`：只读缓存
- `NONSTRICT_READ_WRITE`：非严格读写
- `READ_WRITE`：读写缓存
- `TRANSACTIONAL`：事务缓存

### 5. 自然键

```java
@Entity
@Table(name = "sys_user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    
    @NaturalId(mutable = false)
    @Column(nullable = false, unique = true)
    private String email;
}
```

**使用方式**：
```java
// 通过自然键查询
User user = session.byNaturalId(User.class)
    .using("email", "user@example.com")
    .load();
```

### 6. 类型映射

#### Hibernate 5.x

```java
@Entity
@Table(name = "sys_user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private Map<String, Object> config;
    
    @Type(type = "yes_no")
    @Column(length = 1)
    private Boolean active;
}
```

#### Hibernate 6.x

```java
@Entity
@Table(name = "sys_user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String, Object> config;
    
    @JavaType(BooleanType.class)
    @JdbcType(CharJdbcType.class)
    @Column(length = 1)
    private Boolean active;
}
```

### 7. 计算列

```java
@Entity
@Table(name = "sys_user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String firstName;
    
    private String lastName;
    
    @Formula("firstName || ' ' || lastName")
    private String fullName;
    
    @Formula("YEAR(CURRENT_DATE) - YEAR(birthDate)")
    private int age;
}
```

**注意**：
- `@Formula` 是只读的，不能用于更新
- SQL 表达式使用数据库语法

### 8. 不可变实体

```java
@Entity
@Table(name = "sys_audit_log")
@Immutable
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String action;
    
    private LocalDateTime createTime;
}
```

**效果**：
- 实体不可更新或删除
- 提高性能

## 六、最佳实践

1. **动态 SQL**：
   - 使用 `@DynamicInsert` 和 `@DynamicUpdate` 减少 SQL 长度
   - 适合字段较多的实体

2. **性能优化**：
   - 使用 `@BatchSize` 解决 N+1 问题
   - 使用 `@Fetch` 优化加载策略
   - 合理使用二级缓存

3. **类型映射**：
   - Hibernate 6.x 使用 `@JdbcTypeCode` 替代 `@Type`
   - 优先使用标准 JPA 注解

4. **不可变实体**：
   - 只读数据使用 `@Immutable`
   - 提高性能

5. **自然键**：
   - 使用 `@NaturalId` 标记业务键
   - 配合 `session.byNaturalId()` 查询

6. **过滤器**：
   - 使用 `@Filter` 实现动态过滤
   - 配合 `@FilterDef` 定义过滤器

## 七、常见问题

### Q1: @DynamicInsert 和 @DynamicUpdate 有什么区别？

- `@DynamicInsert`：插入时只包含非 null 字段
- `@DynamicUpdate`：更新时只包含已修改的字段

### Q2: 如何解决 N+1 查询问题？

1. 使用 `@BatchSize`
2. 使用 `@Fetch(FetchMode.SUBSELECT)`
3. 使用 `@EntityGraph`
4. 使用 `JOIN FETCH`

### Q3: @Formula 字段可以更新吗？

不可以，`@Formula` 是只读的，不能用于更新。

### Q4: Hibernate 5.x 和 6.x 有什么区别？

- 6.x 支持 Jakarta Persistence
- 6.x 使用 `@JdbcTypeCode` 替代 `@Type`
- 6.x 需要 Java 11+

## 八、参考链接

- [Hibernate 官方文档](https://hibernate.org/orm/documentation/)
- [Hibernate 注解文档](https://docs.jboss.org/hibernate/orm/6.4/userguide/html_single/Hibernate_User_Guide.html#annotations)
- [Hibernate GitHub](https://github.com/hibernate/hibernate-orm)
- [Maven Repository - hibernate-core](https://mvnrepository.com/artifact/org.hibernate.orm/hibernate-core)
