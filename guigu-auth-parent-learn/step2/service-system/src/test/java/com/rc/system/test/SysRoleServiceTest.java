package com.rc.system.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rc.model.system.SysRole;
import com.rc.system.service.SysRoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class SysRoleServiceTest {

    //注入service
    @Autowired
    private SysRoleService sysRoleService;

    //查询所有
    @Test
    public void findAll() {
        //service方法实现       原理上是，获取 service 对应的 mapper，然后利用mapper中的方式实现
        List<SysRole> list = sysRoleService.list();
        // System.out.println(list);
    }

    //添加
    @Test
    public void add() {
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("角色管理员atguigu");
        sysRole.setRoleCode("role");
        sysRole.setDescription("角色管理员");
        sysRoleService.save(sysRole);
    }

    //修改
    @Test
    public void update() {
        SysRole sysRole = sysRoleService.getById(1);
        sysRole.setDescription("test");
        sysRoleService.updateById(sysRole);
    }

    //删除
    @Test
    public void remove() {
        sysRoleService.removeById(8);
    }

    //条件查询
    @Test
    public void select() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_code","SYSTEM");
        List<SysRole> list = sysRoleService.list(wrapper);
        System.out.println(list);
    }

    //批量添加
    @Test
    public void saveBatch() {
        SysRole r1 = new SysRole();
        r1.setRoleName("测试角色1");
        r1.setRoleCode("test1");
        SysRole r2 = new SysRole();
        r2.setRoleName("测试角色2");
        r2.setRoleCode("test2");
        sysRoleService.saveBatch(Arrays.asList(r1, r2));
    }

    //插入或更新
    @Test
    public void saveOrUpdate() {
        SysRole sysRole = sysRoleService.getById(1);
        sysRole.setDescription("saveOrUpdate");
        sysRoleService.saveOrUpdate(sysRole);
    }

    //根据Map条件删除
    @Test
    public void removeByMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("role_code", "test1");
        sysRoleService.removeByMap(map);
    }

    //根据Wrapper条件删除
    @Test
    public void removeByWrapper() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_code", "test2");
        sysRoleService.remove(wrapper);
    }

    //批量删除
    @Test
    public void removeByIds() {
        sysRoleService.removeByIds(Arrays.asList(9, 10));
    }

    //根据Wrapper条件更新
    @Test
    public void updateByWrapper() {
        UpdateWrapper<SysRole> wrapper = new UpdateWrapper<>();
        wrapper.eq("role_name", "test");
        SysRole sysRole = new SysRole();
        sysRole.setDescription("wrapperUpdate");
        sysRoleService.update(sysRole, wrapper);
    }

    //批量更新
    @Test
    public void updateBatchById() {
        SysRole r1 = sysRoleService.getById(1);
        r1.setDescription("batch1");
        SysRole r2 = sysRoleService.getById(2);
        r2.setDescription("batch2");
        sysRoleService.updateBatchById(Arrays.asList(r1, r2));
    }

    //查询单条记录
    @Test
    public void getOne() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_code", "SYSTEM");
        SysRole sysRole = sysRoleService.getOne(wrapper);
        System.out.println(sysRole);
    }

    //根据ID集合查询
    @Test
    public void listByIds() {
        List<SysRole> list = sysRoleService.listByIds(Arrays.asList(1, 2));
        System.out.println(list);
    }

    //根据Map条件查询
    @Test
    public void listByMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("role_code", "SYSTEM");
        List<SysRole> list = sysRoleService.listByMap(map);
        System.out.println(list);
    }

    //查询总数
    @Test
    public void count() {
        int count = sysRoleService.count();
        System.out.println(count);
    }

    //条件查询总数
    @Test
    public void countByWrapper() {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.gt("id", 1);
        int count = sysRoleService.count(wrapper);
        System.out.println(count);
    }

    //分页查询
    @Test
    public void page() {
        Page<SysRole> page = new Page<>(1, 3);
        Page<SysRole> result = sysRoleService.page(page);
        System.out.println("总记录数: " + result.getTotal());
        System.out.println("总页数: " + result.getPages());
        System.out.println("当前页数据: " + result.getRecords());
    }

    //条件分页查询
    @Test
    public void pageByWrapper() {
        Page<SysRole> page = new Page<>(1, 3);
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        Page<SysRole> result = sysRoleService.page(page, wrapper);
        System.out.println(result.getRecords());
    }

    //查询Map列表
    @Test
    public void listMaps() {
        List<Map<String, Object>> list = sysRoleService.listMaps();
        System.out.println(list);
    }

    //查询第一个字段列表
    @Test
    public void listObjs() {
        List<Object> list = sysRoleService.listObjs();
        System.out.println(list);
    }

    //链式查询
    @Test
    public void lambdaQuery() {
        List<SysRole> list = sysRoleService.lambdaQuery()
                .eq(SysRole::getRoleCode, "SYSTEM")
                .list();
        System.out.println(list);
    }

    //链式更新
    @Test
    public void lambdaUpdate() {
        sysRoleService.lambdaUpdate()
                .eq(SysRole::getRoleName, "test")
                .set(SysRole::getDescription, "lambdaUpdate")
                .update();
    }
}
