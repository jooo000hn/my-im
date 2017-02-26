package com.goku.admin.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by jik on 15/10/28.
 */

public class AdminUser implements Serializable {
	private static final long serialVersionUID = 252809203873291740L;

	/**
	 * 用户id
	 */
	private Integer id;

	/**
	 * 姓名
	 */
	private String username;

	/**
	 * 真实姓名
	 */
	private String realname;

	/**
	 * 登录密码
	 */
	private String password;

	/**
	 * 注册IP
	 */
	private String joinip;

	/**
	 * 状态
	 */
	private int state;

	/**
	 * 是否管理员=1是 =0否
	 */
	private int isadmin;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 手机号码
	 */
	private String mobile;

	/**
	 * 角色集合
	 */
	private String roles;

	/**
	 * 部门ID
	 */
	private int departId;

	/**
	 * 部门路径
	 */
	private int departPath;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;

	/**
	 * 最后修改操作人ID
	 */
	private String lastOperatorId;

	/**
	 * 最后修改操作人姓名
	 */
	private String lastOperatorName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getJoinip() {
		return joinip;
	}

	public void setJoinip(String joinip) {
		this.joinip = joinip;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getIsadmin() {
		return isadmin;
	}

	public void setIsadmin(int isadmin) {
		this.isadmin = isadmin;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public int getDepartId() {
		return departId;
	}

	public void setDepartId(int departId) {
		this.departId = departId;
	}

	public int getDepartPath() {
		return departPath;
	}

	public void setDepartPath(int departPath) {
		this.departPath = departPath;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}
	 
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getLastOperatorId() {
		return lastOperatorId;
	}

	public void setLastOperatorId(String lastOperatorId) {
		this.lastOperatorId = lastOperatorId;
	}

	public String getLastOperatorName() {
		return lastOperatorName;
	}

	public void setLastOperatorName(String lastOperatorName) {
		this.lastOperatorName = lastOperatorName;
	}
}
