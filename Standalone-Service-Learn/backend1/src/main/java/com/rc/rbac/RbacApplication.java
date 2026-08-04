package com.rc.rbac;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * RBAC 权限管理系统启动类
 */
@SpringBootApplication
@MapperScan("com.rc.rbac.mapper")
public class RbacApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(RbacApplication.class, args);
    }
}
