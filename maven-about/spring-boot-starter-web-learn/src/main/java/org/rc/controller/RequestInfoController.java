package org.rc.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求头和 Cookie 控制器 - 演示请求头和 Cookie 绑定
 * 
 * 演示内容：
 * 1. @RequestHeader - 请求头绑定
 * 2. @CookieValue - Cookie 值绑定
 */
@RestController
@RequestMapping("/api/request")
public class RequestInfoController {
    
    /**
     * GET /api/request/headers
     * 获取请求头信息
     * 
     * @param userAgent 浏览器标识
     * @param accept 接受的内容类型
     * @return 请求头信息
     */
    @GetMapping("/headers")
    public Map<String, String> getHeaders(
            @RequestHeader("User-Agent") String userAgent,
            @RequestHeader(value = "Accept", required = false) String accept) {
        
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", userAgent);
        headers.put("Accept", accept);
        
        return headers;
    }
    
    /**
     * GET /api/request/cookie
     * 获取 Cookie 值
     * 
     * @param sessionId 会话 ID (可选，有默认值)
     * @param userId 用户 ID (可选)
     * @return Cookie 信息
     */
    @GetMapping("/cookie")
    public Map<String, String> getCookies(
            @CookieValue(value = "sessionId", defaultValue = "no-session") String sessionId,
            @CookieValue(value = "userId", required = false) String userId) {
        
        Map<String, String> cookies = new HashMap<>();
        cookies.put("sessionId", sessionId);
        cookies.put("userId", userId);
        
        return cookies;
    }
    
    /**
     * GET /api/request/combined
     * 综合演示：请求头 + 路径变量 + 查询参数
     * 
     * @param id 路径变量
     * @param format 查询参数
     * @param userAgent 请求头
     * @return 综合信息
     */
    @GetMapping("/combined/{id}")
    public Map<String, Object> combinedExample(
            @PathVariable Long id,
            @RequestParam(defaultValue = "json") String format,
            @RequestHeader("User-Agent") String userAgent) {
        
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("format", format);
        result.put("userAgent", userAgent);
        
        return result;
    }
}
