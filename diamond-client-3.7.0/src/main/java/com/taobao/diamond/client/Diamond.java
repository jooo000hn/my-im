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
 * �ͻ��˹��ܣ�
 * <ul>
 * <li>����/ֱ��������
 * <li>��������
 * <li>ͬ��������
 * <li>���������ļ�
 * <li>���ػ����ļ���ע��windowsϵͳ�ļ�������
 * <li>����ʱ��⻺����Ч��
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
     * ���ձ������� -> server -> ���ػ�������ȼ���ȡ���á���ʱ��λ�Ǻ��롣
     */
    static public String getConfig(String dataId, String group, long timeoutMs) throws IOException {
        return defaultEnv.getConfig(dataId, group, timeoutMs);
    }
    
    /**
     * ��ȡ���ݽӿڣ��������û�ȡ���ݵ�˳�������������ȣ���<br>
     * feature��������ѡֵ��<br>
     * Constants.GETCONFIG_LOCAL_SERVER_SNAPSHOT(�����ļ�-> ������ -> ���ػ���)<br>
     * Constants.GETCONFIG_LOCAL_SNAPSHOT_SERVER(�����ļ�-> ���ػ��� -> ������)
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
     * ������ѯ�����ص�{@link BatchHttpResult}�а�����{@link ConfigInfoEx}���б������ѯʧ�ܣ����ߴ���Ӧ��ԭ
     * {@link ConfigInfoEx}����ʧ�ܣ��򷵻�ʧ�ܵ�{@link BatchHttpResult}����ѯ�ɹ�ʱ��
     * {@link ConfigInfoEx#getStatus()}
     * ��ʾ�����Ƿ���ڣ�1��ʾ���ڣ�2��ʾ�����ڣ�-1��ʾ��ѯ���ݿⷢ���쳣��ʹ��������Ҫ��Ϊ�˼���ԭ�е�SDK�ӿڡ�
     * 
     * @param dataIds
     *            Ҫ��ѯ��dataId�б�
     * @param group
     *            ����
     * @param timeoutMs
     *            ��ʱʱ��
     * @return {@link BatchHttpResult}����֤��ΪNULL.
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
