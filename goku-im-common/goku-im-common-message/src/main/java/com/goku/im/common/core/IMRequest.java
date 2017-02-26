package com.goku.im.common.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.goku.chat.core.IMSerializer;
import io.goku.chat.core.protocol.IRequest;
import io.goku.chat.core.transport.DataBuffer;

/**
 * 接收到数据请求
 *
 * @author Tony
 * @createAt Feb 18, 2015
 */
public class IMRequest implements IRequest {

	private Logger logger = LoggerFactory.getLogger(IMRequest.class);

	protected MessageHeader mHeader;
	protected DataBuffer mData;
	
	public IMRequest() {
		
	}

	public IMRequest(DataBuffer buffer) {
		decode(buffer);
	}

	public MessageHeader getHeader() {
		return mHeader;
	}

	protected void setHeader(MessageHeader header) {
		mHeader = header;
	}

	public DataBuffer getData() {
		return mData;
	}

	public void setData(DataBuffer data) {
		mData = data;
	}

	/**
	 * 读取对象
	 *
	 * @param entityClass
	 * @param <T>
	 * @return
	 */
	public <T extends IMSerializer> T readEntity(Class<T> entityClass) {
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
	public <T extends IMSerializer> T readEntity(T entity) {
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
	 * Length | Header | Actual Content
	 * 
	 * @param buffer
	 */
	public void decode(DataBuffer buffer) {
		if (buffer != null) {
			try {
				// header
				mHeader.decode(buffer);
				// data
				mData = buffer.readDataBuffer();
			} catch (Exception e) {
				logger.error("decode error :" + e.getMessage());
			}
		}
	}

	@Override
	public DataBuffer encode() {
		DataBuffer dataBuffer = new DataBuffer();
		dataBuffer.writeDataBuffer(this.mHeader.encode());
		dataBuffer.writeDataBuffer(this.getData());

		return dataBuffer;
	}

	@Override
	public int getSize() {
		return mHeader.getSize() + this.getData().getSize();
	}

	public <T extends IMSerializer> void fillMessage(MessageHeader header, T tDTO) throws Exception {
		if (header != null) {
			setHeader(header);
			writeData(tDTO.encode());
			header.setLength(getSize());
		} else {
			throw new Exception("必须包含消息头!!");
		}

	}
	
	

}
