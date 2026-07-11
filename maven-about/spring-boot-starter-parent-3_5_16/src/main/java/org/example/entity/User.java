package org.example.entity;

import jakarta.persistence.*;

/**
 * 用户实体类
 * 
 * 使用 JPA 注解映射到数据库表
 * @Entity - 标记为 JPA 实体类，对应数据库表
 * @Table - 指定表名（默认为类名小写）
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * @Id - 标记主键
     * @GeneratedValue - 主键生成策略
     *   GenerationType.IDENTITY - 数据库自增
     *   GenerationType.AUTO - 自动选择
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @Column - 列配置
     * nullable = false - 不允许为空
     * length = 50 - 最大长度 50
     */
    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)  // 枚举存储为字符串
    @Column(length = 20)
    private UserStatus status = UserStatus.ACTIVE;

    /**
     * 枚举类型定义
     */
    public enum UserStatus {
        ACTIVE,    // 激活状态
        INACTIVE,  // 未激活
        DELETED    // 已删除
    }

    /**
     * JPA 要求提供无参构造函数
     */
    public User() {
    }

    /**
     * 全参构造函数，方便创建对象
     */
    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // ====== Getter 和 Setter 方法 ======

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', email='" + email + "'}";
    }
}
