package com.taobao.diamond.client.impl;

import com.taobao.diamond.client.impl.HttpSimpleClient.HttpResult;
import com.taobao.diamond.common.Constants;
import com.taobao.diamond.common.GroupKey;
import com.taobao.diamond.md5.MD5;
import com.taobao.diamond.mockserver.MockServer;
import com.taobao.diamond.utils.ContentUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static com.taobao.diamond.client.impl.DiamondEnv.log;
import static com.taobao.diamond.common.Constants.LINE_SEPARATOR;
import static com.taobao.diamond.common.Constants.WORD_SEPARATOR;



public class ClientWorker {
    
    /**
     * 对于404响应码，返回NULL.
     * 
     * @throws IOException  
     */
    static String getServerConfig(DiamondEnv env, String dataId, String group, long readTimeout)
            throws IOException {
        if (StringUtils.isBlank(group)) {
            group = Constants.DEFAULT_GROUP;
        }


        if (MockServer.isTestMode()) {
            return MockServer.getConfigInfo(dataId, group, env);
        }

        HttpResult result = null;
        try {
            List<String> params = Arrays.asList("dataId", dataId, "group", group);
            result = env.agent.httpGet("/config.co", null, params, Constants.ENCODE, readTimeout);
        } catch (IOException e) {
            log.warn("[sub-server] get server config exception, dataId=" + dataId + ", group="
                    + group + ", " + e.toString());
            throw e;
        }

        switch (result.code) {
		case HttpURLConnection.HTTP_OK:
			// if (env == defaultEnv) {
			LocalConfigInfoProcessor.saveSnapshot(env, dataId, group, result.content);
			// }
			return result.content;
		case HttpURLConnection.HTTP_NOT_FOUND:
			// if (env == defaultEnv) {
			LocalConfigInfoProcessor.saveSnapshot(env, dataId, group, null);
			// }
            return null;
        case HttpURLConnection.HTTP_CONFLICT: {
            log.warn("[sub-server-error] data being modified");
            throw new IOException("data being modified");
        }
        default: {
            log.warn("[sub-server-error] error code " + result.code);
            throw new IOException("http code: " + result.code);
        }
        }
    }

    private void checkLocalConfigInfo() {
        for (CacheData cacheData : env.getAllCacheDataSnapshot()) {
            try {
                checkLocalConfig(env, cacheData);
            } catch (Exception e) {
                log.error("get local config info error", e);
            }
        }
        checkListenerMd5(env);
    }
    
    /**
     * 检查本地容灾文件。及时修改是否使用本地容灾标志位，本地容灾时间戳和md5.
     */
    static void checkLocalConfig(DiamondEnv env, CacheData cacheData) {
        final String dataId = cacheData.dataId;
        final String group = cacheData.group;
        File path = LocalConfigInfoProcessor.getFailoverFile(env, dataId, group);

        // 没有 -> 有
        if (!cacheData.isUseLocalConfigInfo() && path.exists()) {
            String content = LocalConfigInfoProcessor.getFailover(env, dataId, group);
            String md5 = MD5.getInstance().getMD5String(content);
            cacheData.setUseLocalConfigInfo(true);
            cacheData.setLocalConfigInfoVersion(path.lastModified());
            cacheData.setContent(content);

            log.warn("[failover-change] file created. " + dataId + ", " + group + ", md5=" + md5
                    + ", content=" + ContentUtils.truncateContent(content));
            return;
        }

        // 有 -> 没有。不通知业务监听器，从server拿到配置后通知。
        if (cacheData.isUseLocalConfigInfo() && !path.exists()) {
            cacheData.setUseLocalConfigInfo(false);

            log.warn("[failover-change] file deleted. " + dataId + ", " + group);
            return;
        }

        // 有变更
        if (cacheData.isUseLocalConfigInfo() && path.exists()
                && cacheData.getLocalConfigInfoVersion() != path.lastModified()) {
            String content = LocalConfigInfoProcessor.getFailover(env, dataId, group);
            String md5 = MD5.getInstance().getMD5String(content);
            cacheData.setUseLocalConfigInfo(true);
            cacheData.setLocalConfigInfoVersion(path.lastModified());
            cacheData.setContent(content);

            log.warn("[failover-change] file changed. " + dataId + ", " + group + ", md5=" + md5
                    + ", content=" + ContentUtils.truncateContent(content));
            return;
        }
    }

	public void checkServerConfigInfo() {
		// for (DiamondEnv env : Diamond.allDiamondEnvs()) {
		checkServerConfigInfo(env);
		// }
	}
    
    static public void checkServerConfigInfo(DiamondEnv env) {
        for (String groupKey : checkUpdateDataIds(env)) {
            String dataId = GroupKey.parseKey(groupKey)[0];
            String group = GroupKey.parseKey(groupKey)[1];
            try {
                String content = getServerConfig(env, dataId, group, 3000L);
                CacheData cache = env.getCache(dataId, group);
                cache.setContent(content);

                log.info("[data-received] dataId=" + dataId + ", group=" + group + ", md5="
                        + cache.getMd5() + ", content=" + ContentUtils.truncateContent(content));
            } catch (IOException ioe) {
                log.warn(ioe.toString(), ioe);
            }
        }

        checkListenerMd5(env);
    }

