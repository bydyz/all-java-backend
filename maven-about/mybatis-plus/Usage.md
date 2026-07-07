# MyBatis-Plus 使用指南

## 1. MyBatis-Plus 简介

MyBatis-Plus 是 MyBatis 的增强工具，在 MyBatis 的基础上只做增强不做改变，为简化开发、提高效率而生。

### 1.1 主要特性

- **无侵入**：只做增强不做改变，引入它不会对现有工程产生影响
- **损耗小**：启动即会自动注入基本 CURD，性能基本无损耗
- **强大的 CRUD 操作**：内置通用 Mapper、通用 Service，少量配置即可实现单表大部分 CRUD 操作
- **条件构造器**：丰富的条件判断器，无需编写 XML 即可完成复杂查询
- **代码生成器**：快速生成 Mapper、Service、Controller 等代码
- **分页插件**：内置分页插件，支持多种数据库
- **逻辑删除**：支持逻辑删除
- **自动填充**：支持自动填充创建时间、更新时间等
- **多数据源**：支持多数据源配置

## 2. MyBatis-Plus 与 MyBatis 对比

| 特性 | MyBatis | MyBatis-Plus |
|------|---------|--------------|
| SQL 映射 | 需要编写 XML 或注解 | 通用 Mapper 自动生成 |
| 基础 CRUD | 需要手写 SQL | 内置通用 Service |
| 条件查询 | 需要编写动态 SQL | 条件构造器简化查询 |
| 分页 | 需要集成分页插件 | 内置分页插件 |
| 代码生成 | 需要额外工具 | 内置代码生成器 |
| 学习成本 | 较高 | 较低（在 MyBatis 基础上） |
| 灵活性 | 完全控制 SQL | 保留 MyBatis 灵活性 |

### 2.1 核心优势

1. **开发效率提升**：减少 80% 的 CRUD 代码量
2. **单表操作零 SQL**：通过内置方法实现单表增删改查
3. **条件构造器**：链式调用，代码更简洁
4. **自动填充**：自动维护创建时间、更新时间等字段

## 3. 常见用法与代码示例

### 3.1 实体类注解

```java
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("sys_user") // 指定表名
public class User {
    
    @TableId(type = IdType.AUTO) // 主键自增
    private Long id;
    
    private String username;
    
    private String password;
    
    private String email;
    
    @TableField(fill = FieldFill.INSERT) // 插入时自动填充
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新时自动填充
    private LocalDateTime updateTime;
    
    @TableLogic // 逻辑删除字段
    private Integer deleted;
}
```

### 3.2 Mapper 接口

```java
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 继承 BaseMapper 即可获得基本的 CRUD 方法
    // 无需编写 XML 文件
}
```

### 3.3 Service 接口与实现

```java
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

// Service 接口
public interface UserService extends IService<User> {
    // 自定义业务方法
}

// Service 实现
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    // 实现自定义业务方法
}
```

### 3.4 CRUD 操作示例

```java
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    // 插入操作
    public void insertUser() {
        User user = new User();
        user.setUsername("zhangsan");
        user.setPassword("123456");
        user.setEmail("zhangsan@example.com");
        
        // 方式一：使用 BaseMapper
        int rows = userMapper.insert(user);
        
        // 方式二：使用 IService（推荐）
        boolean success = this.save(user);
        
        // 批量插入
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        this.saveBatch(userList);
    }
    
    // 删除操作
    public void deleteUser() {
        // 方式一：根据 ID 删除
        userMapper.deleteById(1L);
        
        // 方式二：根据条件删除
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, "zhangsan");
        userMapper.delete(wrapper);
        
        // 方式三：使用 IService
        this.removeById(1L);
    }
    
    // 更新操作
    public void updateUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("lisi");
        
        // 方式一：根据 ID 更新
        userMapper.updateById(user);
        
        // 方式二：根据条件更新
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getUsername, "zhangsan")
                    .set(User::getUsername, "lisi");
        userMapper.update(null, updateWrapper);
        
        // 方式三：使用 IService
        this.updateById(user);
    }
    
    // 查询操作
    public void selectUser() {
        // 根据 ID 查询
        User user = userMapper.selectById(1L);
        
        // 查询所有
        List<User> userList = userMapper.selectList(null);
        
        // 条件查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, "zhangsan")
              .ge(User::getAge, 18)
              .orderByDesc(User::getCreateTime);
        
        List<User> result = userMapper.selectList(wrapper);
        
        // 查询单个
        User singleUser = userMapper.selectOne(wrapper);
        
        // 查询总数
        Long count = userMapper.selectCount(wrapper);
    }
}
```

