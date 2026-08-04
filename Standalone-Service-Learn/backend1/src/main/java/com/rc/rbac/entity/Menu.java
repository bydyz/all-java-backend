package com.rc.rbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 菜单实体
 */
@Data
@TableName("menus")
public class Menu implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 菜单ID
     */
    @TableId(type = IdType.AUTO)
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
    
    /**
     * 删除标记
     */
    @TableLogic
    private Integer deleted;
    
    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
