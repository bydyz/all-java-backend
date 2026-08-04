package com.rc.rbac.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色响应
 */
@Data
public class RoleResponse {
    
    /**
     * 角色ID
     */
    private Long id;
    
    /**
     * 角色名称
     */
    private String roleName;
    
    /**
     * 角色标识
     */
    private String roleKey;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 状态 0:禁用 1:启用
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 菜单ID列表
     */
    private List<Long> menuIds;
}
