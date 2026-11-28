package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.R;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "BaseMapper API 演示", description = "演示 MyBatis-Plus BaseMapper 接口的全部方法：插入 / 删除 / 更新 / 查询 / 分页")
@RestController
@RequestMapping("/demo/base-mapper")
@RequiredArgsConstructor
public class BaseMapperDemoController {

    private final UserMapper userMapper;

    // ==================== 插入 API ====================

    @Operation(summary = "插入单条记录", description = "方法签名: int insert(T entity); 入参: User 实体{name,age,email}; 返回值: 受影响行数，自增主键回填到 entity.id")
    @PostMapping("/insert")
    public R<Map<String, Object>> insert(@RequestBody User request) {
        User entity = new User();
        entity.setName(request.getName() != null ? request.getName() : "mapper-insert-demo");
        entity.setAge(request.getAge() != null ? request.getAge() : 20);
        entity.setEmail(request.getEmail() != null ? request.getEmail() : "mapper-insert@test.com");

        // ===== 入参打印 =====
        System.out.println("===== BaseMapper.insert 入参 =====");
        System.out.println("  entity = " + entity);

        // ===== API 调用 =====
        // 方法签名:    int insert(T entity)
        // 入参数据结构: entity → User { id:Long(插入时为 null), name:String, age:Integer, email:String }
        // 返回值结构:   int → 受影响行数(成功 = 1)；副作用：自增主键回填到 entity.id
        int rows = userMapper.insert(entity);

        // ===== 出参打印 =====
        System.out.println("===== BaseMapper.insert 出参 =====");
        System.out.println("  返回值 rows (受影响行数) = " + rows);
        System.out.println("  主键回填 entity.getId() = " + entity.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("savedId", entity.getId());
        return R.ok(result);
    }

    @Operation(summary = "批量插入记录", description = "方法签名: List<BatchResult> insert(Collection<T> entityList) [3.5.7+ 新增]; 入参: User 集合; 返回值: 每个 BatchResult 含 int[] updateCounts(该批次受影响行数)")
    @PostMapping("/insert-batch")
    public R<Map<String, Object>> insertBatch() {
        List<User> users = List.of(
                buildUser("mapper-batch-1", 21, "mB1@test.com"),
                buildUser("mapper-batch-2", 22, "mB2@test.com")
        );

        // ===== 入参打印 =====
        System.out.println("===== BaseMapper.insert(Collection) 入参 =====");
        System.out.println("  entityList.size() = " + users.size());
        System.out.println("  entityList = " + users);

        // ===== API 调用 =====
        // 方法签名:    List<BatchResult> insert(Collection<T> entityList)   // 3.5.7+ 新增
        // 入参数据结构: entityList → List<User>，每个元素 User { id, name, age, email }
        // 返回值结构:   List<BatchResult> → 每个 BatchResult 含 int[] updateCounts(该批次受影响行数)
        List<BatchResult> rows = userMapper.insert(users);

        // ===== 出参打印 =====
        System.out.println("===== BaseMapper.insert(Collection) 出参 =====");
        System.out.println("  返回值 List<BatchResult>.size() = " + (rows != null ? rows.size() : "null"));
        if (rows != null) {
            rows.forEach(r -> System.out.println("    BatchResult updateCounts = " + r.getUpdateCounts()));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("batchResultSize", rows != null ? rows.size() : 0);
        return R.ok(result);
    }

    // ==================== 删除 API ====================

    @Operation(summary = "根据主键删除", description = "方法签名: int deleteById(Serializable id); 入参: 主键 id; 返回值: 受影响行数(成功=1，不存在=0)")
    @DeleteMapping("/delete-by-id/{id}")
    public R<Map<String, Object>> deleteById(@Parameter(description = "用户主键 id") @PathVariable Long id) {
        // ===== 入参打印 =====
        System.out.println("===== BaseMapper.deleteById 入参 =====");
        System.out.println("  id (Serializable) = " + id);

        // ===== API 调用 =====
        // 方法签名:    int deleteById(Serializable id)
        // 入参数据结构: id → Long (主键值)
        // 返回值结构:   int → 受影响行数(成功 = 1，记录不存在 = 0)
        int rows = userMapper.deleteById(id);

        // ===== 出参打印 =====
        System.out.println("===== BaseMapper.deleteById 出参 =====");
        System.out.println("  返回值 rows (受影响行数) = " + rows);

        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        return R.ok(result);
    }

    @Operation(summary = "根据 Map 条件删除", description = "方法签名: int deleteByMap(Map<String,Object> columnMap); 入参: 列名→列值 的 Map(多 key 为 AND); 返回值: 受影响行数")
    @DeleteMapping("/delete-by-map")
    public R<Map<String, Object>> deleteByMap(@Parameter(description = "作为删除条件的列值(name)") @RequestParam String name) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("name", name);

        // ===== 入参打印 =====
        System.out.println("===== BaseMapper.deleteByMap 入参 =====");
        System.out.println("  columnMap (Map<String,Object>) = " + columnMap);

        // ===== API 调用 =====
        // 方法签名:    int deleteByMap(Map<String,Object> columnMap)
        // 入参数据结构: columnMap → Map<列名:String, 列值:Object>，多个 key 之间为 AND 关系
        // 返回值结构:   int → 受影响行数
        int rows = userMapper.deleteByMap(columnMap);

        // ===== 出参打印 =====
        System.out.println("===== BaseMapper.deleteByMap 出参 =====");
        System.out.println("  返回值 rows (受影响行数) = " + rows);

        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        return R.ok(result);
    }

    @Operation(summary = "根据条件构造器删除", description = "方法签名: int delete(Wrapper<T> wrapper); 入参: LambdaQueryWrapper(WHERE 条件); 返回值: 受影响行数")
    @DeleteMapping("/delete-by-wrapper")
    public R<Map<String, Object>> deleteByWrapper(@Parameter(description = "删除条件：name 等于该值") @RequestParam String name) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getName, name);

