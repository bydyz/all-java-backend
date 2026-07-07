-- 创建用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    age INT,
    status INT DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0,
    CONSTRAINT uk_username UNIQUE (username)
);

-- 插入测试数据
INSERT INTO sys_user (username, password, email, age, status) VALUES 
('zhangsan', '123456', 'zhangsan@example.com', 25, 1),
('lisi', '123456', 'lisi@example.com', 30, 1),
('wangwu', '123456', 'wangwu@example.com', 28, 0);