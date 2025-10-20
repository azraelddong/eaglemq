package com.infoepoch.cmgs;

import com.infoepoch.cmgs.cache.CommonCache;
import com.infoepoch.cmgs.config.ConsumerQueueOffsetLoader;
import com.infoepoch.cmgs.config.EagleMqTopicLoader;
import com.infoepoch.cmgs.config.GlobalPropertiesLoader;
import com.infoepoch.cmgs.core.CommitLogAppendHandler;
import com.infoepoch.cmgs.core.ConsumerQueueCommitLogAppendHandler;
import com.infoepoch.cmgs.core.ConsumerQueueConsumeHandler;
import com.infoepoch.cmgs.model.EagleMqTopicModel;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class BrokerApplication {

    private static GlobalPropertiesLoader globalPropertiesLoader;

    private static EagleMqTopicLoader eagleMqTopicLoader;

    private static ConsumerQueueOffsetLoader consumerQueueOffsetLoader;

    private static CommitLogAppendHandler commitLogAppendHandler;

    private static ConsumerQueueCommitLogAppendHandler consumerQueueCommitLogAppendHandler;

    private static ConsumerQueueConsumeHandler consumerQueueConsumeHandler;

    private static void init() throws IOException {
        globalPropertiesLoader = new GlobalPropertiesLoader();
        globalPropertiesLoader.load();

        // topic配置文件信息加载
        eagleMqTopicLoader = new EagleMqTopicLoader();
        eagleMqTopicLoader.load();
        // 开启一个异步线程同步主配置json文件
        eagleMqTopicLoader.eagleMqTopicInfoTask();

        // consumerQueueOffset配置文件加载
        consumerQueueOffsetLoader = new ConsumerQueueOffsetLoader();
        consumerQueueOffsetLoader.load();
        consumerQueueOffsetLoader.consumerQueueOffsetTask();


        commitLogAppendHandler = new CommitLogAppendHandler();

        consumerQueueCommitLogAppendHandler = new ConsumerQueueCommitLogAppendHandler();

        consumerQueueConsumeHandler = new ConsumerQueueConsumeHandler();

        for (EagleMqTopicModel topicModel : CommonCache.getTopicModelMap().values()) {
            commitLogAppendHandler.prepareLoading(topicModel.getTopic());
            consumerQueueCommitLogAppendHandler.prepareLoading(topicModel.getTopic());
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        // 加载配置，缓存对象
        init();
        // 初始化文件映射
        String topic = "order_cancel_topic";
        String group = "order_service_group";
        for (int i = 0; i < 10; i++) {
            byte[] consume = consumerQueueConsumeHandler.consume(topic, group, 0);
            System.out.println("内容：" + new String(consume));
            consumerQueueConsumeHandler.ack(topic, group, 0);
//            System.out.println("===========> 写入数据");
//            commitLogAppendHandler.append(topic, ("this is a test" + i).getBytes());
            TimeUnit.SECONDS.sleep(1);
        }
//        commitLogAppendHandler.read(topic);
    }
}
