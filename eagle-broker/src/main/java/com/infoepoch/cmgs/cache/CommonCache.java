package com.infoepoch.cmgs.cache;

import com.infoepoch.cmgs.config.GlobalProperties;
import com.infoepoch.cmgs.model.EagleMqTopicModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommonCache {

    public static GlobalProperties globalProperties = new GlobalProperties();

    public static List<EagleMqTopicModel> topicModelList = new ArrayList<>();

    public static GlobalProperties getGlobalProperties() {
        return globalProperties;
    }

    public static void setGlobalProperties(GlobalProperties globalProperties) {
        CommonCache.globalProperties = globalProperties;
    }

    public static Map<String, EagleMqTopicModel> getTopicModelMap() {
        return topicModelList.stream().collect(Collectors.toMap(EagleMqTopicModel::getTopic, s -> s));
    }

    public static List<EagleMqTopicModel> getTopicModelList() {
        return topicModelList;
    }

    public static void setTopicModelList(List<EagleMqTopicModel> topicModels) {
        topicModelList = topicModels;
    }
}
