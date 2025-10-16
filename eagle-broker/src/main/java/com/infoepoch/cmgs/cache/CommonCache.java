package com.infoepoch.cmgs.cache;

import com.infoepoch.cmgs.config.GlobalProperties;
import com.infoepoch.cmgs.model.EagleMqTopicModel;

import java.util.HashMap;
import java.util.Map;

public class CommonCache {

    public static GlobalProperties globalProperties = new GlobalProperties();

    public static Map<String, EagleMqTopicModel> topicModelMap = new HashMap<>();

    public static GlobalProperties getGlobalProperties() {
        return globalProperties;
    }

    public static void setGlobalProperties(GlobalProperties globalProperties) {
        CommonCache.globalProperties = globalProperties;
    }

    public static Map<String, EagleMqTopicModel> getTopicModelMap() {
        return topicModelMap;
    }

    public static void setTopicModelMap(Map<String, EagleMqTopicModel> topicModelMap) {
        CommonCache.topicModelMap = topicModelMap;
    }
}