    /**
     * 从DiamondServer获取值变化了的DataID列表。返回的对象里只有dataId和group是有效的。 保证不返回NULL。
     */
    static List<String> checkUpdateDataIds(DiamondEnv env) {
        /*
        if (MockServer.isTestMode()) {
        	List<String> updateList = new ArrayList<String>();
        	//与DiamondEnv的模拟数据源比较，得出数据变化列表
        	for(CacheData cacheData : env.getAllCacheDataSnapshot()){
        		CacheData mockServerData = env.getMockCache(cacheData.dataId, cacheData.group);
        		if(mockServerData == null || !mockServerData.getMd5().equals(cacheData.getMd5()))
        			updateList.add(GroupKey.getKey(cacheData.dataId, cacheData.group));
        	}
            return updateList;
        } */
        if (MockServer.isTestMode()) {
            // 避免 test mode cpu% 过高
            try {
                Thread.sleep(3000l);
            } catch (InterruptedException e) {}
            List<String> updateList = new ArrayList<String>();
            for(CacheData cacheData : env.getAllCacheDataSnapshot()){
                if(!CacheData.getMd5String(MockServer.getConfigInfo(cacheData.dataId, cacheData.group, env))
                        .equals(cacheData.getMd5())) {
                    updateList.add(GroupKey.getKey(cacheData.dataId, cacheData.group));
                }
            }
            return updateList;
        }


        String probeUpdateString = getProbeUpdateString(env);
        List<String> params = Arrays.asList(Constants.PROBE_MODIFY_REQUEST, probeUpdateString);
        long timeout = TimeUnit.SECONDS.toMillis(30L);

        List<String> headers = Arrays.asList("longPullingTimeout", "" + timeout);

        if (StringUtils.isBlank(probeUpdateString)) {
            return Collections.emptyList();
        }

        try {
            HttpResult result = env.agent.httpPost("/config.co", headers, params, Constants.ENCODE,
                    timeout);

            if (HttpURLConnection.HTTP_OK == result.code) {
                return parseUpdateDataIdResponse(result.content);
            } else {
                log.warn("[check-update] get changed dataId error, HTTP State: " + result.code);
            }
        } catch (IOException e) {
            log.warn("[check-update] get changed dataId exception, " + e.toString());
        }
        return Collections.emptyList();
    }

    /**
     * 获取探测更新的DataID的请求字符串
     */
    static private String getProbeUpdateString(DiamondEnv env) {
        StringBuilder sb = new StringBuilder();
        for (CacheData cacheData : env.getAllCacheDataSnapshot()) {
            if (!cacheData.isUseLocalConfigInfo()) {
                sb.append(cacheData.dataId).append(WORD_SEPARATOR);
                sb.append(cacheData.group).append(WORD_SEPARATOR);
                sb.append(cacheData.getMd5()).append(LINE_SEPARATOR);
            }
        }
        return sb.toString();
    }

    /**
     * 从HTTP响应拿到变化的groupKey。保证不返回NULL。
     */
    static private List<String> parseUpdateDataIdResponse(String response) {
        if (StringUtils.isBlank(response)) {
            return Collections.emptyList();
        }

        try {
            response = URLDecoder.decode(response, "UTF-8");
        } catch (Exception e) {
            log.error("decode modifiedDataIdsString error", e);
        }

        List<String> updateList = new LinkedList<String>();

        for (String dataIdAndGroup : response.split(LINE_SEPARATOR)) {
            if (!StringUtils.isBlank(dataIdAndGroup)) {
                int idx = dataIdAndGroup.indexOf(WORD_SEPARATOR);
                if (idx > 0) {
                    String dataId = dataIdAndGroup.substring(0, idx);
                    String group = dataIdAndGroup.substring(idx + 1);
                    updateList.add(GroupKey.getKey(dataId, group));
                    log.info("[md5-change] " + dataId + ", " + group);
                }
            }
        }
        return updateList;
    }
    
    static void checkListenerMd5(DiamondEnv env) {
        for (CacheData cacheData : env.getAllCacheDataSnapshot()) {
            cacheData.checkListenerMd5();
        }
    }


    ClientWorker(final DiamondEnv env) {
    	this.env = env;
        executor = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("com.taobao.diamond.client.Worker."+ env.serverMgr.name);
                t.setDaemon(true);
                return t;
            }
        });

        executor.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                try {
                    checkLocalConfigInfo();
                    checkServerConfigInfo();
                } catch (Throwable e) {
                    log.error("[sub-error-rotate] rotate check error", e);
                }
            }
        }, 1L, 1L, TimeUnit.MILLISECONDS);
    }

    // =================

    final ScheduledExecutorService executor;
    final DiamondEnv env;
}
