package com.goku.im.net.socket.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.goku.im.common.core.IMRequest;
import com.goku.im.common.core.MessageHeader;
import com.goku.im.net.socket.server.context.SocketRequestContext;
import com.goku.im.net.socket.server.context.SocketResponse;
import com.goku.im.net.socket.server.logger.ServerLogger;
import com.goku.im.net.socket.server.logger.ServerLoggerEntity;

import io.goku.chat.core.connetion.ConnectionManager;
import io.goku.chat.core.connetion.IMConnection;
import io.goku.chat.core.handler.AbstractIMHandler;
import io.goku.chat.core.handler.IMHandlerManager;
import io.goku.chat.core.protocol.ProtocolType;
import io.goku.chat.core.transport.DataBuffer;
import io.goku.chat.core.transport.ProtocolHeader;
import io.goku.chat.core.transport.ProtocolMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 
 * 服务器处理
 * 
 * <b>类描述: </b><br/>
 * <b>创 建 人: </b> zhouxj <br/>
 * <b>创建时间: </b> 2017年2月5日 下午11:01:12<br/>
 * <b>修 改 人: </b><br/>
 * <b>修改时间: </b><br/>
 * <b>修改备注: </b><br/>
 * <b>JDK 版本: </b> JDK1.8</br/>
 * 
 * @version 1.0.0<br/>
 */
public class NettySocketServerHandler extends SimpleChannelInboundHandler<IMRequest> {

	private Logger logger = LoggerFactory.getLogger(NettySocketServerHandler.class);

	private final ConnectionManager mConnectionManager = ConnectionManager.getInstance();
	
	private final static int MAX_HEARTBEAT_COUNT = 3;
	private SocketExecuteHandler executor;
	private SocketCloseHandler closer;
	private ServerLogger serverLogger;
	private int heartbeatCount;
	
	public NettySocketServerHandler() {
		super();
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		super.handlerAdded(ctx);

		mConnectionManager.create(ctx);

		logger.info("handlerAdded " + mConnectionManager.count());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		super.handlerRemoved(ctx);

		mConnectionManager.remove(ctx);

		logger.info("handlerRemoved " + mConnectionManager.count());
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, IMRequest request) throws Exception {
		logger.info("messageReceived header:" + request.getHeader());
		
		IMConnection conn = mConnectionManager.find(ctx);
		MessageHeader header = request.getHeader();
		
		AbstractIMHandler handler = IMHandlerManager.getInstance().find(header.getHandlerId());
		if (handler != null) {
			handler.dispatch(conn, request);
		} else {
			logger.warn("Not found handlerId: " + header.getHandlerId());
		}
	}
	
	public void setExecutor(SocketExecuteHandler executor) {
		this.executor = executor;
	}

	public void setCloser(SocketCloseHandler closer) {
		this.closer = closer;
	}

	public void setWebServerLogger(ServerLogger serverLogger) {
		this.serverLogger = serverLogger;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		/// 封装SocketRequestContext对象
		SocketRequestContext context = new SocketRequestContext();
		context.setContext(ctx);
		context.setRequestData(msg);
		context.setAddress(ctx.channel().remoteAddress());
		
		heartbeatCount = 0;
		
		if (msg instanceof ProtocolMessage) {
			ProtocolHeader header = ((ProtocolMessage)msg).getHeader();
			if(header != null){
				switch(header.getProtocolType()){
				case ProtocolType.REQ_CONNECT_SERVER:
				case ProtocolType.RESP_CONNECT_SERVER:
					DataBuffer dataBuffer = ((ProtocolMessage)msg).getData();
					IMRequest request = new IMRequest();
					request.decode(dataBuffer);
					messageReceived(ctx, request);
					break;
					//.....
				 }	
			}
		} else {
			/// 无效操作
		}
		
//		/// 执行业务逻辑处理
//		SocketResponse response = executor.execute(context);
//		if (null != response) {
//			String result = response.toJsonString();
//			/// 返回应答
//			ctx.channel().writeAndFlush(result);
//
//			/// 是否需要关闭通道
//			if (response.needCloseChannel())
//				ctx.channel().close();
//		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			IdleState state = e.state();
			if (state == IdleState.READER_IDLE) {
				heartbeatCount++;
				if (heartbeatCount >= MAX_HEARTBEAT_COUNT) {
					String message = "remote client trigger READ_IDLE event.";
					logInfo(message);
					socketClose(ctx);
				}
			} else if (state == IdleState.WRITER_IDLE) {
			} else if (state == IdleState.ALL_IDLE) {
			}
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		socketClose(ctx);
		String message = "remote client trigger CHANNEL_INACTIVE event.";
		logInfo(message);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		socketClose(ctx);
		String message = "server throw an error: " + cause.getMessage();
		logError(message, cause);
	}

	private void socketClose(ChannelHandlerContext ctx) throws Exception {
		try {
			/*
			 * ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate()).addListener((
			 * future) -> { if (!future.isSuccess()) { //
			 * future.channel().close(); if (null != closer) {
			 * closer.handle(ctx); } } });
			 */
			/// ctx.close();

		} catch (Exception e) {
			String message = "server close socket throw an error: " + e.getMessage();
			logError(message, e);
		}
	}

	private void logInfo(String message) {
		ServerLoggerEntity entity = new ServerLoggerEntity();
		entity.setMessage(message);
		if (null != serverLogger)
			serverLogger.info(entity);
	}

	private void logError(String message, Throwable cause) {
		ServerLoggerEntity entity = new ServerLoggerEntity();
		entity.setMessage(message);
		entity.setCause(cause);
		if (null != serverLogger)
			serverLogger.error(entity);
	}

}
