package io.goku.chat.core;

import io.goku.chat.core.transport.DataBuffer;

/**
 * 
 * 序列化父类
 * 
 * <b>类描述: </b><br/>
 * <b>创 建 人: </b> zhouxj <br/>
 * <b>创建时间: </b> 2017年2月5日 下午3:46:56<br/>
 * <b>修 改 人: </b><br/>
 * <b>修改时间: </b><br/>
 * <b>修改备注: </b><br/>
 * <b>JDK 版本: </b> JDK1.8</br/>
 * 
 * @version 1.0.0<br/>
 */
public interface IMessageSerializerIntf extends IMessageSerializer {
	
	public DataBuffer encode();
	
	public void decode(DataBuffer buffer);
	
	/**
	 * 获得消息的大小, 包含头
	 * 
	 * @return
	 */
	public int getSize();
}
