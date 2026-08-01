package com.rc.model.system;

import com.rc.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_post")
public class SysPost extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@TableField("post_code")
	private String postCode;

	@TableField("name")
	private String name;

	@TableField("description")
	private String description;

	@TableField("status")
	private Integer status;

}