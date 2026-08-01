package com.rc.system.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rc.model.system.SysRole;
import com.rc.system.mapper.SysRoleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class SysRoleMapperTest {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    // ==================== 插入操作 ====================

    // 1. 添加操作
    @Test
    public void add() {
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("测试角色2");
        sysRole.setRoleCode("testManager2");
        sysRole.setDescription("测试角色2");
        int rows = sysRoleMapper.insert(sysRole);
        System.out.println("插入行数: " + rows);
        System.out.println("生成ID: " + sysRole.getId());
    }

    // 2. 批量插入（需要配置）
    @Test
    public void addBatch() {
        SysRole role1 = new SysRole();
        role1.setRoleName("批量角色1");
        role1.setRoleCode("batchRole1");
        role1.setDescription("批量角色1");

        SysRole role2 = new SysRole();
        role2.setRoleName("批量角色2");
        role2.setRoleCode("batchRole2");
        role2.setDescription("批量角色2");

        sysRoleMapper.insert(role1);
        sysRoleMapper.insert(role2);
        System.out.println("批量插入完成");
    }

    // ==================== 删除操作 ====================

    // 3. id删除
    @Test
    public void deleteId() {
        int rows = sysRoleMapper.deleteById("13");
        System.out.println("删除行数: " + rows);
    }

    // 4. 批量删除
    @Test
    public void testBatchDelete() {
        int rows = sysRoleMapper.deleteBatchIds(Arrays.asList("11", "14"));
        System.out.println("批量删除行数: " + rows);
    }

    // 5. 条件删除
    @Test
    public void testDelete() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_name", "用户管理员");
        int rows = sysRoleMapper.delete(wrapper);
        System.out.println("条件删除行数: " + rows);
    }

    // ==================== 修改操作 ====================

    // 6. 修改操作（先查后改）
    @Test
    public void update() {
        SysRole sysRole = sysRoleMapper.selectById("1");
        sysRole.setDescription("系统管理员尚硅谷");
        int rows = sysRoleMapper.updateById(sysRole);
        System.out.println("修改行数: " + rows);
    }

    // 7. 条件修改（UpdateWrapper）
    @Test
    public void updateByWrapper() {
        UpdateWrapper<SysRole> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("role_code", "testManager")
                     .set("description", "通过条件修改");
        int rows = sysRoleMapper.update(null, updateWrapper);
        System.out.println("条件修改行数: " + rows);
    }

    // 8. Lambda条件修改
    @Test
    public void updateByLambda() {
        UpdateWrapper<SysRole> updateWrapper = new UpdateWrapper<>();

        // 原因是 Lombok 的 @Data 注解在测试模块中未被正确识别，导致 getRoleCode()、getDescription() 方法不存在。改为字符串方式更稳定："role_code" 和 "description"。
        // updateWrapper.eq(SysRole::getRoleCode, "testManager")
        //              .set(SysRole::getDescription, "Lambda条件修改");
        updateWrapper.eq("role_code", "testManager")
                     .set("description", "Lambda条件修改");

        int rows = sysRoleMapper.update(null, updateWrapper);
        System.out.println("Lambda修改行数: " + rows);
    }

    // ==================== 查询操作 ====================

    // 9. 查询所有
    @Test
    public void findAll() {
        List<SysRole> list = sysRoleMapper.selectList(null);
        for (SysRole sysRole : list) {
            System.out.println(sysRole);
        }
    }

    // 10. 条件查询
    @Test
    public void testSelect() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.like("role_name", "管理员");
        List<SysRole> list = sysRoleMapper.selectList(wrapper);
        System.out.println(list);
    }

    // 11. 根据id查询
    @Test
    public void selectById() {
        SysRole sysRole = sysRoleMapper.selectById("1");
        System.out.println("查询结果: " + sysRole);
    }

    // 12. 批量id查询
    @Test
    public void selectBatchIds() {
        List<SysRole> list = sysRoleMapper.selectBatchIds(Arrays.asList("1", "2", "3"));
        System.out.println("批量查询结果: " + list);
    }

    // 13. 查询单条记录
    @Test
    public void selectOne() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_code", "testManager");
        SysRole sysRole = sysRoleMapper.selectOne(wrapper);
        System.out.println("单条查询: " + sysRole);
    }

    // 14. 查询数量
    @Test
    public void selectCount() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.like("role_name", "管理员");
        Long count = Long.valueOf(sysRoleMapper.selectCount(wrapper));
        System.out.println("数量: " + count);
    }

    // 15. 查询所有记录数
    @Test
    public void selectTotalCount() {
        Long count = Long.valueOf(sysRoleMapper.selectCount(null));
        System.out.println("总记录数: " + count);
    }

    // 16. 查询Map结果
    @Test
    public void selectMaps() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.select("id", "role_name", "role_code");
        List<Map<String, Object>> maps = sysRoleMapper.selectMaps(wrapper);
        System.out.println("Map查询结果: " + maps);
    }

    // 17. 查询单列结果
    @Test
    public void selectObjs() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.select("role_name");
        List<Object> objects = sysRoleMapper.selectObjs(wrapper);
        System.out.println("单列查询结果: " + objects);
    }

    // 18. 分页查询
    @Test
    public void selectPage() {
        Page<SysRole> page = new Page<>(1, 5); // 当前页，每页大小
        IPage<SysRole> rolePage = sysRoleMapper.selectPage(page, null);
        System.out.println("当前页: " + rolePage.getCurrent());
        System.out.println("每页大小: " + rolePage.getSize());
        System.out.println("总记录数: " + rolePage.getTotal());
        System.out.println("总页数: " + rolePage.getPages());
        System.out.println("当前页数据: " + rolePage.getRecords());
    }

    // 19. 条件分页查询
    @Test
    public void selectPageWithWrapper() {
        Page<SysRole> page = new Page<>(1, 5);
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.like("role_name", "角色");
        IPage<SysRole> rolePage = sysRoleMapper.selectPage(page, wrapper);
        System.out.println("分页查询结果: " + rolePage.getRecords());
    }

    // ==================== 条件构造器高级用法 ====================

    // 20. eq - 等于条件
    @Test
    public void testEq() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_name", "系统管理员");
        List<SysRole> list = sysRoleMapper.selectList(wrapper);
        System.out.println("eq查询: " + list);
    }

    // 21. ne - 不等于条件
    @Test
    public void testNe() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.ne("role_code", "testManager");
        List<SysRole> list = sysRoleMapper.selectList(wrapper);
        System.out.println("ne查询: " + list);
    }

    // 22. likeRight - 右模糊查询（以xxx开头）
    @Test
    public void testLikeRight() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.likeRight("role_name", "系统");
        List<SysRole> list = sysRoleMapper.selectList(wrapper);
        System.out.println("右模糊查询: " + list);
    }

    // 23. likeLeft - 左模糊查询（以xxx结尾）
    @Test
    public void testLikeLeft() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.likeLeft("role_name", "管理员");
        List<SysRole> list = sysRoleMapper.selectList(wrapper);
        System.out.println("左模糊查询: " + list);
    }

    // 24. between - 范围查询
    @Test
    public void testBetween() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.between("id", "1", "5");
        List<SysRole> list = sysRoleMapper.selectList(wrapper);
        System.out.println("范围查询: " + list);
    }

    // 25. in - IN查询
    @Test
    public void testIn() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.in("id", Arrays.asList("1", "2", "3", "4", "5"));
        List<SysRole> list = sysRoleMapper.selectList(wrapper);
        System.out.println("IN查询: " + list);
    }

    // 26. orderByDesc - 降序排序
    @Test
    public void testOrderByDesc() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        List<SysRole> list = sysRoleMapper.selectList(wrapper);
        System.out.println("降序排序: " + list);
    }

    // 27. orderByAsc - 升序排序
    @Test
    public void testOrderByAsc() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("role_name");
        List<SysRole> list = sysRoleMapper.selectList(wrapper);
        System.out.println("升序排序: " + list);
    }

    // 28. last - 追加SQL
    @Test
    public void testLast() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.last("LIMIT 3");
        List<SysRole> list = sysRoleMapper.selectList(wrapper);
        System.out.println("last查询(限制3条): " + list);
    }

    // 29. select - 指定查询字段
    @Test
    public void testSelectFields() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.select("id", "role_name", "description");
        List<SysRole> list = sysRoleMapper.selectList(wrapper);
        System.out.println("指定字段查询: " + list);
    }

    // 30. 组合条件查询
    @Test
    public void testComplexQuery() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_code", "testManager")
               .or()
               .like("role_name", "管理员")
               .orderByDesc("id");
        List<SysRole> list = sysRoleMapper.selectList(wrapper);
        System.out.println("组合条件查询: " + list);
    }

    // 31. 多条件AND查询
    @Test
    public void testAndQuery() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.like("role_name", "角色")
               .eq("role_code", "testManager2");
        List<SysRole> list = sysRoleMapper.selectList(wrapper);
        System.out.println("AND多条件查询: " + list);
    }

    // 32. isNull - 判断为空
    @Test
    public void testIsNull() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.isNull("description");
        List<SysRole> list = sysRoleMapper.selectList(wrapper);
        System.out.println("isnull查询: " + list);
    }

    // 33. isNotNull - 判断不为空
    @Test
    public void testIsNotNull() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.isNotNull("description");
        List<SysRole> list = sysRoleMapper.selectList(wrapper);
        System.out.println("isNotNull查询: " + list);
    }

    // 34. groupBy + having - 分组统计
    @Test
    public void testGroupBy() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.select("role_code, COUNT(*) as count")
               .groupBy("role_code");
        List<Map<String, Object>> maps = sysRoleMapper.selectMaps(wrapper);
        System.out.println("分组统计: " + maps);
    }

    // 35. exists - EXISTS子查询
    @Test
    public void testExists() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.exists("SELECT 1 FROM sys_role WHERE id = '1'");
        List<SysRole> list = sysRoleMapper.selectList(wrapper);
        System.out.println("exists查询: " + list);
    }

    // 36. nested - 嵌套条件
    @Test
    public void testNested() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.nested(w -> w.eq("role_code", "testManager")
                            .or()
                            .eq("role_code", "testManager2"));
        List<SysRole> list = sysRoleMapper.selectList(wrapper);
        System.out.println("嵌套条件查询: " + list);
    }
}
