package com.goku.im.net.socket.server.handler;

import com.goku.im.net.socket.server.context.SocketResponse;

/**
 * 
 * 描述
 * 
 * <b>类描述: </b><br/>
 * <b>创 建 人: </b> zhouxj <br/>
 * <b>创建时间: </b> 2017年2月8日 上午10:35:44<br/>
 * <b>修 改 人: </b><br/>
 * <b>修改时间: </b><br/>
 * <b>修改备注: </b><br/>
 * <b>JDK 版本: </b> JDK1.8</br/>
 * 
 * @version 1.0.0<br/>
 */
public interface ReturnHelperHandler {
	/**
	 * 成功
	 *
	 * @param action
	 *            动作
	 * @param info
	 *            数据
	 * @return
	 */
	public SocketResponse success(String action, Object info);

	/**
	 * 成功
	 *
	 * @param action
	 *            动作
	 * @param info
	 *            数据
	 * @return
	 */
	public SocketResponse success(String action, Object info, boolean needCloseChannel);
	
	/**
	 * 缺少必要参数
	 *
	 * @param action
	 * @return
	 */
	public SocketResponse lostNecessaryParameter(String action);
	
	/**
	 * 参数格式错误
	 *
	 * @param action
	 * @return
	 */
	public SocketResponse parameterFormatError(String action);
	
	/**
	 * 无效操作
	 *
	 * @param action
	 * @return
	 */
	public SocketResponse invalidOperation(String action);

	/**
	 * 非法请求
	 *
	 * @param action
	 * @return
	 */
	public SocketResponse invalidRequest(String action);

	/**
	 * 服务端错误
	 *
	 * @param action
	 * @return
	 */
	public SocketResponse ServerError(String action);
}