package com.rc.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rc.rbac.entity.HttpMethodDemo;

/**
 * HTTP 请求方式演示服务接口
 */
public interface HttpMethodDemoService extends IService<HttpMethodDemo> {

    /**
     * 根据ID获取记录
     */
    HttpMethodDemo getHttpMethodDemoById(Long id);
}
