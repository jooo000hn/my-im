package com.gk.im.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.goku.im.common.core.IMRequest;
import com.goku.im.common.core.MessageHeader;

import io.goku.chat.core.connetion.IMConnection;
import io.goku.chat.core.handler.AbstractIMHandler;
import io.goku.chat.core.handler.IMHandlerManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ChatClientHandler extends SimpleChannelInboundHandler<IMRequest> {
	private Logger logger = LoggerFactory.getLogger(ChatClientHandler.class);

	private IMConnection mConnection = null;

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		logger.info("handlerAdded");

		if (mConnection != null) {
			mConnection.close();
			mConnection = null;
		}
		
		mConnection = new IMConnection(0L, ctx);
	}
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		logger.info("handlerRemoved");
		mConnection = null;
	}
	
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, IMRequest request) throws Exception {
		logger.info("messageReceived");
		
		MessageHeader header = request.getHeader();
		AbstractIMHandler handler = IMHandlerManager.getInstance().find(header.getHandlerId());
		
		if (handler != null) {
			handler.dispatch(mConnection, request);
		} else {
			logger.warn("Not found handlerId: " + header.getHandlerId());
		}
	}

}
