# MyBatis-Plus Service 与 Mapper 关系详解

## 核心问题

**能否只使用 Service，不使用 Mapper？**

**答案：不能。** Service 底层必须依赖 Mapper 才能操作数据库。

---

## 调用链路

```
Controller → Service → Mapper → 数据库
```

---

## 关键代码分析

### ServiceImpl 的泛型定义

```java
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> 
    implements IUserService {
    // UserMapper 是必须的，底层通过 baseMapper 执行SQL
}
```

`ServiceImpl<M extends BaseMapper<T>, T>` 泛型要求必须传入一个 Mapper。

### Controller 中的使用

```java
@RestController
public class UserController {
    @Autowired
    private UserService userService;  // 只注入 Service
    
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getById(id);  // 调用 Service 方法
    }
}
```

即使 Controller 里只看到 Service，但 `UserServiceImpl` 内部还是依赖 `UserMapper`。

---

## 层级依赖关系

| 层级 | 是否必须 | 说明 |
|------|----------|------|
| Controller | 可选 | 可以直接调用 Service |
| Service | 需要 Mapper | 底层依赖 Mapper 执行 SQL |
| Mapper | 必须存在 | 实际操作数据库的组件 |

---

## 变通方案

### 1. ActiveRecord 模式

实体类继承 `Model`，可以直接调用 CRUD 方法：

```java
@Data
@TableName("user")
public class User extends Model<User> {
    private Long id;
    private String name;
}

// 使用时直接操作实体
User user = new User();
user.setName("张三");
user.insert();  // 直接插入，无需手动调用 Mapper
user.selectById(1L);  // 直接查询
```

**注意**：底层仍需要 Mapper 存在，只是开发者不用手动创建/调用。

### 2. 第三方扩展（mybatis-plus-ext）

通过 `@AutoMapper` 注解自动生成 Mapper：

```java
@AutoMapper  // 自动生成 TestTableMapper
@Data
public class TestTable {
    private String id;
    private String name;
}
```

---

## 总结

- Mapper 是 MyBatis-Plus 操作数据库的**必要组件**
- Service 是对 Mapper 的封装，不能完全替代
- 即使 Controller 只注入 Service，Service 内部仍依赖 Mapper
- ActiveRecord 模式可以简化代码，但底层仍需 Mapper 存在
