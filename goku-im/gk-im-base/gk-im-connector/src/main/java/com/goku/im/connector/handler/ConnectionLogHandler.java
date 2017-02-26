package com.goku.im.connector.handler;

import com.goku.im.net.socket.server.logger.ServerLogger;
import com.goku.im.net.socket.server.logger.ServerLoggerEntity;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 
 * 描述
 *   网络连接日志处理器
 *   
 *   
 * <b>类描述: </b><br/>
 * <b>创 建 人: </b> zhouxj <br/>
 * <b>创建时间: </b> 2017年2月5日 下午5:08:12<br/>
 * <b>修 改 人: </b><br/>
 * <b>修改时间: </b><br/>
 * <b>修改备注: </b><br/>
 * <b>JDK 版本: </b> JDK1.8</br/>
 * 
 * @version 1.0.0<br/>
 */
@Component
public class ConnectionLogHandler extends LoggingHandler implements ServerLogger {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionLogHandler.class);
    
    @Override
    public void info(ServerLoggerEntity entity) {
        logger.info(entity.getMessage());
    }
    
    @Override
    public void warning(ServerLoggerEntity entity) {

    }
    
    @Override
    public void error(ServerLoggerEntity entity) {
        logger.error(entity.getMessage(), entity.getCause());
    }
    
	@Override
	public void debug(ServerLoggerEntity entity) {
		  logger.debug(entity.getMessage());
	}
}