package com.goku.im.connector.start;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.goku.im.connector.receiver.MessageReceiveListener;


/**
 * Created by moueimei on 15/11/27.
 * 消息推送队列监听器
 */
@Component
public class PushMessageQueueListenerStart {
    @Autowired
    MessageReceiveListener listener;

    public void start() throws Exception {
        listener.start();
    }
}