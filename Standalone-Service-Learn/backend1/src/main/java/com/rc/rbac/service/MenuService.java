package com.rc.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rc.rbac.dto.request.MenuCreateRequest;
import com.rc.rbac.dto.request.MenuUpdateRequest;
import com.rc.rbac.dto.response.MenuTreeResponse;
import com.rc.rbac.entity.Menu;

import java.util.List;

/**
 * 菜单服务接口
 */
public interface MenuService extends IService<Menu> {
    
    /**
     * 获取菜单树
     */
    List<MenuTreeResponse> getMenuTree();
    
    /**
     * 根据类型获取菜单树
     */
    List<MenuTreeResponse> getMenuTreeByType(String type);
    
    /**
     * 根据用户ID获取菜单树
     */
    List<MenuTreeResponse> getMenuTreeByUserId(Long userId);
    
    /**
     * 获取菜单详情
     */
    Menu getMenuById(Long id);
    
    /**
     * 创建菜单
     */
    void createMenu(MenuCreateRequest request);
    
    /**
     * 更新菜单
     */
    void updateMenu(Long id, MenuUpdateRequest request);
    
    /**
     * 删除菜单
     */
    void deleteMenu(Long id);
    
    /**
     * 构建菜单树
     */
    List<MenuTreeResponse> buildMenuTree(List<Menu> menus);
}
