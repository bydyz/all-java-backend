package com.rc.rbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 接口传参方式演示实体
 */
@Data
@TableName("param_type_demo")
public class ParamTypeDemo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 注解名称
     */
    private String annotation;

    /**
     * 传参方式说明
     */
    private String description;

    /**
     * 参数来源(URL/Header/Cookie/Body/Path)
     */
    private String paramSource;

    /**
     * 使用场景
     */
    private String usageScenario;

    /**
     * 示例
     */
    private String example;

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
