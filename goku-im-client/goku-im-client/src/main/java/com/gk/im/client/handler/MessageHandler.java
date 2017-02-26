package com.gk.im.client.handler;

import com.goku.im.common.core.IMRequest;
import com.goku.im.common.core.IMResponse;
import com.goku.im.common.core.MessageHeader;
import com.goku.im.common.message.Commands;

import io.goku.chat.core.connetion.IMConnection;
import io.goku.chat.core.handler.AbstractIMHandler;
import io.goku.chat.core.protocol.Handlers;
import io.goku.chat.route.server.dto.AckDTO;
import io.goku.chat.route.server.dto.MessageDTO;
import io.goku.chat.route.server.entity.Message;

/**
 * Created by Tony on 2/20/15.
 */
public class MessageHandler extends AbstractIMHandler<IMRequest> {
	
	@Override
	public short getId() {
		return Handlers.MESSAGE;
	}
	
	@Override
	public void dispatch(IMConnection connection, IMRequest request) {
		MessageHeader header = request.getHeader();
		switch (header.getCommandId()) {
		case Commands.USER_MESSAGE_REQUEST:
			receiveMessage(connection, request);
			break;
		case Commands.USER_MESSAGE_SUCCESS:
			onUserMessageSuccess(connection, request);
			break;
		case Commands.ERROR_USER_NOT_EXISTS:
			System.out.println("用户不存在接收不到消息～～");
			break;
		default:
			connection.close();
			break;
		}
	}
	
	private void receiveMessage(IMConnection connection, IMRequest request) {
		MessageDTO messageDTO = request.readEntity(MessageDTO.class);
		Message message = messageDTO.getMessage();

		System.out.println("message: " + message);

		// 回执告诉对方已经收到，如果对方接收不到回执需要重复发送消息，客户端也需要对重复的消息做处理
		IMResponse resp = new IMResponse();
		MessageHeader header = new MessageHeader();
		header.setHandlerId(Handlers.MESSAGE);
		header.setCommandId(Commands.USER_MESSAGE_SUCCESS);

		try {
			resp.fillMessage(header, new AckDTO(message.getFrom(), message.getId()));
			connection.sendResponse(resp);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void onUserMessageSuccess(IMConnection connection, IMRequest request) {
		AckDTO ack = request.readEntity(AckDTO.class);
		System.out.println("onMessageSuccess: " + ack);
	}
}
