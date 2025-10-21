package com.infoepoch.cmgs.config;


import com.infoepoch.cmgs.cache.CommonCache;
import com.infoepoch.cmgs.constants.BrokerConstants;
import io.netty.util.internal.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GlobalPropertiesLoader {


    public void load() throws IOException {
        GlobalProperties globalProperties = new GlobalProperties();
//        String mqHome = System.getenv(BrokerConstants.EAGLE_MQ_HOME);
        // todo: 后期通过环境变量获取
        String mqHome = "/Users/apple/Documents/study/eaglemq";
//        String mqHome = "E:\\study\\eaglemq";
        if (StringUtil.isNullOrEmpty(mqHome)) {
            throw new IllegalArgumentException("EAGLE_MQ_HOME is not set");
        }

        // 读取broker.properties配置文件
        Properties properties = new Properties();
        properties.load(new FileInputStream(new File(mqHome + BrokerConstants.BROKER_PROPERTIES_HOME)));

        globalProperties.setNameServerIp(properties.getProperty("eaglemq.ip"));
        globalProperties.setNameServerPort(Integer.valueOf(properties.getProperty("eaglemq.port")));
        globalProperties.setNameServerUserName(properties.getProperty("eaglemq.username"));
        globalProperties.setNameServerPassword(properties.getProperty("eaglemq.password"));
        globalProperties.setEagleMqHome(mqHome);
        CommonCache.setGlobalProperties(globalProperties);
    }

}
