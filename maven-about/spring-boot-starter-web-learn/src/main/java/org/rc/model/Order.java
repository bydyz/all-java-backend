package org.rc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    private Long id;
    
    private String orderNo;
    
    private String status;
    
    private Double amount;
    
    private LocalDateTime createTime;
}
