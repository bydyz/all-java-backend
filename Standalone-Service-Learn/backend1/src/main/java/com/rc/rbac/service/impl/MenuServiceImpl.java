package com.rc.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rc.rbac.common.Constants;
import com.rc.rbac.dto.request.MenuCreateRequest;
import com.rc.rbac.dto.request.MenuUpdateRequest;
import com.rc.rbac.dto.response.MenuTreeResponse;
import com.rc.rbac.entity.Menu;
import com.rc.rbac.mapper.MenuMapper;
import com.rc.rbac.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    
    private final MenuMapper menuMapper;
    
    @Override
    public List<MenuTreeResponse> getMenuTree() {
        List<Menu> menus = menuMapper.selectAllMenus();
        return buildMenuTree(menus);
    }
    
    @Override
    public List<MenuTreeResponse> getMenuTreeByType(String type) {
        List<Menu> menus = menuMapper.selectAllMenus().stream()
                .filter(menu -> menu.getType().equals(type))
                .collect(Collectors.toList());
        return buildMenuTree(menus);
    }
    
    @Override
    public List<MenuTreeResponse> getMenuTreeByUserId(Long userId) {
        List<Menu> menus = menuMapper.selectMenusByUserId(userId);
        return buildMenuTree(menus);
    }
    
    @Override
    public Menu getMenuById(Long id) {
        return menuMapper.selectById(id);
    }
    
    @Override
    public void createMenu(MenuCreateRequest request) {
        Menu menu = new Menu();
        menu.setMenuName(request.getMenuName());
        menu.setParentId(request.getParentId());
        menu.setPath(request.getPath());
        menu.setComponent(request.getComponent());
        menu.setRedirect(request.getRedirect());
        menu.setIcon(request.getIcon());
        menu.setTitle(request.getTitle());
        menu.setHidden(request.getHidden() != null ? request.getHidden() : Constants.SHOW);
        menu.setKeepAlive(request.getKeepAlive() != null ? request.getKeepAlive() : Constants.NO_CACHE);
        menu.setPermission(request.getPermission());
        menu.setType(request.getType());
        menu.setSort(request.getSort() != null ? request.getSort() : 0);
        menu.setStatus(request.getStatus() != null ? request.getStatus() : Constants.STATUS_ENABLED);
        menu.setCreateBy("admin");
        menuMapper.insert(menu);
    }
    
    @Override
    public void updateMenu(Long id, MenuUpdateRequest request) {
        Menu menu = menuMapper.selectById(id);
        if (menu == null) {
            throw new RuntimeException("菜单不存在");
        }
        
        menu.setMenuName(request.getMenuName());
        menu.setParentId(request.getParentId());
        menu.setPath(request.getPath());
        menu.setComponent(request.getComponent());
        menu.setRedirect(request.getRedirect());
        menu.setIcon(request.getIcon());
        menu.setTitle(request.getTitle());
        menu.setHidden(request.getHidden());
        menu.setKeepAlive(request.getKeepAlive());
        menu.setPermission(request.getPermission());
        menu.setType(request.getType());
        menu.setSort(request.getSort());
        menu.setStatus(request.getStatus());
        menu.setUpdateBy("admin");
        menuMapper.updateById(menu);
    }
    
    @Override
    public void deleteMenu(Long id) {
        // 检查是否有子菜单
        List<Menu> children = menuMapper.selectByParentId(id);
        if (!children.isEmpty()) {
            throw new RuntimeException("存在子菜单，不允许删除");
        }
        menuMapper.deleteById(id);
    }
    
    @Override
    public List<MenuTreeResponse> buildMenuTree(List<Menu> menus) {
        // 获取所有根菜单
        List<Menu> rootMenus = menus.stream()
                .filter(menu -> menu.getParentId().equals(Constants.ROOT_MENU_ID))
                .sorted((m1, m2) -> Integer.compare(m1.getSort(), m2.getSort()))
                .collect(Collectors.toList());
        
        List<MenuTreeResponse> tree = new ArrayList<>();
        for (Menu rootMenu : rootMenus) {
            tree.add(buildMenuTreeNode(rootMenu, menus));
        }
        return tree;
    }
    
    /**
     * 递归构建菜单树节点
     */
    private MenuTreeResponse buildMenuTreeNode(Menu menu, List<Menu> allMenus) {
        MenuTreeResponse node = convertToMenuTreeResponse(menu);
        
        // 获取子菜单
        List<Menu> children = allMenus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .sorted((m1, m2) -> Integer.compare(m1.getSort(), m2.getSort()))
                .collect(Collectors.toList());
        
        if (!children.isEmpty()) {
            List<MenuTreeResponse> childNodes = new ArrayList<>();
            for (Menu child : children) {
                childNodes.add(buildMenuTreeNode(child, allMenus));
            }
            node.setChildren(childNodes);
        }
        
        return node;
    }
    
    /**
     * 转换为菜单树响应对象
     */
    private MenuTreeResponse convertToMenuTreeResponse(Menu menu) {
        MenuTreeResponse response = new MenuTreeResponse();
        response.setId(menu.getId());
        response.setMenuName(menu.getMenuName());
        response.setParentId(menu.getParentId());
        response.setPath(menu.getPath());
        response.setComponent(menu.getComponent());
        response.setRedirect(menu.getRedirect());
        response.setIcon(menu.getIcon());
        response.setTitle(menu.getTitle());
        response.setHidden(menu.getHidden());
        response.setKeepAlive(menu.getKeepAlive());
        response.setPermission(menu.getPermission());
        response.setType(menu.getType());
        response.setSort(menu.getSort());
        response.setStatus(menu.getStatus());
        return response;
    }
}
