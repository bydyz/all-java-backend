package org.rc.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户实体类 - 演示请求体绑定和数据验证
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    private Long id;
    
    /**
     * 用户名 - 不允许为空
     */
    @NotBlank(message = "用户名不能为空")
    private String name;
    
    /**
     * 邮箱 - 必须是有效邮箱格式
     */
    @Email(message = "邮箱格式不正确")
    private String email;
    
    /**
     * 年龄 - 最小值为 18
     */
    @Min(value = 18, message = "年龄必须大于等于18")
    private int age;
    
    /**
     * 手机号
     */
    private String phone;
}
