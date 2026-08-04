package com.rc.rbac.common;

import lombok.Data;
import java.io.Serializable;

/**
 * 统一响应结果
 */
@Data
public class Result<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 状态码
     */
    private Integer code;
    
    /**
     * 消息
     */
    private String message;
    
    /**
     * 数据
     */
    private T data;
    
    /**
     * 是否成功
     */
    private Boolean success;
    
    public Result() {
    }
    
    public Result(Integer code, String message, T data, Boolean success) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
    }
    
    /**
     * 成功结果
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null, true);
    }
    
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data, true);
    }
    
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data, true);
    }
    
    /**
     * 失败结果
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null, false);
    }
    
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null, false);
    }
}
