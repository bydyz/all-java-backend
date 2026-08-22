package com.rc.rbac.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rc.rbac.common.PageResult;
import com.rc.rbac.common.Result;
import com.rc.rbac.entity.ParamTypeDemo;
import com.rc.rbac.service.ParamTypeDemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 接口传参方式全量演示控制器
 * 演示 Spring MVC 支持的全部 8 种参数获取方式
 */
@Tag(name = "接口传参方式演示", description = "全量列举获取接口传参的所有方式")
@RestController
@RequestMapping("/api/param-type")
@RequiredArgsConstructor
public class ParamTypeController {

    private final ParamTypeDemoService paramTypeDemoService;

    // ==================== CRUD 基础接口（供其他方式查询） ====================

    @Operation(summary = "GET - 分页列表")
    @GetMapping
    public Result<PageResult<ParamTypeDemo>> getPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<ParamTypeDemo> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(ParamTypeDemo::getId);
        PageResult<ParamTypeDemo> page = new PageResult<>();
        page.setRecords(paramTypeDemoService.list(wrapper));
        page.setTotal(paramTypeDemoService.count());
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        return Result.success(page);
    }

    @Operation(summary = "GET - 根据ID获取详情")
    @GetMapping("/{id}")
    public Result<ParamTypeDemo> getById(@PathVariable Long id) {
        ParamTypeDemo record = paramTypeDemoService.getParamTypeDemoById(id);
        if (record == null) {
            return Result.error("未找到对应记录");
        }
        return Result.success(record);
    }

    // ==================== 1. @RequestParam 查询参数 ====================

    @Operation(summary = "@RequestParam - 获取URL查询参数，支持默认值和必填校验")
    @GetMapping("/request-param")
    public Result<Map<String, Object>> requestParamDemo(
            @RequestParam String name,
            @RequestParam(defaultValue = "18") Integer age,
            @RequestParam(required = false) String email) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("注解", "@RequestParam");
        result.put("name", name);
        result.put("age", age);
        result.put("email", email);
        result.put("说明", "从URL查询字符串 ?name=xxx&age=xxx 获取参数");
        return Result.success(result);
    }

    // ==================== 2. @PathVariable 路径变量 ====================

    @Operation(summary = "@PathVariable - 获取URL路径中的变量")
    @GetMapping("/path-variable/{id}/{username}")
    public Result<Map<String, Object>> pathVariableDemo(
            @PathVariable Long id,
            @PathVariable String username) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("注解", "@PathVariable");
        result.put("id", id);
        result.put("username", username);
        result.put("说明", "从URL路径 /path-variable/{id}/{username} 中提取变量");
        return Result.success(result);
    }

    // ==================== 3. @RequestBody 请求体 ====================

    @Operation(summary = "@RequestBody - 接收JSON/XML请求体")
    @PostMapping("/request-body")
    public Result<Map<String, Object>> requestBodyDemo(@RequestBody Map<String, Object> body) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("注解", "@RequestBody");
        result.put("收到的参数", body);
        result.put("说明", "从POST/PUT请求体中解析JSON数据");
        return Result.success(result);
    }

    // ==================== 4. @RequestHeader 请求头 ====================

    @Operation(summary = "@RequestHeader - 获取HTTP请求头")
    @GetMapping("/request-header")
    public Result<Map<String, Object>> requestHeaderDemo(
            @RequestHeader("User-Agent") String userAgent,
            @RequestHeader(value = "Accept", required = false) String accept,
            @RequestHeader(value = "X-Custom-Header", required = false, defaultValue = "默认值") String customHeader) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("注解", "@RequestHeader");
        result.put("User-Agent", userAgent);
        result.put("Accept", accept);
        result.put("X-Custom-Header", customHeader);
        result.put("说明", "从HTTP请求头中获取指定值");
        return Result.success(result);
    }

    // ==================== 5. @CookieValue Cookie值 ====================

    @Operation(summary = "@CookieValue - 获取Cookie中的值")
    @GetMapping("/cookie-value")
    public Result<Map<String, Object>> cookieValueDemo(
            @CookieValue(value = "JSESSIONID", required = false) String sessionId,
            @CookieValue(value = "username", required = false) String username) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("注解", "@CookieValue");
        result.put("JSESSIONID", sessionId);
        result.put("username", username);
        result.put("说明", "从请求Cookie中获取指定key的值");
        return Result.success(result);
    }

    // ==================== 6. @ModelAttribute 模型属性 ====================

    @Operation(summary = "@ModelAttribute - 表单数据绑定到对象")
    @GetMapping("/model-attribute")
    public Result<Map<String, Object>> modelAttributeGetDemo(@ModelAttribute UserForm form) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("注解", "@ModelAttribute");
        result.put("form", form);
        result.put("说明", "GET请求：URL查询参数自动绑定到对象属性");
        return Result.success(result);
    }

    @Operation(summary = "@ModelAttribute - 表单提交绑定")
    @PostMapping("/model-attribute")
    public Result<Map<String, Object>> modelAttributePostDemo(@ModelAttribute UserForm form) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("注解", "@ModelAttribute");
        result.put("form", form);
        result.put("说明", "POST请求：表单数据自动绑定到对象属性");
        return Result.success(result);
    }

    // ==================== 7. @MatrixVariable 矩阵变量 ====================

    @Operation(summary = "@MatrixVariable - URL路径分号后的键值对")
    @GetMapping("/matrix-variable/{path}")
    public Result<Map<String, Object>> matrixVariableDemo(
            @PathVariable String path,
            @MatrixVariable(value = "name", required = false) String name,
            @MatrixVariable(value = "age", required = false) Integer age) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("注解", "@MatrixVariable");
        result.put("path", path);
        result.put("name", name);
        result.put("age", age);
        result.put("说明", "从URL路径中分号后的键值对获取，如 /path;name=张三;age=25");
        return Result.success(result);
    }

    // ==================== 8. @RequestPart 文件/混合内容 ====================

    @Operation(summary = "@RequestPart - multipart文件上传与JSON混合")
    @PostMapping("/request-part")
    public Result<Map<String, Object>> requestPartDemo(
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "data", required = false) Map<String, String> data) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("注解", "@RequestPart");
        if (file != null) {
            result.put("文件名", file.getOriginalFilename());
            result.put("文件大小", file.getSize());
            result.put("Content-Type", file.getContentType());
        }
        result.put("data", data);
        result.put("说明", "multipart/form-data中获取文件或JSON片段");
        return Result.success(result);
    }

    // ==================== 查询单条记录（供其他方式演示） ====================

    @Operation(summary = "GET - 查询单条记录（用于演示多种传参方式）")
    @GetMapping("/record")
    public Result<ParamTypeDemo> getRecordByQueryParam(@RequestParam(defaultValue = "1") Long id) {
        ParamTypeDemo record = paramTypeDemoService.getParamTypeDemoById(id);
        return Result.success(record);
    }

    @Operation(summary = "POST - 创建记录（演示@RequestBody）")
    @PostMapping("/record")
    public Result<Void> createRecord(@RequestBody ParamTypeDemo paramTypeDemo) {
        paramTypeDemoService.save(paramTypeDemo);
        return Result.success("创建成功", null);
    }

    @Operation(summary = "PUT - 更新记录（演示@PathVariable + @RequestBody）")
    @PutMapping("/record/{id}")
    public Result<Void> updateRecord(
            @PathVariable Long id,
            @RequestBody ParamTypeDemo paramTypeDemo) {
        paramTypeDemo.setId(id);
        paramTypeDemoService.updateById(paramTypeDemo);
        return Result.success("更新成功", null);
    }

    @Operation(summary = "DELETE - 删除记录（演示@PathVariable）")
    @DeleteMapping("/record/{id}")
    public Result<Void> deleteRecord(@PathVariable Long id) {
        paramTypeDemoService.removeById(id);
        return Result.success("删除成功", null);
    }

    // ==================== 内部表单类 ====================

    /**
     * 表单数据绑定类（用于演示 @ModelAttribute）
     */
    @Data
    public static class UserForm {
        private String username;
        private Integer age;
        private String email;
    }
}
