package org.rc.mybatisplus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.rc.mybatisplus.entity.User;
import org.rc.mybatisplus.mapper.UserMapper;
import org.rc.mybatisplus.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户 Service 实现类
 * 继承 ServiceImpl 获取基本实现
 * 实现 UserService 接口中的自定义方法
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    /**
     * 根据用户名查询用户
     * 使用 LambdaQueryWrapper 构建查询条件
     * 
     * @param username 用户名
     * @return 用户对象
     */
    @Override
    public User getByUsername(String username) {
        // 创建 LambdaQueryWrapper，使用 Lambda 表达式避免字段名硬编码
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        
        // 设置查询条件：username 等于传入的 username
        wrapper.eq(User::getUsername, username);
        
        // 使用 selectOne 查询单条记录
        return this.getOne(wrapper);
    }
    
    /**
     * 批量更新用户状态
     * 使用 LambdaUpdateWrapper 构建更新条件
     * 
     * @param ids 用户 ID 列表
     * @param status 状态值
     * @return 是否成功
     */
    @Override
    public boolean updateStatusBatch(List<Long> ids, Integer status) {
        // 创建 LambdaUpdateWrapper
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        
        // 设置更新条件：id 在 ids 列表中
        updateWrapper.in(User::getId, ids);
        
        // 设置要更新的字段：status 设置为传入的 status 值
        updateWrapper.set(User::getStatus, status);
        
        // 使用 update 方法执行更新
        return this.update(updateWrapper);
    }
}