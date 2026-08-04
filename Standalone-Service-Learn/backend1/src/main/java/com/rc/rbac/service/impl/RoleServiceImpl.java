package com.rc.rbac.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rc.rbac.common.PageResult;
import com.rc.rbac.dto.request.RoleCreateRequest;
import com.rc.rbac.dto.request.RoleUpdateRequest;
import com.rc.rbac.dto.response.RoleResponse;
import com.rc.rbac.entity.Role;
import com.rc.rbac.entity.RoleMenu;
import com.rc.rbac.mapper.RoleMapper;
import com.rc.rbac.mapper.RoleMenuMapper;
import com.rc.rbac.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色服务实现类
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    
    private final RoleMapper roleMapper;
    private final RoleMenuMapper roleMenuMapper;
    
    @Override
    public PageResult<RoleResponse> getRolePage(Integer pageNum, Integer pageSize, String roleName, Integer status) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        if (roleName != null && !roleName.isEmpty()) {
            wrapper.like(Role::getRoleName, roleName);
        }
        if (status != null) {
            wrapper.eq(Role::getStatus, status);
        }
        wrapper.orderByDesc(Role::getCreateTime);
        
        Page<Role> page = new Page<>(pageNum, pageSize);
        Page<Role> result = roleMapper.selectPage(page, wrapper);
        
        List<RoleResponse> records = result.getRecords().stream()
                .map(this::convertToRoleResponse)
                .collect(Collectors.toList());
        
        return PageResult.of(result.getTotal(), records, pageNum, pageSize);
    }
    
    @Override
    public List<RoleResponse> getRoleList() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getStatus, 1);
        wrapper.orderByAsc(Role::getCreateTime);
        
        List<Role> roles = roleMapper.selectList(wrapper);
        return roles.stream()
                .map(this::convertToRoleResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public RoleResponse getRoleById(Long id) {
        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }
        return convertToRoleResponse(role);
    }
    
    @Override
    @Transactional
    public void createRole(RoleCreateRequest request) {
        // 检查角色标识是否已存在
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getRoleKey, request.getRoleKey());
        Long count = roleMapper.selectCount(wrapper);
        if (count > 0) {
            throw new RuntimeException("角色标识已存在");
        }
        
        // 创建角色
        Role role = new Role();
        role.setRoleName(request.getRoleName());
        role.setRoleKey(request.getRoleKey());
        role.setDescription(request.getDescription());
        role.setStatus(request.getStatus() != null ? request.getStatus() : 1);
        role.setCreateBy("admin");
        roleMapper.insert(role);
        
        // 分配菜单权限
        if (request.getMenuIds() != null && !request.getMenuIds().isEmpty()) {
            for (Long menuId : request.getMenuIds()) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(role.getId());
                roleMenu.setMenuId(menuId);
                roleMenuMapper.insert(roleMenu);
            }
        }
    }
    
    @Override
    @Transactional
    public void updateRole(Long id, RoleUpdateRequest request) {
        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }
        
        // 更新角色信息
        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());
        role.setStatus(request.getStatus());
        role.setUpdateBy("admin");
        roleMapper.updateById(role);
        
        // 更新菜单权限
        if (request.getMenuIds() != null) {
            roleMenuMapper.deleteByRoleId(id);
            for (Long menuId : request.getMenuIds()) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(id);
                roleMenu.setMenuId(menuId);
                roleMenuMapper.insert(roleMenu);
            }
        }
    }
    
    @Override
    public void deleteRole(Long id) {
        // 检查是否有用户使用此角色
        // TODO: 检查用户角色关联
        roleMapper.deleteById(id);
    }
    
    @Override
    public void updateRoleStatus(Long id, Integer status) {
        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }
        role.setStatus(status);
        role.setUpdateBy("admin");
        roleMapper.updateById(role);
    }
    
    /**
     * 转换为角色响应对象
     */
    private RoleResponse convertToRoleResponse(Role role) {
        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setRoleName(role.getRoleName());
        response.setRoleKey(role.getRoleKey());
        response.setDescription(role.getDescription());
        response.setStatus(role.getStatus());
        response.setCreateTime(role.getCreateTime());
        
        // 获取角色菜单ID列表
        List<Long> menuIds = roleMapper.selectMenuIdsByRoleId(role.getId());
        response.setMenuIds(menuIds);
        
        return response;
    }
}
