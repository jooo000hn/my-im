package com.taobao.diamond.client.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * DiamondEnv的仓库。
 * 
 * @author JIUREN
 */
public class DiamondEnvRepo {

    static synchronized public List<DiamondEnv> allDiamondEnvs() {
        List<DiamondEnv> envs = new ArrayList<DiamondEnv>(diamondEnvs.values());
        envs.add(defaultEnv);
        return envs;
    }

    static synchronized public DiamondEnv getTargetEnv(String... serverIps) {
        for (int i = 0; i < serverIps.length; ++i) { // check IP list
            serverIps[i] = serverIps[i].trim();
            String ip = serverIps[i];
            try {
                InetAddress.getByName(ip);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }
        String name = getWorkerName(serverIps);
        DiamondEnv env = diamondEnvs.get(name);
        if (null == env) {
            diamondEnvs.put(name, env = new DiamondEnv(serverIps));
        }
        return env;
    }

    /**
     * 得到单元机房的DIAMOND环境。
     * 
     * @param unitName
     *            单元名称
     */
    static synchronized protected DiamondEnv getUnitEnv(String unitName) {
        DiamondEnv env = diamondEnvs.get(unitName);

        if (null != env) {
            return env;
        }

        env = new DiamondEnv(new ServerManager_unitSite(unitName));
        diamondEnvs.put(unitName, env);
        return env;
    }

    static protected String getWorkerName(String... serverIps) {
        StringBuilder sb = new StringBuilder("com.taobao.diamond.client.worker-");
        String split = "";
        for (String serverIp : serverIps) {
            sb.append(split);
            sb.append(serverIp);
            split = "-";
        }
        return sb.toString();
    }

    // ==========================

    static public final DiamondEnv defaultEnv = new DiamondEnv(new ServerListManager());
    
    static private final Map<String, DiamondEnv> diamondEnvs = new HashMap<String, DiamondEnv>();

}
