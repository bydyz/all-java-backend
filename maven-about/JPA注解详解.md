# JPA 注解详解

## 一、JPA 注解是谁提供的功能

JPA（Java Persistence API）注解是 **Jakarta EE**（或 Java EE）规范提供的注解，用于实现对象关系映射（ORM）。

### 注解来源

| 注解类型 | 提供方 | 包路径 |
|----------|--------|--------|
| **Jakarta Persistence（新版）** | Jakarta EE 9+ | `jakarta.persistence.*` |
| **Java Persistence（旧版）** | Java EE 8 及之前 | `javax.persistence.*` |

### 主要注解列表

#### 类级别注解
- `@Entity` - 标记类为持久化实体
- `@Table` - 指定映射的数据库表
- `@MappedSuperclass` - 标记父类为映射超类
- `@Embeddable` - 标记类可被嵌入到其他实体
- `@Inheritance` - 指定继承策略
- `@DiscriminatorColumn` - 指定鉴别器列
- `@DiscriminatorValue` - 指定当前实体的鉴别器值
- `@NamedEntityGraph` - 定义命名实体图

#### 字段级别注解
- `@Id` - 标记主键字段
- `@GeneratedValue` - 主键生成策略
- `@Column` - 映射到数据库列
- `@Enumerated` - 枚举类型映射
- `@Temporal` - 时间类型精度
- `@Lob` - 大对象映射
- `@Transient` - 标记字段不持久化
- `@Version` - 乐观锁版本字段

#### 关系映射注解
- `@OneToOne` - 一对一关联
- `@OneToMany` - 一对多关联
- `@ManyToOne` - 多对一关联
- `@ManyToMany` - 多对多关联
- `@JoinColumn` - 指定外键列
- `@JoinTable` - 指定关联表

#### 生命周期注解
- `@PrePersist` - 持久化前回调
- `@PostPersist` - 持久化后回调
- `@PreUpdate` - 更新前回调
- `@PostUpdate` - 更新后回调
- `@PreRemove` - 删除前回调
- `@PostRemove` - 删除后回调
- `@PostLoad` - 加载后回调

## 二、最小依赖范围

### 1. 最小依赖（仅使用注解）

如果你只需要 JPA 注解本身，**最小依赖**是：

#### Jakarta Persistence（新版，推荐）

```xml
<dependency>
    <groupId>jakarta.persistence</groupId>
    <artifactId>jakarta.persistence-api</artifactId>
    <version>3.1.0</version>
</dependency>
```

#### Java Persistence（旧版）

```xml
<dependency>
    <groupId>javax.persistence</groupId>
    <artifactId>javax.persistence-api</artifactId>
    <version>2.2</version>
</dependency>
```

### 2. 最小依赖的依赖链

```
jakarta.persistence-api (3.1.0)
└── 无其他依赖

javax.persistence-api (2.2)
└── 无其他依赖
```

由于 JPA API 是一个纯接口包，因此：
- **最小依赖的依赖**：无
- **再上一级依赖**：无

## 三、实际工程中使用的依赖

在实际项目中，我们通常不会只引入 JPA API，而是根据 Spring Boot 版本选择对应的 Starter：

| Spring Boot 版本 | 推荐依赖 |
|------------------|----------|
| **Spring Boot 2.x** | `spring-boot-starter-data-jpa` |
| **Spring Boot 3.x** | `spring-boot-starter-data-jpa` |
| **Spring Boot 4.x** | `spring-boot-starter-data-jpa` |

### 依赖关系对比

#### Spring Boot 2.x

```
spring-boot-starter-data-jpa
├── spring-data-jpa
│   └── javax.persistence-api      ← JPA 注解在这里
├── hibernate-core                  ← JPA 实现
│   └── javax.persistence-api
├── spring-data-commons
├── spring-boot-starter-aop
├── spring-boot-starter-jdbc
└── spring-boot-autoconfigure
```

#### Spring Boot 3.x

```
spring-boot-starter-data-jpa
├── spring-data-jpa
│   └── jakarta.persistence-api    ← JPA 注解在这里
├── hibernate-core                  ← JPA 实现
│   └── jakarta.persistence-api
├── spring-data-commons
├── spring-boot-starter-aop
├── spring-boot-starter-jdbc
└── spring-boot-autoconfigure
```

