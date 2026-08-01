# ORM 注解详解与依赖分析

> 涵盖 MyBatis-Plus 与 JPA（Jakarta Persistence）两大主流 ORM 框架的注解体系

---

## 目录

1. [MyBatis-Plus 常用注解](#一mybatis-plus-常用注解)
2. [JPA 常用注解](#二jpa-常用注解)
3. [Hibernate 扩展注解](#三hibernate-扩展注解)
4. [依赖链分析](#四依赖链分析)
5. [版本差异对比](#五版本差异对比)
6. [项目实际使用情况](#六项目实际使用情况)

---

## 一、MyBatis-Plus 常用注解

> 提供方：`com.baomidou:mybatis-plus-core`
> 包路径：`com.baomidou.mybatisplus.annotation.*`

### 1.1 类级别注解

| 注解 | 用途 | 核心属性 | 默认值 |
|------|------|----------|--------|
| `@TableName` | 指定实体类对应的数据库表名 | `value`: 表名<br>`schema`: 数据库schema<br>`autoResultMap`: 是否自动构建resultMap | `value` 默认为类名<br>`autoResultMap` 默认 `false` |
| `@TableId` | 标记主键字段 | `value`: 主键字段名<br>`type`: 主键类型枚举 | `value` 默认为字段名<br>`type` 默认 `IdType.NONE`（跟随全局配置） |
| `@TableLogic` | 标记逻辑删除字段 | `value`: 逻辑未删除值<br>`delval`: 逻辑删除值 | `value` 默认 `"0"`<br>`delval` 默认 `"1"` |
| `@Version` | 标记乐观锁字段 | 无 | 配合乐观锁插件使用 |
| `@KeySequence` | 序列主键策略（Oracle） | `value`: 序列名<br>`configClass`: 配置类 | 无默认值 |
| `@OrderBy` | 默认排序 | `value`: 排序SQL片段<br>`isDesc`: 是否降序<br>`sort`: 排序优先级 | `isDesc` 默认 `false`<br>`sort` 默认 `0` |
| `@InterceptorIgnore` | 插件过滤规则 | `tenant`: 租户插件<br>`pagination`: 分页插件<br>`blockAttack`: 防全表更新<br>`optimisticLocker`: 乐观锁 | 各属性默认 `""`（不过滤） |

### 1.2 字段级别注解

| 注解 | 用途 | 核心属性 | 默认值 |
|------|------|----------|--------|
| `@TableField` | 标记非主键字段 | `value`: 数据库字段名<br>`exist`: 是否为数据库字段<br>`fill`: 自动填充策略<br>`insertStrategy`/`updateStrategy`/`whereStrategy`: 字段策略<br>`select`: 是否查询该字段<br>`typeHandler`: 类型处理器 | `exist` 默认 `true`<br>`fill` 默认 `FieldFill.DEFAULT`<br>`select` 默认 `true`<br>`insertStrategy`/`updateStrategy`/`whereStrategy` 默认跟随全局配置 |
| `@EnumValue` | 标记枚举类中的值字段（通用枚举） | 无 | 标记在枚举类的某个字段上，MP 自动识别 |

### 1.3 IdType 枚举值说明

| 枚举值 | 说明 | 适用数据库 |
|--------|------|-----------|
| `AUTO` | 数据库自增主键 | MySQL/PostgreSQL |
| `ASSIGN_ID` | 雪花算法生成 Long/Integer ID | 全部 |
| `ASSIGN_UUID` | UUID 去横线后作为 String ID | 全部 |
| `INPUT` | 用户自行输入 | 全部 |
| `NONE` | 跟随全局配置 | 全部 |
| `ID_WORKER` | 雪花算法（已废弃，用 ASSIGN_ID） | 全部 |
| `ID_WORKER_STR` | 雪花算法字符串（已废弃，用 ASSIGN_UUID） | 全部 |
| `UUID` | UUID（已废弃，用 ASSIGN_UUID） | 全部 |

### 1.4 FieldFill 枚举值说明

| 枚举值 | 说明 |
|--------|------|
| `DEFAULT` | 不填充 |
| `INSERT` | 仅插入时填充 |
| `UPDATE` | 仅更新时填充 |
| `INSERT_UPDATE` | 插入和更新时都填充 |

### 1.5 FieldStrategy 枚举值说明（3.5.x 新增）

| 枚举值 | 说明 |
|--------|------|
| `DEFAULT` | 跟随全局配置 |
| `NOT_NULL` | 字段不为 null 时才参与 |
| `NOT_EMPTY` | 字段不为 null 且不为空串时参与 |
| `NEVER` | 永远不参与 SQL 生成 |
| `ALWAYS` | 总是参与 |

### 1.6 使用示例

```java
@Data
@TableName("sys_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    @TableField("real_name")
    private String realName;

    @TableField(exist = false)
    private String transientField;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;

    @Version
    private Integer version;
}
```

---

## 二、JPA 常用注解

> 提供方：`jakarta.persistence:jakarta.persistence-api`（新版）或 `javax.persistence:javax.persistence-api`（旧版）
> 包路径：`jakarta.persistence.*` 或 `javax.persistence.*`

### 2.1 类级别注解

| 注解 | 用途 | 核心属性 | 默认值 |
|------|------|----------|--------|
| `@Entity` | 标记类为持久化实体 | `name`: 实体名称 | `name` 默认为类名 |
| `@Table` | 指定映射的数据库表 | `name`: 表名<br>`schema`: 数据库schema<br>`catalog`: catalog<br>`uniqueConstraints`: 唯一约束 | `name` 默认为类名 |
| `@MappedSuperclass` | 标记父类为映射超类（自身不映射表） | 无 | 仅用于继承 |
| `@Embeddable` | 标记类可被嵌入到其他实体 | 无 | - |
| `@Inheritance` | 指定继承策略 | `strategy`: SINGLE_TABLE / TABLE_PER_CLASS / JOINED | 默认 `SINGLE_TABLE` |
| `@DiscriminatorColumn` | 指定鉴别器列 | `name`: 列名<br>`discriminatorType`: 类型<br>`length`: 长度 | 默认 `name="DTYPE"`, `length=31` |
| `@DiscriminatorValue` | 指定当前实体的鉴别器值 | `value`: 鉴别器值 | 默认为类名 |
| `@NamedEntityGraph` | 定义命名实体图 | `name`, `attributeNodes`, `subgraph` | - |

### 2.2 字段级别注解

| 注解 | 用途 | 核心属性 | 默认值 |
|------|------|----------|--------|
| `@Id` | 标记主键字段 | 无 | - |
| `@GeneratedValue` | 主键生成策略 | `strategy`: 生成策略<br>`generator`: 生成器名称 | 默认 `strategy=AUTO` |
| `@Column` | 映射到数据库列 | `name`: 列名<br>`nullable`: 是否可空<br>`unique`: 是否唯一<br>`length`: 长度<br>`precision`/`scale`: 精度<br>`columnDefinition`: DDL定义<br>`updatable`/`insertable`: 是否可更新/插入 | `nullable` 默认 `true`<br>`unique` 默认 `false`<br>`length` 默认 `255`<br>`updatable` 默认 `true`<br>`insertable` 默认 `true` |
| `@Enumerated` | 枚举类型映射 | `value`: STRING 或 ORDINAL | 默认 `EnumType.ORDINAL` |
| `@Temporal` | 时间类型精度 | `value`: DATE / TIME / TIMESTAMP | 用于 `java.util.Date` |
| `@Lob` | 大对象映射 | 无 | 自动识别 Blob/Clob |
| `@Transient` | 标记字段不持久化 | 无 | - |
| `@Version` | 乐观锁版本字段 | 无 | - |

### 2.3 关系映射注解

| 注解 | 用途 | 核心属性 |
|------|------|----------|
| `@OneToOne` | 一对一关联 | `cascade`, `fetch`, `mappedBy`, `optional` |
| `@OneToMany` | 一对多关联 | `cascade`, `fetch`, `mappedBy`, `orphanRemoval` |
| `@ManyToOne` | 多对一关联 | `cascade`, `fetch`, `optional` |
| `@ManyToMany` | 多对多关联 | `cascade`, `fetch`, `mappedBy` |
| `@JoinColumn` | 指定外键列 | `name`, `referencedColumnName`, `nullable`, `unique` |
| `@JoinTable` | 指定关联表（多对多） | `name`, `joinColumns`, `inverseJoinColumns` |

### 2.4 GenerationType 枚举值说明

| 枚举值 | 说明 | 适用数据库 |
|--------|------|-----------|
| `AUTO` | 由 JPA 实现自动选择策略 | 全部 |
| `IDENTITY` | 数据库自增 | MySQL/PostgreSQL |
| `SEQUENCE` | 使用数据库序列 | Oracle/PostgreSQL |
| `TABLE` | 使用辅助表生成 ID | 全部（性能差） |

### 2.5 生命周期回调注解

`@PrePersist`, `@PostPersist`, `@PreUpdate`, `@PostUpdate`, `@PreRemove`, `@PostRemove`, `@PostLoad`

### 2.6 使用示例

```java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private UserStatus status = UserStatus.ACTIVE;

    @Transient
    private String tempField;

    @Version
    private Integer version;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public enum UserStatus { ACTIVE, INACTIVE, DELETED }
}
```

---

## 三、Hibernate 扩展注解

> 提供方：`org.hibernate.orm:hibernate-core`（6.x）或 `org.hibernate:hibernate-core`（5.x）
> 包路径：`org.hibernate.annotations.*`
> 注意：属于实现层特有注解，不可移植到其他 JPA 实现

| 注解 | 用途 |
|------|------|
| `@DynamicInsert` | 动态生成 INSERT SQL，仅包含非 null 列 |
| `@DynamicUpdate` | 动态生成 UPDATE SQL，仅包含已修改的列 |
| `@Where` | 在实体/集合加载时添加自定义 SQL WHERE 条件 |
| `@Filter` | 运行时动态过滤（需配合 `@FilterDef`） |
| `@Formula` | 基于 SQL 表达式的计算列（只读） |
| `@BatchSize` | 批量加载延迟集合/代理的大小 |
| `@Cache` | 指定二级缓存策略 |
| `@NaturalId` | 标记自然键字段 |
| `@NotFound` | 关联找不到时的处理（IGNORE / EXCEPTION） |
| `@SQLInsert` / `@SQLUpdate` / `@SQLDelete` | 自定义 INSERT/UPDATE/DELETE SQL |
| `@Immutable` | 标记实体/集合为不可变 |
| `@OptimisticLocking` | 乐观锁策略配置 |
| `@Type` / `@JavaType` / `@JdbcType` | 自定义类型映射 |

---

## 四、依赖链分析

### 4.1 MyBatis-Plus 依赖传递

#### 方式一：Spring Boot 2.x（`mybatis-plus-boot-starter`）

```
mybatis-plus-boot-starter:3.4.1
├── mybatis-plus-core:3.4.1          ← 所有注解定义在这里
│   ├── mybatis:3.5.x               ← MyBatis 核心
│   └── mybatis-plus-annotation     ← 注解子模块（含 @TableName 等）
├── mybatis-spring-boot-starter      ← Spring Boot 集成
├── mybatis-plus-spring-boot-autoconfigure
├── spring-boot-autoconfigure
└── spring-boot-starter-jdbc
```

**最小依赖**（仅需注解定义，不含运行时）：
```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-annotation</artifactId>
    <version>3.4.1</version>
</dependency>
```

**工程中常用依赖**（包含全部功能）：
```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.4.1</version>
</dependency>
```

#### 方式二：Spring Boot 3.x（`mybatis-plus-spring-boot3-starter`）

```
mybatis-plus-spring-boot3-starter:3.5.16
├── mybatis-plus-core:3.5.16
│   └── mybatis-plus-annotation      ← 注解子模块
├── mybatis-spring                   ← Spring 6.x 集成
├── mybatis-plus-jsqlparser:3.5.16   ← SQL 解析（已拆分为独立模块）
│   └── jsqlparser:5.0+              ← JDK 11+ 版本
├── spring-boot-autoconfigure
└── spring-boot-starter-jdbc
```

**注意**：从 3.5.9 起，`jsqlparser` 被拆分为独立模块：
- JDK 8：`mybatis-plus-jsqlparser-4.9`（依赖 jsqlparser:4.9）
- JDK 11+：`mybatis-plus-jsqlparser`（依赖 jsqlparser:5.0+）

#### 方式三：Spring Boot 4.x（`mybatis-plus-spring-boot4-starter`）

从 3.5.13+ 开始支持 Spring Boot 4，artifact 为 `mybatis-plus-spring-boot4-starter`。

#### 版本管理推荐（3.5.9+）

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-bom</artifactId>
            <version>3.5.16</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 4.2 JPA 依赖传递

#### javax.persistence（旧版，Java EE 8 及之前）

```
javax.persistence-api:2.2
└── 无传递依赖（纯 API 包）
```

**最小依赖**：
```xml
<dependency>
    <groupId>javax.persistence</groupId>
    <artifactId>javax.persistence-api</artifactId>
    <version>2.2</version>
</dependency>
```

#### jakarta.persistence（新版，Jakarta EE 9+）

```
jakarta.persistence-api:3.1.0
└── 无传递依赖（纯 API 包）
```

**最小依赖**：
```xml
<dependency>
    <groupId>jakarta.persistence</groupId>
    <artifactId>jakarta.persistence-api</artifactId>
    <version>3.1.0</version>
</dependency>
```

#### Spring Boot 整合时的完整依赖链

```
spring-boot-starter-data-jpa
├── spring-data-jpa
│   └── jakarta.persistence-api      ← 注解定义
├── hibernate-core                    ← JPA 实现
│   └── jakarta.persistence-api
├── spring-data-commons
├── spring-boot-starter-aop
├── spring-boot-starter-jdbc
└── spring-boot-autoconfigure
```

**工程中常用依赖**：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

### 4.3 两套框架注解对比

| 功能 | MyBatis-Plus | JPA |
|------|-------------|-----|
| 表名映射 | `@TableName("table")` | `@Table(name = "table")` |
| 主键标记 | `@TableId` | `@Id` |
| 主键策略 | `@TableId(type = IdType.AUTO)` | `@GeneratedValue(strategy = GenerationType.IDENTITY)` |
| 字段映射 | `@TableField("column")` | `@Column(name = "column")` |
| 非持久化字段 | `@TableField(exist = false)` | `@Transient` |
| 逻辑删除 | `@TableLogic` | 无原生支持（需自定义查询） |
| 乐观锁 | `@Version` | `@Version` |
| 枚举映射 | `@EnumValue` + 全局配置 | `@Enumerated(EnumType.STRING)` |
| 自动填充 | `@TableField(fill = FieldFill.INSERT)` | `@PrePersist` 生命周期回调 |

---

## 五、版本差异对比

### 5.1 MyBatis-Plus 版本演进

| 版本 | 重要变化 | 包名 |
|------|---------|------|
| **3.0.x** | 基础版本，提供核心注解 | `com.baomidou.mybatisplus.annotation` |
| **3.1.2** | `@TableField` 新增 `insertStrategy`/`updateStrategy`/`whereStrategy` 替代旧的 `strategy` 属性 | 同上 |
| **3.3.0** | 新增 `IdType.ASSIGN_ID`（雪花算法）和 `IdType.ASSIGN_UUID` | 同上 |
| **3.4.1** | 稳定版，广泛使用（本项目 step1/step2 使用） | 同上 |
| **3.5.2** | 通用枚举功能无需手动配置 `typeHandlersPackage` 即可自动识别 | 同上 |
| **3.5.4** | 非 starter 方式需要自行引入 `mybatis-spring` | 同上 |
| **3.5.9+** | 新增 `mybatis-plus-bom` 统一版本管理；拆分 jsqlparser 模块 | 同上 |
| **3.5.13+** | 新增 `mybatis-plus-spring-boot4-starter` 支持 Spring Boot 4 | 同上 |
| **3.5.16** | 当前最新稳定版（本项目 maven-about 模块使用） | 同上 |

### 5.2 JPA 版本演进（javax → jakarta）

| 版本 | 包名 | 说明 |
|------|------|------|
| `javax.persistence-api:2.2` | `javax.persistence.*` | Java EE 8，2017 年最后发布 |
| `jakarta.persistence-api:2.2.x` | `javax.persistence.*` | 过渡版本，保持 javax 包名 |
| `jakarta.persistence-api:3.0+` | `jakarta.persistence.*` | Jakarta EE 9，纯重命名 |
| `jakarta.persistence-api:3.1+` | `jakarta.persistence.*` | Jakarta EE 10，新增功能 |
| `jakarta.persistence-api:3.2+` | `jakarta.persistence.*` | Jakarta EE 11，最新稳定版 |

### 5.3 迁移要点（javax → jakarta）

```diff
- import javax.persistence.*;
+ import jakarta.persistence.*;
```

- Spring Boot 2.x → 3.x 必须进行此迁移
- `javax.sql`, `javax.crypto`, `javax.net` 等 JDK 核心包**不需要**改

### 5.4 依赖选择速查

| 场景 | 推荐依赖 |
|------|---------|
| Spring Boot 2.x + MyBatis-Plus | `mybatis-plus-boot-starter` |
| Spring Boot 3.x + MyBatis-Plus | `mybatis-plus-spring-boot3-starter` + `mybatis-plus-jsqlparser` |
| Spring Boot 4.x + MyBatis-Plus | `mybatis-plus-spring-boot4-starter` |
| 仅需注解（model 模块，scope=provided） | `mybatis-plus-boot-starter`（scope=provided） |
| Spring Boot 2.x + JPA | `spring-boot-starter-data-jpa`（自动引入 `javax.persistence-api:2.2`） |
| Spring Boot 3.x + JPA | `spring-boot-starter-data-jpa`（自动引入 `jakarta.persistence-api:3.1`） |
| 仅需 JPA 注解定义 | `jakarta.persistence-api:3.1.0` 或 `javax.persistence-api:2.2` |

---

## 六、项目实际使用情况

### 6.1 项目依赖概况

| 模块 | ORM 框架 | 版本 | Spring Boot 版本 |
|------|---------|------|-----------------|
| `guigu-auth-parent-learn/step1` | MyBatis-Plus | 3.4.1 | 2.3.6.RELEASE |
| `guigu-auth-parent-learn/step2` | MyBatis-Plus | 3.4.1 | 2.3.6.RELEASE |
| `maven-about/baseon-com_h2database_h2` | MyBatis-Plus | 3.5.16 | 3.2.5 |
| `maven-about/spring-boot-starter-parent-3_5_16` | Spring Data JPA | - | 3.5.16 |

### 6.2 项目中已使用的 MyBatis-Plus 注解

| 注解 | 使用频率 | 示例文件 |
|------|---------|---------|
| `@TableName` | 高（所有实体类） | `SysUser.java`, `SysMenu.java`, `User.java` |
| `@TableId` | 高（所有实体类） | `BaseEntity.java`, `User.java` |
| `@TableField` | 极高（100+ 处） | `BaseEntity.java`, `SysUser.java` |
| `@TableLogic` | 中 | `BaseEntity.java`, `User.java` |
| `@Version` | 未使用 | - |
| `@EnumValue` | 未使用 | - |

### 6.3 项目中已使用的 JPA 注解

| 注解 | 使用频率 | 示例文件 |
|------|---------|---------|
| `@Entity` | 低（仅 1 个示例） | `maven-about/.../entity/User.java` |
| `@Table` | 低 | 同上 |
| `@Id` | 低 | 同上 |
| `@GeneratedValue` | 低 | 同上 |
| `@Column` | 低 | 同上 |
| `@Enumerated` | 低 | 同上 |

### 6.4 BaseEntity 基类设计模式

项目中的 `BaseEntity` 统一处理了公共字段：

```java
@Data
public class BaseEntity implements Serializable {
    @TableId(type = IdType.AUTO)
    private String id;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;

    @TableField(exist = false)
    private Map<String,Object> param = new HashMap<>();  // 非数据库字段，用于查询条件传递
}
```

---

## 附录：常见问题 FAQ

### Q1: 引入 `mybatis-plus-boot-starter` 后还需要引入 `mybatis-spring-boot-starter` 吗？

**不需要**。`mybatis-plus-boot-starter` 已经包含了 `mybatis-spring-boot-starter` 的全部功能，重复引入会导致配置冲突。

### Q2: model 模块只需要注解定义，如何最小化依赖？

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <scope>provided</scope>
</dependency>
```

`scope=provided` 表示仅编译期使用，不传递到运行时，避免 model 模块引入不必要的依赖。

### Q3: `@TableField(exist = false)` 和 `@Transient` 可以互换吗？

**不可以**。它们属于不同框架：
- `@TableField(exist = false)` → MyBatis-Plus
- `@Transient` → JPA

在 MyBatis-Plus 项目中只能用 `@TableField(exist = false)`，在 JPA 项目中只能用 `@Transient`。

### Q4: 项目从 Spring Boot 2.x 升级到 3.x，ORM 注解需要怎么改？

MyBatis-Plus 注解**不需要改**（包名不变），但 starter artifact 需要更换：
```diff
- mybatis-plus-boot-starter
+ mybatis-plus-spring-boot3-starter
+ mybatis-plus-jsqlparser  <!-- 新增 -->
```

JPA 注解**需要改**：
```diff
- import javax.persistence.*;
+ import jakarta.persistence.*;
```
