package com.rc.rbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 数据类型演示实体
 */
@Data
@TableName("data_type_demo")
public class DataTypeDemo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 字符串
     */
    private String stringVal;
    
    /**
     * 整数
     */
    private Integer integerVal;
    
    /**
     * 长整数
     */
    private Long longVal;
    
    /**
     * 单精度浮点
     */
    private Float floatVal;
    
    /**
     * 双精度浮点
     */
    private Double doubleVal;
    
    /**
     * 布尔值
     */
    private Boolean booleanVal;
    
    /**
     * 高精度数值
     */
    private BigDecimal decimalVal;
    
    /**
     * 日期
     */
    private LocalDate dateVal;
    
    /**
     * 日期时间
     */
    private LocalDateTime datetimeVal;
    
    /**
     * 列表(JSON)
     */
    private String listVal;
    
    /**
     * 集合(JSON)
     */
    private String setVal;
    
    /**
     * 映射(JSON)
     */
    private String mapVal;
    
    /**
     * 数组(JSON)
     */
    private String arrayVal;
    
    /**
     * 状态 0:禁用 1:启用
     */
    private Integer status;
    
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
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
