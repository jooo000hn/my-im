package com.taobao.diamond.manager;

import com.taobao.diamond.client.ContentIdentityPattern;
import com.taobao.diamond.client.DiamondConfigure;


/**
 * 发布接口
 * 
 * @author leiwen
 * 
 */
@Deprecated
public interface BaseStonePubManager {

    /**
     * 发布或更新单条数据, 异步。指定内容唯一标识的pattern
     */
    void publish(String dataId, String group, String configInfo, ContentIdentityPattern pattern);


    /**
     * 发布或更新单条数据, 同步. 指定内容唯一标识的pattern.  method=syncUpdateConfig
     */
    boolean syncPublish(String dataId, String group, String configInfo, long timeout,
            ContentIdentityPattern pattern);
    
    /**
     * 替换已有的整条配置信息
     */
    void publishAll(String dataId, String group, String configInfo);
    
    /**
     * 替换已有的整条配置信息
     */
    boolean syncPublishAll(String dataId, String group, String configInfo, long timeout);


    /**
     * 删除所有数据条目
     */
    void removeAll(String dataId, String group);

    /**
     * 删除所有数据条目
     */
    boolean syncRemoveAll(String dataId, String group, long timeout);



    /**
     * 获取发布相关的配置
     * 
     * @return
     */
    DiamondConfigure getDiamondConfigure();


    /**
     * 设置发布相关的配置
     * 
     * @param diamondConfigure
     */
    void setDiamondConfigure(DiamondConfigure diamondConfigure);


    /**
     * 关闭发布者
     */
    void close();


    /**
     * 发布或更新单个数据条目
     * 
     * @param datumId
     *            聚合数据的标识, datumId相同覆盖, 不同追加
     */
    boolean publish(String dataId, String group, String datumId, String configInfo, long timeout);

    /**
     * 删除单个数据条目
     */
    boolean unPublish(String dataId, String group, String datumId, long timeout);

    /**
     * 删除 dataId + group 下面所有的数据条目
     */
    boolean unPublishAll(String dataId, String group, long timeout);
}
