package io.goku.chat.core.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.goku.im.common.core.IMRequest;

import io.goku.chat.core.connetion.IMConnection;
import io.goku.chat.core.protocol.IRequest;
import io.goku.chat.core.protocol.ProtocolType;
import io.goku.chat.core.transport.ProtocolHeader;
import io.goku.chat.core.transport.ProtocolMessage;

/**
 * 
 * 协议层的消息处理器
 * 
 * <b>类描述: </b><br/>
 * <b>创 建 人: </b> zhouxj <br/>
 * <b>创建时间: </b> 2017年2月8日 上午11:43:43<br/>
 * <b>修 改 人: </b><br/>
 * <b>修改时间: </b><br/>
 * <b>修改备注: </b><br/>
 * <b>JDK 版本: </b> JDK1.8</br/>
 * 
 * @version 1.0.0<br/>
 */
public class ProtocolHandler extends AbstractIMHandler<ProtocolMessage> {

	private Logger logger = LoggerFactory.getLogger(ProtocolHandler.class);
	
	@Override
	public short getId() {
		// return Handlers.USER;
		return -1;
	}
	
	// /**	
	// * 请求连接服务器
	// */
	// public static final int REQ_CONNECT_SERVER_MESSAGE = 0x10;
	// /**
	// * 服务端响应连接请求
	// */
	// public static final int RESP_CONNECT_SERVER_MESSAGE = -0x10;
	//
	// /**
	// * 验证
	// */
	// public static final int REQ_VERIFY_SERVER_MESSAGE = 0x11;
	// /**
	// * 验证响应
	// */
	// public static final int RESP_VERIFY_SERVER_MESSAGE = -0x11;
	//
	// /**
	// * 客户端的请求消息
	// */
	// public static final int REQ_TRANSFER_MESSAGE = 0x21;
	// /**
	// * 客户端的应答消息
	// */
	// public static final int RESP_TRANSFER_MESSAGE = -0x21;
	//
	// /**
	// * 服务器之间的通信消息
	// */
	// public static final int REQ_SERVER_MESSAGE = 0x20;
	// /**
	// * 服务器的应答消息
	// */
	// public static final int RESP_SERVER_MESSAGE = -0x20;
	//
	// /**
	// * 请求断开连接消息
	// */
	// public static final int REQ_DISCONNECT_SERVER_MESSAGE = 0x30;
	// /**
	// * 请求断开的的应答消息
	// */
	// public static final int RESP_DISCONNECT_SERVER_MESSAGE = -0x30;
	//
	// /**
	// * 请求链路检测 TCP心跳(由客户端发送)
	// */
	// public static final int REQ_HEART_BEAT_PING = 0x40;
	// /**
	// * 链路检测的应答消息 TCP心跳应答(由服务端发送)
	// */
	// public static final int RESP_HEART_BEAT_PONG = -0x40;
	//
	// /**
	// * 转发客户端消息到别的服务器，例如地图服务器
	// */
	// public static final int TRANSFER_CLIENT_REQUEST = 0x50;
	// /**
	// * 转发消息的应答消息
	// */
	// public static final int TRANSFER_CLIENT_RESPONSE = -0x50;
	
	@Override
	public void dispatch(IMConnection connection, ProtocolMessage request) {
		ProtocolHeader header = request.getHeader();
		
		switch (header.getProtocolType()) {
		case ProtocolType.REQ_CONNECT_SERVER:
			onReqConnectServerMessage(connection, request);
			break;
		case ProtocolType.RESP_CONNECT_SERVER:
			onRespConnectServerMessage(connection, request);
			break;
		case ProtocolType.REQ_VERIFY_SERVER_MESSAGE:
			onReqConnectServerMessage(connection, request);
			break;
		case ProtocolType.RESP_VERIFY_SERVER_MESSAGE:
			onReqConnectServerMessage(connection, request);
			break;
		case ProtocolType.REQ_TRANSFER_MESSAGE:
			onReqConnectServerMessage(connection, request);
			break;
		case ProtocolType.RESP_TRANSFER_MESSAGE:
			onReqConnectServerMessage(connection, request);
			break;
		case ProtocolType.REQ_SERVER_MESSAGE:
			onReqConnectServerMessage(connection, request);
			break;
		case ProtocolType.RESP_SERVER_MESSAGE:
			onReqConnectServerMessage(connection, request);
			break;
		case ProtocolType.REQ_DISCONNECT_SERVER_MESSAGE:
			onReqConnectServerMessage(connection, request);
			break;
		case ProtocolType.RESP_DISCONNECT_SERVER_MESSAGE:
			onReqConnectServerMessage(connection, request);
			break;
		case ProtocolType.REQ_HEART_BEAT_PING:
			onReqConnectServerMessage(connection, request);
			break;
		case ProtocolType.RESP_HEART_BEAT_PONG:
			onReqConnectServerMessage(connection, request);
			break;
		case ProtocolType.TRANSFER_CLIENT_REQUEST:
			onReqConnectServerMessage(connection, request);
			break;
		case ProtocolType.TRANSFER_CLIENT_RESPONSE:
			onReqConnectServerMessage(connection, request);
			break;
		default:
			break;
		}
	}

	private void onReqConnectServerMessage(IMConnection connection, IRequest request) {
		logger.info("onReqConnectServerMessage");
	}

	private void onRespConnectServerMessage(IMConnection connection, IRequest request) {
		logger.info("onRespConnectServerMessage");
	}

}
