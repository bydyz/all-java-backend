package com.rc.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rc.rbac.entity.RoleMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 角色菜单关联 Mapper 接口
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    
    /**
     * 根据角色ID删除角色菜单关联
     */
    @Delete("DELETE FROM role_menus WHERE role_id = #{roleId}")
    int deleteByRoleId(@Param("roleId") Long roleId);
}
