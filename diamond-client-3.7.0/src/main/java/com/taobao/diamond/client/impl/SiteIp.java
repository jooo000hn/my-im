package com.taobao.diamond.client.impl;

import static com.taobao.diamond.client.impl.DiamondEnv.log;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.taobao.diamond.client.impl.HttpSimpleClient.HttpResult;

/**
 * 根据网络IP和掩码进行匹配的工具类。
 */
public class SiteIp {
    
    public static void main(String[] args) throws Exception {
        String ip = "172.223.1.1";
        
        System.out.println(getSite(ip));
    }

    /**
     * 根据IP计算在哪个机房。如果没有匹配的机房，返回NULL。
     */
    static public String getSite(String ip) {
        if (null == ip2siteMap) {
            return null;
        }
        
        int intip = ip2int(ip);
        for (Map.Entry<int[], String> entry : ip2siteMap.entrySet()) {
            int siteIp = entry.getKey()[0];
            int mask = entry.getKey()[1];
            if ((intip & mask) == (siteIp & mask)) {
                return entry.getValue();
            }
        }
        return null;
    }
    
    static private void getSiteIp() {
        try {
            HttpResult result = HttpSimpleClient.httpGet(SITEIP_URL, null, null, null, 2000);
            if (200 == result.code) {
                List<String> lines = IOUtils.readLines(new StringReader(result.content));
                Map<int[], String> ip2site = new HashMap<int[], String>();
                for (String line : lines) {
                    if (null == line || line.trim().isEmpty()) { // skip empty line
                        continue;
                    }
                    line = line.trim();
                    String[] tokens = line.split("=");
                    String site = tokens[1];
                    String[] ipAndMask = tokens[0].split("/");
                    String ip = ipAndMask[0];
                    String bits = ipAndMask[1];
                    int[] arr = new int[] { ip2int(ip), calculateMask(Integer.parseInt(bits)) };
                    ip2site.put(arr, site);
                }
                ip2siteMap = ip2site;
            } else {
                log.error("[siteip] failed to get siteip, error code "
                        + result.code);
            }
        } catch (IOException e) {
            log.debug("[siteip] exception, " + e.toString(), e);
        }
    }
    
    static private int ip2int(String ip) {
        String[] tokens = ip.split("\\.");
        int v = 0;
        v |= (Integer.parseInt(tokens[0]) << 24);
        v |= (Integer.parseInt(tokens[1]) << 16);
        v |= (Integer.parseInt(tokens[2]) << 8);
        v |= Integer.parseInt(tokens[3]);
        return v;
    }
    
    // 根据掩码位数计算掩码
    static private int calculateMask(int maskBitCount) {
        int v = 0;
        for (int i = 0, shiftCount = 32; i < maskBitCount; ++i) {
            v |= (1 << --shiftCount);
        }
        return v;
    }
    
    // =================
    
    static public final String SITEIP_URL = "http://jmenv.tbsite.net:8080/siteip";
    
    static volatile Map<int[], String> ip2siteMap = null;
    static {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    getSiteIp();
                } catch (Exception e) {
                    log.error("unexpected exception, " + e.toString(), e);
                }
            }
        };
        
        task.run();
        TimerService.scheduleWithFixedDelay(task, 0L, 1L, TimeUnit.HOURS); // 每小时运行一次
    }
}