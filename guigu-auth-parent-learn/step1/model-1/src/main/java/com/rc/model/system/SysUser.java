package com.rc.model.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import com.rc.model.base.BaseEntity;
import lombok.Data;

import java.util.List;

@Data
@TableName("sys_user")
public class SysUser extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@TableField("username")
	private String username;

	@TableField("password")
	private String password;

	@TableField("name")
	private String name;

	@TableField("phone")
	private String phone;

	@TableField("head_url")
	private String headUrl;

	@TableField("dept_id")
	private Long deptId;

	@TableField("post_id")
	private Long postId;

	@TableField("description")
	private String description;

	@TableField("status")
	private Integer status;



	// exist = false：这个参数告诉 MyBatis-Plus，尽管字段在类中存在，但它并不对应数据库表中的任何列
	// 换句话说，这个字段不会参与数据库的 CRUD 操作（创建、读取、更新、删除）。
	// 临时数据，既不会写入数据库，从数据库读取其所在表时，也不会有它
	@TableField(exist = false)
	private List<SysRole> roleList;
	//岗位
	@TableField(exist = false)
	private String postName;
	//部门
	@TableField(exist = false)
	private String deptName;
}

