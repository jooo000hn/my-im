package com.goku.im.net.web.server.handler;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author moueimei
 */
public interface WebSocketCloseHandler {
    void handle(ChannelHandlerContext context);
}