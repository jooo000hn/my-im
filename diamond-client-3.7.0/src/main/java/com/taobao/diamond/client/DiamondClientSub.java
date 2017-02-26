package com.taobao.diamond.client;

/**
 * Diamond是一个支持持久的可靠的文本配置信息管理中心，用于文本配置信息的订阅。<br>
 * Diamond目前是使用ops发布持久配置信息。<br>
 * Diamond由于使用集中的数据库保存持久的配置信息，配置信息十分安全，并且，Diamond能够使客户永远获取最新的订阅信息。<br>
 * 目前Diamond客户端拥有如下几种方式： <br>
 * 1.主动获取<br>
 * 2.定时获取<br>
 * Diamond客户端还支持相对远程配置信息而言，优先级更高的本地配置配置信息的获取
 * 
 * @author aoqiong
 * 
 */
public interface DiamondClientSub {

    /**
     * 设置diamond配置对象
     * 
     * @param diamondConfigure
     */
    public void setDiamondConfigure(DiamondConfigure diamondConfigure);


    /**
     * 获取diamond配置对象
     * 
     * @return
     */
    public DiamondConfigure getDiamondConfigure();


    /**
     * 启动diamond
     */
    public void start();


    /**
     * 关闭diamond
     */
    public void close();
}
