package com.rc.model.system;

import com.rc.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_role_menu")
public class SysRoleMenu extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@TableField("role_id")
	private String roleId;

	@TableField("menu_id")
	private String menuId;

}

