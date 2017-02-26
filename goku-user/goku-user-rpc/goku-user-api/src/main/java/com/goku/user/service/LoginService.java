package com.goku.user.service;

import java.util.Map;

/**
 * 登录登出接口 User: jys
 */
public interface LoginService {
	
	/**	
	 * 登录
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param appId
	 * @param appKey
	 * @param userType
	 *            用户平台类型
	 * @param ip
	 *            ip
	 * @return Map key：token；userID
	 * @throws Exception
	 */
	public Map<String, String> login(String username, String password, String appId, String appKey, String userType,
			String ip) throws Exception;
	
	/**
	 * 用户登出
	 * 
	 * @param token
	 *            usertoken
	 * @return
	 * @throws Exception
	 */
	public boolean logout(String token) throws Exception;

	/**
	 * token 校验
	 * 
	 * @param token
	 *            usertoken
	 * @return
	 * @throws Exception
	 */
	public boolean validate(String token) throws Exception;

	/**
	 * 清除用户缓存信息
	 * 
	 * @param phone
	 * @return
	 * @throws Exception
	 */
	public boolean delUserCache(String phone) throws Exception;

	/**
	 * 凌晨定时清空登录的记录的数据
	 */
	public void clearLoginCount();
}
