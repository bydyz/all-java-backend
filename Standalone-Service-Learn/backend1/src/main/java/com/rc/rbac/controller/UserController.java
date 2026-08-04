package com.rc.rbac.controller;

import com.rc.rbac.common.PageResult;
import com.rc.rbac.common.Result;
import com.rc.rbac.dto.request.UserCreateRequest;
import com.rc.rbac.dto.request.UserUpdateRequest;
import com.rc.rbac.dto.response.UserResponse;
import com.rc.rbac.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器
 */
@Tag(name = "用户管理", description = "用户增删改查接口")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @Operation(summary = "获取用户分页列表")
    @GetMapping
    public Result<PageResult<UserResponse>> getUserPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer status) {
        PageResult<UserResponse> result = userService.getUserPage(pageNum, pageSize, username, status);
        return Result.success(result);
    }
    
    @Operation(summary = "获取用户详情")
    @GetMapping("/{id}")
    public Result<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return Result.success(user);
    }
    
    @Operation(summary = "创建用户")
    @PostMapping
    public Result<Void> createUser(@Valid @RequestBody UserCreateRequest request) {
        try {
            userService.createUser(request);
            return Result.success("创建成功", null);
        } catch (Exception e) {
            return Result.error("创建失败: " + e.getMessage());
        }
    }
    
    @Operation(summary = "更新用户")
    @PutMapping("/{id}")
    public Result<Void> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        try {
            userService.updateUser(id, request);
            return Result.success("更新成功", null);
        } catch (Exception e) {
            return Result.error("更新失败: " + e.getMessage());
        }
    }
    
    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error("删除失败: " + e.getMessage());
        }
    }
    
    @Operation(summary = "修改用户状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateUserStatus(@PathVariable Long id, @RequestBody Integer status) {
        try {
            userService.updateUserStatus(id, status);
            return Result.success("操作成功", null);
        } catch (Exception e) {
            return Result.error("操作失败: " + e.getMessage());
        }
    }
    
    @Operation(summary = "重置用户密码")
    @PutMapping("/{id}/password")
    public Result<Void> resetPassword(@PathVariable Long id, @RequestBody String password) {
        try {
            userService.resetPassword(id, password);
            return Result.success("密码重置成功", null);
        } catch (Exception e) {
            return Result.error("密码重置失败: " + e.getMessage());
        }
    }
}
