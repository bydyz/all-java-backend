package com.rc.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rc.rbac.common.PageResult;
import com.rc.rbac.dto.request.LoginRequest;
import com.rc.rbac.dto.request.RegisterRequest;
import com.rc.rbac.dto.request.UserCreateRequest;
import com.rc.rbac.dto.request.UserUpdateRequest;
import com.rc.rbac.dto.response.LoginResponse;
import com.rc.rbac.dto.response.MenuTreeResponse;
import com.rc.rbac.dto.response.UserResponse;
import com.rc.rbac.entity.User;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    
    /**
     * 用户登录
     */
    LoginResponse login(LoginRequest request);
    
    /**
     * 用户登出
     */
    void logout(String token);
    
    /**
     * 用户注册
     */
    void register(RegisterRequest request);
    
    /**
     * 获取用户分页列表
     */
    PageResult<UserResponse> getUserPage(Integer pageNum, Integer pageSize, String username, Integer status);
    
    /**
     * 获取用户详情
     */
    UserResponse getUserById(Long id);
    
    /**
     * 创建用户
     */
    void createUser(UserCreateRequest request);
    
    /**
     * 更新用户
     */
    void updateUser(Long id, UserUpdateRequest request);
    
    /**
     * 删除用户
     */
    void deleteUser(Long id);
    
    /**
     * 修改用户状态
     */
    void updateUserStatus(Long id, Integer status);
    
    /**
     * 重置用户密码
     */
    void resetPassword(Long id, String password);
    
    /**
     * 根据用户名查询用户
     */
    User getByUsername(String username);
    
    /**
     * 获取用户的权限标识列表
     */
    List<String> getUserPermissions(Long userId);
    
    /**
     * 获取当前登录用户信息
     */
    LoginResponse.UserInfo getCurrentUserInfo();
    
    /**
     * 获取当前登录用户的菜单树
     */
    List<MenuTreeResponse> getCurrentUserMenus();
}
