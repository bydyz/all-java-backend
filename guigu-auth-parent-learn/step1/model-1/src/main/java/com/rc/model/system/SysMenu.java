package com.rc.model.system;

import com.rc.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("sys_menu")
public class SysMenu extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@TableField("parent_id")
	private Long parentId;

	@TableField("name")
	private String name;

	@TableField("type")
	private Integer type;

	@TableField("path")
	private String path;

	@TableField("component")
	private String component;

	@TableField("perms")
	private String perms;

	@TableField("icon")
	private String icon;

	@TableField("sort_value")
	private Integer sortValue;

	@TableField("status")
	private Integer status;


	// exist = false：这个参数告诉 MyBatis-Plus，尽管字段在类中存在，但它并不对应数据库表中的任何列
	// 换句话说，这个字段不会参与数据库的 CRUD 操作（创建、读取、更新、删除）。
	// 临时数据，既不会写入数据库，从数据库读取其所在表时，也不会有它
	@TableField(exist = false)
	private List<SysMenu> children;

	// exist = false：这个参数告诉 MyBatis-Plus，尽管字段在类中存在，但它并不对应数据库表中的任何列
	// 换句话说，这个字段不会参与数据库的 CRUD 操作（创建、读取、更新、删除）。
	// 临时数据，既不会写入数据库，从数据库读取其所在表时，也不会有它
	@TableField(exist = false)
	private boolean isSelect;
}

