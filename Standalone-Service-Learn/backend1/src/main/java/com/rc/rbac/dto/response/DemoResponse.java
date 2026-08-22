package com.rc.rbac.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * 数据结构示例响应
 */
@Data
public class DemoResponse {
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 字符串类型
     */
    private String name;
    
    /**
     * 文本类型
     */
    private String description;
    
    /**
     * 整数类型
     */
    private Integer age;
    
    /**
     * 长整数类型
     */
    private Long amount;
    
    /**
     * 单精度浮点
     */
    private Float score;
    
    /**
     * 双精度浮点
     */
    private Double price;
    
    /**
     * 高精度数值
     */
    private BigDecimal bigDecimal;
    
    /**
     * 布尔类型
     */
    private Boolean enabled;
    
    /**
     * 状态值
     */
    private Integer status;
    
    /**
     * 日期类型
     */
    private LocalDate birthday;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 创建者
     */
    private String createBy;
    
    /**
     * 更新者
     */
    private String updateBy;
}
