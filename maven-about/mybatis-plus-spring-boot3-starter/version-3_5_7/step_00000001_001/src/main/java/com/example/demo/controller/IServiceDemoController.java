package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.R;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "IService API 演示", description = "演示 MyBatis-Plus IService 接口的全部方法：save / saveOrUpdate / remove / update / get / list / count / exists / page")
@RestController
@RequestMapping("/demo/iservice")
@RequiredArgsConstructor
public class IServiceDemoController {

    private final UserService userService;

    // ==================== save 系列 API ====================

    @Operation(summary = "保存单条记录", description = "方法签名: boolean save(T entity); 入参: User 实体{id(为 null 时 INSERT),name,age,email}; 返回值: boolean true 表示插入成功；副作用：自增主键回填到 entity.id")
    @PostMapping("/save")
    public R<Map<String, Object>> save(@RequestBody User request) {
        User entity = new User();
        entity.setName(request.getName() != null ? request.getName() : "iservice-save-demo");
        entity.setAge(request.getAge() != null ? request.getAge() : 20);
        entity.setEmail(request.getEmail() != null ? request.getEmail() : "is-save@test.com");

        // ===== 入参打印 =====
        System.out.println("===== IService.save(T entity) 入参 =====");
        System.out.println("  entity = " + entity);

        // ===== API 调用 =====
        // 方法签名:    boolean save(T entity)
        // 入参数据结构: entity → User { id:Long(为 null 时 INSERT), name:String, age:Integer, email:String }
        // 返回值结构:   boolean → true 表示插入成功；副作用：自增主键回填到 entity.id
        boolean ok = userService.save(entity);

        // ===== 出参打印 =====
        System.out.println("===== IService.save(T entity) 出参 =====");
        System.out.println("  返回值 boolean ok = " + ok + " (是否保存成功)");
        System.out.println("  主键回填 entity.getId() = " + entity.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("ok", ok);
        result.put("savedId", entity.getId());
        return R.ok(result);
    }

    @Operation(summary = "批量保存记录", description = "方法签名: boolean saveBatch(Collection<T> entityList); 入参: User 集合(默认批次 1000 条); 返回值: boolean 全部插入成功返回 true")
    @PostMapping("/save-batch")
    public R<Map<String, Object>> saveBatch() {
        List<User> users = List.of(
                buildUser("is-batch-1", 21, "isB1@test.com"),
                buildUser("is-batch-2", 22, "isB2@test.com")
        );

        // ===== 入参打印 =====
        System.out.println("===== IService.saveBatch(Collection<T>) 入参 =====");
        System.out.println("  entityList.size() = " + users.size());
        System.out.println("  entityList = " + users);

        // ===== API 调用 =====
        // 方法签名:    boolean saveBatch(Collection<T> entityList)
        // 入参数据结构: entityList → List<User>，每个元素 User { id, name, age, email }；默认批次 1000 条
        // 返回值结构:   boolean → 全部插入成功返回 true
        boolean ok = userService.saveBatch(users);

        // ===== 出参打印 =====
        System.out.println("===== IService.saveBatch(Collection<T>) 出参 =====");
        System.out.println("  返回值 boolean ok = " + ok + " (批量插入是否全部成功)");

        Map<String, Object> result = new HashMap<>();
        result.put("ok", ok);
        result.put("size", users.size());
        return R.ok(result);
    }

    @Operation(summary = "指定批次大小批量保存", description = "方法签名: boolean saveBatch(Collection<T> entityList, int batchSize); 入参: User 集合 + 每批提交条数; 返回值: boolean 全部插入成功返回 true")
    @PostMapping("/save-batch-size")
    public R<Map<String, Object>> saveBatchWithSize(@Parameter(description = "每批提交到 DB 的条数") @RequestParam(defaultValue = "1") int batchSize) {
        List<User> users = List.of(
                buildUser("is-batchSize-1", 23, "isBS1@test.com"),
                buildUser("is-batchSize-2", 24, "isBS2@test.com")
        );

        // ===== 入参打印 =====
        System.out.println("===== IService.saveBatch(Collection<T>, int batchSize) 入参 =====");
        System.out.println("  entityList.size() = " + users.size());
        System.out.println("  batchSize (每批条数) = " + batchSize);

        // ===== API 调用 =====
        // 方法签名:    boolean saveBatch(Collection<T> entityList, int batchSize)
        // 入参数据结构: entityList → List<User>; batchSize → int(每批提交到 DB 的条数)
        // 返回值结构:   boolean → 全部插入成功返回 true
        boolean ok = userService.saveBatch(users, batchSize);

        // ===== 出参打印 =====
        System.out.println("===== IService.saveBatch(Collection<T>, int batchSize) 出参 =====");
        System.out.println("  返回值 boolean ok = " + ok + " (指定批次大小的批量插入是否全部成功)");

        Map<String, Object> result = new HashMap<>();
        result.put("ok", ok);
        result.put("batchSize", batchSize);
        return R.ok(result);
    }

    // ==================== saveOrUpdate 系列 API ====================

    @Operation(summary = "保存或更新单条", description = "方法签名: boolean saveOrUpdate(T entity); 入参: User(含 id 主键 → 先 selectById 命中则 update，否则 insert); 返回值: boolean 保存或更新成功返回 true")
    @PostMapping("/save-or-update")
    public R<Map<String, Object>> saveOrUpdate(@RequestBody User request) {
        // ===== 入参打印 =====
        System.out.println("===== IService.saveOrUpdate(T entity) 入参 =====");
        System.out.println("  entity = " + request);
        System.out.println("  判断逻辑: 主键(id)存在 → UPDATE; 主键不存在 → INSERT");

        // ===== API 调用 =====
        // 方法签名:    boolean saveOrUpdate(T entity)
        // 入参数据结构: entity → User，含 id(主键) → 先 selectById 命中则 update，否则 insert
        // 返回值结构:   boolean → 保存或更新成功返回 true
        boolean ok = userService.saveOrUpdate(request);

        // ===== 出参打印 =====
        System.out.println("===== IService.saveOrUpdate(T entity) 出参 =====");
        System.out.println("  返回值 boolean ok = " + ok + " (保存或更新是否成功)");

        Map<String, Object> result = new HashMap<>();
        result.put("ok", ok);
        result.put("entity", request);
        return R.ok(result);
    }

    @Operation(summary = "批量保存或更新", description = "方法签名: boolean saveOrUpdateBatch(Collection<T> entityList); 入参: User 集合(带主键走 update，不带主键走 insert); 返回值: boolean 全部操作成功返回 true")
    @PostMapping("/save-or-update-batch")
    public R<Map<String, Object>> saveOrUpdateBatch() {
        User exists = buildUser("souBatch-exists", 27, "souBE@test.com");
        userService.save(exists);
        User notExists = buildUser("souBatch-new", 28, "souBN@test.com");
        exists.setAge(50);

        List<User> list = List.of(exists, notExists);

        // ===== 入参打印 =====
        System.out.println("===== IService.saveOrUpdateBatch(Collection<T>) 入参 =====");
        System.out.println("  entityList.size() = " + list.size());
        System.out.println("  第1个带主键(exists) → UPDATE: " + exists);
        System.out.println("  第2个不带主键(new) → INSERT: " + notExists);

        // ===== API 调用 =====
        // 方法签名:    boolean saveOrUpdateBatch(Collection<T> entityList)
        // 入参数据结构: entityList → List<User>，带主键的走 update，不带主键的走 insert
        // 返回值结构:   boolean → 全部操作成功返回 true
        boolean ok = userService.saveOrUpdateBatch(list);

        // ===== 出参打印 =====
        System.out.println("===== IService.saveOrUpdateBatch(Collection<T>) 出参 =====");
        System.out.println("  返回值 boolean ok = " + ok + " (批量保存或更新是否全部成功)");

        Map<String, Object> result = new HashMap<>();
        result.put("ok", ok);
        result.put("size", list.size());
        return R.ok(result);
    }

    // ==================== remove 系列 API ====================

    @Operation(summary = "根据主键删除", description = "方法签名: boolean removeById(Serializable id); 入参: 主键 id; 返回值: boolean 删除成功返回 true(基于 ret > -2 语义，0 条也可能为 true)")
    @DeleteMapping("/remove-by-id/{id}")
    public R<Map<String, Object>> removeById(@Parameter(description = "用户主键 id") @PathVariable Long id) {
        // ===== 入参打印 =====
        System.out.println("===== IService.removeById(Serializable id) 入参 =====");
        System.out.println("  id = " + id);

        // ===== API 调用 =====
        // 方法签名:    boolean removeById(Serializable id)
        // 入参数据结构: id → Long (主键值)
        // 返回值结构:   boolean → 删除成功返回 true(基于 ret > -2 语义，0 条也可能为 true)
        boolean ok = userService.removeById(id);

        // ===== 出参打印 =====
        System.out.println("===== IService.removeById(Serializable id) 出参 =====");
        System.out.println("  返回值 boolean ok = " + ok + " (按主键删除是否成功)");

        Map<String, Object> result = new HashMap<>();
        result.put("ok", ok);
        return R.ok(result);
    }

    @Operation(summary = "根据 Map 条件删除", description = "方法签名: boolean removeByMap(Map<String,Object> columnMap); 入参: 列名→列值 的 Map(多 key 为 AND); 返回值: boolean 至少影响 1 行返回 true")
    @DeleteMapping("/remove-by-map")
    public R<Map<String, Object>> removeByMap(@Parameter(description = "作为删除条件的列值(name)") @RequestParam String name) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("name", name);

        // ===== 入参打印 =====
        System.out.println("===== IService.removeByMap(Map) 入参 =====");
        System.out.println("  columnMap = " + columnMap);

        // ===== API 调用 =====
        // 方法签名:    boolean removeByMap(Map<String,Object> columnMap)
        // 入参数据结构: columnMap → Map<列名:String, 列值:Object>，多 key 为 AND
        // 返回值结构:   boolean → 至少影响 1 行返回 true
        boolean ok = userService.removeByMap(columnMap);

        // ===== 出参打印 =====
        System.out.println("===== IService.removeByMap(Map) 出参 =====");
        System.out.println("  返回值 boolean ok = " + ok + " (至少删除 1 条即为 true)");

        Map<String, Object> result = new HashMap<>();
        result.put("ok", ok);
        return R.ok(result);
    }

