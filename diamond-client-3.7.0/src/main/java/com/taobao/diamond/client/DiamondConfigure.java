package com.taobao.diamond.client;

import static com.taobao.diamond.client.impl.DiamondEnv.log;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.taobao.diamond.common.Constants;


/**
 * Diamond客户端的配置信息
 * 
 * @author aoqiong
 * 
 */
public class DiamondConfigure {

    public DiamondConfigure(ClusterType clusterType) {
        initSystemProperty();
    }
    
    private DiamondConfigure() {
        initSystemProperty();
    }

    private void initSystemProperty() {
        // 读取环境变量, 设置轮询时间
        try {
            String pollingIntervaStr = System.getProperty("diamond.polling.interval");
            if (pollingIntervaStr != null) {
                this.pollingIntervalTime = Integer.parseInt(pollingIntervaStr);
            }
            log.warn("diamond polling interval:" + this.pollingIntervalTime + "s");
        }
        catch (Exception e) {
            log.warn("parse system property error - diamond.polling.interval, use default:" + this.pollingIntervalTime
                    + "s," + e.getMessage());
        }

        // 读取环境变量, 设置HTTP单台host的最大连接数
        try {
            String httpMaxConns = System.getProperty("diamond.http.maxhostconn");
            if (httpMaxConns != null) {
                this.maxHostConnections = Integer.parseInt(httpMaxConns);
            }
            log.warn("diamond max host conn:" + this.maxHostConnections);
        }
        catch (Exception e) {
            log.warn("parse system property error - diamond.http.maxhostconn, use default:" + this.maxHostConnections
                    + "," + e.getMessage());
        }

        // 读取环境变量, 设置HTTP总的最大连接数
        try {
            String httpTotalConns = System.getProperty("diamond.http.maxtotalconn");
            if (httpTotalConns != null) {
                this.maxTotalConnections = Integer.parseInt(httpTotalConns);
            }
            log.warn("diamond max total conn:" + this.maxTotalConnections);
        }
        catch (Exception e) {
            log.warn("parse system property error - diamond.http.maxtotalconn, use default:" + this.maxTotalConnections
                    + "," + e.getMessage());
        }
    }


    /**
     * 获取和同一个DiamondServer的最大连接数
     * 
     * @return
     */
    public int getMaxHostConnections() {
        return maxHostConnections;
    }


    /**
     * 设置和同一个DiamondServer的最大连接数<br>
     * 不支持运行时动态更新
     * 
     * @param maxHostConnections
     */
    public void setMaxHostConnections(int maxHostConnections) {
        this.maxHostConnections = maxHostConnections;
    }


    /**
     * 是否允许对陈旧的连接情况进行检测。<br>
     * 如果不检测，性能上会有所提升，但是，会有使用不可用连接的风险导致的IO Exception，默认检测
     * 
     * @return
     */
    public boolean isConnectionStaleCheckingEnabled() {
        return connectionStaleCheckingEnabled;
    }


    /**
     * 设置是否允许对陈旧的连接情况进行检测。<br>
     * 不支持运行时动态更新
     * 
     * @param connectionStaleCheckingEnabled
     */
    public void setConnectionStaleCheckingEnabled(boolean connectionStaleCheckingEnabled) {
        this.connectionStaleCheckingEnabled = connectionStaleCheckingEnabled;
    }


    /**
     * 获取允许的最大的连接数量。
     * 
     * @return
     */
    public int getMaxTotalConnections() {
        return maxTotalConnections;
    }


