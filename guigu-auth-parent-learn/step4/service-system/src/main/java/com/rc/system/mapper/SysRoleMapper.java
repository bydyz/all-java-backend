package com.rc.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rc.model.system.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rc.model.vo.SysRoleQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {
    //条件分页查询
    // @Param("vo") 在 mybatis 中提供的      便于向后面的 xml 中传递参数
    IPage<SysRole> selectPage(Page<SysRole> pageParam, @Param("vo") SysRoleQueryVo sysRoleQueryVo);
}
