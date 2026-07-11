package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * 配置类
 * 
 * @Configuration - 标记为配置类
 * @Bean - 标记方法返回值为 Spring Bean
 */
@Configuration
public class AppConfig {

    /**
     * CORS 跨域配置
     * 
     * 允许前端跨域请求 API
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // 允许的来源（生产环境应该指定具体域名）
        config.setAllowedOrigins(Arrays.asList("*"));
        
        // 允许的 HTTP 方法
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // 允许的请求头
        config.setAllowedHeaders(Arrays.asList("*"));
        
        // 允许携带凭证（如 Cookie）
        config.setAllowCredentials(false);
        
        // 预检请求的有效期（秒）
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}
