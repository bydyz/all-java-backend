package org.rc.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.rc.mybatisplus.entity.User;

/**
 * 用户 Mapper 接口
 * 继承 BaseMapper 即可获得基本的 CRUD 方法
 * 无需编写 XML 文件
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 继承 BaseMapper 后，自动获得以下方法：
    // insert(T entity) - 插入一条记录
    // deleteById(Serializable id) - 根据 ID 删除
    // deleteBatchIds(Collection<?> idList) - 批量删除
    // updateById(T entity) - 根据 ID 更新
    // selectById(Serializable id) - 根据 ID 查询
    // selectBatchIds(Collection<?> idList) - 批量查询
    // selectList(Wrapper<T> queryWrapper) - 条件查询
    // selectCount(Wrapper<T> queryWrapper) - 条件查询总数
    // selectOne(Wrapper<T> queryWrapper) - 条件查询单条
    // selectPage(Page<T> page, Wrapper<T> queryWrapper) - 分页查询
    
    // 可以在此处添加自定义方法
    // 例如：
    // @Select("SELECT * FROM sys_user WHERE username = #{username}")
    // User selectByUsername(@Param("username") String username);
}