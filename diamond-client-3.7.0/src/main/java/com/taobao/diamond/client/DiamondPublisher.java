package com.taobao.diamond.client;

/**
 * 发布者接口
 * 
 * @author leiwen
 * 
 */
@Deprecated
public interface DiamondPublisher {

    void setClusterType(ClusterType clusterType);

    void publish(String dataId, String group, String configInfo, ContentIdentityPattern pattern);

    boolean syncPublish(String dataId, String group, String configInfo, long timeout,
            ContentIdentityPattern pattern);

    void publishAll(String dataId, String group, String configInfo);
    
    boolean syncPublishAll(String dataId, String group, String configInfo, long timeout);
    
    void unpublishAll(String dataId, String group);
    
    boolean syncUnpublishAll(String dataId, String group, long timeout);
    
    DiamondConfigure getDiamondConfigure();
    
    void setDiamondConfigure(DiamondConfigure diamondConfigure);
    
    void start();
    
    void close();
    
    /**
     * 发布单条数据
     * 
     * @param datumId
     *            聚合数据的标识, datumId相同覆盖, 不同追加
     */
    boolean addDatum(String dataId, String group, String datumId, String configInfo, long timeout);

    /**
     * 删除单条数据。
     */
    boolean deleteDatum(String dataId, String group, String datumId, long timeout);

    /**
     * 删除 dataId + group 下面所有的数据条目。
     */
    boolean deleteAllDatums(String dataId, String group, long timeout);
    


}
