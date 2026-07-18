package com.rc.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.rc.system.mapper")
public class Step1 {

    public static void main(String[] args) {
        SpringApplication.run(Step1.class, args);
    }
}

