package com.rc.rbac.controller;

import com.rc.rbac.common.Result;
import com.rc.rbac.dto.request.MenuCreateRequest;
import com.rc.rbac.dto.request.MenuUpdateRequest;
import com.rc.rbac.dto.response.MenuTreeResponse;
import com.rc.rbac.entity.Menu;
import com.rc.rbac.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理控制器
 */
@Tag(name = "菜单管理", description = "菜单增删改查接口")
@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {
    
    private final MenuService menuService;
    
    @Operation(summary = "获取菜单树")
    @GetMapping("/tree")
    public Result<List<MenuTreeResponse>> getMenuTree(@RequestParam(required = false) String type) {
        List<MenuTreeResponse> tree;
        if (type != null && !type.isEmpty()) {
            tree = menuService.getMenuTreeByType(type);
        } else {
            tree = menuService.getMenuTree();
        }
        return Result.success(tree);
    }
    
    @Operation(summary = "获取菜单详情")
    @GetMapping("/{id}")
    public Result<Menu> getMenuById(@PathVariable Long id) {
        Menu menu = menuService.getMenuById(id);
        return Result.success(menu);
    }
    
    @Operation(summary = "创建菜单")
    @PostMapping
    public Result<Void> createMenu(@Valid @RequestBody MenuCreateRequest request) {
        try {
            menuService.createMenu(request);
            return Result.success("创建成功", null);
        } catch (Exception e) {
            return Result.error("创建失败: " + e.getMessage());
        }
    }
    
    @Operation(summary = "更新菜单")
    @PutMapping("/{id}")
    public Result<Void> updateMenu(@PathVariable Long id, @Valid @RequestBody MenuUpdateRequest request) {
        try {
            menuService.updateMenu(id, request);
            return Result.success("更新成功", null);
        } catch (Exception e) {
            return Result.error("更新失败: " + e.getMessage());
        }
    }
    
    @Operation(summary = "删除菜单")
    @DeleteMapping("/{id}")
    public Result<Void> deleteMenu(@PathVariable Long id) {
        try {
            menuService.deleteMenu(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error("删除失败: " + e.getMessage());
        }
    }
}
