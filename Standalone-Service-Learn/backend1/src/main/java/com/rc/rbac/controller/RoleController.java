package com.rc.rbac.controller;

import com.rc.rbac.common.PageResult;
import com.rc.rbac.common.Result;
import com.rc.rbac.dto.request.RoleCreateRequest;
import com.rc.rbac.dto.request.RoleUpdateRequest;
import com.rc.rbac.dto.response.RoleResponse;
import com.rc.rbac.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 */
@Tag(name = "角色管理", description = "角色增删改查接口")
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    
    private final RoleService roleService;
    
    @Operation(summary = "获取角色分页列表")
    @GetMapping
    public Result<PageResult<RoleResponse>> getRolePage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) Integer status) {
        PageResult<RoleResponse> result = roleService.getRolePage(pageNum, pageSize, roleName, status);
        return Result.success(result);
    }
    
    @Operation(summary = "获取角色列表（用于下拉选择）")
    @GetMapping("/list")
    public Result<List<RoleResponse>> getRoleList() {
        List<RoleResponse> result = roleService.getRoleList();
        return Result.success(result);
    }
    
    @Operation(summary = "获取角色详情")
    @GetMapping("/{id}")
    public Result<RoleResponse> getRoleById(@PathVariable Long id) {
        RoleResponse role = roleService.getRoleById(id);
        return Result.success(role);
    }
    
    @Operation(summary = "创建角色")
    @PostMapping
    public Result<Void> createRole(@Valid @RequestBody RoleCreateRequest request) {
        try {
            roleService.createRole(request);
            return Result.success("创建成功", null);
        } catch (Exception e) {
            return Result.error("创建失败: " + e.getMessage());
        }
    }
    
    @Operation(summary = "更新角色")
    @PutMapping("/{id}")
    public Result<Void> updateRole(@PathVariable Long id, @Valid @RequestBody RoleUpdateRequest request) {
        try {
            roleService.updateRole(id, request);
            return Result.success("更新成功", null);
        } catch (Exception e) {
            return Result.error("更新失败: " + e.getMessage());
        }
    }
    
    @Operation(summary = "删除角色")
    @DeleteMapping("/{id}")
    public Result<Void> deleteRole(@PathVariable Long id) {
        try {
            roleService.deleteRole(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error("删除失败: " + e.getMessage());
        }
    }
    
    @Operation(summary = "修改角色状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateRoleStatus(@PathVariable Long id, @RequestBody Integer status) {
        try {
            roleService.updateRoleStatus(id, status);
            return Result.success("操作成功", null);
        } catch (Exception e) {
            return Result.error("操作失败: " + e.getMessage());
        }
    }
}
