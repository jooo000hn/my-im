package com.gk.im.client;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.goku.im.common.core.IMRequest;
import com.goku.im.common.core.MessageHeader;
import com.goku.im.common.message.Commands;

import io.goku.chat.core.protocol.Handlers;
import io.goku.chat.core.protocol.ProtocolType;
import io.goku.chat.core.transport.ProtocolHeader;
import io.goku.chat.core.transport.ProtocolMessage;
import io.goku.chat.route.server.bean.ClientType;
import io.goku.chat.route.server.dto.UserDTO;
import io.goku.chat.route.server.entity.User;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 
 * 描述
 * 
 * <b>类描述: </b><br/>
 * <b>创 建 人: </b> zhouxj <br/>
 * <b>创建时间: </b> 2017年2月5日 下午3:06:39<br/>
 * <b>修 改 人: </b><br/>
 * <b>修改时间: </b><br/>
 * <b>修改备注: </b><br/>
 * <b>JDK 版本: </b> JDK1.8</br/>
 * 
 * @version 1.0.0<br/>
 */
public class ChatClient {
	private Logger logger = LoggerFactory.getLogger(ChatClient.class);
	private final String host;
	private final int port;
	
	public ChatClient() {
		host = "127.0.0.1";
		port = 6000;
	}
	
	public ChatClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public void run() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class)
					.handler(new ChatClientInitializer());

			ChannelFuture future = bootstrap.connect(host, port);
			// awaitUninterruptibly() 等待连接成功
			Channel channel = future.awaitUninterruptibly().channel();
			logger.debug("启动客户端!!!");
			doLogin(channel);

			// future.channel().closeFuture().awaitUninterruptibly();
		} finally {
			// group.shutdownGracefully();
		}
	}
	
	private ProtocolMessage getMessage() throws Exception {
		User user = new User();
		user.setClientType(ClientType.MAC.value());
		user.setAccount("test2");
		user.setPassword("test2");
		user.setAvatarUrl("http:///");
		user.setNickName("john");
		user.setGender(Byte.valueOf("1"));
		user.setClientType(Byte.valueOf((byte) 1));
		user.setUserDetailId(BigInteger.valueOf(2323));

		IMRequest request = new IMRequest();
		
		MessageHeader header = new MessageHeader();
		header.setHandlerId(Handlers.LOGIN);
		header.setCommandId(Commands.LOGIN_REQUEST);
		
		request.fillMessage(header, new UserDTO(user));
		
		ProtocolMessage message = new ProtocolMessage();
		ProtocolHeader protocolHead = new ProtocolHeader();
		protocolHead.setProtocolType(ProtocolType.REQ_CONNECT_SERVER);

		message.fillMessage(protocolHead, request);
		
		return message;
	}
	
	public void doLogin(Channel channel) {
		
		try {
			ProtocolMessage message = getMessage();
			
			if (channel != null) {
				channel.writeAndFlush(message).awaitUninterruptibly();
			}
			
			logger.debug("发送消息 !!!" + message.toString());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 1; i++) {
			new ChatClient().run();
			Thread.sleep(10);
		}
		// ChatClient client = new ChatClient();
		// client.doLogin(null);
	}
}
