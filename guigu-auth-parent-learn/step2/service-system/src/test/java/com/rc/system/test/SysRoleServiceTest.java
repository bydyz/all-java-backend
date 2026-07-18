package com.rc.system.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rc.model.system.SysRole;
import com.rc.system.service.SysRoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
}
