package com.rc.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// 扫描指定包下的 Mapper 接口，自动注册为 MyBatis 的 Mapper
@MapperScan("com.rc.system.mapper")
public class Step1 {

    public static void main(String[] args) {
        SpringApplication.run(Step1.class, args);
    }
}

