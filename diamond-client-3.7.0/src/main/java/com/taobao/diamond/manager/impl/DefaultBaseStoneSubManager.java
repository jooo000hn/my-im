package com.taobao.diamond.manager.impl;

import java.util.Arrays;
import java.util.List;

import com.taobao.diamond.manager.BaseStoneSubManager;
import com.taobao.diamond.manager.ManagerListener;


@Deprecated
public class DefaultBaseStoneSubManager extends DefaultDiamondManager implements
        BaseStoneSubManager {

    /**
     * 订阅数据的构造方法, 指定监听器
     * 
     * @param dataId
     * @param group
     * @param managerListener
     *            监听器, 运行中接收到数据变化后回调
     */
    public DefaultBaseStoneSubManager(String dataId, String group, ManagerListener managerListener) {
        super(group, dataId, Arrays.asList(managerListener));
    }

    /**
     * 订阅数据的构造方法, 指定监听器列表
     * 
     * @param dataId
     * @param group
     * @param managerListenerList
     *            监听器列表, 运行中接收到数据变化后依次回调列表中的每一个监听器
     */
    public DefaultBaseStoneSubManager(String dataId, String group,
            List<ManagerListener> managerListenerList) {
        super(group, dataId, managerListenerList);
    }

}
