package com.infoepoch.cmgs.config;


import com.infoepoch.cmgs.cache.CommonCache;
import io.netty.util.internal.StringUtil;

public class GlobalPropertiesLoader {


    public void load() {
        GlobalProperties globalProperties = new GlobalProperties();
//        String mqHome = System.getenv(BrokerConstants.EAGLE_MQ_HOME);
//        String mqHome = "/Users/apple/Documents/study/eaglemq";
        String mqHome = "E:\\study\\eaglemq";
        if (StringUtil.isNullOrEmpty(mqHome)) {
            throw new IllegalArgumentException("EAGLE_MQ_HOME is not set");
        }
        globalProperties.setEagleMqHome(mqHome);
        CommonCache.setGlobalProperties(globalProperties);
    }
}
