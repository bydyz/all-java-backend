package com.rc.rbac.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全工具类
 */
@Slf4j
public class SecurityUtils {
    
    /**
     * 获取当前登录用户名
     */
    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("SecurityUtils - authentication: {}, principal: {}, name: {}", 
                authentication != null ? authentication.getClass().getSimpleName() : "null",
                authentication != null ? authentication.getPrincipal() : "null",
                authentication != null ? authentication.getName() : "null");
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
    
    /**
     * 获取当前登录用户
     */
    public static Object getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getPrincipal();
        }
        return null;
    }
    
    /**
     * 判断是否已认证
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}
