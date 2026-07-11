package org.example.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@Component
public class DatabaseConnectionDemo {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionDemo.class);

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Autowired
    public DatabaseConnectionDemo(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    /**
     * 测试数据库连接
     */
    public void testConnection() {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            logger.info("数据库连接成功！");
            logger.info("数据库产品名称: {}", metaData.getDatabaseProductName());
            logger.info("数据库产品版本: {}", metaData.getDatabaseProductVersion());
            logger.info("驱动程序名称: {}", metaData.getDriverName());
            logger.info("驱动程序版本: {}", metaData.getDriverVersion());
            logger.info("URL: {}", metaData.getURL());
            logger.info("用户名: {}", metaData.getUserName());
        } catch (SQLException e) {
            logger.error("数据库连接失败", e);
        }
    }

    /**
     * 执行简单查询
     */
    public void executeSimpleQuery() {
        try {
            // 查询数据库版本
            String sql = "SELECT VERSION()";
            String version = jdbcTemplate.queryForObject(sql, String.class);
            logger.info("MySQL 版本: {}", version);

            // 查询当前数据库
            sql = "SELECT DATABASE()";
            String database = jdbcTemplate.queryForObject(sql, String.class);
            logger.info("当前数据库: {}", database);
        } catch (Exception e) {
            logger.error("查询执行失败", e);
        }
    }

    /**
     * 创建示例表
     */
    public void createExampleTable() {
        try {
            // 先删除旧表
            String dropSql = "DROP TABLE IF EXISTS users";
            jdbcTemplate.execute(dropSql);
            logger.info("旧表 'users' 已删除");
            
            // 创建新表
            String createSql = """
                CREATE TABLE users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(50) NOT NULL,
                    email VARCHAR(100) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;
            jdbcTemplate.execute(createSql);
            logger.info("示例表 'users' 创建成功");
        } catch (Exception e) {
            logger.error("创建表失败", e);
        }
    }
}