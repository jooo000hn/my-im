package com.goku.im.common;

/**
 * 
 * 描述
 * 
 * <b>类描述: </b><br/>
 * <b>创 建 人: </b> zhouxj <br/>
 * <b>创建时间: </b> 2017年2月4日 下午5:18:15<br/>
 * <b>修 改 人: </b><br/>
 * <b>修改时间: </b><br/>
 * <b>修改备注: </b><br/>
 * <b>JDK 版本: </b> JDK1.8</br/>
 * 
 * @version 1.0.0<br/>
 */
public class ServiceStartException extends Exception {
	public ServiceStartException() {
	}

	public ServiceStartException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceStartException(String message) {
		super(message);
	}

	public ServiceStartException(Throwable cause) {
		super(cause);
	}
}