package com.rc.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rc.model.system.SysRole;
import com.rc.model.vo.SysRoleQueryVo;
import com.rc.system.mapper.SysRoleMapper;
import com.rc.system.service.SysRoleService;
import org.springframework.stereotype.Service;


@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    //条件分页查询
    @Override
    public IPage<SysRole> selectPage(Page<SysRole> pageParam, SysRoleQueryVo sysRoleQueryVo) {
        // 需要在 mapper 中创建 selectPage ，否则 baseMapper.selectPage(pageParam,sysRoleQueryVo) 会报警告
        IPage<SysRole> pageModel = baseMapper.selectPage(pageParam, sysRoleQueryVo);
        return pageModel;
    }
}
