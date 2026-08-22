package com.rc.rbac.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rc.rbac.common.PageResult;
import com.rc.rbac.common.Result;
import com.rc.rbac.entity.HttpMethodDemo;
import com.rc.rbac.service.HttpMethodDemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * HTTP 请求方式全量演示控制器
 * 演示 Spring MVC 支持的全部 7 种 HTTP 请求方式
 */
@Tag(name = "HTTP请求方式演示", description = "全量列举 Spring MVC 支持的所有 HTTP 请求方式")
@RestController
@RequestMapping("/api/http-method")
@RequiredArgsConstructor
public class HttpMethodController {

    private final HttpMethodDemoService httpMethodDemoService;

    // ==================== 1. GET ====================

    @Operation(summary = "GET - 获取资源，查询数据")
    @GetMapping
    public Result<PageResult<HttpMethodDemo>> getHttpMethodPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<HttpMethodDemo> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(HttpMethodDemo::getId);
        PageResult<HttpMethodDemo> page = new PageResult<>();
        page.setRecords(httpMethodDemoService.list(wrapper));
        page.setTotal(httpMethodDemoService.count());
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        return Result.success(page);
    }

    @Operation(summary = "GET - 根据ID获取详情")
    @GetMapping("/{id}")
    public Result<HttpMethodDemo> getHttpMethodById(@PathVariable Long id) {
        HttpMethodDemo record = httpMethodDemoService.getHttpMethodDemoById(id);
        if (record == null) {
            return Result.error("未找到对应记录");
        }
        return Result.success(record);
    }

    // ==================== 2. POST ====================

    @Operation(summary = "POST - 创建资源")
    @PostMapping
    public Result<Void> createHttpMethod(@RequestBody HttpMethodDemo httpMethodDemo) {
        httpMethodDemoService.save(httpMethodDemo);
        return Result.success("创建成功", null);
    }

    // ==================== 3. PUT ====================

    @Operation(summary = "PUT - 全量更新资源")
    @PutMapping("/{id}")
    public Result<Void> updateHttpMethod(@PathVariable Long id, @RequestBody HttpMethodDemo httpMethodDemo) {
        httpMethodDemo.setId(id);
        httpMethodDemoService.updateById(httpMethodDemo);
        return Result.success("更新成功", null);
    }

    // ==================== 4. DELETE ====================

    @Operation(summary = "DELETE - 删除资源")
    @DeleteMapping("/{id}")
    public Result<Void> deleteHttpMethod(@PathVariable Long id) {
        httpMethodDemoService.removeById(id);
        return Result.success("删除成功", null);
    }

    // ==================== 5. PATCH ====================

    @Operation(summary = "PATCH - 部分更新资源")
    @PatchMapping("/{id}")
    public Result<Void> patchHttpMethod(@PathVariable Long id, @RequestBody HttpMethodDemo httpMethodDemo) {
        httpMethodDemo.setId(id);
        httpMethodDemoService.updateById(httpMethodDemo);
        return Result.success("部分更新成功", null);
    }

    // ==================== 6. HEAD ====================

    @Operation(summary = "HEAD - 获取资源的元数据头信息（无响应体）")
    @RequestMapping(value = "/{id}/head", method = RequestMethod.HEAD)
    public void headHttpMethod(@PathVariable Long id) {
        HttpMethodDemo record = httpMethodDemoService.getHttpMethodDemoById(id);
        if (record == null) {
            throw new RuntimeException("未找到对应记录");
        }
        // HEAD 请求只返回响应头，不返回响应体
        // Spring MVC 会自动处理 Content-Type 等头部信息
    }

    // ==================== 7. OPTIONS ====================

    @Operation(summary = "OPTIONS - 查询服务器支持的请求方法")
    @RequestMapping(value = "/{id}/options", method = RequestMethod.OPTIONS)
    public Result<List<String>> optionsHttpMethod(@PathVariable Long id) {
        List<String> supportedMethods = List.of("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS");
        return Result.success(supportedMethods);
    }
}
