package com.taobao.diamond.client.impl;

import com.taobao.diamond.client.impl.EventDispatcher.ServerlistChangeEvent;
import com.taobao.diamond.client.impl.HttpSimpleClient.HttpResult;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.taobao.diamond.client.impl.DiamondEnv.log;


/**
 * 启动时和运行时定期获取地址列表。启动时拿不到地址列表，进程退出。
 * 只有一个域名，即jmenv.tbsite.net
 * 
 * 返回的地址迭代器以同机房优先。
 * 
 * @author jiuRen
 */
public class ServerListManager {
    
    public ServerListManager() {
        isFixed = false;
        isStarted = false;
        name = DEFAULT_NAME;
    }

    public ServerListManager(List<String> fixed) {
        isFixed = true;
        isStarted = true;
        serverUrls = new ArrayList<String>(fixed);
        name = DiamondEnvRepo.getWorkerName(fixed.toArray(new String[fixed.size()]));
    }
    
    public synchronized void start() {
        if (isStarted || isFixed) {
            return;
        }

        GetServerListTask getServersTask = new GetServerListTask(ADDRESS_SERVER_URL);
        while (serverUrls.isEmpty()) {
            getServersTask.run();
            try {
                Thread.sleep(1000L);
            } catch (Exception e) {
            }
        }

        TimerService.scheduleWithFixedDelay(getServersTask, 0L, 30L, TimeUnit.SECONDS);
        isStarted = true;
    }
    
    Iterator<String> iterator() {
        if (serverUrls.isEmpty()) {
            log.error("[serverlist] No server address defined!");
        }
        return new ServerAddressIterator(serverUrls);
    }
    

    class GetServerListTask implements Runnable {
        final String url;
        
        GetServerListTask(String url) {
            this.url = url;
        }
        
        @Override
        public void run() {
            try {
                updateIfChanged(getApacheServerList(url));
            } catch (Exception e) {
                log.error("[serverlist] failed to get serverlist, " + e.toString(), e);
            }
        }
    }
    
    private void updateIfChanged(List<String> newList) {
        if (null == newList || newList.isEmpty()) {
            log.warn("apache diamond serverlist is empty!!!");
            return;
        }
        
        if (newList.equals(serverUrls)) { // no change
            return;
        }        
        serverUrls = new ArrayList<String>(newList);
        //LocalConfigInfoProcessor.cleanEnvSnapshot(name);
        LocalConfigInfoProcessor.saveServerlist(name, serverUrls);
        if(env != null && env.agent != null){
		    env.agent.reSetCurrentServerIp();
        }
        EventDispatcher.fireEvent(new ServerlistChangeEvent());
        log.info("[" + toString() + "] updated to " + serverUrls);
    }

    // 从地址服务器拿地址列表，返回NULL表示遇到服务器故障。
    static List<String> getApacheServerList(String url) {
        try {
            HttpResult httpResult = HttpSimpleClient.httpGet(url, null, null, null, 2000);

            if (200 == httpResult.code) {
                List<String> lines = IOUtils.readLines(new StringReader(httpResult.content));
                List<String> result = new ArrayList<String>(lines.size());
                for (String line : lines) {
                    if (null == line || line.trim().isEmpty()) {
                        continue;
                    } else {
                        result.add(line.trim());
                    }
                }
                return result;
            } else {
                log.error("[serverlist] error code " + httpResult.code);
                return null;
            }
        } catch (IOException e) {
            log.error("[serverlist] exception, " + e.toString(), e);
            return null;
        }
    }
    
    //初始化服务器列表
    public void initServerList(){    	
        GetServerListTask getServersTask = new GetServerListTask(ADDRESS_SERVER_URL);
        for (int i = 0; i < 3 && serverUrls.isEmpty(); ++i) {
            getServersTask.run();
            try {
                Thread.sleep(100L);
            } catch (Exception e) {
            }
        }
    }
    
    String getUrlString() {
        return serverUrls.toString();
    }
        
    
    public DiamondEnv getEnv() {
		return env;
	}

	public void setEnv(DiamondEnv env) {
		this.env = env;
	}

	@Override
    public String toString() {
        return "ServerManager-" + (isFixed ? getUrlString() : "default");
    }

    
    // ==========================
    
    static public final String ADDRESS_SERVER_URL = "http://jmenv.tbsite.net:8080/diamond-server/diamond";
    static public final String DEFAULT_NAME = "default";
    
    // 和其他server的连接超时和socket超时
    static final int TIMEOUT = 5000;
    
    final boolean isFixed;
    boolean isStarted = false;
    volatile List<String> serverUrls = new ArrayList<String>();
	String name;// 不同环境的名称
	private DiamondEnv env = null; //对应的DiamondEnv实例	
	

}


/**
 * 对地址列表排序，同机房优先。 
 */
class ServerAddressIterator implements Iterator<String> {

    static class RandomizedServerAddress implements Comparable<RandomizedServerAddress> {
        static Random random = new Random();
        
        String serverIp;
        int priority = 0;
        int seed;
        
        public RandomizedServerAddress(String ip) {
            try {
                this.serverIp = ip;
                this.seed = random.nextInt(32);
                this.priority = calculatePriority();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        int calculatePriority() {
            String clientSite = SiteIp.getSite(DiamondEnv.selfIp);
            String serverSite = SiteIp.getSite(serverIp);
            return StringUtils.equals(clientSite, serverSite) ? 2 : 0;
        }
        
        @Override
        public int compareTo(RandomizedServerAddress other) {
            if (priority != other.priority) {
                return other.priority - priority;
            } else {
                return other.seed - seed;
            }
        }
    }

    public ServerAddressIterator(List<String> source) {
        sorted = new ArrayList<RandomizedServerAddress>();
        for (String address : source) {
            sorted.add(new RandomizedServerAddress(address));
        }
        Collections.sort(sorted);
        iter = sorted.iterator();
    }

    public boolean hasNext() {
        return iter.hasNext();
    }

    public String next() {
        return iter.next().serverIp;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
    
    final List<RandomizedServerAddress> sorted;
    final Iterator<RandomizedServerAddress> iter;
}
