# SysRoleService 分析文档

## 一、文件概览

| 文件 | 路径 | 类型 | 说明 |
|------|------|------|------|
| `SysRoleService.java` | `service/SysRoleService.java` | **接口** | 继承 MyBatis-Plus 的 `IService<SysRole>` |
| `SysRoleServiceImpl.java` | `service/impl/SysRoleServiceImpl.java` | **实现类** | 标注 `@Service`，继承 `ServiceImpl<SysRoleMapper, SysRole>` |
| `SysRoleServiceTest.java` | `test/.../SysRoleServiceTest.java` | **测试类** | `@SpringBootTest`，注入并使用 |

---

## 二、接口 SysRoleService.java

```java
package com.rc.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rc.model.system.SysRole;

public interface SysRoleService extends IService<SysRole> {

}
```

### 作用

- 继承 MyBatis-Plus 提供的 **`IService<SysRole>`** 通用 Service 接口
- 自动获得对 `sys_role` 表的标准 CRUD 方法：

| 方法分类 | 方法举例 |
|----------|----------|
| **增** | `save(SysRole)`, `saveBatch(Collection)` |
| **删** | `removeById(Serializable)`, `removeByIds(Collection)` |
| **改** | `updateById(SysRole)`, `update(Wrapper)` |
| **查** | `getById(Serializable)`, `list()`, `listByIds(Collection)` |
| **分页** | `page(Page<SysRole>)`, `page(Page, Wrapper)` |
| **计数** | `count()`, `count(Wrapper)` |

- 当前接口体为空，是**面向接口编程**的体现：对外只暴露接口类型，隐藏实现细节

---

## 三、实现类 SysRoleServiceImpl.java

```java
package com.rc.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rc.model.system.SysRole;
import com.rc.system.mapper.SysRoleMapper;
import com.rc.system.service.SysRoleService;
import org.springframework.stereotype.Service;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

}
```

### 关键点

| 要素 | 说明 |
|------|------|
| `@Service` | Spring 注解，将该类注册为一个 Bean，纳入 Spring 容器管理 |
| `extends ServiceImpl<SysRoleMapper, SysRole>` | 继承 MP 通用 Service 实现，自动获得所有 CRUD 方法的具体实现 |
| `implements SysRoleService` | 实现自定义接口，保证面向接口编程 |

### ServiceImpl 内部工作原理（源码简化）

```java
public class ServiceImpl<M extends BaseMapper<T>, T> implements IService<T> {
    @Autowired
    protected M baseMapper;  // 关键：泛型注入

    // 比如 list() 方法内部：
    public List<T> list() {
        return baseMapper.selectList(null);  // 委托给 Mapper
    }

    // save() 方法内部：
    public boolean save(T entity) {
        return baseMapper.insert(entity) > 0;  // 委托给 Mapper
    }
}
```

- `ServiceImpl<M, T>` 第二个泛型参数 `M` 用于确定 **Mapper 类型**
- Spring 会根据泛型信息自动注入对应的 `SysRoleMapper` Bean
- 所有 CRUD 方法最终委托给 `baseMapper`（即 `SysRoleMapper`）执行

### 完整调用链路

```
sysRoleService.list()
    → ServiceImpl.list()
        → baseMapper.selectList(null)       [baseMapper = SysRoleMapper]
            → SysRoleMapper.xml 中的 SQL     [MyBatis 映射]
                → JDBC 执行 SELECT * FROM sys_role
```

---

## 四、注入机制（为什么能被注入系统）

### 4.1 Spring 依赖注入（DI）

```mermaid
graph TD
    A[Spring 组件扫描] --> B[扫描到 @Service 标注的 SysRoleServiceImpl]
    B --> C[将其注册为 Bean, Bean 类型为 SysRoleService]
    C --> D[容器中存在 SysRoleService 类型的 Bean]
    D --> E[@Autowired 按类型查找并注入]
```

### 4.2 `@Autowired` 注入过程

```java
@Autowired
private SysRoleService sysRoleService;
```

1. Spring 容器检测到 `@Autowired` 注解
2. 按类型 `SysRoleService` 在容器中查找匹配的 Bean
3. 找到 `SysRoleServiceImpl` 实例（因为它实现了 `SysRoleService`）
4. 注入到目标字段中

> **关键**：声明类型是 `SysRoleService`（接口），实际注入的是 `SysRoleServiceImpl`（实现类），这是典型的**面向接口编程** + **依赖注入**。

### 4.3 项目中注入 SysRoleService 的位置

| 位置 | 文件 | 代码 |
|------|------|------|
| Controller | `SysRoleController.java` | `@Autowired private SysRoleService sysRoleService;` |
| Test | `SysRoleServiceTest.java` | `@Autowired private SysRoleService sysRoleService;` |

---

## 五、测试文件使用详解

```java
@SpringBootTest   // 启动完整的 Spring 容器（加载所有 Bean）
public class SysRoleServiceTest {

    @Autowired                      // 依赖注入
    private SysRoleService sysRoleService;

    // 1. 查询所有
    @Test
    public void findAll() {
        List<SysRole> list = sysRoleService.list();
        // 底层: baseMapper.selectList(null)
    }

    // 2. 添加
    @Test
    public void add() {
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("角色管理员atguigu");
        sysRole.setRoleCode("role");
        sysRole.setDescription("角色管理员");
        sysRoleService.save(sysRole);
        // 底层: baseMapper.insert(sysRole)
    }

    // 3. 修改（先查再改）
    @Test
    public void update() {
        SysRole sysRole = sysRoleService.getById(1);
        // 底层: baseMapper.selectById(1)
        sysRole.setDescription("test");
        sysRoleService.updateById(sysRole);
        // 底层: baseMapper.updateById(sysRole)
    }

    // 4. 删除
    @Test
    public void remove() {
        sysRoleService.removeById(8);
        // 底层: baseMapper.deleteById(8)
    }

    // 5. 条件查询（使用 QueryWrapper 封装条件）
    @Test
    public void select() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_code", "SYSTEM");
        List<SysRole> list = sysRoleService.list(wrapper);
        // 底层: baseMapper.selectList(wrapper)
        // 生成的 SQL: SELECT * FROM sys_role WHERE role_code = 'SYSTEM'
    }
}
```

### `@SpringBootTest` 的作用

- 加载完整的 Spring 应用程序上下文，**包括所有 Bean 的创建、注入**
- 不加 `@SpringBootTest` → `@Autowired` 字段为 `null` → 报 `NullPointerException`
- 加了 `@SpringBootTest` → Spring 扫描到 `SysRoleServiceImpl` 上的 `@Service` → 创建 Bean → 注入到 `sysRoleService` 字段

---

## 六、总结（一句话）

> **`SysRoleService` 是一个空接口，继承 MyBatis-Plus 的 `IService<SysRole>`；实现类 `SysRoleServiceImpl` 标注 `@Service` 注册为 Bean，通过 `@Autowired` 注入到 Controller/Test 中使用，所有 CRUD 操作最终委托给 `SysRoleMapper`（继承 `BaseMapper`）执行。**
