package com.taobao.diamond.client;

import java.util.List;
import java.util.Set;


/**
 * DiamondSubscriber用于订阅持久的文本配置信息。<br>
 * 
 * @author aoqiong
 * 
 */
@Deprecated
public interface DiamondSubscriber extends DiamondClientSub {

    /**
     * 获取一份可用的配置信息，按照<strong>本地文件->diamond服务器</strong>
     * 的顺序获取一份有效的配置信息, 如果所有途径都无法获取一份有效配置信息, 则返回null
     * 
     * @param dataId
     * @param group
     * @param timeout
     * @return
     */
    String getAvailableConfigureInfomation(String dataId, String group, long timeout);
    
    /**
     * 同getAvailableConfigureInfomation。
     */
    String getConfigureInfomation(String dataId, String group, long timeout);

    /**
     * 获取支持的所有的DataID
     */
    Set<String> getDataIds();
    
    List<String> getServerList();
    
}
