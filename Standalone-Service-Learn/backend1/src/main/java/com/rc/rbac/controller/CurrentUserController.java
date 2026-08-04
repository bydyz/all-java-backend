package com.rc.rbac.controller;

import com.rc.rbac.common.Result;
import com.rc.rbac.dto.response.LoginResponse;
import com.rc.rbac.dto.response.MenuTreeResponse;
import com.rc.rbac.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 当前用户控制器
 */
@Tag(name = "当前用户", description = "获取当前登录用户信息接口")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class CurrentUserController {
    
    private final UserService userService;
    
    @Operation(summary = "获取当前用户信息")
    @GetMapping("/info")
    public Result<LoginResponse.UserInfo> getUserInfo() {
        LoginResponse.UserInfo userInfo = userService.getCurrentUserInfo();
        return Result.success(userInfo);
    }
    
    @Operation(summary = "获取当前用户菜单树")
    @GetMapping("/menus")
    public Result<List<MenuTreeResponse>> getUserMenus() {
        List<MenuTreeResponse> menus = userService.getCurrentUserMenus();
        return Result.success(menus);
    }
}
