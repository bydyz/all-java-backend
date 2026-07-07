package org.rc.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * 演示 @Getter/@Setter 注解
 * 自动生成所有字段的 getter 和 setter 方法
 */
@Getter
@Setter
public class User {

    private String name;
    private int age;
    private String email;

    // 使用 @Getter(AccessLevel.NONE) 禁用某个字段的 getter
    @Getter(AccessLevel.NONE)
    private String password;

    // 使用 @Setter(AccessLevel.NONE) 禁用某个字段的 setter
    @Setter(AccessLevel.NONE)
    private String id;

    public User() {
        this.id = String.valueOf(System.currentTimeMillis());
    }
}
