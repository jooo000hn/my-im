package com.goku.im.common.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.goku.chat.core.IMSerializer;
import io.goku.chat.core.protocol.IResponse;
import io.goku.chat.core.transport.DataBuffer;

/**
 * 
 * 应答，发送数据
 * 
 * <b>类描述: </b><br/>
 * <b>创 建 人: </b> zhouxj <br/>
 * <b>创建时间: </b> 2017年2月7日 下午10:00:44<br/>
 * <b>修 改 人: </b><br/>
 * <b>修改时间: </b><br/>
 * <b>修改备注: </b><br/>
 * <b>JDK 版本: </b> JDK1.8</br/>
 * 
 * @version 1.0.0<br/>
 */
public class IMResponse implements IResponse {
	private Logger logger = LoggerFactory.getLogger(IMResponse.class);

	protected MessageHeader mHeader;
	protected DataBuffer mData;

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
