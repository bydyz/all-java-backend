package com.rc.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rc.rbac.entity.DataTypeDemo;

/**
 * 数据类型演示服务接口
 */
public interface DataTypeDemoService extends IService<DataTypeDemo> {
    
    /**
     * 根据ID获取记录
     * @param id 主键ID
     * @return DataTypeDemo
     */
    DataTypeDemo getDataTypeDemoById(Long id);
}
