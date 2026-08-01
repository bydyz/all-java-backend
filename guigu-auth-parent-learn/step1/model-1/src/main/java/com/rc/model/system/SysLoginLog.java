package com.rc.model.system;

import com.rc.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("sys_login_log")
public class SysLoginLog extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@TableField("username")
	private String username;

	@TableField("ipaddr")
	private String ipaddr;

	@TableField("status")
	private Integer status;

	@TableField("msg")
	private String msg;

	@TableField("access_time")
	private Date accessTime;

}