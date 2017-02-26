package com.gk.im.client;

import com.gk.im.client.handler.MessageHandler;
import com.gk.im.client.handler.UserHandler;

import io.goku.chat.core.codec.PacketDecoder;
import io.goku.chat.core.codec.PacketEncoder;
import io.goku.chat.core.handler.IMHandlerManager;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ChatClientInitializer extends ChannelInitializer<SocketChannel> {
	
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        
        pipeline.addLast("decoder", new PacketDecoder(8192, 0, 4));
        pipeline.addLast("encoder", new PacketEncoder());
        
        pipeline.addLast("handler", new ChatClientHandler());

        initIMHandler();
    }
    
    private void initIMHandler() {
        IMHandlerManager.getInstance().register(UserHandler.class);
        IMHandlerManager.getInstance().register(MessageHandler.class);
    }
}
