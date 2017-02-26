package com.taobao.diamond.manager;

import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.taobao.diamond.client.DiamondConfigure;


/**
 * DiamondManager用于订阅一个且仅有一个DataID对应的配置信息
 * 
 * @author aoqiong
 * 
 */
@Deprecated
public interface DiamondManager {

    /**
     * 添加新的业务数据监听器。
     */
    void addListeners(List<ManagerListener> newListeners);
    
    /**
     * 删除自己的监听器。
     */
    void clearSelfListener();
    
    
    /**
     * 重置业务数据监听器， 即用新的监听器替换原有的业务数据监听器。
     */
    @Deprecated
    public void setManagerListener(ManagerListener managerListener);


    /**
     * 重置业务数据监听器， 即用新的监听器替换原有的业务数据监听器。
     */
    @Deprecated
    public void setManagerListeners(List<ManagerListener> managerListenerList);


    /**
     * 返回该DiamondManager设置的listener列表
     */
    public List<ManagerListener> getManagerListeners();


    /**
     * 同步获取配置信息,优先级：本地容灾目录 -> server
     * 
     * @param timeoutMs
     *            从网络获取配置信息的超时，单位毫秒
     * @return
     */
    public String getConfigureInfomation(long timeoutMs);


    /**
     * 同步获取一份有效的配置信息，优先级：本地容灾目录 -> server -> 本地snapshot。
     * 如果这些途径都无效，则返回null
     */
    public String     getAvailableConfigureInfomation(long timeoutMs);
    public Properties getAvailablePropertiesConfigureInfomation(long timeoutMs);
    
    
    public void setDiamondConfigure(DiamondConfigure diamondConfigure);
    public DiamondConfigure getDiamondConfigure();


    /**
     * 关闭这个DiamondManager
     */
    public void close();


    /**
     * 获取当前正在使用的服务器列表
     */
    public List<String> getServerAddress();


    /**
     * 获取注册监听的所有dataId
     */
    public Set<String> getAllDataId();

}
