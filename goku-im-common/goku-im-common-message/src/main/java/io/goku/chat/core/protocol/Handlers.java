package io.goku.chat.core.protocol;

/**
 * @author Tony
 * @createAt Feb 17, 2015
 */
public class Handlers {

	// /**
	// * 路由转发
	// */
	// public static final short ROUTE = 0x1000;
	////
	// /**
	// * 用户
	// */
	// public static final short USER = 0x0001;
	//
	// /**
	// * 消息
	// */
	// public static final short MESSAGE = 0x0002;
	
	///////////////// IMRequest 消息
	/**
	 * 登录请求(由客户端发送)
	 */
	public final static short LOGIN = 0x01;

	/**
	 * 发送消息请求(由客户端发送)
	 */
	public final static short SEND_MSG = 0x02;

	/**
	 * 推送消息请求(由服务端发送)
	 */
	public final static short PUSH_MSG = 0x03;
	
	/**
	 * 推送通知请求(由服务端发送)
	 */
	public final static short PUSH_NOTIFY = 0x04;

//	/**
//	 * 重新连接(由客户端发送)
//	 */
//	public final static short CONNECTION = 0x05;
	/**
	 * 登出请求(由客户端发送)
	 */
	public final static short LOGOUT = 0x06;
	

	///////////////// IMResponse 消息
	/**
	 * 登录回执(由服务端发送)
	 */
	public final static short ACK_LOGIN = -0x01;

	/**
	 * 发送消息回执(由服务端发送)
	 */
	public final static short ACK_SEND_MSG = -0x02;
	
	/**
	 * 推送消息回执(由客户端发送)
	 */
	public final static short ACK_PUSH_MSG = -0x03;
	/**
	 * 推送通知请求(由服务端发送)
	 */
	public final static short ACK_PUSH_NOTIFY = -0x04;
	
//	/**
//	 * 客户端连接应答(由服务端发送)
//	 */
//	public final static short ACK_CONNECT = -0x05;

	/**
	 * 客户端登出应答(由服务端发送)
	 */
	public final static short ACK_LOGOUT = -0x06;
	
}
