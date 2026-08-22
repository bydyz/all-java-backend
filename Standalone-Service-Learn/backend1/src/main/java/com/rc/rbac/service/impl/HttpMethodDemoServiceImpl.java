package com.rc.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rc.rbac.entity.HttpMethodDemo;
import com.rc.rbac.mapper.HttpMethodDemoMapper;
import com.rc.rbac.service.HttpMethodDemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * HTTP 请求方式演示服务实现类
 */
@Service
@RequiredArgsConstructor
public class HttpMethodDemoServiceImpl extends ServiceImpl<HttpMethodDemoMapper, HttpMethodDemo> implements HttpMethodDemoService {

    private final HttpMethodDemoMapper httpMethodDemoMapper;

    @Override
    public HttpMethodDemo getHttpMethodDemoById(Long id) {
        return httpMethodDemoMapper.selectById(id);
    }
}
