package com.rc.rbac.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rc.rbac.common.Constants;
import com.rc.rbac.common.PageResult;
import com.rc.rbac.dto.request.LoginRequest;
import com.rc.rbac.dto.request.RegisterRequest;
import com.rc.rbac.dto.request.UserCreateRequest;
import com.rc.rbac.dto.request.UserUpdateRequest;
import com.rc.rbac.dto.response.LoginResponse;
import com.rc.rbac.dto.response.MenuTreeResponse;
import com.rc.rbac.dto.response.UserResponse;
import com.rc.rbac.entity.Role;
import com.rc.rbac.entity.User;
import com.rc.rbac.entity.UserRole;
import com.rc.rbac.mapper.RoleMapper;
import com.rc.rbac.mapper.UserMapper;
import com.rc.rbac.mapper.UserRoleMapper;
import com.rc.rbac.security.JwtTokenProvider;
import com.rc.rbac.security.SecurityUtils;
import com.rc.rbac.service.MenuService;
import com.rc.rbac.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate redisTemplate;
    private final MenuService menuService;
    
    @Override
    public LoginResponse login(LoginRequest request) {
        // 认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 生成token
        String token = jwtTokenProvider.generateToken(authentication);
        
        // 获取用户信息
        User user = userMapper.selectByUsername(request.getUsername());
        
        // 获取角色列表
        List<Role> roles = roleMapper.selectRolesByUserId(user.getId());
        List<String> roleKeys = roles.stream().map(Role::getRoleKey).collect(Collectors.toList());
        
        // 获取权限列表
        List<String> permissions = userMapper.selectPermissionsByUserId(user.getId());
        
        // 构建响应
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setNickname(user.getNickname());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setRoles(roleKeys);
        userInfo.setPermissions(permissions);
        
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserInfo(userInfo);
        
        // 存储token到Redis
        redisTemplate.opsForValue().set(
                "token:" + token,
                user.getId().toString(),
                24, TimeUnit.HOURS
        );
        
        return response;
    }
    
    @Override
    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        // 从Redis删除token
        redisTemplate.delete("token:" + token);
        // 清除SecurityContext
        SecurityContextHolder.clearContext();
    }
    
    @Override
    @Transactional
    public void register(RegisterRequest request) {
        // 检查用户名是否已存在
        User existingUser = userMapper.selectByUsername(request.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setStatus(Constants.STATUS_ENABLED);
        user.setCreateBy("system");
        userMapper.insert(user);
        
        // 分配默认角色（普通用户）
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(2L); // 普通用户角色ID
        userRoleMapper.insert(userRole);
    }
    
    @Override
    public PageResult<UserResponse> getUserPage(Integer pageNum, Integer pageSize, String username, Integer status) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (username != null && !username.isEmpty()) {
            wrapper.like(User::getUsername, username);
        }
        if (status != null) {
            wrapper.eq(User::getStatus, status);
        }
        wrapper.orderByDesc(User::getCreateTime);
        
        Page<User> page = new Page<>(pageNum, pageSize);
        Page<User> result = userMapper.selectPage(page, wrapper);
        
        List<UserResponse> records = result.getRecords().stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
        
        return PageResult.of(result.getTotal(), records, pageNum, pageSize);
    }
    
    @Override
    public UserResponse getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return convertToUserResponse(user);
    }
    
    @Override
    @Transactional
    public void createUser(UserCreateRequest request) {
        // 检查用户名是否已存在
        User existingUser = userMapper.selectByUsername(request.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setStatus(request.getStatus() != null ? request.getStatus() : Constants.STATUS_ENABLED);
        user.setCreateBy("admin");
        userMapper.insert(user);
        
        // 分配角色
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            for (Long roleId : request.getRoleIds()) {
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            }
        }
    }
    
    @Override
    @Transactional
    public void updateUser(Long id, UserUpdateRequest request) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 更新用户信息
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAvatar(request.getAvatar());
        user.setUpdateBy("admin");
        userMapper.updateById(user);
        
        // 更新角色
        if (request.getRoleIds() != null) {
            userRoleMapper.deleteByUserId(id);
            for (Long roleId : request.getRoleIds()) {
                UserRole userRole = new UserRole();
                userRole.setUserId(id);
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            }
        }
    }
    
    @Override
    public void deleteUser(Long id) {
        userMapper.deleteById(id);
    }
    
    @Override
    public void updateUserStatus(Long id, Integer status) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setStatus(status);
        user.setUpdateBy("admin");
        userMapper.updateById(user);
    }
    
    @Override
    public void resetPassword(Long id, String password) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setPassword(passwordEncoder.encode(password));
        user.setUpdateBy("admin");
        userMapper.updateById(user);
    }
    
    @Override
    public User getByUsername(String username) {
        return userMapper.selectByUsername(username);
    }
    
    @Override
    public List<String> getUserPermissions(Long userId) {
        return userMapper.selectPermissionsByUserId(userId);
    }
    
    @Override
    public LoginResponse.UserInfo getCurrentUserInfo() {
        // 从 SecurityContext 获取当前用户名
        String username = SecurityUtils.getUsername();
        if (username == null) {
            throw new RuntimeException("用户未登录");
        }
        
        // 查询用户信息
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 获取角色列表
        List<Role> roles = roleMapper.selectRolesByUserId(user.getId());
        List<String> roleKeys = roles.stream().map(Role::getRoleKey).collect(Collectors.toList());
        
        // 获取权限列表
        List<String> permissions = userMapper.selectPermissionsByUserId(user.getId());
        
        // 构建响应
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setNickname(user.getNickname());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setRoles(roleKeys);
        userInfo.setPermissions(permissions);
        
        return userInfo;
    }
    
    @Override
    public List<MenuTreeResponse> getCurrentUserMenus() {
        // 从 SecurityContext 获取当前用户名
        String username = SecurityUtils.getUsername();
        if (username == null) {
            throw new RuntimeException("用户未登录");
        }
        
        // 查询用户信息
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 获取用户的菜单树
        return menuService.getMenuTreeByUserId(user.getId());
    }
    
    /**
     * 转换为用户响应对象
     */
    private UserResponse convertToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setNickname(user.getNickname());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setAvatar(user.getAvatar());
        response.setStatus(user.getStatus());
        response.setCreateTime(user.getCreateTime());
        
        // 获取用户角色
        List<Role> roles = roleMapper.selectRolesByUserId(user.getId());
        List<UserResponse.RoleInfo> roleInfos = roles.stream().map(role -> {
            UserResponse.RoleInfo roleInfo = new UserResponse.RoleInfo();
            roleInfo.setId(role.getId());
            roleInfo.setRoleName(role.getRoleName());
            roleInfo.setRoleKey(role.getRoleKey());
            return roleInfo;
        }).collect(Collectors.toList());
        response.setRoles(roleInfos);
        
        return response;
    }
}
