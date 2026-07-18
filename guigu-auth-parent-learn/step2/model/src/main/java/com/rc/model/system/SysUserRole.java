package com.rc.model.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rc.model.base.BaseEntity;
import lombok.Data;

@Data
@TableName("sys_user_role")
public class SysUserRole extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@TableField("role_id")
	private String roleId;

	@TableField("user_id")
	private String userId;
}

