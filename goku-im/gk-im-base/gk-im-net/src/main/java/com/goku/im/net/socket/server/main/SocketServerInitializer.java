package com.goku.im.net.socket.server.main;

import java.util.concurrent.TimeUnit;

import com.goku.im.net.socket.server.conf.ServerConfig;
import com.goku.im.net.socket.server.handler.NettySocketServerHandler;
import com.goku.im.net.socket.server.handler.SocketCloseHandler;
import com.goku.im.net.socket.server.handler.SocketExecuteHandler;
import com.goku.im.net.socket.server.handler.SocketServerHandler;
import com.goku.im.net.socket.server.logger.ServerLogger;

import io.goku.chat.core.codec.PacketDecoder;
import io.goku.chat.core.codec.PacketEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author moueimei
 */
public class SocketServerInitializer extends ChannelInitializer<SocketChannel> {
    private SocketExecuteHandler executor;
    private SocketCloseHandler closer;
    private ServerConfig serverConfig;
    private ServerLogger serverLogger;

    public SocketServerInitializer() {
    }

    public void setExecutor(SocketExecuteHandler executor) {
        this.executor = executor;
    }

    public void setCloser(SocketCloseHandler closer) {
        this.closer = closer;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public void setWebServerLogger(ServerLogger serverLogger) {
        this.serverLogger = serverLogger;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        final ChannelPipeline pipeline = ch.pipeline();
        long readIdleTime = serverConfig.getReadIdleTime();
        long writeIdleTime = serverConfig.getWriteIdleTime();
        long allIdleTime = serverConfig.getAllIdleTime();
        if (readIdleTime > 0 || writeIdleTime > 0 || allIdleTime > 0) {
            IdleStateHandler idleStateHandler = new IdleStateHandler(readIdleTime, writeIdleTime, allIdleTime, TimeUnit.MILLISECONDS);
            pipeline.addLast("idleState", idleStateHandler);
        }

		/*
        LengthFieldBasedFrameDecoder frameDecoder = new LengthFieldBasedFrameDecoder(serverConfig.getMaxDataLength(), 0, 4, 0 ,4);
		LengthFieldPrepender frameEncoder = new LengthFieldPrepender(4, false);
		pipeline.addLast("framedecoder",frameDecoder);
		pipeline.addLast("frameencoder",frameEncoder);
		pipeline.addLast("decoder",new StringDecoder(CharsetUtil.UTF_8));
        pipeline.addLast("encoder",new StringEncoder(CharsetUtil.UTF_8));
		*/
        
//        pipeline.addLast("decoder", new ImBinaryDecoder());
//        pipeline.addLast("encoder", new ImBinaryEncoder());
        //TODO zhouxj
        pipeline.addLast("decoder", new PacketDecoder(8192, 0, 4));
        pipeline.addLast("encoder", new PacketEncoder());
        
//        ChannelInboundHandlerAdapter handler = new SocketServerHandler();
//        if (null != closer)
//            ((SocketServerHandler) handler).setCloser(closer);
//        if (null != executor)
//            ((SocketServerHandler) handler).setExecutor(executor);
//        if (null != serverLogger)
//            ((SocketServerHandler) handler).setWebServerLogger(serverLogger);
//        pipeline.addLast("handler", handler);
        
        
        
        SimpleChannelInboundHandler handler = new NettySocketServerHandler();
      if (null != closer)
          ((NettySocketServerHandler) handler).setCloser(closer);
      if (null != executor)
          ((NettySocketServerHandler) handler).setExecutor(executor);
      if (null != serverLogger)
          ((NettySocketServerHandler) handler).setWebServerLogger(serverLogger);
      
      pipeline.addLast("handler", handler);
    }
}