package com.goku.im.common;

import com.goku.im.exception.InitializeException;

public interface IService {
	/**
	 * 服务的状态
	 */
	public static enum STATUS {
		STATUS_STOP,
		
		STATUS_STARTING,

		STATUS_RUNNING;
	}
	
	/**
	 * 服务的名称
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 初始化服务的配置
	 * 
	 * @param config
	 * @throws InitializeException
	 */
	public void init(ServiceConfig config) throws InitializeException;

	/**
	 * 获取服务的基本配置
	 * 
	 * @return
	 */
	public ServiceConfig getServiceConfigure();
	
	/**
	 * 启动服务
	 * 
	 * @throws ServiceStartException
	 */
	public void start() throws ServiceStartException;
	
	/**
	 * 获取服务的状态
	 * 
	 * @return
	 */
	public STATUS getStatus();
	
	/**
	 * 设置服务的状态
	 * 
	 * @param paramStatus
	 */

	public void setStatus(STATUS paramStatus);
	
	/**
	 * 关闭服务
	 */
	public void shutdown();

}