    /**
     * 设置允许的最大的连接数量。<br>
     * 不支持运行时动态更新
     * 
     * @param maxTotalConnections
     */
    public void setMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
    }


    /**
     * 获取轮询的间隔时间。单位：秒<br>
     * 此间隔时间代表轮询查找一次配置信息的间隔时间，对于容灾相关，请设置短一些；<br>
     * 对于其他不可变的配置信息，请设置长一些
     * 
     * @return
     */
    public int getPollingIntervalTime() {
        return pollingIntervalTime;
    }


    /**
     * 设置轮询的间隔时间。单位：秒<br>
     * 
     * @param pollingIntervalTime
     */
    public void setPollingIntervalTime(int pollingIntervalTime) {
        if (pollingIntervalTime < Constants.POLLING_INTERVAL_TIME) {
            return;
        }
        this.pollingIntervalTime = pollingIntervalTime;
    }


    /**
     * 获取当前支持的所有的DiamondServer域名列表
     * <p>新域名固定为 jmenv.tbsite.net。
     * 
     * @return
     */
    public List<String> getDomainNameList() {
        return Arrays.asList("jmenv.tbsite.net");
    }


    /**
     * 设置当前支持的所有的DiamondServer域名列表.
     * <p>该方法无效，因为新域名固定为 jmenv.tbsite.net。
     */
    @Deprecated
    public void setDomainNameList(List<String> domainNameList) {
        if (null == domainNameList) {
            throw new NullPointerException();
        }
        this.domainNameList = new LinkedList<String>(domainNameList);
    }


    /**
     * 添加一个DiamondServer域名
     * <p>该方法无效，因为新域名固定为 jmenv.tbsite.net。
     */
    @Deprecated
    public void addDomainName(String domainName) {
        if (null == domainName) {
            throw new NullPointerException();
        }
        this.domainNameList.add(domainName);
    }


    /**
     * 添加多个DiamondServer域名
     * <p>该方法无效，因为新域名固定为 jmenv.tbsite.net。
     */
    @Deprecated
    public void addDomainNames(Collection<String> domainNameList) {
        if (null == domainNameList) {
            throw new NullPointerException();
        }
        this.domainNameList.addAll(domainNameList);
    }


    /**
     * 获取DiamondServer的端口号
     * 
     * @return
     */
    public int getPort() {
        return port;
    }


    /**
     * 设置DiamondServer的端口号, 请不要用于生产环境, 仅在需要自行搭建diamond-server进行测试时使用, 默认端口8080<br>
     * 
     * <p>不能改端口号。
     */
    @Deprecated
    public void setPort(int port) {
        this.port = port;
    }


    /**
     * 获取对于一个DiamondServer所对应的查询一个DataID对应的配置信息的Timeout时间<br>
     * 即一次HTTP请求的超时时间<br>
     * 单位：毫秒<br>
     * 
     * @return
     */
    public int getOnceTimeout() {
        return onceTimeout;
    }


    /**
     * 设置对于一个DiamondServer所对应的查询一个DataID对应的配置信息的Timeout时间<br>
     * 单位：毫秒<br>
     * 配置信息越大，请将此值设置得越大
     * 
     * @return
     */
    public void setOnceTimeout(int onceTimeout) {
        this.onceTimeout = onceTimeout;
    }


    /**
     * 获取和DiamondServer的连接建立超时时间。单位：毫秒
     * 
     * @return
     */
    public int getConnectionTimeout() {
        return connectionTimeout;
    }


    /**
     * 设置和DiamondServer的连接建立超时时间。单位：毫秒<br>
     * 
     * @param connectionTimeout
     */
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }


    /**
     * 获取请求超时时间, 单位毫秒
     * 
     * @return
     */
    public int getSoTimeout() {
        return soTimeout;
    }


    /**
     * 设置请求超时时间, 单位毫秒
     * 
     * @param soTimeout
     */
    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    /**
     * 设置探测本地文件的路径<br>
     * <p>不能修改客户端缓存目录。
     */
    @Deprecated
    public void setFilePath(String filePath) {
    }
    
    /**
     * 设置一个DataID的最长等待时间<br>
     * 实际最长等待时间小于receiveWaitTime + min(connectionTimeout, onceTimeout)
     */
    @Deprecated
    public void setReceiveWaitTime(int receiveWaitTime) {
    }
    
    @Deprecated
    public void setTotalTimeout(long totalTimeout) {
    }
    
    // ======================
    
    static final public DiamondConfigure singleton = new DiamondConfigure();
    

    // 异步查询的间隔时间, 单位为秒
    private volatile int pollingIntervalTime = 5;

    // server地址列表
    private volatile List<String> domainNameList = new LinkedList<String>();

    // 以下参数不支持运行后动态更新
    // 是否检查过期连接
    private boolean connectionStaleCheckingEnabled = true;
    // 连接到每台server的最大连接数
    private int maxHostConnections = 20;
    // 总的最大连接数
    private int maxTotalConnections = 50;

    // 请求超时时间, 单位为毫秒
    private int soTimeout = Constants.SO_TIMEOUT;
    // 连接超时时间, 单位为毫秒
    private int connectionTimeout = Constants.CONN_TIMEOUT;
    // 对server的一次请求的超时时间, 单位为毫秒
    private volatile int onceTimeout = Constants.ONCE_TIMEOUT;
    
    // 连接端口
    private int port = Constants.DEFAULT_PORT;
    
}
