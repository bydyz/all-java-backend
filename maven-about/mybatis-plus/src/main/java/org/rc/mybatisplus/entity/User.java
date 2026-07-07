package org.rc.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类
 * 使用 Lombok 注解简化代码
 * 使用 MyBatis-Plus 注解配置表映射关系
 */
@Data
@TableName("sys_user")  // 指定数据库表名
public class User {
    
    /**
     * 主键 ID
     * 使用 @TableId 注解指定主键策略为自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 年龄
     */
    private Integer age;
    
    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
    
    /**
     * 创建时间
     * 使用 @TableField 注解配置自动填充策略
     * FieldFill.INSERT 表示在插入时自动填充
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     * FieldFill.INSERT_UPDATE 表示在插入和更新时自动填充
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
     * 逻辑删除字段
     * 0-未删除，1-已删除
     * 使用 @TableLogic 注解配置逻辑删除
     */
    @TableLogic
    private Integer deleted;
}