package com.rc.system.mapper;

import com.rc.model.system.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

// SysRoleMapper 继承 BaseMapper<SysRole> 后，自动拥有 BaseMapper定义的操作数据库 的一些列简便方法
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {

}