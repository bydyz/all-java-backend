package com.rc.rbac.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

/**
 * 角色创建请求
 */
@Data
public class RoleCreateRequest {
    
    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 2, max = 50, message = "角色名称长度必须在2-50之间")
    private String roleName;
    
    /**
     * 角色标识
     */
    @NotBlank(message = "角色标识不能为空")
    @Size(min = 2, max = 50, message = "角色标识长度必须在2-50之间")
    private String roleKey;
    
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
