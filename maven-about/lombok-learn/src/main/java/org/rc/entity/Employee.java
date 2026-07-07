package org.rc.entity;

import lombok.*;

/**
 * 演示综合注解使用
 * @Data + @Builder + @ToString + @EqualsAndHashCode
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"salary"})  // 排除 salary 字段
@EqualsAndHashCode(of = {"employeeId"})  // 只用 employeeId 比较
public class Employee {

    private String employeeId;
    private String name;
    private String department;
    private double salary;

    /**
     * 自定义方法 - Lombok 不会影响手写的方法
     */
    public String getMaskedName() {
        if (name == null || name.length() < 2) {
            return name;
        }
        return name.charAt(0) + "*".repeat(name.length() - 1);
    }
}
