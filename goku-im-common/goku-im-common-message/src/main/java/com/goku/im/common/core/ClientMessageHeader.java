package com.goku.im.common.core;

import io.goku.chat.core.transport.DataBuffer;

/**
 * 本协议控制长度为 8 + 4 + 32 + 8 == 52字节 timestamp 8 字节 random 4 md5 32 version 8 描述
 * 
 * <b>类描述: </b><br/>
 * <b>创 建 人: </b> zhouxj <br/>
 * <b>创建时间: </b> 2017年2月7日 上午9:49:35<br/>
 * <b>修 改 人: </b><br/>
 * <b>修改时间: </b><br/>
 * <b>修改备注: </b><br/>
 * <b>JDK 版本: </b> JDK1.8</br/>
 * 
 * @version 1.0.0<br/>
 */
public class ClientMessageHeader extends MessageHeader {
	
	/**
	 * 协议包头设计
	 */
	public static final int PROTOCOL_HEADER_LENGTH = 8 + 52;

	/**
	 * 时间戳
	 */
	private long timestamp;

	/**
	 * 随机数 random
	 */
	private int random; // random

	/**
	 * 加密字符串
	 */
	private String md5;

	/**
	 * 版本号
	 */
	private String version;

	@Override
	public DataBuffer encode() {
		DataBuffer data = new DataBuffer(PROTOCOL_HEADER_LENGTH);
		data.writeInt(getLength());
		data.writeShort(super.getHandlerId());
		data.writeShort(super.getCommandId());
		data.writeLong(timestamp);
		data.writeInt(random);
		data.writeString(md5);
		data.writeString(version);

		return data;
	}
	
	@Override
	public void decode(DataBuffer buffer) {
		super.setLength(buffer.readInt());
		super.setHandlerId(buffer.readShort());
		super.setCommandId(buffer.readShort());
		timestamp = buffer.readLong();
		random = buffer.readInt();
		md5 = buffer.readString();
		version = buffer.readString();
	}

}
