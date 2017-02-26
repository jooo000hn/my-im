package io.goku.chat.core.webtransport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.goku.chat.core.IMSerializer;
import io.goku.chat.core.protocol.IResponse;
import io.goku.chat.core.transport.DataBuffer;
import io.goku.chat.core.transport.ProtocolHeader;

public class IMWebResponse implements IResponse{
	private Logger logger = LoggerFactory.getLogger(IMWebResponse.class);

	protected ProtocolHeader mHeader;
	protected DataBuffer mData;

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
	 * 写入对象
	 *
	 * @param buffer
	 */
	public void writeData(DataBuffer buffer) {
		if (mData == null) {
			mData = new DataBuffer();
		}
		mData.writeDataBuffer(buffer);
	}

	/**
	 * 写入对象
	 *
	 * @param IMSerializer
	 */
	public void writeEntity(IMSerializer IMSerializer) {
		writeData(IMSerializer.encode());
	}

	/**
	 * 写入消息体
	 * 
	 * @param IMSerializer
	 */
	public void writeBody(IMSerializer IMSerializer) {
		this.writeEntity(IMSerializer);
	}
	
	/**
	 * Length | Header | Actual Content
	 *
	 * @return buffer
	 */
	public DataBuffer encode() {
		try {
			
			DataBuffer buffer = new DataBuffer();
			// header
			// mHeader.setLength(length);
			buffer.writeDataBuffer(mHeader.encode());
			// data
			buffer.writeDataBuffer(mData);

			return buffer;
		} catch (Exception e) {
			logger.error("encode error!!!", e);
			throw new RuntimeException("encode error!!!");
		}
	}

	@Override
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
	public int getSize() {
		return mHeader.getSize() + this.getData().getSize();
	}
}
