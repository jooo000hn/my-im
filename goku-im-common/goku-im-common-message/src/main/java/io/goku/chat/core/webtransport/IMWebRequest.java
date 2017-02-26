package io.goku.chat.core.webtransport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.goku.chat.core.IMSerializer;
import io.goku.chat.core.protocol.IRequest;
import io.goku.chat.core.transport.DataBuffer;
import io.goku.chat.core.transport.ProtocolHeader;

public class IMWebRequest implements IRequest {
	private Logger logger = LoggerFactory.getLogger(IMWebRequest.class);

	protected ProtocolHeader mHeader;
	protected DataBuffer mData;

	public IMWebRequest(ProtocolHeader mHeader, DataBuffer mData) {
		super();
		this.mHeader = mHeader;
		this.mData = mData;
	}

	public IMWebRequest() {

	}

	public ProtocolHeader getHeader() {
		return mHeader;
	}

	public void setHeader(ProtocolHeader header) {
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
}
