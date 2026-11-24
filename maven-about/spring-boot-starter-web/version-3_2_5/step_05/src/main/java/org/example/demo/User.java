package org.example.demo;

import jakarta.persistence.*;
import lombok.Data; // 若使用 Lombok

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 对应 MySQL 自增主键
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String email;
}