### 3.5 条件构造器（QueryWrapper）

```java
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public void queryExamples() {
    // 基本条件查询
    LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
    
    // 等值查询
    wrapper.eq(User::getUsername, "zhangsan");
    
    // 不等值查询
    wrapper.ne(User::getStatus, 0);
    
    // 大于/小于查询
    wrapper.gt(User::getAge, 18);
    wrapper.lt(User::getAge, 30);
    
    // 大于等于/小于等于查询
    wrapper.ge(User::getAge, 18);
    wrapper.le(User::getAge, 30);
    
    // BETWEEN 查询
    wrapper.between(User::getAge, 18, 30);
    
    // LIKE 查询
    wrapper.like(User::getUsername, "zhang");
    wrapper.notLike(User::getUsername, "test");
    wrapper.likeLeft(User::getUsername, "san");  // %san
    wrapper.likeRight(User::getUsername, "zhang");  // zhang%
    
    // IN 查询
    wrapper.in(User::getId, Arrays.asList(1, 2, 3));
    
    // NOT IN 查询
    wrapper.notIn(User::getId, Arrays.asList(4, 5, 6));
    
    // IS NULL 查询
    wrapper.isNull(User::getEmail);
    
    // IS NOT NULL 查询
    wrapper.isNotNull(User::getEmail);
    
    // 排序
    wrapper.orderByAsc(User::getAge);
    wrapper.orderByDesc(User::getCreateTime);
    
    // 分组
    wrapper.groupBy(User::getStatus);
    
    // HAVING
    wrapper.having("count(*) > 1");
    
    // 查询字段指定
    wrapper.select(User::getId, User::getUsername, User::getEmail);
    
    // 使用 Wrapper 进行查询
    List<User> userList = userMapper.selectList(wrapper);
    
    // 分页查询
    Page<User> page = new Page<>(1, 10);  // 当前页，每页大小
    Page<User> result = userMapper.selectPage(page, wrapper);
    
    // 获取分页信息
    long total = result.getTotal();  // 总记录数
    List<User> records = result.getRecords();  // 数据列表
}
```

### 3.6 更新条件构造器（UpdateWrapper）

```java
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

public void updateExamples() {
    LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
    
    // 设置更新字段
    updateWrapper.set(User::getStatus, 1)
                .set(User::getUpdateTime, LocalDateTime.now());
    
    // 条件更新
    updateWrapper.eq(User::getUsername, "zhangsan")
                .set(User::getUsername, "lisi");
    
    // 使用 Wrapper 更新
    userMapper.update(null, updateWrapper);
    
    // 使用 IService 更新
    this.update(updateWrapper);
}
```

### 3.7 逻辑删除

```java
// 实体类中配置逻辑删除字段
@TableLogic
private Integer deleted;

// 使用时，删除操作会自动变为更新操作
// deleteById(1L) 会执行：UPDATE user SET deleted=1 WHERE id=1 AND deleted=0

// 查询时会自动过滤已删除记录
// selectList(null) 会执行：SELECT * FROM user WHERE deleted=0

// 如果需要查询已删除的记录
LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(User::getDeleted, 1);  // 注意：这样写不会生效，需要使用以下方式

// 方式一：使用 @TableField(exist = false) 和自定义 SQL
// 方式二：使用 Wrapper 的自定义条件
wrapper.apply("deleted = 1");  // 使用 apply 方法
```

