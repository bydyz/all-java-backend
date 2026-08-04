package com.rc.rbac.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 菜单更新请求
 */
@Data
public class MenuUpdateRequest {
    
    /**
     * 菜单名称
     */
    @Size(min = 1, max = 50, message = "菜单名称长度必须在1-50之间")
    private String menuName;
    
    /**
     * 父菜单ID
     */
    private Long parentId;
    
    /**
     * 路由路径
     */
    private String path;
    
    /**
     * 组件路径
     */
    private String component;
    
    /**
     * 重定向地址
     */
    private String redirect;
    
    /**
     * 图标
     */
    private String icon;
    
    /**
     * 显示标题
     */
    private String title;
    
    /**
     * 是否隐藏 0:显示 1:隐藏
     */
    private Integer hidden;
    
    /**
     * 是否缓存 0:不缓存 1:缓存
     */
    private Integer keepAlive;
    
    /**
     * 权限标识
     */
    private String permission;
    
    /**
     * 类型 D:目录 M:菜单 B:按钮
     */
    private String type;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 状态 0:禁用 1:启用
     */
    private Integer status;
}
