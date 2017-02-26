package com.taobao.diamond.client.impl;

import com.taobao.diamond.client.Diamond;
import com.taobao.diamond.common.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.taobao.diamond.client.impl.DiamondEnv.log;


/**
 * 本地容灾目录相关。
 */
public class LocalConfigInfoProcessor {

    /**
     * 获取容灾配置内容。NULL表示没有本地文件或抛出异常。
     */
    static public String getFailover(DiamondEnv env, String dataId, String group) {
        File localPath = getFailoverFile(env, dataId, group);
        if (!localPath.exists() || !localPath.isFile()) {
            return null;
        }

        try {
            return readFile(localPath);
        } catch (IOException ioe) {
            log.error("get failover error, " + localPath + ioe.toString());
            return null;
        }
    }
    
    /**
     * 获取本地缓存文件内容。NULL表示没有本地文件或抛出异常。
     */
    static public String getSnapshot(DiamondEnv env, String dataId, String group) {
        File file = getSnapshotFile(env, dataId, group);
        if (!file.exists() || !file.isFile()) {
            return null;
        }

        try {
            return readFile(file);
        } catch (IOException ioe) {
            log.error("get snapshot error, " + file + ", " + ioe.toString());
            return null;
        }
    }
    
    static private String readFile(File file) throws IOException {
        if (!file.exists() || !file.isFile()) {
            return null;
        }

        InputStream is = null;
        try {
            is = new FileInputStream(file);
            return IOUtils.toString(is, Constants.ENCODE);
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
            } catch (IOException ioe) {
            }
        }
    }

    /**
     * 保存snapshot。如果内容为NULL，则删除snapshot。
     */
    static public void saveSnapshot(DiamondEnv env, String dataId, String group, String config) {
        saveSnapshot(env.serverMgr.name, dataId, group, config);
    }
    
    static public void saveSnapshot(String envName, String dataId, String group, String config) {
        File file = getSnapshotFile(envName, dataId, group);
        if (null == config) {
            try {
                IOUtils.delete(file);
            } catch (IOException ioe) {
                log.error("delete snapshot error, " + file + ", " + ioe.toString());
            }
        } else {
            try {
                file.getParentFile().mkdirs();
//                file.createNewFile();//这里不需要创建
                IOUtils.writeStringToFile(file, config, Constants.ENCODE);
            } catch (IOException ioe) {
                log.error("save snapshot error, " + file + ", " + ioe.toString());
            }
        }
    }
    
    /**
     * 清除snapshot目录下所有缓存文件。
     */
    static public void cleanAllSnapshot() {
        try {
        	File rootFile = new File(localFileRootPath);
        	File[] files = rootFile.listFiles();
        	for(File file : files){
        		if(file.getName().endsWith("_diamond")){
        			IOUtils.cleanDirectory(file);
        		}
        	}
        } catch (IOException ioe) {
            log.error("clean all snapshot error, " + ioe.toString(), ioe);
        }
    }
    
    static public void cleanEnvSnapshot(String envName){
    	File tmp = new File(localFileRootPath, envName + "_diamond");
    	tmp = new File(tmp, "snapshot");
    	try {
			IOUtils.cleanDirectory(tmp);
			log.info("success dlelet " + envName + "-snapshot");
		} catch (IOException e) {
			log.info("fail dlelet " + envName + "-snapshot, " + e.toString());
			e.printStackTrace();
		}
    }
    
    
    
    static public void saveServerlist(DiamondEnv env, List<String> serverUrls) {
        saveServerlist(env.serverMgr.name, serverUrls);
    }
    
    static public void saveServerlist(String envName, List<String> serverUrls) {
        StringBuilder sb = new StringBuilder();
        for (String ip : serverUrls) {
            sb.append(ip).append("\r\n");
        }
        saveSnapshot(envName, DATAID_SERVER_LIST, "DEFAULT_GROUP", sb.toString());
    }
    
    static public List<String> readServerlist(DiamondEnv env) {
        String content = getSnapshot(env, DATAID_SERVER_LIST, "DEFAULT_GROUP");
        if (null == content) {
            return null;
        }

        List<String> serverlist = new ArrayList<String>();
        try {
            BufferedReader is = new BufferedReader(new StringReader(content));
            String line = null;
            for (;;) {
                line = is.readLine();
                if (null == line) {
                    break;
                } else if (!"".equals(line.trim())) {
                    serverlist.add(line);
                }
            }
            return serverlist;
        } catch (IOException ioe) {
            log.error("get serverlist snapshot error, " + ioe.toString());
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        //LocalConfigInfoProcessor.readServerlist(DiamondEnvRepo.defaultEnv);

        Diamond.getConfig("test","test",2000l);
    }

    static File getFailoverFile(DiamondEnv env, String dataId, String group) {
    	File tmp = new File(localFileRootPath, env.serverMgr.name + "_diamond");
    	tmp = new File(tmp, "data");
        tmp = new File(tmp, "config-data");
        return new File(new File(tmp, group), dataId);
    }
    
    static File getSnapshotFile(DiamondEnv env, String dataId, String group) {
    	return getSnapshotFile(env.serverMgr.name, dataId, group);
    }
    
    static File getSnapshotFile(String envName, String dataId, String group) {
    	File tmp = new File(localFileRootPath, envName + "_diamond");
    	tmp = new File(tmp, "snapshot");
        return new File(new File(tmp, group), dataId);
    }
    
    
    
//    static public boolean hasLocalFile(String dataId, String group) {
//        File file = getFilePath(dataId, group);
//        return file.exists() && file.isFile();
//    }
    
    // =================

    public static String getLogFile(){
    	return new File(logRoot, "diamond-client.log").getAbsolutePath();
    	
    }
    static public final String DATAID_SERVER_LIST = "com.taobao.diamond.serverlist";
    static public final LocalConfigInfoProcessor singleton = new LocalConfigInfoProcessor();

//    static final File failoverRoot;
//    static final File snapshotRoot;
    public static final String localFileRootPath;
    static final File logRoot;
    static {
    	localFileRootPath = System.getProperty("JM.LOG.PATH", System.getProperty("user.home")) + File.separator + "diamond";
    	
//        File tmp = new File(localFileRootPath, "diamond");
//        tmp = new File(tmp, "data");
//        tmp = new File(tmp, "config-data");
//        tmp.mkdirs();
//        if (!tmp.exists()) {
//            throw new RuntimeException("create failover directory error: " + tmp);
//        }
//        failoverRoot = tmp;
//
//        tmp = new File(localFileRootPath, "diamond");
//        tmp = new File(tmp, "snapshot");
//        tmp.mkdirs();
//        if (!tmp.exists()) {
//            throw new RuntimeException("create snapshot directory error: " + tmp);
//        }
//        snapshotRoot = tmp;
        
        File tmp = new File(localFileRootPath, "logs");
        tmp.mkdirs();
        if(!tmp.exists()){
        	throw new RuntimeException("create log directory error: " + tmp);
        }
        logRoot = tmp;
    }

}