### 3.8 自动填充

```java
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

@Component
public class AutoFillHandler implements MetaObjectHandler {
    
    @Override
    public void insertFill(MetaObject metaObject) {
        // 插入时自动填充
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
    
    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时自动填充
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
```

### 3.9 分页插件配置

```java
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {
    
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        
        // 添加分页插件
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        paginationInterceptor.setMaxLimit(500L);  // 设置最大单页限制数量
        interceptor.addInnerInterceptor(paginationInterceptor);
        
        return interceptor;
    }
}
```

### 3.10 代码生成器

```java
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;

public class CodeGenerator {
    
    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(System.getProperty("user.dir") + "/src/main/java");
        gc.setAuthor("admin");
        gc.setOpen(false);
        mpg.setGlobalConfig(gc);
        
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/mydb?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        mpg.setDataSource(dsc);
        
        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("user");
        pc.setParent("com.example");
        mpg.setPackageInfo(pc);
        
        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(com.baomidou.mybatisplus.generator.config.rules.NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(com.baomidou.mybatisplus.generator.config.rules.NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setInclude("sys_user");  // 需要生成的表名
        mpg.setStrategy(strategy);
        
        // 执行生成
        mpg.execute();
    }
}
```

### 3.11 多数据源配置

```java
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSValidator;
import com.baomidou.dynamic.datasource.processor.DsAnnotationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamicDataSourceConfig {
    
    @Bean
    public DsAnnotationInterceptor dsAnnotationInterceptor() {
        return new DsAnnotationInterceptor();
    }
}

// 使用示例
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    @DS("slave")  // 使用从数据源
    public List<User> selectFromSlave() {
        return this.list();
    }
    
    @DS("master")  // 使用主数据源
    public boolean saveToMaster(User user) {
        return this.save(user);
    }
}
```

## 4. 最佳实践

### 4.1 实体类设计

1. 使用 Lombok 简化代码
2. 使用 @TableName 指定表名
3. 使用 @TableId 指定主键策略
4. 使用 @TableField 指定字段映射和填充策略
5. 使用 @TableLogic 指定逻辑删除字段

### 4.2 Mapper 设计

1. 继承 BaseMapper 获取基本 CRUD 方法
2. 复杂查询使用 XML 或注解
3. 使用 @Mapper 注解

### 4.3 Service 设计

1. 继承 IService 获取通用业务方法
2. 继承 ServiceImpl 获取基本实现
3. 复杂业务逻辑在实现类中编写

### 4.4 查询优化

1. 使用 LambdaQueryWrapper 避免字段名硬编码
2. 使用 select 方法指定查询字段，避免 SELECT *
3. 合理使用分页，避免一次查询过多数据
4. 使用索引优化查询性能

## 5. 常见问题

### 5.1 分页不生效

确保已配置分页插件，并且查询时使用 Page 对象。

### 5.2 逻辑删除问题

- 逻辑删除字段默认值为 0（未删除），1（已删除）
- 查询时会自动过滤已删除记录
- 需要查询已删除记录时使用 apply 方法

### 5.3 自动填充不生效

确保已配置 MetaObjectHandler，并且实体类字段使用 @TableField(fill = FieldFill.INSERT) 等注解。

### 5.4 条件构造器使用注意

- 使用 LambdaQueryWrapper 避免字段名硬编码
- 链式调用时注意条件顺序
- 复杂条件使用 and/or 组合

## 6. 总结

MyBatis-Plus 是 MyBatis 的优秀增强工具，提供了丰富的功能和简洁的 API，能够显著提升开发效率。通过合理使用其特性，可以大大减少代码量，提高代码质量。