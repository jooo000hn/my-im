package com.taobao.diamond.client;

import com.taobao.diamond.client.impl.DiamondEnv;
import com.taobao.diamond.client.impl.DiamondEnvRepo;
import com.taobao.diamond.client.impl.LocalConfigInfoProcessor;
import com.taobao.diamond.client.impl.LogConstants;
import com.taobao.diamond.domain.ConfigInfoEx;
import com.taobao.diamond.manager.ManagerListener;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.taobao.diamond.client.impl.DiamondEnv.log;
import static com.taobao.diamond.client.impl.DiamondEnvRepo.defaultEnv;

/**
 * 客户端功能：
 * <ul>
 * <li>订阅/直接拿数据
 * <li>发布数据
 * <li>同机房优先
 * <li>本地容灾文件
 * <li>本地缓存文件，注意windows系统文件名限制
 * <li>启动时检测缓存有效性
 * 
 * @author jiuRen
 */
public class Diamond {

    static public void addListener(String dataId, String group, ManagerListener listener) {
        defaultEnv.addListeners(dataId, group, Arrays.asList(listener));
    }
    static public void addListeners(String dataId, String group, List<ManagerListener> listeners) {
        defaultEnv.addListeners(dataId, group, listeners);
    }

    static public void removeListener(String dataId, String group, ManagerListener listener) {
        defaultEnv.removeListener(dataId, group, listener);
    }
    
    static public List<ManagerListener> getListeners(String dataId, String group) {
        return defaultEnv.getListeners(dataId, group);
    }
    
    /**
     * 按照本地容灾 -> server -> 本地缓存的优先级获取配置。超时单位是毫秒。
     */
    static public String getConfig(String dataId, String group, long timeoutMs) throws IOException {
        return defaultEnv.getConfig(dataId, group, timeoutMs);
    }
    
    /**
     * 获取数据接口，可以设置获取数据的顺序（容灾配置优先）。<br>
     * feature有两个可选值：<br>
     * Constants.GETCONFIG_LOCAL_SERVER_SNAPSHOT(容灾文件-> 服务器 -> 本地缓存)<br>
     * Constants.GETCONFIG_LOCAL_SNAPSHOT_SERVER(容灾文件-> 本地缓存 -> 服务器)
     */
    static public String getConfig(String dataId, String group, int feature, long timeoutMs) throws IOException{
    	return defaultEnv.getConfig(dataId, group, feature, timeoutMs);
    }

    static public boolean publishSingle(String dataId, String group, String content) {
        return defaultEnv.publishSingle(dataId, group, content);
    }

    static public boolean publishAggr(String dataId, String group, String datumId, String content) {
        return defaultEnv.publishAggr(dataId, group, datumId, content);
    }

    static public boolean remove(String dataId, String group) {
        return defaultEnv.remove(dataId, group);
    }

    static public boolean removeAggr(String dataId, String group, String datumId) {
        return defaultEnv.removeAggr(dataId, group, datumId);
    }

    static public DiamondEnv getTargetEnv(String... serverIps) {
        return DiamondEnvRepo.getTargetEnv(serverIps);
    }
    
    static public List<DiamondEnv> allDiamondEnvs() {
        return DiamondEnvRepo.allDiamondEnvs();
    }

    /**
     * 批量查询，返回的{@link BatchHttpResult}中包含了{@link ConfigInfoEx}的列表。如果查询失败，或者从响应还原
     * {@link ConfigInfoEx}对象失败，则返回失败的{@link BatchHttpResult}。查询成功时，
     * {@link ConfigInfoEx#getStatus()}
     * 表示数据是否存在，1表示存在，2表示不存在，-1表示查询数据库发生异常，使用数字主要是为了兼容原有的SDK接口。
     * 
     * @param dataIds
     *            要查询的dataId列表
     * @param group
     *            组名
     * @param timeoutMs
     *            超时时间
     * @return {@link BatchHttpResult}，保证不为NULL.
     */
    static public BatchHttpResult<ConfigInfoEx> batchQuery(List<String> dataIds, String group,
            long timeoutMs) {
        return defaultEnv.batchQuery(dataIds, group, timeoutMs);
    }

    static public BatchHttpResult<ConfigInfoEx> batchGetConfig(List<String> dataIds, String group,
                                                           long timeoutMs) {
        return defaultEnv.batchGetConfig(dataIds, group, timeoutMs);
    }
    


    static private void initLog() throws Exception {
    	
        final String logPath = LocalConfigInfoProcessor.getLogFile();
        System.out.println("diamond client log path : " + new File(logPath).getAbsolutePath());

        FileAppender appender = new DailyRollingFileAppender();
        appender.setAppend(true);
        appender.setEncoding("GBK");
        appender.setFile(logPath);
        appender.setLayout(new PatternLayout("%d %-5p - %m%n%n"));
        appender.activateOptions();

        log.setLevel(Level.INFO);
        log.setAdditivity(false);
        log.addAppender(appender);
    }

    static private void checkSnapshotValidity() {
        List<String> localServerlist = LocalConfigInfoProcessor.readServerlist(defaultEnv);
        List<String> apacheServerlist = defaultEnv.getServerUrls();
        log.info("[apache-urls] " + apacheServerlist);
        log.info("[cache-urls] " + localServerlist);

        boolean isNotChange = apacheServerlist.equals(localServerlist);
        if (isNotChange) {
            log.info(LogConstants.PREFFIX + "environment ok.");
        } else {
            log.warn(LogConstants.PREFFIX + "environment changed. clear cache.");
            LocalConfigInfoProcessor.cleanAllSnapshot();
            LocalConfigInfoProcessor.saveServerlist(defaultEnv, apacheServerlist);
        }
    }


    // ==========================
    static {
        try {
            //
            initLog();
            //
            checkSnapshotValidity();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
