package com.rc.rbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * 数据结构示例实体
 * 演示各种数据类型在数据库中的存储
 */
@Data
@TableName("demos")
public class Demo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 字符串类型 - VARCHAR(100)
     */
    private String name;
    
    /**
     * 文本类型 - TEXT
     */
    private String description;
    
    /**
     * 整数类型 - INT
     */
    private Integer age;
    
    /**
     * 长整数类型 - BIGINT
     */
    private Long amount;
    
    /**
     * 单精度浮点 - FLOAT
     */
    private Float score;
    
    /**
     * 双精度浮点 - DOUBLE
     */
    private Double price;
    
    /**
     * 高精度数值 - DECIMAL(10,2)
     */
    private BigDecimal bigDecimal;
    
    /**
     * 布尔类型 - TINYINT(1)
     */
    private Boolean enabled;
    
    /**
     * 状态值 - TINYINT
     */
    private Integer status;
    
    /**
     * 日期类型 - DATE
     */
    private LocalDate birthday;
    
    /**
     * 日期时间类型 - DATETIME
     */
    private LocalDateTime createTime;
    
    /**
     * 删除标记 0:未删除 1:已删除
     */
    @TableLogic
    private Integer deleted;
    
    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
}
