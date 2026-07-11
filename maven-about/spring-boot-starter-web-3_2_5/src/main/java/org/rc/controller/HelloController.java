package org.rc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基础 REST 控制器示例
 * 演示 @RestController 和 @RequestMapping 的基本使用
 */
@RestController
@RequestMapping("/api/hello")
public class HelloController {
    
    /**
     * GET /api/hello
     * 返回简单的问候消息
     */
    @GetMapping
    public String sayHello() {
        return "Hello, Spring Boot Web Starter!";
    }
    
    /**
     * GET /api/hello/greeting
     * 返回带参数的问候消息
     */
    @GetMapping("/greeting")
    public String greeting() {
        return "欢迎学习 Spring Boot Web Starter!";
    }
}
