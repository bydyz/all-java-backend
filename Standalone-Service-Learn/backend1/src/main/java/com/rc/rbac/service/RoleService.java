package com.rc.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rc.rbac.common.PageResult;
import com.rc.rbac.dto.request.RoleCreateRequest;
import com.rc.rbac.dto.request.RoleUpdateRequest;
import com.rc.rbac.dto.response.RoleResponse;
import com.rc.rbac.entity.Role;

import java.util.List;

/**
 * 角色服务接口
 */
public interface RoleService extends IService<Role> {
    
    /**
     * 获取角色分页列表
     */
    PageResult<RoleResponse> getRolePage(Integer pageNum, Integer pageSize, String roleName, Integer status);
    
    /**
     * 获取角色列表（用于下拉选择）
     */
    List<RoleResponse> getRoleList();
    
    /**
     * 获取角色详情
     */
    RoleResponse getRoleById(Long id);
    
    /**
     * 创建角色
     */
    void createRole(RoleCreateRequest request);
    
    /**
     * 更新角色
     */
    void updateRole(Long id, RoleUpdateRequest request);
    
    /**
     * 删除角色
     */
    void deleteRole(Long id);
    
    /**
     * 修改角色状态
     */
    void updateRoleStatus(Long id, Integer status);
}
