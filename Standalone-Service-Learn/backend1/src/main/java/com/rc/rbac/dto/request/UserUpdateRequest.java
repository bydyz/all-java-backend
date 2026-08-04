package com.rc.rbac.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

/**
 * 用户更新请求
 */
@Data
public class UserUpdateRequest {
    
    /**
     * 昵称
     */
    @Size(max = 50, message = "昵称长度不能超过50")
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
     * 角色ID列表
     */
    private List<Long> roleIds;
}
