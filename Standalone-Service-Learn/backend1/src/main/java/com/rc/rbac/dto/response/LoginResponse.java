package com.rc.rbac.dto.response;

import lombok.Data;
import java.util.List;

/**
 * 登录响应
 */
@Data
public class LoginResponse {
    
    /**
     * 令牌
     */
    private String token;
    
    /**
     * 用户信息
     */
    private UserInfo userInfo;
    
    /**
     * 用户信息内部类
     */
    @Data
    public static class UserInfo {
        private Long id;
        private String username;
        private String nickname;
        private String avatar;
        private List<String> roles;
        private List<String> permissions;
    }
}
