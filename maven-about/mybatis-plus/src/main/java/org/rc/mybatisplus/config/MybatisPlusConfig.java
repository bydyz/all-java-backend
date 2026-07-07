package org.rc.mybatisplus.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus 配置类
 * 配置分页插件等 MyBatis-Plus 相关配置
 */
@Configuration
public class MybatisPlusConfig {
    
    /**
     * 配置 MyBatis-Plus 拦截器
     * 添加分页插件
     * 
     * @return MybatisPlusInterceptor 拦截器实例
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // 创建 MybatisPlusInterceptor 实例
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        
        // 创建分页插件
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor(DbType.H2);
        
        // 设置最大单页限制数量，默认 500 条
        paginationInterceptor.setMaxLimit(500L);
        
        // 添加分页插件到拦截器链
        interceptor.addInnerInterceptor(paginationInterceptor);
        
        return interceptor;
    }
}