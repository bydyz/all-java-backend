package com.rc.rbac.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

/**
 * 角色更新请求
 */
@Data
public class RoleUpdateRequest {
    
    /**
     * 角色名称
     */
    @Size(min = 2, max = 50, message = "角色名称长度必须在2-50之间")
    private String roleName;
    
    /**
     * 描述
     */
    @Size(max = 200, message = "描述长度不能超过200")
    private String description;
    
    /**
     * 状态 0:禁用 1:启用
     */
    private Integer status;
    
    /**
     * 菜单ID列表
     */
    private List<Long> menuIds;
}
