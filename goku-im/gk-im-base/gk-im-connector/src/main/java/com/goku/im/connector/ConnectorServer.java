package com.goku.im.connector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gkframework.boot.Main;
import com.goku.im.connector.start.PushMessageQueueListenerStart;
import com.goku.im.connector.start.SocketServerStart;

/**
 * Created by moueimei on 15/11/27.
 */
public class ConnectorServer {
	private static final Logger logger = LoggerFactory.getLogger(ConnectorServer.class);

	public static void main(String[] args) {
		try {
			logger.debug("开始启动服务器 ..... ");
			Main.main(new String[] {});
			
			/*********************************
			 * PushQueue Listenner
			 *********************************/
			PushMessageQueueListenerStart listenerStart = new PushMessageQueueListenerStart();
			listenerStart.start();
			logger.debug("开始启动 PushQueue Listenner ..... ");
			/*********************************
			 * Scoket Server
			 *********************************/
			SocketServerStart serverStart = new SocketServerStart();
			serverStart.start();
			logger.debug("开始启动  Scoket Server ..... ");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("socket server start error.", e.getCause());
		}
	}
}