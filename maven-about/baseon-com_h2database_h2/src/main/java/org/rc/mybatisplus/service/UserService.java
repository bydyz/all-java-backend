package org.rc.mybatisplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.rc.mybatisplus.entity.User;

import java.util.List;

/**
 * 用户 Service 接口
 * 继承 IService 即可获得通用的业务方法
 */
public interface UserService extends IService<User> {
    // 继承 IService 后，自动获得以下常用方法：
    // save(T entity) - 保存（插入）
    // saveBatch(Collection<T> entityList) - 批量保存
    // removeById(Serializable id) - 根据 ID 删除
    // removeBatchByIds(Collection<?> idList) - 批量删除
    // updateById(T entity) - 根据 ID 更新
    // updateBatchById(Collection<T> entityList) - 批量更新
    // getById(Serializable id) - 根据 ID 查询
    // list() - 查询所有
    // list(Wrapper<T> queryWrapper) - 条件查询
    // page(Page<T> page, Wrapper<T> queryWrapper) - 分页查询
    // count() - 查询总数
    // count(Wrapper<T> queryWrapper) - 条件查询总数
    
    /**
     * 自定义业务方法：根据用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    User getByUsername(String username);
    
    /**
     * 自定义业务方法：批量更新用户状态
     * @param ids 用户 ID 列表
     * @param status 状态值
     * @return 是否成功
     */
    boolean updateStatusBatch(List<Long> ids, Integer status);
}