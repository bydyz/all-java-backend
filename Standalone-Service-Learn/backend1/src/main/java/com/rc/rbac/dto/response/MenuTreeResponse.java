package com.rc.rbac.dto.response;

import lombok.Data;
import java.util.List;

/**
 * 菜单树响应
 */
@Data
public class MenuTreeResponse {
    
    /**
     * 菜单ID
     */
    private Long id;
    
    /**
     * 菜单名称
     */
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
     * 是否隐藏
     */
    private Integer hidden;
    
    /**
     * 是否缓存
     */
    private Integer keepAlive;
    
    /**
     * 权限标识
     */
    private String permission;
    
    /**
     * 类型
     */
    private String type;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 状态
     */
    private Integer status;
    
    /**
     * 子菜单列表
     */
    private List<MenuTreeResponse> children;
}
