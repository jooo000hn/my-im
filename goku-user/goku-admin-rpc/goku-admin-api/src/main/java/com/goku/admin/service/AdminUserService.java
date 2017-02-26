package com.goku.admin.service;

import com.goku.admin.model.AdminUser;

import java.util.List;

/**
 * 接口 User: moueimei Date: 15/10/23 Version: 1.0
 */
public interface AdminUserService {
	
	/**
	 * 根据userd查询后台用户
	 * 
	 * @param userId
	 *            后台用户ID
	 * @return 后台用户对象
	 */
	public AdminUser findAdminUserByUserId(int userId);
	
	/**
	 * 根据username查询后台用户
	 * 
	 * @param username
	 *            后台用户名
	 * @return 后台用户对象
	 */
	public AdminUser findAdminUserByUserName(String username);

	/**
	 * 根据username查询后台用户密码
	 * 
	 * @param username
	 *            后台用户名
	 * @return 后台用户密码
	 */
	public String findAdminUserPwByUserName(String username);

	/**
	 * 添加管理员
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param phone
	 *            手机号
	 * @param createBy
	 *            来源
	 * @param ip
	 * @return
	 */
	public int addAdmin(String username, String password, String phone, String createBy, String ip);

	/**
	 * 查询管理员用户
	 * 
	 * @param isAdmin
	 *            是否是管理员
	 * @return 管理员用户
	 */
	public List<AdminUser> findByAdmin(boolean isAdmin);

}
