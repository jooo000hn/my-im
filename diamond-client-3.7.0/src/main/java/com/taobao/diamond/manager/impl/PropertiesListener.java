package com.taobao.diamond.manager.impl;

import static com.taobao.diamond.client.impl.DiamondEnv.log;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.taobao.diamond.manager.ManagerListenerAdapter;



public abstract class PropertiesListener extends ManagerListenerAdapter {

    public void receiveConfigInfo(String configInfo) {
        if (StringUtils.isEmpty(configInfo)) {
            return;
        }

        Properties properties = new Properties();
        try {
            properties.load(new StringReader(configInfo));
            innerReceive(properties);
        }
        catch (IOException e) {
            log.error("load properties error£º" + configInfo, e);
        }

    }


    public abstract void innerReceive(Properties properties);

}
