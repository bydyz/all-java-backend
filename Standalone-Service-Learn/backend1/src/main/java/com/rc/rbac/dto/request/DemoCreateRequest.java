package com.rc.rbac.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 数据结构示例创建请求
 */
@Data
public class DemoCreateRequest {
    
    /**
     * 名称（必填）
     */
    @NotBlank(message = "名称不能为空")
    @Size(min = 1, max = 100, message = "名称长度必须在1-100之间")
    private String name;
    
    /**
     * 描述（可选）
     */
    @Size(max = 1000, message = "描述长度不能超过1000")
    private String description;
    
    /**
     * 年龄（可选）
     */
    @Min(value = 0, message = "年龄不能小于0")
    @Max(value = 150, message = "年龄不能大于150")
    private Integer age;
    
    /**
     * 金额（可选）
     */
    @Min(value = 0, message = "金额不能为负数")
    private Long amount;
    
    /**
     * 分数（可选）
     */
    @DecimalMin(value = "0.0", message = "分数不能小于0")
    @DecimalMax(value = "100.0", message = "分数不能大于100")
    private Float score;
    
    /**
     * 价格（可选）
     */
    @DecimalMin(value = "0.0", message = "价格不能小于0")
    private Double price;
    
    /**
     * 高精度数值（可选）
     */
    @DecimalMin(value = "0.0", message = "数值不能小于0")
    private BigDecimal bigDecimal;
    
    /**
     * 是否启用（可选）
     */
    private Boolean enabled;
    
    /**
     * 状态（可选）
     */
    private Integer status;
    
    /**
     * 生日（可选）
     */
    private LocalDate birthday;
}
