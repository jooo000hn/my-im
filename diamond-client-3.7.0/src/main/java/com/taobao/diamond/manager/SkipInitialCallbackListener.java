package com.taobao.diamond.manager;

import org.apache.commons.lang.StringUtils;


/**
 * TDDL需求，启动时TDDL主动获取配置并初始化，然后添加监听器，监听器收到初始回调，导致TDDL再一次初始化。
 * 
 * <p>解决办法：可以把主动获取到的配置保存在监听器，第一次收到回调时，跟初始配置比较，如果一样就立即返回。
 * 
 * <p>示例代码：
 * <p><blockquote><pre>
 *     String config = Diamond.getConfig(dataId, group);
 *     // initialization code
 *     
 *     SkipInitialCallbackListener bizListener = new xxx(config);
 *     Diamond.addListener(dataId, group, bizListener);
 * </pre></blockquote>
 * 
 * @author JIUREN
 */
public abstract class SkipInitialCallbackListener implements ManagerListener {

    private final String initialValue;
    private boolean hasCallbacked = false;

    /**
     * 构造函数。传入主动获取到的配置内容。
     */
    public SkipInitialCallbackListener(String initialConfig) {
        initialValue = initialConfig;
    }

    /**
     * 接收配置信息
     * 
     * @param configInfo
     */
    public void receiveConfigInfo(final String configInfo) {
        if (!hasCallbacked) {
            hasCallbacked = true;

            if (StringUtils.equals(initialValue, configInfo)) {
                return;
            }
        }

        receiveConfigInfo0(configInfo);
    }
    
    abstract public void receiveConfigInfo0(String configInfo);
}
