package com.rc.rbac.controller;

import com.rc.rbac.common.PageResult;
import com.rc.rbac.common.Result;
import com.rc.rbac.dto.request.DemoCreateRequest;
import com.rc.rbac.dto.request.DemoUpdateRequest;
import com.rc.rbac.dto.response.DemoResponse;
import com.rc.rbac.service.DemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据结构示例控制器
 * 演示从数据库获取各种数据结构类型
 */
@Tag(name = "数据结构示例", description = "演示各种数据类型的CRUD操作")
@RestController
@RequestMapping("/api/demo")
@RequiredArgsConstructor
public class DemoController {
    
    private final DemoService demoService;
    
    // ==================== 基础CRUD接口 ====================
    
    @Operation(summary = "获取分页列表")
    @GetMapping
    public Result<PageResult<DemoResponse>> getDemoPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer status) {
        
        PageResult<DemoResponse> result = demoService.getDemoPage(pageNum, pageSize, name, status);
        return Result.success(result);
    }
    
    @Operation(summary = "获取详情")
    @GetMapping("/{id}")
    public Result<DemoResponse> getDemoById(@PathVariable Long id) {
        DemoResponse response = demoService.getDemoById(id);
        return Result.success(response);
    }
    
    @Operation(summary = "创建记录")
    @PostMapping
    public Result<Void> createDemo(@Valid @RequestBody DemoCreateRequest request) {
        try {
            demoService.createDemo(request);
            return Result.success("创建成功", null);
        } catch (Exception e) {
            return Result.error("创建失败: " + e.getMessage());
        }
    }
    
    @Operation(summary = "更新记录")
    @PutMapping("/{id}")
    public Result<Void> updateDemo(@PathVariable Long id, @Valid @RequestBody DemoUpdateRequest request) {
        try {
            demoService.updateDemo(id, request);
            return Result.success("更新成功", null);
        } catch (Exception e) {
            return Result.error("更新失败: " + e.getMessage());
        }
    }
    
    @Operation(summary = "删除记录")
    @DeleteMapping("/{id}")
    public Result<Void> deleteDemo(@PathVariable Long id) {
        try {
            demoService.deleteDemo(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error("删除失败: " + e.getMessage());
        }
    }
    
    // ==================== 自定义查询接口 ====================
    
    @Operation(summary = "根据名称查询")
    @GetMapping("/name/{name}")
    public Result<List<DemoResponse>> getDemoByName(@PathVariable String name) {
        List<DemoResponse> result = demoService.getDemoByName(name);
        return Result.success(result);
    }
    
    @Operation(summary = "查询启用状态的记录")
    @GetMapping("/enabled")
    public Result<List<DemoResponse>> getEnabledDemos() {
        List<DemoResponse> result = demoService.getEnabledDemos();
        return Result.success(result);
    }
    
    @Operation(summary = "根据年龄范围查询")
    @GetMapping("/age-range")
    public Result<List<DemoResponse>> getDemoByAgeRange(
            @RequestParam Integer minAge,
            @RequestParam Integer maxAge) {
        List<DemoResponse> result = demoService.getDemoByAgeRange(minAge, maxAge);
        return Result.success(result);
    }
}
