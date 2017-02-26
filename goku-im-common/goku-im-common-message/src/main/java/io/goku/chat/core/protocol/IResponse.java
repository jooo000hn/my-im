package io.goku.chat.core.protocol;

import io.goku.chat.core.IMSerializer;
import io.goku.chat.core.transport.DataBuffer;

public interface IResponse extends IMSerializer {
	
	/**
	 * 获取消息大小, 字节数
	 * @return
	 */
	public int getSize();
}