        // ===== 入参打印 =====
        System.out.println("===== BaseMapper.delete(Wrapper) 入参 =====");
        System.out.println("  wrapper (LambdaQueryWrapper) = " + wrapper);
        System.out.println("  实际构造 SQL 条件: name = '" + name + "'");

        // ===== API 调用 =====
        // 方法签名:    int delete(Wrapper<T> wrapper)
        // 入参数据结构: wrapper → LambdaQueryWrapper<User>，封装 WHERE 条件(eq/ne/in/gt ...)
        // 返回值结构:   int → 受影响行数
        int rows = userMapper.delete(wrapper);

        // ===== 出参打印 =====
        System.out.println("===== BaseMapper.delete(Wrapper) 出参 =====");
        System.out.println("  返回值 rows (受影响行数) = " + rows);

        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        return R.ok(result);
    }

    @Operation(summary = "根据主键集合批量删除", description = "方法签名: int deleteBatchIds(Collection<?> idList); 入参: 主键集合; 返回值: 删除的总条数")
    @DeleteMapping("/delete-batch-ids")
    public R<Map<String, Object>> deleteBatchIds(@Parameter(description = "待删除的主键 id 集合，多个用逗号分隔") @RequestParam List<Long> ids) {
        // ===== 入参打印 =====
        System.out.println("===== BaseMapper.deleteBatchIds 入参 =====");
        System.out.println("  idList (Collection<?>) = " + ids);

        // ===== API 调用 =====
        // 方法签名:    int deleteBatchIds(Collection<?> idList)
        // 入参数据结构: idList → Collection<Long> 主键集合
        // 返回值结构:   int → 受影响行数(删除的总条数)
        int rows = userMapper.deleteBatchIds(ids);

        // ===== 出参打印 =====
        System.out.println("===== BaseMapper.deleteBatchIds 出参 =====");
        System.out.println("  返回值 rows (受影响行数) = " + rows);

        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        return R.ok(result);
    }

    // ==================== 更新 API ====================

    @Operation(summary = "根据主键更新", description = "方法签名: int updateById(T entity); 入参: 带主键的 User(非 null 字段参与 SET，null 字段不更新); 返回值: 受影响行数")
    @PutMapping("/update-by-id")
    public R<Map<String, Object>> updateById(@RequestBody User request) {
        // ===== 入参打印 =====
        System.out.println("===== BaseMapper.updateById 入参 =====");
        System.out.println("  entity (带主键) = " + request);

        // ===== API 调用 =====
        // 方法签名:    int updateById(T entity)
        // 入参数据结构: entity → User，必须含 id(主键)；非 null 字段参与 SET，null 字段不更新
        // 返回值结构:   int → 受影响行数(成功 = 1)
        int rows = userMapper.updateById(request);

        // ===== 出参打印 =====
        System.out.println("===== BaseMapper.updateById 出参 =====");
        System.out.println("  返回值 rows (受影响行数) = " + rows);

        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        return R.ok(result);
    }

    @Operation(summary = "根据条件构造器更新", description = "方法签名: int update(T entity, Wrapper<T> updateWrapper); 入参: entity(SET 部分) + UpdateWrapper(WHERE 条件); 返回值: 受影响行数")
    @PutMapping("/update-by-wrapper")
    public R<Map<String, Object>> updateByWrapper(
            @Parameter(description = "更新条件：name 等于该值的记录") @RequestParam String whereName,
            @Parameter(description = "更新后的新年龄") @RequestParam Integer newAge) {

        User updateEntity = new User();
        updateEntity.setAge(newAge);

        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .eq("name", whereName);

        // ===== 入参打印 =====
        System.out.println("===== BaseMapper.update(entity, wrapper) 入参 =====");
        System.out.println("  entity (更新字段) = " + updateEntity);
        System.out.println("  wrapper (更新条件, UpdateWrapper) = " + wrapper);
        System.out.println("  条件: name = '" + whereName + "'; 新值: age = " + newAge);

        // ===== API 调用 =====
        // 方法签名:    int update(T entity, Wrapper<T> updateWrapper)
        // 入参数据结构: entity → User(SET 部分, 非 null 字段才更新); wrapper → UpdateWrapper<User>(WHERE 条件)
        // 返回值结构:   int → 受影响行数
        int rows = userMapper.update(updateEntity, wrapper);

        // ===== 出参打印 =====
        System.out.println("===== BaseMapper.update(entity, wrapper) 出参 =====");
        System.out.println("  返回值 rows (受影响行数) = " + rows);

        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        return R.ok(result);
    }

    // ==================== 查询 API — 单条 ====================

    @Operation(summary = "根据主键查询单条", description = "方法签名: T selectById(Serializable id); 入参: 主键 id; 返回值: User 实体，未命中返回 null")
    @GetMapping("/select-by-id/{id}")
    public R<User> selectById(@Parameter(description = "用户主键 id") @PathVariable Long id) {
        // ===== 入参打印 =====
        System.out.println("===== BaseMapper.selectById 入参 =====");
        System.out.println("  id (Serializable) = " + id);

        // ===== API 调用 =====
        // 方法签名:    T selectById(Serializable id)
        // 入参数据结构: id → Long (主键值)
        // 返回值结构:   User → 命中的实体记录；未命中返回 null
        User found = userMapper.selectById(id);

        // ===== 出参打印 =====
        System.out.println("===== BaseMapper.selectById 出参 =====");
        System.out.println("  返回值 User = " + found);

        return R.ok(found);
    }

    @Operation(summary = "根据条件查询单条", description = "方法签名: T selectOne(Wrapper<T> queryWrapper); 入参: LambdaQueryWrapper; 返回值: 单条 User；0 条返回 null，>1 条抛 TooManyResultsException")
    @GetMapping("/select-one")
    public R<User> selectOne(@Parameter(description = "查询条件：name 等于该值(需保证唯一)") @RequestParam String name) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getName, name);

        // ===== 入参打印 =====
        System.out.println("===== BaseMapper.selectOne 入参 =====");
        System.out.println("  wrapper (LambdaQueryWrapper) = " + wrapper);
        System.out.println("  条件: name = '" + name + "'");

        // ===== API 调用 =====
        // 方法签名:    T selectOne(Wrapper<T> queryWrapper)
        // 入参数据结构: wrapper → LambdaQueryWrapper<User>，WHERE 条件
        // 返回值结构:   User → 单条记录；0 条返回 null；>1 条抛 TooManyResultsException
        User found = userMapper.selectOne(wrapper);

        // ===== 出参打印 =====
        System.out.println("===== BaseMapper.selectOne 出参 =====");
        System.out.println("  返回值 User (多条会抛 TooManyResultsException) = " + found);

        return R.ok(found);
    }

    // ==================== 查询 API — 批量/列表 ====================

    @Operation(summary = "根据主键集合批量查询", description = "方法签名: List<T> selectBatchIds(Collection<?> idList); 入参: 主键集合; 返回值: 命中记录列表，不存在的 id 自动忽略")
    @GetMapping("/select-batch-ids")
    public R<List<User>> selectBatchIds(@Parameter(description = "主键 id 集合，多个用逗号分隔") @RequestParam List<Long> ids) {
        // ===== 入参打印 =====
        System.out.println("===== BaseMapper.selectBatchIds 入参 =====");
        System.out.println("  idList (Collection<?>) = " + ids);

        // ===== API 调用 =====
        // 方法签名:    List<T> selectBatchIds(Collection<?> idList)
        // 入参数据结构: idList → Collection<Long> 主键集合
        // 返回值结构:   List<User> → 命中记录列表；不存在的 id 自动忽略，不会报错
        List<User> list = userMapper.selectBatchIds(ids);

        // ===== 出参打印 =====
        System.out.println("===== BaseMapper.selectBatchIds 出参 =====");
        System.out.println("  返回值 List<User>.size() = " + (list != null ? list.size() : "null"));
        if (list != null) {
            list.forEach(u -> System.out.println("    User: id=" + u.getId() + ", name=" + u.getName()));
        }

        return R.ok(list);
    }

    @Operation(summary = "根据 Map 条件查询列表", description = "方法签名: List<T> selectByMap(Map<String,Object> columnMap); 入参: 列名→列值 的 Map(多 key 为 AND); 返回值: 命中记录列表")
    @GetMapping("/select-by-map")
    public R<List<User>> selectByMap(@Parameter(description = "查询条件列值(name)") @RequestParam String name) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("name", name);

        // ===== 入参打印 =====
        System.out.println("===== BaseMapper.selectByMap 入参 =====");
        System.out.println("  columnMap (Map<String,Object>) = " + columnMap);

        // ===== API 调用 =====
        // 方法签名:    List<T> selectByMap(Map<String,Object> columnMap)
        // 入参数据结构: columnMap → Map<列名:String, 列值:Object>，多 key 为 AND
        // 返回值结构:   List<User> → 命中记录列表
        List<User> list = userMapper.selectByMap(columnMap);

        // ===== 出参打印 =====
        System.out.println("===== BaseMapper.selectByMap 出参 =====");
        System.out.println("  返回值 List<User>.size() = " + (list != null ? list.size() : "null"));
        if (list != null) {
            list.forEach(u -> System.out.println("    User: " + u));
        }

        return R.ok(list);
    }

    @Operation(summary = "根据条件查询列表", description = "方法签名: List<T> selectList(Wrapper<T> queryWrapper); 入参: LambdaQueryWrapper(WHERE 条件); 返回值: 命中记录列表")
    @GetMapping("/select-list")
    public R<List<User>> selectList(@Parameter(description = "查询条件：name IN 该集合，多个用逗号分隔") @RequestParam List<String> names) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .in(User::getName, names);

        // ===== 入参打印 =====
        System.out.println("===== BaseMapper.selectList 入参 =====");
        System.out.println("  wrapper (LambdaQueryWrapper) = " + wrapper);
        System.out.println("  条件: name IN " + names);

        // ===== API 调用 =====
        // 方法签名:    List<T> selectList(Wrapper<T> queryWrapper)
        // 入参数据结构: wrapper → LambdaQueryWrapper<User>，WHERE 条件
        // 返回值结构:   List<User> → 命中记录列表
        List<User> list = userMapper.selectList(wrapper);

        // ===== 出参打印 =====
        System.out.println("===== BaseMapper.selectList 出参 =====");
        System.out.println("  返回值 List<User>.size() = " + (list != null ? list.size() : "null"));
        if (list != null) {
            list.forEach(u -> System.out.println("    User: " + u));
        }

        return R.ok(list);
    }

    // ==================== 查询 API — 特殊结果集 ====================

    @Operation(summary = "查询结果为 Map 列表", description = "方法签名: List<Map<String,Object>> selectMaps(Wrapper<T> queryWrapper); 入参: QueryWrapper(可 .select 指定列); 返回值: 每条记录为 Map{列名→列值}")
    @GetMapping("/select-maps")
    public R<List<Map<String, Object>>> selectMaps(@Parameter(description = "查询条件：name 等于该值") @RequestParam String name) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .eq("name", name)
                .select("name", "age");

        // ===== 入参打印 =====
        System.out.println("===== BaseMapper.selectMaps 入参 =====");
        System.out.println("  wrapper (QueryWrapper) = " + wrapper);
        System.out.println("  条件: name = '" + name + "'; 只查询 name, age 两列");

        // ===== API 调用 =====
        // 方法签名:    List<Map<String,Object>> selectMaps(Wrapper<T> queryWrapper)
        // 入参数据结构: wrapper → QueryWrapper<User>，可通过 .select() 指定查询列
        // 返回值结构:   List<Map<String,Object>> → 每条记录为 Map，key = 列名，value = 列值
        List<Map<String, Object>> maps = userMapper.selectMaps(wrapper);

        // ===== 出参打印 =====
        System.out.println("===== BaseMapper.selectMaps 出参 =====");
        System.out.println("  返回值 List<Map>.size() = " + (maps != null ? maps.size() : "null"));
        if (maps != null) {
            maps.forEach(m -> System.out.println("    Map (列名→列值): " + m));
        }

        return R.ok(maps);
    }

    @Operation(summary = "查询第一列值列表", description = "方法签名: List<Object> selectObjs(Wrapper<T> queryWrapper); 入参: QueryWrapper(.select 指定列); 返回值: 仅查询结果第一列的值集合")
    @GetMapping("/select-objs")
    public R<List<Object>> selectObjs(@Parameter(description = "查询条件：name 等于该值") @RequestParam String name) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .eq("name", name)
                .select("name");

        // ===== 入参打印 =====
        System.out.println("===== BaseMapper.selectObjs 入参 =====");
        System.out.println("  wrapper (QueryWrapper) = " + wrapper);
        System.out.println("  条件: name = '" + name + "'; 只取第一列值");

        // ===== API 调用 =====
        // 方法签名:    List<Object> selectObjs(Wrapper<T> queryWrapper)
        // 入参数据结构: wrapper → QueryWrapper<User>，通过 .select() 指定列
        // 返回值结构:   List<Object> → 仅返回查询结果的第一列值集合
        List<Object> objs = userMapper.selectObjs(wrapper);

        // ===== 出参打印 =====
        System.out.println("===== BaseMapper.selectObjs 出参 =====");
        System.out.println("  返回值 List<Object> (仅第一列) = " + objs);

        return R.ok(objs);
    }

    @Operation(summary = "查询记录总数", description = "方法签名: Long selectCount(Wrapper<T> queryWrapper); 入参: LambdaQueryWrapper(WHERE 条件); 返回值: 符合条件的记录总数")
    @GetMapping("/select-count")
    public R<Long> selectCount() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .isNotNull(User::getId);

        // ===== 入参打印 =====
        System.out.println("===== BaseMapper.selectCount 入参 =====");
        System.out.println("  wrapper (LambdaQueryWrapper) = " + wrapper);
        System.out.println("  条件: id IS NOT NULL");

        // ===== API 调用 =====
        // 方法签名:    Long selectCount(Wrapper<T> queryWrapper)
        // 入参数据结构: wrapper → LambdaQueryWrapper<User>，WHERE 条件
        // 返回值结构:   Long → 符合条件的记录总数
        Long count = userMapper.selectCount(wrapper);

        // ===== 出参打印 =====
        System.out.println("===== BaseMapper.selectCount 出参 =====");
        System.out.println("  返回值 Long count = " + count);

        return R.ok(count);
    }

    // ==================== 查询 API — 分页 ====================

    @Operation(summary = "分页查询实体", description = "方法签名: IPage<T> selectPage(IPage<T> page, Wrapper<T> queryWrapper); 入参: Page{current,size} + 条件(null 为全表); 返回值: IPage{records,total,pages,size}")
    @GetMapping("/select-page")
    public R<IPage<User>> selectPage(
            @Parameter(description = "当前页码，从 1 开始") @RequestParam(defaultValue = "1") long current,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "2") long size) {

        Page<User> page = new Page<>(current, size);

        // ===== 入参打印 =====
        System.out.println("===== BaseMapper.selectPage 入参 =====");
        System.out.println("  page (IPage) = " + page);
        System.out.println("  wrapper = null (无条件)");

        // ===== API 调用 =====
        // 方法签名:    IPage<T> selectPage(IPage<T> page, Wrapper<T> queryWrapper)
        // 入参数据结构: page → Page<User> { current:long(当前页), size:long(每页条数) }; wrapper → null(无条件)
        // 返回值结构:   IPage<User> → { records:List<User>(当前页数据), total:long(总记录数), pages:long(总页数), size:long }
        IPage<User> result = userMapper.selectPage(page, null);

        // ===== 出参打印 =====
        System.out.println("===== BaseMapper.selectPage 出参 =====");
        System.out.println("  返回值 IPage<User>.total = " + result.getTotal());
        System.out.println("  返回值 IPage<User>.pages = " + result.getPages());
        System.out.println("  返回值 IPage<User>.size = " + result.getSize());
        System.out.println("  返回值 IPage<User>.records.size() = " + result.getRecords().size());

        return R.ok(result);
    }

    @Operation(summary = "分页查询为 Map", description = "方法签名: IPage<Map<String,Object>> selectMapsPage(IPage<T> page, Wrapper<T> queryWrapper); 入参: Page + QueryWrapper(.select 指定列); 返回值: IPage，records 中每条为 Map{列名→列值}")
    @GetMapping("/select-maps-page")
    public R<IPage<Map<String, Object>>> selectMapsPage(
            @Parameter(description = "当前页码，从 1 开始") @RequestParam(defaultValue = "1") long current,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "2") long size) {

        Page<User> page = new Page<>(current, size);
        QueryWrapper<User> wrapper = new QueryWrapper<User>().select("name", "age");

        // ===== 入参打印 =====
        System.out.println("===== BaseMapper.selectMapsPage 入参 =====");
        System.out.println("  page (IPage) = " + page);
        System.out.println("  wrapper (QueryWrapper) = " + wrapper);
        System.out.println("  只查询 name, age 两列");

        // ===== API 调用 =====
        // 方法签名:    IPage<Map<String,Object>> selectMapsPage(IPage<T> page, Wrapper<T> queryWrapper)
        // 入参数据结构: page → Page<User>; wrapper → QueryWrapper<User>，通过 .select() 指定列
        // 返回值结构:   IPage<Map<String,Object>> → records 中每条记录为 Map { 列名 → 列值 }
        IPage<Map<String, Object>> result = userMapper.selectMapsPage(page, wrapper);

        // ===== 出参打印 =====
        System.out.println("===== BaseMapper.selectMapsPage 出参 =====");
        System.out.println("  返回值 IPage<Map>.total = " + result.getTotal());
        System.out.println("  返回值 IPage<Map>.size = " + result.getSize());
        System.out.println("  返回值 IPage<Map>.records = " + result.getRecords());

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
