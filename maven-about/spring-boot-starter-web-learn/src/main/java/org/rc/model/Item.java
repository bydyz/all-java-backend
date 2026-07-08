package org.rc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 搜索结果项
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    
    private Long id;
    
    private String title;
    
    private String description;
    
    private Double price;
}
