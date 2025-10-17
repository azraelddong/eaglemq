package com.infoepoch.cmgs.cache;

import com.infoepoch.cmgs.config.GlobalProperties;
import com.infoepoch.cmgs.core.ConsumerQueueOffsetMMapFileModelManager;
import com.infoepoch.cmgs.model.ConsumerQueueOffsetModel;
import com.infoepoch.cmgs.model.EagleMqTopicModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通用模型
 */
public class CommonCache {

    /**
     * 全局配置
     */
    public static GlobalProperties globalProperties = new GlobalProperties();

    /**
     * 主题模型
     */
    public static List<EagleMqTopicModel> topicModelList = new ArrayList<>();

    /**
     * 消费者队列模型
     */
    public static ConsumerQueueOffsetModel consumerQueueModel = new ConsumerQueueOffsetModel();

    /**
     * 消费者队列管理器
     */
    public static ConsumerQueueOffsetMMapFileModelManager consumerQueueOffsetMMapFileModelManager = new ConsumerQueueOffsetMMapFileModelManager();

    public static ConsumerQueueOffsetMMapFileModelManager getConsumerQueueOffsetMMapFileModelManager() {
        return consumerQueueOffsetMMapFileModelManager;
    }

    public static void setConsumerQueueOffsetMMapFileModelManager(ConsumerQueueOffsetMMapFileModelManager consumerQueueOffsetMMapFileModelManager) {
        CommonCache.consumerQueueOffsetMMapFileModelManager = consumerQueueOffsetMMapFileModelManager;
    }

    public static ConsumerQueueOffsetModel getConsumerQueueModel() {
        return consumerQueueModel;
    }

    public static void setConsumerQueueModel(ConsumerQueueOffsetModel consumerQueueModel) {
        CommonCache.consumerQueueModel = consumerQueueModel;
    }

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
