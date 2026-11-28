package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.User;
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
class IServiceTest {

    @Autowired
    private UserService userService;

    private static Long savedId;

    // ==================== save 系列 ====================

    @Test
    @Order(1)
    @DisplayName("save(T entity) — 插入单条")
    void testSave() {
        User user = buildUser("IService-save", 20, "is-save@test.com");
        boolean ok = userService.save(user);
        // boolean ok: 是否保存成功，成功返回 true
        System.out.println("save 返回值 ok = " + ok);
        System.out.println("save 后主键回填 user.getId() = " + user.getId());
        assertTrue(ok);
        assertNotNull(user.getId());
        savedId = user.getId();
    }

    @Test
    @Order(2)
    @DisplayName("saveBatch(Collection<T> entityList) — 批量插入（默认批次大小）")
    void testSaveBatch() {
        List<User> users = List.of(
                buildUser("is-batch-1", 21, "isB1@test.com"),
                buildUser("is-batch-2", 22, "isB2@test.com")
        );
        boolean ok = userService.saveBatch(users);
        // boolean ok: 批量插入是否全部成功，成功返回 true
        System.out.println("saveBatch(Collection) 返回值 ok = " + ok);
        assertTrue(ok);
    }

    @Test
    @Order(3)
    @DisplayName("saveBatch(Collection<T> entityList, int batchSize) — 指定批次大小")
    void testSaveBatchWithSize() {
        List<User> users = List.of(
                buildUser("is-batchSize-1", 23, "isBS1@test.com"),
                buildUser("is-batchSize-2", 24, "isBS2@test.com")
        );
        boolean ok = userService.saveBatch(users, 1);   // 每批 1 条
        // boolean ok: 指定批次大小的批量插入，是否全部成功
        System.out.println("saveBatch(Collection, batchSize) 返回值 ok = " + ok);
        assertTrue(ok);
    }

    // ==================== saveOrUpdate 系列 ====================

    @Test
    @Order(4)
    @DisplayName("saveOrUpdate(T entity) — 主键存在则更新，否则插入")
    void testSaveOrUpdate() {
        // 先 save 一条，确保主键存在
        User user = buildUser("saveOrUpdate", 25, "sou@test.com");
        userService.save(user);

        // 修改后 saveOrUpdate → 应执行 UPDATE
        user.setAge(99);
        boolean ok = userService.saveOrUpdate(user);
        // boolean ok: 主键存在时执行 UPDATE，返回是否更新成功
        System.out.println("saveOrUpdate(更新) 返回值 ok = " + ok);
        assertTrue(ok);
        assertEquals(99, userService.getById(user.getId()).getAge());

        // 不带主键的新实体 → 应执行 INSERT
        User newUser = buildUser("saveOrUpdate-new", 26, "souN@test.com");
        boolean insertOk = userService.saveOrUpdate(newUser);
        // boolean insertOk: 主键不存在时执行 INSERT，返回是否插入成功
        System.out.println("saveOrUpdate(插入) 返回值 insertOk = " + insertOk);
        System.out.println("saveOrUpdate(插入) 后 newUser.getId() = " + newUser.getId());
        assertTrue(insertOk);
        assertNotNull(newUser.getId());
    }

    @Test
    @Order(5)
    @DisplayName("saveOrUpdateBatch(Collection<T> entityList) — 批量插入或更新")
    void testSaveOrUpdateBatch() {
        User exists = buildUser("souBatch-exists", 27, "souBE@test.com");
        userService.save(exists);

        User notExists = buildUser("souBatch-new", 28, "souBN@test.com");

        exists.setAge(50);
        boolean ok = userService.saveOrUpdateBatch(List.of(exists, notExists));
        // boolean ok: 批量保存或更新是否全部成功
        System.out.println("saveOrUpdateBatch 返回值 ok = " + ok);
        assertTrue(ok);
    }

    // ==================== remove 系列 ====================

    @Test
    @Order(10)
    @DisplayName("removeById(Serializable id) — 按主键删除")
    void testRemoveById() {
        User user = buildUser("removeById", 30, "remById@test.com");
        userService.save(user);
        boolean ok = userService.removeById(user.getId());
        // boolean ok: 按主键删除是否成功
        System.out.println("removeById 返回值 ok = " + ok);
        assertTrue(ok);
    }

