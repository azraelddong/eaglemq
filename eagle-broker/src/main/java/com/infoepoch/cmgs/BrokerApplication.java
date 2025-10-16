package com.infoepoch.cmgs;

import com.infoepoch.cmgs.cache.CommonCache;
import com.infoepoch.cmgs.config.GlobalPropertiesLoader;
import com.infoepoch.cmgs.config.EagleMqTopicLoader;
import com.infoepoch.cmgs.core.CommitLogAppendHandler;
import com.infoepoch.cmgs.model.EagleMqTopicModel;

import java.io.IOException;

public class BrokerApplication {

    private static GlobalPropertiesLoader globalPropertiesLoader;

    private static EagleMqTopicLoader eagleMqTopicLoader;

    private static CommitLogAppendHandler commitLogAppendHandler;

    private static void init() throws IOException {
        globalPropertiesLoader = new GlobalPropertiesLoader();
        globalPropertiesLoader.load();

        eagleMqTopicLoader = new EagleMqTopicLoader();
        eagleMqTopicLoader.load();

        commitLogAppendHandler = new CommitLogAppendHandler();

        for (EagleMqTopicModel topicModel : CommonCache.getTopicModelMap().values()) {
            commitLogAppendHandler.prepareLoading(topicModel.getTopic());
        }
    }

    public static void main(String[] args) throws IOException {
        // 加载配置，缓存对象
        init();

        // 初始化文件映射
        String topic = "order_cancel_topic";
        commitLogAppendHandler.append(topic, "this is a test".getBytes());
        commitLogAppendHandler.read(topic);
    }
}
