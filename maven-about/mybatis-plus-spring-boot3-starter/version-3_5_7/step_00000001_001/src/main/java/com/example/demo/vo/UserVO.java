package com.example.demo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVO {
    private Long id;
    private String name;
    private Integer age;
    private String email;
    private LocalDateTime createTime;
}
