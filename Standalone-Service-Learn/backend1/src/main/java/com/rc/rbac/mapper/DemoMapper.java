package com.rc.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rc.rbac.entity.Demo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 数据结构示例 Mapper 接口
 */
@Mapper
public interface DemoMapper extends BaseMapper<Demo> {
    
    /**
     * 根据名称查询
     * @param name 名称
     * @return Demo列表
     */
    @Select("SELECT * FROM demos WHERE name = #{name} AND deleted = 0")
    List<Demo> selectByName(@Param("name") String name);
    
    /**
     * 查询启用状态的记录
     * @return Demo列表
     */
    @Select("SELECT * FROM demos WHERE status = 1 AND deleted = 0 ORDER BY create_time DESC")
    List<Demo> selectEnabledList();
    
    /**
     * 根据年龄范围查询
     * @param minAge 最小年龄
     * @param maxAge 最大年龄
     * @return Demo列表
     */
    @Select("SELECT * FROM demos WHERE age BETWEEN #{minAge} AND #{maxAge} AND deleted = 0")
    List<Demo> selectByAgeRange(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);
}
