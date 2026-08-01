# @InterceptorIgnore 注解详解

## 一、@InterceptorIgnore 是谁提供的功能

`@InterceptorIgnore` 是 **MyBatis-Plus** 框架提供的注解，用于指定某些方法或 SQL 忽略特定的插件拦截。

- **全限定类名**: `com.baomidou.mybatisplus.annotation.InterceptorIgnore`
- **所属项目**: [MyBatis-Plus](https://github.com/baomidou/mybatis-plus)
- **提供方**: 包头团队（baomidou）
- **引入版本**: MyBatis-Plus 3.0 起提供

## 二、最小依赖范围

### 1. 最小依赖（仅使用注解）

如果你只需要 `@InterceptorIgnore` 注解本身，**最小依赖**是：

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
│   └── mybatis-plus-annotation  ← @InterceptorIgnore 在这里
│   └── mybatis (3.5.x)
├── mybatis-plus-extension
└── mybatis-spring-boot-starter

mybatis-plus-spring-boot3-starter (Spring Boot 3.x)
├── mybatis-plus-core
│   └── mybatis-plus-annotation  ← @InterceptorIgnore 在这里
│   └── mybatis (3.5.x)
├── mybatis-plus-extension
└── mybatis-spring-boot-starter (3.0.x+)

mybatis-plus-spring-boot4-starter (Spring Boot 4.x)
├── mybatis-plus-core
│   └── mybatis-plus-annotation  ← @InterceptorIgnore 在这里
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

## 四、@InterceptorIgnore 的默认值

### 注解属性及默认值

| 属性 | 类型 | 是否必须 | 默认值 | 说明 |
|------|------|----------|--------|------|
| `tenant` | `String` | 否 | `""` (空字符串) | 忽略租户插件 |
| `pagination` | `String` | 否 | `""` (空字符串) | 忽略分页插件 |
| `blockAttack` | `String` | 否 | `""` (空字符串) | 忽略防全表更新插件 |
| `optimisticLocker` | `String` | 否 | `""` (空字符串) | 忽略乐观锁插件 |
| `sqlParser` | `String` | 否 | `""` (空字符串) | 忽略 SQL 解析器 |
| `multiTenancy` | `String` | 否 | `""` (空字符串) | 忽略多租户插件 |

### 默认值行为

1. **所有属性默认值**：当属性值为空字符串时，表示不忽略对应插件
2. **属性值**：可以设置为以下值：
   - `"true"` - 忽略插件
   - `"false"` - 不忽略插件
   - 其他值 - 视为 `"true"`

### 插件忽略工作原理

当方法或 SQL 标记了 `@InterceptorIgnore` 后：

1. **方法级别**：忽略该方法的所有 SQL 执行
2. **SQL 级别**：忽略特定 SQL 的插件拦截
3. **插件级别**：可以选择性忽略特定插件

## 五、不同版本使用方法

### 1. MyBatis-Plus 3.0.x ~ 3.3.x

```java
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;

@Mapper
public interface UserMapper {
    
    @InterceptorIgnore(tenant = "true")
    List<User> selectAllUsers();
    
    @InterceptorIgnore(pagination = "true")
    List<User> selectWithoutPagination();
}
```

**此版本支持的属性**：
- `tenant` - 忽略租户插件
- `pagination` - 忽略分页插件
- `blockAttack` - 忽略防全表更新插件
- `optimisticLocker` - 忽略乐观锁插件

**此版本不支持的属性**：
- `sqlParser`（@since 3.4.0）
- `multiTenancy`（@since 3.4.0）

### 2. MyBatis-Plus 3.4.x

```java
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;

@Mapper
public interface UserMapper {
    
    @InterceptorIgnore(tenant = "true", sqlParser = "true")
    List<User> selectAllUsers();
    
    @InterceptorIgnore(multiTenancy = "true")
    List<User> selectWithoutMultiTenancy();
}
```

**此版本变化**：
- 新增 `sqlParser` 属性
- 新增 `multiTenancy` 属性
- 支持更细粒度的插件忽略控制

### 3. MyBatis-Plus 3.5.x（当前最新）

```java
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;

@Mapper
public interface UserMapper {
    
    @InterceptorIgnore(tenant = "true", pagination = "true", optimisticLocker = "true")
    List<User> selectAllUsers();
    
    @InterceptorIgnore(blockAttack = "true")
    void deleteAllUsers();
}
```

**此版本变化**：
- 完全支持所有属性
- 推荐使用最新版本的属性配置

### 4. 版本对比表

| 特性 | 3.0.x ~ 3.3.x | 3.4.x | 3.5.x+ |
|------|----------------|--------|---------|
| `tenant` | ✅ | ✅ | ✅ |
| `pagination` | ✅ | ✅ | ✅ |
| `blockAttack` | ✅ | ✅ | ✅ |
| `optimisticLocker` | ✅ | ✅ | ✅ |
| `sqlParser` | ❌ | ✅ 新增 | ✅ |
| `multiTenancy` | ❌ | ✅ 新增 | ✅ |

## 六、使用示例

### 1. 基础用法：忽略租户插件

忽略租户插件，查询所有租户的数据：

```java
@Mapper
public interface UserMapper {
    
    @InterceptorIgnore(tenant = "true")
    List<User> selectAllUsers();
}
```

```java
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    
    public List<User> getAllUsers() {
        // 忽略租户插件，查询所有租户的用户
        return userMapper.selectAllUsers();
    }
}
```

### 2. 忽略分页插件

忽略分页插件，查询所有数据：

```java
@Mapper
public interface UserMapper {
    
    @InterceptorIgnore(pagination = "true")
    List<User> selectAllUsers();
}
```

### 3. 忽略防全表更新插件

忽略防全表更新插件，执行全表更新：

```java
@Mapper
public interface UserMapper {
    
    @InterceptorIgnore(blockAttack = "true")
    void updateAllUsers(@Param("status") Integer status);
}
```

```xml
<!-- UserMapper.xml -->
<update id="updateAllUsers">
    UPDATE sys_user SET status = #{status}
</update>
```

### 4. 忽略乐观锁插件

忽略乐观锁插件，强制更新：

```java
@Mapper
public interface UserMapper {
    
    @InterceptorIgnore(optimisticLocker = "true")
    void forceUpdateUser(@Param("id") Long id, @Param("username") String username);
}
```

```xml
<!-- UserMapper.xml -->
<update id="forceUpdateUser">
    UPDATE sys_user SET username = #{username} WHERE id = #{id}
</update>
```

### 5. 忽略 SQL 解析器

忽略 SQL 解析器，执行自定义 SQL：

```java
@Mapper
public interface UserMapper {
    
    @InterceptorIgnore(sqlParser = "true")
    @Select("SELECT * FROM sys_user WHERE id = #{id}")
    User selectUserById(@Param("id") Long id);
}
```

### 6. 忽略多租户插件

忽略多租户插件，查询所有租户的数据：

```java
@Mapper
public interface UserMapper {
    
    @InterceptorIgnore(multiTenancy = "true")
    List<User> selectAllUsers();
}
```

### 7. 多个插件同时忽略

同时忽略多个插件：

```java
@Mapper
public interface UserMapper {
    
    @InterceptorIgnore(tenant = "true", pagination = "true", optimisticLocker = "true")
    List<User> selectAllUsers();
}
```

### 8. 配合 Service 层使用

```java
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    
    @InterceptorIgnore(tenant = "true")
    public List<User> getAllUsers() {
        // 忽略租户插件，查询所有租户的用户
        return userMapper.selectList(null);
    }
    
    @InterceptorIgnore(pagination = "true")
    public List<User> getAllUsersWithoutPagination() {
        // 忽略分页插件，查询所有数据
        return userMapper.selectList(null);
    }
}
```

## 七、最佳实践

1. **明确需求**：在使用 `@InterceptorIgnore` 前，明确需要忽略哪个插件
2. **最小范围**：只忽略必要的插件，避免影响其他功能
3. **注释说明**：在方法上添加注释，说明为什么需要忽略插件
4. **测试验证**：使用后需要测试验证功能是否正常
5. **性能考虑**：忽略插件可能影响性能，需要权衡利弊
6. **权限控制**：某些操作需要特殊权限，确保安全

## 八、常见问题

### Q1: @InterceptorIgnore 注解不生效怎么办？

检查以下几点：
1. 注解是否正确使用
2. 方法是否被调用
3. 插件是否正确配置

### Q2: 如何在 XML 中使用 @InterceptorIgnore？

在 XML 中无法直接使用注解，可以使用自定义 SQL 或在接口方法上使用注解。

### Q3: 忽略插件会影响其他功能吗？

会的，忽略插件可能会导致某些功能失效。例如，忽略分页插件会导致查询不分页。

### Q4: 如何在全局范围内忽略插件？

可以通过配置全局忽略：
```yaml
mybatis-plus:
  global-config:
    sql-parser:
      ignore: true
```

## 九、参考链接

- [MyBatis-Plus 官方文档](https://baomidou.com/)
- [MyBatis-Plus 插件配置](https://baomidou.com/en/features/plugin/)
- [MyBatis-Plus GitHub](https://github.com/baomidou/mybatis-plus)
- [Maven Repository - mybatis-plus-annotation](https://mvnrepository.com/artifact/com.baomidou/mybatis-plus-annotation)