    @Operation(summary = "根据条件构造器删除", description = "方法签名: boolean remove(Wrapper<T> queryWrapper); 入参: LambdaQueryWrapper(WHERE 条件); 返回值: boolean 按条件删除成功返回 true")
    @DeleteMapping("/remove")
    public R<Map<String, Object>> remove(@Parameter(description = "删除条件：name 等于该值") @RequestParam String name) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getName, name);

        // ===== 入参打印 =====
        System.out.println("===== IService.remove(Wrapper) 入参 =====");
        System.out.println("  wrapper (LambdaQueryWrapper) = " + wrapper);
        System.out.println("  条件: name = '" + name + "'");

        // ===== API 调用 =====
        // 方法签名:    boolean remove(Wrapper<T> queryWrapper)
        // 入参数据结构: wrapper → LambdaQueryWrapper<User>，封装 WHERE 条件
        // 返回值结构:   boolean → 按条件删除成功返回 true
        boolean ok = userService.remove(wrapper);

        // ===== 出参打印 =====
        System.out.println("===== IService.remove(Wrapper) 出参 =====");
        System.out.println("  返回值 boolean ok = " + ok + " (按条件删除是否成功)");

        Map<String, Object> result = new HashMap<>();
        result.put("ok", ok);
        return R.ok(result);
    }

    @Operation(summary = "根据主键集合删除", description = "方法签名: boolean removeByIds(Collection<?> idList); 入参: 主键集合; 返回值: boolean 批量删除成功返回 true")
    @DeleteMapping("/remove-by-ids")
    public R<Map<String, Object>> removeByIds(@Parameter(description = "待删除的主键 id 集合，多个用逗号分隔") @RequestParam List<Long> ids) {
        // ===== 入参打印 =====
        System.out.println("===== IService.removeByIds(Collection<?>) 入参 =====");
        System.out.println("  idList = " + ids);

        // ===== API 调用 =====
        // 方法签名:    boolean removeByIds(Collection<?> idList)
        // 入参数据结构: idList → Collection<Long> 主键集合
        // 返回值结构:   boolean → 批量删除成功返回 true
        boolean ok = userService.removeByIds(ids);

        // ===== 出参打印 =====
        System.out.println("===== IService.removeByIds(Collection<?>) 出参 =====");
        System.out.println("  返回值 boolean ok = " + ok + " (按主键集合批量删除是否全部成功)");

        Map<String, Object> result = new HashMap<>();
        result.put("ok", ok);
        return R.ok(result);
    }

    @Operation(summary = "JDBC 批量按主键删除", description = "方法签名: boolean removeBatchByIds(Collection<?> idList) [3.5.0+ 新增]; 入参: 主键集合; 返回值: boolean 批量删除全部成功返回 true")
    @DeleteMapping("/remove-batch-by-ids")
    public R<Map<String, Object>> removeBatchByIds(@Parameter(description = "待删除的主键 id 集合，多个用逗号分隔") @RequestParam List<Long> ids) {
        // ===== 入参打印 =====
        System.out.println("===== IService.removeBatchByIds(Collection<?>) 入参 =====");
        System.out.println("  idList = " + ids);
        System.out.println("  (JDBC 批量删除, 3.5.0+ 新增)");

        // ===== API 调用 =====
        // 方法签名:    boolean removeBatchByIds(Collection<?> idList)   // 3.5.0+ 新增，JDBC 批量
        // 入参数据结构: idList → Collection<Long> 主键集合
        // 返回值结构:   boolean → 批量删除全部成功返回 true
        boolean ok = userService.removeBatchByIds(ids);

        // ===== 出参打印 =====
        System.out.println("===== IService.removeBatchByIds(Collection<?>) 出参 =====");
        System.out.println("  返回值 boolean ok = " + ok + " (批量删除是否全部成功)");

        Map<String, Object> result = new HashMap<>();
        result.put("ok", ok);
        return R.ok(result);
    }

    // ==================== update 系列 API ====================

    @Operation(summary = "根据主键更新", description = "方法签名: boolean updateById(T entity); 入参: 带主键的 User(非 null 字段参与 SET，null 字段不更新); 返回值: boolean 按主键更新成功返回 true")
    @PutMapping("/update-by-id")
    public R<Map<String, Object>> updateById(@RequestBody User request) {
        // ===== 入参打印 =====
        System.out.println("===== IService.updateById(T entity) 入参 =====");
        System.out.println("  entity (带主键) = " + request);

        // ===== API 调用 =====
        // 方法签名:    boolean updateById(T entity)
        // 入参数据结构: entity → User，必须含 id(主键)；非 null 字段参与 SET，null 字段不更新
        // 返回值结构:   boolean → 按主键更新成功返回 true
        boolean ok = userService.updateById(request);

        // ===== 出参打印 =====
        System.out.println("===== IService.updateById(T entity) 出参 =====");
        System.out.println("  返回值 boolean ok = " + ok + " (按主键更新是否成功)");

        Map<String, Object> result = new HashMap<>();
        result.put("ok", ok);
        return R.ok(result);
    }

    @Operation(summary = "根据条件构造器更新", description = "方法签名: boolean update(T entity, Wrapper<T> updateWrapper); 入参: entity(SET 部分) + LambdaUpdateWrapper(WHERE 条件); 返回值: boolean 按条件更新成功返回 true")
    @PutMapping("/update")
    public R<Map<String, Object>> update(
            @Parameter(description = "更新条件：name 等于该值的记录") @RequestParam String whereName,
            @Parameter(description = "更新后的新年龄") @RequestParam Integer newAge) {

        User updateEntity = new User();
        updateEntity.setAge(newAge);

        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<User>()
                .eq(User::getName, whereName);

        // ===== 入参打印 =====
        System.out.println("===== IService.update(entity, wrapper) 入参 =====");
        System.out.println("  entity (更新字段) = " + updateEntity);
        System.out.println("  wrapper (LambdaUpdateWrapper) = " + wrapper);
        System.out.println("  条件: name = '" + whereName + "'; 新值: age = " + newAge);

        // ===== API 调用 =====
        // 方法签名:    boolean update(T entity, Wrapper<T> updateWrapper)
        // 入参数据结构: entity → User(SET 部分, 非 null 字段才更新); wrapper → LambdaUpdateWrapper<User>(WHERE 条件)
        // 返回值结构:   boolean → 按条件更新成功返回 true
        boolean ok = userService.update(updateEntity, wrapper);

        // ===== 出参打印 =====
        System.out.println("===== IService.update(entity, wrapper) 出参 =====");
        System.out.println("  返回值 boolean ok = " + ok + " (按条件更新是否成功)");

        Map<String, Object> result = new HashMap<>();
        result.put("ok", ok);
        return R.ok(result);
    }

    @Operation(summary = "批量按主键更新", description = "方法签名: boolean updateBatchById(Collection<T> entityList); 入参: 带主键的 User 集合; 返回值: boolean 批量按主键更新全部成功返回 true")
    @PutMapping("/update-batch-by-id")
    public R<Map<String, Object>> updateBatchById() {
        User u1 = buildUser("updBatch-1", 40, "updB1@test.com");
        User u2 = buildUser("updBatch-2", 41, "updB2@test.com");
        userService.saveBatch(List.of(u1, u2));

        u1.setAge(100);
        u2.setAge(101);
        List<User> list = List.of(u1, u2);

        // ===== 入参打印 =====
        System.out.println("===== IService.updateBatchById(Collection<T>) 入参 =====");
        System.out.println("  entityList.size() = " + list.size());
        System.out.println("  entityList (带主键) = " + list);

        // ===== API 调用 =====
        // 方法签名:    boolean updateBatchById(Collection<T> entityList)
        // 入参数据结构: entityList → List<User>，每个元素必须带 id(主键)
        // 返回值结构:   boolean → 批量按主键更新全部成功返回 true
        boolean ok = userService.updateBatchById(list);

        // ===== 出参打印 =====
        System.out.println("===== IService.updateBatchById(Collection<T>) 出参 =====");
        System.out.println("  返回值 boolean ok = " + ok + " (批量按主键更新是否全部成功)");

        Map<String, Object> result = new HashMap<>();
        result.put("ok", ok);
        return R.ok(result);
    }

    // ==================== get 系列 API — 查询单条 ====================

    @Operation(summary = "根据主键查询单条", description = "方法签名: T getById(Serializable id); 入参: 主键 id; 返回值: User 实体，未命中返回 null")
    @GetMapping("/get-by-id/{id}")
    public R<User> getById(@Parameter(description = "用户主键 id") @PathVariable Long id) {
        // ===== 入参打印 =====
        System.out.println("===== IService.getById(Serializable id) 入参 =====");
        System.out.println("  id = " + id);

        // ===== API 调用 =====
        // 方法签名:    T getById(Serializable id)
        // 入参数据结构: id → Long (主键值)
        // 返回值结构:   User → 命中的实体记录；未命中返回 null
        User found = userService.getById(id);

        // ===== 出参打印 =====
        System.out.println("===== IService.getById(Serializable id) 出参 =====");
        System.out.println("  返回值 User = " + found);

        return R.ok(found);
    }

    @Operation(summary = "根据条件查询单条(多条抛异常)", description = "方法签名: T getOne(Wrapper<T> queryWrapper); 入参: LambdaQueryWrapper; 返回值: User；0 条返回 null，>1 条抛 TooManyResultsException")
    @GetMapping("/get-one")
    public R<User> getOne(@Parameter(description = "查询条件：name 等于该值(需保证唯一)") @RequestParam String name) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getName, name);

        // ===== 入参打印 =====
        System.out.println("===== IService.getOne(Wrapper) 入参 =====");
        System.out.println("  wrapper (LambdaQueryWrapper) = " + wrapper);
        System.out.println("  条件: name = '" + name + "'; 多条时抛 TooManyResultsException");

        // ===== API 调用 =====
        // 方法签名:    T getOne(Wrapper<T> queryWrapper)
        // 入参数据结构: wrapper → LambdaQueryWrapper<User>，WHERE 条件
        // 返回值结构:   User → 0 条返回 null；>1 条抛 TooManyResultsException
        User found = userService.getOne(wrapper);

        // ===== 出参打印 =====
        System.out.println("===== IService.getOne(Wrapper) 出参 =====");
        System.out.println("  返回值 User = " + found);

        return R.ok(found);
    }

    @Operation(summary = "根据条件查询单条(多条不抛异常)", description = "方法签名: T getOne(Wrapper<T> queryWrapper, boolean throwEx); 入参: LambdaQueryWrapper + throwEx(false=多条取第一条不抛异常); 返回值: User 多条时取第一条")
    @GetMapping("/get-one-safe")
    public R<User> getOneWithoutThrow(@Parameter(description = "查询条件：name 等于该值") @RequestParam String name) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getName, name);

        // ===== 入参打印 =====
        System.out.println("===== IService.getOne(Wrapper, boolean throwEx) 入参 =====");
        System.out.println("  wrapper = " + wrapper);
        System.out.println("  throwEx = false (多条时取第一条, 不抛异常)");
        System.out.println("  条件: name = '" + name + "'");

        // ===== API 调用 =====
        // 方法签名:    T getOne(Wrapper<T> queryWrapper, boolean throwEx)
        // 入参数据结构: wrapper → LambdaQueryWrapper<User>; throwEx → boolean(false=多条取第一条不抛异常)
        // 返回值结构:   User → 多条时取第一条；throwEx=true 且多条时抛 TooManyResultsException
        User found = userService.getOne(wrapper, false);

        // ===== 出参打印 =====
        System.out.println("===== IService.getOne(Wrapper, boolean throwEx) 出参 =====");
        System.out.println("  返回值 User = " + found);

        return R.ok(found);
    }

    @Operation(summary = "查询单条记录为 Map", description = "方法签名: Map<String,Object> getMap(Wrapper<T> queryWrapper); 入参: LambdaQueryWrapper(可 .select 指定列); 返回值: Map{列名→列值}，未命中返回 null")
    @GetMapping("/get-map")
    public R<Map<String, Object>> getMap(@Parameter(description = "查询条件：name 等于该值") @RequestParam String name) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getName, name)
                .select(User::getName, User::getAge);

        // ===== 入参打印 =====
        System.out.println("===== IService.getMap(Wrapper) 入参 =====");
        System.out.println("  wrapper (LambdaQueryWrapper) = " + wrapper);
        System.out.println("  条件: name = '" + name + "'; 只查询 name, age");

        // ===== API 调用 =====
        // 方法签名:    Map<String,Object> getMap(Wrapper<T> queryWrapper)
        // 入参数据结构: wrapper → LambdaQueryWrapper<User>，可通过 .select() 指定查询列
        // 返回值结构:   Map<String,Object> → 单条记录，key=列名，value=列值；未命中返回 null
        Map<String, Object> map = userService.getMap(wrapper);

        // ===== 出参打印 =====
        System.out.println("===== IService.getMap(Wrapper) 出参 =====");
        System.out.println("  返回值 Map<String,Object> (单条记录, key 为列名) = " + map);

        return R.ok(map);
    }

    @Operation(summary = "查询第一列值(经 Function 转换)", description = "方法签名: Object getObj(Wrapper<T> queryWrapper, Function<? super Object,Object> mapper); 入参: LambdaQueryWrapper(.select 指定列) + 转换函数; 返回值: Object 第一列值经 mapper 转换后，多条取第一条")
    @GetMapping("/get-obj")
    public R<Object> getObj(@Parameter(description = "查询条件：name 等于该值") @RequestParam String name) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getName, name)
                .select(User::getName);

        // ===== 入参打印 =====
        System.out.println("===== IService.getObj(Wrapper, Function) 入参 =====");
        System.out.println("  wrapper = " + wrapper);
        System.out.println("  Function = Object::toString");
        System.out.println("  条件: name = '" + name + "'; 只取第一列");

        // ===== API 调用 =====
        // 方法签名:    Object getObj(Wrapper<T> queryWrapper, Function<? super Object, Object> mapper)
        // 入参数据结构: wrapper → LambdaQueryWrapper<User>，.select() 指定列；mapper → 转换函数(此处 Object::toString)
        // 返回值结构:   Object → 第一列值经 mapper 转换后；多条时取第一条
        Object obj = userService.getObj(wrapper, Object::toString);

        // ===== 出参打印 =====
        System.out.println("===== IService.getObj(Wrapper, Function) 出参 =====");
        System.out.println("  返回值 Object (第一列值, 经 Function 转换) = " + obj);

        return R.ok(obj);
    }

    // ==================== list / count / exists 系列 API ====================

    @Operation(summary = "查询全部列表", description = "方法签名: List<T> list(); 入参: 无; 返回值: List<User> 全表数据(可能很大，生产环境慎用)")
    @GetMapping("/list")
    public R<List<User>> list() {
        // ===== 入参打印 =====
        System.out.println("===== IService.list() 入参 =====");
        System.out.println("  无入参 (查询全部)");

        // ===== API 调用 =====
        // 方法签名:    List<T> list()
        // 入参数据结构: 无入参
        // 返回值结构:   List<User> → 全表数据(可能很大，生产环境慎用)
        List<User> list = userService.list();

        // ===== 出参打印 =====
        System.out.println("===== IService.list() 出参 =====");
        System.out.println("  返回值 List<User>.size() = " + (list != null ? list.size() : "null"));

        return R.ok(list);
    }

    @Operation(summary = "根据条件查询列表", description = "方法签名: List<T> list(Wrapper<T> queryWrapper); 入参: LambdaQueryWrapper(WHERE 条件); 返回值: List<User> 命中记录列表")
    @GetMapping("/list-by-wrapper")
    public R<List<User>> listByWrapper(@Parameter(description = "查询条件：name IN 该集合，多个用逗号分隔") @RequestParam List<String> names) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .in(User::getName, names);

        // ===== 入参打印 =====
        System.out.println("===== IService.list(Wrapper) 入参 =====");
        System.out.println("  wrapper (LambdaQueryWrapper) = " + wrapper);
        System.out.println("  条件: name IN " + names);

        // ===== API 调用 =====
        // 方法签名:    List<T> list(Wrapper<T> queryWrapper)
        // 入参数据结构: wrapper → LambdaQueryWrapper<User>，WHERE 条件
        // 返回值结构:   List<User> → 命中记录列表
        List<User> list = userService.list(wrapper);

        // ===== 出参打印 =====
        System.out.println("===== IService.list(Wrapper) 出参 =====");
        System.out.println("  返回值 List<User>.size() = " + (list != null ? list.size() : "null"));
        if (list != null) {
            list.forEach(u -> System.out.println("    User: id=" + u.getId() + ", name=" + u.getName()));
        }

        return R.ok(list);
    }

    @Operation(summary = "根据主键集合查询列表", description = "方法签名: List<T> listByIds(Collection<?> idList); 入参: 主键集合; 返回值: List<User> 命中记录列表，不存在的 id 自动忽略")
    @GetMapping("/list-by-ids")
    public R<List<User>> listByIds(@Parameter(description = "主键 id 集合，多个用逗号分隔") @RequestParam List<Long> ids) {
        // ===== 入参打印 =====
        System.out.println("===== IService.listByIds(Collection<?>) 入参 =====");
        System.out.println("  idList = " + ids);

        // ===== API 调用 =====
        // 方法签名:    List<T> listByIds(Collection<?> idList)
        // 入参数据结构: idList → Collection<Long> 主键集合
        // 返回值结构:   List<User> → 命中记录列表，不存在的 id 自动忽略
        List<User> list = userService.listByIds(ids);

        // ===== 出参打印 =====
        System.out.println("===== IService.listByIds(Collection<?>) 出参 =====");
        System.out.println("  返回值 List<User>.size() = " + (list != null ? list.size() : "null"));
        if (list != null) {
            list.forEach(u -> System.out.println("    User: " + u));
        }

        return R.ok(list);
    }

    @Operation(summary = "根据 Map 条件查询列表", description = "方法签名: List<T> listByMap(Map<String,Object> columnMap); 入参: 列名→列值 的 Map(多 key 为 AND); 返回值: List<User> 命中记录列表")
    @GetMapping("/list-by-map")
    public R<List<User>> listByMap(@Parameter(description = "查询条件列值(name)") @RequestParam String name) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("name", name);

        // ===== 入参打印 =====
        System.out.println("===== IService.listByMap(Map) 入参 =====");
        System.out.println("  columnMap = " + columnMap);

        // ===== API 调用 =====
        // 方法签名:    List<T> listByMap(Map<String,Object> columnMap)
        // 入参数据结构: columnMap → Map<列名:String, 列值:Object>，多 key 为 AND
        // 返回值结构:   List<User> → 命中记录列表
        List<User> list = userService.listByMap(columnMap);

        // ===== 出参打印 =====
        System.out.println("===== IService.listByMap(Map) 出参 =====");
        System.out.println("  返回值 List<User>.size() = " + (list != null ? list.size() : "null"));

        return R.ok(list);
    }

    @Operation(summary = "查询记录总数", description = "方法签名: long count(Wrapper<T> queryWrapper); 入参: LambdaQueryWrapper(WHERE 条件); 返回值: long 符合条件的记录总数")
    @GetMapping("/count")
    public R<Long> count() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .isNotNull(User::getId);

        // ===== 入参打印 =====
        System.out.println("===== IService.count(Wrapper) 入参 =====");
        System.out.println("  wrapper (LambdaQueryWrapper) = " + wrapper);
        System.out.println("  条件: id IS NOT NULL");

        // ===== API 调用 =====
        // 方法签名:    long count(Wrapper<T> queryWrapper)
        // 入参数据结构: wrapper → LambdaQueryWrapper<User>，WHERE 条件
        // 返回值结构:   long → 符合条件的记录总数
        long count = userService.count(wrapper);

        // ===== 出参打印 =====
        System.out.println("===== IService.count(Wrapper) 出参 =====");
        System.out.println("  返回值 long count = " + count);

        return R.ok(count);
    }

    @Operation(summary = "判断记录是否存在", description = "方法签名: boolean exists(Wrapper<T> queryWrapper) [3.4.3.2+ 新增]; 入参: LambdaQueryWrapper(WHERE 条件); 返回值: boolean 存在符合条件记录返回 true(底层 SELECT 1 LIMIT 1)")
    @GetMapping("/exists")
    public R<Boolean> exists(@Parameter(description = "查询条件：name 等于该值") @RequestParam String name) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getName, name);

        // ===== 入参打印 =====
        System.out.println("===== IService.exists(Wrapper) 入参 =====");
        System.out.println("  wrapper = " + wrapper);
        System.out.println("  条件: name = '" + name + "'");

        // ===== API 调用 =====
        // 方法签名:    boolean exists(Wrapper<T> queryWrapper)   // 3.4.3.2+ 新增
        // 入参数据结构: wrapper → LambdaQueryWrapper<User>，WHERE 条件
        // 返回值结构:   boolean → 存在符合条件记录返回 true(底层 SELECT 1 LIMIT 1)
        boolean exists = userService.exists(wrapper);

        // ===== 出参打印 =====
        System.out.println("===== IService.exists(Wrapper) 出参 =====");
        System.out.println("  返回值 boolean exists = " + exists + " (是否存在符合条件的记录)");

        return R.ok(exists);
    }

    // ==================== page 系列 API ====================

    @Operation(summary = "无条件分页查询", description = "方法签名: IPage<T> page(IPage<T> page); 入参: Page{current,size}(无条件); 返回值: IPage{records,total,pages,size}")
    @GetMapping("/page")
    public R<IPage<User>> page(
            @Parameter(description = "当前页码，从 1 开始") @RequestParam(defaultValue = "1") long current,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "2") long size) {

        Page<User> page = new Page<>(current, size);

        // ===== 入参打印 =====
        System.out.println("===== IService.page(IPage) 入参 =====");
        System.out.println("  page (IPage) = " + page);
        System.out.println("  无条件分页");

        // ===== API 调用 =====
        // 方法签名:    IPage<T> page(IPage<T> page)
        // 入参数据结构: page → Page<User> { current:long(当前页), size:long(每页条数) }，无条件
        // 返回值结构:   IPage<User> → { records:List<User>(当前页数据), total:long(总数), pages:long(总页数), size:long }
        IPage<User> result = userService.page(page);

        // ===== 出参打印 =====
        System.out.println("===== IService.page(IPage) 出参 =====");
        System.out.println("  返回值 IPage<User>.total = " + result.getTotal());
        System.out.println("  返回值 IPage<User>.pages = " + result.getPages());
        System.out.println("  返回值 IPage<User>.size = " + result.getSize());
        System.out.println("  返回值 IPage<User>.records.size() = " + result.getRecords().size());

        return R.ok(result);
    }

    @Operation(summary = "带条件分页查询", description = "方法签名: IPage<T> page(IPage<T> page, Wrapper<T> queryWrapper); 入参: Page{current,size} + LambdaQueryWrapper(条件 age >= minAge); 返回值: IPage{records,total,pages,size}")
    @GetMapping("/page-by-wrapper")
    public R<IPage<User>> pageByWrapper(
            @Parameter(description = "当前页码，从 1 开始") @RequestParam(defaultValue = "1") long current,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "5") long size,
            @Parameter(description = "查询条件：年龄大于等于该值") @RequestParam(defaultValue = "0") int minAge) {

        Page<User> page = new Page<>(current, size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .ge(User::getAge, minAge);

        // ===== 入参打印 =====
        System.out.println("===== IService.page(IPage, Wrapper) 入参 =====");
        System.out.println("  page = " + page);
        System.out.println("  wrapper (LambdaQueryWrapper) = " + wrapper);
        System.out.println("  条件: age >= " + minAge);

        // ===== API 调用 =====
        // 方法签名:    IPage<T> page(IPage<T> page, Wrapper<T> queryWrapper)
        // 入参数据结构: page → Page<User>; wrapper → LambdaQueryWrapper<User>，条件 age >= minAge
        // 返回值结构:   IPage<User> → { records:List<User>, total:long, pages:long, size:long }
        IPage<User> result = userService.page(page, wrapper);

        // ===== 出参打印 =====
        System.out.println("===== IService.page(IPage, Wrapper) 出参 =====");
        System.out.println("  返回值 IPage<User>.total = " + result.getTotal());
        System.out.println("  返回值 IPage<User>.records.size() = " + result.getRecords().size());

        return R.ok(result);
    }

    @Operation(summary = "带条件分页查询(3.5.7 重载)", description = "方法签名: IPage<T> page(IPage<T> page, Wrapper<T> queryWrapper) [3.5.7 重载解析，含查询优化]; 入参: Page + LambdaQueryWrapper(条件 name IS NOT NULL); 返回值: IPage{records,total,pages,size}")
    @GetMapping("/page-with-wrapper-357")
    public R<IPage<User>> pageWithWrapper(
            @Parameter(description = "当前页码，从 1 开始") @RequestParam(defaultValue = "1") long current,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "3") long size) {

        Page<User> page = new Page<>(current, size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .isNotNull(User::getName);

        // ===== 入参打印 =====
        System.out.println("===== IService.page(IPage, Wrapper) 3.5.7+重载 入参 =====");
        System.out.println("  page = " + page);
        System.out.println("  wrapper = " + wrapper);
        System.out.println("  条件: name IS NOT NULL");

        // ===== API 调用 =====
        // 方法签名:    IPage<T> page(IPage<T> page, Wrapper<T> queryWrapper)   // 3.5.7 重载解析，含查询优化
        // 入参数据结构: page → Page<User>; wrapper → LambdaQueryWrapper<User>，条件 name IS NOT NULL
        // 返回值结构:   IPage<User> → { records:List<User>, total:long, pages:long, size:long }
        IPage<User> result = userService.page(page, wrapper);

        // ===== 出参打印 =====
        System.out.println("===== IService.page(IPage, Wrapper) 3.5.7+重载 出参 =====");
        System.out.println("  返回值 IPage<User>.total = " + result.getTotal());
        System.out.println("  返回值 IPage<User>.records.size() = " + result.getRecords().size());

        return R.ok(result);
    }

    // ==================== 辅助 ====================

    private User buildUser(String name, Integer age, String email) {
        User user = new User();
        user.setName(name);
        user.setAge(age);
        user.setEmail(email);
        return user;
    }
}
