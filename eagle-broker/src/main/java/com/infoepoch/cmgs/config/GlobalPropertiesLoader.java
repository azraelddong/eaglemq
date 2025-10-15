package com.infoepoch.cmgs.config;


import com.infoepoch.cmgs.cache.CommonCache;
import com.infoepoch.cmgs.constants.BrokerConstants;
import io.netty.util.internal.StringUtil;

public class GlobalPropertiesLoader {


    public void load() {
        GlobalProperties globalProperties = new GlobalProperties();
        String mqHome = System.getProperty(BrokerConstants.EAGLE_MQ_HOME);
        if (StringUtil.isNullOrEmpty(mqHome)) {
            throw new IllegalArgumentException("EAGLE_MQ_HOME is not set");
        }
        globalProperties.setEagleMqHome(mqHome);
        CommonCache.setGlobalProperties(globalProperties);
    }
}
