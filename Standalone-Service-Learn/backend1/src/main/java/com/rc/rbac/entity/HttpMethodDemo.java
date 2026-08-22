package com.rc.rbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * HTTP 请求方式演示实体
 */
@Data
@TableName("http_method_demo")
public class HttpMethodDemo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 请求方式名称(GET/POST/PUT/DELETE/PATCH/HEAD/OPTIONS)
     */
    private String methodName;

    /**
     * Spring注解(@GetMapping等)
     */
    private String annotation;

    /**
     * 请求方式说明
     */
    private String description;

    /**
     * 是否支持请求体 0:否 1:是
     */
    private Integer requestBody;

    /**
     * 是否幂等 0:否 1:是
     */
    private Integer idempotent;

    /**
     * 状态 0:禁用 1:启用
     */
    private Integer status;

    /**
     * 删除标记 0:未删除 1:已删除
     */
    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
