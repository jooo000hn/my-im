package com.taobao.diamond.client.impl;

import com.taobao.diamond.client.DiamondPublisher;
import com.taobao.diamond.client.DiamondSubscriber;


/**
 * Diamond客户端工厂类，可以产生一个单例的DiamondSubscriber，供所有的DiamondManager共用 不同的集群对应不同的单例
 * 
 * @author aoqiong
 * 
 */
@Deprecated
public class DiamondClientFactory {

    /**
     * 获取单例的diamond集群订阅者
     */
    public synchronized static DiamondSubscriber getSingletonDiamondSubscriber() {
        return DefaultDiamondSubscriber.singleton;
    }

    /**
     * 获取单例的basestone集群订阅者
     */
    public synchronized static DiamondSubscriber getSingletonBasestoneSubscriber() {
        return DefaultDiamondSubscriber.singleton;
    }

    /**
     * 获取单例的basestone集群发布者
     */
    public synchronized static DiamondPublisher getSingletonBasestonePublisher() {
        return DefaultDiamondPublisher.singleton;
    }
}
