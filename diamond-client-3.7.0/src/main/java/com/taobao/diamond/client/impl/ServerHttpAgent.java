package com.taobao.diamond.client.impl;

import com.taobao.diamond.client.impl.HttpSimpleClient.HttpResult;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.List;

/**
 * 按照优先级顺序给server发请求，直到某个server请求成功。
 * 
 * 粘性，如果请求某个server成功，那么下个请求也会首先发给该server。
 */
class ServerHttpAgent {
    public static final String appKey;
    public static final String appName;

    static {
        // 客户端身份信息
        appKey = System.getProperty("diamond.client.appKey", "");
        appName = System.getProperty("diamond.client.appName", "");
    }

    ServerHttpAgent(ServerListManager mgr) {
        serverListMgr = mgr;
    }

    /**
     * @param path
     *            相对于web应用根，以/开头
     * @param headers
     * @param paramValues
     * @param encoding
     * @param readTimeoutMs
     * @return
     * @throws IOException
     */
    public HttpResult httpGet(String path, List<String> headers, List<String> paramValues,
            String encoding, long readTimeoutMs) throws IOException {
        final long endTime = System.currentTimeMillis() + readTimeoutMs;

        if (null != currentServerIp) {
            try {
                HttpResult result = HttpSimpleClient.httpGet(getUrl(currentServerIp, path),
                        headers, paramValues, encoding, readTimeoutMs);
                return result;
            } catch (ConnectException ce) {
            } catch (SocketTimeoutException stoe) {
            } catch (IOException ioe) {
                throw ioe;
            }
        }

        for (Iterator<String> serverIter = serverListMgr.iterator(); serverIter.hasNext();) {
            long timeout = endTime - System.currentTimeMillis();
            if (timeout <= 0) {
                currentServerIp = serverIter.next(); // previous node performs slowly
                throw new IOException("timeout");
            }

            String ip = serverIter.next();
            try {
                HttpResult result = HttpSimpleClient.httpGet(getUrl(ip, path), headers,
                        paramValues, encoding, timeout);
                currentServerIp = ip;
                return result;
            } catch (ConnectException ce) {
            } catch (SocketTimeoutException stoe) {
            } catch (IOException ioe) {
                throw ioe;
            }
        }
        throw new ConnectException("no available server");
    }

    public HttpResult httpPost(String path, List<String> headers, List<String> paramValues,
            String encoding, long readTimeoutMs) throws IOException {
        final long endTime = System.currentTimeMillis() + readTimeoutMs;

        if (null != currentServerIp) {
            try {
                HttpResult result = HttpSimpleClient.httpPost(getUrl(currentServerIp, path),
                        headers, paramValues, encoding, readTimeoutMs);
                return result;
            } catch (ConnectException ce) {
            } catch (SocketTimeoutException stoe) {
            } catch (IOException ioe) {
                throw ioe;
            }
        }

        for (Iterator<String> serverIter = serverListMgr.iterator(); serverIter.hasNext();) {
            long timeout = endTime - System.currentTimeMillis();
            if (timeout <= 0) {
                currentServerIp = serverIter.next(); // previous node performs slowly
                throw new IOException("timeout");
            }

            String ip = serverIter.next();
            try {
                HttpResult result = HttpSimpleClient.httpPost(getUrl(ip, path), headers,
                        paramValues, encoding, timeout);
                currentServerIp = ip;
                return result;
            } catch (ConnectException ce) {
            } catch (SocketTimeoutException stoe) {
            } catch (IOException ioe) {
                throw ioe;
            }
        }
        throw new ConnectException("no available server");
    }

    // relativePath相对于web应用根路径，以/开头
    static String getUrl(String ip, String relativePath) {
        return "http://" + ip + ":8080/diamond-server" + relativePath;
    }
    
    /**
     * 集群服务器列表发送变化，重置currentServerIp
     */
    public void reSetCurrentServerIp(){
    	if(currentServerIp!=null)
    		currentServerIp = null;
    }

    // =================
    final ServerListManager serverListMgr;
    volatile String currentServerIp;
}
