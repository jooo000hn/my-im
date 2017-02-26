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
public interface IMProtocolSerializer extends IMessageSerializer {
	/**
	 * 协议包头大小， 8 字节
	 * 
	 */
	public static final int PROTOCOL_HEADER_LENGTH = 8;
	
	/**
	 * 把Request数据结构编码成一个DataBuffer
	 */
	public DataBuffer encode();
	
	/**
	 * 把DataBuffer解包构造一个Response对象，getResponse函数必须在调用完decode函数之后才能得到真实的包
	 */
	public void decode(DataBuffer buffer);
	
	
	public int getSize();

}
