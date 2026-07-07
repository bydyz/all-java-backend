package org.rc.mybatisplus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;

/**
 * Spring Boot 启动类
 * 使用 @SpringBootApplication 注解启动 Spring Boot 应用
 */
@SpringBootApplication
@RestController
public class Application {

    // 使用 CountDownLatch 保持应用运行
    private static CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(Application.class, args);

        // 打印提示信息
        System.out.println("==================================");
        System.out.println("应用已启动，按 Ctrl+C 停止");
        System.out.println("API 接口：");
        System.out.println("  健康检查: GET http://localhost:8080/api/users/health");
        System.out.println("  获取所有用户: GET http://localhost:8080/api/users");
        System.out.println("==================================");

        try {
            // 等待直到 latch 被重置
            latch.await();
        } catch (InterruptedException e) {
            System.out.println("应用正在停止...");
        }

        System.out.println("应用已关闭");
    }

    /**
     * 应用健康检查接口
     */
    @GetMapping("/health")
    public String health() {
        return "Application OK";
    }

    /**
     * 停止应用
     */
    public static void shutdown() {
        System.out.println("收到停止信号，正在关闭应用...");
        latch.countDown();
    }
}
