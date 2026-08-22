package com.rc.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rc.rbac.entity.ParamTypeDemo;
import com.rc.rbac.mapper.ParamTypeDemoMapper;
import com.rc.rbac.service.ParamTypeDemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 接口传参方式演示服务实现类
 */
@Service
@RequiredArgsConstructor
public class ParamTypeDemoServiceImpl extends ServiceImpl<ParamTypeDemoMapper, ParamTypeDemo> implements ParamTypeDemoService {

    private final ParamTypeDemoMapper paramTypeDemoMapper;

    @Override
    public ParamTypeDemo getParamTypeDemoById(Long id) {
        return paramTypeDemoMapper.selectById(id);
    }
}
