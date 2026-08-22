package com.rc.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rc.rbac.entity.ParamTypeDemo;

/**
 * 接口传参方式演示服务接口
 */
public interface ParamTypeDemoService extends IService<ParamTypeDemo> {

    /**
     * 根据ID获取记录
     */
    ParamTypeDemo getParamTypeDemoById(Long id);
}
