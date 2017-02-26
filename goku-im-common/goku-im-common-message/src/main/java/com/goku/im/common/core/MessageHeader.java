package com.goku.im.common.core;

import io.goku.chat.core.IMessageSerializerIntf;
import io.goku.chat.core.transport.DataBuffer;

public class MessageHeader implements IMessageSerializerIntf {
	/**
	 * 协议包头大小， 8 字节
	 * 
	 */
	public static final int PROTOCOL_HEADER_LENGTH = 8;
	/**
	 * 数据包长度，包括包头长度
	 */
	private int length; // 数据包长度，包括包头长度

	/**
	 * 处理类型 SID
	 */
	private short handlerId; // SID

	/**
	 * 命令类型 CID
	 */
	private short commandId; // CID

	public short getHandlerId() {
		return handlerId;
	}

	public void setHandlerId(short handlerId) {
		this.handlerId = handlerId;
	}

	public short getCommandId() {
		return commandId;
	}

	public void setCommandId(short commandId) {
		this.commandId = commandId;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	@Override
	public String toString() {
		return "MessageHeader [length=" + length + ", handlerId=" + handlerId + ", commandId=" + commandId + "]";
	}

	@Override
	public DataBuffer encode() {
		DataBuffer data = new DataBuffer(PROTOCOL_HEADER_LENGTH);
		data.writeInt(PROTOCOL_HEADER_LENGTH);
		data.writeShort(handlerId);
		data.writeShort(commandId);
		return data;
	}
	
	@Override
	public void decode(DataBuffer buffer) {
		this.length = buffer.readInt();
		handlerId = buffer.readShort();
		commandId = buffer.readShort();
	}
	
	@Override
	public int getSize() {
		return PROTOCOL_HEADER_LENGTH;
	}

}
