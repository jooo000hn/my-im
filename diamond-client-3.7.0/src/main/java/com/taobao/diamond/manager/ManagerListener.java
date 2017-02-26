package com.taobao.diamond.manager;

import java.util.concurrent.Executor;


/**
 * 客户如果想接收DataID对应的配置信息，需要自己实现一个监听器
 * 
 * @author aoqiong
 * 
 */
public interface ManagerListener {

    public Executor getExecutor();


    /**
     * 接收配置信息
     * 
     * @param configInfo
     */
    public void receiveConfigInfo(final String configInfo);
}
