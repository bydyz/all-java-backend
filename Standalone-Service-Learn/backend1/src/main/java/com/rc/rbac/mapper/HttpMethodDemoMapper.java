package com.rc.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rc.rbac.entity.HttpMethodDemo;
import org.apache.ibatis.annotations.Mapper;

/**
 * HTTP 请求方式演示 Mapper 接口
 */
@Mapper
public interface HttpMethodDemoMapper extends BaseMapper<HttpMethodDemo> {
}
