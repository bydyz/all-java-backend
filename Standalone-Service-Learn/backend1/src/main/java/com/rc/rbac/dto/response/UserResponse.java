package com.rc.rbac.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户响应
 */
@Data
public class UserResponse {
    
    /**
     * 用户ID
     */
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 头像地址
     */
    private String avatar;
    
    /**
     * 状态 0:禁用 1:启用
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 角色列表
     */
    private List<RoleInfo> roles;
    
    /**
     * 角色信息内部类
     */
    @Data
    public static class RoleInfo {
        private Long id;
        private String roleName;
        private String roleKey;
    }
}
