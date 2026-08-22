package com.rc.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rc.rbac.entity.DataTypeDemo;
import com.rc.rbac.mapper.DataTypeDemoMapper;
import com.rc.rbac.service.DataTypeDemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 数据类型演示服务实现类
 */
@Service
@RequiredArgsConstructor
public class DataTypeDemoServiceImpl extends ServiceImpl<DataTypeDemoMapper, DataTypeDemo> implements DataTypeDemoService {
    
    private final DataTypeDemoMapper dataTypeDemoMapper;
    
    @Override
    public DataTypeDemo getDataTypeDemoById(Long id) {
        return dataTypeDemoMapper.selectById(id);
    }
}
