package com.rc.model.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private String id;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    @TableLogic  //逻辑删除 默认效果 0 没有删除 1 已经删除
    @TableField("is_deleted")
    private Integer isDeleted;

    // exist = false：这个参数告诉 MyBatis-Plus，尽管字段在类中存在，但它并不对应数据库表中的任何列
    // 换句话说，这个字段不会参与数据库的 CRUD 操作（创建、读取、更新、删除）。
    // 临时数据，既不会写入数据库，从数据库读取其所在表时，也不会有它
    @TableField(exist = false)
    private Map<String,Object> param = new HashMap<>();
}
