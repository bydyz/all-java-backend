package com.rc.rbac.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态枚举
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {
    
    DISABLED(0, "禁用"),
    ENABLED(1, "启用");
    
    private final Integer code;
    private final String description;
    
    /**
     * 根据code获取枚举
     */
    public static StatusEnum getByCode(Integer code) {
        for (StatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
