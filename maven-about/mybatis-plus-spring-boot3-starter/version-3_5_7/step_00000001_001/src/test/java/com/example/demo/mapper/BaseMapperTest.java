package com.example.demo.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.User;
import org.apache.ibatis.executor.BatchResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BaseMapperTest {

    @Autowired
    private UserMapper userMapper;

    private static Long savedId;

    // ==================== 插入 ====================

    @Test
    @Order(1)
    @DisplayName("insert(T entity) — 插入单条记录")
    void testInsert() {
        User user = new User();
        user.setName("BaseMapper-insert");
        user.setAge(20);
        user.setEmail("insert@test.com");

        int rows = userMapper.insert(user);
        // int rows: 受影响行数，插入成功返回 1
        System.out.println("insert 返回值 rows = " + rows);
        System.out.println("insert 后主键回填 user.getId() = " + user.getId());
        assertEquals(1, rows);
        assertNotNull(user.getId(), "自增主键应回填到实体");
        savedId = user.getId();
    }

    @Test
    @Order(2)
    @DisplayName("insert(Collection<T> entityList) — 3.5.7+ 批量插入")
    void testInsertBatch() {
        List<User> users = List.of(
                buildUser("batch-1", 21, "batch1@test.com"),
                buildUser("batch-2", 22, "batch2@test.com")
        );
        List<BatchResult> rows = userMapper.insert(users);
        // List<BatchResult>: 批量执行后的结果列表，每个 BatchResult 包含该批次受影响行数
        System.out.println("insert(Collection) 返回值 rows.size() = " + rows.size());
        rows.forEach(r -> System.out.println("  BatchResult updateCounts = " + r.getUpdateCounts()));
        assertEquals(2, rows);
    }

    // ==================== 删除 ====================

    @Test
    @Order(10)
    @DisplayName("deleteById(Serializable id) — 按主键删除")
    void testDeleteById() {
        User user = buildUser("deleteById", 30, "deleteById@test.com");
        userMapper.insert(user);
        int rows = userMapper.deleteById(user.getId());
        // int rows: 受影响行数，删除成功返回 1
        System.out.println("deleteById 返回值 rows = " + rows);
        assertEquals(1, rows);
    }

    @Test
    @Order(11)
    @DisplayName("deleteByMap(Map<String,Object> columnMap) — 按 Map 条件删除")
    void testDeleteByMap() {
        User user = buildUser("deleteByMap", 31, "deleteByMap@test.com");
        userMapper.insert(user);

        Map<String, Object> map = new HashMap<>();
        map.put("name", "deleteByMap");
        int rows = userMapper.deleteByMap(map);
        // int rows: 受影响行数，按 Map 条件删除，成功返回匹配的记录数
        System.out.println("deleteByMap 返回值 rows = " + rows);
        assertEquals(1, rows);
    }

    @Test
    @Order(12)
    @DisplayName("delete(Wrapper<T> wrapper) — 按条件删除")
    void testDeleteByWrapper() {
        userMapper.insert(buildUser("deleteWrapper", 32, "delWrapper@test.com"));
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getName, "deleteWrapper");
        int rows = userMapper.delete(wrapper);
        // int rows: 受影响行数，按 Wrapper 条件删除
        System.out.println("delete(Wrapper) 返回值 rows = " + rows);
        assertEquals(1, rows);
    }

    @Test
    @Order(13)
    @DisplayName("deleteBatchIds(Collection<?> idList) — 按主键批量删除")
    void testDeleteBatchIds() {
        User u1 = buildUser("delBatch-1", 33, "delB1@test.com");
        User u2 = buildUser("delBatch-2", 34, "delB2@test.com");
        userMapper.insert(u1);
        userMapper.insert(u2);

        int rows = userMapper.deleteBatchIds(List.of(u1.getId(), u2.getId()));
        // int rows: 受影响行数，批量按主键删除，返回删除的总条数
        System.out.println("deleteBatchIds 返回值 rows = " + rows);
        assertEquals(2, rows);
    }

    // ==================== 更新 ====================

    @Test
    @Order(20)
    @DisplayName("updateById(T entity) — 按主键更新")
    void testUpdateById() {
        User user = buildUser("updateById", 25, "updateById@test.com");
        userMapper.insert(user);

        user.setName("updateById-new");
        int rows = userMapper.updateById(user);
        // int rows: 受影响行数，按主键更新，成功返回 1
        System.out.println("updateById 返回值 rows = " + rows);
        System.out.println("updateById 后再查询 name = " + userMapper.selectById(user.getId()).getName());
        assertEquals(1, rows);
        assertEquals("updateById-new", userMapper.selectById(user.getId()).getName());
    }

    @Test
    @Order(21)
    @DisplayName("update(T entity, Wrapper<T> wrapper) — 按条件更新")
    void testUpdateByWrapper() {
        User user = buildUser("updateWrapper", 26, "updWrapper@test.com");
        userMapper.insert(user);

        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .eq("name", "updateWrapper");
        User updateEntity = new User();
        updateEntity.setAge(99);

        int rows = userMapper.update(updateEntity, wrapper);
        // int rows: 受影响行数，按 Wrapper 条件更新，返回匹配的记录数
        System.out.println("update(entity, wrapper) 返回值 rows = " + rows);
        System.out.println("update 后再查询 age = " + userMapper.selectById(user.getId()).getAge());
        assertEquals(1, rows);
        assertEquals(99, userMapper.selectById(user.getId()).getAge());
    }

    // ==================== 查询 — 单条 ====================

    @Test
    @Order(30)
    @DisplayName("selectById(Serializable id) — 按主键查询")
    void testSelectById() {
        User user = buildUser("selectById", 27, "selById@test.com");
        userMapper.insert(user);

        User found = userMapper.selectById(user.getId());
        // User found: 查询到的实体对象，未找到则返回 null
        System.out.println("selectById 返回值 found = " + found);
        System.out.println("selectById found.getName() = " + (found != null ? found.getName() : "null"));
        assertNotNull(found);
        assertEquals("selectById", found.getName());
    }

    @Test
    @Order(31)
    @DisplayName("selectOne(Wrapper<T> wrapper) — 条件查询单条")
    void testSelectOne() {
        userMapper.insert(buildUser("selectOne", 28, "selOne@test.com"));

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getName, "selectOne");
        User found = userMapper.selectOne(wrapper);
        // User found: 按条件查询到的单条实体，未找到返回 null，多条则抛 TooManyResultsException
        System.out.println("selectOne 返回值 found = " + found);
        assertNotNull(found);
        assertEquals("selectOne", found.getName());
    }

    // ==================== 查询 — 批量/列表 ====================

    @Test
    @Order(32)
    @DisplayName("selectBatchIds(Collection<?> idList) — 按主键批量查询")
    void testSelectBatchIds() {
        User u1 = buildUser("selBatch-1", 29, "selB1@test.com");
        User u2 = buildUser("selBatch-2", 30, "selB2@test.com");
        userMapper.insert(u1);
        userMapper.insert(u2);

        List<User> list = userMapper.selectBatchIds(List.of(u1.getId(), u2.getId()));
        // List<User>: 批量查询到的实体列表，ID 不存在则忽略
        System.out.println("selectBatchIds 返回值 list.size() = " + list.size());
        list.forEach(u -> System.out.println("  User: id=" + u.getId() + ", name=" + u.getName()));
        assertEquals(2, list.size());
    }

    @Test
    @Order(33)
    @DisplayName("selectByMap(Map<String,Object> columnMap) — 按 Map 条件查询")
    void testSelectByMap() {
        userMapper.insert(buildUser("selectByMap", 31, "selByMap@test.com"));

        Map<String, Object> map = new HashMap<>();
        map.put("name", "selectByMap");
        List<User> list = userMapper.selectByMap(map);
        // List<User>: 按 Map 条件查询到的实体列表
        System.out.println("selectByMap 返回值 list.size() = " + list.size());
        list.forEach(u -> System.out.println("  User: " + u));
        assertEquals(1, list.size());
    }

    @Test
    @Order(34)
    @DisplayName("selectList(Wrapper<T> wrapper) — 条件查询列表")
    void testSelectList() {
        userMapper.insert(buildUser("listA", 32, "listA@test.com"));
        userMapper.insert(buildUser("listB", 33, "listB@test.com"));

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .in(User::getName, List.of("listA", "listB"));
        List<User> list = userMapper.selectList(wrapper);
        // List<User>: 按 Wrapper 条件查询到的实体列表
        System.out.println("selectList 返回值 list.size() = " + list.size());
        list.forEach(u -> System.out.println("  User: " + u));
        assertEquals(2, list.size());
    }

    // ==================== 查询 — 特殊结果集 ====================

    @Test
    @Order(35)
    @DisplayName("selectMaps(Wrapper<T> wrapper) — 返回 Map 列表")
    void testSelectMaps() {
        userMapper.insert(buildUser("mapResult", 34, "mapResult@test.com"));

        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .eq("name", "mapResult")
                .select("name", "age");
        List<Map<String, Object>> maps = userMapper.selectMaps(wrapper);
        // List<Map<String,Object>>: 查询结果以 Map 形式返回，key 为列名，value 为列值
        System.out.println("selectMaps 返回值 maps.size() = " + maps.size());
        maps.forEach(m -> System.out.println("  Map: " + m));
        assertEquals(1, maps.size());
        assertTrue(maps.get(0).containsKey("name"));
    }

    @Test
    @Order(36)
    @DisplayName("selectObjs(Wrapper<T> wrapper) — 只返回第一列的值")
    void testSelectObjs() {
        userMapper.insert(buildUser("objsResult", 35, "objsResult@test.com"));

        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .eq("name", "objsResult")
                .select("name");
        List<Object> objs = userMapper.selectObjs(wrapper);
        // List<Object>: 只返回查询结果的第一列值，常用于聚合或单列查询
        System.out.println("selectObjs 返回值 objs = " + objs);
        assertEquals(1, objs.size());
        assertEquals("objsResult", objs.get(0));
    }

    @Test
    @Order(37)
    @DisplayName("selectCount(Wrapper<T> wrapper) — 条件计数")
    void testSelectCount() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .isNotNull(User::getId);
        Long count = userMapper.selectCount(wrapper);
        // Long count: 按条件统计的记录总数
        System.out.println("selectCount 返回值 count = " + count);
        assertTrue(count >= 0);
    }

    // ==================== 分页 ====================

    @Test
    @Order(40)
    @DisplayName("selectPage(IPage<T> page, Wrapper<T> wrapper) — 分页查询实体")
    void testSelectPage() {
        Page<User> page = new Page<>(1, 2);
        IPage<User> result = userMapper.selectPage(page, null);
        // IPage<User>: 分页结果对象，包含 records(当前页数据)、total(总记录数)、pages(总页数) 等
        System.out.println("selectPage 返回值 result.getSize() = " + result.getSize());
        System.out.println("selectPage 返回值 result.getTotal() = " + result.getTotal());
        System.out.println("selectPage 返回值 result.getPages() = " + result.getPages());
        System.out.println("selectPage 返回值 result.getRecords().size() = " + result.getRecords().size());
        assertNotNull(result);
        assertTrue(result.getSize() <= 2);
        assertTrue(result.getTotal() >= 0);
    }

    @Test
    @Order(41)
    @DisplayName("selectMapsPage(IPage<T> page, Wrapper<T> wrapper) — 分页查询 Map")
    void testSelectMapsPage() {
        Page<User> page = new Page<>(1, 2);
        QueryWrapper<User> wrapper = new QueryWrapper<User>().select("name", "age");
        IPage<Map<String, Object>> result = userMapper.selectMapsPage(page, wrapper);
        // IPage<Map<String,Object>>: 分页查询结果，每条记录以 Map 形式返回
        System.out.println("selectMapsPage 返回值 result.getSize() = " + result.getSize());
        System.out.println("selectMapsPage 返回值 result.getTotal() = " + result.getTotal());
        System.out.println("selectMapsPage 返回值 records = " + result.getRecords());
        assertNotNull(result);
        assertTrue(result.getSize() <= 2);
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
