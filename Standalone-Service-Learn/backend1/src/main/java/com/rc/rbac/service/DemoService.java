package com.rc.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rc.rbac.common.PageResult;
import com.rc.rbac.dto.request.DemoCreateRequest;
import com.rc.rbac.dto.request.DemoUpdateRequest;
import com.rc.rbac.dto.response.DemoResponse;
import com.rc.rbac.entity.Demo;

import java.util.List;

/**
 * 数据结构示例服务接口
 */
public interface DemoService extends IService<Demo> {
    
    /**
     * 获取分页列表
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param name 名称（可选）
     * @param status 状态（可选）
     * @return 分页结果
     */
    PageResult<DemoResponse> getDemoPage(Integer pageNum, Integer pageSize, String name, Integer status);
    
    /**
     * 获取详情
     * @param id 主键ID
     * @return Demo响应对象
     */
    DemoResponse getDemoById(Long id);
    
    /**
     * 创建
     * @param request 创建请求
     */
    void createDemo(DemoCreateRequest request);
    
    /**
     * 更新
     * @param id 主键ID
     * @param request 更新请求
     */
    void updateDemo(Long id, DemoUpdateRequest request);
    
    /**
     * 删除
     * @param id 主键ID
     */
    void deleteDemo(Long id);
    
    /**
     * 根据名称查询
     * @param name 名称
     * @return Demo列表
     */
    List<DemoResponse> getDemoByName(String name);
    
    /**
     * 查询启用状态的记录
     * @return Demo列表
     */
    List<DemoResponse> getEnabledDemos();
    
    /**
     * 根据年龄范围查询
     * @param minAge 最小年龄
     * @param maxAge 最大年龄
     * @return Demo列表
     */
    List<DemoResponse> getDemoByAgeRange(Integer minAge, Integer maxAge);
}
