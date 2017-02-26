package io.goku.chat.core.transport;

import io.goku.chat.core.IMProtocolSerializer;

/**
 * 底层协议
 * 
 * <b>类描述: </b><br/>
 * <b>创 建 人: </b> zhouxj <br/>
 * <b>创建时间: </b> 2017年2月7日 下午10:16:22<br/>
 * <b>修 改 人: </b><br/>
 * <b>修改时间: </b><br/>
 * <b>修改备注: </b><br/>
 * <b>JDK 版本: </b> JDK1.8</br/>
 * 
 * @version 1.0.0<br/>
 */
public class ProtocolHeader implements IMProtocolSerializer {

	/**
	 * 包长度
	 */
	private int length; // 数据包长度，包括包头长度

	/**
	 * 协议ID
	 */
	private int protocolType;

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * 获取协议类型
	 * 
	 * @return
	 */
	public int getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(int protocolType) {
		this.protocolType = protocolType;
	}

	@Override
	public String toString() {
		return "ProtocolHeader [length=" + length + ", protocolType=" + protocolType + "]";
	}

	/**
	 * 消息头转换为dataBuffer
	 * 
	 * @return
	 */
	public DataBuffer encode() {
		DataBuffer data = new DataBuffer(PROTOCOL_HEADER_LENGTH);
		data.writeInt(length);
		data.writeInt(protocolType);
		return data;
	}
	
	@Override
	public void decode(DataBuffer buffer) {
		length = buffer.readInt();
		protocolType = buffer.readInt();
	}
	
	@Override
	public int getSize() {
		return PROTOCOL_HEADER_LENGTH;
	}

}
