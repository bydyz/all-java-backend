package com.rc.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rc.rbac.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户 Mapper 接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 根据用户名查询用户
     */
    @Select("SELECT * FROM users WHERE username = #{username} AND deleted = 0")
    User selectByUsername(@Param("username") String username);
    
    /**
     * 查询用户的角色ID列表
     */
    @Select("SELECT role_id FROM user_roles WHERE user_id = #{userId}")
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);
    
    /**
     * 查询用户的权限标识列表
     */
    @Select("SELECT DISTINCT m.permission FROM menus m " +
            "INNER JOIN role_menus rm ON m.id = rm.menu_id " +
            "INNER JOIN user_roles ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND m.permission IS NOT NULL AND m.permission != '' AND m.status = 1 AND m.deleted = 0")
    List<String> selectPermissionsByUserId(@Param("userId") Long userId);
}
