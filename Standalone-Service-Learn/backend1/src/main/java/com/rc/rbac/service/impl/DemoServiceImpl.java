package com.rc.rbac.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rc.rbac.common.PageResult;
import com.rc.rbac.dto.request.DemoCreateRequest;
import com.rc.rbac.dto.request.DemoUpdateRequest;
import com.rc.rbac.dto.response.DemoResponse;
import com.rc.rbac.entity.Demo;
import com.rc.rbac.mapper.DemoMapper;
import com.rc.rbac.service.DemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据结构示例服务实现类
 */
@Service
@RequiredArgsConstructor
public class DemoServiceImpl extends ServiceImpl<DemoMapper, Demo> implements DemoService {
    
    private final DemoMapper demoMapper;
    
    @Override
    public PageResult<DemoResponse> getDemoPage(Integer pageNum, Integer pageSize, String name, Integer status) {
        LambdaQueryWrapper<Demo> wrapper = new LambdaQueryWrapper<>();
        
        // 条件查询：名称模糊匹配
        if (name != null && !name.isEmpty()) {
            wrapper.like(Demo::getName, name);
        }
        
        // 条件查询：状态精确匹配
        if (status != null) {
            wrapper.eq(Demo::getStatus, status);
        }
        
        // 按创建时间倒序
        wrapper.orderByDesc(Demo::getCreateTime);
        
        // 执行分页查询
        Page<Demo> page = new Page<>(pageNum, pageSize);
        Page<Demo> result = demoMapper.selectPage(page, wrapper);
        
        // 转换为响应对象
        List<DemoResponse> records = result.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        return PageResult.of(result.getTotal(), records, pageNum, pageSize);
    }
    
    @Override
    public DemoResponse getDemoById(Long id) {
        Demo demo = demoMapper.selectById(id);
        if (demo == null) {
            throw new RuntimeException("记录不存在");
        }
        return convertToResponse(demo);
    }
    
    @Override
    @Transactional
    public void createDemo(DemoCreateRequest request) {
        Demo demo = new Demo();
        demo.setName(request.getName());
        demo.setDescription(request.getDescription());
        demo.setAge(request.getAge());
        demo.setAmount(request.getAmount());
        demo.setScore(request.getScore());
        demo.setPrice(request.getPrice());
        demo.setBigDecimal(request.getBigDecimal());
        demo.setEnabled(request.getEnabled() != null ? request.getEnabled() : true);
        demo.setStatus(request.getStatus() != null ? request.getStatus() : 1);
        demo.setBirthday(request.getBirthday());
        demo.setCreateBy("system");
        
        demoMapper.insert(demo);
    }
    
    @Override
    @Transactional
    public void updateDemo(Long id, DemoUpdateRequest request) {
        Demo demo = demoMapper.selectById(id);
        if (demo == null) {
            throw new RuntimeException("记录不存在");
        }
        
        // 仅更新非空字段
        if (request.getName() != null) {
            demo.setName(request.getName());
        }
        if (request.getDescription() != null) {
            demo.setDescription(request.getDescription());
        }
        if (request.getAge() != null) {
            demo.setAge(request.getAge());
        }
        if (request.getAmount() != null) {
            demo.setAmount(request.getAmount());
        }
        if (request.getScore() != null) {
            demo.setScore(request.getScore());
        }
        if (request.getPrice() != null) {
            demo.setPrice(request.getPrice());
        }
        if (request.getBigDecimal() != null) {
            demo.setBigDecimal(request.getBigDecimal());
        }
        if (request.getEnabled() != null) {
            demo.setEnabled(request.getEnabled());
        }
        if (request.getStatus() != null) {
            demo.setStatus(request.getStatus());
        }
        if (request.getBirthday() != null) {
            demo.setBirthday(request.getBirthday());
        }
        
        demo.setUpdateBy("system");
        demoMapper.updateById(demo);
    }
    
    @Override
    public void deleteDemo(Long id) {
        demoMapper.deleteById(id);
    }
    
    @Override
    public List<DemoResponse> getDemoByName(String name) {
        return demoMapper.selectByName(name).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<DemoResponse> getEnabledDemos() {
        return demoMapper.selectEnabledList().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<DemoResponse> getDemoByAgeRange(Integer minAge, Integer maxAge) {
        return demoMapper.selectByAgeRange(minAge, maxAge).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 实体转响应对象
     */
    private DemoResponse convertToResponse(Demo demo) {
        DemoResponse response = new DemoResponse();
        response.setId(demo.getId());
        response.setName(demo.getName());
        response.setDescription(demo.getDescription());
        response.setAge(demo.getAge());
        response.setAmount(demo.getAmount());
        response.setScore(demo.getScore());
        response.setPrice(demo.getPrice());
        response.setBigDecimal(demo.getBigDecimal());
        response.setEnabled(demo.getEnabled());
        response.setStatus(demo.getStatus());
        response.setBirthday(demo.getBirthday());
        response.setCreateTime(demo.getCreateTime());
        response.setUpdateTime(demo.getUpdateTime());
        response.setCreateBy(demo.getCreateBy());
        response.setUpdateBy(demo.getUpdateBy());
        return response;
    }
}
