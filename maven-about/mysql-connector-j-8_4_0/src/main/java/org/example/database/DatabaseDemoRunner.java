package org.example.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseDemoRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseDemoRunner.class);

    private final DatabaseConnectionDemo connectionDemo;

    public DatabaseDemoRunner(DatabaseConnectionDemo connectionDemo) {
        this.connectionDemo = connectionDemo;
    }

    @Override
    public void run(String... args) {
        logger.info("=== 数据库连接示例 ===");
        
        // 测试数据库连接
        connectionDemo.testConnection();
        
        // 执行简单查询
        connectionDemo.executeSimpleQuery();
        
        // 创建示例表
        connectionDemo.createExampleTable();
        
        logger.info("=== 数据库连接示例完成 ===");
    }
}