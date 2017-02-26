package io.goku.chat.core.codec;

import io.goku.chat.core.transport.ProtocolMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<ProtocolMessage> {

	@Override
	protected void encode(ChannelHandlerContext ctx, ProtocolMessage protocolMessage, ByteBuf out) throws Exception {
		System.out.println("编码消息：" + protocolMessage.toString());
		out.writeBytes(protocolMessage.encode().getOrignalBuffer());
	}
	
}
