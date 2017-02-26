package io.goku.chat.core.connetion;

import java.net.SocketAddress;

import com.goku.im.common.core.IMResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * 
 * 连接封装类，可以对连接发送数据
 * 
 * <b>类描述: </b><br/>
 * <b>创 建 人: </b> zhouxj <br/>
 * <b>创建时间: </b> 2017年2月8日 上午11:41:49<br/>
 * <b>修 改 人: </b><br/>
 * <b>修改时间: </b><br/>
 * <b>修改备注: </b><br/>
 * <b>JDK 版本: </b> JDK1.8</br/>
 * 
 * @version 1.0.0<br/>
 */
public class IMConnection {
	
	public static final AttributeKey<Long> ATTR_CONN_ID = AttributeKey.valueOf("connection_id");
	public static final AttributeKey<Long> ATTR_CONN_UIN = AttributeKey.valueOf("connection_uin");
	
	private long mId;
	private volatile boolean isClosed = false;
	private ChannelHandlerContext mContext;
	private ConnectionCloseListener closeListener;
	private Object requestData;
	private SocketAddress address;

	public Object getRequestData() {
		return requestData;
	}

	public void setRequestData(Object requestData) {
		this.requestData = requestData;
	}

	public SocketAddress getAddress() {
		return address;
	}

	public void setAddress(SocketAddress address) {
		this.address = address;
	}

	public IMConnection(Long id, ChannelHandlerContext ctx) {
		mId = id;
		mContext = ctx;
	}

	public long getId() {
		return mId;
	}

	public long getUin() {
		return mContext.attr(IMConnection.ATTR_CONN_UIN).get();
	}

	public void setUin(long mUIN) {
		mContext.attr(IMConnection.ATTR_CONN_UIN).set(mUIN);
	}

	public boolean validate() {
		if (isClosed()) {
			return false;
		}
		// 发送一个心跳包，同步等待success
		return true;
	}

	public boolean isActive() {
		return mContext != null && mContext.channel().isActive();
	}

	public boolean isClosed() {
		return isClosed || !isActive();
	}

	public void close() {
		if (!isClosed) {
			mContext.channel().close();
			notifyRemoved();
		}
	}

	public void sendResponse(IMResponse resp) {
		if (isActive()) {
			mContext.writeAndFlush(resp);
		}
	}

	public void notifyRemoved() {
		if (closeListener != null) {
			closeListener.onClosed(this);
		}
		isClosed = true;
		mContext = null;
		closeListener = null;
	}

	public void registerCloseListener(ConnectionCloseListener listener) {
		if (closeListener != null) {
			throw new IllegalStateException("Close listener already configured");
		}
		if (isClosed()) {
			listener.onClosed(this);
		} else {
			closeListener = listener;
		}
	}

	public void removeCloseListener(ConnectionCloseListener listener) {
		if (closeListener == listener) {
			closeListener = null;
		}
	}

	public interface ConnectionCloseListener {
		public void onClosed(IMConnection connection);
	}

}
