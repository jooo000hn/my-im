package io.goku.chat.core.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.goku.chat.core.IMProtocolSerializer;
import io.goku.chat.core.IMSerializer;
import io.goku.chat.core.protocol.IRequest;

/**
 * 接收到数据请求
 *
 * @author Tony
 * @createAt Feb 18, 2015
 */
public class ProtocolMessage implements IRequest {

	private Logger logger = LoggerFactory.getLogger(ProtocolMessage.class);

	protected ProtocolHeader mHeader;
	protected DataBuffer mData;

	public ProtocolMessage() {

	}

	public ProtocolMessage(DataBuffer buffer) {
		decode(buffer);
	}

	public ProtocolHeader getHeader() {
		return mHeader;
	}

	protected void setHeader(ProtocolHeader mHeader) {
		this.mHeader = mHeader;
	}

	public DataBuffer getData() {
		return mData;
	}

	protected void setData(DataBuffer data) {
		this.mData = data;
	}

	/**
	 * 读取对象
	 *
	 * @param entityClass
	 * @param <T>
	 * @return
	 */
	public <T extends IMProtocolSerializer> T readEntity(Class<T> entityClass) {
		try {
			T entity = entityClass.newInstance();
			readEntity(entity);
			return entity;
		} catch (Exception e) {
			logger.error("entity newInstance error!!!", e);
		}
		return null;
	}

	/**
	 * 读取对象
	 *
	 * @param entity
	 * @param <T>
	 * @return
	 */
	public <T extends IMProtocolSerializer> T readEntity(T entity) {
		entity.decode(mData);
		return entity;
	}

	/**
	 * 写入对象
	 *
	 * @param buffer
	 */
	protected void writeData(DataBuffer buffer) {
		if (mData == null) {
			mData = new DataBuffer();
		}
		mData.writeDataBuffer(buffer);
	}

	/**
	 * 写入消息体
	 * 
	 * @param IMSerializer
	 */
	protected void writeBody(IMSerializer IMSerializer) {
		writeData(IMSerializer.encode());
	}

	/**
	 * 转换为DataBuffer对象
	 * 
	 * @return
	 */
	public DataBuffer encode() {
		DataBuffer dataBuffer = new DataBuffer();
		dataBuffer.writeDataBuffer(this.mHeader.encode());
		dataBuffer.writeDataBuffer(this.getData());

		return dataBuffer;
	}

	/**
	 * | ProtocolHeader | DataBuffer | Length | getProtocolType | Actual Content
	 * |
	 * 
	 * @param buffer
	 */
	public void decode(DataBuffer buffer) {
		if (buffer != null) {
			try {
				// length
				// int length = buffer.readInt();
				mHeader = new ProtocolHeader();
				// mHeader.setLength(length);

				// header
				mHeader.decode(buffer);
				// data
				mData = buffer.readDataBuffer();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}

	@Override
	public String toString() {
		return "ProtocolMessage [header=" + mHeader + ", data=" + mData + "]";
	}
	
	@Override
	public int getSize() {
		return mHeader.getSize() + this.getData().getSize();
	}
	
	public <T extends IMSerializer> void fillMessage(ProtocolHeader header, T tDTO) throws Exception {
		if (header != null) {
			setHeader(header);
			writeData(tDTO.encode());
			header.setLength(getSize());
		} else {
			throw new Exception("必须包含消息头!!");
		}

	}

}
