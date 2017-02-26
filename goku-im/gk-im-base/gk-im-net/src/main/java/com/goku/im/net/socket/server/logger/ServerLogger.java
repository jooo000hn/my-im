package com.goku.im.net.socket.server.logger;

/**
 * 
 * 描述
 *     服务器日志
 *     
 * <b>类描述: </b><br/>
 * <b>创 建 人: </b> zhouxj <br/>
 * <b>创建时间: </b> 2017年2月5日 下午5:09:00<br/>
 * <b>修 改 人: </b><br/>
 * <b>修改时间: </b><br/>
 * <b>修改备注: </b><br/>
 * <b>JDK 版本: </b> JDK1.8</br/>
 * 
 * @version 1.0.0<br/>
 */
public interface ServerLogger {

	public void debug(ServerLoggerEntity entity);

	public void info(ServerLoggerEntity entity);

	public void warning(ServerLoggerEntity entity);

	public void error(ServerLoggerEntity entity);

}