### 本工程实际使用的依赖

本项目中使用 JPA 的模块均通过以下方式引入：

```xml
<!-- Spring Boot 2.x 项目 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Spring Boot 3.x 项目 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

## 四、JPA 注解的默认值

### 类级别注解默认值

| 注解 | 属性 | 默认值 | 说明 |
|------|------|--------|------|
| `@Entity` | `name` | 类名 | 实体名称 |
| `@Table` | `name` | 类名 | 表名 |
| `@Table` | `schema` | `""` | 数据库 schema |
| `@Table` | `catalog` | `""` | 数据库 catalog |
| `@Inheritance` | `strategy` | `SINGLE_TABLE` | 继承策略 |
| `@DiscriminatorColumn` | `name` | `"DTYPE"` | 鉴别器列名 |
| `@DiscriminatorColumn` | `length` | `31` | 鉴别器列长度 |
| `@DiscriminatorValue` | `value` | 类名 | 鉴别器值 |

### 字段级别注解默认值

| 注解 | 属性 | 默认值 | 说明 |
|------|------|--------|------|
| `@GeneratedValue` | `strategy` | `AUTO` | 主键生成策略 |
| `@Column` | `name` | 字段名 | 列名 |
| `@Column` | `nullable` | `true` | 是否可空 |
| `@Column` | `unique` | `false` | 是否唯一 |
| `@Column` | `length` | `255` | 字符串长度 |
| `@Column` | `updatable` | `true` | 是否可更新 |
| `@Column` | `insertable` | `true` | 是否可插入 |
| `@Enumerated` | `value` | `ORDINAL` | 枚举映射类型 |
| `@Temporal` | `value` | 无 | 时间精度（必须指定） |

### 关系映射注解默认值

| 注解 | 属性 | 默认值 | 说明 |
|------|------|--------|------|
| `@OneToOne` | `fetch` | `LAZY` | 获取策略 |
| `@OneToOne` | `optional` | `true` | 是否可选 |
| `@OneToMany` | `fetch` | `LAZY` | 获取策略 |
| `@OneToMany` | `orphanRemoval` | `false` | 是否删除孤儿 |
| `@ManyToOne` | `fetch` | `EAGER` | 获取策略 |
| `@ManyToOne` | `optional` | `true` | 是否可选 |
| `@ManyToMany` | `fetch` | `LAZY` | 获取策略 |
| `@JoinColumn` | `nullable` | `true` | 是否可空 |
| `@JoinColumn` | `unique` | `false` | 是否唯一 |

## 五、不同版本使用方法

### 1. javax.persistence（Java EE 8 及之前）

```java
import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private UserStatus status;
    
    @Version
    private Integer version;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();
    
    public enum UserStatus { ACTIVE, INACTIVE, DELETED }
}
```

**依赖**：
```xml
<dependency>
    <groupId>javax.persistence</groupId>
    <artifactId>javax.persistence-api</artifactId>
    <version>2.2</version>
</dependency>
```

### 2. jakarta.persistence（Jakarta EE 9+）

```java
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private UserStatus status;
    
    @Version
    private Integer version;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();
    
    public enum UserStatus { ACTIVE, INACTIVE, DELETED }
}
```

**依赖**：
```xml
<dependency>
    <groupId>jakarta.persistence</groupId>
    <artifactId>jakarta.persistence-api</artifactId>
    <version>3.1.0</version>
</dependency>
```

### 3. 版本对比表

| 特性 | javax.persistence 2.2 | jakarta.persistence 3.0+ |
|------|----------------------|--------------------------|
| 包名 | `javax.persistence.*` | `jakarta.persistence.*` |
| Java 版本 | Java 8+ | Java 11+ |
| Spring Boot 版本 | 2.x | 3.x+ |
| 功能 | 完整 | 完整 |
| 兼容性 | 向后兼容 | 向后兼容 |

## 六、使用示例

### 1. 基础用法：实体类映射

```java
@Entity
@Table(name = "sys_user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String username;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private UserStatus status = UserStatus.ACTIVE;
    
    @Version
    private Integer version;
    
    @Transient
    private String tempField;
    
    public enum UserStatus { ACTIVE, INACTIVE, DELETED }
}
```

### 2. 关系映射

```java
@Entity
@Table(name = "sys_user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private UserProfile profile;
    
    public void addOrder(Order order) {
        orders.add(order);
        order.setUser(this);
    }
    
    public void removeOrder(Order order) {
        orders.remove(order);
        order.setUser(null);
    }
}