    @Test
    @Order(11)
    @DisplayName("removeByMap(Map<String,Object> columnMap) — 按 Map 条件删除")
    void testRemoveByMap() {
        userService.save(buildUser("removeByMap", 31, "remMap@test.com"));

        Map<String, Object> map = new HashMap<>();
        map.put("name", "removeByMap");
        boolean ok = userService.removeByMap(map);
        // boolean ok: 按 Map 条件删除是否成功（至少删除 1 条记录即为 true）
        System.out.println("removeByMap 返回值 ok = " + ok);
        assertTrue(ok);
    }

    @Test
    @Order(12)
    @DisplayName("remove(Wrapper<T> queryWrapper) — 按条件删除")
    void testRemoveByWrapper() {
        userService.save(buildUser("removeWrapper", 32, "remW@test.com"));

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getName, "removeWrapper");
        boolean ok = userService.remove(wrapper);
        // boolean ok: 按 Wrapper 条件删除是否成功
        System.out.println("remove(Wrapper) 返回值 ok = " + ok);
        assertTrue(ok);
    }

    @Test
    @Order(13)
    @DisplayName("removeByIds(Collection<?> idList) — 按主键批量删除")
    void testRemoveByIds() {
        User u1 = buildUser("remIds-1", 33, "remI1@test.com");
        User u2 = buildUser("remIds-2", 34, "remI2@test.com");
        userService.saveBatch(List.of(u1, u2));

        boolean ok = userService.removeByIds(List.of(u1.getId(), u2.getId()));
        // boolean ok: 按主键集合批量删除是否全部成功
        System.out.println("removeByIds 返回值 ok = " + ok);
        assertTrue(ok);
    }

    @Test
    @Order(14)
    @DisplayName("removeBatchByIds(Collection<?> list) — JDBC 批量删除（3.5.0+）")
    void testRemoveBatchByIds() {
        User u1 = buildUser("remBatch-1", 35, "remB1@test.com");
        User u2 = buildUser("remBatch-2", 36, "remB2@test.com");
        userService.saveBatch(List.of(u1, u2));

        boolean ok = userService.removeBatchByIds(List.of(u1.getId(), u2.getId()));
        // boolean ok: JDBC 批量删除（3.5.0+），返回是否全部成功
        System.out.println("removeBatchByIds 返回值 ok = " + ok);
        assertTrue(ok);
    }

    // ==================== update 系列 ====================

    @Test
    @Order(20)
    @DisplayName("updateById(T entity) — 按主键更新")
    void testUpdateById() {
        User user = buildUser("is-updateById", 25, "isUpdById@test.com");
        userService.save(user);

        user.setName("is-updateById-new");
        boolean ok = userService.updateById(user);
        // boolean ok: 按主键更新是否成功
        System.out.println("updateById 返回值 ok = " + ok);
        System.out.println("updateById 后再查询 name = " + userService.getById(user.getId()).getName());
        assertTrue(ok);
        assertEquals("is-updateById-new", userService.getById(user.getId()).getName());
    }

    @Test
    @Order(21)
    @DisplayName("update(T entity, Wrapper<T> updateWrapper) — 条件更新")
    void testUpdate() {
        User user = buildUser("is-updateWrapper", 26, "isUpdW@test.com");
        userService.save(user);

        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<User>()
                .eq(User::getName, "is-updateWrapper");
        User updateEntity = new User();
        updateEntity.setAge(88);

        boolean ok = userService.update(updateEntity, wrapper);
        // boolean ok: 按 Wrapper 条件更新是否成功
        System.out.println("update(entity, wrapper) 返回值 ok = " + ok);
        System.out.println("update 后再查询 age = " + userService.getById(user.getId()).getAge());
        assertTrue(ok);
        assertEquals(88, userService.getById(user.getId()).getAge());
    }

    @Test
    @Order(22)
    @DisplayName("updateBatchById(Collection<T> entityList) — 批量按主键更新")
    void testUpdateBatchById() {
        User u1 = buildUser("updBatch-1", 40, "updB1@test.com");
        User u2 = buildUser("updBatch-2", 41, "updB2@test.com");
        userService.saveBatch(List.of(u1, u2));

        u1.setAge(100);
        u2.setAge(101);
        boolean ok = userService.updateBatchById(List.of(u1, u2));
        // boolean ok: 批量按主键更新是否全部成功
        System.out.println("updateBatchById 返回值 ok = " + ok);
        assertTrue(ok);
    }

    // ==================== get 系列 — 查询单条 ====================

    @Test
    @Order(30)
    @DisplayName("getById(Serializable id) — 按主键查询")
    void testGetById() {
        User user = buildUser("is-getById", 27, "isGetById@test.com");
        userService.save(user);

        User found = userService.getById(user.getId());
        // User found: 查询到的实体对象，未找到则返回 null
        System.out.println("getById 返回值 found = " + found);
        System.out.println("getById found.getName() = " + (found != null ? found.getName() : "null"));
        assertNotNull(found);
        assertEquals("is-getById", found.getName());
    }

    @Test
    @Order(31)
    @DisplayName("getOne(Wrapper<T> queryWrapper) — 条件查询一条，多条抛异常")
    void testGetOne() {
        userService.save(buildUser("is-getOne", 28, "isGetOne@test.com"));

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getName, "is-getOne");
        User found = userService.getOne(wrapper);
        // User found: 条件查询到的单条实体，未找到返回 null，多条则抛 TooManyResultsException
        System.out.println("getOne 返回值 found = " + found);
        assertNotNull(found);
    }

    @Test
    @Order(32)
    @DisplayName("getOne(Wrapper<T> queryWrapper, boolean throwEx) — 多条时不抛异常")
    void testGetOneWithoutThrow() {
        userService.save(buildUser("dupName", 29, "dup1@test.com"));
        userService.save(buildUser("dupName", 30, "dup2@test.com"));

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getName, "dupName");
        // throwEx = false，多条时取第一条，不抛异常
        User found = userService.getOne(wrapper, false);
        // User found: throwEx=false 时，多条结果取第一条，不会抛异常
        System.out.println("getOne(wrapper, false) 返回值 found = " + found);
        assertNotNull(found);
    }

    @Test
    @Order(33)
    @DisplayName("getMap(Wrapper<T> queryWrapper) — 返回 Map 单条")
    void testGetMap() {
        userService.save(buildUser("is-getMap", 31, "isGetMap@test.com"));

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getName, "is-getMap")
                .select(User::getName, User::getAge);
        Map<String, Object> map = userService.getMap(wrapper);
        // Map<String,Object>: 条件查询结果以 Map 形式返回单条记录，key 为列名
        System.out.println("getMap 返回值 map = " + map);
        assertNotNull(map);
        assertEquals("is-getMap", map.get("name"));
    }

    @Test
    @Order(34)
    @DisplayName("getObj(Wrapper<T> queryWrapper, Function) — 返回单个字段值")
    void testGetObj() {
        userService.save(buildUser("is-getObj", 32, "isGetObj@test.com"));

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getName, "is-getObj")
                .select(User::getName);
        Object name = userService.getObj(wrapper, Object::toString);
        // Object name: 查询结果的第一列值，经 Function 转换后返回
        System.out.println("getObj 返回值 name = " + name);
        assertEquals("is-getObj", name);
    }

    // ==================== list / count / exists ====================

    @Test
    @Order(35)
    @DisplayName("list() — 查询全部")
    void testList() {
        List<User> list = userService.list();
        // List<User>: 查询全部记录返回的实体列表
        System.out.println("list() 返回值 list.size() = " + list.size());
        assertNotNull(list);
    }

    @Test
    @Order(36)
    @DisplayName("list(Wrapper<T> queryWrapper) — 条件查询列表")
    void testListByWrapper() {
        userService.save(buildUser("is-listA", 33, "isListA@test.com"));
        userService.save(buildUser("is-listB", 34, "isListB@test.com"));

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .in(User::getName, List.of("is-listA", "is-listB"));
        List<User> list = userService.list(wrapper);
        // List<User>: 按 Wrapper 条件查询到的实体列表
        System.out.println("list(wrapper) 返回值 list.size() = " + list.size());
        list.forEach(u -> System.out.println("  User: id=" + u.getId() + ", name=" + u.getName()));
        assertEquals(2, list.size());
    }

    @Test
    @Order(37)
    @DisplayName("listByIds(Collection<?> idList) — 按主键批量查询")
    void testListByIds() {
        User u1 = buildUser("is-listIds-1", 35, "isLI1@test.com");
        User u2 = buildUser("is-listIds-2", 36, "isLI2@test.com");
        userService.saveBatch(List.of(u1, u2));

        List<User> list = userService.listByIds(List.of(u1.getId(), u2.getId()));
        // List<User>: 按主键集合批量查询到的实体列表
        System.out.println("listByIds 返回值 list.size() = " + list.size());
        list.forEach(u -> System.out.println("  User: " + u));
        assertEquals(2, list.size());
    }

    @Test
    @Order(38)
    @DisplayName("listByMap(Map<String,Object> columnMap) — 按 Map 条件查询")
    void testListByMap() {
        userService.save(buildUser("is-listByMap", 37, "isLBM@test.com"));

        Map<String, Object> map = new HashMap<>();
        map.put("name", "is-listByMap");
        List<User> list = userService.listByMap(map);
        // List<User>: 按 Map 条件查询到的实体列表
        System.out.println("listByMap 返回值 list.size() = " + list.size());
        list.forEach(u -> System.out.println("  User: " + u));
        assertEquals(1, list.size());
    }

    @Test
    @Order(39)
    @DisplayName("count(Wrapper<T> queryWrapper) — 条件计数")
    void testCount() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .isNotNull(User::getId);
        long count = userService.count(wrapper);
        // long count: 按条件统计的记录总数
        System.out.println("count(wrapper) 返回值 count = " + count);
        assertTrue(count >= 0);
    }

    @Test
    @Order(40)
    @DisplayName("exists(Wrapper<T> queryWrapper) — 判断是否存在")
    void testExists() {
        userService.save(buildUser("is-exists", 38, "isExists@test.com"));

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getName, "is-exists");
        boolean exists = userService.exists(wrapper);
        // boolean exists: 按条件判断记录是否存在，存在返回 true
        System.out.println("exists(wrapper) 返回值 exists = " + exists);
        assertTrue(exists);
    }

    // ==================== 分页 ====================

    @Test
    @Order(50)
    @DisplayName("page(IPage<T> page) — 无条件分页")
    void testPage() {
        Page<User> page = new Page<>(1, 2);
        IPage<User> result = userService.page(page);
        // IPage<User>: 无条件分页结果，包含 records、total、pages 等信息
        System.out.println("page() 返回值 result.getSize() = " + result.getSize());
        System.out.println("page() 返回值 result.getTotal() = " + result.getTotal());
        System.out.println("page() 返回值 result.getRecords().size() = " + result.getRecords().size());
        assertNotNull(result);
        assertTrue(result.getSize() <= 2);
    }

    @Test
    @Order(51)
    @DisplayName("page(IPage<T> page, Wrapper<T> queryWrapper) — 条件分页")
    void testPageByWrapper() {
        Page<User> page = new Page<>(1, 5);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .ge(User::getAge, 0);
        IPage<User> result = userService.page(page, wrapper);
        // IPage<User>: 条件分页结果，按 Wrapper 条件筛选后分页
        System.out.println("page(wrapper) 返回值 result.getTotal() = " + result.getTotal());
        System.out.println("page(wrapper) 返回值 result.getRecords().size() = " + result.getRecords().size());
        assertNotNull(result);
        assertTrue(result.getRecords().stream().allMatch(u -> u.getAge() >= 0));
    }

    @Test
    @Order(52)
    @DisplayName("page(IPage<T> page, Wrapper<T> queryWrapper) — 3.5.7+ 支持 Wrapper 重载")
    void testPageWithWrapper() {
        Page<User> page = new Page<>(1, 3);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .isNotNull(User::getName);
        IPage<User> result = userService.page(page, wrapper);
        // IPage<User>: 3.5.7+ 重载，条件分页查询
        System.out.println("page(wrapper 重载) 返回值 result.getRecords().size() = " + result.getRecords().size());
        System.out.println("page(wrapper 重载) 返回值 result.getTotal() = " + result.getTotal());
        assertNotNull(result);
        assertTrue(result.getRecords().stream().allMatch(u -> u.getName() != null));
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
