package com.infoepoch.cmgs.config;


import com.alibaba.fastjson.JSON;
import com.infoepoch.cmgs.cache.CommonCache;
import com.infoepoch.cmgs.constants.BrokerConstants;
import com.infoepoch.cmgs.model.ConsumerQueueOffsetModel;
import com.infoepoch.cmgs.utils.FileUtil;
import io.netty.util.internal.StringUtil;

import java.util.concurrent.TimeUnit;

public class ConsumerQueueOffsetLoader {

    private String filePath;

    public void load() {
        GlobalProperties globalProperties = CommonCache.getGlobalProperties();
        String basePath = globalProperties.getEagleMqHome();
        if (StringUtil.isNullOrEmpty(basePath)) {
            throw new IllegalArgumentException("eagle mq home is invalid");
        }
        filePath = basePath + "/broker/config/consumerqueue-offset.json";
        String content = FileUtil.read(filePath);
        ConsumerQueueOffsetModel consumerQueueModel = JSON.parseObject(content, ConsumerQueueOffsetModel.class);
        CommonCache.setConsumerQueueModel(consumerQueueModel);
    }

    /**
     * 开启一个线程任务
     */
    public void consumerQueueOffsetTask() {
        // 异步线程
        CommonThreadPoolConfig.refreshConsumerQueueOffsetExecutor.execute(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(BrokerConstants.REFRESH_CONSUMER_QUEUE_OFFSET_TIME_STAMP);
                    System.out.println("========>consumer queue offset 配置信息刷新到磁盘");
                    ConsumerQueueOffsetModel consumerQueueOffsetModel = CommonCache.getConsumerQueueModel();
                    FileUtil.write(filePath, JSON.toJSONString(consumerQueueOffsetModel));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
