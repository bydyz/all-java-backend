package com.rc.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rc.rbac.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 菜单 Mapper 接口
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    
    /**
     * 根据用户ID查询有权限的菜单列表
     */
    @Select("SELECT DISTINCT m.* FROM menus m " +
            "INNER JOIN role_menus rm ON m.id = rm.menu_id " +
            "INNER JOIN user_roles ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND m.type != 'B' AND m.status = 1 AND m.deleted = 0 " +
            "ORDER BY m.sort")
    List<Menu> selectMenusByUserId(@Param("userId") Long userId);
    
    /**
     * 查询所有菜单列表
     */
    @Select("SELECT * FROM menus WHERE status = 1 AND deleted = 0 ORDER BY sort")
    List<Menu> selectAllMenus();
    
    /**
     * 根据父ID查询子菜单列表
     */
    @Select("SELECT * FROM menus WHERE parent_id = #{parentId} AND status = 1 AND deleted = 0 ORDER BY sort")
    List<Menu> selectByParentId(@Param("parentId") Long parentId);
}
