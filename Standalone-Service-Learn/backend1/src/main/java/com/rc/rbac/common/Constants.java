package com.rc.rbac.common;

/**
 * 常量类
 */
public class Constants {
    
    /**
     * 用户状态：禁用
     */
    public static final Integer STATUS_DISABLED = 0;
    
    /**
     * 用户状态：启用
     */
    public static final Integer STATUS_ENABLED = 1;
    
    /**
     * 菜单类型：目录
     */
    public static final String MENU_TYPE_DIRECTORY = "D";
    
    /**
     * 菜单类型：菜单
     */
    public static final String MENU_TYPE_MENU = "M";
    
    /**
     * 菜单类型：按钮
     */
    public static final String MENU_TYPE_BUTTON = "B";
    
    /**
     * 父菜单ID：顶级菜单
     */
    public static final Long ROOT_MENU_ID = 0L;
    
    /**
     * 删除标记：未删除
     */
    public static final Integer NOT_DELETED = 0;
    
    /**
     * 删除标记：已删除
     */
    public static final Integer DELETED = 1;
    
    /**
     * 隐藏：显示
     */
    public static final Integer SHOW = 0;
    
    /**
     * 隐藏：隐藏
     */
    public static final Integer HIDDEN = 1;
    
    /**
     * 缓存：不缓存
     */
    public static final Integer NO_CACHE = 0;
    
    /**
     * 缓存：缓存
     */
    public static final Integer CACHE = 1;
}
