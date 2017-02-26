package com.taobao.diamond.client.impl;

import static com.taobao.diamond.client.impl.DiamondEnv.log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.taobao.diamond.common.Constants;
import com.taobao.diamond.manager.ManagerListener;
import com.taobao.diamond.md5.MD5;

public class CacheData {
    
    public String getMd5() {
        return md5;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String newContent) {
        this.content = newContent;
        this.md5 = getMd5String(content);
    }
    
    /**
     * ��Ӽ�����
     */
    public void addListener(ManagerListener listener) {
        if (null == listener) {
            throw new IllegalArgumentException("listener is null");
        }
        ManagerListenerWrap wrap = new ManagerListenerWrap(listener);
        if (listeners.addIfAbsent(wrap)) {
            log.info("[add-listener] ok, " + dataId + ", " + group + ", count=" + listeners.size());
        }
    }
    
    public void removeListener(ManagerListener listener) {
        if (null == listener) {
            throw new IllegalArgumentException("listener is null");
        }
        ManagerListenerWrap wrap = new ManagerListenerWrap(listener);
        if (listeners.remove(wrap)) {
            log.info("[remove-listener] ok, " + dataId + ", " + group + ", count="
                    + listeners.size());
        }
    }
    
    /**
     * ���ؼ������б��ϵĵ�������ֻ������֤������NULL��
     */
    public List<ManagerListener> getListeners() {
        List<ManagerListener> result = new ArrayList<ManagerListener>();
        for (ManagerListenerWrap wrap : listeners) {
            result.add(wrap.listener);
        }
        return result;
    }

    
    public long getLocalConfigInfoVersion() {
        return localConfigLastModified;
    }
    public void setLocalConfigInfoVersion(long localConfigLastModified) {
        this.localConfigLastModified = localConfigLastModified;
    }


    public boolean isUseLocalConfigInfo() {
        return isUseLocalConfig;
    }
    public void setUseLocalConfigInfo(boolean useLocalConfigInfo) {
        this.isUseLocalConfig = useLocalConfigInfo;
        if (!useLocalConfigInfo) {
            localConfigLastModified = -1;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataId == null) ? 0 : dataId.hashCode());
        result = prime * result + ((group == null) ? 0 : group.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj || obj.getClass() != getClass()) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        CacheData other = (CacheData) obj;
        return dataId.equals(other.dataId) && group.equals(other.group);
    }

    @Override
    public String toString() {
        return "CacheData [" + dataId + ", " + group + "]";
    }
    
    
    void checkListenerMd5() {
        for (ManagerListenerWrap wrap : listeners) {
            if (!md5.equals(wrap.lastCallMd5)) {
                safeNotifyListener(dataId, group, content, md5, wrap);
            }
        }
    }
    
    static void safeNotifyListener(final String dataId, final String group, final String content,
            final String md5, ManagerListenerWrap listenerWrap) {
        final ManagerListener listener = listenerWrap.listener;
        listenerWrap.lastCallMd5 = md5;

        Runnable job = new Runnable() {
            public void run() {
                try {
                    listener.receiveConfigInfo(content);
                    log.info("[notify-ok] " + dataId + ", " + group + ", md5=" + md5
                            + ", listener=" + listener);
                } catch (Throwable t) {
                    log.error("[notify-error] " + dataId + ", " + group + ", md5=" + md5
                            + ", listener=" + listener.toString(), t);
                }
            }
        };

        try {
            if (null != listener.getExecutor()) {
                listener.getExecutor().execute(job);
            } else {
                job.run();
            }
        } catch (Throwable t) {
            log.error("[notify-error] " + dataId + ", " + group + ", md5=" + md5 + ", listener="
                    + listener.toString(), t);
        }
    }
    
    static public String getMd5String(String config) {
        return (null == config) ? Constants.NULL : MD5.getInstance().getMD5String(config);
    }

    public CacheData(String dataId, String group) {
        if (null == dataId || null == group) {
            throw new IllegalArgumentException("dataId=" + dataId + ", group=" + group);
        }

        this.dataId = dataId;
        this.group = group;
        listeners = new CopyOnWriteArrayList<ManagerListenerWrap>();
        this.content = null;
        this.md5 = getMd5String(content);
    }
    
    // ==================
    
    public final String dataId;
    public final String group;
    private final CopyOnWriteArrayList<ManagerListenerWrap> listeners;
    
    private volatile String md5;
    private volatile boolean isUseLocalConfig = false; // �Ƿ�ʹ�ñ�����������
    private volatile long localConfigLastModified; // ����޸�ʱ��
    private volatile String content;
}

class ManagerListenerWrap {
    final ManagerListener listener;
    String lastCallMd5 = CacheData.getMd5String(null);

    ManagerListenerWrap(ManagerListener _listener) {
        listener = _listener;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj || obj.getClass() != getClass()) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        ManagerListenerWrap other = (ManagerListenerWrap) obj;
        return listener.equals(other.listener);
    }
}
