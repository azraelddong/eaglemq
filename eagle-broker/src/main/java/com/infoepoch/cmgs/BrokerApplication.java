package com.infoepoch.cmgs;

import com.infoepoch.cmgs.cache.CommonCache;
import com.infoepoch.cmgs.config.GlobalPropertiesLoader;
import com.infoepoch.cmgs.config.EagleMqTopicLoader;
import com.infoepoch.cmgs.core.CommitLogAppendHandler;
import com.infoepoch.cmgs.model.EagleMqTopicModel;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class BrokerApplication {

    private static GlobalPropertiesLoader globalPropertiesLoader;

    private static EagleMqTopicLoader eagleMqTopicLoader;

    private static CommitLogAppendHandler commitLogAppendHandler;

    private static void init() throws IOException {
        globalPropertiesLoader = new GlobalPropertiesLoader();
        globalPropertiesLoader.load();

        eagleMqTopicLoader = new EagleMqTopicLoader();
        eagleMqTopicLoader.load();

        // 开启一个异步线程同步主配置json文件
        eagleMqTopicLoader.eagleMqTopicInfoTask();

        commitLogAppendHandler = new CommitLogAppendHandler();

        for (EagleMqTopicModel topicModel : CommonCache.getTopicModelMap().values()) {
            commitLogAppendHandler.prepareLoading(topicModel.getTopic());
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        // 加载配置，缓存对象
        init();

        // 初始化文件映射
        String topic = "order_cancel_topic";
        for (int i = 0; i < 10; i++) {
            System.out.println("===========> 写入数据");
            commitLogAppendHandler.append(topic, "this is a test".getBytes());
            TimeUnit.SECONDS.sleep(5);
        }
        commitLogAppendHandler.read(topic);
    }
}
