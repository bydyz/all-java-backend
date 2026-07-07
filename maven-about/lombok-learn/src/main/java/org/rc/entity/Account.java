package org.rc.entity;

import lombok.*;

/**
 * 演示 @Builder 注解
 * 支持链式构建对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private String username;
    private String email;
    private int level;

    // Builder 模式使用示例:
    // Account account = Account.builder()
    //     .username("admin")
    //     .email("admin@example.com")
    //     .level(1)
    //     .build();
}
