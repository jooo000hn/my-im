package com.goku.im.common.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.goku.chat.core.IMSerializer;
import io.goku.chat.core.transport.DataBuffer;

/**
 * 
 * 请求数据
 * 
 * <b>类描述: </b><br/>
 * <b>创 建 人: </b> zhouxj <br/>
 * <b>创建时间: </b> 2017年2月7日 下午9:53:33<br/>
 * <b>修 改 人: </b><br/>
 * <b>修改时间: </b><br/>
 * <b>修改备注: </b><br/>
 * <b>JDK 版本: </b> JDK1.8</br/>
 * 
 * @version 1.0.0<br/>
 */
public class IMClientRequest extends IMRequest {
	
	private Logger logger = LoggerFactory.getLogger(IMClientRequest.class);
	
	protected ClientMessageHeader mHeader;
	
	public IMClientRequest() {
		
	}

	public IMClientRequest(DataBuffer buffer) {
		decode(buffer);
	}

	public ClientMessageHeader getHeader() {
		return mHeader;
	}

	public void setHeader(ClientMessageHeader header) {
		mHeader = header;
	}
	
	public DataBuffer getData() {
		return super.getData();
	}
	
	public void setData(DataBuffer data) {
		super.setData(data);
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
