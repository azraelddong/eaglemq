package com.infoepoch.cmgs.config;

import com.infoepoch.cmgs.cache.CommonCache;
import io.netty.util.internal.StringUtil;

public class TopicInfoLoader {

    private TopicInfo topicInfo;

    public void load() {
        GlobalProperties globalProperties = CommonCache.getGlobalProperties();
        String basePath = globalProperties.getEagleMqHome();
        if (StringUtil.isNullOrEmpty(basePath)) {
            throw new IllegalArgumentException("eagle mq home is invalide");
        }
        String topicJsonFilePath = basePath + "/broker/config/eaglemq-topic.json";
        this.topicInfo = new TopicInfo();
    }
}
