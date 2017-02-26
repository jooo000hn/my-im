package com.taobao.diamond.client.impl;

import com.taobao.diamond.client.impl.HttpSimpleClient.HttpResult;

import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.taobao.diamond.client.impl.DiamondEnv.log;
import static com.taobao.diamond.client.impl.DiamondEnvRepo.defaultEnv;

/**
 * 单元化机房需求。
 * 
 * @author JIUREN
 */
public class DiamondUnitSite {

    /**
     * 该client是否在中心机房
     */
    static public boolean isInCenterUnit() {
        return serverMgr_default.getUrlString().equals(serverMgr_center.getUrlString());
    }
    
    /**
     * 切换到中心机房的DIAMOND集群。
     */
    static public void switchToCenterUnit() {
        /*
    	if(MockServer.isTestMode()){
    		defaultEnv.setMockDataSource(mockServerCache.get("center"));
    	} */
    	defaultEnv.initServerManager(serverMgr_center);
    }
    
    /**
     * 切换到自己所在单元机房的DIAMOND集群。
     */
    static public void switchToLocalUnit() {
        /*
    	if(MockServer.isTestMode()){
    		defaultEnv.setMockDataSource(mockServerCache.get("default"));
    	} */
        defaultEnv.initServerManager(serverMgr_default);
    }
    
    /**
     * 得到中心机房的Diamond环境。
     */
    static public DiamondEnv getCenterUnitEnv() {
    	DiamondEnv env = DiamondEnvRepo.getUnitEnv("center");
        /*
    	if(MockServer.isTestMode()){
    		env.setMockDataSource(mockServerCache.get("center"));
    	}
    	*/
        return env;
    }
    
    /**
     * 得到指定单元的Diamond环境。
     */
    static public DiamondEnv getDiamondUnitEnv(String unitName) {
    	DiamondEnv env = DiamondEnvRepo.getUnitEnv(unitName);
        /*
    	if(MockServer.isTestMode()){
    		if(unitName.equals("center"))
    			env.setMockDataSource(mockServerCache.get("center"));
    	} */
        return env;
    }

    
    /**
     * 向所有单元（包括中心单元）发布普通数据。
     * 
     * @throws IOException
     *             向某单元发布失败后抛出异常
     */
    static public void publishToAllUnit(String dataId, String group, String content)
            throws IOException {
        for (DiamondEnv env : getUnitList()) {
            if (!env.publishSingle(dataId, group, content)) {
                throw new IOException("pub fail to unit " + env + ", " + dataId + ", " + group);
            }
        }
    }

    /**
     * 向所有单元（包括中心单元）删除数据。
     * 
     * @throws IOException
     */
    static public void removeToAllUnit(String dataId, String group) throws IOException {
        for (DiamondEnv env : getUnitList()) {
            if (!env.remove(dataId, group)) {
                throw new IOException("rm fail to unit " + env + ", " + dataId + ", " + group);
            }
        }
    }
    
    /**
     * 得到所有单元环境列表。
     * 
     * @throws IOException  获取单元列表时出错.
     */
    static public List<DiamondEnv> getUnitList() throws IOException {
        List<String> unitNameList = null;

        HttpResult httpResult = HttpSimpleClient.httpGet(
                "http://jmenv.tbsite.net:8080/diamond-server/unit-list?nofix=1", null, null, "GBK",
                1000L);

        if (HttpURLConnection.HTTP_OK == httpResult.code) {
            unitNameList = IOUtils.readLines(new StringReader(httpResult.content));
        } else {
            throw new IOException("http code " + httpResult.code + ", msg: " + httpResult.content);
        }

        if (!unitNameList.contains("center")) {
            unitNameList.add("center");
        }
        List<DiamondEnv> envList = new ArrayList<DiamondEnv>(unitNameList.size());
        for (String unitName : unitNameList) {
            DiamondEnv env = DiamondEnvRepo.getUnitEnv(unitName);
            envList.add(env);
        }
        return envList;
    }

    // ==================

    static private ServerListManager serverMgr_default = new ServerListManager();
    static private ServerListManager serverMgr_center = new ServerManager_unitSite("center");

    // 单元名到ServerManager的映射
    static Map<String, ServerListManager> unit2serverMgr = new HashMap<String, ServerListManager>();
    
    //保存default与center的数据的cache
    static public  Map<String, Map<String, CacheData>> mockServerCache = null;
    
    static{
    	//初始化服务器列表
    	serverMgr_default.initServerList();
    	serverMgr_center.initServerList();

        /*
    	//初始化cache
    	mockServerCache = new HashMap<String, Map<String, CacheData>>();
    	mockServerCache.put("default", DiamondEnvRepo.defaultEnv.getMockDataSource());
    	if(DiamondUnitSite.isInCenterUnit())
    		mockServerCache.put("center", DiamondEnvRepo.defaultEnv.getMockDataSource());
    	else
    		mockServerCache.put("center", new HashMap<String, CacheData>());
        */
    }

}

/**
 * 单元化需求对应的ServerManager。 向地址服务器获取列表时增加参数 nofix，表示不做校正。
 */
class ServerManager_unitSite extends ServerListManager {

    public ServerManager_unitSite(String unitName) {
        unit = unitName;
        super.name = unitName;
        getServersUrl = "http://jmenv.tbsite.net:8080/diamond-server/diamond-unit-" + unitName
                + "?nofix=1";
    }

    public synchronized void start() {
        if (isStarted || isFixed) {
            return;
        }

        GetServerListTask getServersTask = new GetServerListTask(getServersUrl);
        for (int i = 0; i < 3 && serverUrls.isEmpty(); ++i) {
            getServersTask.run();
            try {
                Thread.sleep(100L);
            } catch (Exception e) {
            }
        }
        if (serverUrls.isEmpty()) {
            log.warn("no diamond in unit: " + unit);
        }
        
        TimerService.scheduleWithFixedDelay(getServersTask, 0L, 30L, TimeUnit.SECONDS);
        isStarted = true;
    }
    
    //初始化服务器列表，不启动定时任务
    public void initServerList(){    	   	
        GetServerListTask getServersTask = new GetServerListTask(getServersUrl);
        for (int i = 0; i < 3 && serverUrls.isEmpty(); ++i) {
            getServersTask.run();
            try {
                Thread.sleep(100L);
            } catch (Exception e) {
            }
        }
    }
    
    
    @Override
    public String toString() {
        return "ServerManager-unit-" + unit;
    }

    // ==========================
    final String unit;
    final String getServersUrl;
}
