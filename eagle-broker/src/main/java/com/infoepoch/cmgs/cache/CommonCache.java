package com.infoepoch.cmgs.cache;

import com.infoepoch.cmgs.config.GlobalProperties;
import com.infoepoch.cmgs.config.TopicInfo;

public class CommonCache {

    public static GlobalProperties  globalProperties = new GlobalProperties();

    public static TopicInfo topicInfo = new TopicInfo();

    public static GlobalProperties getGlobalProperties() {
        return globalProperties;
    }

    public static void setGlobalProperties(GlobalProperties globalProperties) {
        CommonCache.globalProperties = globalProperties;
    }

    public static TopicInfo getTopicInfo() {
        return topicInfo;
    }

    public static void setTopicInfo(TopicInfo topicInfo) {
        CommonCache.topicInfo = topicInfo;
    }
}