@Entity
@Table(name = "sys_order")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String orderNo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
```

### 3. 继承映射

```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "payment_type")
public abstract class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private BigDecimal amount;
}

@Entity
@DiscriminatorValue("CREDIT_CARD")
public class CreditCardPayment extends Payment {
    
    private String cardNumber;
}

@Entity
@DiscriminatorValue("BANK_TRANSFER")
public class BankTransferPayment extends Payment {
    
    private String bankAccount;
}
```

### 4. 生命周期回调

```java
@Entity
@Table(name = "sys_user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
    
    @PostLoad
    protected void onLoad() {
        System.out.println("User loaded: " + username);
    }
}
```

### 5. 嵌入对象

```java
@Embeddable
public class Address {
    
    private String street;
    private String city;
    private String state;
    private String zipCode;
    
    // getter/setter 方法
}

@Entity
@Table(name = "sys_user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street", column = @Column(name = "home_street")),
        @AttributeOverride(name = "city", column = @Column(name = "home_city"))
    })
    private Address homeAddress;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street", column = @Column(name = "work_street")),
        @AttributeOverride(name = "city", column = @Column(name = "work_city"))
    })
    private Address workAddress;
}
```

### 6. 实体图

```java
@Entity
@NamedEntityGraph(
    name = "User.orders",
    attributeNodes = @NamedAttributeNode("orders")
)
@Table(name = "sys_user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();
}

// 使用实体图
@EntityGraph("User.orders")
@Query("SELECT u FROM User u WHERE u.username = :username")
User findByUsernameWithOrders(@Param("username") String username);
```

## 七、最佳实践

1. **包名选择**：
   - 新项目使用 `jakarta.persistence.*`
   - 旧项目保持 `javax.persistence.*`

2. **主键生成策略**：
   - MySQL/PostgreSQL：使用 `GenerationType.IDENTITY`
   - Oracle：使用 `GenerationType.SEQUENCE`
   - 跨数据库：使用 `GenerationType.AUTO`

3. **关系映射**：
   - 一对多/多对多：使用 `FetchType.LAZY`
   - 多对一/一对一：根据业务需求选择
   - 避免 N+1 查询问题

4. **枚举映射**：
   - 推荐使用 `EnumType.STRING`
   - 避免使用 `EnumType.ORDINAL`

5. **时间字段**：
   - 使用 `LocalDateTime` 替代 `Date`
   - 使用 `@PrePersist` 和 `@PreUpdate` 自动填充时间

6. **性能优化**：
   - 使用实体图解决 N+1 问题
   - 合理使用二级缓存
   - 避免过度抓取

## 八、常见问题

### Q1: javax 和 jakarta 如何选择？

- Spring Boot 2.x：使用 `javax.persistence`
- Spring Boot 3.x+：使用 `jakarta.persistence`

### Q2: 如何从 javax 迁移到 jakarta？

1. 修改依赖：`javax.persistence-api` → `jakarta.persistence-api`
2. 修改 import：`javax.persistence.*` → `jakarta.persistence.*`
3. 重新编译测试

### Q3: 关联关系如何选择 Fetch 策略？

- `LAZY`：延迟加载，需要时才加载关联数据
- `EAGER`：立即加载，查询时一起加载关联数据
- 推荐：一对多/多对多使用 `LAZY`，多对一/一对一根据需求选择

### Q4: 如何解决 N+1 查询问题？

1. 使用 `@EntityGraph`
2. 使用 `JOIN FETCH`
3. 使用 `@BatchSize`
4. 使用 Hibernate 二级缓存

## 九、参考链接

- [Jakarta Persistence API](https://jakarta.ee/specifications/persistence/3.1/)
- [Hibernate 官方文档](https://hibernate.org/orm/documentation/)
- [Spring Data JPA 官方文档](https://spring.io/projects/spring-data-jpa)
- [Maven Repository - jakarta.persistence-api](https://mvnrepository.com/artifact/jakarta.persistence/jakarta.persistence-api)
